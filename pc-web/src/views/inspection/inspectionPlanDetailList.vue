<template>
  <div class="inspection-plan-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('inspectionPlanDetail.title') }}</span>
      </div>
        <div class="content-wrapper">
          <div class="detail-wrapper">
            <el-row :gutter="10">
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.planName') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.inspectionPlanName }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.planRoute') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.inspectionRouteName }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.planPeriod') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.inspectionPlanPeriodName }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.signType') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.signTypeName }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.dateRange') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.startDate }}~{{ inspectionPlanDetailInfo.endDate }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.timeRange') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.startTime }}~{{ inspectionPlanDetailInfo.endTime }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.beforeTime') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.beforeTime }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.creator') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.createUserName }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.createTime') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.createTime }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="detail-item">
                  <span class="detail-label">{{ $t('inspectionPlanDetail.status') }}：</span>
                  <span>{{ inspectionPlanDetailInfo.stateName }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
    </el-card>

    <el-card class="box-card margin-top">
      <div class="content-wrapper">
        <el-tabs v-model="inspectionPlanDetailInfo._currentTab" @tab-click="changeTab(inspectionPlanDetailInfo._currentTab)">
          <el-tab-pane :label="$t('inspectionPlanDetail.staff')" name="inspectionPlanDetailStaff">
            <inspection-plan-detail-staff ref="inspectionPlanDetailStaff" />
          </el-tab-pane>
          <el-tab-pane :label="$t('inspectionPlanDetail.route')" name="pointRoute">
            <point-route ref="pointRoute" />
          </el-tab-pane>
          <el-tab-pane :label="$t('inspectionPlanDetail.point')" name="inspectionRoutePoint">
            <inspection-route-point ref="inspectionRoutePoint" />
          </el-tab-pane>
          <el-tab-pane :label="$t('inspectionPlanDetail.task')" name="routeTask">
            <route-task ref="routeTask" />
          </el-tab-pane>
          <el-tab-pane :label="$t('inspectionPlanDetail.detail')" name="pointTaskDetail">
            <point-task-detail ref="pointTaskDetail" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import InspectionPlanDetailStaff from '@/components/inspection/InspectionPlanDetailStaff'
import PointRoute from '@/components/inspection/pointRoute'
import InspectionRoutePoint from '@/components/inspection/InspectionRoutePoint'
import RouteTask from '@/components/inspection/RouteTask'
import PointTaskDetail from '@/components/inspection/pointTaskDetail'
import { getInspectionPlanDetail } from '@/api/inspection/inspectionPlanDetailApi'

export default {
  name: 'InspectionPlanDetailList',
  components: {
    InspectionPlanDetailStaff,
    PointRoute,
    InspectionRoutePoint,
    RouteTask,
    PointTaskDetail
  },
  data() {
    return {
      inspectionPlanDetailInfo: {
        beforeTime: '',
        canReexamine: '',
        createTime: '',
        createUserId: '',
        createUserName: '',
        endDate: '',
        endTime: '',
        inspectionDay: '',
        inspectionMonth: '',
        inspectionPlanId: '',
        inspectionPlanName: '',
        inspectionPlanPeriod: '',
        inspectionPlanPeriodName: '',
        inspectionRouteId: '',
        inspectionRouteName: '',
        inspectionWorkday: '',
        remark: '',
        signType: '',
        signTypeName: '',
        startDate: '',
        startTime: '',
        state: '',
        stateName: '',
        total: 0,
        records: 1,
        moreCondition: false,
        inspectionName: '',
        pointObjTypes: [],
        _currentTab: 'inspectionPlanDetailStaff'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.inspectionPlanDetailInfo.inspectionPlanId = this.$route.query.inspectionPlanId
    this.getInspectionPlanDetail()
  },
  methods: {
    async getInspectionPlanDetail() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          inspectionPlanId: this.inspectionPlanDetailInfo.inspectionPlanId
        }
        const { data } = await getInspectionPlanDetail(params)
        Object.assign(this.inspectionPlanDetailInfo, data[0])
        this.changeTab(this.inspectionPlanDetailInfo._currentTab)
      } catch (error) {
        console.error('获取巡检计划详情失败:', error)
      }
    },
    changeTab(tab) {
      this.inspectionPlanDetailInfo._currentTab = tab
      setTimeout(() => {
        const componentRef = this.$refs[`${tab}`]
        if (componentRef && componentRef.open) {
          componentRef.open({
            inspectionPlanId: this.inspectionPlanDetailInfo.inspectionPlanId,
            inspectionRouteId: this.inspectionPlanDetailInfo.inspectionRouteId
          })
        }
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-plan-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .content-wrapper {
      padding: 0;
    }
  }

  .detail-wrapper {
    .detail-item {
      padding: 8px 0;
      font-size: 14px;
      color: #606266;
    }

    .detail-label {
      margin-right: 4px;
    }
  }

  .margin-top {
    margin-top: 15px;
  }
}
</style>