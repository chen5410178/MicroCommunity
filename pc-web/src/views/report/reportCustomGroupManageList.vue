<template>
  <div class="report-custom-group-manage-container">
    <!-- 查询条件 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between ">
            <span>{{ $t('reportCustomGroupManage.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="searchForm.groupId"
                :placeholder="$t('reportCustomGroupManage.search.groupId')"
                clearable
              />
            </el-col>
            <el-col :span="8">
              <el-input
                v-model="searchForm.name"
                :placeholder="$t('reportCustomGroupManage.search.name')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="searchForm.url"
                :placeholder="$t('reportCustomGroupManage.search.url')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报表组信息 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('reportCustomGroupManage.list.title') }}</span>
            <el-button
              type="primary"
              size="small"
              style="float: right"
              @click="handleAdd"
            >
              <i class="el-icon-plus"></i>
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table
            v-loading="loading"
            :data="tableData"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="groupId"
              :label="$t('reportCustomGroupManage.table.groupId')"
              align="center"
            />
            <el-table-column
              prop="name"
              :label="$t('reportCustomGroupManage.table.name')"
              align="center"
            />
            <el-table-column
              prop="url"
              :label="$t('reportCustomGroupManage.table.url')"
              align="center"
            />
            <el-table-column
              prop="remark"
              :label="$t('reportCustomGroupManage.table.remark')"
              align="center"
            />
            <el-table-column
              :label="$t('common.operation')"
              align="center"
              width="200"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.row)"
                >
                  {{ $t('common.edit') }}
                </el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.row)"
                >
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page.sync="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            :total="page.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加弹窗 -->
    <add-report-custom-group
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <!-- 编辑弹窗 -->
    <edit-report-custom-group
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <!-- 删除弹窗 -->
    <delete-report-custom-group
      :visible.sync="deleteVisible"
      :group-id="currentRow.groupId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import {
  listReportCustomGroup
} from '@/api/report/reportCustomGroupManageApi'
import AddReportCustomGroup from '@/components/report/AddReportCustomGroup'
import EditReportCustomGroup from '@/components/report/EditReportCustomGroup'
import DeleteReportCustomGroup from '@/components/report/DeleteReportCustomGroup'

export default {
  name: 'ReportCustomGroupManageList',
  components: {
    AddReportCustomGroup,
    EditReportCustomGroup,
    DeleteReportCustomGroup
  },
  data() {
    return {
      loading: false,
      searchForm: {
        groupId: '',
        name: '',
        url: ''
      },
      tableData: [],
      page: {
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
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listReportCustomGroup(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('reportCustomGroupManage.fetchError'))
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
        groupId: '',
        name: '',
        url: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.deleteVisible = true
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
.report-custom-group-manage-container {
  padding: 15px;

  .el-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>