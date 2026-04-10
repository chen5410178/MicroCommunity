<template>
  <div class="repair-complaint-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="bg-white index-1-left-item">
          <div class="index-title"><span>{{ $t('propertyIndex.repairStats') }}</span></div>
          <div id="repairCount" style="height:250px"></div>
          <div class="flex justify-between text-center stats-container">
            <div v-for="(item, index) in repairStats" :key="index">
              <div class="index-bottom-number">{{ item.value }}</div>
              <div class="index-bottom-number-desc">{{ $t(`propertyIndex.${item.label}`) }}</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="bg-white index-1-left-item">
          <div class="index-title"><span>{{ $t('propertyIndex.complaintStats') }}</span></div>
          <div id="complaintCount" style="height:250px"></div>
          <div class="flex justify-around text-center stats-container">
            <div v-for="(item, index) in complaintStats" :key="index">
              <div class="index-bottom-number">{{ item.value }}</div>
              <div class="index-bottom-number-desc">{{ $t(`propertyIndex.${item.label}`) }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getRepairIndex, getComplaintIndex } from '@/api/index/propertyIndexApi'

export default {
  name: 'IndexRepairComplaint',
  data() {
    return {
      indexRepairComplaintInfo: {
        allCount: 0,
        waitCount: 0,
        doingCount: 0,
        finishCount: 0,
        allComplaintCount: 0,
        waitComplaintCount: 0,
        finishComplaintCount: 0,
      },
      repairChart: null,
      complaintChart: null
    }
  },
  computed: {
    repairStats() {
      return [
        { label: 'allRepair', value: this.indexRepairComplaintInfo.allCount },
        { label: 'toBeDispatched', value: this.indexRepairComplaintInfo.waitCount },
        { label: 'processing', value: this.indexRepairComplaintInfo.doingCount },
        { label: 'processed', value: this.indexRepairComplaintInfo.finishCount }
      ]
    },
    complaintStats() {
      return [
        { label: 'allComplaint', value: this.indexRepairComplaintInfo.allComplaintCount },
        { label: 'processing', value: this.indexRepairComplaintInfo.waitComplaintCount },
        { label: 'processed', value: this.indexRepairComplaintInfo.finishComplaintCount }
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
    if (this.repairChart) {
      this.repairChart.dispose()
    }
    if (this.complaintChart) {
      this.complaintChart.dispose()
    }
  },
  methods: {
    initData() {
      this._loadIndexRepairData()
      this._loadIndexComplaintData()
    },
    handleResize() {
      if (this.repairChart) {
        this.repairChart.resize()
      }
      if (this.complaintChart) {
        this.complaintChart.resize()
      }
    },
    async _loadIndexRepairData() {
      const param = {
        page: 1,
        row: 10,
        communityId: this.getCommunityId()
      }

      try {
        const res = await getRepairIndex(param)
        Object.assign(this.indexRepairComplaintInfo, res.data)

        this.$nextTick(() => {
          const dom = document.getElementById('repairCount')
          if (!dom) return

          if (this.repairChart) {
            this.repairChart.dispose()
          }
          this.repairChart = echarts.init(dom)
          this._initEcharts(
            this.repairChart,
            this.indexRepairComplaintInfo.finishCount,
            this.indexRepairComplaintInfo.allCount - this.indexRepairComplaintInfo.finishCount,
            this.$t('propertyIndex.repairInfo'),
            this.$t('propertyIndex.processed'),
            this.$t('propertyIndex.unprocessed'),
            '#4B7AF0',
            '#E2EDF6'
          )
        })
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    async _loadIndexComplaintData() {
      const param = {
        page: 1,
        row: 10,
        communityId: this.getCommunityId()
      }

      try {
        const res = await getComplaintIndex(param)
        Object.assign(this.indexRepairComplaintInfo, res.data)

        this.$nextTick(() => {
          const dom = document.getElementById('complaintCount')
          if (!dom) return

          if (this.complaintChart) {
            this.complaintChart.dispose()
          }
          this.complaintChart = echarts.init(dom)
          this._initEcharts(
            this.complaintChart,
            this.indexRepairComplaintInfo.finishComplaintCount,
            this.indexRepairComplaintInfo.allComplaintCount - this.indexRepairComplaintInfo.finishComplaintCount,
            this.$t('propertyIndex.complaintStats'),
            this.$t('propertyIndex.processed'),
            this.$t('propertyIndex.unprocessed'),
            '#01C36D',
            '#E2EDF6'
          )
        })
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    _initEcharts(chart, userCount, freeCount, _title, _userCountName, _freeCountName, userColor, freeColor) {
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


.index-1-left-item {
  background: #fff;
  border-radius: 4px;
  padding: 15px;

  .index-title {
    text-align: left;
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 15px;
    color: #333;
  }

  .stats-container {
    padding-top: 15px;
    border-top: 1px solid #eee;

    >div {
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
</style>