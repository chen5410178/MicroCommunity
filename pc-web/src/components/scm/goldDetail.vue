<template>
  <div class="gold-detail-container">
    <el-table v-loading="loading" :data="goldDetails" border style="width: 100%">
      <el-table-column prop="detailId" :label="$t('goldDetail.id')" align="center" />
      <el-table-column prop="goldName" :label="$t('goldDetail.goldAccount')" align="center" />
      <el-table-column :label="$t('goldDetail.type')" align="center">
        <template slot-scope="scope">
          {{ scope.row.detailType === '1001' ? $t('goldDetail.transferIn') : $t('goldDetail.transferOut') }}
        </template>
      </el-table-column>
      <el-table-column prop="communityName" :label="$t('goldDetail.communityName')" align="center" />
      <el-table-column prop="communityId" :label="$t('goldDetail.communityId')" align="center" />
      <el-table-column prop="amount" :label="$t('goldDetail.transactionAmount')" align="center" />
      <el-table-column prop="totalAmount" :label="$t('goldDetail.currentAmount')" align="center" />
      <el-table-column prop="orderId" :label="$t('goldDetail.transactionNo')" align="center" />
      <el-table-column prop="remark" :label="$t('goldDetail.remark')" align="center" />
      <el-table-column prop="createTime" :label="$t('goldDetail.time')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getPropertyGoldDetail } from '@/api/scm/goldApi'

export default {
  name: 'GoldDetail',

  data() {
    return {
      goldId: '',
      goldName: '',
      loading: false,
      goldDetails: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(gold) {
      this.goldId = gold.goldId
      this.goldName = gold.goldName
      this.loadData()
    },
    async loadData() {
      if (!this.goldId) return

      try {
        this.loading = true
        const params = {
          communityId: getCommunityId(),
          goldId: this.goldId,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await getPropertyGoldDetail(params)
        this.goldDetails = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load gold details:', error)
      } finally {
        this.loading = false
      }
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

<style lang="scss" scoped>
.gold-detail-container {
  padding: 20px 0;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>