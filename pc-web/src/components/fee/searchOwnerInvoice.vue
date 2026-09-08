<template>
  <el-dialog :title="$t('searchOwnerInvoice.title')" :visible.sync="visible" width="80%" @close="handleClose">
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.userPhone" :placeholder="$t('searchOwnerInvoice.placeholderUserPhone')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.keyword" :placeholder="$t('searchOwnerInvoice.placeholderOwner')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('searchOwnerInvoice.search') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="headings" border style="width: 100%; margin-top: 20px">
        <el-table-column prop="headingType" :label="$t('searchOwnerInvoice.invoiceType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.headingType === '个人' ? $t('searchOwnerInvoice.personal') : $t('searchOwnerInvoice.company') }}
          </template>
        </el-table-column>
        <el-table-column prop="headingName" :label="$t('searchOwnerInvoice.invoiceTitle')" align="center" />
        <el-table-column prop="taxNo" :label="$t('searchOwnerInvoice.taxpayerId')" align="center" />
        <el-table-column prop="address" :label="$t('searchOwnerInvoice.addressPhone')" align="center" />
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
import { listInvoiceHeadings } from '@/api/fee/ownerApplyInvoiceApi'

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
        userPhone: '',
        keyword: ''
      },
      headings: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(userPhone) {
      if (userPhone) {
        this.searchForm.userPhone = userPhone
      }
      this.visible = true
      this.pagination.current = 1
      this.loadData()
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    async loadData() {
      if (!this.searchForm.userPhone) {
        this.$message.warning(this.$t('searchOwnerInvoice.userPhoneRequired'))
        return
      }
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          userPhone: this.searchForm.userPhone,
          keyword: this.searchForm.keyword
        }

        const res = await listInvoiceHeadings(params)
        this.headings = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('Failed to load invoice headings:', error)
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
    handleSelect(heading) {
      // 商城字段映射回物业字段: 个人/公司 -> 1001/2002
      const mapped = {
        headingId: heading.headingId,
        invoiceType: heading.headingType === '公司' ? '2002' : '1001',
        invoiceName: heading.headingName,
        invoiceNum: heading.taxNo,
        invoiceAddress: heading.address,
        invoiceLink: heading.phone,
        email: heading.email,
        invoiceAccount: heading.bankNo,
        remark: heading.remark,
        userPhone: heading.userPhone || this.searchForm.userPhone
      }
      this.$emit('select', mapped)
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
