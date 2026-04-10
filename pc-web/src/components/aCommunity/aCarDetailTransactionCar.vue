<template>
  <div class="margin-top">
    <el-row class="margin-top-lg"></el-row>
    
    <div class="margin-top">
      <el-table :data="aCarDetailTransactionCarInfo.translates" style="width: 100%">
        <el-table-column prop="machineTranslateId" :label="$t('carDetailTransaction.syncId')" align="center"></el-table-column>
        <el-table-column prop="machineCode" :label="$t('carDetailTransaction.deviceCode')" align="center"></el-table-column>
        <el-table-column prop="typeCdName" :label="$t('carDetailTransaction.objectType')" align="center"></el-table-column>
        <el-table-column prop="objName" :label="$t('carDetailTransaction.objectName')" align="center"></el-table-column>
        <el-table-column prop="machineCmdName" :label="$t('carDetailTransaction.command')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailTransaction.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailTransaction.remark')" align="center" width="80"></el-table-column>
        <el-table-column prop="updateTime" :label="$t('carDetailTransaction.syncTime')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12" class="text-right">
          <el-pagination
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminMachineTranslates } from '@/api/aCommunity/aCarDetailTransactionCarApi'

export default {
  name: 'ACarDetailTransactionCar',
  data() {
    return {
      aCarDetailTransactionCarInfo: {
        translates: [],
        ownerId: '',
        carId: '',
        memberId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.handleSwitch(data)
    },
    handleSwitch(data) {
      this.aCarDetailTransactionCarInfo.carId = data.carId
      this.aCarDetailTransactionCarInfo.memberId = data.memberId
      this._loadACarDetailTransactionCarData(1, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailTransactionCarData(page, this.pageSize)
    },
    async _loadACarDetailTransactionCarData(page, row) {
      try {
        const param = {
          page,
          row,
          objId: this.aCarDetailTransactionCarInfo.memberId,
          typeCd: '4455'
        }
        const response = await listAdminMachineTranslates(param)
        this.aCarDetailTransactionCarInfo.translates = response.machineTranslates
        this.total = response.total
      } catch (error) {
        console.error('Failed to load transaction data:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.margin-top-lg {
  margin-top: 40px;
}
.text-right {
  text-align: right;
}
</style>