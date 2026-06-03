<template>
  <div class="notice-manage-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-wrapper">
            <div class="type-list-item" :class="{ 'selected': selectedNoticeType === '' }"
              @click="switchNoticeType('')">
              {{ $t('noticeManage.allTypes') }}
            </div>
            <div v-for="(item, index) in noticeTypeCds" :key="index"
              class="type-list-item"
              :class="{ 'selected': selectedNoticeType === item.noticeTypeCd }"
              @click="switchNoticeType(item.noticeTypeCd)">
              {{ item.noticeTypeName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('noticeManage.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.title" :placeholder="$t('noticeManage.search.titlePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.startTime" type="datetime"
                :placeholder="$t('noticeManage.search.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.endTime" type="datetime"
                :placeholder="$t('noticeManage.search.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
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

        <el-card class="table-card">
          <el-tabs v-model="activeStateTab" @tab-click="handleStateTabClick">
            <el-tab-pane :label="$t('noticeManage.allStates')" name="ALL" />
            <el-tab-pane v-for="(item, index) in states" :key="index" :label="item.name" :name="item.statusCd" />
          </el-tabs>

          <div style="text-align:right;margin-bottom:10px">
            <el-button type="primary" size="small" @click="handleAdd">
              <i class="el-icon-plus"></i>
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
            @current-change="handleCurrentChange" class="margin-top" />
        </el-card>
      </el-col>
    </el-row>

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
      selectedNoticeType: '',
      activeStateTab: 'ALL',
      noticeTypeCds: [],
      states: [],
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
    this.initNoticeTypes()
    this.getList()
    this.loadStates()
  },
  methods: {
    initNoticeTypes() {
      this.noticeTypeCds = [
        { noticeTypeCd: '1000', noticeTypeName: this.$t('noticeManage.search.ownerNotice') },
        { noticeTypeCd: '1001', noticeTypeName: this.$t('noticeManage.search.staffNotice') }
      ]
    },
    async loadStates() {
      this.states = [
        { statusCd: '1000', name: this.$t('noticeManage.search.pendingNotice') },
        { statusCd: '2000', name: this.$t('noticeManage.search.notifying') },
        { statusCd: '3000', name: this.$t('noticeManage.search.noticeComplete') }
      ]
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm,
          communityId: this.communityId
        }
        if (params.noticeTypeCd === undefined) {
          params.noticeTypeCd = ''
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
    switchNoticeType(noticeTypeCd) {
      this.selectedNoticeType = noticeTypeCd
      this.searchForm.noticeTypeCd = noticeTypeCd
      this.pagination.current = 1
      this.getList()
    },
    handleStateTabClick() {
      this.searchForm.state = this.activeStateTab === 'ALL' ? '' : this.activeStateTab
      this.pagination.current = 1
      this.getList()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        title: '',
        noticeTypeCd: this.selectedNoticeType,
        state: this.activeStateTab === 'ALL' ? '' : this.activeStateTab,
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

  .left-panel {
    .type-card {
      .type-list-wrapper {
        max-height: 600px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .type-list-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .margin-top {
      margin-top: 20px;
    }
  }
}
</style>