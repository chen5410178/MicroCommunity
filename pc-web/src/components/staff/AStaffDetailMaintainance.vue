<template>
  <div class="staff-maintainance-container">
    <el-row :gutter="20">
      <el-col :span="16" />
      <el-col :span="8" class="text-right">
        <el-button-group>
          <el-button
            :type="aStaffDetailMaintainanceInfo.undoOrder === '20200405' ? 'primary' : 'default'"
            size="small"
            @click="switchUndoMaintainanceOrder('20200405')"
          >
            {{ $t('staffDetailMaintainance.notStarted') }}
          </el-button>
          <el-button
            :type="aStaffDetailMaintainanceInfo.undoOrder === '20200406' ? 'primary' : 'default'"
            size="small"
            @click="switchUndoMaintainanceOrder('20200406')"
          >
            {{ $t('staffDetailMaintainance.pendingCheck') }}
          </el-button>
          <el-button
            :type="aStaffDetailMaintainanceInfo.undoOrder === '20200407' ? 'primary' : 'default'"
            size="small"
            @click="switchUndoMaintainanceOrder('20200407')"
          >
            {{ $t('staffDetailMaintainance.completed') }}
          </el-button>
        </el-button-group>
      </el-col>
    </el-row>
    <el-table :data="aStaffDetailMaintainanceInfo.maintainanceTasks" border style="width: 100%" class="margin-top">
      <el-table-column prop="taskDetailId" :label="$t('staffDetailMaintainance.taskDetailId')" align="center" />
      <el-table-column prop="machineName" :label="$t('staffDetailMaintainance.machineName')" align="center" />
      <el-table-column prop="planName" :label="$t('staffDetailMaintainance.planName')" align="center" />
      <el-table-column prop="standardName" :label="$t('staffDetailMaintainance.standardName')" align="center" />
      <el-table-column prop="planUserName" :label="$t('staffDetailMaintainance.planUserName')" align="center" />
      <el-table-column :label="$t('staffDetailMaintainance.planTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.planInsTime }}<br>{{ scope.row.planEndTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailMaintainance.actualInspectionTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.inspectionTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailMaintainance.actualUserName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.actUserName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('staffDetailMaintainance.taskState')" align="center" />
      <el-table-column :label="$t('staffDetailMaintainance.description')" align="center">
        <template slot-scope="scope">
          <span class="text-primary">{{ scope.row.description || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailMaintainance.photos')" align="center">
        <template slot-scope="scope">
          <el-image
            v-for="photo in scope.row.photos"
            :key="photo.url"
            :src="photo.url"
            style="width: 60px; height: 60px; margin-right: 5px;"
            @click="openFile(photo)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('staffDetailMaintainance.createTime')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-link :href="`/views/inspection/maintainanceTaskDetailView?taskDetailId=${scope.row.taskDetailId}`">
            {{ $t('common.detail') }}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page.sync="page.current"
      :page-sizes="[10, 15, 20, 30]"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listAdminMaintainanceTaskDetail } from '@/api/staff/adminStaffDetailApi'
import ViewImage from '@/components/staff/ViewImage.vue'

export default {
  name: 'AStaffDetailMaintainance',
  components: { ViewImage },
  data() {
    return {
      aStaffDetailMaintainanceInfo: {
        maintainanceTasks: [],
        staffId: '',
        undoOrder: '20200405'
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    switchTab(data) {
      this.aStaffDetailMaintainanceInfo.staffId = data.staffId
      this.loadMaintainanceData(1, this.page.size)
    },
    async loadMaintainanceData(page, row) {
      try {
        const { data, total } = await listAdminMaintainanceTaskDetail({
          planUserId: this.aStaffDetailMaintainanceInfo.staffId,
          state: this.aStaffDetailMaintainanceInfo.undoOrder,
          page,
          row
        })
        this.aStaffDetailMaintainanceInfo.maintainanceTasks = data.inspectionTaskDetails
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('staffDetailMaintainance.fetchError'))
      }
    },
    switchUndoMaintainanceOrder(order) {
      this.aStaffDetailMaintainanceInfo.undoOrder = order
      this.page.current = 1
      this.loadMaintainanceData(this.page.current, this.page.size)
    },
    openFile(photo) {
      this.$refs.viewImage.open({ url: photo.url })
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadMaintainanceData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadMaintainanceData(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-maintainance-container {
  padding: 20px;
  .margin-top {
    margin-top: 20px;
  }
  .text-primary {
    color: #409EFF;
  }
}
</style>