<template>
    <div class="pre-pay-fee-detail-container">
        <div class="margin-top">
            <el-table v-loading="loading" :data="prePayFeeDetailInfo.fees" border stripe style="width: 100%">
                <el-table-column prop="feeName" :label="$t('prePayFeeDetail.feeName')" align="center"></el-table-column>
                <el-table-column prop="payerObjName" :label="$t('prePayFeeDetail.payerObjName')"
                    align="center"></el-table-column>
                <el-table-column prop="ownerName" :label="$t('prePayFeeDetail.ownerName')"
                    align="center"></el-table-column>
                <el-table-column prop="cycles" :label="$t('prePayFeeDetail.cycles')" align="center"></el-table-column>
                <el-table-column :label="$t('prePayFeeDetail.receivableReceivedAmount')" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}
                    </template>
                </el-table-column>
                <el-table-column prop="remainAmount" :label="$t('prePayFeeDetail.remainAmount')"
                    align="center"></el-table-column>
                <el-table-column prop="primeRateName" :label="$t('prePayFeeDetail.primeRateName')"
                    align="center"></el-table-column>
                <el-table-column :label="$t('prePayFeeDetail.prePayPeriod')" align="center">
                    <template slot-scope="scope">
                        {{ dateFormat(scope.row.startTime) }}<br>~{{ dateFormat(scope.row.endTime) }}
                    </template>
                </el-table-column>
                <el-table-column prop="stateName" :label="$t('prePayFeeDetail.stateName')"
                    align="center"></el-table-column>
                <el-table-column prop="cashierName" :label="$t('prePayFeeDetail.cashierName')"
                    align="center"></el-table-column>
                <el-table-column prop="discountAmount" :label="$t('prePayFeeDetail.discountAmount')"
                    align="center"></el-table-column>
                <el-table-column prop="createTime" :label="$t('prePayFeeDetail.createTime')"
                    align="center"></el-table-column>
                <el-table-column :label="$t('prePayFeeDetail.deductionWay')" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.way == 'Y' ? $t('prePayFeeDetail.auto') : $t('prePayFeeDetail.manual') }}
                    </template>
                </el-table-column>
                <el-table-column prop="remark" :label="$t('prePayFeeDetail.remark')" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column :label="$t('prePayFeeDetail.operation')" align="center" width="300">
                    <template slot-scope="scope">
                        <div class="btn-group">
                            <el-button
                                v-if="scope.row.remainAmount == scope.row.receivedAmount && scope.row.payOrderId != '-1'"
                                class="btn-white" size="mini" @click="_refundPrePayFee(scope.row)">
                                {{ $t('prePayFeeDetail.refund') }}
                            </el-button>
                            <el-button v-if="scope.row.way == 'Y'" class="btn-white" size="mini"
                                @click="_updatePrePayFeeWay(scope.row, 'N')">
                                {{ $t('prePayFeeDetail.manualDeduction') }}
                            </el-button>
                            <el-button v-else class="btn-white" size="mini"
                                @click="_updatePrePayFeeWay(scope.row, 'Y')">
                                {{ $t('prePayFeeDetail.autoDeduction') }}
                            </el-button>
                            <el-button class="btn-white" size="mini" @click="_prePayFeeDeductionDetail(scope.row)">
                                {{ $t('prePayFeeDetail.deductionDetail') }}
                            </el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-row class="margin-top">
                <el-col :span="12" class="text-right">
                    <span>{{ $t('prePayFeeDetail.totalRemainAmount') }}：{{ prePayFeeDetailInfo.totalAmount }}</span>
                </el-col>
                <el-col :span="12" class="text-right">
                    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
                        :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
                </el-col>
            </el-row>
        </div>

    </div>
</template>

