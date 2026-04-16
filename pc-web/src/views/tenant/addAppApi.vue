<template>
    <div class="add-app-api-container">
        <el-card class="box-card">
            <div class="card-title" slot="header">
                <span>
                    {{ $t('addAppApi.title') }}
                </span>
            </div>
            <div class="api-info-list">
                <div class="api-info-row">
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.appName') }}：
                        </span>
                        <span class="value">
                            {{ form.name || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.appId') }}：
                        </span>
                        <span class="value">
                            {{ form.appId || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.remark') }}：
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
                    {{ $t('addAppApi.selectApiTitle') }}
                </span>
                <el-button @click="showSelectDialog" class="select-btn" size="mini" type="primary">
                    {{ $t('common.selectBtn') }}
                </el-button>
            </div>
            <div class="api-info-list">
                <div class="api-info-row">
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.interfaceName.label') }}：
                        </span>
                        <span class="value">
                            {{ selectApi.apiName || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.interfaceCode.label') }}：
                        </span>
                        <span class="value">
                            {{ selectApi.apiCode || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.seq.label') }}：
                        </span>
                        <span class="value">
                            {{ selectApi.seq || '-' }}
                        </span>
                    </div>
                    <div class="api-info-item">
                        <span class="label">
                            {{ $t('addAppApi.remark') }}：
                        </span>
                        <span class="value">
                            {{ selectApi.remark || '-' }}
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
        <select-hc-api :dialog-visible="selectDialogVisible" @select="handleApiSelect" @update:dialogvisible="val => selectDialogVisible = val" ref="selectHcApiRef">
        </select-hc-api>
    </div>
</template>

<script>
    import { getHcAppList } from '@/api/tenant/hcAppApi'
    import { addAppApi } from '@/api/tenant/appApiApi'
    import { deepCopy } from '@/api/util/vcApi'
    import SelectHcApi from '@/components/tenant/SelectHcApi.vue';

    export default {
        name: 'AddAppApi',
        data() {
            return {
                form: {
                    appId: '',
                    name: '',
                    remark: ''
                },
                selectApi: {
                    apiId: '',
                    apiCode: '',
                    apiName: '',
                    seq: '',
                    remark: ''
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
            SelectHcApi
        },
        methods: {
            showSelectDialog() {
                this.$refs.selectHcApiRef.openDialog();
            },
            handleApiSelect(api) {
                deepCopy(api, this.selectApi)
                console.log('Selected API:', api);
            },
            async getApp() {
                const params = {
                    page: 1,
                    row: 1,
                    appId: this.form.appId,
                }
                const { data } = await getHcAppList(params)
                deepCopy(data.data[0], this.form)
                console.log(this.form, data.data[0])
            },
            async handleSubmit() {
                console.log('addAppApi type:', typeof addAppApi); // 添加这行调试信息
                try {
                    await addAppApi({
                        appId: this.form.appId,
                        apiId: this.selectApi.apiId,
                        apiCode: this.selectApi.apiCode,
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
    width: 100%;
    padding: 40px 0 0 0;

    .box-card {
        width: 100%;
        box-sizing: border-box;

        .card-title {
            text-align: left;
            font-size: 18px;
            font-weight: bold;
            color: #333;
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