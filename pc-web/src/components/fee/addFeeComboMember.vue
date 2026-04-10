<template>
  <el-dialog :title="$t('common.add')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px" :rules="rules">
      <el-form-item :label="$t('feeComboMemberManage.feeType')" prop="feeTypeCd">
        <el-select v-model="formData.feeTypeCd" :placeholder="$t('feeComboMemberManage.selectFeeType')"
          style="width:100%" @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeCds" :key="item.statusCd" :label="item.name" :value="item.statusCd"
            :disabled="item.statusCd === '888800010008' || item.statusCd === '888800010017'" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('feeComboMemberManage.feeItem')" prop="configId">
        <el-select v-model="formData.configId" :placeholder="$t('feeComboMemberManage.selectFeeItem')"
          style="width:100%">
          <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { listFeeConfigs } from '@/api/fee/feeComboMemberManageApi'
import { saveFeeComboMember } from '@/api/fee/feeComboMemberManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddFeeComboMember',
  data() {
    return {
      visible: false,
      formData: {
        feeTypeCd: '',
        configId: '',
        comboId: '',
        communityId: ''
      },
      feeTypeCds: [],
      feeConfigs: [],
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('feeComboMemberManage.feeTypeRequired'), trigger: 'change' }
        ],
        configId: [
          { required: true, message: this.$t('feeComboMemberManage.feeItemRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.formData.comboId = params.comboId
      this.formData.communityId = getCommunityId()
      this.loadFeeTypeCds()
      this.visible = true
    },
    async loadFeeTypeCds() {
      try {
        this.feeTypeCds = await getDict('pay_fee_config', 'fee_type_cd')
      } catch (error) {
        this.$message.error(this.$t('feeComboMemberManage.fetchDictError'))
      }
    },
    async handleFeeTypeChange(feeTypeCd) {
      try {
        this.formData.configId = ''
        const params = {
          page: 1,
          row: 500,
          communityId: this.formData.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeConfigs = feeConfigs
      } catch (error) {
        this.$message.error(this.$t('feeComboMemberManage.fetchFeeConfigError'))
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveFeeComboMember(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('feeComboMemberManage.addFailed'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.feeConfigs = []
    }
  }
}
</script>