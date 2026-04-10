<template>
  <div class="fee-print-page-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feePrintPageManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.pageId" :placeholder="$t('feePrintPageManage.search.pageId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.pageName" :placeholder="$t('feePrintPageManage.search.pageName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('feePrintPageManage.search.state')" style="width:100%">
            <el-option :label="$t('feePrintPageManage.search.all')" value="" />
            <el-option :label="$t('feePrintPageManage.search.enabled')" value="T" />
            <el-option :label="$t('feePrintPageManage.search.disabled')" value="F" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 收据模板列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feePrintPageManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="pageId" :label="$t('feePrintPageManage.table.pageId')" align="center" />
        <el-table-column prop="pageName" :label="$t('feePrintPageManage.table.pageName')" align="center" />
        <el-table-column prop="communityId" :label="$t('feePrintPageManage.table.communityId')" align="center" />
        <el-table-column prop="templateName" :label="$t('feePrintPageManage.table.templateName')" align="center" />
        <el-table-column :label="$t('feePrintPageManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'T' ? $t('feePrintPageManage.table.enabled') :
              $t('feePrintPageManage.table.disabled') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button v-if="scope.row.state === 'F'" size="mini" type="success" @click="handleEnable(scope.row)">
              {{ $t('common.enabled') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 添加弹窗 -->
    <add-fee-print-page ref="addDialog" @success="handleSuccess" />

    <!-- 编辑弹窗 -->
    <edit-fee-print-page ref="editDialog" @success="handleSuccess" />

    <!-- 删除弹窗 -->
    <delete-fee-print-page ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listFeePrintPage, updateFeePrintPageState } from '@/api/system/feePrintPageManageApi'
import AddFeePrintPage from '@/components/system/addFeePrintPage'
import EditFeePrintPage from '@/components/system/editFeePrintPage'
import DeleteFeePrintPage from '@/components/system/deleteFeePrintPage'

export default {
  name: 'FeePrintPageManageList',
  components: {
    AddFeePrintPage,
    EditFeePrintPage,
    DeleteFeePrintPage
  },
  data() {
    return {
      loading: false,
      communityId: '',
      searchForm: {
        pageId: '',
        pageName: '',
        state: ''
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
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId,
          ...this.searchForm
        }
        const { data, total } = await listFeePrintPage(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('feePrintPageManage.fetchError'))
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
        pageId: '',
        pageName: '',
        state: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleEnable(row) {
      // TODO: 启用逻辑
      console.log(row)
      updateFeePrintPageState({
        pageId: row.pageId,
        state: 'T'
      }).then(() => {
        this.$message.success(this.$t('common.operationSuccess'))
        this.getList()
      })
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
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
.fee-print-page-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>