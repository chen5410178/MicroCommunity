<template>
  <div class="owner-invoice-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('searchCondition') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.ownerName" :placeholder="$t('ownerNamePlaceholder')" />
        </el-col>

        <el-col :span="6">
          <el-select v-model="searchForm.invoiceType" :placeholder="$t('invoiceTypePlaceholder')" style="width: 100%">
            <el-option value="" :label="$t('invoiceTypePlaceholder')" />
            <el-option value="1001" :label="$t('personal')" />
            <el-option value="2002" :label="$t('enterprise')" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-input v-model="searchForm.invoiceName" :placeholder="$t('invoiceHeaderPlaceholder')" />
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="fetchData">{{ $t('search') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 发票抬头列表 -->
    <el-card class="list-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('invoiceHeader') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          {{ $t('add') }}
        </el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="oiId" :label="$t('serialNumber')" align="center" min-width="80" />

        <el-table-column prop="ownerName" :label="$t('ownerName')" align="center" min-width="120" />

        <el-table-column prop="invoiceType" :label="$t('invoiceType')" align="center" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.invoiceType === '1001' ? $t('personal') : $t('enterprise') }}
          </template>
        </el-table-column>

        <el-table-column prop="invoiceName" :label="$t('invoiceHeader')" align="center" min-width="150" />

        <el-table-column prop="invoiceNum" :label="$t('taxpayerId')" align="center" min-width="150" />

        <el-table-column prop="invoiceAddress" :label="$t('address')" align="center" min-width="150" />

        <el-table-column prop="invoiceLink" :label="$t('phone')" align="center" min-width="120" />

        <el-table-column prop="invoiceAccount" :label="$t('bankAccount')" align="center" min-width="200" />

        <el-table-column prop="remark" :label="$t('remark')" align="center" min-width="150" />

        <el-table-column :label="$t('operation')" align="center" fixed="right" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('modify') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination background layout="total, sizes, prev, pager, next" :current-page="pagination.current"
        :page-size="pagination.size" :total="pagination.total" @size-change="handleSizeChange"
        @current-change="handlePageChange" style="margin-top: 20px; text-align: right;" />
    </el-card>

    <!-- 编辑组件 -->
    <edit-owner-invoice ref="editDialog" @success="fetchData" />

    <!-- 删除组件 -->
    <delete-owner-invoice ref="deleteDialog" @success="fetchData" />
  </div>
</template>

<script>
import { listOwnerInvoice } from '@/api/fee/ownerInvoiceApi'
import EditOwnerInvoice from '@/components/fee/editOwnerInvoice'
import DeleteOwnerInvoice from '@/components/fee/deleteOwnerInvoice'

export default {
  name: 'OwnerInvoiceList',
  components: {
    EditOwnerInvoice,
    DeleteOwnerInvoice
  },
  data() {
    return {
      loading: false,
      searchForm: {
        ownerName: '',
        invoiceType: '',
        invoiceName: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const response = await listOwnerInvoice(params)
        this.tableData = response.data
        this.pagination.total = response.total
      } catch (error) {
        this.$message.error(error.message || this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },

    handleAdd() {
      this.$router.push('/views/fee/addOwnerInvoice')
    },

    handleEdit(row) {
      this.$refs.editDialog.open({ ...row })
    },

    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },

    handlePageChange(page) {
      this.pagination.current = page
      this.fetchData()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-invoice-container {
  padding: 20px;

  .search-card,
  .list-card {
    margin-bottom: 20px;
  }

  .clearfix {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>