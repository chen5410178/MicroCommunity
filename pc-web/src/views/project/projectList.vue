<!-- 引入项目管理相关的组件 -->
<template>
  <div class="project-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('project.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.projectName"
          :placeholder="$t('project.search.placeholder')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <!-- 项目列表 -->
    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('project.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="projectId"
          :label="$t('project.table.projectId')"
          width="200"
        />
        <el-table-column
          prop="projectName"
          :label="$t('project.table.projectName')"
          width="250"
        />
        <el-table-column
          prop="personName"
          :label="$t('project.table.personName')"
          width="200"
        />
        <el-table-column
          prop="personLink"
          :label="$t('project.table.personLink')"
          width="120"
        />
        <el-table-column
          prop="remark"
          :label="$t('project.table.remark')"
          min-width="200"
        />
        <el-table-column
          prop="createTime"
          :label="$t('project.table.createTime')"
          min-width="200"
        />
        <el-table-column
          :label="$t('common.operation')"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >{{ $t('common.edit') }}</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >{{ $t('common.delete') }}</el-button>
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
    </div>

    <!-- 添加项目对话框 -->
    <add-project
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <!-- 编辑项目对话框 -->
    <edit-project
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <!-- 删除项目对话框 -->
    <del-project
      :visible.sync="delVisible"
      :project-id="currentRow.projectId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getProjectList } from '@/api/project/projectApi'
import AddProject from '@/components/project/AddProject'
import EditProject from '@/components/project/EditProject'
import DelProject from '@/components/project/DelProject'

export default {
  name: 'ProjectList',
  components: {
    AddProject,
    EditProject,
    DelProject
  },
  data() {
    return {
      loading: false,
      searchForm: {
        projectName: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      delVisible: false,
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
          projectName: this.searchForm.projectName
        }
        const  data  = await getProjectList(params)
        this.tableData = data.data
        this.page.total = data.total
      } catch (error) {
        this.$message.error(this.$t('project.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
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
      this.delVisible = true
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
.project-container {
  padding: 0;
  margin: 0;

  .search-wrapper {
    background-color: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;

    .search-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 20px;
      text-align: left;
      color: #333;
    }

    .search-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .search-item {
        width: 200px;
      }
    }
  }

  .list-wrapper {
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;

    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .list-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style> 