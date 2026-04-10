<template>
  <div class="operational-analysis-container">
    <el-row class="animated fadeInRight ecommerce">
      <el-col :span="4">
        <select-admin-one-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col v-for="(chart, index) in charts" :key="index" :span="8" class="margin-bottom">
            <el-card class="box-card">
              <div :id="chart.id" style="width: 100%; height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminOneCommunity from '@/components/community/selectAdminOneCommunity'
import { getCommunityOperationalAnalysis } from '@/api/report/operationalAnalysisApi'
import * as echarts from 'echarts'

export default {
  name: 'OperationalAnalysisList',
  components: {
    SelectAdminOneCommunity
  },
  data() {
    return {
      charts: [
        { id: 'communityPayFeeDetail', title: this.$t('operationalAnalysis.chart.payFee') },
        { id: 'communityRepair', title: this.$t('operationalAnalysis.chart.repair') },
        { id: 'communityInspection', title: this.$t('operationalAnalysis.chart.inspection') },
        { id: 'communityMaintainance', title: this.$t('operationalAnalysis.chart.maintainance') },
        { id: 'communityItemIn', title: this.$t('operationalAnalysis.chart.itemIn') },
        { id: 'communityItemOut', title: this.$t('operationalAnalysis.chart.itemOut') },
        { id: 'communityCarIn', title: this.$t('operationalAnalysis.chart.carIn') },
        { id: 'communityPersonIn', title: this.$t('operationalAnalysis.chart.personIn') },
        { id: 'communityContract', title: this.$t('operationalAnalysis.chart.contract') }
      ],
      communityId: ''
    }
  },
  methods: {
    handleCommunityChange(community) {
      this.communityId = community.communityId
      this.loadOperationalAnalysisData()
    },
    async loadOperationalAnalysisData() {
      try {
        const params = {
          communityId: this.communityId
        }
        const res = await getCommunityOperationalAnalysis(params)
        this.initCharts(res.data)
      } catch (error) {
        this.$message.error(this.$t('operationalAnalysis.fetchError'))
      }
    },
    initCharts(data) {
      this.initAnalysisChart(data.feeDetailData, 'communityPayFeeDetail', this.$t('operationalAnalysis.chart.payFee'), this.$t('operationalAnalysis.chart.payFee'))
      this.initAnalysisChart(data.repairData, 'communityRepair', this.$t('operationalAnalysis.chart.repair'), this.$t('operationalAnalysis.chart.repair'))
      this.initAnalysisChart(data.inspectionData, 'communityInspection', this.$t('operationalAnalysis.chart.inspection'), this.$t('operationalAnalysis.chart.inspection'))
      this.initAnalysisChart(data.maintainanceData, 'communityMaintainance', this.$t('operationalAnalysis.chart.maintainance'), this.$t('operationalAnalysis.chart.maintainance'))
      this.initAnalysisChart(data.itemInData, 'communityItemIn', this.$t('operationalAnalysis.chart.itemIn'), this.$t('operationalAnalysis.chart.itemIn'))
      this.initAnalysisChart(data.itemOutData, 'communityItemOut', this.$t('operationalAnalysis.chart.itemOut'), this.$t('operationalAnalysis.chart.itemOut'))
      this.initAnalysisChart(data.carInData, 'communityCarIn', this.$t('operationalAnalysis.chart.carIn'), this.$t('operationalAnalysis.chart.carIn'))
      this.initAnalysisChart(data.personInData, 'communityPersonIn', this.$t('operationalAnalysis.chart.personIn'), this.$t('operationalAnalysis.chart.personIn'))
      this.initAnalysisChart(data.contractData, 'communityContract', this.$t('operationalAnalysis.chart.contract'), this.$t('operationalAnalysis.chart.contract'))
    },
    initAnalysisChart(data, elementId, title, lineName) {
      const dom = document.getElementById(elementId)
      if (!dom) return
      
      const myChart = echarts.init(dom)
      const createTime = []
      const countValues = []
      
      data.forEach(item => {
        createTime.push(item.createTime)
        countValues.push(item.countValue)
      })

      const option = {
        title: {
          text: title
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: createTime
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: createTime
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: lineName,
          type: 'line',
          stack: 'Total',
          data: countValues
        }]
      }

      myChart.setOption(option)
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.operational-analysis-container {
  padding: 20px;
  
  .margin-bottom {
    margin-bottom: 20px;
  }
  
  .box-card {
    margin: 0 10px;
  }
}
</style>