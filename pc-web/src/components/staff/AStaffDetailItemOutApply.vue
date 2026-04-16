<template>
    <div class="staff-item-out-apply-container">
      <el-table :data="aStaffDetailItemOutApplyInfo.itemOuts" border style="width: 100%" class="margin-top">
        <el-table-column prop="applyOrderId" :label="$t('staffDetailItemOutApply.applyOrderId')" align="center" />
        <el-table-column prop="resourceNames" :label="$t('staffDetailItemOutApply.resourceNames')" align="center" />
        <el-table-column prop="userName" :label="$t('staffDetailItemOutApply.userName')" align="center" />
        <el-table-column prop="createUserName" :label="$t('staffDetailItemOutApply.createUserName')" align="center" />
        <el-table-column prop="createTime" :label="$t('staffDetailItemOutApply.createTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('staffDetailItemOutApply.stateName')" align="center" />
        <el-table-column :label="$t('staffDetailItemOutApply.warehousingWay')" align="center">
          <template slot-scope="scope">
            {{ scope.row.warehousingWay === '10000' ? $t('staffDetailItemOutApply.direct') : $t('staffDetailItemOutApply.review') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openDetailItemOutModel(scope.row)">
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
    name: 'AStaffDetailItemOutApply',
    data() {
      return {
        aStaffDetailItemOutApplyInfo: {
          itemOuts: [],
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
        this.aStaffDetailItemOutApplyInfo.staffId = data.staffId
        this.aStaffDetailItemOutApplyInfo.tel = data.tel
        this.loadItemOutApplyData(1, this.page.size)
      },
      async loadItemOutApplyData(page, row) {
        try {
          const { data, total } = await listAdminPurchaseApplys({
            resOrderType: '20000',
            endUserTel: this.aStaffDetailItemOutApplyInfo.tel,
            page,
            row
          })
          this.aStaffDetailItemOutApplyInfo.itemOuts = data.purchaseApplys
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailItemOutApply.fetchError'))
        }
      },
      openDetailItemOutModel(itemOut) {
        this.$router.push(`/views/common/purchaseApplyDetail?applyOrderId=${itemOut.applyOrderId}&resOrderType=20000`)
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadItemOutApplyData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadItemOutApplyData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-item-out-apply-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>