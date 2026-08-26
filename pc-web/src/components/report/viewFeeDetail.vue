<template>
  <el-dialog :title="`${viewFeeDetailInfo.curYear}${$t('viewFeeDetail.title')}(${viewFeeDetailInfo.roomName})`"
    :visible.sync="dialogVisible" width="80%" @close="handleClose">
    <el-table :data="viewFeeDetailInfo.feeDetails" border style="width: 100%">
      <el-table-column prop="cycles" :label="$t('viewFeeDetail.table.cycle')" align="center" />
      <el-table-column prop="receivableAmount" :label="$t('viewFeeDetail.table.receivableAmount')" align="center" />
      <el-table-column prop="receivedAmount" :label="$t('viewFeeDetail.table.receivedAmount')" align="center" />
      <el-table-column prop="createTime" :label="$t('viewFeeDetail.table.payTime')" align="center" />
      <el-table-column prop="startTime" :label="$t('viewFeeDetail.table.startTime')" align="center">
        <template slot-scope="scope">
          {{ dateFormat(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" :label="$t('viewFeeDetail.table.endTime')" align="center">
        <template slot-scope="scope">
          {{ dateFormat(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('viewFeeDetail.table.status')" align="center" />
      <el-table-column prop="remark" :label="$t('viewFeeDetail.table.remark')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
      layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
  </el-dialog>
</template>

<script>
import { queryFeeDetail } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'ViewFeeDetail',
  data() {
    return {
      dialogVisible: false,
      viewFeeDetailInfo: {
        feeDetails: [],
        total: 0,
        records: 1,
        feeId: '',
        configId: '',
        payerObjId: '',
        curYear: '',
        roomName: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.viewFeeDetailInfo = {
        ...this.viewFeeDetailInfo,
        ...params
      }
      this.dialogVisible = true
      this.listFeeDetail()
    },
    handleClose() {
      this.dialogVisible = false
      this.pagination.current = 1
    },
    async listFeeDetail() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          curYear: this.viewFeeDetailInfo.curYear,
          configId: this.viewFeeDetailInfo.configId,
          payerObjId: this.viewFeeDetailInfo.payerObjId
        }

        const { data: feeDetails, total } = await queryFeeDetail(params)

        this.viewFeeDetailInfo.feeDetails = feeDetails
        this.pagination.total = total
      } catch (error) {
        console.error('获取费用明细失败:', error)
      }
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this.listFeeDetail()
    },
    dateFormat
  }
}
</script>

<style lang="scss" scoped>
.margin-top {
  margin-top: 20px;
}
</style>