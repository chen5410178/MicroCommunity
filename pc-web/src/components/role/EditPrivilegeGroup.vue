<template>
  <el-dialog :title="$t('role.edit')" :visible.sync="visible" width="40%" @close="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('role.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('role.requiredName')" />
      </el-form-item>

      <el-form-item :label="$t('role.description')" prop="description">
        <el-input v-model="form.description" type="textarea" :placeholder="$t('role.descriptionLength')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('role.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePrivilegeGroup } from '@/api/role/roleApi'

export default {
  name: 'EditPrivilegeGroup',
  data() {
    return {
      visible: false,
      form: {
        pgId: '',
        name: '',
        description: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('role.requiredName'), trigger: 'blur' },
          { min: 2, max: 10, message: this.$t('role.nameLength'), trigger: 'blur' }
        ],
        description: [
          { max: 200, message: this.$t('role.descriptionLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    show(role) {
      this.form = {
        pgId: role.pgId,
        name: role.name,
        description: role.description
      }
      this.visible = true
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updatePrivilegeGroup(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
    }
  }
}
</script>