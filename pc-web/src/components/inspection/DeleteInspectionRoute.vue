<template>
  <el-dialog :title="$t('common.confirmDelete')" :visible.sync="visible" width="30%" @close="handleClose">
    <div class="text-center">
      <p>{{ $t('inspectionRoute.confirmDeleteRoute') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="deleteInspectionRoute">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteInspectionRoute as deleteRoute } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'DeleteInspectionRoute',
  data() {
    return {
      visible: false,
      deleteInspectionRouteInfo: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.visible = true
      this.deleteInspectionRouteInfo = { ...params }
    },
    async deleteInspectionRoute() {
      try {
        const params = {
          ...this.deleteInspectionRouteInfo,
          communityId: this.communityId
        }
        await deleteRoute(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除巡检路线失败:', error)
        this.$message.error(this.$t('inspectionRoute.deleteFailed'))
      }
    },
    handleClose() {
      this.visible = false
      this.deleteInspectionRouteInfo = {}
    }
  }
}
</script>