<template>
  <div class="machine-printer-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('machinePrinterManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.machineName" :placeholder="$t('machinePrinterManage.search.machineName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.machineCode" :placeholder="$t('machinePrinterManage.search.machineCode')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('machinePrinterManage.list.title') }}</span>
        <div class="header-right">
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="machineId" :label="$t('machinePrinterManage.table.machineId')" align="center" />
        <el-table-column prop="machineName" :label="$t('machinePrinterManage.table.machineName')" align="center" />
        <el-table-column prop="machineCode" :label="$t('machinePrinterManage.table.machineCode')" align="center" />
        <el-table-column prop="implBeanName" :label="$t('machinePrinterManage.table.implBeanName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-machine-printer ref="addMachinePrinter" @success="handleSuccess" />
    <edit-machine-printer ref="editMachinePrinter" @success="handleSuccess" />
    <delete-machine-printer ref="deleteMachinePrinter" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMachinePrinter } from '@/api/machine/machinePrinterManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddMachinePrinter from '@/components/machine/addMachinePrinter'
import EditMachinePrinter from '@/components/machine/editMachinePrinter'
import DeleteMachinePrinter from '@/components/machine/deleteMachinePrinter'

export default {
  name: 'MachinePrinterManageList',
  components: {
    AddMachinePrinter,
    EditMachinePrinter,
    DeleteMachinePrinter
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineName: '',
        machineCode: '',
        communityId: getCommunityId()
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listMachinePrinter(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('machinePrinterManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        machineName: '',
        machineCode: '',
        communityId: getCommunityId()
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addMachinePrinter.open()
    },
    handleEdit(row) {
      this.$refs.editMachinePrinter.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMachinePrinter.open(row)
    },
    handleShowDoc() {
      // 显示文档逻辑
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.machine-printer-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .header-right {
      float: right;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>