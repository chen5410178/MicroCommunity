<template>
  <div class="point-route-container" >
    <el-row :gutter="20" v-if="pointRouteInfo.routes && pointRouteInfo.routes.length>0">
      <el-col :span="6">
        <el-card class="route-list-card">
          <div class="route-list">
            <ul>
              <li v-for="(route, index) in pointRouteInfo.routes" :key="index" @click="_switchPointRoute(route)"
                :class="{ 'active': route.inspectionRouteId == pointRouteInfo.inspectionRouteId }">
                {{ route.routeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <inspection-route-map ref="inspectionRouteMap" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listInspectionRoutes } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'
import InspectionRouteMap from '@/components/inspection/inspectionRouteMap'

export default {
  name: 'PointRoute',
  components: {
    InspectionRouteMap
  },
  data() {
    return {
      pointRouteInfo: {
        routes: [],
        inspectionRouteId: '',
        inspectionId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.pointRouteInfo.inspectionId = params.inspectionId
      this.pointRouteInfo.inspectionRouteId = params.inspectionRouteId
      this._loadPointRouteData()
    },
    loadData(point) {
      if (!point) return

      this.pointRouteInfo.inspectionId = point.inspectionId
      this.pointRouteInfo.inspectionRouteId = point.inspectionRouteId
      this._loadPointRouteData()
    },
    async _loadPointRouteData() {
      try {
        const params = {
          communityId: this.communityId,
          inspectionId: this.pointRouteInfo.inspectionId,
          inspectionRouteId: this.pointRouteInfo.inspectionRouteId,
          page: 1,
          row: 100
        }

        const response = await listInspectionRoutes(params)
        this.pointRouteInfo.routes = response.inspectionRoutes

        if (response.inspectionRoutes && response.inspectionRoutes.length > 0) {
          this._switchPointRoute(response.inspectionRoutes[0])
        }
      } catch (error) {
        console.error('获取巡检路线失败:', error)
        this.$message.error(this.$t('pointRoute.fetchError'))
      }
    },
    _switchPointRoute(route) {
      this.pointRouteInfo.inspectionRouteId = route.inspectionRouteId

      // 通知地图组件加载路线
      this.$nextTick(() => {
        if (this.$refs.inspectionRouteMap) {
          this.$refs.inspectionRouteMap.loadData(route)
        }
      })
    }
  }
}
</script>

<style scoped>
.point-route-container {
  padding: 20px;
}

.route-list-card {
  height: 600px;
  overflow: hidden;
}

.route-list {
  height: 550px;
  overflow-y: auto;
}

.route-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.route-list li {
  padding: 12px 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.route-list li:hover {
  background-color: #f5f7fa;
}

.route-list li.active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: bold;
}
</style>