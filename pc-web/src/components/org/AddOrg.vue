<template>
    <el-dialog
      :title="$t('org.addOrg')"
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
  import { saveOrg } from '@/api/org/orgApi'
  
  export default {
    name: 'AddOrg',
    data() {
      return {
        visible: false,
        form: {
          orgName: '',
          parentOrgId: '',
          parentOrgName: '',
          description: ''
        },
        rules: {
          orgName: [
            { required: true, message: this.$t('org.orgNameRequired'), trigger: 'blur' },
            { min: 2, max: 50, message: this.$t('org.orgNameLength'), trigger: 'blur' }
          ],
          parentOrgId: [
            { required: true, message: this.$t('org.parentOrgRequired') }
          ],
          description: [
            { required: true, message: this.$t('org.descriptionRequired'), trigger: 'blur' },
            { max: 200, message: this.$t('org.descriptionMaxLength'), trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      show(data) {
        this.form = {
          orgName: '',
          parentOrgId: data.parentOrgId,
          parentOrgName: data.parentOrgName,
          description: ''
        }
        this.visible = true
      },
      handleSubmit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            saveOrg(this.form).then(response => {
              console.log(response)
              this.$message.success(this.$t('common.operationSuccess'))
              this.visible = false
              this.$emit('refresh')
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