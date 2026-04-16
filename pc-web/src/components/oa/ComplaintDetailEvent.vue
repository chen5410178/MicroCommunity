<template>
  <div class="complaint-detail-event">
    <el-table :data="events" border style="width: 100%" v-loading="loading">
      <el-table-column prop="eventType" :label="$t('complaintDetailEvent.type')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.eventType === '1000'">{{ $t('complaintDetailEvent.submit') }}</span>
          <span v-else-if="scope.row.eventType === '1001'">{{ $t('complaintDetailEvent.process') }}</span>
          <span v-else-if="scope.row.eventType === '2002'">{{ $t('complaintDetailEvent.evaluate') }}</span>
          <span v-else>{{ $t('complaintDetailEvent.reply') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" :label="$t('complaintDetailEvent.operator')" align="center" />
      <el-table-column prop="remark" :label="$t('complaintDetailEvent.remark')" align="center" />
      <el-table-column prop="createTime" :label="$t('complaintDetailEvent.time')" align="center" />
    </el-table>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listComplaintEvent } from '@/api/oa/complaintDetailApi'

export default {
  name: 'ComplaintDetailEvent',
  data() {
    return {
      events: [],
      loading: false,
      complaintId: '',
      communityId: ''
    }
  },
  methods: {
    async initData(params) {
      this.communityId = getCommunityId()
      this.complaintId = params.complaintId
      await this.loadData()
    },
    async loadData() {
      try {
        this.loading = true
        const params = {
          communityId: this.communityId,
          complaintId: this.complaintId,
          page: 1,
          row: 100
        }
        const { data } = await listComplaintEvent(params)
        this.events = data || []
      } catch (error) {
        console.error('获取工单流转数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-detail-event {
  margin-top: 20px;
}
</style>