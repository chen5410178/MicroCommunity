<template>
  <el-dialog 
    :title="$t('dataPrivilege.addTitle')" 
    :visible.sync="visible"
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('dataPrivilege.name')" prop="name">
        <el-input 
          v-model="form.name" 
          :placeholder="$t('dataPrivilege.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('dataPrivilege.code')" prop="code">
        <el-input 
          v-model="form.code" 
          :placeholder="$t('dataPrivilege.codePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('dataPrivilege.remark')" prop="remark">
        <el-input 
          v-model="form.remark" 
          type="textarea"
          :placeholder="$t('dataPrivilege.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveDataPrivilege } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddDataPrivilege',
  data() {
    return {
      visible: false,
      form: {
        name: '',
        code: '',
        remark: '',
        communityId: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('dataPrivilege.nameRequired'), trigger: 'blur' },
          { max: 50, message: this.$t('dataPrivilege.nameMaxLength'), trigger: 'blur' }
        ],
        code: [
          { required: true, message: this.$t('dataPrivilege.codeRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('dataPrivilege.codeMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('dataPrivilege.remarkRequired'), trigger: 'blur' },
          { max: 256, message: this.$t('dataPrivilege.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.form.communityId = getCommunityId()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveDataPrivilege(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Failed to add data privilege:', error)
          }
        }
      })
    }
  }
}
</script>