<template>
  <el-dialog
    :title="$t('startWork.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('startWork.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteWorkPool } from '@/api/oa/startWorkApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteWork',
  data() {
    return {
      visible: false,
      currentWork: null,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(work) {
      this.currentWork = work
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.currentWork = null
    },
    async handleConfirm() {
      try {
        await deleteWorkPool({
          workId: this.currentWork.workId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('startWork.delete.error'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>