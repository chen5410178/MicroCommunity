<template>
  <div class="inspection-plan-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('inspectionPlanDetail.title') }}</span>
      </div>
      <div class="content-wrapper">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.planName') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.inspectionPlanName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.planRoute') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.inspectionRouteName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.planPeriod') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.inspectionPlanPeriodName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.signType') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.signTypeName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.dateRange') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.startDate }}~{{ inspectionPlanDetailInfo.endDate }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.timeRange') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.startTime }}~{{ inspectionPlanDetailInfo.endTime }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.beforeTime') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.beforeTime }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.creator') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.createUserName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.createTime') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.createTime }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">
                {{ $t('inspectionPlanDetail.status') }}:
              </label>
              <label>{{ inspectionPlanDetailInfo.stateName }}</label>
            </div>
          </el-col>
        </el-row>
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
      padding: 20px;

      .form-group {
        margin-bottom: 15px;
        display: flex;
        align-items: center;

        .col-form-label {
          margin-right: 10px;
          min-width: 100px;
        }
      }
    }
  }

  .margin-top {
    margin-top: 20px;
  }
}
</style>