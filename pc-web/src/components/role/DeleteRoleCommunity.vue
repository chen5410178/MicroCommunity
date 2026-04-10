<template>
  <el-dialog 
    :title="$t('role.confirmDelete')" 
    :visible.sync="visible" 
    width="30%"
    custom-class="center-dialog"
  >
    <div class="dialog-content">
      <span>{{ $t('role.deleteTip') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('role.delete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteRoleCommunity } from '@/api/role/roleApi'

export default {
  name: 'DeleteRoleCommunity',
  data() {
    return {
      visible: false,
      community: null
    }
  },
  methods: {
    show(community) {
      this.community = community
      this.visible = true
    },
    async confirmDelete() {
      try {
        await deleteRoleCommunity({
          roleId: this.community.roleId,
          communityId: this.community.communityId
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

<style scoped>
/* Center the dialog content */
.dialog-content {
  text-align: center;
}

/* Center the footer buttons */
.dialog-footer {
  display: flex;
  justify-content: center;
}
</style>

<style>
/* This needs to be global to override Element UI styles */
.center-dialog .el-dialog__header {
  text-align: center;
}

.center-dialog .el-dialog__body {
  text-align: center;
  padding: 15px 20px;
}
</style>