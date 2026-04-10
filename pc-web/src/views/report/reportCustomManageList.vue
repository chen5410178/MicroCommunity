<template>
  <div class="report-custom-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between ">
        <span>{{ $t('reportCustomManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.customId" :placeholder="$t('reportCustomManage.customId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.groupId" :placeholder="$t('reportCustomManage.groupId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.title" :placeholder="$t('reportCustomManage.title')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('reportCustomManage.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('reportCustomManage.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报表列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between ">
        <span>{{ $t('reportCustomManage.reportTitle') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          {{ $t('reportCustomManage.addReport') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="customId" :label="$t('reportCustomManage.customId')" align="center" />
        <el-table-column prop="groupName" :label="$t('reportCustomManage.groupId')" align="center" />
        <el-table-column prop="title" :label="$t('reportCustomManage.title')" align="center" />
        <el-table-column prop="seq" :label="$t('reportCustomManage.seq')" align="center" />
        <el-table-column prop="remark" :label="$t('reportCustomManage.remark')" align="center" />
        <el-table-column :label="$t('reportCustomManage.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleRelate(scope.row)">
              {{ $t('reportCustomManage.relateComponent') }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('reportCustomManage.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('reportCustomManage.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 添加报表弹窗 -->
    <add-report-custom ref="addReportCustom" @success="handleSuccess" />

    <!-- 编辑报表弹窗 -->
    <edit-report-custom ref="editReportCustom"  :form-data="currentRow" @success="handleSuccess" />

    <!-- 删除确认弹窗 -->
    <delete-report-custom ref="deleteReportCustom" :custom-id="currentRow.customId" @success="handleSuccess" />
  </div>
</template>

<script>
import { listReportCustom } from '@/api/report/reportCustomManageApi'
import AddReportCustom from '@/components/report/AddReportCustom'
import EditReportCustom from '@/components/report/EditReportCustom'
import DeleteReportCustom from '@/components/report/DeleteReportCustom'

export default {
  name: 'ReportCustomManageList',
  components: {
    AddReportCustom,
    EditReportCustom,
    DeleteReportCustom
  },
  data() {
    return {
      loading: false,
      searchForm: {
        customId: '',
        groupId: '',
        title: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      deleteVisible: false,
      currentRow: {}
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
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await listReportCustom(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        customId: '',
        groupId: '',
        title: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addReportCustom.open()
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.$refs.editReportCustom.open()

    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.$refs.deleteReportCustom.open()
    },
    handleRelate(row) {
      this.$router.push({
        path: '/views/report/reportCustomComponentRelManage',
        query: {
          customId: row.customId,
          title: row.title
        }
      })
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-custom-container {
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
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>