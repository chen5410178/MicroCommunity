<template>
  <div class="aInspectionPlanDetail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('aInspectionPlanDetail.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.planName') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.inspectionPlanName }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.planRoute') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.inspectionRouteName }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.planPeriod') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.inspectionPlanPeriodName }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.signType') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.signTypeName }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.dateRange') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.startDate }}~{{ aInspectionPlanDetailInfo.endDate }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.timeRange') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.startTime }}~{{ aInspectionPlanDetailInfo.endTime }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.taskAdvance') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.beforeTime }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.creator') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.createUserName }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.createTime') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.createTime }}</label>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-group">
            <label class="col-form-label">{{ $t('aInspectionPlanDetail.status') }}：</label>
            <label>{{ aInspectionPlanDetailInfo.stateName }}</label>
          </div>
        </el-col>
      </el-row>
      <divider></divider>
      <div class="margin-top-sm">
        <el-tabs v-model="aInspectionPlanDetailInfo._currentTab"
          @tab-click="changeTab(aInspectionPlanDetailInfo._currentTab)">
          <el-tab-pane :label="$t('aInspectionPlanDetail.staff')" name="aInspectionPlanDetailStaff">
            <a-inspection-plan-detail-staff ref="aInspectionPlanDetailStaff" />
          </el-tab-pane>
          <el-tab-pane :label="$t('aInspectionPlanDetail.route')" name="adminPointRoute">
            <admin-point-route ref="adminPointRoute" />
          </el-tab-pane>
          <el-tab-pane :label="$t('aInspectionPlanDetail.point')" name="aInspectionRoutePoint">
            <a-inspection-route-point ref="aInspectionRoutePoint" />
          </el-tab-pane>
          <el-tab-pane :label="$t('aInspectionPlanDetail.task')" name="adminRouteTask">
            <admin-route-task ref="adminRouteTask" />
          </el-tab-pane>
          <el-tab-pane :label="$t('aInspectionPlanDetail.detail')" name="adminPointTaskDetail">
            <admin-point-task-detail ref="adminPointTaskDetail" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getInspectionPlanDetail } from '@/api/inspection/aInspectionPlanDetailApi'
import AInspectionPlanDetailStaff from '@/components/inspection/AInspectionPlanDetailStaff'
import AdminPointRoute from '@/components/inspection/AdminPointRoute'
import AInspectionRoutePoint from '@/components/inspection/AInspectionRoutePoint'
import AdminRouteTask from '@/components/inspection/AdminRouteTask'
import AdminPointTaskDetail from '@/components/inspection/AdminPointTaskDetail'
import divider from '@/components/system/divider'

export default {
  name: 'AInspectionPlanDetailList',
  components: {
    AInspectionPlanDetailStaff,
    AdminPointRoute,
    AInspectionRoutePoint,
    AdminRouteTask,
    AdminPointTaskDetail,
    divider
  },
  data() {
    return {
      aInspectionPlanDetailInfo: {
        beforeTime: '',
        createTime: '',
        createUserId: '',
        createUserName: '',
        endDate: '',
        endTime: '',
        inspectionPlanId: '',
        inspectionPlanName: '',
        inspectionPlanPeriod: '',
        inspectionPlanPeriodName: '',
        inspectionRouteId: '',
        inspectionRouteName: '',
        signType: '',
        signTypeName: '',
        startDate: '',
        startTime: '',
        state: '',
        stateName: '',
        _currentTab: 'aInspectionPlanDetailStaff'
      }
    }
  },
  created() {
    this.aInspectionPlanDetailInfo.inspectionPlanId = this.$route.query.inspectionPlanId
    this.getInspectionPlanDetail()
  },
  methods: {
    async getInspectionPlanDetail() {
      try {
        const params = {
          inspectionPlanId: this.aInspectionPlanDetailInfo.inspectionPlanId,
          page: 1,
          row: 1
        }
        const { data } = await getInspectionPlanDetail(params)
        if (data && data.length > 0) {
          Object.assign(this.aInspectionPlanDetailInfo, data[0])
          this.changeTab(this.aInspectionPlanDetailInfo._currentTab)
        }
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    changeTab(tab) {
      this.aInspectionPlanDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].loadData({
            inspectionPlanId: this.aInspectionPlanDetailInfo.inspectionPlanId,
            inspectionRouteId: this.aInspectionPlanDetailInfo.inspectionRouteId
          })
        }
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.aInspectionPlanDetail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .form-group {
    margin-bottom: 15px;
    text-align: left;
    font-size: 15px;

    .col-form-label {
      margin-right: 10px;
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }
}
</style>