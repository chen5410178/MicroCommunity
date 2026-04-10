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
  import { deleteRoleStaff } from '@/api/role/roleApi'
  
  export default {
    name: 'DeleteRoleStaff',
    data() {
      return {
        visible: false,
        staff: null,
        roleId:''
      }
    },
    methods: {
      show(staff,roleId) {
        this.staff = staff
        this.roleId = roleId
        this.visible = true
      },
      async confirmDelete() {
        try {
          console.log(this.staff)
          await deleteRoleStaff({
            roleId: this.roleId,
            staffId: this.staff.userId,
            userId:this.staff.userId
          })
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