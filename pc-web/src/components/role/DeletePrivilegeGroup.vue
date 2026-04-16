<template>
    <el-dialog
      :title="$t('role.confirmDelete')"
      :visible.sync="visible"
      width="30%"
    >
      <span>{{ $t('role.deleteTip') }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDelete">{{ $t('role.delete') }}</el-button>
      </span>
    </el-dialog>
  </template>
  
  <script>
  import { deletePrivilegeGroup } from '@/api/role/roleApi'
  
  export default {
    name: 'DeletePrivilegeGroup',
    data() {
      return {
        visible: false,
        pgId: ''
      }
    },
    methods: {
      show(role) {
        this.pgId = role.pgId
        this.visible = true
      },
      async confirmDelete() {
        try {
          await deletePrivilegeGroup({ pgId: this.pgId })
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        } catch (error) {
          this.$message.error(error.message)
        }
      }
    }
  }
  </script>