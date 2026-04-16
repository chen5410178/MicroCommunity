<template>
  <div class="notice-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('noticeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-select v-model="searchForm.noticeTypeCd" :placeholder="$t('noticeManage.search.noticeTypePlaceholder')"
            style="width:100%">
            <el-option :label="$t('noticeManage.search.noticeTypePlaceholder')" value="" />
            <el-option :label="$t('noticeManage.search.ownerNotice')" value="1000" />
            <el-option :label="$t('noticeManage.search.staffNotice')" value="1001" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.state" :placeholder="$t('noticeManage.search.statePlaceholder')"
            style="width:100%">
            <el-option :label="$t('noticeManage.search.statePlaceholder')" value="" />
            <el-option :label="$t('noticeManage.search.pendingNotice')" value="1000" />
            <el-option :label="$t('noticeManage.search.notifying')" value="2000" />
            <el-option :label="$t('noticeManage.search.noticeComplete')" value="3000" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.title" :placeholder="$t('noticeManage.search.titlePlaceholder')" />
        </el-col>
        <el-col :span="4">
          <el-date-picker v-model="searchForm.startTime" type="datetime"
            :placeholder="$t('noticeManage.search.startTimePlaceholder')" style="width:100%" />
        </el-col>
        <el-col :span="4">
          <el-date-picker v-model="searchForm.endTime" type="datetime"
            :placeholder="$t('noticeManage.search.endTimePlaceholder')" style="width:100%" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 公告列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('noticeManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="noticeId" :label="$t('noticeManage.table.noticeId')" align="center" />
        <el-table-column prop="title" :label="$t('noticeManage.table.title')" align="center" />
        <el-table-column prop="noticeTypeCdName" :label="$t('noticeManage.table.noticeType')" align="center" />
        <el-table-column prop="startTime" :label="$t('noticeManage.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('noticeManage.table.endTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('noticeManage.table.state')" align="center" />
        <el-table-column prop="objName" :label="$t('noticeManage.table.noticeRange')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button v-if="scope.row.state !== '2000'" size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 删除确认对话框 -->
    <delete-notice ref="deleteNotice" @success="handleSuccess" />
  </div>
</template>

<script>
import { listNotices } from '@/api/oa/noticeManageApi'
import DeleteNotice from '@/components/oa/deleteNotice'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NoticeManageList',
  components: {
    DeleteNotice
  },
  data() {
    return {
      loading: false,
      searchForm: {
        title: '',
        noticeTypeCd: '',
        state: '',
        startTime: '',
        endTime: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
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
          ...this.searchForm,
          communityId: this.communityId
        }
        const { notices, total } = await listNotices(params)
        this.tableData = notices
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('noticeManage.fetchError'))
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
        title: '',
        noticeTypeCd: '',
        state: '',
        startTime: '',
        endTime: ''
      }
      this.getList()
    },
    handleAdd() {
      this.$router.push('/views/oa/addNoticeView')
    },
    handleEdit(row) {
      this.$router.push(`/views/oa/editNoticeView?noticeId=${row.noticeId}`)
    },
    handleDetail(row) {
      this.$router.push(`/views/oa/noticeDetail?noticeId=${row.noticeId}`)
    },
    handleDelete(row) {
      this.$refs.deleteNotice.open(row)
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
.notice-manage-container {
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