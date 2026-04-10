<template>
  <el-dialog 
    :title="$t('deleteParkingSpaceApply.confirmOperation')" 
    :visible.sync="visible" 
    width="500px"
  >
    <div class="text-center">
      <p style="font-size: 16px; margin-bottom: 20px;">
        {{ $t('deleteParkingSpaceApply.deletePrompt') }}
      </p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteParkingSpaceApply.cancel') }}
      </el-button>
      <el-button type="danger" @click="confirmDelete">
        {{ $t('deleteParkingSpaceApply.confirmDeleteAction') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteParkingSpaceApply } from '@/api/car/parkingSpaceApplyManageApi'

export default {
  name: 'DeleteParkingSpaceApply',
  data() {
    return {
      visible: false,
      currentApply: {}
    }
  },
  methods: {
    open(apply) {
      this.currentApply = { ...apply }
      this.visible = true
    },
    
    async confirmDelete() {
      try {
        const response = await deleteParkingSpaceApply({
          applyId: this.currentApply.applyId
        })
        
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(response.msg || this.$t('common.deleteFailed'))
        }
      } catch (error) {
        console.error('Error deleting parking space application:', error)
        this.$message.error(this.$t('common.requestError'))
      }
    }
  }
}
</script>