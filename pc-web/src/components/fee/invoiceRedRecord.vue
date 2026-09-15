<template>
  <div>
   

    <el-table :data="invoiceReds" border style="width: 100%" :empty-text="$t('invoiceApply.redEmptyTip')">
      <el-table-column prop="applyId" :label="$t('invoiceApply.redTable.applyId')" align="center" />
      <el-table-column prop="redId" :label="$t('invoiceApply.redTable.redId')" align="center" />
      <el-table-column prop="invoiceNo" :label="$t('invoiceApply.redTable.invoiceNo')" align="center">
        <template slot-scope="scope">
          {{ scope.row.invoiceNo || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="buyerName" :label="$t('invoiceApply.redTable.buyerName')" align="center" />
      <el-table-column prop="amount" :label="$t('invoiceApply.redTable.amount')" align="center" />
      <el-table-column :label="$t('invoiceApply.redTable.reason')" align="center">
        <template slot-scope="scope">
          {{ redReasonName(scope.row.reason) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('invoiceApply.redTable.remark')" align="center" width="200">
        <template slot-scope="scope">
          <el-tooltip :content="scope.row.remark || '-'" :disabled="!scope.row.remark" placement="top">
            <div class="text-ellipsis">{{ scope.row.remark || '-' }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column :label="$t('invoiceApply.redTable.state')" align="center">
        <template slot-scope="scope">
          {{ redStateName(scope.row.state) }}
        </template>
      </el-table-column>
      <el-table-column prop="redNo" :label="$t('invoiceApply.redTable.redNo')" align="center">
        <template slot-scope="scope">
          {{ scope.row.redNo || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('invoiceApply.redTable.createTime')" align="center" />
    </el-table>

    <el-pagination class="margin-top" :current-page="page.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listInvoiceReds } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InvoiceRedRecord',
  props: {
    // 父页面查询条件(房屋树/表单筛选)
    conditions: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      invoiceReds: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.reload()
  },
  methods: {
    // 由父页面在查询/重置/树筛选后调用
    reload() {
      this.page.current = 1
      this.listInvoiceReds()
    },
    async listInvoiceReds() {
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const res = await listInvoiceReds(params)
        this.invoiceReds = res.data || []
        this.page.total = res.total || 0
      } catch (error) {
        // 商城未开启/商城调用异常时后端已返回空分页, 此处兜底为空列表, 不弹错误提示
        this.invoiceReds = []
        this.page.total = 0
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listInvoiceReds()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listInvoiceReds()
    },
    redStateName(state) {
      if (!state) {
        return '-'
      }
      const map = {
        WAIT: this.$t('invoiceApply.redState.WAIT'),
        DONE: this.$t('invoiceApply.redState.DONE')
      }
      return map[state] || state
    },
    redReasonName(reason) {
      if (!reason) {
        return '-'
      }
      const map = {
        开票有误: this.$t('invoiceApply.redReasonOptions.invoiceError'),
        销售退回: this.$t('invoiceApply.redReasonOptions.salesReturn'),
        服务终止: this.$t('invoiceApply.redReasonOptions.serviceStop'),
        其他: this.$t('invoiceApply.redReasonOptions.other')
      }
      return map[reason] || reason
    }
  }
}
</script>

<style scoped>
.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
