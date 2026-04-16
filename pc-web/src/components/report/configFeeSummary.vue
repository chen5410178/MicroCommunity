<template>
  <el-card class="config-fee-summary-container">

    <div id="configFeeSummary" style="height:300px;width:100%;"></div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'
import { queryReportConfigFeeSummary } from '@/api/report/reportFeeSummaryApi'

export default {
  name: 'ConfigFeeSummary',
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
      this.loadConfigFeeSummaryRate()
    },
    initChart() {
      this.chart = echarts.init(document.getElementById('configFeeSummary'))
      this.setDefaultOption()
    },
    setDefaultOption() {
      const option = {
        legend: {},
        tooltip: {},
        title: {
          show: true,
          text: this.$t('reportFeeSummary.configFeeRateStatistics')
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
    async loadConfigFeeSummaryRate() {
      try {
        const { data } = await queryReportConfigFeeSummary(this.conditions )
        this.initConfigFeeSummaryChart(data)
      } catch (error) {
        console.error('Failed to load config fee summary:', error)
      }
    },
    initConfigFeeSummaryChart(data) {
      if (!this.chart) {
        this.initChart()
      }

      const source = [
        ['product', this.$t('reportFeeSummary.roomChargeRate'), this.$t('reportFeeSummary.chargeRate')]
      ]

      data.forEach(item => {
        source.push([
          item.name,
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
.config-fee-summary-container {
  margin-bottom: 20px;
}
</style>