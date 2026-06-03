<template>
  <div class="maintainance-task-details-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="plan-card">
          <div class="plan-list-wrapper">
            <div class="plan-list-item"
              :class="{ 'selected': searchForm.maintainancePlanId == '' }"
              @click="_switchPlan({ planId: '', planName: '' })">
              {{ $t('common.all') }}
            </div>
            <div v-for="(item, index) in planList" :key="index"
              class="plan-list-item"
              :class="{ 'selected': searchForm.maintainancePlanId == item.planId }"
              @click="_switchPlan(item)">
              {{ item.planName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="search-wrapper">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskDetails.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.planUserName"
                :placeholder="$t('maintainanceTaskDetails.search.planUserName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.maintainanceStartTime" type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('maintainanceTaskDetails.search.maintainanceStartTime')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.maintainanceEndTime" type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('maintainanceTaskDetails.search.maintainanceEndTime')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.taskState" :placeholder="$t('maintainanceTaskDetails.search.taskState')"
                style="width:100%">
                <el-option :label="$t('maintainanceTaskDetails.search.selectState')" value="" />
                <el-option v-for="(item, index) in taskStateList" :key="index" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4" >
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

        <el-card class="list-wrapper">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskDetails.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width:100%">
            <el-table-column prop="taskDetailId" :label="$t('maintainanceTaskDetails.table.taskDetailId')"
              align="center" />
            <el-table-column prop="machineName" :label="$t('maintainanceTaskDetails.table.machineName')"
              align="center" />
            <el-table-column prop="planName" :label="$t('maintainanceTaskDetails.table.planName')" align="center" />
            <el-table-column prop="standardName" :label="$t('maintainanceTaskDetails.table.standardName')"
              align="center" />
            <el-table-column prop="planUserName" :label="$t('maintainanceTaskDetails.table.planUserName')"
              align="center" />
            <el-table-column :label="$t('maintainanceTaskDetails.table.planTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.planInsTime }}<br />{{ scope.row.planEndTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('maintainanceTaskDetails.table.inspectionTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.inspectionTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('maintainanceTaskDetails.table.actUserName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.actUserName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('maintainanceTaskDetails.table.stateName')" align="center" />
            <el-table-column :label="$t('maintainanceTaskDetails.table.description')" align="center">
              <template slot-scope="scope">
                <span class="text-primary">
                  {{ scope.row.description || '-' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('maintainanceTaskDetails.table.photos')" align="center">
              <template slot-scope="scope">
                <span v-for="photo in scope.row.photos" :key="photo.url">
                  <el-image style="width:60px;height:60px;margin-right:5px" :src="photo.url"
                    :preview-src-list="[photo.url]" fit="cover" />
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('maintainanceTaskDetails.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDetail(scope.row.taskDetailId)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <view-map ref="viewMap" />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { getMaintainanceTaskDetailList, getMaintainancePlanList } from '@/api/inspection/maintainanceTaskDetailsApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewMap from '@/components/system/ViewMap'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'MaintainanceTaskDetailsList',
  components: {
    ViewMap,
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        planUserName: '',
        taskDetailId: '',
        maintainanceStartTime: '',
        maintainanceEndTime: '',
        maintainancePlanId: '',
        taskState: ''
      },
      tableData: [],
      planList: [],
      taskStateList: [],
      page: {
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
    this.getPlanList()
    this.getTaskStateList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const { data, total } = await getMaintainanceTaskDetailList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('maintainanceTaskDetails.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getPlanList() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 200
        }
        const { data } = await getMaintainancePlanList(params)
        this.planList = data
      } catch (error) {
        console.error('获取保养计划列表失败:', error)
      }
    },
    async getTaskStateList() {
      try {
        const data = await getDict('maintainance_task_detail', 'state')
        this.taskStateList = data
      } catch (error) {
        console.error('获取任务状态字典失败:', error)
      }
    },
    _switchPlan(item) {
      this.searchForm.maintainancePlanId = item.planId
      this.page.current = 1
      this.getList()
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.planUserName = ''
      this.searchForm.taskDetailId = ''
      this.searchForm.maintainanceStartTime = ''
      this.searchForm.maintainanceEndTime = ''
      this.searchForm.maintainancePlanId = ''
      this.searchForm.taskState = ''
      this.page.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    handleDetail(taskDetailId) {
      this.$router.push({
        path: '/views/inspection/maintainanceTaskDetailView',
        query: { taskDetailId }
      })
    },
    openFile(photo) {
      this.$refs.viewImage.open(photo.url)
    },
    openMap(lat, lng) {
      if (!lat || !lng) {
        this.$message.warning(this.$t('maintainanceTaskDetails.noLocation'))
        return
      }
      this.$refs.viewMap.open({ lat, lng })
    }
  }
}
</script>

<style lang="scss" scoped>
.maintainance-task-details-container {
  padding: 20px;

  .left-panel {
    .plan-card {
      height: 700px;

      .plan-list-wrapper {
        height: 660px;
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

        .plan-list-item {
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

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .margin-top-xs {
    margin-top: 10px;
  }

  .text-primary {
    color: #409EFF;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
