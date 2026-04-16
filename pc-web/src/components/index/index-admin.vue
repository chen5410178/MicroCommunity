<template>
    <div class="admin-index-container">
        <div class="vc-index-nav">
            <span><i class="fa fa-home margin-right-sm"></i>{{ $t('adminIndex.home') }}</span>
            <span class="margin-left-sm margin-right-sm">/</span>
            <span>{{ $t('adminIndex.platformData') }}</span>
        </div>
        <el-row :gutter="20">
            <el-col :span="4" v-for="(item, index) in adminIndexInfo.datas" :key="index" class="margin-bottom">
                <div class="white-bg text-center padding-top-sm" style="height:140px;border: 1px solid #e0e5eb;">
                    <div style="font-size: 38px;font-weight: 600;" :style="{ 'color': item.color }">{{ item.value }}</div>
                    <div class="margin-top-lg" style="font-size: 20px;font-weight: 100;">{{ item.name }}</div>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <div id="communityFeeCharts" style="height:400px;width: 96%;" class="bg-white padding"></div>
            </el-col>
            <el-col :span="12">
                <div id="communityRepairCharts" style="height:400px;width: 96%;" class="bg-white padding"></div>
            </el-col>
        </el-row>
    </div>
</template>
  
<script>
import * as echarts from 'echarts'
import {
    queryAdminCount,
    queryCommunityFee,
    queryCommunityRepair
} from '@/api/user/adminIndexApi'

export default {
    name: 'AdminIndex',
    data() {
        return {
            adminIndexInfo: {
                hostCount: 0,
                datas: [],
                action: ''
            }
        }
    },
    mounted() {
        this._loadViewAdminData()
        this._loadCommunityFee()
        this._loadCommunityRepair()
    },
    methods: {
        async _loadViewAdminData() {
            try {
                const res = await queryAdminCount({ platform: 'admin' })
                res.data.forEach((item, index) => {
                    if (index % 4 === 0) item.color = '#1acda1'
                    if (index % 4 === 1) item.color = '#ffae11'
                    if (index % 4 === 2) item.color = '#ff7911'
                    if (index % 4 === 3) item.color = '#3a68f2'
                })
                this.adminIndexInfo.datas = res.data
            } catch (error) {
                console.error('Failed to load admin data:', error)
            }
        },
        async _loadCommunityFee() {
            try {
                const res = await queryCommunityFee({ platform: 'admin' })
                this._initAdminIndexCommunityFeeCharts(res.data)
            } catch (error) {
                console.error('Failed to load community fee data:', error)
            }
        },
        _initAdminIndexCommunityFeeCharts(_data) {
            const dom = document.getElementById('communityFeeCharts')
            const _source = [['product', this.$t('adminIndex.paymentCount'), this.$t('adminIndex.paymentAmount')]]

            _data.forEach(item => {
                _source.push([
                    item.communityName,
                    item.count,
                    item.receivedAmount
                ])
            })

            const myChart = echarts.init(dom)
            const option = {
                legend: {},
                tooltip: {},
                title: {
                    show: true,
                    text: this.$t('adminIndex.communityFeeStats')
                },
                color: ['#FFDAB9', '#66CDAA'],
                dataset: { source: _source },
                xAxis: { type: 'category' },
                yAxis: {},
                series: [
                    { type: 'bar' },
                    { type: 'bar' }
                ]
            }

            if (option && typeof option === 'object') {
                myChart.setOption(option, true)
            }
        },
        async _loadCommunityRepair() {
            try {
                const res = await queryCommunityRepair({ platform: 'admin' })
                this._initAdminIndexCommunityRepairCharts(res.data)
            } catch (error) {
                console.error('Failed to load community repair data:', error)
            }
        },
        _initAdminIndexCommunityRepairCharts(_data) {
            const dom = document.getElementById('communityRepairCharts')
            const _source = [['product', this.$t('adminIndex.repairCount')]]

            _data.forEach(item => {
                _source.push([
                    item.communityName,
                    item.count
                ])
            })

            const myChart = echarts.init(dom)
            const option = {
                legend: {},
                tooltip: {},
                title: {
                    show: true,
                    text: this.$t('adminIndex.communityRepairStats')
                },
                color: ['#FFDAB9', '#66CDAA'],
                dataset: { source: _source },
                xAxis: { type: 'category' },
                yAxis: {},
                series: [
                    { type: 'bar' }
                ]
            }

            if (option && typeof option === 'object') {
                myChart.setOption(option, true)
            }
        }
    }
}
</script>
  
<style lang="scss" scoped>
.admin-index-container {
  padding: 0 10px; // 添加左右内边距
}

.vc-index-nav {
    padding: 10px;
    font-size: 16px;
    text-align: left;
}

.margin-right-sm {
    margin-right: 5px;
}

.margin-left-sm {
    margin-left: 5px;
}

.margin-top-lg {
    margin-top: 15px;
}

.margin-bottom {
    margin-bottom: 20px;
}

.white-bg {
    background: #fff;
}

.text-center {
    text-align: center;
}

.padding-top-sm {
    padding-top: 10px;
}

.padding {
    padding: 15px;
}

.bg-white {
    background: #fff;
}</style>