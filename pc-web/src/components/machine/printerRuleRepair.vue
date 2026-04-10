<template>
  <div class="printer-rule-repair">

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item >
        <el-input v-model="searchForm.prrId" :placeholder="$t('printerRuleRepair.idPlaceholder')" clearable />
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchForm.repairTypeName" :placeholder="$t('printerRuleRepair.repairTypePlaceholder')"
          clearable />
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
      <el-table-column prop="prrId" :label="$t('printerRuleRepair.table.id')" align="center" />
      <el-table-column prop="repairTypeName" :label="$t('printerRuleRepair.table.repairType')" align="center" />
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


    <add-printer-rule-repair ref="addPrinterRuleRepair" :rule-id="searchForm.ruleId" @success="handleSuccess" />
    <delete-printer-rule-repair ref="deletePrinterRuleRepair" @success="handleSuccess" />
  </div>
</template>

<script>
import { listPrinterRuleRepairs } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddPrinterRuleRepair from './addPrinterRuleRepair'
import DeletePrinterRuleRepair from './deletePrinterRuleRepair'

export default {
  name: 'PrinterRuleRepair',
  components: {
    AddPrinterRuleRepair,
    DeletePrinterRuleRepair
  },
  data() {
    return {
      searchForm: {
        prrId: '',
        repairTypeName: '',
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
        const { data, total } = await listPrinterRuleRepairs(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取报修自动打印列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.prrId = ''
      this.searchForm.repairTypeName = ''
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
      this.$refs.addPrinterRuleRepair.open()
    },
    openDeleteModal(row) {
      this.$refs.deletePrinterRuleRepair.open(row)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style scoped>
.printer-rule-repair {
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