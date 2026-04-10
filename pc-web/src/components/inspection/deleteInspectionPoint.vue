<template>
  <el-dialog
    :title="$t('deleteInspectionPoint.title')"
    :visible.sync="dialogVisible"
    width="30%"
    center
  >
    <div class="text-center">
      <i class="el-icon-warning" style="font-size: 80px; color: #e6a23c;"></i>
      <p style="font-size: 18px; margin: 20px 0;">
        {{ $t('deleteInspectionPoint.confirmText') }}
      </p>
      <p style="font-size: 16px; color: #409EFF; font-weight: bold;">
        {{ deleteInspectionPointInfo.inspectionName }}
      </p>
    </div>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="deleteInspectionPoint">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteInspectionPoint } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteInspectionPoint',
  data() {
    return {
      dialogVisible: false,
      deleteInspectionPointInfo: {
        inspectionId: '',
        inspectionName: '',
        communityId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(inspectionPoint) {
      this.dialogVisible = true
      this.deleteInspectionPointInfo = {
        inspectionId: inspectionPoint.inspectionId,
        inspectionName: inspectionPoint.inspectionName,
        communityId: this.communityId
      }
    },
    
    async deleteInspectionPoint() {
      try {
        const response = await deleteInspectionPoint(this.deleteInspectionPointInfo)
        
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.dialogVisible = false
          this.$emit('success')
        } else {
          this.$message.error(response.msg || this.$t('deleteInspectionPoint.deleteFailed'))
        }
      } catch (error) {
        console.error('删除巡检点失败:', error)
        this.$message.error(this.$t('deleteInspectionPoint.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>