<template>
  <div class="select-map-pos-container">
    <div id="selectMap" style="width: 100%; height: 100%;"></div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SelectMapPos',
  props: {
    elementId: {
      type: String,
      default: 'selectMap'
    },
    initData: {
      type: Object,
      default: () => ({})
    }
  },

  data() {
    return {
      map: null,
      marker: null,
      communityId: '',
      mapData: {
        lat: 0,
        lng: 0
      }
    }
  },
  mounted() {
    this.communityId = getCommunityId()

  },
  methods: {
    initMap(_data) {
      // 清除现有地图
      let _lng = 116.397128;
      let _lat = 39.916527;

      if (_data.lng) {
        _lng = _data.lng;
      }

      if (_data.lat) {
        _lat = _data.lat;
      }
      try {
        if (this.map) {
          this.map.destroy();
        }
      } catch (e) {
        console.log(e)
      }


      let addMap = new window.TMap.Map('selectMap', {
        center: new window.TMap.LatLng(_lat, _lng),
        zoom: 13
      });
      // $that.addMapInfo.addMap = addMap;
      let marker;
      //添加监听事件  获取鼠标点击事件
      let _this = this
      addMap.on('click', function (event) {
        console.log(event)
        if (!marker) {
          marker = new window.TMap.MultiMarker({
            styleId: "marker",
            position: event.latLng,
            map: addMap,
            id: "1",
          });
        }
        console.log(event)
        //marker.setPosition(event.latLng);
        _this.$emit('position-change', {
          lat: event.latLng.lat,
          lng: event.latLng.lng
        })

        marker.updateGeometries([
          {
            "styleId": "marker",
            "id": "1",
            "position": event.latLng,
          }
        ])
      });
      //为地图注册click事件获取鼠标点击出的经纬度坐标

      this.map = addMap;
    },


    addMarker(position) {
      // 清除现有标记
      if (this.marker) {
        this.marker.setMap(null)
      }

      // 创建新标记
      let TMap = window.TMap

      this.marker = new TMap.MultiMarker({
        map: this.map,
        styles: {
          "marker": new TMap.MarkerStyle({
            width: 25,
            height: 35,
            src: '/img/maper.png',
            anchor: { x: 32, y: 32 }
          })
        },
        geometries: [{
          id: '1',
          styleId: 'marker',
          position: position
        }]
      })

      // 移动地图中心到标记位置
      this.map.setCenter(position)
      this.map.setZoom(16)
    },

    updatePosition(lat, lng) {
      if (!this.map) return

      const position = new window.TMap.LatLng(lat, lng)
      this.addMarker(position)
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
.select-map-pos-container {
  width: 100%;
  height: 100%;
}
</style>