<script>
import { getPrePayFeeList, modifyPrePayFeeWay } from '@/api/fee/prePayFeeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
    name: 'PrePayFeeDetailList',
    data() {
        return {
            loading: false,
            prePayFeeDetailInfo: {
                fees: [],
                selectPpfIds: [],
                ownerId: '',
                payerObjId: '',
                totalAmount: 0.0
            },
            page: {
                current: 1,
                size: 10,
                total: 0
            }
        }
    },
    created() {
        this.communityId = getCommunityId()

    },
    methods: {
        open(params) {
            this.handleSwitch(params)
        },
        handleSwitch(param) {
            this.prePayFeeDetailInfo.payerObjId = param.payerObjId
            this.prePayFeeDetailInfo.ownerId = param.ownerId
            this._loadPrePayFeeDetailData(this.page.current, this.page.size)
        },

        async _loadPrePayFeeDetailData(_page, _row) {
            try {
                this.loading = true
                const params = {
                    communityId: this.communityId,
                    payerObjId: this.prePayFeeDetailInfo.payerObjId,
                    ownerId: this.prePayFeeDetailInfo.ownerId,
                    page: _page,
                    row: _row
                }

                const { data, records } = await getPrePayFeeList(params)

                this.prePayFeeDetailInfo.fees = data || []
                this.page.total = records || 0

                let _totalAmount = 0.0
                data.forEach(fee => {
                    _totalAmount += parseFloat(fee.remainAmount || 0)
                })
                this.prePayFeeDetailInfo.totalAmount = _totalAmount.toFixed(2)
            } catch (error) {
                console.error('请求失败处理:', error)
                this.$message.error(this.$t('prePayFeeDetail.fetchError'))
            } finally {
                this.loading = false
            }
        },
        _qureyPrePayFeeDetail() {
            this.page.current = 1
            this._loadPrePayFeeDetailData(this.page.current, this.page.size)
        },
        _prePayFeeDeductionDetail(fee) {
            this.$router.push({ path: '/views/fee/prePayFeeDeductionDetail', query: { ppfId: fee.ppfId } })
        },
        _refundPrePayFee(fee) {
            this.$router.push({ path: '/views/fee/refundPrePayFee', query: { payOrderId: fee.payOrderId } })
        },
        _prestoreAccount(_account) {
            this.$emit('prestoreAccountOpenAddModal', {
                ownerId: this.prePayFeeDetailInfo.ownerId,
                acctType: _account.acctType,
                tel: _account.link,
                roomId: _account.roomId
            })
        },
        _prestoreAccount1() {
            this.$emit('prestoreAccountOpenAddModal', {})
        },
        checkAllPrePayFeeReceipt(checked) {
            if (checked) {
                this.prePayFeeDetailInfo.selectPpfIds = this.prePayFeeDetailInfo.fees.map(item => item.ppfId)
            } else {
                this.prePayFeeDetailInfo.selectPpfIds = []
            }
        },
        _printPrePayFeeReceipt() {
            const _selectPpfIds = this.prePayFeeDetailInfo.selectPpfIds.join(',')
            window.open(`/#/pages/print/printPrePayFeeReceipt?ppfIds=${_selectPpfIds}&payerObjId=${this.prePayFeeDetailInfo.payerObjId}`)
        },
        async _updatePrePayFeeWay(fee, way) {
            try {
                const _data = {
                    ppfId: fee.ppfId,
                    way: way,
                    communityId: this.communityId
                }

                const response = await modifyPrePayFeeWay(_data)

                if (response.code === 0) {
                    this.$message.success(this.$t('common.operationSuccess'))
                    this._loadPrePayFeeDetailData(this.page.current, this.page.size)
                } else {
                    this.$message.error(response.msg)
                }
            } catch (error) {
                console.error('请求失败处理:', error)
                this.$message.error(this.$t('prePayFeeDetail.operationError'))
            }
        },
        dateFormat,
        handleSizeChange(val) {
            this.page.size = val
            this._loadPrePayFeeDetailData(this.page.current, this.page.size)
        },
        handleCurrentChange(val) {
            this.page.current = val
            this._loadPrePayFeeDetailData(this.page.current, this.page.size)
        }
    }
}
</script>

<style lang="scss" scoped>
.pre-pay-fee-detail-container {
    padding: 0;
    margin: 0;

    .margin-top {
        margin-top: 20px;
    }

    .text-right {
        text-align: right;
    }

    .padding-right-lg {
        padding-right: 20px;
    }

    .btn-white {
        background-color: #fff;
        border-color: #dcdfe6;
        color: #606266;

        &:hover {
            background-color: #f5f7fa;
            border-color: #c6e2ff;
            color: #409eff;
        }
    }

    .btn-group {
        display: flex;
        gap: 5px;
        justify-content: center;

        .el-button {
            margin: 0;
        }
    }


}
</style>