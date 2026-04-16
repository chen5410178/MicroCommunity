<template>
    <div class="staff-inspection-container">
      <el-row :gutter="20">
        <el-col :span="16" />
        <el-col :span="8" class="text-right">
          <el-button-group>
            <el-button
              :type="aStaffDetailInspectionInfo.undoOrder === '20200405' ? 'primary' : 'default'"
              size="small"
              @click="switchUndoInspectionOrder('20200405')"
            >
              {{ $t('staffDetailInspection.notStarted') }}
            </el-button>
            <el-button
              :type="aStaffDetailInspectionInfo.undoOrder === '20200406' ? 'primary' : 'default'"
              size="small"
              @click="switchUndoInspectionOrder('20200406')"
            >
              {{ $t('staffDetailInspection.inProgress') }}
            </el-button>
            <el-button
              :type="aStaffDetailInspectionInfo.undoOrder === '20200407' ? 'primary' : 'default'"
              size="small"
              @click="switchUndoInspectionOrder('20200407')"
            >
              {{ $t('staffDetailInspection.completed') }}
            </el-button>
          </el-button-group>
        </el-col>
      </el-row>
      <el-table :data="aStaffDetailInspectionInfo.inspectionTasks" border style="width: 100%" class="margin-top">
        <el-table-column prop="taskDetailId" :label="$t('staffDetailInspection.taskDetailId')" align="center" />
        <el-table-column prop="inspectionName" :label="$t('staffDetailInspection.inspectionName')" align="center" />
        <el-table-column prop="inspectionPlanName" :label="$t('staffDetailInspection.inspectionPlanName')" align="center" />
        <el-table-column prop="routeName" :label="$t('staffDetailInspection.routeName')" align="center" />
        <el-table-column :label="$t('staffDetailInspection.planTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.planInsTime }}<br>{{ scope.row.planEndTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('staffDetailInspection.pointTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.pointStartTime }}<br>{{ scope.row.pointEndTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('staffDetailInspection.actualInspectionTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.inspectionTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('staffDetailInspection.inspectionState')" align="center">
          <template slot-scope="scope">
            <span :class="{ 'text-primary': scope.row.inspectionState === '60000', 'text-danger': scope.row.inspectionState !== '60000' }">
              {{ scope.row.inspectionStateName || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="planUserName" :label="$t('staffDetailInspection.planUserName')" align="center" />
        <el-table-column :label="$t('staffDetailInspection.actualUserName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.actUserName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="signTypeName" :label="$t('staffDetailInspection.signType')" align="center" />
        <el-table-column :label="$t('staffDetailInspection.taskState')" align="center">
          <template slot-scope="scope">
            <span :class="{ 'text-danger': scope.row.state === '20200408' }">
              {{ scope.row.stateName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('staffDetailInspection.description')" align="center">
          <template slot-scope="scope">
            <span class="text-primary">{{ scope.row.description || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('staffDetailInspection.photos')" align="center">
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
        <el-table-column prop="createTime" :label="$t('staffDetailInspection.createTime')" align="center" />
        <el-table-column :label="$t('staffDetailInspection.location')" align="center" width="70">
          <template slot-scope="scope">
            <el-button size="mini" type="info" @click="openMap(scope.row.latitude, scope.row.longitude)">
              {{ $t('common.view') }}
            </el-button>
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
      <view-map ref="viewMap" />
      <view-image ref="viewImage" />
    </div>
  </template>
  
  <script>
  import { listAdminInspectionTaskDetails } from '@/api/staff/adminStaffDetailApi'
  import ViewMap from '@/components/system/ViewMap.vue'
  import ViewImage from '@/components/system/viewImage.vue'
  
  export default {
    name: 'AStaffDetailInspection',
    components: { ViewMap, ViewImage },
    data() {
      return {
        aStaffDetailInspectionInfo: {
          inspectionTasks: [],
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
        this.aStaffDetailInspectionInfo.staffId = data.staffId
        this.loadInspectionData(1, this.page.size)
      },
      async loadInspectionData(page, row) {
        try {
          const { data, total } = await listAdminInspectionTaskDetails({
            planUserId: this.aStaffDetailInspectionInfo.staffId,
            state: this.aStaffDetailInspectionInfo.undoOrder,
            page,
            row
          })
          this.aStaffDetailInspectionInfo.inspectionTasks = data.inspectionTaskDetails
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailInspection.fetchError'))
        }
      },
      switchUndoInspectionOrder(order) {
        this.aStaffDetailInspectionInfo.undoOrder = order
        this.page.current = 1
        this.loadInspectionData(this.page.current, this.page.size)
      },
      openFile(photo) {
        this.$refs.viewImage.open({ url: photo.url })
      },
      openMap(lat, lng) {
        if (!lat || !lng) {
          this.$message.warning(this.$t('staffDetailInspection.noLocation'))
          return
        }
        this.$refs.viewMap.open({ lat, lng })
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadInspectionData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadInspectionData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-inspection-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
    .text-primary {
      color: #409EFF;
    }
    .text-danger {
      color: #F56C6C;
    }
  }
  </style>