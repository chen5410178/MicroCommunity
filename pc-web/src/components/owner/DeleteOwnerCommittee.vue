<template>
  <el-dialog :title="$t('ownerCommitteeManage.deleteTitle')" :visible.sync="visible" width="500px"
    :close-on-click-modal="false" @close="handleClose">
    <div class="delete-confirm">
      <p>{{ $t('common.deleteConfirm') }}</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" :loading="loading" @click="handleDelete">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteOwnerCommittee } from '@/api/owner/ownerCommitteeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteOwnerCommittee',
  data() {
    return {
      visible: false,
      loading: false,
      currentData: null
    }
  },
  methods: {
    open(data) {
      this.currentData = data
      this.visible = true
    },
    handleClose() {
      this.currentData = null
    },
    async handleDelete() {
      try {
        this.loading = true
        const params = {
          ...this.currentData,
          communityId: getCommunityId()
        }
        await deleteOwnerCommittee(params)
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('ownerCommitteeManage.deleteError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-confirm {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>