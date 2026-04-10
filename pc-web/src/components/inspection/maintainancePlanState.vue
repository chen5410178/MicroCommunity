<template>
  <el-dialog
    :title="$t('maintainancePlanState.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>
        {{ $t('maintainancePlanState.confirm') }}
        <span style="color:#409EFF">{{ formData.stateName }}</span>
        {{ $t('maintainancePlanState.plan') }}
      </p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMaintainancePlanState } from '@/api/inspection/maintainancePlanManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainancePlanState',
  data() {
    return {
      visible: false,
      formData: {
        planId: '',
        stateName: '',
        state: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.visible = true
    },
    async handleConfirm() {
      try {
        const params = {
          planId: this.formData.planId,
          state: this.formData.state,
          communityId: this.communityId
        }
        await updateMaintainancePlanState(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('maintainancePlanState.error'))
      }
    },
    handleClose() {
      this.visible = false
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>