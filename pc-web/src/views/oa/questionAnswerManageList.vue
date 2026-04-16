<template>
  <div class="question-answer-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('questionAnswerManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="searchForm.qaName" :placeholder="$t('questionAnswerManage.search.qaName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="8">
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

    <!-- 问卷列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('questionAnswerManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="qaName" :label="$t('questionAnswerManage.table.qaName')" align="center" />
        <el-table-column :label="$t('questionAnswerManage.table.validity')" align="center">
          <template slot-scope="scope">
            <div>{{ scope.row.startTime }}</div>
            <div>{{ scope.row.endTime }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('questionAnswerManage.table.voteCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.voteCount }}/{{ scope.row.votedCount }}
          </template>
        </el-table-column>
        <el-table-column prop="score" :label="$t('questionAnswerManage.table.score')" align="center" />
        <el-table-column :label="$t('questionAnswerManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'C' ? $t('questionAnswerManage.state.published') :
              $t('questionAnswerManage.state.unpublished') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('questionAnswerManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleResult(scope.row)">
              {{ $t('questionAnswerManage.button.result') }}
            </el-button>
            <el-button size="mini" @click="handleDetail(scope.row)">
              {{ $t('questionAnswerManage.button.detail') }}
            </el-button>
            <el-button v-if="scope.row.state === 'W'" size="mini" type="success" @click="handlePublish(scope.row)">
              {{ $t('questionAnswerManage.button.publish') }}
            </el-button>
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
    <delete-question-answer ref="deleteDialog" @success="fetchData" />
    <publish-question-answer ref="publishDialog" @success="fetchData" />
  </div>
</template>

<script>
import { listQuestionAnswer } from '@/api/oa/questionAnswerManageApi'
import DeleteQuestionAnswer from '@/components/oa/deleteQuestionAnswer'
import PublishQuestionAnswer from '@/components/oa/publishQuestionAnswer'

export default {
  name: 'QuestionAnswerManageList',
  components: {
    DeleteQuestionAnswer,
    PublishQuestionAnswer
  },
  data() {
    return {
      loading: false,
      searchForm: {
        qaName: '',
        qaType: ''
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
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          qaName: this.searchForm.qaName,
          qaType: this.searchForm.qaType
        }
        const { data, total } = await listQuestionAnswer(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('questionAnswerManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        qaName: '',
        qaType: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$router.push('/views/oa/addQuestionAnswer')
    },
    handleEdit(row) {
      this.$router.push(`/views/oa/editQuestionAnswer?qaId=${row.qaId}`)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handlePublish(row) {
      this.$refs.publishDialog.open(row)
    },
    handleResult(row) {
      window.open(`/#/pages/question/printQuestionAnswer?qaId=${row.qaId}`)
    },
    handleDetail(row) {
      window.open(`/#/pages/question/printQuestionAnswerDetail?qaId=${row.qaId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.question-answer-manage-container {
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