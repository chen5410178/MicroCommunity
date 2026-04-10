<template>
  <div class="start-work-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <div class="state-sidebar">
          <ul class="state-list">
            <li v-for="(item, index) in states" :key="index" class="state-item"
              :class="{ 'active-state': conditions.state === item.state }" @click="switchWorkState(item)">
              {{ $t(`startWork.state.${item.state || 'all'}`) }}
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="21">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('startWork.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="conditions" class="search-form text-left">
            <el-form-item>
              <el-input v-model="conditions.workName" :placeholder="$t('startWork.search.workName')" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="conditions.staffName" :placeholder="$t('startWork.search.staffName')" clearable />
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="conditions.queryStartTime" type="datetime"
                :placeholder="$t('startWork.search.startTime')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="conditions.queryEndTime" type="datetime"
                :placeholder="$t('startWork.search.endTime')" value-format="yyyy-MM-dd HH:mm:ss"
                :picker-options="endDateOptions" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="work-list-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('startWork.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right" @click="handleAdd">
              <i class="el-icon-plus" />
              {{ $t('startWork.list.draft') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="works" border style="width: 100%">
            <el-table-column prop="workId" :label="$t('startWork.table.workId')" align="center" />
            <el-table-column prop="workName" :label="$t('startWork.table.workName')" align="center" />
            <el-table-column prop="typeName" :label="$t('startWork.table.typeName')" align="center" />
            <el-table-column :label="$t('startWork.table.workCycle')" align="center">
              <template slot-scope="scope">
                {{ scope.row.workCycle === '1001' ? $t('startWork.cycle.once') : $t('startWork.cycle.periodic') }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('startWork.table.startTime')" align="center" />
            <el-table-column prop="endTime" :label="$t('startWork.table.endTime')" align="center" />
            <el-table-column prop="createUserName" :label="$t('startWork.table.createUser')" align="center" />
            <el-table-column prop="curStaffName" :label="$t('startWork.table.handler')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curStaffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="curCopyName" :label="$t('startWork.table.ccUser')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curCopyName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('startWork.table.state')" align="center" />
            <el-table-column prop="createTime" :label="$t('startWork.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="300">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
                <el-button v-if="scope.row.state === 'W'" size="mini" type="primary" @click="handleEdit(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
                <el-button v-if="scope.row.state !== 'S'" size="mini" type="warning"
                  @click="handleUpdateState(scope.row, 'S')">
                  {{ $t('startWork.operation.stop') }}
                </el-button>
                <el-button v-if="scope.row.state === 'S'" size="mini" type="success"
                  @click="handleUpdateState(scope.row, 'W')">
                  {{ $t('startWork.operation.start') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>

    <delete-work ref="deleteWork" @success="fetchData" />
  </div>
</template>

<script>
import { queryStartWork, updateWorkState } from '@/api/oa/startWorkApi'
import DeleteWork from '@/components/oa/deleteWork'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'StartWorkList',
  components: {
    DeleteWork
  },
  data() {
    return {
      loading: false,
      communityId: '',
      states: [
        { name: '全部', state: '' },
        { name: '待处理', state: 'W' },
        { name: '处理中', state: 'D' },
        { name: '处理完成', state: 'C' }
      ],
      works: [],
      conditions: {
        state: '',
        workName: '',
        staffName: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      endDateOptions: {
        disabledDate: (time) => {
          if (this.conditions.queryStartTime) {
            return time.getTime() <= new Date(this.conditions.queryStartTime).getTime()
          }
          return false
        }
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const { data, total } = await queryStartWork(params)
        this.works = data
        this.page.total = total
      } catch (error) {
        console.error('获取工单列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    switchWorkState(item) {
      this.conditions.state = item.state
      this.page.current = 1
      this.fetchData()
    },
    handleSearch() {
      this.page.current = 1
      this.fetchData()
    },
    handleReset() {
      this.conditions = {
        state: '',
        workName: '',
        staffName: '',
        queryStartTime: '',
        queryEndTime: ''
      }
      this.page.current = 1
      this.fetchData()
    },
    handleAdd() {
      this.$router.push('/views/oa/addWork')
    },
    handleEdit(row) {
      this.$router.push(`/views/oa/editWork?workId=${row.workId}`)
    },
    handleDetail(row) {
      this.$router.push(`/views/oa/workDetail?workId=${row.workId}`)
    },
    handleDelete(row) {
      this.$refs.deleteWork.open(row)
    },
    async handleUpdateState(row, state) {
      try {
        await updateWorkState({
          workId: row.workId,
          state,
          communityId: this.communityId
        })
        this.$message.success(state === 'S' ? '停止成功' : '启用成功')
        this.fetchData()
      } catch (error) {
        console.error('更新状态失败:', error)
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.fetchData()
    },
    handlePageChange(current) {
      this.page.current = current
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.start-work-container {
  padding: 20px;

  .state-sidebar {
    background-color: #fff;
    border-radius: 4px;
    padding: 10px 0;
    height: 100%;

    .state-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .state-item {
        padding: 10px 15px;
        cursor: pointer;
        text-align: center;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active-state {
          background-color: #409eff;
          color: #fff;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .work-list-card {
    .el-table {
      margin-top: 10px;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>