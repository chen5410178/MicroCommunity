<template>
  <div class="work-deduction-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('workDeduction.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model.trim="searchForm.staffNameLike"
            :placeholder="$t('workDeduction.search.staffNameLike')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model.trim="searchForm.deductionPersonNameLike"
            :placeholder="$t('workDeduction.search.deductionPersonNameLike')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工单扣款列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('workDeduction.list.title') }}</span>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="staffName"
          :label="$t('workDeduction.table.staffName')"
          align="center"
        />
        <el-table-column
          :label="$t('workDeduction.table.timeRange')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column
          prop="content"
          :label="$t('workDeduction.table.content')"
          align="center"
          width="400"
        />
        <el-table-column
          prop="finishTime"
          :label="$t('workDeduction.table.finishTime')"
          align="center"
        />
        <el-table-column
          :label="$t('workDeduction.table.state')"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.state === 'CC'">
              {{ $t('workDeduction.state.CC') }}
            </span>
            <span v-else-if="scope.row.state === 'C'">
              {{ $t('workDeduction.state.C') }}
            </span>
            <span v-else>
              {{ $t('workDeduction.state.W') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          :label="$t('workDeduction.table.remark')"
          align="center"
        />
        <el-table-column
          prop="score"
          :label="$t('workDeduction.table.score')"
          align="center"
        />
        <el-table-column
          prop="deductionMoney"
          :label="$t('workDeduction.table.deductionMoney')"
          align="center"
        />
        <el-table-column
          prop="deductionReason"
          :label="$t('workDeduction.table.deductionReason')"
          align="center"
        />
        <el-table-column
          prop="deductionPersonName"
          :label="$t('workDeduction.table.deductionPersonName')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('workDeduction.table.createTime')"
          align="center"
        />
        <el-table-column
          :label="$t('common.operation')"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleDetail(scope.row)"
            >
              {{ $t('common.detail') }}
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
  </div>
</template>

<script>
import { listWorkDeduction } from '@/api/oa/workDeductionApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WorkDeductionList',
  data() {
    return {
      loading: false,
      searchForm: {
        staffNameLike: '',
        deductionPersonNameLike: '',
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
        const { data, total } = await listWorkDeduction(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('workDeduction.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleDetail(row) {
      this.$router.push({
        path: '/views/oa/workDetail',
        query: { workId: row.workId }
      })
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
.work-deduction-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
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