<template>
  <el-dialog :title="$t('sectionManage.deleteTitle')" :visible.sync="visible" width="30%" center>
    <div style="text-align: center">
      <p>{{ $t('sectionManage.deleteConfirm') }}</p>
      <p style="font-weight: bold; color: #e6a23c; margin-top: 10px">
        {{ deleteSectionInfo.sectionName || deleteSectionInfo.sectionNum }}
      </p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('sectionManage.cancel') }}</el-button>
      <el-button type="danger" @click="deleteSection" :loading="deleting">
        {{ $t('sectionManage.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteParkingAreaSection } from '@/api/car/listParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteSection',
  data() {
    return {
      visible: false,
      deleting: false,
      deleteSectionInfo: {}
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.deleteSectionInfo = { ...row }
    },

    async deleteSection() {
      try {
        this.deleting = true
        const params = {
          sectionId: this.deleteSectionInfo.sectionId,
          paId: this.deleteSectionInfo.paId,
          communityId: getCommunityId()
        }

        await deleteParkingAreaSection(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除停车区域失败:', error)
        this.$message.error(error.message || this.$t('sectionManage.deleteFailed'))
      } finally {
        this.deleting = false
      }
    }
  }
}
</script>
