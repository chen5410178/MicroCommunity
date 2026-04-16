<template>
  <div class="notice-detail-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('noticeDetail.title') }}</span>
        <el-button type="primary" size="small" @click="_goBack">
          <i class="el-icon-close"></i>
          {{ $t('noticeDetail.back') }}
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-card shadow="never">
            <el-form label-position="left" label-width="120px">
              <el-form-item :label="$t('noticeDetail.titleLabel')">
                <el-input v-model="noticeDetailInfo.title" readonly class="readonly-input"></el-input>
              </el-form-item>

              <el-form-item :label="$t('noticeDetail.noticeType')">
                <el-input v-model="noticeDetailInfo.noticeTypeCdName" readonly class="readonly-input"></el-input>
              </el-form-item>

              <el-form-item :label="$t('noticeDetail.startTime')">
                <el-input v-model="noticeDetailInfo.startTime" readonly class="readonly-input"></el-input>
              </el-form-item>

              <el-form-item :label="$t('noticeDetail.endTime')">
                <el-input v-model="noticeDetailInfo.endTime" readonly class="readonly-input"></el-input>
              </el-form-item>

              <el-form-item :label="$t('noticeDetail.createTime')">
                <el-input v-model="noticeDetailInfo.createTime" readonly class="readonly-input"></el-input>
              </el-form-item>

              <el-form-item :label="$t('noticeDetail.content')">
                <div class="notice-content" v-html="noticeDetailInfo.context"></div>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getNoticeDetail } from '@/api/oa/noticeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NoticeDetailList',
  data() {
    return {
      noticeDetailInfo: {
        noticeId: '',
        title: '',
        context: '',
        startTime: '',
        endTime: '',
        noticeTypeCd: '',
        noticeTypeCdName: '',
        createTime: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    const noticeId = this.$route.query.noticeId
    if (noticeId) {
      this.noticeDetailInfo.noticeId = noticeId
      this._listNotices()
    }
  },
  methods: {
    async _listNotices() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          noticeId: this.noticeDetailInfo.noticeId
        }
        const { notices } = await getNoticeDetail(params)
        if (notices && notices.length > 0) {
          // 过滤XSS攻击
          notices[0].context = this.filterXSS(notices[0].context)
          this.noticeDetailInfo =notices[0]
        }
      } catch (error) {
        this.$message.error(this.$t('noticeDetail.fetchError'))
      }
    },
    filterXSS(html) {
      // 这里应该实现XSS过滤逻辑
      // 实际项目中应该使用专门的XSS过滤库
      return html || ''
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .float-right {
      float: right;
    }
  }

  .readonly-input {
    ::v-deep .el-input__inner {
      background-color: #f5f7fa;
      border-color: #e4e7ed;
      color: #606266;
      cursor: not-allowed;
    }
  }

  .notice-content {
    padding: 10px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    min-height: 100px;
    background-color: #f5f7fa;
  }
}
</style>