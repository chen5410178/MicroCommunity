<template>
  <el-dialog
    :title="$t('inspectionPlan.confirmDeleteTitle')"
    :visible.sync="visible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('inspectionPlan.confirmDeleteContent') }}</p>
    </div>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('inspectionPlan.cancel') }}</el-button>
      <el-button type="danger" @click="confirmDelete">{{ $t('inspectionPlan.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteInspectionPlan } from '@/api/inspection/inspectionPlanApi'

export default {
  name: 'DeleteInspectionPlan',
  data() {
    return {
      visible: false,
      currentPlanId: null
    }
  },
  methods: {
    open(plan) {
      this.currentPlanId = plan.inspectionPlanId
      this.visible = true
    },
    
    async confirmDelete() {
      try {
       const {code,msg} = await deleteInspectionPlan({ inspectionPlanId: this.currentPlanId })
       if(code != 0){
        this.$message.error(msg)
        return 
       }
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error)
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