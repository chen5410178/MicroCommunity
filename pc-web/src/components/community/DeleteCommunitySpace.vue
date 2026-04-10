<template>
  <el-dialog 
    :title="$t('communitySpaceManage.confirmDelete')" 
    :visible.sync="visible"
    width="30%"
  >
    <p>{{ $t('communitySpaceManage.confirmDeleteSpace') }}</p>
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
import { deleteCommunitySpace } from '@/api/community/communitySpaceManageApi'

export default {
  name: 'DeleteCommunitySpace',
  data() {
    return {
      visible: false,
      spaceId: ''
    }
  },
  methods: {
    open(space) {
      this.spaceId = space.spaceId
      this.visible = true
    },
    
    async confirmDelete() {
      try {
        await deleteCommunitySpace(this.spaceId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除场地失败:', error)
      }
    }
  }
}
</script>