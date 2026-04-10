<template>
  <div>
    <el-table :data="feeDetailRuleBillInfo.bills" border style="width: 100%">
      <el-table-column prop="billName" :label="$t('feeDetailRuleBill.billName')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailRuleBill.feeItem')" align="center">
        <template slot-scope="scope">
          {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailRuleBill.receivablePeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.endTime }}~{{ scope.row.deadlineTime }}
        </template>
      </el-table-column>
      <el-table-column prop="feeTotalPrice" :label="$t('feeDetailRuleBill.receivableAmount')" align="center"></el-table-column>
      <el-table-column prop="ruleId" :label="$t('feeDetailRuleBill.ruleId')" align="center"></el-table-column>
      <el-table-column prop="feeId" :label="$t('feeDetailRuleBill.feeId')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('feeDetailRuleBill.status')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailRuleBill.description')" align="center">
        <template slot-scope="scope">
          <div>{{ $t('feeDetailRuleBill.price') }}: {{ scope.row.squarePrice }}</div>
          <div>{{ $t('feeDetailRuleBill.fixedFee') }}: {{ scope.row.additionalAmount }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small">
            <a :href="'/#/views/fee/feeDetail?feeId=' + scope.row.feeId" target="_blank">
              {{ $t('common.detail') }}
            </a>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
  </div>
</template>

<script>
import { listPayFeeRuleBill } from '@/api/fee/feeDetailRuleBillApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailRuleBill',
  props: {
    feeId: {
      type: String,
      default: ''
    },
    roomId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      feeDetailRuleBillInfo: {
        bills: [],
        feeId: '',
        roomId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    async loadData() {
      const params = {
        communityId: getCommunityId(),
        feeId: this.feeDetailRuleBillInfo.feeId,
        payerObjId: this.feeDetailRuleBillInfo.roomId,
        page: this.currentPage,
        row: this.pageSize
      }

      try {
        const res = await listPayFeeRuleBill(params)
        if (res.code === 0) {
          this.feeDetailRuleBillInfo.bills = res.data
          this.total = res.total
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadData()
    },
    open(params) {
      // 这个方法用于接收参数并初始化组件
      if (params.feeId) {
        this.feeDetailRuleBillInfo.feeId = params.feeId
      }
      if (params.roomId) {
        this.feeDetailRuleBillInfo.roomId = params.roomId
      }
      this.loadData()
    }
  },
  watch: {
    feeId(newVal) {
      this.feeDetailRuleBillInfo.feeId = newVal
      this.loadData()
    },
    roomId(newVal) {
      this.feeDetailRuleBillInfo.roomId = newVal
      this.loadData()
    }
  }
}
</script>