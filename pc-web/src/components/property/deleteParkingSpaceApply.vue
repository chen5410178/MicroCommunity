<template>
  <el-dialog :title="$t('deleteParkingSpaceApply.title')" :visible.sync="visible" width="30%">
    <span>{{ $t('deleteParkingSpaceApply.confirmDelete') }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteParkingSpaceApply } from '@/api/property/deleteParkingSpaceApplyApi'

export default {
  name: 'DeleteParkingSpaceApply',
  data() {
    return {
      visible: false,
      applyId: ''
    }
  },
  methods: {
    open(data) {
      this.applyId = data.applyId
      this.visible = true
    },
    handleConfirm() {
      deleteParkingSpaceApply({ applyId: this.applyId }).then(response => {
        console.log(response)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      }).catch(error => {
        this.$message.error(error.message)
      })
    }
  }
}
</script>