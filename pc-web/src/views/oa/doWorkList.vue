<template>
  <div class="do-work-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="state-list">
            <li v-for="(item, index) in doWorkInfo.states" :key="index" @click="swatchWorkState(item)"
              :class="{ 'active-state': doWorkInfo.conditions.state === item.state }">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('doWork.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="doWorkInfo.conditions.workNameLike"
                :placeholder="$t('doWork.search.workNameLike')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="doWorkInfo.conditions.staffNameLike"
                :placeholder="$t('doWork.search.staffNameLike')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="doWorkInfo.conditions.queryStartTime" type="datetime"
                :placeholder="$t('doWork.search.queryStartTime')" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="doWorkInfo.conditions.queryEndTime" type="datetime"
                :placeholder="$t('doWork.search.queryEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!doWorkInfo.conditions.queryStartTime">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryDoWorkMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetDoWorkMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('doWork.table.title') }}</span>
          </div>
          <el-table :data="doWorkInfo.works" border style="width: 100%" v-loading="loading">
            <el-table-column prop="workId" :label="$t('doWork.table.workId')" align="center">
            </el-table-column>
            <el-table-column prop="workName" :label="$t('doWork.table.workName')" align="center">
            </el-table-column>
            <el-table-column prop="typeName" :label="$t('doWork.table.typeName')" align="center">
            </el-table-column>
            <el-table-column prop="workCycle" :label="$t('doWork.table.workCycle')" align="center">
              <template slot-scope="scope">
                {{ scope.row.workCycle === '1001' ? $t('doWork.workCycle.once') : $t('doWork.workCycle.periodic') }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('doWork.table.startTime')" align="center">
            </el-table-column>
            <el-table-column prop="endTime" :label="$t('doWork.table.endTime')" align="center">
            </el-table-column>
            <el-table-column prop="createUserName" :label="$t('doWork.table.createUserName')" align="center">
            </el-table-column>
            <el-table-column prop="curStaffName" :label="$t('doWork.table.curStaffName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curStaffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="curCopyName" :label="$t('doWork.table.curCopyName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curCopyName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('doWork.table.stateName')" align="center">
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('doWork.table.createTime')" align="center">
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="180">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === 'W' || scope.row.state === 'D'" type="text" size="small"
                  @click="_openTodoTaskDetail(scope.row)">
                  {{ $t('common.process') }}
                </el-button>
                <el-button type="text" size="small" @click="_toWorkDetailPage(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryTaskWork } from '@/api/oa/doWorkApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DoWorkList',
  data() {
    return {
      loading: false,
      communityId: '',
      doWorkInfo: {
        works: [],
        states: [
          {
            name: this.$t('doWork.state.all'),
            state: ''
          },
          {
            name: this.$t('doWork.state.pending'),
            state: 'W'
          },
          {
            name: this.$t('doWork.state.processed'),
            state: 'C'
          }
        ],
        conditions: {
          workNameLike: '',
          staffNameLike: '',
          state: '',
          typeName: '',
          timeout: '',
          queryEndTime: '',
          queryStartTime: '',
          page: 1,
          row: 10
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listDoWorks()
  },
  methods: {
    async _listDoWorks() {
      try {
        this.loading = true
        const params = {
          ...this.doWorkInfo.conditions,
          communityId: this.communityId
        }
        const { data, total } = await queryTaskWork(params)
        this.doWorkInfo.works = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('doWork.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryDoWorkMethod() {
      this.pagination.current = 1
      this.doWorkInfo.conditions.page = 1
      this._listDoWorks()
    },
    _resetDoWorkMethod() {
      this.doWorkInfo.conditions = {
        workNameLike: '',
        staffNameLike: '',
        state: '',
        typeName: '',
        timeout: '',
        queryEndTime: '',
        queryStartTime: '',
        page: 1,
        row: 10
      }
      this._listDoWorks()
    },
    swatchWorkState(state) {
      this.doWorkInfo.conditions.state = state.state
      this._listDoWorks()
    },
    _openTodoTaskDetail(work) {
      this.$router.push({
        path: '/pages/oa/workTaskDetail',
        query: {
          workId: work.workId,
          taskId: work.taskId,
          todo: 'ON'
        }
      })
    },
    _toWorkDetailPage(work) {
      this.$router.push({
        path: '/views/oa/workDetail',
        query: {
          workId: work.workId
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.doWorkInfo.conditions.row = val
      this._listDoWorks()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.doWorkInfo.conditions.page = val
      this._listDoWorks()
    }
  }
}
</script>

<style lang="scss" scoped>
.do-work-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .state-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active-state {
          background-color: #409EFF;
          color: white;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .el-date-editor {
    width: 100%;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>