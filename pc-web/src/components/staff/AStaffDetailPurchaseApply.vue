<template>
    <div class="staff-purchase-apply-container">
      <el-table :data="aStaffDetailPurchaseApplyInfo.purchaseApplys" border style="width: 100%" class="margin-top">
        <el-table-column prop="applyOrderId" :label="$t('staffDetailPurchaseApply.applyOrderId')" align="center" />
        <el-table-column prop="userName" :label="$t('staffDetailPurchaseApply.userName')" align="center" />
        <el-table-column prop="endUserName" :label="$t('staffDetailPurchaseApply.endUserName')" align="center" />
        <el-table-column prop="createUserName" :label="$t('staffDetailPurchaseApply.createUserName')" align="center" />
        <el-table-column prop="resourceNames" :label="$t('staffDetailPurchaseApply.resourceNames')" align="center" />
        <el-table-column prop="createTime" :label="$t('staffDetailPurchaseApply.createTime')" align="center" />
        <el-table-column :label="$t('staffDetailPurchaseApply.warehousingWay')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.warehousingWay === '10000'">{{ $t('staffDetailPurchaseApply.direct') }}</span>
            <span v-else-if="scope.row.warehousingWay === '20000'">{{ $t('staffDetailPurchaseApply.purchase') }}</span>
            <span v-else>{{ $t('staffDetailPurchaseApply.urgent') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('staffDetailPurchaseApply.stateName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openDetailPurchaseApplyModel(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script>
  import { listAdminPurchaseApplys } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailPurchaseApply',
    data() {
      return {
        aStaffDetailPurchaseApplyInfo: {
          purchaseApplys: [],
          staffId: '',
          tel: ''
        },
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailPurchaseApplyInfo.staffId = data.staffId
        this.aStaffDetailPurchaseApplyInfo.tel = data.tel
        this.loadPurchaseApplyData(1, this.page.size)
      },
      async loadPurchaseApplyData(page, row) {
        try {
          const { data, total } = await listAdminPurchaseApplys({
            resOrderType: '10000',
            endUserTel: this.aStaffDetailPurchaseApplyInfo.tel,
            page,
            row
          })
          this.aStaffDetailPurchaseApplyInfo.purchaseApplys = data.purchaseApplys
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailPurchaseApply.fetchError'))
        }
      },
      openDetailPurchaseApplyModel(purchaseApply) {
        this.$router.push(`/views/common/purchaseApplyDetail?applyOrderId=${purchaseApply.applyOrderId}&resOrderType=10000`)
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadPurchaseApplyData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadPurchaseApplyData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-purchase-apply-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>