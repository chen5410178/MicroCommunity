<template>
  <el-table :data="listData" border style="width: 100%" :empty-text="$t('invoiceApply.redEmptyTip')">
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
        {{ reasonName(scope.row.reason) }}
      </template>
    </el-table-column>
    <el-table-column prop="remark" :label="$t('invoiceApply.redTable.remark')" align="center" width="200">
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
</template>

<script>
export default {
  name: 'InvoiceRedTable',
  props: {
    listData: {
      type: Array,
      default: () => []
    }
  },
  methods: {
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
    reasonName(reason) {
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
