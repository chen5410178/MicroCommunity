<template>
  <div class="padding">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>{{ $t('repairDispatchManage.searchConditions') }}</div>
          </div>
          <div class="">
            <el-row :gutter="15">
              <el-col :span="4">
                <el-input :placeholder="$t('repairDispatchManage.placeholder.repairId')"
                  v-model.trim="repairDispatchManageInfo.conditions.repairId" clearable />
              </el-col>
              <el-col :span="4">
                <el-input :placeholder="$t('repairDispatchManage.placeholder.repairName')"
                  v-model.trim="repairDispatchManageInfo.conditions.repairName" clearable />
              </el-col>
              <el-col :span="4">
                <el-input :placeholder="$t('repairDispatchManage.placeholder.tel')"
                  v-model.trim="repairDispatchManageInfo.conditions.tel" clearable />
              </el-col>
              <el-col :span="4">
                <el-select v-model="repairDispatchManageInfo.conditions.repairType" style="width:100%">
                  <el-option :label="$t('repairDispatchManage.placeholder.selectRepairType')" value="" />
                  <el-option v-for="(item, index) in repairDispatchManageInfo.repairTypes" :key="index"
                    :label="item.repairTypeName" :value="item.repairType" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-select v-model="repairDispatchManageInfo.conditions.state" style="width:100%">
                  <el-option :label="$t('repairDispatchManage.placeholder.selectStatus')" value="" />
                  <el-option v-for="(item, index) in repairDispatchManageInfo.states" :key="index" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="_queryOwnerRepairMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('common.search') }}
                </el-button>
                <el-button type="default" @click="_resetOwnerRepairMethod">
                  <i class="el-icon-refresh"></i>
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="margin-top">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ repairDispatchManageInfo.conditions.roomName }}</span>
              <span>{{ $t('repairDispatchManage.repairBacklog') }}</span>
            </div>
          </div>
          <div class="">
            <el-table :data="repairDispatchManageInfo.ownerRepairs" border style="width:100%">
              <el-table-column prop="repairObjName" :label="$t('repairDispatchManage.table.location')" align="center" />
              <el-table-column prop="repairTypeName" :label="$t('repairDispatchManage.table.repairType')"
                align="center" />
              <el-table-column prop="repairName" :label="$t('repairDispatchManage.table.repairPerson')" align="center" />
              <el-table-column prop="tel" :label="$t('repairDispatchManage.table.repairPhone')" align="center" />
              <el-table-column prop="appointmentTime" :label="$t('repairDispatchManage.table.appointmentTime')"
                align="center" />
              <el-table-column :label="$t('repairDispatchManage.table.status')" align="center">
                <template slot-scope="scope">
                  <span
                    v-if="scope.row.state == '1800' && (scope.row.returnVisitFlag == '001' || scope.row.returnVisitFlag == '002')">
                    {{ scope.row.stateName }}({{ $t('repairDispatchManage.scheduledTask') }})
                  </span>
                  <span v-else>
                    {{ scope.row.stateName }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" align="center" width="300">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button
                      v-if="(scope.row.state == '1100' || scope.row.state == '1200' || scope.row.state == '1300') && scope.row.state != '2001'"
                      size="mini" @click="_openDispatchRepairModel(scope.row)">
                      {{ $t('repairDispatchManage.transferOrder') }}
                    </el-button>
                    <el-button
                      v-if="scope.row.preStaffId != '-1' && scope.row.ruId != scope.row.preRuId && scope.row.state != '2001'"
                      size="mini" @click="_openBackRepairModel(scope.row)">
                      {{ $t('repairDispatchManage.returnOrder') }}
                    </el-button>
                    <el-button
                      v-if="(scope.row.state == '1100' || scope.row.state == '1200' || scope.row.state == '1300') && scope.row.state != '2001'"
                      size="mini" @click="_openFinishRepair(scope.row)">
                      {{ $t('repairDispatchManage.complete') }}
                    </el-button>
                    <el-button
                      v-if="hasPrivilege('502021040151320003') && scope.row.state == '1800' && scope.row.returnVisitFlag == '003' && scope.row.state != '2001'"
                      size="mini" @click="_openAppraiseRepair(scope.row)">
                      {{ $t('repairDispatchManage.returnVisit') }}
                    </el-button>
                    <el-button v-if="scope.row.state == '1100' || scope.row.state == '1200' || scope.row.state == '1300'"
                      size="mini" @click="_openStopRepair(scope.row)">
                      {{ $t('repairDispatchManage.pause') }}
                    </el-button>
                    <el-button v-if="scope.row.state == '2001'" size="mini" @click="_openStartRepair(scope.row)">
                      {{ $t('repairDispatchManage.start') }}
                    </el-button>
                    <el-button size="mini" @click="_openDispatchRepairDetail(scope.row)">
                      {{ $t('common.detail') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" class="pagination" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <dispatch-repair ref="dispatchRepair" @success="handleSuccess" />
    <appraise-repair ref="appraiseRepair" @success="handleSuccess" />
    <stop-repair ref="stopRepair" @success="handleSuccess" />
    <start-repair ref="startRepair" @success="handleSuccess" />
  </div>
</template>

<script>
import { listStaffRepairs, listRepairSettings } from '@/api/work/repairDispatchManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import DispatchRepair from '@/components/work/dispatchRepair'
import AppraiseRepair from '@/components/work/AppraiseRepair'
import StopRepair from '@/components/work/StopRepair'
import StartRepair from '@/components/work/StartRepair'

export default {
  name: 'RepairDispatchManageList',
  components: {
    DispatchRepair,
    AppraiseRepair,
    StopRepair,
    StartRepair
  },
  data() {
    return {
      repairDispatchManageInfo: {
        ownerRepairs: [],
        total: 0,
        records: 1,
        moreCondition: false,
        repairTypes: [],
        states: [],
        conditions: {
          repairId: '',
          repairName: '',
          tel: '',
          repairType: '',
          roomId: '',
          roomName: '',
          ownerId: '',
          userId: '',
          userName: '',
          state: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
  },
  methods: {
    async _initData() {
      try {
        // 获取报修类型字典
        this.repairDispatchManageInfo.states = await getDict('r_repair_pool', 'state')
        this._listRepairTypes()
        this._listOwnerRepairs()
      } catch (error) {
        console.error('初始化数据失败:', error)
        this.$message.error(this.$t('common.initFailed'))
      }
    },
    async _listOwnerRepairs() {
      try {
        const params = {
          ...this.repairDispatchManageInfo.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }

        const res = await listStaffRepairs(params)
        this.repairDispatchManageInfo.ownerRepairs = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('获取报修列表失败:', error)
        this.$message.error(this.$t('repairDispatchManage.fetchRepairListFailed'))
      }
    },
    async _listRepairTypes() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.communityId
        }

        const res = await listRepairSettings(params)
        this.repairDispatchManageInfo.repairTypes = res.data
      } catch (error) {
        console.error('获取报修类型失败:', error)
        this.$message.error(this.$t('repairDispatchManage.fetchRepairTypesFailed'))
      }
    },
    _queryOwnerRepairMethod() {
      this.page.current = 1
      this._listOwnerRepairs()
    },
    _resetOwnerRepairMethod() {
      this.repairDispatchManageInfo.conditions.repairId = ''
      this.repairDispatchManageInfo.conditions.repairName = ''
      this.repairDispatchManageInfo.conditions.tel = ''
      this.repairDispatchManageInfo.conditions.repairType = ''
      this.repairDispatchManageInfo.conditions.state = ''
      this._listOwnerRepairs()
    },
    _openDispatchRepairModel(row) {
      this.$refs.dispatchRepair.open({ ...row, action: 'TRANSFER' })
    },
    _openBackRepairModel(row) {
      this.$refs.dispatchRepair.open({ ...row, action: 'BACK' })
    },
    _openFinishRepair(row) {
      this.$router.push({
        path: '/views/work/finishRepair',
        query: {
          repairType: row.repairType,
          repairId: row.repairId,
          repairObjType: row.repairObjType,
          publicArea: row.publicArea,
          repairChannel: row.repairChannel
        }
      })
    },
    _openDispatchRepairDetail(row) {
      this.$router.push({
        path: '/views/work/repairDetail',
        query: { repairId: row.repairId }
      })
    },
    _openAppraiseRepair(row) {
      this.$refs.appraiseRepair.open(row)
    },
    _openStopRepair(row) {
      this.$refs.stopRepair.open(row)
    },
    _openStartRepair(row) {
      this.$refs.startRepair.open(row)
    },
    handleSizeChange(size) {
      this.page.size = size
      this._listOwnerRepairs()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._listOwnerRepairs()
    },
    handleSuccess() {
      this._listOwnerRepairs()
    }
  }
}
</script>

<style scoped>
.ibox-content {
  padding: 15px 20px 20px 20px;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
</style>