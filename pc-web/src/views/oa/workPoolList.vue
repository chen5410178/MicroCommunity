<template>
  <div class="work-pool-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="state-sidebar">
          <ul class="state-list">
            <li
              v-for="(item, index) in workPoolInfo.states"
              :key="index"
              class="state-item"
              :class="{ 'active-state': workPoolInfo.conditions.state === item.state }"
              @click="swatchWorkState(item)"
            >
              {{ item.name }}
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workPool.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input
                v-model.trim="workPoolInfo.conditions.workNameLike"
                :placeholder="$t('workPool.search.workName')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-input
                v-model.trim="workPoolInfo.conditions.createUserNameLike"
                :placeholder="$t('workPool.search.createUser')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-input
                v-model.trim="workPoolInfo.conditions.staffNameLike"
                :placeholder="$t('workPool.search.staffName')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-date-picker
                v-model="workPoolInfo.conditions.queryStartTime"
                type="datetime"
                :placeholder="$t('workPool.search.startTime')"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="4">
              <el-date-picker
                v-model="workPoolInfo.conditions.queryEndTime"
                type="datetime"
                :placeholder="$t('workPool.search.endTime')"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryWorkPoolMethod">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetWorkPoolMethod">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="work-list-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workPool.list.title') }}</span>
          </div>
          <el-table
            :data="workPoolInfo.works"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="workId"
              :label="$t('workPool.table.id')"
              width="80"
              align="center"
            />
            <el-table-column
              prop="workName"
              :label="$t('workPool.table.workName')"
              align="center"
            />
            <el-table-column
              prop="typeName"
              :label="$t('workPool.table.typeName')"
              align="center"
            />
            <el-table-column
              prop="workCycle"
              :label="$t('workPool.table.workCycle')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.workCycle === '1001' ? $t('workPool.table.once') : $t('workPool.table.cycle') }}
              </template>
            </el-table-column>
            <el-table-column
              prop="createUserName"
              :label="$t('workPool.table.createUser')"
              align="center"
            />
            <el-table-column
              prop="staffName"
              :label="$t('workPool.table.staffName')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.staffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('workPool.table.timeRange')"
              align="center"
            >
              <template slot-scope="scope">
                <div>{{ scope.row.startTime }}</div>
                <div>{{ scope.row.endTime }}</div>
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('workPool.table.status')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.stateName }}
                <span v-if="scope.row.state === 'C' && scope.row.taskTimeout === 'Y'">
                  ({{ $t('workPool.table.timeout') }})
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              :label="$t('workPool.table.createTime')"
              align="center"
            />
            <el-table-column
              prop="finishTime"
              :label="$t('workPool.table.finishTime')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.finishTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('common.operation')"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="primary"
                  @click="_toWorkDetailPage(scope.row)"
                >
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            :current-page.sync="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listWorkTask } from '@/api/oa/workPoolApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WorkPoolList',
  data() {
    return {
      loading: false,
      workPoolInfo: {
        works: [],
        states: [
          { name: this.$t('workPool.state.all'), state: '' },
          { name: this.$t('workPool.state.pending'), state: 'W' },
          { name: this.$t('workPool.state.completed'), state: 'C' },
          { name: this.$t('workPool.state.timeout'), state: 'timeout' }
        ],
        conditions: {
          state: '',
          workNameLike: '',
          createUserNameLike: '',
          staffNameLike: '',
          queryStartTime: '',
          queryEndTime: '',
          page: 1,
          row: 10,
          communityId: ''
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
    this.workPoolInfo.conditions.communityId = getCommunityId()
    this._listWorkPools()
  },
  methods: {
    async _listWorkPools() {
      try {
        this.loading = true
        const params = { ...this.workPoolInfo.conditions }
        if (params.state === 'timeout') {
          params.state = 'C'
          params.taskTimeout = 'Y'
        }
        
        const { data, total } = await listWorkTask(params)
        this.workPoolInfo.works = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('workPool.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryWorkPoolMethod() {
      this.pagination.current = 1
      this.workPoolInfo.conditions.page = 1
      this._listWorkPools()
    },
    _resetWorkPoolMethod() {
      this.workPoolInfo.conditions = {
        ...this.workPoolInfo.conditions,
        workNameLike: '',
        createUserNameLike: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: '',
        state: ''
      }
      this._queryWorkPoolMethod()
    },
    swatchWorkState(state) {
      this.workPoolInfo.conditions.state = state.state
      this._queryWorkPoolMethod()
    },
    _toWorkDetailPage(work) {
      this.$router.push(`/views/oa/workDetail?workId=${work.workId}`)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.workPoolInfo.conditions.row = val
      this._listWorkPools()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.workPoolInfo.conditions.page = val
      this._listWorkPools()
    }
  }
}
</script>

<style lang="scss" scoped>
.work-pool-container {
  padding: 20px;

  .state-sidebar {
    background: #fff;
    border-radius: 4px;
    padding: 10px 0;

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
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>