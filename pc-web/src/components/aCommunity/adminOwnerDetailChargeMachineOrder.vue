<template>
  <div class="admin-owner-detail-charge-machine-order">
    <div class="margin-top">
      <el-table
        :data="chargeMachineOrderInfo.orders"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="machineName"
          :label="$t('adminOwnerDetailChargeMachineOrder.machineName')"
          align="center"
        />
        <el-table-column
          prop="orderTypeName"
          :label="$t('adminOwnerDetailChargeMachineOrder.orderType')"
          align="center"
        />
        <el-table-column
          prop="userName"
          :label="$t('adminOwnerDetailChargeMachineOrder.userName')"
          align="center"
        />
        <el-table-column
          prop="tel"
          :label="$t('adminOwnerDetailChargeMachineOrder.phone')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('adminOwnerDetailChargeMachineOrder.createTime')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('adminOwnerDetailChargeMachineOrder.status')"
          align="center"
        />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16"></el-col>
        <el-col :span="8">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
          />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminOwnerDetailChargeMachineOrder',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      chargeMachineOrderInfo: {
        orders: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    handleSwitch(data) {
      this.chargeMachineOrderInfo.ownerId = data.ownerId
      this._loadChargeMachineOrderData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadChargeMachineOrderData(val, this.DEFAULT_ROWS)
    },
    async _loadChargeMachineOrderData(page, row) {
      const param = {
        page,
        row,
        objId: this.chargeMachineOrderInfo.ownerId,
        objType: '8899'
      }

      try {
        const response = await this.$http.get('/machineOrder.listAdminMachineOrders', { params: param })
        const orderInfo = response.data
        this.chargeMachineOrderInfo.orders = orderInfo.machineOrders
        this.total = orderInfo.total
        this.currentPage = page
      } catch (error) {
        console.error('请求失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>