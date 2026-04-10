<template>
  <div class="callable-container">
    <el-table :data="callables" border>
      <el-table-column prop="ofcId" :label="$t('adminRoomFee.id')" align="center" />
      <el-table-column prop="ownerName" :label="$t('adminRoomFee.name')" align="center" />
      <el-table-column prop="payerObjName" :label="$t('adminRoomFee.payerObj')" align="center" />
      <el-table-column prop="feeName" :label="$t('adminRoomFee.feeName')" align="center" />
      <el-table-column prop="amountdOwed" :label="$t('adminRoomFee.callableAmount')" align="center" />
      <el-table-column :label="$t('adminRoomFee.owePeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}<br>
          ~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="callableWayName" :label="$t('adminRoomFee.callableMethod')" align="center" />
      <el-table-column prop="staffName" :label="$t('adminRoomFee.callablePerson')" align="center" />
      <el-table-column prop="stateName" :label="$t('adminRoomFee.status')" align="center" />
      <el-table-column prop="remark" :label="$t('adminRoomFee.remark')" align="center">
        <template slot-scope="scope">
          <div class="text-auto" style="max-width: 200px;">{{ scope.row.remark }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('adminRoomFee.createTime')" align="center" />
    </el-table>

    <el-pagination
      :current-page="page.current"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { listAdminOweFeeCallable } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'ARoomFeeCallable',
  props: {
    roomId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      callables: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.page.current = 1
      this.loadData()
    },
    loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        payerObjId: this.roomId
      }
      
      listAdminOweFeeCallable(params).then(res => {
        this.callables = res.data
        this.page.total = res.total
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.text-auto {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>