<template>
  <div class="inspection-task-map-container">
    <div id="inspectionTaskMap" style="width: 100%; height: 600px;"></div>
  </div>
</template>

<script>
import { queryInspectionTaskDetail } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InspectionTaskMap',
  data() {
    return {
      map: null,
      communityId: '',
      taskPoints: [],
      markerLayer: null,
      labelLayer: null,
      polylineLayer: null
    }
  },
  mounted() {
    this.communityId = getCommunityId()
    // 延迟初始化地图，确保DOM已经渲染
  },
  methods: {
    loadData(task) {
      console.log('loadData',task)
      this.initMap()
      this.loadTask(task)
    },
    initMap() {
      // 若已有地图或图层，先清理避免重复初始化
      if (this.markerLayer) {
        this.markerLayer.setMap(null)
        this.markerLayer = null
      }
      if (this.labelLayer) {
        this.labelLayer.setMap(null)
        this.labelLayer = null
      }
      if (this.polylineLayer) {
        this.polylineLayer.setMap(null)
        this.polylineLayer = null
      }
      if (this.map) {
        this.map.destroy()
        this.map = null
      }
      console.log('initMap',this.map)

      // 默认中心点（北京）
      const center = new window.TMap.LatLng(39.916527, 116.397128)
      console.log('center',center)

      // 创建地图实例
      this.map = new window.TMap.Map(document.getElementById('inspectionTaskMap'), {
        center: center,
        zoom: 13,
        baseMap: {
          type: 'vector',
          features: ['base', 'building2d', 'point']
        }
      })
    },

    async loadTask(task) {
      if (!task || !task.taskId) return

      try {
        const params = {
          communityId: this.communityId,
          taskId: task.taskId,
          page: 1,
          row: 1000
        }

        const response = await queryInspectionTaskDetail(params)
        this.taskPoints = response.data
        this.addPointsToMap(this.taskPoints)
      } catch (error) {
        console.error('获取任务点失败:', error)
        this.$message.error(this.$t('inspectionTaskMap.fetchError'))
      }
    },

    addPointsToMap(points) {
      if (!this.map || !points || points.length === 0) return

      // 清除之前的标记
      if (this.markerLayer) this.markerLayer.setMap(null)
      if (this.labelLayer) this.labelLayer.setMap(null)
      if (this.polylineLayer) this.polylineLayer.setMap(null)

      const geometriesMarker = []
      const geometriesLabel = []
      const path = []

      points.forEach(point => {
        if (!point.lat || !point.lng) return

        const position = new window.TMap.LatLng(point.lat, point.lng)

        // 确定状态对应的样式
        const markerStyle = point.state === '20200405' ? 'myStyle_red' : 'myStyle'
        const labelStyle = point.state === '20200405' ? 'label_red' : 'label'

        // 添加标记
        geometriesMarker.push({
          id: point.inspectionId,
          styleId: markerStyle,
          position: position
        })

        // 添加标签
        geometriesLabel.push({
          id: `label_${point.inspectionId}`,
          styleId: labelStyle,
          position: position,
          content: point.inspectionName
        })

        // 添加路径点
        path.push(position)
      })

      // 创建标记图层
      this.markerLayer = new window.TMap.MultiMarker({
        map: this.map,
        styles: {
          "myStyle": new window.TMap.MarkerStyle({
            width: 25,
            height: 35,
            src: '/img/inspection.png',
            anchor: { x: 32, y: 32 }
          }),
          "myStyle_red": new window.TMap.MarkerStyle({
            width: 25,
            height: 35,
            src: '/img/inspection_do.png',
            anchor: { x: 32, y: 32 }
          })
        },
        geometries: geometriesMarker
      })

      // 创建标签图层
      this.labelLayer = new window.TMap.MultiLabel({
        map: this.map,
        styles: {
          'label': new window.TMap.LabelStyle({
            color: '#3777FF',
            size: 20,
            offset: { x: 0, y: 15 },
            angle: 0,
            alignment: 'center',
            verticalAlignment: 'middle'
          }),
          'label_red': new window.TMap.LabelStyle({
            color: '#CC0000',
            size: 20,
            offset: { x: 0, y: 15 },
            angle: 0,
            alignment: 'center',
            verticalAlignment: 'middle'
          })
        },
        geometries: geometriesLabel
      })

      // 创建路径图层
      this.polylineLayer = new window.TMap.MultiPolyline({
        map: this.map,
        styles: {
          'style_blue': new window.TMap.PolylineStyle({
            color: '#3777FF',
            width: 6,
            borderWidth: 5,
            borderColor: '#FFF',
            lineCap: 'butt'
          }),
          'style_red': new window.TMap.PolylineStyle({
            color: '#CC0000',
            width: 6,
            borderWidth: 5,
            borderColor: '#FFF',
            lineCap: 'butt'
          })
        },
        geometries: [
          {
            id: 'pl_1',
            styleId: 'style_blue',
            paths: path
          },
          {
            id: 'pl_2',
            styleId: 'style_red',
            paths: path
          }
        ]
      })

      // 如果有路径点，调整地图中心点和缩放级别
      if (path.length > 0) {
        this.map.setCenter(path[0])
        this.map.setZoom(16)
      }
    }
  },
  beforeDestroy() {
    // 组件销毁时清除地图实例
    if (this.map) {
      this.map.destroy()
      this.map = null
    }
  }
}
</script>

<style scoped>
.inspection-task-map-container {
  width: 100%;
  height: 100%;
}
</style>