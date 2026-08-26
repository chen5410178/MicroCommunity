<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="24">
        <el-table
          :data="simplifyHisFeeInfo.feeDetails"
          style="width: 100%; margin-top: 10px"
          border
          stripe
        >
          <el-table-column prop="feeName" :label="$t('simplifyHisFee.feeItem')" align="center"></el-table-column>
          <el-table-column prop="payerObjName" :label="$t('simplifyHisFee.payerObject')" align="center"></el-table-column>
          <el-table-column :label="$t('simplifyHisFee.receiptNumber')" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.receiptCode">{{ scope.row.receiptCode }}</div>
              <div v-else>
                <el-link
                  v-if="scope.row.state === '1400'"
                  type="primary"
                  @click="_openGeneratorReceiptCode(scope.row)"
                >
                  {{ $t('simplifyHisFee.manualGenerate') }}
                </el-link>
                <span v-else>-</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="cycles" :label="$t('simplifyHisFee.cycle')" align="center"></el-table-column>
          <el-table-column :label="$t('simplifyHisFee.receivableReceived')" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.state === '1500'">
                {{ scope.row.payerObjName }}{{ $t('simplifyHisFee.viewInFee') }}
              </div>
              <div v-else>
                {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}<br>
                <div v-if="scope.row.acctAmount > 0">
                  {{ $t('simplifyHisFee.accountDeduction') }}: {{ scope.row.acctAmount }}<br>
                </div>
                <div
                  v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList"
                  :key="index"
                >
                  {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="primeRateName" :label="$t('simplifyHisFee.paymentMethod')" align="center"></el-table-column>
          <el-table-column :label="$t('simplifyHisFee.paymentPeriod')" align="center">
            <template slot-scope="scope">
              {{ formatDate(scope.row.startTime) }}~<br>
              {{ formatDate(scope.row.endTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('simplifyHisFee.paymentTime')" align="center"></el-table-column>
          <el-table-column prop="cashierName" :label="$t('simplifyHisFee.cashier')" align="center">
            <template slot-scope="scope">
              {{ scope.row.cashierName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('simplifyHisFee.status')" align="center"></el-table-column>
          <el-table-column prop="remark" :label="$t('simplifyHisFee.remark')" align="center"></el-table-column>
          <el-table-column :label="$t('simplifyHisFee.operation')" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                plain
                @click="_toHisFeeDetail(scope.row)"
              >
                {{ $t('simplifyHisFee.detail') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="simplifyHisFeeInfo.total"
          class="pagination-container"
        ></el-pagination>
      </el-col>
    </el-row>

    <el-dialog
      :title="$t('simplifyHisFee.generateReceipt')"
      :visible.sync="receiptDialogVisible"
      width="50%"
    >
      <el-form label-position="left" label-width="120px">
        <el-form-item :label="$t('simplifyHisFee.receiptNumber')">
          <el-select
            v-model="simplifyHisFeeInfo.receiptType"
            class="w-100"
            :placeholder="$t('simplifyHisFee.receiptNumberRequired')"
          >
            <el-option disabled value="">{{ $t('simplifyHisFee.receiptNumberRequired') }}</el-option>
            <el-option value="Y" :label="$t('simplifyHisFee.systemGenerate')"></el-option>
            <el-option value="N" :label="$t('simplifyHisFee.custom')"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="simplifyHisFeeInfo.receiptType === 'N'"
          :label="$t('simplifyHisFee.receiptNumber')"
        >
          <el-input
            v-model="simplifyHisFeeInfo.receiptCode"
            :placeholder="$t('simplifyHisFee.receiptNumberRequired')"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="receiptDialogVisible = false">{{ $t('simplifyHisFee.cancel') }}</el-button>
        <el-button type="primary" @click="_generatorReceiptCode">{{ $t('simplifyHisFee.generate') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryFeeDetail, generatorReceipt } from '@/api/fee/simplifyHisFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'SimplifyHisFee',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      currentPage: 1,
      pageSize: 10,
      receiptDialogVisible: false,
      simplifyHisFeeInfo: {
        total: 0,
        records: 1,
        feeDetails: [],
        ownerId: '',
        feeDetail: {},
        receiptType: 'Y',
        payerObjId: '',
        receiptCode: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      this.clearSimplifyHisFeeInfo()
      if (!param.ownerId) {
        return
      }
      Object.assign(this.simplifyHisFeeInfo, param)
      this._listSimplifyFeeDetails(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleNotify() {
      this._listSimplifyFeeDetails(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(page) {
      this._listSimplifyFeeDetails(page, this.DEFAULT_ROWS)
    },
    formatDate(date) {
      if (!date) return ''
      return dateFormat(date)
    },
    async _listSimplifyFeeDetails(page, row) {
      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          ownerId: this.simplifyHisFeeInfo.ownerId,
          payerObjId: this.simplifyHisFeeInfo.payerObjId
        }

        const { data: feeDetails, total, records } = await queryFeeDetail(params)
        this.simplifyHisFeeInfo.total = total
        this.simplifyHisFeeInfo.records = records
        this.simplifyHisFeeInfo.feeDetails = feeDetails
        this.currentPage = page
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    clearSimplifyHisFeeInfo() {
      this.simplifyHisFeeInfo = {
        total: 0,
        records: 1,
        feeDetails: [],
        ownerId: '',
        feeDetail: {},
        receiptType: 'Y',
        receiptCode: ''
      }
    },
    _toHisFeeDetail(detail) {
      this.$router.push(`/views/fee/propertyFee?feeId=${detail.feeId}`)
    },
    _openGeneratorReceiptCode(detail) {
      this.simplifyHisFeeInfo.feeDetail = detail
      this.receiptDialogVisible = true
    },
    _generatorReceiptCode() {
      const data = {
        detailId: this.simplifyHisFeeInfo.feeDetail.detailId,
        communityId: this.communityId,
        receiptCode: this.simplifyHisFeeInfo.receiptCode
      }

      generatorReceipt(data)
        .then(response => {
          const res = response
          this.$message.success(res.msg)
          if (res.code != '0') {
            return
          }
          this.receiptDialogVisible = false
          setTimeout(() => {
            this.$emit('doSearch')
          }, 1000)
        })
        .catch(error => {
          console.error('请求失败处理', error)
          this.$message.error(error.message || '操作失败')
        })
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.w-100 {
  width: 100%;
}
.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>