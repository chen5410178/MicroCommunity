<template>
  <div class="app-container padding">
    <el-row :gutter="20">
      <!-- 左侧状态栏 -->
      <el-col :span="3">
        <el-card>
          <div class="status-list">
            <div v-for="(item, index) in states" :key="index" class="status-item"
              :class="{ 'status-active': conditions.state === item.statusCd }" @click="switchRepairState(item)">
              {{ item.name }}
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="21">
        <!-- 查询条件 -->
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairPoolManage.queryConditions') }}</span>
            <!-- <el-button style="float: right; padding: 3px 0" type="text" @click="toggleMoreCondition">
              {{ moreCondition ? $t('repairPoolManage.hide') : $t('repairPoolManage.more') }}
            </el-button> -->
          </div>

          <el-form :model="conditions" label-width="auto">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairId" :placeholder="$t('repairPoolManage.workOrderNumber')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairName" :placeholder="$t('repairPoolManage.repairPersonName')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.tel" :placeholder="$t('repairPoolManage.repairPhone')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="conditions.repairType" :placeholder="$t('repairPoolManage.selectRepairType')"
                    style="width:100%">
                    <el-option v-for="(item, index) in repairSettings" :key="index" :label="item.repairTypeName"
                      :value="item.repairType" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.repairObjName" :placeholder="$t('repairPoolManage.repairLocation')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="conditions.maintenanceType"
                    :placeholder="$t('repairPoolManage.selectMaintenanceType')" style="width:100%">
                    <el-option v-for="(item, index) in maintenanceTypes" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.startTime" type="date" style="width:100%" value-format="yyyy-MM-dd"                    :placeholder="$t('repairPoolManage.startTime')"  />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.endTime" type="date" style="width:100%" value-format="yyyy-MM-dd"                    :placeholder="$t('repairPoolManage.endTime')"  />
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
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('repairPoolManage.repairOrderPool') }}</span>
          </div>

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
            <el-table-column :label="$t('repairPoolManage.operation')" align="center" width="300">
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
            @current-change="handleCurrentChange" />
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
      repairSettingTypes: [],
      maintenanceTypes: [],
      moreCondition: false,
      conditions: {
        repairId: '',
        repairName: '',
        tel: '',
        repairType: '',
        repairSettingType: '',
        roomId: '',
        roomName: '',
        ownerId: '',
        state: '',
        maintenanceType: '',
        startTime: '',
        endTime: '',
        repairObjName: ''
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
        // 获取状态字典
        const stateData = await getDict('r_repair_pool', 'state')
        this.states = [
          { statusCd: '', name: this.$t('repairPoolManage.all') },
          { statusCd: 'TIMEOUT', name: this.$t('repairPoolManage.timeoutOrders') },
          ...(stateData || [])
        ]

        // 获取报修设置类型字典
        this.repairSettingTypes = await getDict('r_repair_setting', 'repair_setting_type')

        // 获取维修类型字典
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

    switchRepairState(item) {
      this.conditions.state = item.statusCd
      this.fetchData()
    },

    toggleMoreCondition() {
      this.moreCondition = !this.moreCondition
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
        repairSettingType: '',
        repairObjName: '',
        maintenanceType: '',
        startTime: '',
        endTime: '',
        state: this.conditions.state // 保持当前状态
      }
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

<style scoped>
.status-list {
  display: flex;
  flex-direction: column;
}

.status-item {
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
}

.status-item:hover {
  background-color: #f5f7fa;
}

.status-active {
  background-color: #409eff;
  color: white;
}

.box-card {
  margin-bottom: 20px;
}
</style>