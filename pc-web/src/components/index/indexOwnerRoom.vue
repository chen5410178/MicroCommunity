<template>
  <div class="owner-room-container">
    <div class="bg-white index-1-right-item">
      <div class="index-title"><span>{{ $t('propertyIndex.residentRegStats') }}</span></div>
      <div id="ownerRoomCount" style="height:250px"></div>
      <div class="flex justify-between text-center stats-container">
        <div v-for="(item, index) in stats" :key="index">
          <div class="index-bottom-number">{{ item.value }}</div>
          <div class="index-bottom-number-desc">{{ $t(`propertyIndex.${item.label}`) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getOwnerRegisterIndex } from '@/api/index/propertyIndexApi'

export default {
  name: 'IndexOwnerRoom',
  data() {
    return {
      indexOwnerRoomInfo: {
        unbindCount: 0,
        bindCount: 0,
        unbindRoomCount: 0,
        bindRoomCount: 0,
      },
      ownerRoomChart: null
    }
  },
  computed: {
    stats() {
      return [
        { label: 'unregistered', value: this.indexOwnerRoomInfo.unbindCount },
        { label: 'registered', value: this.indexOwnerRoomInfo.bindCount },
        { label: 'unboundRoom', value: this.indexOwnerRoomInfo.unbindRoomCount },
        { label: 'boundRoom', value: this.indexOwnerRoomInfo.bindRoomCount }
      ]
    }
  },
  created() {
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.ownerRoomChart) {
      this.ownerRoomChart.dispose()
    }
  },
  methods: {
    handleResize() {
      if (this.ownerRoomChart) {
        this.ownerRoomChart.resize()
      }
    },
    async _loadIndexOwnerRegisterData() {
      const param = {
        page: 1,
        row: 10,
        communityId: this.getCommunityId()
      }

      try {
        const res = await getOwnerRegisterIndex(param)
        Object.assign(this.indexOwnerRoomInfo, res.data)
        
        this.$nextTick(() => {
          const dom = document.getElementById('ownerRoomCount')
          if (!dom) return
          
          if (this.ownerRoomChart) {
            this.ownerRoomChart.dispose()
          }
          this.ownerRoomChart = echarts.init(dom)
          this._initOwnerEcharts(
            this.ownerRoomChart,
            this.indexOwnerRoomInfo.bindCount,
            this.indexOwnerRoomInfo.unbindCount,
            this.$t('propertyIndex.residentInfo'),
            this.$t('propertyIndex.registered'),
            this.$t('propertyIndex.unregistered'),
            '#4B7AF0',
            '#E2EDF6'
          )
        })
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    _initOwnerEcharts(chart, userCount, freeCount, _title, _userCountName, _freeCountName, userColor, freeColor) {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: {
            color: '#606266'
          },
          data: [_userCountName, _freeCountName]
        },
        color: [userColor, freeColor],
        series: [{
          name: _title,
          type: 'pie',
          radius: ['40%', '65%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '16',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: userCount, name: _userCountName },
            { value: freeCount, name: _freeCountName }
          ]
        }]
      }
      
      chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-room-container {
  .index-1-right-item {
    background: #fff;
    border-radius: 4px;
    padding: 15px;
    height: 100%;
    
    .index-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 15px;
      color: #333;
    }
    
    .stats-container {
      padding-top: 15px;
      border-top: 1px solid #eee;
      
      > div {
        flex: 1;
      }
      
      .index-bottom-number {
        font-size: 20px;
        font-weight: bold;
        color: #333;
      }
      
      .index-bottom-number-desc {
        font-size: 12px;
        color: #999;
        margin-top: 5px;
      }
    }
  }
}
</style>