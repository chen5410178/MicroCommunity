<template>
  <el-card class="floor-fee-summary-container">

    <div id="floorFeeSummary" style="height:300px;width:100%;"></div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'
import { queryReportFloorFeeSummary } from '@/api/report/reportFeeSummaryApi'

export default {
  name: 'FloorFeeSummary',
  data() {
    return {
      chart: null,
      conditions: {}
    }
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    notify(params) {
      this.conditions = params
      this.loadFloorFeeSummaryRate()
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('floorFeeSummary'))
      this.setDefaultOption()
    },
    setDefaultOption() {
      const option = {
        legend: {},
        tooltip: {},
        title: {
          show: true,
          text: this.$t('reportFeeSummary.floorFeeRateStatistics')
        },
        color: ['#FFDAB9', '#66CDAA'],
        dataset: {
          source: [
            ['product', this.$t('reportFeeSummary.roomChargeRate'), this.$t('reportFeeSummary.chargeRate')]
          ]
        },
        xAxis: { type: 'category' },
        yAxis: {},
        series: [
          { type: 'bar' },
          { type: 'bar' }
        ]
      }
      this.chart.setOption(option)
    },
    async loadFloorFeeSummaryRate() {
      try {
        const { data } = await queryReportFloorFeeSummary( this.conditions )
        this.initFloorFeeSummaryChart(data)
      } catch (error) {
        console.error('Failed to load floor fee summary:', error)
      }
    },
    initFloorFeeSummaryChart(data) {
      if (!this.chart) {
        this.initChart()
      }

      const source = [
        ['product', this.$t('reportFeeSummary.roomChargeRate'), this.$t('reportFeeSummary.chargeRate')]
      ]

      data.forEach(item => {
        source.push([
          item.floorName,
          item.feeRoomRate,
          item.feeRate
        ])
      })

      this.chart.setOption({
        dataset: {
          source
        }
      })
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.floor-fee-summary-container {
  margin-bottom: 20px;
}
</style>