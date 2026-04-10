<template>
  <div id="aInspectionTaskMap" class="map-container"></div>
</template>

<script>
import { queryAdminInspectionTaskDetail } from '@/api/inspection/adminInspectionTaskDetailApi'

export default {
  name: 'AInspectionTaskMap',
  data() {
    return {
      map: null,
      points: []
    }
  },
  methods: {
    loadData(params) {
      console.log(params)
      //this.initMap(params)
    },
    async initMap(params) {
      try {
        const { data } = await queryAdminInspectionTaskDetail({
          taskId: params.taskId,
          page: 1,
          row: 1000
        })
        this.points = data
        this.initMapView()
      } catch (error) {
        console.error(error)
        this.$message.error(error)
      }
    },
    initMapView() {
      if (!this.points || this.points.length === 0) return

      // 这里需要根据实际地图API实现地图初始化
      // 示例代码，需要替换为实际地图API调用
      console.log('Initialize task map with points:', this.points)
      
      // 示例：使用腾讯地图API
      if (window.TMap) {
        const center = new window.TMap.LatLng(this.points[0].lat, this.points[0].lng)
        this.map = new window.TMap.Map(document.getElementById('aInspectionTaskMap'), {
          center: center,
          zoom: 17
        })
        
        // 添加标记点
        const markers = this.points.map(point => ({
          position: new window.TMap.LatLng(point.lat, point.lng),
          properties: {
            title: point.inspectionName,
            status: point.state
          }
        }))
        
        new window.TMap.MultiMarker({
          map: this.map,
          styles: {
            normal: new window.TMap.MarkerStyle({
              width: 25,
              height: 35,
              src: '/img/inspection.png',
              anchor: { x: 16, y: 32 }
            }),
            done: new window.TMap.MarkerStyle({
              width: 25,
              height: 35,
              src: '/img/inspection_done.png',
              anchor: { x: 16, y: 32 }
            })
          },
          geometries: markers.map(marker => ({
            ...marker,
            styleId: marker.properties.status === '20200405' ? 'done' : 'normal'
          }))
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