<template>
  <el-dialog
    :title="$t('org.editOrg')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('org.orgName')" prop="orgName">
        <el-input v-model="form.orgName" :placeholder="$t('org.orgName')" />
      </el-form-item>
      <el-form-item :label="$t('org.parentOrg')" prop="parentOrgName">
        <el-input v-model="form.parentOrgName" disabled />
      </el-form-item>
      <el-form-item :label="$t('org.description')" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :placeholder="$t('org.description')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('org.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('org.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateOrg, listOrgs } from '@/api/org/orgApi'

export default {
  name: 'EditOrg',
  data() {
    return {
      visible: false,
      form: {
        orgId: '',
        orgName: '',
        orgLevel:'',
        parentOrgId: '',
        parentOrgName: '',
        description: ''
      },
      rules: {
        orgName: [
          { required: true, message: this.$t('org.orgName'), trigger: 'blur' }
        ],
        parentOrgId: [
          { required: true, message: this.$t('org.parentOrg') }
        ],
        description: [
          { required: true, message: this.$t('org.description'), trigger: 'blur' }
        ],
        orgId: [
          { required: true, message: this.$t('org.orgIdRequired') }
        ]
      }
    }
  },
  methods: {
    show(data) {
      console.log(data)
      this.form = {
        orgId: data.id,
        orgName: data.text,
        orgLevel: '',
       // parentOrgId: data.parentId,
       // parentOrgName: data.parentText || '',
        //description: data.text || ''
      }
      this.visible = true
      this.getList()
    },
    async getList() {
     const {orgs} = await listOrgs({
       page: 1,
       row: 1,
       orgId: this.form.orgId
     })
     this.form = {...orgs[0]}
     console.log(orgs)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          updateOrg(this.form).then(response => {
              console.log(response)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('refresh')
          },err=>{
            this.$message.error(err)
          })
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>