<template>
  <el-dialog
    :title="$t('common.map')"
    :visible.sync="viewMapInfo.showMap"
    width="80%"
    :before-close="closeMap"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
  >
    <div class="map-container">
      <div id="qqmapcontainer" style="width: 100%; height: 500px; background-color: antiquewhite;"></div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ViewMap',
  data() {
    return {
      viewMapInfo: {
        lat: '',
        lng: '',
        showMap: false,
        map: null
      }
    }
  },
  methods: {
    open(param) {
      this.viewMapInfo.showMap = true

      if (param.lng && param.lat) {
        const lngLat = this.wgs84togcj02(param.lng, param.lat)
        this.viewMapInfo.lat = lngLat.lat
        this.viewMapInfo.lng = lngLat.lon
      } else {
        this.viewMapInfo.lat = 36.72
        this.viewMapInfo.lng = 100.985
      }

      this.$nextTick(() => this.initMap())
    },
    initMap() {
      const center = new window.TMap.LatLng(this.viewMapInfo.lat, this.viewMapInfo.lng)
      const container = document.getElementById('qqmapcontainer')
      this.viewMapInfo.map = new window.TMap.Map(container, {
        center,
        zoom: 17.2,
        viewMode: '2D'
      })
      new window.TMap.MultiMarker({
        id: 'marker-layer',
        map: this.viewMapInfo.map,
        styles: {
          marker: new window.TMap.MarkerStyle({
            width: 25,
            height: 35,
            anchor: { x: 16, y: 32 },
            src: 'https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/markerDefault.png'
          })
        },
        geometries: [{
          id: 'demo',
          styleId: 'marker',
          position: new window.TMap.LatLng(this.viewMapInfo.lat, this.viewMapInfo.lng),
          properties: { title: 'marker' }
        }]
      })
    },
    closeMap() {
      this.viewMapInfo.showMap = false
      if (this.viewMapInfo.map) {
        this.viewMapInfo.map.destroy()
        this.viewMapInfo.map = null
      }
    },
    wgs84togcj02(lng, lat) {
      // Placeholder for coordinate conversion
      return { lon: lng, lat }
    }
  }
}
</script>

<style lang="scss" scoped>
.map-container {
  width: 100%;
  height: 500px;
}
</style>