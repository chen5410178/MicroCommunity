<template>
  <el-dialog :title="$t('common.confirm')" :visible.sync="visible" width="30%">
    <span>{{ $t('org.confirmDelete') }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteOrgRelStaff } from '@/api/org/orgApi'

export default {
  name: 'DeleteOrgRelStaff',
  data() {
    return {
      visible: false,
      currentStaff: null
    }
  },
  methods: {
    show(staff) {
      this.currentStaff = staff
      this.visible = true
    },
    handleConfirm() {
      deleteOrgRelStaff({
        staffId: this.currentStaff.userId,
        orgId: this.currentStaff.orgId
      }).then(response => {
        console.log(response)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      },error => {
        console.log(error)
        this.$message.error(error)
      })
    }
  }
}
</script>