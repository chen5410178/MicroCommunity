<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('inspectionRoute.confirmDeletePoint') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="deleteInspectionRoutePoint"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteInspectionRoutePoint as deletePoint } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'DeleteInspectionRoutePoint',
  data() {
    return {
      visible: false,
      deleteInspectionRoutePointInfo: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.visible = true
      this.deleteInspectionRoutePointInfo = { ...params }
    },
    async deleteInspectionRoutePoint() {
      try {
        const params = {
          ...this.deleteInspectionRoutePointInfo,
          communityId: this.communityId
        }
        await deletePoint(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除巡检点失败:', error)
        this.$message.error(this.$t('inspectionRoute.deleteFailed'))
      }
    },
    handleClose() {
      this.visible = false
      this.deleteInspectionRoutePointInfo = {}
    }
  }
}
</script>