<template>
  <div class="examine-staff-value-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('examineStaffValue.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.staffName" :placeholder="$t('examineStaffValue.search.staffName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.projectName" :placeholder="$t('examineStaffValue.search.projectName')" clearable
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

    <!-- 考核记录列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('examineStaffValue.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="esYear" :label="$t('examineStaffValue.table.year')" align="center" />
        <el-table-column prop="staffId" :label="$t('examineStaffValue.table.staffId')" align="center" />
        <el-table-column prop="staffName" :label="$t('examineStaffValue.table.staffName')" align="center" />
        <el-table-column prop="projectName" :label="$t('examineStaffValue.table.projectName')" align="center" />
        <el-table-column prop="weight" :label="$t('examineStaffValue.table.weight')" align="center">
          <template slot-scope="scope">
            {{ scope.row.weight }}%
          </template>
        </el-table-column>
        <el-table-column prop="examineValue" :label="$t('examineStaffValue.table.examineValue')" align="center" />
        <el-table-column prop="ownerName" :label="$t('examineStaffValue.table.ownerName')" align="center" />
        <el-table-column prop="roomName" :label="$t('examineStaffValue.table.roomName')" align="center" />
        <el-table-column prop="createTime" :label="$t('examineStaffValue.table.createTime')" align="center" />
      </el-table>

      <!-- 分页 -->
      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { listExamineStaffValue } from '@/api/oa/examineStaffValueApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ExamineStaffValueList',
  data() {
    return {
      loading: false,
      searchForm: {
        staffName: '',
        projectName: '',
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
          staffName: this.searchForm.staffName.trim(),
          projectName: this.searchForm.projectName.trim(),
          communityId: this.searchForm.communityId
        }
        const { data, total } = await listExamineStaffValue(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('examineStaffValue.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.staffName = ''
      this.searchForm.projectName = ''
      this.handleSearch()
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
.examine-staff-value-container {
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