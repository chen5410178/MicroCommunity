<template>
  <div class="complaint-detail-container">
    <el-card class="box-card">
      <div class="flex justify-between">
        <div class="text-title">{{ $t('complaintDetail.title') }}</div>
        <div>
          <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <!-- 业主信息 -->
      <div class="margin-top">
        <el-row :gutter="20"  class="text-left">
          <el-col :span="24">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.orderNo') }}
                  </label>
                  <label class="">{{complaintDetailInfo.complaintId}}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.type') }}
                  </label>
                  <label class="">{{complaintDetailInfo.typeName}}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.house') }}
                  </label>
                  <label class="">
                    {{complaintDetailInfo.roomName}}
                  </label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.contact') }}
                  </label>
                  <label class="">{{complaintDetailInfo.complaintName}}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.phone') }}
                  </label>
                  <label class="">{{complaintDetailInfo.tel}}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.status') }}
                  </label>
                  <label class="">{{complaintDetailInfo.stateName}}</label>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.createTime') }}
                  </label>
                  <label class="">{{complaintDetailInfo.createTime}}</label>
                </div>
              </el-col>
              <el-col :span="24">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('complaintDetailInfo.content') }}
                  </label>
                  <label class="">{{complaintDetailInfo.context}}</label>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <div class="margin-top-sm">
        <el-tabs v-model="complaintDetailInfo._currentTab" @tab-click="changeTab(complaintDetailInfo._currentTab)">
          <el-tab-pane :label="$t('complaintDetail.workflow')" name="complaintDetailEvent">
            <complaint-detail-event v-if="complaintDetailInfo._currentTab === 'complaintDetailEvent'" 
              ref="complaintDetailEvent" />
          </el-tab-pane>
          <el-tab-pane :label="$t('complaintDetail.evaluation')" name="complaintDetailAppraise">
            <complaint-detail-appraise v-if="complaintDetailInfo._currentTab === 'complaintDetailAppraise'" 
              ref="complaintDetailAppraise" />
          </el-tab-pane>
          <el-tab-pane :label="$t('complaintDetail.type')" name="complaintDetailType">
            <complaint-detail-type v-if="complaintDetailInfo._currentTab === 'complaintDetailType'" 
              ref="complaintDetailType" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ComplaintDetailEvent from '@/components/oa/ComplaintDetailEvent'
import ComplaintDetailAppraise from '@/components/oa/ComplaintDetailAppraise'
import ComplaintDetailType from '@/components/oa/ComplaintDetailType'
import { getComplaintDetail } from '@/api/oa/complaintDetailApi'
import divider from '@/components/system/divider'

export default {
  name: 'ComplaintDetailList',
  components: {
    ComplaintDetailEvent,
    ComplaintDetailAppraise,
    ComplaintDetailType,
    divider
  },
  data() {
    return {
      complaintDetailInfo: {
        viewComplaintFlag: '',
        complaintId: "",
        typeName: '',
        typeCd: '',
        roomName: "",
        complaintName: "",
        tel: "",
        stateName: "",
        createTime: "",
        context: '',
        _currentTab: 'complaintDetailEvent',
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.complaintDetailInfo.complaintId = this.$route.query.complaintId
    if (!this.complaintDetailInfo.complaintId) {
      return
    }
    const currentTab = this.$route.query.currentTab
    if (currentTab) {
      this.complaintDetailInfo._currentTab = currentTab
    }
    this.loadComplaintInfo()
  },
  methods: {
    async loadComplaintInfo() {
      try {
        const params = {
          complaintId: this.complaintDetailInfo.complaintId,
          page: 1,
          row: 1,
          communityId: this.communityId,
          ownerTypeCd: '1001'
        }
        const { data } = await getComplaintDetail(params)
        if (data && data.length > 0) {
          Object.assign(this.complaintDetailInfo, data[0])
        }
      } catch (error) {
        console.error('获取投诉详情失败:', error)
      }
    },
    changeTab(tab) {
      this.complaintDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].initData({
            complaintId: this.complaintDetailInfo.complaintId,
            typeCd: this.complaintDetailInfo.typeCd
          })
        }
      }, 500)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-detail-container {
  padding: 20px;

  .box-card {
    padding: 20px;
  }

  .text-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .form-group {
    margin-bottom: 15px;

    .col-form-label {
      margin-bottom: 5px;
      color: #606266;
    }
  }
}
</style>