<template>
  <el-dialog :title="$t('feeComboManage.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('feeComboManage.add.comboName')" prop="comboName">
        <el-input v-model="form.comboName" :placeholder="$t('feeComboManage.add.comboNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('feeComboManage.add.feeItems')" prop="configIds">
        <el-select v-model="form.configIds" multiple filterable style="width: 100%"
          :placeholder="$t('feeComboManage.add.feeItemsPlaceholder')">
          <el-option v-for="item in configs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('feeComboManage.add.remark')">
        <el-input v-model="form.remark" type="textarea" :rows="3"
          :placeholder="$t('feeComboManage.add.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveFeeCombo } from '@/api/fee/feeComboManageApi'
import { listFeeConfigs } from '@/api/fee/feeConfigManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddFeeCombo',
  data() {
    return {
      visible: false,
      form: {
        comboName: '',
        configIds: [],
        remark: '',
        communityId: ''
      },
      rules: {
        comboName: [
          {
            required: true,
            message: this.$t('feeComboManage.validate.comboNameRequired'),
            trigger: 'blur'
          },
          {
            max: 30,
            message: this.$t('feeComboManage.validate.comboNameMaxLength'),
            trigger: 'blur'
          }
        ],
        configIds: [
          {
            required: true,
            message: this.$t('feeComboManage.validate.feeItemsRequired'),
            trigger: 'change'
          }
        ]
      },
      configs: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.form.communityId = getCommunityId()
      this.getFeeConfigs()
    },
    async getFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.form.communityId
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.configs = feeConfigs
      } catch (error) {
        this.$message.error(this.$t('feeComboManage.fetchConfigError'))
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveFeeCombo(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('feeComboManage.add.error'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        comboName: '',
        configIds: [],
        remark: '',
        communityId: ''
      }
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>