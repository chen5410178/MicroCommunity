<template>
  <el-dialog :title="$t('importRoomFee.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" class="text-left" label-position="right">
      <el-form-item :label="$t('importRoomFee.form.feeTypeCd')" prop="feeTypeCd" :rules="[
        { required: true, message: $t('importRoomFee.rules.feeTypeCdRequired'), trigger: 'change' }
      ]">
        <el-select v-model="form.feeTypeCd" :placeholder="$t('importRoomFee.placeholder.feeTypeCd')" style="width:100%"
          @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeCds" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('importRoomFee.form.objType')" prop="objType" :rules="[
        { required: true, message: $t('importRoomFee.rules.objTypeRequired'), trigger: 'change' }
      ]">
        <el-select v-model="form.objType" :placeholder="$t('importRoomFee.placeholder.objType')" style="width:100%">
          <el-option :label="$t('importRoomFee.options.room')" value="3333" />
          <el-option :label="$t('importRoomFee.options.parking')" value="6666" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('importRoomFee.form.file')" prop="file" :rules="[
        { required: true, message: $t('importRoomFee.rules.fileRequired'), trigger: 'change' }
      ]">
        <el-upload ref="upload" :auto-upload="false" :limit="1" action="" :on-change="handleFileChange"
          :on-remove="handleFileRemove" accept=".xls,.xlsx">
          <el-button size="small" type="primary">
            {{ $t('importRoomFee.button.selectFile') }}
          </el-button>
          <div slot="tip" class="el-upload__tip">
            {{ $t('importRoomFee.tips.fileFormat') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('importRoomFee.form.template')">
        <span>
          {{ $t('importRoomFee.tips.downloadTemplate1') }}
          <el-link type="primary" @click="handleDownloadTemplate">
            {{ $t('importRoomFee.tips.downloadTemplate2') }}
          </el-link>
          {{ $t('importRoomFee.tips.downloadTemplate3') }}
        </span>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importRoomFeeData, downloadImportTemplate } from '@/api/fee/roomFeeImportApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ImportRoomFee',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        feeTypeCd: '',
        objType: '3333',
        file: null
      },
      feeTypeCds: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getDictData()
    },
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleFeeTypeChange(value) {
      this.form.feeTypeCd = value
    },
    handleFileChange(file) {
      this.form.file = file.raw
    },
    handleFileRemove() {
      this.form.file = null
    },
    async handleSubmit() {
      try {
        this.$refs.form.validate(async valid => {
          if (!valid) return
          if (!this.form.file) {
            this.$message.warning(this.$t('importRoomFee.rules.fileRequired'))
            return
          }

          this.loading = true
          const formData = new FormData()
          formData.append('uploadFile', this.form.file)
          formData.append('communityId', getCommunityId())
          formData.append('feeTypeCd', this.form.feeTypeCd)
          formData.append('objType', this.form.objType)
          formData.append('importAdapt', 'importRoomFee')

          const res = await importRoomFeeData(formData).catch(error => {
            return {code: 500, msg: error}
          })
          if (res.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
            this.$router.push({
              path: '/views/system/assetImportLogDetail',
              query: {
                logId: res.data.logId,
                logType: 'importRoomFee'
              }
            })
          } else {
            this.$message.error(res.msg)
          }
        })
      } catch (error) {
        console.log('error:', error)
        this.$message.error(error)
      } finally {
        this.loading = false
      }
    },
    async handleDownloadTemplate() {
      try {
        const params = {
          communityId: getCommunityId(),
          objType: this.form.objType,
          pagePath: 'exportFeeImportTemplate'
        }
       const res = await downloadImportTemplate(params)
        if (res.code === 0) {
          this.visible = false
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('下载模板失败:', error)
        this.$message.error(this.$t('importRoomFee.message.downloadFailed'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.$refs.upload.clearFiles()
      this.form.file = null
    }
  }
}
</script>

<style scoped>
.el-upload__tip {
  margin-top: 7px;
  color: #606266;
  font-size: 12px;
}
</style>