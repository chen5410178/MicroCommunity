<template>
  <div class="inspection-route-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="route-card">
          <div style="display: flex; align-items: center; margin-bottom: 12px; gap: 6px;">
            <el-input v-model="inspectionRouteInfo.conditions.routeName"
              :placeholder="$t('inspectionRoute.routeNamePlaceholder')"
              size="small"
              clearable
              style="flex: 1;"
            />
            <el-button type="text" size="small" @click="_queryInspectionRouteMethod">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" size="small" @click="_openAddInspectionRouteModal">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="route-list-wrapper">
            <ul class="route-ul">
              <li v-for="(route, index) in inspectionRouteInfo.inspectionRoutes" :key="index"
                :class="{
                  'selected': route.inspectionRouteId == inspectionRouteInfo.route.inspectionRouteId
                }"
                class="route-list-item"
                @click="_switchInspectionRoute(route)">
                {{ route.routeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-row v-if="inspectionRouteInfo.route.inspectionRouteId">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('inspectionRoute.routeInfo') }}</span>
                <div class="ibox-tools">
                  <el-button size="small" @click="_openEditInspectionRouteModel(inspectionRouteInfo.route)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="small" type="danger" @click="_openDeleteInspectionRouteModel(inspectionRouteInfo.route)">
                    {{ $t('common.delete') }}
                  </el-button>
                </div>
              </div>

              <div class="detail-wrapper">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionRoute.routeName') }}：</span>
                      <span>{{ inspectionRouteInfo.route.routeName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionRoute.sequence') }}：</span>
                      <span>{{ inspectionRouteInfo.route.seq }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionRoute.createTime') }}：</span>
                      <span>{{ inspectionRouteInfo.route.createTime }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionRoute.remark') }}：</span>
                      <span>{{ inspectionRouteInfo.route.remark }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row v-if="inspectionRouteInfo.route.inspectionRouteId" class="margin-top">
          <el-col :span="24">
            <el-card>
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
          if (this.inspectionRouteInfo.route.inspectionRouteId) {
            var found = data.inspectionRoutes.find(function (r) {
              return r.inspectionRouteId === this.inspectionRouteInfo.route.inspectionRouteId
            }.bind(this))
            if (found) {
              this.inspectionRouteInfo.route = found
              this.changeTab(this.inspectionRouteInfo._currentTab)
            } else {
              this._switchInspectionRoute(data.inspectionRoutes[0])
            }
          } else {
            this._switchInspectionRoute(data.inspectionRoutes[0])
          }
        } else {
          this.inspectionRouteInfo.route = {}
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
}

.margin-top {
  margin-top: 15px;
}

.left-panel {
  .route-card {
    height: 700px;

    .route-list-wrapper {
      height: 630px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .route-ul {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .route-list-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }
}

.right-panel {
  padding-left: 5px;
}

.ibox-tools {
  float: right;
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
</style>