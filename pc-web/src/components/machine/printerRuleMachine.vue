<template>
  <div class="printer-rule-machine">

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item >
        <el-input v-model="searchForm.prmId" :placeholder="$t('printerRuleMachine.idPlaceholder')" clearable />
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchForm.machineName" :placeholder="$t('printerRuleMachine.machineNamePlaceholder')"
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
      <el-table-column prop="prmId" :label="$t('printerRuleMachine.table.id')" align="center" />
      <el-table-column prop="machineName" :label="$t('printerRuleMachine.table.machineName')" align="center" />
      <el-table-column prop="quantity" :label="$t('printerRuleMachine.table.quantity')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="openEditModal(scope.row)">
            {{ $t('common.edit') }}
          </el-button>
          <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />


    <add-printer-rule-machine ref="addPrinterRuleMachine" :ruleId="searchForm.ruleId" @success="handleSuccess" />
    <edit-printer-rule-machine ref="editPrinterRuleMachine" @success="handleSuccess" />
    <delete-printer-rule-machine ref="deletePrinterRuleMachine" @success="handleSuccess" />
  </div>
</template>

<script>
import { listPrinterRuleMachines } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddPrinterRuleMachine from './addPrinterRuleMachine'
import EditPrinterRuleMachine from './editPrinterRuleMachine'
import DeletePrinterRuleMachine from './deletePrinterRuleMachine'

export default {
  name: 'PrinterRuleMachine',
  components: {
    AddPrinterRuleMachine,
    EditPrinterRuleMachine,
    DeletePrinterRuleMachine
  },
  data() {
    return {
      searchForm: {
        prmId: '',
        machineName: '',
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
        const { data, total } = await listPrinterRuleMachines(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取打印机规则机器列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.prmId = ''
      this.searchForm.machineName = ''
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
      this.$refs.addPrinterRuleMachine.open(this.searchForm.ruleId )
    },
    openEditModal(row) {
      this.$refs.editPrinterRuleMachine.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deletePrinterRuleMachine.open(row)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style scoped>
.printer-rule-machine {
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