<template>
  <el-dialog 
    :title="$t('marketBlacklist.edit.title')" 
    :visible.sync="visible" 
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketBlacklist.form.personName')" prop="personName">
        <el-input v-model="formData.personName" />
      </el-form-item>
      <el-form-item :label="$t('marketBlacklist.form.filter')" prop="filter">
        <el-input v-model="formData.filter" />
      </el-form-item>
      <el-form-item :label="$t('marketBlacklist.form.remark')" prop="remark">
        <el-input v-model="formData.remark" type="textarea" />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateMarketBlacklist } from '@/api/market/marketBlacklistManageApi'

export default {
  name: 'EditMarketBlacklist',
  data() {
    return {
      visible: false,
      formData: {
        blId: '',
        personName: '',
        filter: '',
        remark: ''
      },
      rules: {
        personName: [
          { required: true, message: this.$t('marketBlacklist.validate.personName'), trigger: 'blur' },
          { max: 64, message: this.$t('marketBlacklist.validate.personNameMax'), trigger: 'blur' }
        ],
        filter: [
          { required: true, message: this.$t('marketBlacklist.validate.filter'), trigger: 'blur' },
          { max: 64, message: this.$t('marketBlacklist.validate.filterMax'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('marketBlacklist.validate.remark'), trigger: 'blur' },
          { max: 512, message: this.$t('marketBlacklist.validate.remarkMax'), trigger: 'blur' }
        ],
        blId: [
          { required: true, message: this.$t('marketBlacklist.validate.blId'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMarketBlacklist(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>