<template>
  <div class="animated fadeInRight">
    <el-row :gutter="20">
      <!-- 左侧报修类型 -->
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li class="parking-area-item" :class="{ 'selected': selectedRepairType === '' }"
                @click="switchRepairType('')">
                {{ $t('repairPoolManage.all') }}
              </li>
              <li v-for="(item, index) in repairSettings" :key="index"
                class="parking-area-item"
                :class="{ 'selected': selectedRepairType === item.repairType }"
                @click="switchRepairType(item.repairType)">
                {{ item.repairTypeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="21" class="right-panel">
        <!-- 查询条件 -->
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairPoolManage.queryConditions') }}</span>
          </div>

          <el-form :model="conditions" label-width="auto">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairId" :placeholder="$t('repairPoolManage.workOrderNumber')" clearable />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairName" :placeholder="$t('repairPoolManage.repairPersonName')" clearable />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.tel" :placeholder="$t('repairPoolManage.repairPhone')" clearable />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairObjName" :placeholder="$t('repairPoolManage.repairLocation')" clearable />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="conditions.maintenanceType"
                    :placeholder="$t('repairPoolManage.selectMaintenanceType')" style="width:100%" clearable>
                    <el-option v-for="(item, index) in maintenanceTypes" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.startTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('repairPoolManage.startTime')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.endTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('repairPoolManage.endTime')" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="queryRepairPool">{{ $t('repairPoolManage.search') }}</el-button>
                <el-button @click="resetRepairPool">{{ $t('repairPoolManage.reset') }}</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <!-- 报修工单池 -->
        <el-card class="box-card">
          <!-- 状态Tab -->
          <el-tabs v-model="activeStateTab" @tab-click="handleStateTabClick">
            <el-tab-pane :label="$t('repairPoolManage.all')" name="ALL" />
            <el-tab-pane :label="$t('repairPoolManage.timeoutOrders')" name="TIMEOUT" />
            <el-tab-pane v-for="(item, index) in states" :key="index" :label="item.name" :name="item.statusCd" />
          </el-tabs>

          <el-table v-loading="loading" :data="repairPools" border style="width: 100%">
            <el-table-column prop="repairId" :label="$t('repairPoolManage.workOrderCode')" align="center" />
            <el-table-column prop="repairObjName" :label="$t('repairPoolManage.location')" align="center" />
            <el-table-column prop="repairTypeName" :label="$t('repairPoolManage.repairType')" align="center" />
            <el-table-column :label="$t('repairPoolManage.maintenanceType')" align="center">
              <template slot-scope="scope">
                {{ formatMaintenanceType(scope.row.maintenanceType) }}
              </template>
            </el-table-column>
            <el-table-column prop="repairName" :label="$t('repairPoolManage.repairPerson')" align="center" />
            <el-table-column prop="tel" :label="$t('repairPoolManage.contact')" align="center" />
            <el-table-column :label="$t('repairPoolManage.appointmentTime')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.appointmentTime }}</div>
                <div>~{{ scope.row.timeout }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('repairPoolManage.submissionTime')" align="center" />
            <el-table-column prop="submitHours" :label="$t('repairPoolManage.submissionDuration')" align="center">
              <template slot-scope="scope">
                {{ scope.row.submitHours || '0' }}
              </template>
            </el-table-column>
            <el-table-column prop="finishTime" :label="$t('repairPoolManage.completionTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.finishTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('repairPoolManage.status')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === '1800' && ['001', '002'].includes(scope.row.returnVisitFlag)">
                  {{ scope.row.stateName }}({{ $t('repairPoolManage.timedTaskProcessing') }})
                </span>
                <span v-else>{{ scope.row.stateName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('repairPoolManage.operation')" align="center" width="250">
              <template slot-scope="scope">
                <el-button v-if="hasPrivilege('502021012067300023') && ['1000', '1200'].includes(scope.row.state)"
                  size="mini" @click="openEditModal(scope.row)">
                  {{ $t('repairPoolManage.modify') }}
                </el-button>
                <el-button v-if="hasPrivilege('502019101946430010') && scope.row.state === '1000'" size="mini"
                  type="primary" @click="openDispatchModal(scope.row)">
                  {{ $t('repairPoolManage.dispatch') }}
                </el-button>
                <el-button
                  v-if="hasPrivilege('502021012099350016') && scope.row.state === '1000' && scope.row.repairWay === '100'"
                  size="mini" type="success" @click="grabOrder(scope.row)">
                  {{ $t('repairPoolManage.grabOrder') }}
                </el-button>

                <el-button
                  v-if="hasPrivilege('502019101946430010') && !['1900', '1700', '1800'].includes(scope.row.state)"
                  size="mini" type="warning" @click="openEndModal(scope.row)">
                  {{ $t('repairPoolManage.end') }}
                </el-button>

                <el-button size="mini" type="info" @click="openDetail(scope.row)">
                  {{ $t('repairPoolManage.details') }}
                </el-button>

                <el-button v-if="hasPrivilege('502021012051410024') && ['1000', '1200'].includes(scope.row.state)"
                  size="mini" type="danger" @click="openDeleteModal(scope.row.repairId)">
                  {{ $t('repairPoolManage.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination background layout="total, sizes, prev, pager, next, jumper" :current-page="page.current"
            :page-sizes="[10, 20, 30, 50]" :page-size="page.size" :total="page.total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" class="pagination" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 组件 -->
    <dispatch-repair ref="dispatchRepair" @success="fetchData" />
    <end-repair ref="endRepair" @success="fetchData" />
    <edit-owner-repair ref="editOwnerRepair" @success="fetchData" />
    <delete-owner-repair ref="deleteOwnerRepair" @success="fetchData" />
  </div>
</template>

<script>
import { listRepairPools, grabbingRepair } from '@/api/work/repairPoolManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import DispatchRepair from '@/components/work/dispatchRepair'
import EndRepair from '@/components/work/endRepair'
import EditOwnerRepair from '@/components/work/editOwnerRepair'
import DeleteOwnerRepair from '@/components/work/deleteOwnerRepair'
import { listRepairSettings } from '@/api/work/ownerRepairManageApi'

export default {
  name: 'RepairPoolManage',
  components: {
    DispatchRepair,
    EndRepair,
    EditOwnerRepair,
    DeleteOwnerRepair
  },
  data() {
    return {
      loading: false,
      repairPools: [],
      repairSettings: [],
      states: [],
      maintenanceTypes: [],
      selectedRepairType: '',
      activeStateTab: 'ALL',
      conditions: {
        repairId: '',
        repairName: '',
        tel: '',
        repairType: '',
        repairObjName: '',
        maintenanceType: '',
        startTime: '',
        endTime: '',
        state: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchRepairSettings()
    this.loadDictData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }

        const res = await listRepairPools(params)
        this.repairPools = res.data || []
        this.page.total = res.total || 0
      } catch (error) {
        console.error('Failed to fetch repair pools:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchRepairSettings() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId()
        }

        const res = await listRepairSettings(params)
        this.repairSettings = res.data || []
      } catch (error) {
        console.error('Failed to fetch repair settings:', error)
      }
    },

    async loadDictData() {
      try {
        this.states = await getDict('r_repair_pool', 'state')
        this.maintenanceTypes = await getDict('r_repair_pool', 'maintenance_type')
      } catch (error) {
        console.error('Failed to load dict data:', error)
      }
    },

    formatMaintenanceType(type) {
      const types = {
        '1001': this.$t('repairPoolManage.paidService'),
        '1002': this.$t('repairPoolManage.freeService'),
        '1003': this.$t('repairPoolManage.needMaterials'),
        '1004': this.$t('repairPoolManage.noMaterialsNeeded')
      }
      return types[type] || '--'
    },

    switchRepairType(repairType) {
      this.selectedRepairType = repairType
      this.conditions.repairType = repairType
      this.page.current = 1
      this.fetchData()
    },

    handleStateTabClick() {
      this.conditions.state = this.activeStateTab === 'ALL' ? '' : this.activeStateTab
      this.page.current = 1
      this.fetchData()
    },

    queryRepairPool() {
      this.page.current = 1
      this.fetchData()
    },

    resetRepairPool() {
      this.conditions = {
        repairId: '',
        repairName: '',
        tel: '',
        repairType: '',
        repairObjName: '',
        maintenanceType: '',
        startTime: '',
        endTime: '',
        state: this.conditions.state
      }
      this.selectedRepairType = ''
      this.page.current = 1
      this.fetchData()
    },

    openEditModal(repair) {
      this.$refs.editOwnerRepair.open(repair)
    },

    openDispatchModal(repair) {
      this.$refs.dispatchRepair.open(repair)
    },

    async grabOrder(repair) {
      try {
        const data = {
          communityId: getCommunityId(),
          repairId: repair.repairId
        }

        await grabbingRepair(data)
        this.$message.success(this.$t('common.operationSuccess'))
        this.fetchData()
      } catch (error) {
        console.error('Grab order failed:', error)
      }
    },

    openEndModal(repair) {
      this.$refs.endRepair.open(repair)
    },

    openDetail(repair) {
      this.$router.push(`/views/work/repairDetail?repairId=${repair.repairId}`)
    },

    openDeleteModal(repairId) {
      this.$refs.deleteOwnerRepair.open({repairId})
    },

    handleSizeChange(size) {
      this.page.size = size
      this.fetchData()
    },

    handleCurrentChange(current) {
      this.page.current = current
      this.fetchData()
    },

  }
}
</script>

<style lang="scss" scoped>
.animated {
  padding: 20px;
}

.right-panel {
  padding-left: 5px;
}

.left-panel {
  .parking-area-card {
    height: 700px;

    .parking-area-list-wrapper {
      height: 630px;
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

      .parking-area-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .parking-area-item {
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
}

.box-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
</style>