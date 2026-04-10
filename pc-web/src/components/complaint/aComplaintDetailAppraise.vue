<template>
  <div class="appraise-container">
    <el-table :data="appraises" border style="width: 100%">
      <el-table-column prop="createUserName" :label="$t('complaintDetailAppraise.userName')" align="center"></el-table-column>
      <el-table-column prop="context" :label="$t('complaintDetailAppraise.content')" align="center"></el-table-column>
      <el-table-column prop="score" :label="$t('complaintDetailAppraise.score')" align="center"></el-table-column>
      <el-table-column prop="state" :label="$t('complaintDetailAppraise.status')" align="center">
        <template slot-scope="scope">
          {{ scope.row.state === 'W' ? $t('complaintDetailAppraise.pending') : $t('complaintDetailAppraise.replied') }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('complaintDetailAppraise.time')" align="center"></el-table-column>
      <el-table-column prop="replyUserName" :label="$t('complaintDetailAppraise.replyUser')" align="center"></el-table-column>
      <el-table-column prop="replyContext" :label="$t('complaintDetailAppraise.replyContent')" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listComplaintAppraises } from '@/api/complaint/adminComplaintDetailApi'

export default {
  name: 'AComplaintDetailAppraise',
  props: {
    complaintId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      appraises: []
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
        const { data } = await listComplaintAppraises(params)
        this.appraises = data || []
      } catch (error) {
        this.$message.error(this.$t('complaintDetailAppraise.fetchError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.appraise-container {
  padding: 20px 0;
}
</style>