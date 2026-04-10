<template>
  <el-card class="pay-fee-config-discount-manage">
    <div slot="header">
      <el-row type="flex" justify="space-between" align="middle">
        <el-col :span="12" class="text-left">
          <span>{{ $t('payFeeConfigDiscountManage.title') }}</span>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="primary" size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="openAddModal">
            {{ $t('common.add') }}
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="payFeeConfigDiscounts" border style="width: 100%">
      <el-table-column prop="configDiscountId" :label="$t('payFeeConfigDiscountManage.discountId')" align="center" />
      <el-table-column prop="feeName" :label="$t('payFeeConfigDiscountManage.feeName')" align="center" >
        <template >
          {{ feeName }}
        </template>
      </el-table-column>
      <el-table-column prop="discountName" :label="$t('payFeeConfigDiscountManage.discountName')" align="center" />
      <el-table-column :label="$t('payFeeConfigDiscountManage.rule')" align="center">
        <template slot-scope="scope">
          <div v-for="(item, index) in scope.row.feeDiscountSpecs" :key="index">
            {{ item.specName }}：{{ item.specValue }}
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('payFeeConfigDiscountManage.discountType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.discountType === '1001' ? $t('payFeeConfigDiscountManage.discount') :
            $t('payFeeConfigDiscountManage.penalty') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('payFeeConfigDiscountManage.paymentPeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="payMaxEndTime" :label="$t('payFeeConfigDiscountManage.discountEndTime')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="openDeleteModal(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top-xs">
      <el-col :span="21" class="text-left discount-desc">
        <div>{{ $t('payFeeConfigDiscountManage.paymentPeriodTip') }}</div>
        <div>{{ $t('payFeeConfigDiscountManage.discountEndTimeTip') }}</div>
        <div>{{ $t('payFeeConfigDiscountManage.currentYearTip1') }}</div>
        <div>{{ $t('payFeeConfigDiscountManage.currentYearTip2') }}</div>
      </el-col>
      <el-col :span="3" class="text-right">
        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, prev, pager, next" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </el-col>
    </el-row>

    <add-pay-fee-config-discount ref="addModal" @success="fetchData" />
    <delete-pay-fee-config-discount ref="deleteModal" @success="fetchData" />
  </el-card>
</template>

<script>
import { getPayFeeConfigDiscountList } from '@/api/fee/payFeeConfigDiscountManageApi'
import AddPayFeeConfigDiscount from '@/components/fee/addPayFeeConfigDiscount'
import DeletePayFeeConfigDiscount from '@/components/fee/deletePayFeeConfigDiscount'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PayFeeConfigDiscountManageList',
  components: {
    AddPayFeeConfigDiscount,
    DeletePayFeeConfigDiscount
  },
  data() {
    return {
      payFeeConfigDiscounts: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      configId: '',
      feeName: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.configId = this.$route.query.configId
    this.feeName = this.$route.query.feeName
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          configId: this.configId,
          communityId: this.communityId
        }
        const response = await getPayFeeConfigDiscountList(params)
        this.payFeeConfigDiscounts = response.data
        this.page.total = response.total
      } catch (error) {
        this.$message.error(this.$t('payFeeConfigDiscountManage.fetchError'))
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.fetchData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.fetchData()
    },
    goBack() {
      this.$router.go(-1)
    },
    openAddModal() {
      this.$refs.addModal.open(this.configId)
    },
    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-config-discount-manage {
  .margin-top-xs {
    margin-top: 20px;
  }

  .text-right {
    text-align: right;
  }

  .discount-desc {
    text-align: left;
    font-size: 13px;
    color: #606266;
  }
}
</style>