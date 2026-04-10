<template>
  <el-dialog :title="$t('meterWater.meterReadingImport')" :visible.sync="dialogVisible" width="40%"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right" class="text-left">
      <el-form-item :label="$t('meterWater.feeType')" prop="feeTypeCd">
        <el-select v-model="form.feeTypeCd" :placeholder="$t('meterWater.selectFeeType')" style="width: 100%"
          @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.feeItem')" prop="configId">
        <el-select v-model="form.configId" :placeholder="$t('meterWater.selectFeeItem')" style="width: 100%">
          <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.meterType')" prop="meterType">
        <el-select v-model="form.meterType" :placeholder="$t('meterWater.selectMeterType')" style="width: 100%">
          <el-option v-for="item in meterTypes" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.selectFile')" prop="file">
        <el-upload ref="upload" :auto-upload="false" :limit="1" :on-change="handleFileChange" :file-list="fileList"
          accept=".xls,.xlsx" action="">
          <el-button size="small" type="primary">{{ $t('meterWater.selectFile') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileList.length > 0 ? fileList[0].name : $t('meterWater.fileRequired') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('meterWater.downloadTemplate')">
        <div>
          {{ $t('meterWater.downloadTip') }}
          <el-link type="primary" @click="handleDownloadTemplate">
            {{ $t('meterWater.importTemplate') }}
          </el-link>
          {{ $t('meterWater.prepareData') }}
        </div>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { importMeterWaterData, exportMeterWaterTemplate } from '@/api/fee/meterWaterManageApi'
import { listFeeConfigs, listMeterTypes } from '@/api/fee/meterWaterManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ImportMeterWaterFee',
  data() {
    return {
      dialogVisible: false,
      form: {
        feeTypeCd: '',
        configId: '',
        meterType: '',
        file: null,
        communityId: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('meterWater.feeTypeRequired'), trigger: 'blur' }
        ],
        configId: [
          { required: true, message: this.$t('meterWater.feeItemRequired'), trigger: 'blur' }
        ],
        meterType: [
          { required: true, message: this.$t('meterWater.meterTypeRequired'), trigger: 'blur' }
        ],
        file: [
          { required: true, message: this.$t('meterWater.fileRequired'), trigger: 'blur' }
        ]
      },
      feeTypeOptions: [],
      feeConfigs: [],
      meterTypes: [],
      fileList: []
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.loadFeeTypes()
    this.loadMeterTypes()
  },
  methods: {
    open() {
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.fileList = []
    },
    async loadFeeTypes() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeOptions = data.map(item => ({
          value: item.statusCd,
          label: item.name
        }))
      } catch (error) {
        console.error('Failed to load fee types:', error)
      }
    },
    async loadMeterTypes() {
      try {
        const { data } = await listMeterTypes({
          communityId: this.form.communityId,
          page: 1,
          row: 50
        })
        this.meterTypes = data
      } catch (error) {
        console.error('Failed to load meter types:', error)
      }
    },
    async handleFeeTypeChange(feeTypeCd) {
      try {
        const { data } = await listFeeConfigs({
          communityId: this.form.communityId,
          feeTypeCd,
          isDefault: 'F',
          valid: '1',
          page: 1,
          row: 20
        })
        this.feeConfigs = data
        this.form.configId = ''
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    handleFileChange(file) {
      this.form.file = file.raw
      this.fileList = [file]
    },
    async handleDownloadTemplate() {
      if (!this.form.meterType) {
        this.$message.warning(this.$t('meterWater.selectMeterTypeFirst'))
        return
      }

      try {
        await exportMeterWaterTemplate({
          communityId: this.form.communityId,
          meterType: this.form.meterType,
          pagePath:'exportMeterWater'
        })
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')

      } catch (error) {
        console.error('Failed to download template:', error)
        this.$message.error(error.message || this.$t('common.downloadFailed'))
      }
    },
    async handleImport() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        if (!this.form.file) {
          this.$message.warning(this.$t('meterWater.fileRequired'))
          return
        }

        const formData = new FormData()
        formData.append('uploadFile', this.form.file)
        formData.append('communityId', this.form.communityId)
        formData.append('feeTypeCd', this.form.feeTypeCd)
        formData.append('configId', this.form.configId)
        formData.append('meterType', this.form.meterType)
        formData.append('importAdapt', 'importMeterWaterFee')

        try {
          const res = await importMeterWaterData(formData)
          if(!res.code){
            this.$message.error(res)
            return ;
          }
          this.$message.success(this.$t('common.operationSuccess'))
          this.dialogVisible = false
          this.$emit('success', res.logId)
        } catch (error) {
          this.$message.error(error)
        }
      })
    }
  }
}
</script>