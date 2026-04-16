<template>
  <div>
    <point-plan ref="pointPlan" :inspection-id="currentInspectionId" />
  </div>
</template>

<script>
import PointPlan from '@/components/inspection/pointPlan'

export default {
  components: {
    PointPlan
  },
  data() {
    return {
      currentInspectionId: '123456' // 示例巡检点ID
    }
  },
  mounted() {
    // 模拟切换巡检点
    this.$refs.pointPlan.refresh()
    
    // 示例：设置筛选条件
    this.$refs.pointPlan.setPlanUserName('张三')
    this.$refs.pointPlan.setDateRange('2023-01-01', '2023-12-31')
  }
}
</script>