<template>
  <div>
    <div class="margin-top">
      <el-table
        :data="feeDetailImportInfo.importFeeDetails"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="floorNum"
          :label="$t('feeDetailImport.floorNum')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="unitNum"
          :label="$t('feeDetailImport.unitNum')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="roomNum"
          :label="$t('feeDetailImport.roomNum')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="feeName"
          :label="$t('feeDetailImport.feeName')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="startTime"
          :label="$t('feeDetailImport.startTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="endTime"
          :label="$t('feeDetailImport.endTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="amount"
          :label="$t('feeDetailImport.totalAmount')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="remark"
          :label="$t('feeDetailImport.remark')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="state"
          :label="$t('feeDetailImport.status')"
          align="center"
        >
          <template #default="{row}">
            <el-tag :type="row.state === '1000' ? 'success' : 'danger'">
              {{row.state === '1000' ? $t('feeDetailImport.importSuccess') : $t('feeDetailImport.importFailed')}}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
          ></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryImportFeeDetail } from '@/api/fee/feeDetailImportApi'
import { getCommunityId } from '@/api/community/communityApi' 

export default {
  name: 'FeeDetailImport',
  data() {
    return {
      feeDetailImportInfo: {
        importFeeDetails: [],
        feeId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(params) {
      this.feeDetailImportInfo.feeId = params.feeId
      this._loadFeeDetailImportData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadFeeDetailImportData(this.currentPage, this.pageSize)
    },
    async _loadFeeDetailImportData(page, row) {
      try {
        const params = {
          communityId: getCommunityId(),
          feeId: this.feeDetailImportInfo.feeId,
          page,
          row
        }
        
        const res = await queryImportFeeDetail(params)
        this.feeDetailImportInfo.importFeeDetails = res.data
        this.total = res.total
      } catch (error) {
        console.error('Failed to load import fee data:', error)
      }
    }
  }
}
</script>