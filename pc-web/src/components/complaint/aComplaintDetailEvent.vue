<template>
  <div class="event-container">
    <el-table :data="events" border style="width: 100%">
      <el-table-column prop="eventType" :label="$t('complaintDetailEvent.type')" align="center">
        <template slot-scope="scope">
          {{ getEventTypeName(scope.row.eventType) }}
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" :label="$t('complaintDetailEvent.operator')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('complaintDetailEvent.remark')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('complaintDetailEvent.time')" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listComplaintEvents } from '@/api/complaint/adminComplaintDetailApi'

export default {
  name: 'AComplaintDetailEvent',
  props: {
    complaintId: {
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
        const params = {
          complaintId: this.complaintId,
          page: 1,
          row: 100
        }
        const { data } = await listComplaintEvents(params)
        this.events = data || []
      } catch (error) {
        this.$message.error(this.$t('complaintDetailEvent.fetchError'))
      }
    },
    getEventTypeName(eventType) {
      switch (eventType) {
        case '1000': return this.$t('complaintDetailEvent.submit')
        case '1001': return this.$t('complaintDetailEvent.process')
        case '2002': return this.$t('complaintDetailEvent.evaluate')
        default: return this.$t('complaintDetailEvent.reply')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.event-container {
  padding: 20px 0;
}
</style>