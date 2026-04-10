<template>
  <div class="inspection-route-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="search-card">
          <div class="flex justify-start">
            <el-input v-model="inspectionRouteInfo.conditions.routeName"
              :placeholder="$t('inspectionRoute.routeNamePlaceholder')" class="search-input" clearable />
            <el-button type="text" class="margin-left-xs" size="small" @click="_queryInspectionRouteMethod">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" class="margin-left-xs" size="small" @click="_openAddInspectionRouteModal">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="route-list margin-top">
            <ul class="route-ul">
              <li v-for="(route, index) in inspectionRouteInfo.inspectionRoutes" :key="index" :class="{
                'active-route':
                  route.inspectionRouteId ==
                  inspectionRouteInfo.route.inspectionRouteId
              }" @click="_switchInspectionRoute(route)">
                {{ route.routeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="route-detail-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('inspectionRoute.routeInfo') }}</span>
            <div class="ibox-tools">
              <el-button size="mini" @click="_openEditInspectionRouteModel(inspectionRouteInfo.route)">
                {{ $t('common.edit') }}
              </el-button>
              <el-button size="mini" type="danger" @click="_openDeleteInspectionRouteModel(inspectionRouteInfo.route)">
                {{ $t('common.delete') }}
              </el-button>
            </div>
          </div>

          <el-row class="text-left">
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('inspectionRoute.routeName') }}:
                </label>
                <label>{{ inspectionRouteInfo.route.routeName }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('inspectionRoute.sequence') }}:
                </label>
                <label>{{ inspectionRouteInfo.route.seq }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('inspectionRoute.createTime') }}:
                </label>
                <label>{{ inspectionRouteInfo.route.createTime }}</label>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('inspectionRoute.remark') }}:
                </label>
                <label>{{ inspectionRouteInfo.route.remark }}</label>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="tab-card margin-top">
          <el-tabs v-model="inspectionRouteInfo._currentTab" @tab-click="changeTab(inspectionRouteInfo._currentTab)">
            <el-tab-pane label="巡检点" name="inspectionRoutePoint">
              <inspection-route-point ref="inspectionRoutePoint" />
            </el-tab-pane>
            <el-tab-pane label="巡检地图" name="inspectionRouteMap">
              <inspection-route-map ref="inspectionRouteMap" />
            </el-tab-pane>
            <el-tab-pane label="巡检计划" name="routePlan">
              <route-plan ref="routePlan" />
            </el-tab-pane>
            <el-tab-pane label="巡检任务" name="routeTask">
              <route-task ref="routeTask" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <add-inspection-route ref="addInspectionRoute" @success="handleSuccess" />
    <edit-inspection-route ref="editInspectionRoute" @success="handleSuccess" />
    <delete-inspection-route ref="deleteInspectionRoute" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  listInspectionRoutes,

} from '@/api/inspection/inspectionRouteApi'
import InspectionRoutePoint from '@/components/inspection/InspectionRoutePoint'
import InspectionRouteMap from '@/components/inspection/inspectionRouteMap'
import RoutePlan from '@/components/inspection/RoutePlan'
import RouteTask from '@/components/inspection/RouteTask'
import AddInspectionRoute from '@/components/inspection/AddInspectionRoute'
import EditInspectionRoute from '@/components/inspection/EditInspectionRoute'
import DeleteInspectionRoute from '@/components/inspection/DeleteInspectionRoute'

export default {
  name: 'InspectionRouteList',
  components: {
    InspectionRoutePoint,
    InspectionRouteMap,
    RoutePlan,
    RouteTask,
    AddInspectionRoute,
    EditInspectionRoute,
    DeleteInspectionRoute
  },
  data() {
    return {
      inspectionRouteInfo: {
        inspectionRoutes: [],
        route: {},
        _currentTab: 'inspectionRoutePoint',
        conditions: {
          routeName: '',
          inspectionRouteId: '',
          seq: ''
        }
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listInspectionRoutes(1, 100)
  },
  methods: {
    async _listInspectionRoutes(page, rows) {
      try {
        const params = {
          page,
          row: rows,
          communityId: this.communityId,
          ...this.inspectionRouteInfo.conditions
        }
        const data = await listInspectionRoutes(params)
        this.inspectionRouteInfo.inspectionRoutes = data.inspectionRoutes
        if (data.inspectionRoutes && data.inspectionRoutes.length > 0) {
          this._switchInspectionRoute(data.inspectionRoutes[0])
        }
      } catch (error) {
        console.error('获取巡检路线列表失败:', error)
      }
    },
    _openAddInspectionRouteModal() {
      this.$refs.addInspectionRoute.open()
    },
    _openEditInspectionRouteModel(route) {
      this.$refs.editInspectionRoute.open(route)
    },
    _openDeleteInspectionRouteModel(route) {
      this.$refs.deleteInspectionRoute.open(route)
    },
    _queryInspectionRouteMethod() {
      this._listInspectionRoutes(1, 100)
    },
    _switchInspectionRoute(route) {
      this.inspectionRouteInfo.route = route
      this.changeTab(this.inspectionRouteInfo._currentTab)
    },
    changeTab(tab) {
      this.inspectionRouteInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].loadData(this.inspectionRouteInfo.route)
        }
      }, 500)
    },
    handleSuccess() {
      this._listInspectionRoutes(1, 100)
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-route-container {
  padding: 20px;
  height: 100%;

  .left-panel {
    height: 100%;

    .search-card {
      height: 100%;

      .search-input {
        width: 60%;
      }

      .route-list {
        height: calc(100% - 60px);
        overflow-y: auto;

        .route-ul {
          list-style: none;
          padding: 0;
          margin: 0;

          li {
            padding: 10px;
            cursor: pointer;

            &:hover {
              background-color: #f5f5f5;
            }

            &.active-route {
              background-color: #409eff;
              color: white;
            }
          }
        }
      }
    }
  }

  .right-panel {
    height: 100%;

    .route-detail-card {
      margin-bottom: 20px;
    }

    .tab-card {
      height: calc(100% - 180px);
    }
  }

  .margin-top {
    margin-top: 10px;
  }

  .margin-left-xs {
    margin-left: 10px;
  }
}
</style>