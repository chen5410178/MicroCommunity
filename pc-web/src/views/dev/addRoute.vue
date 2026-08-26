<template>
    <div class="add-app-api-container padding">
        <el-card class="box-card">
            <div class="card-title" slot="header">
                <span>
                    {{ $t('addRoute.title') }}
                </span>
            </div>
            <div class="api-info-list">
                <div class="api-info-row">
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.appName') }}：
                        </span>
                        <span class="value">
                            {{ form.name || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.appId') }}：
                        </span>
                        <span class="value">
                            {{ form.appId || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.remark') }}：
                        </span>
                        <span class="value">
                            {{ form.remark || '-' }}
                        </span>
                    </div>
                </div>
            </div>
        </el-card>
        <!-- 新增：选择接口卡片 -->
        <el-card class="box-card select-api-card">
            <div class="card-title select-api-title-row" slot="header">
                <span>
                    {{ $t('addRoute.selectApiTitle') }}
                </span>
                <el-button @click="showSelectDialog" class="select-btn" size="mini" type="primary">
                    {{ $t('common.selectBtn') }}
                </el-button>
            </div>
            <div class="api-info-list">
                <div class="api-info-row">
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.serviceCode') }}：
                        </span>
                        <span class="value">
                            {{ service.serviceCode || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.serviceName') }}：
                        </span>
                        <span class="value">
                            {{ service.name || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.serviceId') }}：
                        </span>
                        <span class="value">
                            {{ service.serviceId || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addRoute.url') }}：
                        </span>
                        <span class="value">
                            {{ service.url || '-' }}
                        </span>
                    </div>
                </div>
            </div>
        </el-card>


        <!-- 页面底部提交按钮 -->
        <div class="page-actions">
            <!--返回按钮-->
            <el-button @click="$router.go(-1)" type="default">
                {{ $t('common.cancel') }}
            </el-button>
            <el-button @click="handleSubmit" type="primary">
                {{ $t('common.submit') }}
            </el-button>
        </div>
        <select-service :dialog-visible="selectDialogVisible" @select="handleApiSelect"
            @update:dialogvisible="val => selectDialogVisible = val" ref="selectHcApiRef">
        </select-service>
    </div>
</template>

<script>
import { getAppList } from '@/api/dev/appApi'
import { addRoute } from '@/api/dev/routeApi'
import { deepCopy } from '@/api/util/vcApi'
import SelectService from '@/components/dev/SelectService.vue';

export default {
    name: 'AddAppApi',
    data() {
        return {
            form: {
                appId: '',
                name: '',
                remark: ''
            },
            service: {
                serviceId: '',
                name: '',
                serviceCode: '',
                url: '',
                method: ''
            },
            selectDialogVisible: false,
            apiList: [], // 这里填充你的API列表数据
            selectedApi: null
        }
    },
    created() {
        const { appId } = this.$route.query
        this.form.appId = appId;
        this.getApp();
    },
    components: {
        SelectService
    },
    methods: {
        showSelectDialog() {
            this.$refs.selectHcApiRef.openDialog();
        },
        handleApiSelect(api) {
            deepCopy(api, this.service)
        },
        async getApp() {
            const params = {
                page: 1,
                row: 1,
                appId: this.form.appId,
            }
            const { apps } = await getAppList(params)
            deepCopy(apps[0], this.form)
        },
        async handleSubmit() {
            let data = []
            data.push({
                index: 2,
                flowComponent: "App",
                appId: this.form.appId,
                name: this.form.name
            });
            data.push({
                flowComponent: "Service",
                index: 2,
                isInstance: "CMD",
                messageQueueName: "",
                method: this.service.method,
                name: this.service.name,
                retryCount: "3",
                serviceCode: this.service.serviceCode,
                serviceId: this.service.serviceId,
                timeout: "60",
                url: this.service.url,
            });
            let _orderTypeCd = "D"
            if (this.service.method == 'GET') {
                _orderTypeCd = "Q"
            }
            data.push({
                flowComponent: "addRouteView",
                invokeLimitTimes: "1000",
                invokeModel: "S",
                orderTypeCd: _orderTypeCd
            })
            try {
                await addRoute({
                    data: data
                })
                this.$message.success(this.$t('common.operationSuccess'))
                this.$emit('success')
                this.$router.go(-1)
            } catch (error) {
                console.error('添加失败:', error)
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.add-app-api-container {
    .box-card {
        width: 100%;
        box-sizing: border-box;

        .card-title {
            text-align: left;
            font-size: 18px;
        }

        .api-info-list {
            margin-top: 20px;

            .api-info-row {
                display: flex;
                flex-wrap: nowrap;

                .api-info-item {
                    flex: 1 1 0;
                    min-width: 0;
                    display: flex;
                    flex-direction: row;
                    align-items: center;
                    margin-right: 24px;

                    .label {
                        color: #888;
                        font-size: 14px;
                        margin-bottom: 0;
                        margin-right: 0;
                        white-space: nowrap;
                    }

                    .value {
                        color: #222;
                        font-size: 16px;
                        word-break: break-all;
                        flex: 1;
                    }
                }

                .api-info-item:last-child {
                    margin-right: 0;
                }
            }
        }
    }

    .select-api-card {
        margin-top: 20px;
    }

    .select-api-title-row {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .select-btn {
            margin-left: 16px;
        }
    }

    .page-actions {
        width: 100%;
        margin-top: 32px;
        display: flex;
        justify-content: flex-end;
    }
}
</style>