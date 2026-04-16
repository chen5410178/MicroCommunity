<template>
  <div>
    <el-table v-loading="loading" :data="details" border style="width: 100%">
      <el-table-column prop="detailId" :label="$t('communityIntegralDetail.id')" align="center" />
      <el-table-column prop="integralName" :label="$t('communityIntegralDetail.integralAccount')" align="center" />
      <el-table-column prop="detailType" :label="$t('communityIntegralDetail.type')" align="center">
        <template slot-scope="scope">
          {{ scope.row.detailType === '1001' ? $t('communityIntegralDetail.transferIn') :
            $t('communityIntegralDetail.transferOut') }}
        </template>
      </el-table-column>
      <el-table-column prop="amount" :label="$t('communityIntegralDetail.transactionAmount')" align="center" />
      <el-table-column prop="totalAmount" :label="$t('communityIntegralDetail.currentAmount')" align="center" />
      <el-table-column prop="orderId" :label="$t('communityIntegralDetail.transactionNo')" align="center" />
      <el-table-column prop="remark" :label="$t('communityIntegralDetail.remark')" align="center" />
      <el-table-column prop="createTime" :label="$t('communityIntegralDetail.time')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { queryCommunityIntegralDetail } from '@/api/scm/communityIntegralApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommunityIntegralDetail',

  data() {
    return {  
      integralId: '',
      integralName: '',
      loading: false,
      details: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(integral) {
      this.integralId = integral.integralId
      this.integralName = integral.integralName
      this.loadData()
    },
    async loadData() {
      try {
        this.loading = true
        const params = {
          communityId: getCommunityId(),
          integralId: this.integralId,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryCommunityIntegralDetail(params)
        this.details = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load community integral details:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>