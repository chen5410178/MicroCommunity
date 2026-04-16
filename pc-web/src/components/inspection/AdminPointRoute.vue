<template>
  <div class="route-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="route-list">
          <el-scrollbar>
            <ul class="route-ul">
              <li v-for="(route, index) in routes" :key="index"
                :class="{ 'active': route.inspectionRouteId === currentRouteId }" @click="switchRoute(route)">
                {{ route.routeName }}
              </li>
            </ul>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="18">
        <a-inspection-route-map ref="routeMap" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminInspectionRoutes } from '@/api/inspection/aInspectionPlanDetailApi'
import AInspectionRouteMap from './AInspectionRouteMap'

export default {
  name: 'AdminPointRoute',
  components: {
    AInspectionRouteMap
  },
  data() {
    return {
      loading: false,
      routes: [],
      currentRouteId: '',
      inspectionId: ''
    }
  },
  methods: {
    async loadData(params) {
      this.inspectionId = params.inspectionPlanId
      await this.getRouteList()
    },
    async getRouteList() {
      try {
        this.loading = true
        const params = {
          inspectionId: this.inspectionId,
          page: 1,
          row: 100
        }
        const { inspectionRoutes } = await listAdminInspectionRoutes(params)
        this.routes = inspectionRoutes
        if (this.routes.length > 0) {
          this.switchRoute(this.routes[0])
        }
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    switchRoute(route) {
      this.currentRouteId = route.inspectionRouteId
      this.$refs.routeMap.initMap({
        inspectionRouteId: route.inspectionRouteId
      })
    }
  }
}
</script>

<style scoped>
.route-container {
  padding: 20px;
}

.route-list {
  height: 600px;
}

.route-ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.route-ul li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.route-ul li:hover {
  background-color: #f5f5f5;
}

.route-ul li.active {
  background-color: #409EFF;
  color: white;
}
</style>