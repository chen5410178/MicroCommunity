<template>
  <div id="aInspectionRouteMap" class="map-container"></div>
</template>

<script>
import { listAdminInspectionRoutePoints } from '@/api/inspection/aInspectionPlanDetailApi'

export default {
  name: 'AInspectionRouteMap',
  data() {
    return {
      map: null,
      points: []
    }
  },
 async created() {
   await this.loadQQMapScript()
  },
  methods: {
    loadQQMapScript() {
      if (window.TMap) {
        return Promise.resolve();
      }
      
      return new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = `https://map.qq.com/api/gljs?v=1.exp&key=您的腾讯地图KEY`;
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
      });
    },
    async initMap(params) {
      try {
        const { data } = await listAdminInspectionRoutePoints({
          inspectionRouteId: params.inspectionRouteId,
          page: 1,
          row: 1000
        })
        this.points = data
        this.initMapView()
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    initMapView() {
      if (!this.points || this.points.length === 0) return

      // 这里需要根据实际地图API实现地图初始化
      // 示例代码，需要替换为实际地图API调用
      console.log('Initialize map with points:', this.points)

      // 示例：使用腾讯地图API
      if (window.TMap) {
        const center = new window.TMap.LatLng(this.points[0].lat, this.points[0].lng)
        this.map = new window.TMap.Map(document.getElementById('aInspectionRouteMap'), {
          center: center,
          zoom: 17
        })

        // 添加标记点
        const markers = this.points.map(point => ({
          position: new window.TMap.LatLng(point.lat, point.lng),
          properties: {
            title: point.inspectionName
          }
        }))

        new window.TMap.MultiMarker({
          map: this.map,
          geometries: markers
        })
      }
    }
  }
}
</script>

<style scoped>
.map-container {
  height: 600px;
  width: 100%;
}
</style>