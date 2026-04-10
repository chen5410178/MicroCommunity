<template>
  <div class="examine-project-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('examineProjectManage.search.title') }}</span>
      </div>
      <el-row :gutter="20" >
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('examineProjectManage.search.namePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.post" :placeholder="$t('examineProjectManage.search.postPlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('examineProjectManage.search.statePlaceholder')"
            style="width:100%">
            <el-option :label="$t('examineProjectManage.status.enable')" value="Y" />
            <el-option :label="$t('examineProjectManage.status.disable')" value="N" />
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

    <!-- 考核项目列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('examineProjectManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="name" :label="$t('examineProjectManage.table.name')" align="center" />
        <el-table-column prop="post" :label="$t('examineProjectManage.table.post')" align="center" />
        <el-table-column prop="weight" :label="$t('examineProjectManage.table.weight')" align="center">
          <template slot-scope="scope">
            {{ scope.row.weight }}%
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('examineProjectManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'Y' ? $t('examineProjectManage.status.enable') :
              $t('examineProjectManage.status.disable') }}
          </template>
        </el-table-column>
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

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-examine-project ref="addExamineProject" @success="handleSuccess" />
    <edit-examine-project ref="editExamineProject" @success="handleSuccess" />
    <delete-examine-project ref="deleteExamineProject" @success="handleSuccess" />
  </div>
</template>

<script>
import { listExamineProject } from '@/api/oa/examineProjectManageApi'
import AddExamineProject from '@/components/oa/addExamineProject'
import EditExamineProject from '@/components/oa/editExamineProject'
import DeleteExamineProject from '@/components/oa/deleteExamineProject'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ExamineProjectManageList',
  components: {
    AddExamineProject,
    EditExamineProject,
    DeleteExamineProject
  },
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        post: '',
        state: '',
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
        const { data, total } = await listExamineProject(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('examineProjectManage.fetchError'))
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
        name: '',
        post: '',
        state: '',
        communityId: getCommunityId()
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addExamineProject.open()
    },
    handleEdit(row) {
      this.$refs.editExamineProject.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteExamineProject.open(row)
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
.examine-project-manage-container {
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