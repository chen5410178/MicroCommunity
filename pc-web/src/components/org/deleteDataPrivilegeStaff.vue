<template>
  <el-dialog :title="$t('dataPrivilege.confirmOperation')" :visible.sync="visible" width="400px" center>
    <div class="delete-content">
      <p>{{ $t('dataPrivilege.confirmCancelAuth') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteDataPrivilegeStaff } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteDataPrivilegeStaff',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        dpId: '',
        staffId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        dpId: data.dpId,
        staffId: data.staffId,
        dpsId: data.dpsId,
        communityId: getCommunityId()
      }
      this.visible = true
      this.loading = false
    },
    async handleConfirm() {
      this.loading = true
      try {
        await deleteDataPrivilegeStaff(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete data privilege staff:', error)
        this.$message.error(this.$t('dataPrivilege.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: center;
}
</style>