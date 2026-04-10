<template>
  <el-dialog
    :title="$t('deleteUnit.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteUnit.confirmMessage') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('deleteUnit.cancel') }}</el-button>
      <el-button type="danger" @click="deleteUnit">{{ $t('deleteUnit.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteUnit } from '@/api/room/deleteUnitApi'

export default {
  name: 'DeleteUnit',
  data() {
    return {
      visible: false,
      deleteUnitInfo: {
        _currentFloorId: '',
        _currentUnitId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.deleteUnitInfo._currentFloorId = params.floorId
      this.deleteUnitInfo._currentUnitId = params.unitId
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async deleteUnit() {
      const param = {
        floorId: this.deleteUnitInfo._currentFloorId,
        unitId: this.deleteUnitInfo._currentUnitId,
        communityId: this.getCommunityId()
      }
      
      try {
        await deleteUnit(param)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('handleRefreshTree', {})

        this.handleClose()
      } catch (error) {
        console.error('删除单元失败:', error)
        this.$message.error(error.message || this.$t('deleteUnit.errorMessage'))
      }
    }
  }
}
</script>