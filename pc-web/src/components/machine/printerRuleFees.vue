<template>
  <div class="printer-rule-fees">

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item >
        <el-input v-model="searchForm.prfId" :placeholder="$t('printerRuleFees.idPlaceholder')" clearable />
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchForm.feeConfigName" :placeholder="$t('printerRuleFees.feeNamePlaceholder')" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          {{ $t('common.search') }}
        </el-button>
        <el-button type="primary" @click="openAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </el-form-item>
    </el-form>



    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="prfId" :label="$t('printerRuleFees.table.id')" align="center" />
      <el-table-column prop="feeConfigName" :label="$t('printerRuleFees.table.feeName')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />


    <add-printer-rule-fee ref="addPrinterRuleFee" :rule-id="searchForm.ruleId" @success="handleSuccess" />
    <delete-printer-rule-fee ref="deletePrinterRuleFee" @success="handleSuccess" />
  </div>
</template>

<script>
import { listPrinterRuleFees } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddPrinterRuleFee from './addPrinterRuleFee'
import DeletePrinterRuleFee from './deletePrinterRuleFee'

export default {
  name: 'PrinterRuleFees',
  components: {
    AddPrinterRuleFee,
    DeletePrinterRuleFee
  },
  data() {
    return {
      searchForm: {
        prfId: '',
        feeConfigName: '',
        ruleId: '',
        communityId: ''
      },
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()

  },
  methods: {
    open(ruleId) {
      this.searchForm.ruleId = ruleId
      this.getList()
    },
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listPrinterRuleFees(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取缴费自动打印列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.prfId = ''
      this.searchForm.feeConfigName = ''
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    openAddModal() {
      this.$refs.addPrinterRuleFee.open()
    },
    openDeleteModal(row) {
      this.$refs.deletePrinterRuleFee.open(row)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style scoped>
.printer-rule-fees {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>