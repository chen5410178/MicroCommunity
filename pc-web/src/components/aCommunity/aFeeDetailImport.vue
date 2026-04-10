<template>
  <div class="margin-top">
    <el-table :data="aFeeDetailImportInfo.importFeeDetails" border style="width: 100%">
      <el-table-column prop="floorNum" :label="$t('aFeeDetailImport.floorNum')" align="center"></el-table-column>
      <el-table-column prop="unitNum" :label="$t('aFeeDetailImport.unitNum')" align="center"></el-table-column>
      <el-table-column prop="roomNum" :label="$t('aFeeDetailImport.roomNum')" align="center"></el-table-column>
      <el-table-column prop="feeName" :label="$t('aFeeDetailImport.feeName')" align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('aFeeDetailImport.startTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('aFeeDetailImport.endTime')" align="center"></el-table-column>
      <el-table-column prop="amount" :label="$t('aFeeDetailImport.totalAmount')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('aFeeDetailImport.remark')" align="center"></el-table-column>
      <el-table-column :label="$t('aFeeDetailImport.status')" align="center">
        <template #default="{row}">
          <el-tag :type="row.state === '1000' ? 'success' : 'danger'">
            {{row.state === '1000' ? $t('aFeeDetailImport.importSuccess') : $t('aFeeDetailImport.importFailed')}}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="12"></el-col>
      <el-col :span="12">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminImportFeeDetail } from '@/api/aCommunity/aFeeDetailImportApi'

export default {
  name: 'AFeeDetailImport',
  data() {
    return {
      aFeeDetailImportInfo: {
        importFeeDetails: [],
        feeId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aFeeDetailImportInfo.feeId = data.feeId
      this._loadAFeeDetailImportData()
    },
    handleSwitch(data) {
      this.aFeeDetailImportInfo.feeId = data.feeId
      this._loadAFeeDetailImportData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadAFeeDetailImportData()
    },
    async _loadAFeeDetailImportData() {
      try {
        const params = {
          feeId: this.aFeeDetailImportInfo.feeId,
          page: this.currentPage,
          row: this.pageSize
        }
        
        const res = await queryAdminImportFeeDetail(params)
        this.aFeeDetailImportInfo.importFeeDetails = res.data
        this.total = res.total
      } catch (error) {
        console.error('加载导入费用明细数据失败:', error)
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