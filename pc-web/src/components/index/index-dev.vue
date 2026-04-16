<template>
    <div class="padding">
        <div class="vc-index-nav">
            <span><i class="fa fa-home margin-right-sm"></i>首页</span><span
                class="margin-left-sm margin-right-sm">/</span><span>开发中心</span>
        </div>
        <el-row :gutter="20">
            <el-col :span="4" class=" margin-bottom" v-for="(item, index) in datas" :key="index">
                <div class="white-bg text-center padding-top border-radius" style="height:140px;border: 1px solid #e0e5eb;">
                    <div style="font-size: 38px;font-weight: 600;" class="margin-top" :style="{ 'color': item.color }">{{
                        item.value }}</div>
                    <div class="margin-top-lg" style="font-size: 20px;font-weight: 100;">{{ item.name }}</div>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <div class="ibox">
                    <div class="ibox-title">
                        <h5>常用功能</h5>
                    </div>
                    <div class="ibox-content">
                        <el-row class="white-bg padding margin-xs">
                            <el-col :span="6" class=" text-center  margin-top margin-bottom" style="cursor:pointer"
                                v-for="(item, index) in pages" :key="index">
                                <div @click="newFlow(item)">
                                    <div>
                                        <img :src="item.icon" width="60px" />
                                    </div>
                                    <div class="margin-top-sm">
                                        <span>{{ item.name }}</span>
                                    </div>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                </div>
            </el-col>
            <el-col :span="12">
                <div ref="chartDom" style="height:400px;width: 96%;" class="bg-white padding border-radius">
                </div>
            </el-col>
        </el-row>
    </div>
</template>
  
<script>
import { queryDevCount, queryAppServiceData } from '@/api/user/indexApi'
import * as echarts from 'echarts'
export default {
    name: 'indexDev',
    components: {

    },
    data() {
        return {
            datas: [],
            action: '',
            pages: [{
                name: '应用',
                url: '/#/pages/dev/appManage?tab=应用信息',
                icon: '/img/app.png'
            }, {
                name: '服务',
                url: '/#/pages/dev/serviceManage?tab=服务信息',
                icon: '/img/api.png'
            }, {
                name: '服务注册',
                url: '/#/pages/dev/serviceRegisterManage?tab=服务注册',
                icon: '/img/register.png'
            }, {
                name: '菜单维护',
                url: '/#/pages/dev/menuManage?tab=菜单维护',
                icon: '/img/menu.png'
            }, {
                name: '配置中心',
                url: '/#/pages/dev/mappingManage?tab=配置中心',
                icon: '/img/config.png'
            }, {
                name: '刷新缓存',
                url: '/#/pages/dev/cacheManage?tab=刷新缓存',
                icon: '/img/cache.png'
            }, {
                name: '定时任务',
                url: '/#/pages/dev/jobManage?tab=定时任务',
                icon: '/img/job.png'
            }],
        }
    },
    created() {
        this._loadData()
        this._loadAppServiceData()
    },
    methods: {
        newFlow(item) {
            console.log(item)
            let _url = item.url.replace('#/', '')
            this.$router.push(_url)
        },
        async _loadData() {
            this.loading = true
            try {
                const _json = await queryDevCount()
                for (let _index = 0; _index < _json.data.length; _index++) {
                    if (_index % 4 == 0) {
                        _json.data[_index].color = "#1acda1";
                    }
                    if (_index % 4 == 1) {
                        _json.data[_index].color = "#ffae11";
                    }
                    if (_index % 4 == 2) {
                        _json.data[_index].color = "#ff7911";
                    }
                    if (_index % 4 == 3) {
                        _json.data[_index].color = "#3a68f2";
                    }
                }
                this.datas = _json.data;
            } catch (error) {
                console.error('登录失败：', error)
            } finally {
                this.loading = false
            }
        },
        async _loadAppServiceData() {
            this.loading = true
            try {
                const _json = await queryAppServiceData()
                this._initDevIndexAppServiceCharts(_json.data)
            } catch (error) {
                console.error('登录失败：', error)
            } finally {
                this.loading = false
            }
        },
        _initDevIndexAppServiceCharts: function (_data) {
            const chartDom = this.$refs.chartDom
            let _source = [
                ['product', '接口数'],
            ];

            _data.forEach(item => {
                _source.push([
                    item.name,
                    item.serviceCount,
                ])
            });

            let myChart = echarts.init(chartDom);
            let option = null;
            option = {
                legend: {},
                tooltip: {},
                title: {
                    show: "true",
                    text: '应用接口数'
                },
                color: ['#66CDAA'],
                dataset: {
                    source: _source
                },
                xAxis: { type: 'category' },
                yAxis: {},
                series: [
                    { type: 'bar' }
                ]
            };

            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }

        },
    }
}
</script>
  
<style lang="scss" scoped>
.vc-index-nav {
    padding: 10px;
    font-size: 16px;
    text-align: left;
}
</style> 