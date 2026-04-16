<template>
    <div class="admin-inspection-task-detail-container">
        <el-card class="box-card">
            <div slot="header" class="clearfix flex justify-between">
                <span>{{ $t('adminInspectionTaskDetail.title') }}</span>
            </div>
            <div class="card-content">
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.taskId') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.taskId }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.inspectionPlanName') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.inspectionPlanName }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.planTime') }}：
                            </label>
                            <label>
                                {{ adminInspectionTaskDetailInfo.planInsTime }}<br>~{{
                                    adminInspectionTaskDetailInfo.planEndTime }}
                            </label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.actInsTime') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.actInsTime || '-' }}</label>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.originalPlanUser') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.originalPlanUserName || '-' }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.currentPlanUser') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.planUserName }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.transferDesc') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.transferDesc || '-' }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.signType') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.signTypeName }}</label>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="form-group">
                            <label class="col-form-label">
                                {{ $t('adminInspectionTaskDetail.state') }}：
                            </label>
                            <label>{{ adminInspectionTaskDetailInfo.stateName }}</label>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </el-card>

        <el-card class="box-card margin-top">
            <div class="card-content">
                <el-tabs v-model="adminInspectionTaskDetailInfo._currentTab"
                    @tab-click="handleTabClick(adminInspectionTaskDetailInfo._currentTab)">
                    <el-tab-pane :label="$t('adminInspectionTaskDetail.tab.detail')" name="adminPointTaskDetail">
                        <admin-point-task-detail ref="adminPointTaskDetail" />
                    </el-tab-pane>
                    <el-tab-pane :label="$t('adminInspectionTaskDetail.tab.map')" name="aInspectionTaskMap">
                        <a-inspection-task-map ref="aInspectionTaskMap" />
                    </el-tab-pane>
                    <el-tab-pane :label="$t('adminInspectionTaskDetail.tab.plan')" name="adminRoutePlan">
                        <admin-route-plan ref="adminRoutePlan" />
                    </el-tab-pane>
                    <el-tab-pane :label="$t('adminInspectionTaskDetail.tab.route')" name="adminPointRoute">
                        <admin-point-route ref="adminPointRoute" />
                    </el-tab-pane>
                    <el-tab-pane :label="$t('adminInspectionTaskDetail.tab.point')" name="aInspectionRoutePoint">
                        <a-inspection-route-point ref="aInspectionRoutePoint" />
                    </el-tab-pane>
                </el-tabs>
            </div>
        </el-card>
    </div>
</template>
  
<script>
import AdminPointTaskDetail from '@/components/inspection/AdminPointTaskDetail'
import AInspectionTaskMap from '@/components/inspection/AInspectionTaskMap'
import AdminRoutePlan from '@/components/inspection/AdminRoutePlan'
import AdminPointRoute from '@/components/inspection/AdminPointRoute'
import AInspectionRoutePoint from '@/components/inspection/AInspectionRoutePoint'
import { getAdminInspectionTaskDetail } from '@/api/inspection/adminInspectionTaskDetailApi'

export default {
    name: 'AdminInspectionTaskDetailList',
    components: {
        AdminPointTaskDetail,
        AInspectionTaskMap,
        AdminRoutePlan,
        AdminPointRoute,
        AInspectionRoutePoint
    },
    data() {
        return {
            adminInspectionTaskDetailInfo: {
                inspectionPlanId: '',
                inspectionPlanName: '',
                originalPlanUserId: '',
                originalPlanUserName: '',
                planEndTime: '',
                planInsTime: '',
                planUserId: '',
                planUserName: '',
                signType: '',
                signTypeName: '',
                state: '',
                stateName: '',
                taskId: '',
                taskType: '',
                inspectionRouteId: '',
                total: 0,
                records: 1,
                moreCondition: false,
                inspectionName: '',
                pointObjTypes: [],
                _currentTab: 'adminPointTaskDetail'
            }
        }
    },
    created() {
        this.adminInspectionTaskDetailInfo.taskId = this.$route.query.taskId
        this.loadAdminInspectionTaskDetail()
    },
    methods: {
        async loadAdminInspectionTaskDetail() {
            try {
                const params = {
                    page: 1,
                    row: 1,
                    taskId: this.adminInspectionTaskDetailInfo.taskId
                }
                const { inspectionTasks } = await getAdminInspectionTaskDetail(params)
                Object.assign(this.adminInspectionTaskDetailInfo, inspectionTasks[0])
                this.handleTabClick(this.adminInspectionTaskDetailInfo._currentTab)
            } catch (error) {
                this.$message.error(this.$t('common.fetchError'))
            }
        },
        handleTabClick(tab) {
            this.adminInspectionTaskDetailInfo._currentTab = tab
            setTimeout(() => {
                if (this.$refs[tab]) {
                    this.$refs[tab].loadData({
                        taskId: this.adminInspectionTaskDetailInfo.taskId,
                        inspectionPlanId: this.adminInspectionTaskDetailInfo.inspectionPlanId,
                        inspectionRouteId: this.adminInspectionTaskDetailInfo.inspectionRouteId
                    })
                }
            }, 500)
        }
    }
}
</script>
  
<style lang="scss" scoped>
.admin-inspection-task-detail-container {
    padding: 20px;

    .box-card {
        margin-bottom: 20px;

        .card-content {
            padding: 20px;
        }
    }

    .form-group {
        margin-bottom: 15px;
        text-align: left;

        .col-form-label {
            margin-right: 5px;
        }
    }

    .margin-top {
        margin-top: 20px;
    }
}
</style>