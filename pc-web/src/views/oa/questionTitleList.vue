<template>
  <div class="question-title-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('questionTitle.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.itemTitle" :placeholder="$t('questionTitle.search.itemTitlePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.titleType" :placeholder="$t('questionTitle.search.titleTypePlaceholder')"
            style="width:100%">
            <el-option :label="$t('questionTitle.search.pleaseSelect')" value="" />
            <el-option :label="$t('questionTitle.search.singleChoice')" value="1001" />
            <el-option :label="$t('questionTitle.search.multipleChoice')" value="2002" />
            <el-option :label="$t('questionTitle.search.shortAnswer')" value="3003" />
          </el-select>
        </el-col>
        <el-col :span="12">
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

    <!-- 问卷题目列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('questionTitle.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="qaTitle" :label="$t('questionTitle.table.name')" align="center" />
        <el-table-column prop="titleType" :label="$t('questionTitle.table.type')" align="center">
          <template slot-scope="scope">
            {{ getTitleTypeName(scope.row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('questionTitle.table.options')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.titleValues" :key="index">
              {{ item.seq }}、{{ item.qaValue }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('questionTitle.table.createTime')" align="center" />
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

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 添加题目对话框 -->
    <add-question-title ref="addDialog" @success="handleSuccess" />

    <!-- 编辑题目对话框 -->
    <edit-question-title ref="editDialog" @success="handleSuccess" />

    <!-- 删除题目对话框 -->
    <delete-question-title ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listQuestionTitle } from '@/api/oa/questionTitleApi'
import AddQuestionTitle from '@/components/oa/addQuestionTitle'
import EditQuestionTitle from '@/components/oa/editQuestionTitle'
import DeleteQuestionTitle from '@/components/oa/deleteQuestionTitle'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'QuestionTitleList',
  components: {
    AddQuestionTitle,
    EditQuestionTitle,
    DeleteQuestionTitle
  },
  data() {
    return {
      loading: false,
      searchForm: {
        itemTitle: '',
        titleType: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
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
        const { data, total } = await listQuestionTitle(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('questionTitle.fetchError'))
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
        itemTitle: '',
        titleType: '',
        communityId: getCommunityId()
      }
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
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
    },
    getTitleTypeName(type) {
      switch (type) {
        case '1001':
          return this.$t('questionTitle.types.singleChoice')
        case '2002':
          return this.$t('questionTitle.types.multipleChoice')
        case '3003':
          return this.$t('questionTitle.types.shortAnswer')
        default:
          return ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.question-title-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 10px;
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