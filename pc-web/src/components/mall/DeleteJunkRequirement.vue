<template>
  <el-dialog
    :title="$t('junkRequirement.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="close"
  >
    <div class="text-center">
      <p>{{ $t('junkRequirement.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="deleteJunkRequirement">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteJunkRequirement } from '@/api/mall/junkRequirementManageApi'

export default {
  name: 'DeleteJunkRequirement',
  data() {
    return {
      visible: false,
      junkRequirementId: ''
    }
  },
  methods: {
    open(row) {
      this.junkRequirementId = row.junkRequirementId
      this.visible = true
    },
    close() {
      this.visible = false
      this.junkRequirementId = ''
    },
    async deleteJunkRequirement() {
      try {
        await deleteJunkRequirement({
          junkRequirementId: this.junkRequirementId,
          communityId: this.getCommunityId()
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.close()
      } catch (error) {
        this.$message.error(error.message || this.$t('junkRequirement.delete.error'))
      }
    }
  }
}
</script>