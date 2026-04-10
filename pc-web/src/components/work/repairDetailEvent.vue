<template>
  <div>
    <el-table :data="events" border style="width: 100%">
      <el-table-column prop="eventTypeName" :label="$t('repairDetailEvent.eventType')" align="center" />
      <el-table-column prop="staffName" :label="$t('repairDetailEvent.staff')" align="center" />
      <el-table-column prop="remark" :label="$t('repairDetailEvent.description')" align="center" />
      <el-table-column prop="createTime" :label="$t('repairDetailEvent.createTime')" align="center" />
    </el-table>
  </div>
</template>

<script>
import { getRepairEvents } from '@/api/work/repairDetailApi'

export default {
  name: 'RepairDetailEvent',
  props: {
    repairId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      events: []
    }
  },
  methods: {
    async loadData() {
      try {
        this.events = await getRepairEvents(this.repairId)
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>