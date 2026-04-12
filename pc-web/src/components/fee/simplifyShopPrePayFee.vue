<template>
  <div class="simplify-shop-pre-pay-fee">
    <el-row class="margin-top">
      <el-col :span="24">
        <el-button type="primary" size="small" @click="openAddPrePayDialog">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyShopPrePayFee.addPrePay') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="simplifyShopPrePayFeeInfo.fees" border stripe style="width: 100%; margin-top: 15px">
      <el-table-column prop="feeName" :label="$t('simplifyShopPrePayFee.feeName')" align="center"></el-table-column>
      <el-table-column prop="payerObjName" :label="$t('simplifyShopPrePayFee.payerObjName')" align="center"></el-table-column>
      <el-table-column prop="cycles" :label="$t('simplifyShopPrePayFee.cycles')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyShopPrePayFee.receivableReceivedAmount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}
        </template>
      </el-table-column>
      <el-table-column prop="remainAmount" :label="$t('simplifyShopPrePayFee.remainAmount')" align="center"></el-table-column>
      <el-table-column prop="primeRateName" :label="$t('simplifyShopPrePayFee.paymentMethod')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyShopPrePayFee.prePayPeriod')" align="center">
        <template slot-scope="scope">
          {{ dateFormat(scope.row.startTime) }}<br>~{{ dateFormat(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyShopPrePayFee.status')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('simplifyShopPrePayFee.createTime')" align="center"></el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      class="pagination-container"
    ></el-pagination>

    <el-dialog
      :title="$t('simplifyShopPrePayFee.addPrePay')"
      :visible.sync="addDialogVisible"
      width="500px"
      @close="handleCloseAddDialog"
    >
      <el-form :model="prePayForm" :rules="prePayRules" ref="prePayFormRef" label-width="120px">
        <el-form-item :label="$t('simplifyShopPrePayFee.amount')" prop="amount">
          <el-input-number
            v-model="prePayForm.amount"
            :min="0"
            :precision="2"
            controls-position="right"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item :label="$t('simplifyShopPrePayFee.remark')">
          <el-input
            type="textarea"
            v-model="prePayForm.remark"
            :rows="3"
            :placeholder="$t('simplifyShopPrePayFee.remarkPlaceholder')"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloseAddDialog">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmitPrePay" :loading="loading">{{ $t('common.submit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShopPrePayFees, saveShopPrePayFee } from '@/api/shop/shopCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'SimplifyShopPrePayFee',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      addDialogVisible: false,
      loading: false,
      simplifyShopPrePayFeeInfo: {
        shopId: '',
        shopName: '',
        fees: []
      },
      prePayForm: {
        amount: 0,
        remark: ''
      },
      prePayRules: {
        amount: [
          { required: true, message: this.$t('simplifyShopPrePayFee.amountRequired'), trigger: 'blur' }
        ]
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
      if (!param.shopId) {
        return
      }
      this.simplifyShopPrePayFeeInfo.shopId = param.shopId
      this.simplifyShopPrePayFeeInfo.shopName = param.shopName
      this.listSimplifyShopPrePayFees(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleNotify() {
      this.listSimplifyShopPrePayFees(this.currentPage, this.pageSize)
    },
    listSimplifyShopPrePayFees(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        payerObjId: this.simplifyShopPrePayFeeInfo.shopId,
        payerObjType: '4444'
      }

      listShopPrePayFees(params).then(res => {
        this.simplifyShopPrePayFeeInfo.fees = res.data || []
        this.total = res.records || 0
      }).catch(error => {
        console.error('Failed to fetch pre pay fees:', error)
      })
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.listSimplifyShopPrePayFees(page, this.pageSize)
    },
    openAddPrePayDialog() {
      this.addDialogVisible = true
    },
    handleCloseAddDialog() {
      this.addDialogVisible = false
      if (this.$refs.prePayFormRef) {
        this.$refs.prePayFormRef.resetFields()
      }
      this.prePayForm.amount = 0
      this.prePayForm.remark = ''
    },
    handleSubmitPrePay() {
      this.$refs.prePayFormRef.validate((valid) => {
        if (!valid) return
        this.loading = true
        const data = {
          payerObjId: this.simplifyShopPrePayFeeInfo.shopId,
          payerObjType: '4444',
          amount: this.prePayForm.amount,
          remark: this.prePayForm.remark
        }

        saveShopPrePayFee(data).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleCloseAddDialog()
          this.handleNotify()
        }).catch(error => {
          console.error('Failed to save pre pay fee:', error)
          this.$message.error(error.message || this.$t('common.operationFailed'))
        }).finally(() => {
          this.loading = false
        })
      })
    },
    dateFormat
  }
}
</script>

<style scoped>
.simplify-shop-pre-pay-fee {
  padding: 0;
}

.margin-top {
  margin-top: 15px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
