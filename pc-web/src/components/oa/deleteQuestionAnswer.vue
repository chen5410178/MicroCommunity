<template>
  <el-dialog
    :title="$t('ownerVoting.deleteQuestionAnswer.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div>
      <p>{{ $t('ownerVoting.deleteQuestionAnswer.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="danger"
        @click="handleConfirm"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteQuestionAnswer } from '@/api/oa/ownerVotingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteQuestionAnswer',
  data() {
    return {
      visible: false,
      qaId: '',
      communityId: getCommunityId()
    }
  },
  methods: {
    open(qa) {
      this.qaId = qa.qaId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteQuestionAnswer({
          qaId: this.qaId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('ownerVoting.deleteQuestionAnswer.error'))
      }
    },
    handleClose() {
      this.qaId = ''
    }
  }
}
</script>