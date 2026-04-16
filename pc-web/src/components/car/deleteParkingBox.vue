<template>
  <el-dialog
    :title="$t('deleteParkingBox.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <p>{{ $t('deleteParkingBox.confirmDelete') }}</p>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteParkingBox.cancel') }}
      </el-button>
      <el-button type="danger" @click="handleConfirm">
        {{ $t('deleteParkingBox.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteParkingBox } from '@/api/car/parkingBoxManageApi'

export default {
  name: 'DeleteParkingBox',
  data() {
    return {
      visible: false,
      boxId: ''
    }
  },
  methods: {
    open(row) {
      this.boxId = row.boxId
      this.visible = true
    },
    
    async handleConfirm() {
      try {
        await deleteParkingBox({ boxId: this.boxId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除岗亭失败:', error)
        this.$message.error(this.$t('parkingBoxManage.deleteError'))
      }
    }
  }
}
</script>