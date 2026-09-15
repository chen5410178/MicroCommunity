<template>
  <el-table :data="listData" border style="width: 100%">
    <el-table-column prop="applyId" :label="$t('invoiceApply.table.id')" align="center" />
    <el-table-column :label="$t('invoiceApply.table.invoiceKind')" align="center">
      <template slot-scope="scope">
        {{ scope.row.invoiceKind || $t('invoiceApply.normalInvoice') }}
      </template>
    </el-table-column>
    <el-table-column :label="$t('invoiceApply.table.invoiceType')" align="center">
      <template slot-scope="scope">
        {{ scope.row.invoiceType === '1001' ? $t('invoiceApply.personal') : $t('invoiceApply.company') }}
      </template>
    </el-table-column>
    <el-table-column prop="ownerName" :label="$t('invoiceApply.table.ownerName')" align="center" />
    <el-table-column v-if="showRoomColumn" :label="$t('invoiceApply.table.roomName')" align="center">
      <template slot-scope="scope">
        {{ scope.row.roomName || '-' }}
      </template>
    </el-table-column>
    <el-table-column :label="$t('invoiceApply.table.applicant')" align="center">
      <template slot-scope="scope">
        {{ scope.row.createUserName }}({{ scope.row.applyTel }})
      </template>
    </el-table-column>
    <el-table-column prop="invoiceName" :label="$t('invoiceApply.table.invoiceName')" align="center" />
    <el-table-column prop="invoiceNum" :label="$t('invoiceApply.table.taxNumber')" align="center" />
    <el-table-column prop="invoiceAddress" :label="$t('invoiceApply.table.addressPhone')" align="center" />
    <el-table-column prop="invoiceAmount" :label="$t('invoiceApply.table.amount')" align="center" />
    <el-table-column prop="invoiceCode" :label="$t('invoiceApply.table.invoiceCode')" align="center">
      <template slot-scope="scope">
        {{ scope.row.invoiceCode || $t('invoiceApply.notUploaded') }}
      </template>
    </el-table-column>
    <el-table-column prop="stateName" :label="$t('invoiceApply.table.submitState')" align="center" />
    <el-table-column :label="$t('invoiceApply.table.mallState')" align="center">
      <template slot-scope="scope">
        {{ mallStateName(scope.row.mallState) }}
      </template>
    </el-table-column>
    <el-table-column :label="$t('invoiceApply.table.failReason')" align="center" width="200">
      <template slot-scope="scope">
        <el-tooltip :content="failReason(scope.row)" :disabled="failReason(scope.row) === '-'" placement="top">
          <div class="text-ellipsis">{{ failReason(scope.row) }}</div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" :label="$t('invoiceApply.table.applyTime')" align="center" />
    <el-table-column :label="$t('invoiceApply.table.invoicePdf')" align="center">
      <template slot-scope="scope">
        <el-button v-if="scope.row.mallPdfUrl" type="text" @click="_downloadInvoicePdf(scope.row)">
          {{ $t('invoiceApply.downloadInvoice') }}
        </el-button>
        <span v-else>{{ $t('invoiceApply.noInvoicePdf') }}</span>
      </template>
    </el-table-column>
    <el-table-column :label="$t('common.operation')" align="center" width="200" fixed="right">
      <template slot-scope="scope">
        <el-button size="mini" @click="$emit('detail', scope.row)">
          {{ $t('common.detail') }}
        </el-button>
        <el-button v-if="scope.row.mallState === 'DONE'" size="mini" type="warning" @click="$emit('red', scope.row)">
          {{ $t('invoiceApply.invoiceRed') }}
        </el-button>
        <el-button v-if="scope.row.state === 'F'" size="mini" type="primary" @click="$emit('submit', scope.row)">
          {{ $t('invoiceApply.reSubmit') }}
        </el-button>
        <el-button size="mini" type="danger" @click="$emit('delete', scope.row)">
          {{ $t('common.delete') }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: 'InvoiceApplyTable',
  props: {
    listData: {
      type: Array,
      default: () => []
    },
    // 房屋收费详情页已锁定房屋, 隐藏房屋编号列
    showRoomColumn: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    mallStateName(mallState) {
      if (!mallState) {
        return '-'
      }
      const map = {
        AUDIT: this.$t('invoiceApply.mallState.AUDIT'),
        WAIT: this.$t('invoiceApply.mallState.WAIT'),
        DONE: this.$t('invoiceApply.mallState.DONE'),
        FAIL: this.$t('invoiceApply.mallState.FAIL'),
        RED: this.$t('invoiceApply.mallState.RED')
      }
      return map[mallState] || mallState
    },
    failReason(row) {
      if (row.failReason) {
        return row.failReason
      }
      if (row.state === 'F') {
        return row.remark
      }
      return '-'
    },
    _downloadInvoicePdf(row) {
      if (!row.mallPdfUrl) {
        this.$message.warning(this.$t('invoiceApply.noInvoicePdf'))
        return
      }
      window.open(row.mallPdfUrl)
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
