<template>
  <div class="padding">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>{{ $t('myRepairDispatchManage.queryCondition') }}</div>
            <div class="ibox-tools" style="top:10px;">
              <el-button type="text" style="margin-right:10px;" @click="_moreCondition()">
                {{ myRepairDispatchInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
              </el-button>
            </div>
          </div>
          <div class="">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-select v-model="myRepairDispatchInfo.conditions.maintenanceType"
                  :placeholder="$t('myRepairDispatchManage.selectMaintenanceType')" style="width:100%;">
                  <el-option v-for="(item, index) in myRepairDispatchInfo.maintenanceTypes" :key="index" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-input v-model.trim="myRepairDispatchInfo.conditions.repairName"
                  :placeholder="$t('myRepairDispatchManage.inputRepairName')" clearable />
              </el-col>
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-input v-model.trim="myRepairDispatchInfo.conditions.tel"
                  :placeholder="$t('myRepairDispatchManage.inputRepairTel')" clearable />
              </el-col>
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-select v-model="myRepairDispatchInfo.conditions.repairType"
                  :placeholder="$t('myRepairDispatchManage.selectRepairType')" style="width:100%;">
                  <el-option v-for="(item, index) in myRepairDispatchInfo.repairTypes" :key="index"
                    :label="item.repairTypeName" :value="item.repairType" />
                </el-select>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-select v-model="myRepairDispatchInfo.conditions.state"
                  :placeholder="$t('myRepairDispatchManage.selectRepairState')" style="width:100%;">
                  <el-option v-for="(item, index) in myRepairDispatchInfo.states" :key="index" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-button type="primary" icon="el-icon-search" @click="_queryMyRepairDispatchMethod">
                  {{ $t('common.search') }}
                </el-button>
                <el-button icon="el-icon-refresh" @click="_resetMyRepairDispatchMethod">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
            <el-row v-show="myRepairDispatchInfo.moreCondition" :gutter="20" style="margin-top:15px;">
              <el-col :xs="24" :sm="12" :md="6" :lg="4">
                <el-input v-model.trim="myRepairDispatchInfo.conditions.repairId"
                  :placeholder="$t('myRepairDispatchManage.inputRepairId')" clearable />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top:20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ myRepairDispatchInfo.conditions.roomName }}</span>
              <span>{{ $t('myRepairDispatchManage.repairDispatch') }}</span>
            </div>
          </div>
          <div class="">
            <el-table :data="myRepairDispatchInfo.ownerRepairs" border style="width: 100%">
              <el-table-column prop="repairId" :label="$t('myRepairDispatchManage.repairId')" align="center" />
              <el-table-column prop="repairObjName" :label="$t('myRepairDispatchManage.location')" align="center" />
              <el-table-column prop="repairTypeName" :label="$t('myRepairDispatchManage.repairType')" align="center" />
              <el-table-column :label="$t('myRepairDispatchManage.maintenanceType')" align="center">
                <template slot-scope="scope">
                  {{ getMaintenanceTypeName(scope.row.maintenanceType) }}
                </template>
              </el-table-column>
              <el-table-column prop="repairName" :label="$t('myRepairDispatchManage.repairPerson')" align="center" />
              <el-table-column prop="tel" :label="$t('myRepairDispatchManage.contact')" align="center" />
              <el-table-column prop="appointmentTime" :label="$t('myRepairDispatchManage.appointmentTime')"
                align="center" />
              <el-table-column :label="$t('myRepairDispatchManage.state')" align="center">
                <template slot-scope="scope">
                  <span
                    v-if="scope.row.state == '1800' && (scope.row.returnVisitFlag == '001' || scope.row.returnVisitFlag == '002')">
                    {{ scope.row.stateName }}({{ $t('myRepairDispatchManage.scheduledTask') }})
                  </span>
                  <span v-else>
                    {{ scope.row.stateName }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="_openRepairDetail(scope.row)">
                    {{ $t('common.detail') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination background layout="total, sizes, prev, pager, next, jumper"
              :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
              :total="pagination.total" @size-change="handleSizeChange" @current-change="handleCurrentChange"
              style="margin-top: 20px;" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <close-order ref="closeOrderModal" @success="handleCloseOrderSuccess" />
  </div>
</template>
  
<script>
import { listStaffFinishRepairs, listRepairSettings, closeRepairDispatch } from '@/api/work/myRepairDispatchManageApi'
import { getDict } from '@/api/community/communityApi'
import CloseOrder from '@/components/work/closeOrder'

export default {
  name: 'MyRepairDispatchManageList',
  components: {
    CloseOrder
  },
  data() {
    return {
      myRepairDispatchInfo: {
        ownerRepairs: [],
        moreCondition: false,
        currentRepairId: '',
        repairTypes: [],
        states: [],
        maintenanceTypes: [],
        conditions: {
          pageFlag: 'myRepairDispatch',
          repairId: '',
          repairName: '',
          tel: '',
          repairType: '',
          roomId: '',
          roomName: '',
          ownerId: '',
          state: '',
          maintenanceType: '',
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
    this.initData()
  },
  methods: {
    async initData() {
      try {
        this.myRepairDispatchInfo.conditions.communityId = this.getCommunityId()
        await this.getDictData()
        this._listRepairTypes()
        this._listOwnerRepairs(1, this.pagination.size)
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },

    async getDictData() {
      try {
        const stateData = await getDict('r_repair_pool', 'state')
        this.myRepairDispatchInfo.states = stateData

        const maintenanceTypeData = await getDict('r_repair_pool', 'maintenance_type')
        this.myRepairDispatchInfo.maintenanceTypes = maintenanceTypeData
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async _listOwnerRepairs(page, size) {
      try {
        this.myRepairDispatchInfo.conditions.page = page
        this.myRepairDispatchInfo.conditions.row = size

        const params = {
          ...this.myRepairDispatchInfo.conditions,
          repairId: this.myRepairDispatchInfo.conditions.repairId.trim(),
          repairName: this.myRepairDispatchInfo.conditions.repairName.trim(),
          tel: this.myRepairDispatchInfo.conditions.tel.trim()
        }

        const res = await listStaffFinishRepairs(params)
        this.myRepairDispatchInfo.ownerRepairs = res.data
        this.pagination.total = res.total
        this.pagination.current = page
      } catch (error) {
        console.error('获取报修列表失败:', error)
      }
    },

    async _listRepairTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.getCommunityId()
        }

        const res = await listRepairSettings(params)
        this.myRepairDispatchInfo.repairTypes = res.data
      } catch (error) {
        console.error('获取报修类型失败:', error)
      }
    },

    _moreCondition() {
      this.myRepairDispatchInfo.moreCondition = !this.myRepairDispatchInfo.moreCondition
    },

    _openRepairDetail(repairPool) {
      this.$router.push({
        path: '/views/work/repairDetail',
        query: { repairId: repairPool.repairId }
      })
    },

    _openDealRepair(ownerRepair) {
      this.myRepairDispatchInfo.currentRepairId = ownerRepair.repairId
      this.$refs.closeOrderModal.open()
    },

    async handleCloseOrderSuccess(orderInfo) {
      try {
        const params = {
          repairId: this.myRepairDispatchInfo.currentRepairId,
          context: orderInfo.remark,
          communityId: this.getCommunityId(),
          state: orderInfo.state === '1100' ? '10002' : '10003'
        }

        await closeRepairDispatch(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listOwnerRepairs(this.pagination.current, this.pagination.size)
      } catch (error) {
        console.error('结单操作失败:', error)
      }
    },

    _queryMyRepairDispatchMethod() {
      this._listOwnerRepairs(1, this.pagination.size)
    },

    _resetMyRepairDispatchMethod() {
      this.myRepairDispatchInfo.conditions = {
        ...this.myRepairDispatchInfo.conditions,
        repairId: '',
        repairName: '',
        tel: '',
        repairType: '',
        state: '',
        maintenanceType: ''
      }
      this._listOwnerRepairs(1, this.pagination.size)
    },

    getMaintenanceTypeName(type) {
      const mapping = {
        '1001': this.$t('myRepairDispatchManage.paidService'),
        '1002': this.$t('myRepairDispatchManage.freeService'),
        '1003': this.$t('myRepairDispatchManage.needMaterial'),
        '1004': this.$t('myRepairDispatchManage.noMaterial')
      }
      return mapping[type] || '--'
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this._listOwnerRepairs(1, size)
    },

    handleCurrentChange(page) {
      this._listOwnerRepairs(page, this.pagination.size)
    }
  }
}
</script>
  
<style scoped>
.ibox-content {
  padding: 15px 20px 20px 20px;
}

.ibox-tools {
  position: absolute;
  right: 15px;
  top: 15px;
}
</style>