<template>
  <div class="complaint-detail-appraise">
    <el-table :data="appraises" border style="width: 100%" v-loading="loading">
      <el-table-column prop="createUserName" :label="$t('complaintDetailAppraise.userName')" align="center" />
      <el-table-column prop="context" :label="$t('complaintDetailAppraise.content')" align="center" />
      <el-table-column prop="score" :label="$t('complaintDetailAppraise.score')" align="center" />
      <el-table-column prop="state" :label="$t('complaintDetailAppraise.status')" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.state === 'W' ? $t('complaintDetailAppraise.waitReply') :
            $t('complaintDetailAppraise.replied') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('complaintDetailAppraise.time')" align="center" />
      <el-table-column prop="replyUserName" :label="$t('complaintDetailAppraise.replyUser')" align="center" />
      <el-table-column prop="replyContext" :label="$t('complaintDetailAppraise.replyContent')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.state === 'W'" type="text" size="small" @click="openReplyModal(scope.row)">
            {{ $t('complaintDetailAppraise.reply') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <reply-complaint-appraise ref="replyComplaintAppraise" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listComplaintAppraise } from '@/api/oa/complaintDetailApi'
import ReplyComplaintAppraise from './ReplyComplaintAppraise'

export default {
  name: 'ComplaintDetailAppraise',
  components: {
    ReplyComplaintAppraise
  },
  data() {
    return {
      appraises: [],
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
        const { data } = await listComplaintAppraise(params)
        this.appraises = data || []
      } catch (error) {
        console.error('获取工单评价数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    openReplyModal(row) {
      this.$refs.replyComplaintAppraise.open(row)
    },
    handleSuccess() {
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-detail-appraise {
  margin-top: 20px;
}
</style>