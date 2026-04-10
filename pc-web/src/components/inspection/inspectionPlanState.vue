<template>
  <el-dialog :title="$t('inspectionPlan.confirmOperation')" :visible.sync="visible" width="30%">
    <div class="text-center">
      <p>{{ $t('inspectionPlan.confirmStateChange', { state: stateName }) }}</p>
    </div>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('inspectionPlan.cancel') }}</el-button>
      <el-button type="primary" @click="confirmChange">{{ $t('inspectionPlan.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateInspectionPlanState } from '@/api/inspection/inspectionPlanApi'

export default {
  name: 'InspectionPlanState',
  data() {
    return {
      visible: false,
      inspectionPlanId: null,
      state: null,
      stateName: ''
    }
  },
  methods: {
    open({ inspectionPlanId, state, stateName }) {
      this.inspectionPlanId = inspectionPlanId
      this.state = state
      this.stateName = stateName
      this.visible = true
    },

    async confirmChange() {
      try {
        await updateInspectionPlanState({
          inspectionPlanId: this.inspectionPlanId,
          state: this.state
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('inspectionPlan.stateChangeError'))
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