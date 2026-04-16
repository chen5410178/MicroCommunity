<template>
  <div class="room-fee-container">
    <el-row :gutter="20">
      <el-col :span="6" class="text-left">
        <el-select v-model="state" @change="changeState">
          <el-option label="请选择状态" value="" />
          <el-option label="有效" value="2008001" />
          <el-option label="收费结束" value="2009001" />
        </el-select>
      </el-col>
    </el-row>

    <el-table :data="fees" class="margin-top" border>
      <el-table-column label="房屋" prop="payerObjName" align="center" />
      <el-table-column label="费用项目" prop="feeName" align="center" />
      <el-table-column label="费用标识" prop="feeFlagName" align="center" />
      <el-table-column label="费用类型" prop="feeTypeCdName" align="center" />
      <el-table-column label="应收金额" prop="amountOwed" align="center" />
      <el-table-column label="建账时间" prop="startTime" align="center" />
      <el-table-column label="应收时间段" align="center">
        <template slot-scope="scope">
          {{ getRoomEndTime(scope.row) }}~<br />
          {{ getRoomDeadlineTime(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="说明" align="center" width="150">
        <template slot-scope="scope">
          <div v-if="scope.row.computingFormula === '5005' || scope.row.computingFormula === '9009'">
            <div>上期度数：{{ scope.row.preDegrees }}</div>
            <div>本期度数：{{ scope.row.curDegrees }}</div>
            <div>单价：{{ scope.row.mwPrice || scope.row.squarePrice }}</div>
            <div>附加费：{{ scope.row.additionalAmount }}</div>
          </div>
          <div v-else-if="scope.row.computingFormula === '6006'">
            <div>用量：{{ getRoomAttrValue(scope.row.feeAttrs, '390006') }}</div>
            <div>单价：{{ scope.row.squarePrice }}</div>
            <div>附加费：{{ scope.row.additionalAmount }}</div>
          </div>
          <div v-else-if="scope.row.feeTypeCd === '888800010017'">
            <div>算法：{{ getRoomAttrValue(scope.row.feeAttrs, '390005') }}</div>
            <div>用量：{{ getRoomAttrValue(scope.row.feeAttrs, '390003') }}</div>
          </div>
          <div v-else-if="scope.row.computingFormula === '4004'">
            <div>费用根据实际情况而定</div>
          </div>
          <div v-else>
            <div>单价：{{ scope.row.squarePrice }}</div>
            <div v-if="scope.row.feeFlag === '1003006'">附加费：{{ scope.row.additionalAmount }}</div>
            <div v-else>固定费：{{ scope.row.additionalAmount }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="stateName" align="center" />
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button size="mini" @click="toAdminFeeDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="12" class="text-left">
        <div>注意：应收结束时间 "-" 表示未到应收时间 或 收费已结束</div>
        <div>应收金额为-1 一般为费用项公式设置出错请检查</div>
      </el-col>
      <el-col :span="4">
        欠费小计：{{ totalAmount }}
      </el-col>
      <el-col :span="8">
        <el-pagination :current-page="page.current" :page-size="page.size" :total="page.total"
          layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminFee } from '@/api/fee/adminRoomFeeApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'ARoomDetailRoomFee',
  props: {
    roomId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      fees: [],
      state: '2008001',
      totalAmount: 0,
      page: {
        current: 1,
        size: 30,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, roomId) {
      console.log(ownerId, roomId)
      this.loadData()
    },
    async loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        payerObjId: this.roomId,
        state: this.state
      }

      const res = await listAdminFee(params);
      this.fees = res.fees
      this.page.total = res.total
      this.totalAmount = res.fees.reduce((sum, fee) => {
        return sum + parseFloat(fee.amountOwed || 0)
      }, 0).toFixed(2)
    },
    getRoomAttrValue(attrs, specCd) {
      const attr = attrs.find(item => item.specCd === specCd)
      return attr ? attr.value : ''
    },
    getRoomDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime)
    },
    getRoomEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime)
    },
    changeState() {
      this.loadData()
    },
    toAdminFeeDetail(fee) {
      this.$router.push(`/pages/fee/adminFeeDetail?feeId=${fee.feeId}`)
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