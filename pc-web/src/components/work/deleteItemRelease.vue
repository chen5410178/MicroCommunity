<template>
  <el-dialog
    :title="$t('common.confirmOperation')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center">
      <p>{{ $t('itemReleaseManage.delete.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteItemRelease } from '@/api/work/itemReleaseManageApi'

export default {
  name: 'DeleteItemRelease',
  data() {
    return {
      visible: false,
      communityId: '',
      currentItem: {}
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(item) {
      this.currentItem = {
        ...item,
        communityId: this.communityId
      }
      this.visible = true
    },
    handleClose() {
      this.currentItem = {}
    },
    async handleConfirm() {
      try {
        await deleteItemRelease(this.currentItem)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('删除物品放行失败:', error)
      }
    }
  }
}
</script>