<template>
    <div class="staff-repair-container">
      <el-row :gutter="20">
        <el-col :span="16" />
        <el-col :span="8" class="text-right">
          <el-button-group>
            <el-button
              :type="aStaffDetailRepairInfo.undoOrder === 'undo' ? 'primary' : 'default'"
              size="small"
              @click="switchUndoOrder('undo')"
            >
              {{ $t('staffDetailRepair.undoOrder') }}
            </el-button>
            <el-button
              :type="aStaffDetailRepairInfo.undoOrder === 'do' ? 'primary' : 'default'"
              size="small"
              @click="switchUndoOrder('do')"
            >
              {{ $t('staffDetailRepair.doneOrder') }}
            </el-button>
          </el-button-group>
        </el-col>
      </el-row>
      <el-table :data="aStaffDetailRepairInfo.ownerRepairs" border style="width: 100%" class="margin-top">
        <el-table-column prop="repairId" :label="$t('staffDetailRepair.repairId')" align="center" />
        <el-table-column prop="repairObjName" :label="$t('staffDetailRepair.location')" align="center" />
        <el-table-column prop="repairTypeName" :label="$t('staffDetailRepair.repairType')" align="center" />
        <el-table-column :label="$t('staffDetailRepair.maintenanceType')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.maintenanceType === '1001'">{{ $t('staffDetailRepair.paidService') }}</span>
            <span v-else-if="scope.row.maintenanceType === '1002'">{{ $t('staffDetailRepair.freeService') }}</span>
            <span v-else-if="scope.row.maintenanceType === '1003'">{{ $t('staffDetailRepair.materialRequired') }}</span>
            <span v-else-if="scope.row.maintenanceType === '1004'">{{ $t('staffDetailRepair.noMaterial') }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="repairName" :label="$t('staffDetailRepair.repairPerson')" align="center" />
        <el-table-column prop="tel" :label="$t('staffDetailRepair.contact')" align="center" />
        <el-table-column prop="appointmentTime" :label="$t('staffDetailRepair.appointmentTime')" align="center" />
        <el-table-column :label="$t('staffDetailRepair.status')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.state === '1800' && (scope.row.returnVisitFlag === '001' || scope.row.returnVisitFlag === '002')">
              {{ scope.row.stateName }} ({{ $t('staffDetailRepair.timedTask') }})
            </span>
            <span v-else>{{ scope.row.stateName }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openRepairDetail(scope.row)">
              {{ $t('common.detail') }}
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
    </div>
  </template>
  
  <script>
  import { listAdminUndoRepairsByStaff, listAdminFinishRepairsByStaff } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailRepair',
    data() {
      return {
        aStaffDetailRepairInfo: {
          ownerRepairs: [],
          staffId: '',
          undoOrder: 'undo'
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
        this.aStaffDetailRepairInfo.staffId = data.staffId
        this.loadRepairData(1, this.page.size)
      },
      async loadRepairData(page, row) {
        try {
          const url = this.aStaffDetailRepairInfo.undoOrder === 'do'
            ? listAdminFinishRepairsByStaff
            : listAdminUndoRepairsByStaff
          const { data, total } = await url({ staffId: this.aStaffDetailRepairInfo.staffId, page, row })
          this.aStaffDetailRepairInfo.ownerRepairs = data
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailRepair.fetchError'))
        }
      },
      switchUndoOrder(order) {
        this.aStaffDetailRepairInfo.undoOrder = order
        this.page.current = 1
        this.loadRepairData(this.page.current, this.page.size)
      },
      openRepairDetail(repair) {
        this.$router.push(`/views/work/adminRepairDetail?repairId=${repair.repairId}`)
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadRepairData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadRepairData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-repair-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>