<template>
  <div class="owner-voting-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('ownerVoting.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.qaId" :placeholder="$t('ownerVoting.search.qaId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.qaName" :placeholder="$t('ownerVoting.search.qaName')" clearable
            @keyup.enter.native="handleSearch" />
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

    <!-- 投票信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('ownerVoting.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="qaName" :label="$t('ownerVoting.table.qaName')" align="center" />
        <el-table-column :label="$t('ownerVoting.table.validity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="titleType" :label="$t('ownerVoting.table.type')" align="center">
          <template slot-scope="scope">
            {{ scope.row.titleType === '1001' ? $t('ownerVoting.type.single') : $t('ownerVoting.type.multiple') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('ownerVoting.table.options')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.titleValues" :key="index">
              {{ item.seq }}、{{ item.qaValue }}({{ item.personCount }}{{ $t('ownerVoting.table.peopleChose') }})
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('ownerVoting.table.voteCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.voteCount }}/{{ scope.row.votedCount }}
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('ownerVoting.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'C' ? $t('ownerVoting.state.published') : $t('ownerVoting.state.unpublished') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerVoting.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
              {{ $t('ownerVoting.operation.result') }}
            </el-button>
            <el-button v-if="scope.row.state === 'W'" size="mini" type="warning" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button v-if="scope.row.state === 'W'" size="mini" type="success" @click="handlePublish(scope.row)">
              {{ $t('ownerVoting.operation.publish') }}
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
    <delete-question-answer ref="deleteQuestionAnswer" @success="handleSuccess" />
    <publish-question-answer ref="publishQuestionAnswer" @success="handleSuccess" />
  </div>
</template>

<script>
import { listOwnerVote } from '@/api/oa/ownerVotingApi'
import DeleteQuestionAnswer from '@/components/oa/deleteQuestionAnswer'
import PublishQuestionAnswer from '@/components/oa/publishQuestionAnswer'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerVotingList',
  components: {
    DeleteQuestionAnswer,
    PublishQuestionAnswer
  },
  data() {
    return {
      loading: false,
      searchForm: {
        qaId: '',
        qaName: '',
        qaType: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
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
        const { data, total } = await listOwnerVote(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('ownerVoting.fetchError'))
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
        qaId: '',
        qaName: '',
        qaType: '',
        communityId: getCommunityId()
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$router.push('/views/oa/addOwnerVoting')
    },
    handleEdit(row) {
      this.$router.push(`/views/oa/editOwnerVoting?qaId=${row.qaId}`)
    },
    handleDelete(row) {
      this.$refs.deleteQuestionAnswer.open(row)
    },
    handlePublish(row) {
      this.$refs.publishQuestionAnswer.open(row)
    },
    handleDetail(row) {
      window.open(`/#/views/oa/printOwnerVoting?qaId=${row.qaId}`)
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
.owner-voting-container {
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