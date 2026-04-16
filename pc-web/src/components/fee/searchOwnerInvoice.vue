<template>
  <el-dialog :title="$t('searchOwnerInvoice.title')" :visible.sync="visible" width="80%" @close="handleClose">
    <div>
      <el-row :gutter="20">
   
        <el-col :span="9">
          <el-input v-model="searchForm.ownerNameLike" :placeholder="$t('searchOwnerInvoice.placeholderOwner')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('searchOwnerInvoice.search') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="ownerInvoices" border style="width: 100%; margin-top: 20px">
        <el-table-column prop="ownerName" :label="$t('searchOwnerInvoice.ownerName')" align="center" />
        <el-table-column prop="invoiceType" :label="$t('searchOwnerInvoice.invoiceType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.invoiceType === '1001' ? $t('searchOwnerInvoice.personal') : $t('searchOwnerInvoice.company') }}
          </template>
        </el-table-column>
        <el-table-column prop="invoiceName" :label="$t('searchOwnerInvoice.invoiceTitle')" align="center" />
        <el-table-column prop="invoiceNum" :label="$t('searchOwnerInvoice.taxpayerId')" align="center" />
        <el-table-column prop="invoiceAddress" :label="$t('searchOwnerInvoice.addressPhone')" align="center" />
        <el-table-column :label="$t('searchOwnerInvoice.operation')" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="handleSelect(scope.row)">
              {{ $t('searchOwnerInvoice.select') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next" :total="pagination.total" @size-change="handleSizeChange"
        @current-change="handlePageChange" />
    </div>
  </el-dialog>
</template>

<script>
import { listOwnerInvoice } from '@/api/fee/ownerApplyInvoiceApi'

export default {
  name: 'SearchOwnerInvoice',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      searchForm: {
        roomName: '',
        ownerNameLike: ''
      },
      ownerInvoices: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadData()
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ownerNameLike: this.searchForm.ownerNameLike,
          roomName: this.searchForm.roomName
        }

        const res = await listOwnerInvoice(params)
        this.ownerInvoices = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('Failed to load owner invoices:', error)
        this.$message.error(this.$t('common.loadFailed'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    handleSelect(owner) {
      this.$emit('select', owner)
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
</style>