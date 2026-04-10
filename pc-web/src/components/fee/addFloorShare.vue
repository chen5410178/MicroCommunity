<template>
  <el-dialog :title="$t('floorShare.addTitle')" :visible.sync="dialogVisible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="text-left" label-position="right">
      <el-form-item :label="$t('floorShare.floor')" prop="floorNum">
        <el-input v-model="form.floorNum" disabled />
      </el-form-item>

      <el-form-item :label="$t('floorShare.meterType')" prop="meterType">
        <el-select v-model="form.meterType" style="width:100%">
          <el-option v-for="item in meterTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('floorShare.meterNum')">
        <el-input v-model="form.meterNum" :placeholder="$t('floorShare.meterNumPlaceholder')" />
        <span class="tip-text">{{ $t('floorShare.meterNumTip') }}</span>
      </el-form-item>

      <el-form-item :label="$t('floorShare.feeItem')" prop="configId">
        <el-select v-model="form.configId" style="width:100%">
          <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('floorShare.feePrice')" prop="sharePrice">
        <el-input v-model.number="form.sharePrice" type="number" />
      </el-form-item>

      <el-form-item :label="$t('floorShare.shareType')" prop="shareType">
        <el-select v-model="form.shareType" style="width:100%" @change="handleShareTypeChange">
          <el-option v-for="item in shareTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.shareType === '3003'" :label="$t('floorShare.formula')" prop="formulaValue">
        <el-input v-model="form.formulaValue" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveFloorShareMeter, listFeeConfigs } from '@/api/fee/floorShareApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddFloorShare',
  data() {
    return {
      dialogVisible: false,
      form: {
        floorId: '',
        floorNum: '',
        meterType: '',
        meterNum: '',
        shareType: '',
        formulaValue: '',
        configId: '',
        sharePrice: '',
        communityId: ''
      },
      feeConfigs: [],
      meterTypes: [
        { value: 'D', label: this.$t('floorShare.electricMeter') },
        { value: 'S', label: this.$t('floorShare.waterMeter') }
      ],
      shareTypes: [
        { value: '1001', label: this.$t('floorShare.byArea') },
        { value: '2002', label: this.$t('floorShare.byHouse') },
        { value: '3003', label: this.$t('floorShare.byFormula') }
      ],
      rules: {
        meterType: [
          { required: true, message: this.$t('floorShare.meterTypeRequired'), trigger: 'blur' }
        ],
        configId: [
          { required: true, message: this.$t('floorShare.feeItemRequired'), trigger: 'blur' }
        ],
        sharePrice: [
          { required: true, message: this.$t('floorShare.feePriceRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('floorShare.feePriceNumber'), trigger: 'blur' }
        ],
        shareType: [
          { required: true, message: this.$t('floorShare.shareTypeRequired'), trigger: 'blur' }
        ],
        formulaValue: [
          { required: true, message: this.$t('floorShare.formulaRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        floorId: data.floorId,
        floorNum: data.floorNum,
        communityId: getCommunityId()
      }
      this.loadFeeConfigs()
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    async loadFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.form.communityId,
          feeTypeCd: '888800010017',
          isDefault: 'F',
          state: 'Y'
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeConfigs = feeConfigs
      } catch (error) {
        this.$message.error(this.$t('floorShare.fetchFeeConfigError'))
      }
    },
    handleShareTypeChange(value) {
      if (value !== '3003') {
        this.form.formulaValue = ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveFloorShareMeter(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('floorShare.addFailed'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.tip-text {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}
</style>