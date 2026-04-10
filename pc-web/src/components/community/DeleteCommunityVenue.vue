<template>
  <el-dialog 
    :title="$t('communitySpaceManage.confirmDelete')" 
    :visible.sync="visible"
    width="30%"
  >
    <p>{{ $t('communitySpaceManage.confirmDeleteVenue') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('communitySpaceManage.cancel') }}
      </el-button>
      <el-button type="danger" @click="confirmDelete">
        {{ $t('communitySpaceManage.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteCommunityVenue } from '@/api/community/communitySpaceManageApi'

export default {
  name: 'DeleteCommunityVenue',
  data() {
    return {
      visible: false,
      venueId: ''
    }
  },
  methods: {
    open(venue) {
      this.venueId = venue.venueId
      this.visible = true
    },
    
    async confirmDelete() {
      try {
        await deleteCommunityVenue(this.venueId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除场馆失败:', error)
      }
    }
  }
}
</script>