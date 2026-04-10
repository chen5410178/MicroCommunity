<template>
  <div class="maintainance-plan-machine-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('maintainancePlanMachine.title') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_goBack" style="margin-right:10px">
            <i class="el-icon-close"></i>
            <span>{{ $t('maintainancePlanMachine.back') }}</span>
          </el-button>
          <el-button type="primary" size="small" @click="_openAddMaintainancePlanMachineModal">
            <i class="el-icon-plus"></i>
            <span>{{ $t('maintainancePlanMachine.add') }}</span>
          </el-button>
        </div>
      </div>

      <el-table :data="maintainancePlanMachineInfo.machines" border style="width: 100%">
        <el-table-column prop="machineName" :label="$t('maintainancePlanMachine.machineName')" align="center">
        </el-table-column>
        <el-table-column prop="machineId" :label="$t('maintainancePlanMachine.machineId')" align="center">
        </el-table-column>
        <el-table-column :label="$t('maintainancePlanMachine.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="_openDeleteMaintainancePlanMachineModel(scope.row)">
              {{ $t('maintainancePlanMachine.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
      </el-pagination>
    </el-card>

    <choose-maintainance-plan-machine ref="chooseMachineModal"
      @success="handleSuccess"></choose-maintainance-plan-machine>
    <delete-maintainance-plan-machine ref="deleteMachineModal"
      @success="handleSuccess"></delete-maintainance-plan-machine>
  </div>
</template>

<script>
import { listMaintainancePlanMachine } from '@/api/inspection/maintainancePlanMachineApi'
import ChooseMaintainancePlanMachine from '@/components/inspection/chooseMaintainancePlanMachine'
import DeleteMaintainancePlanMachine from '@/components/inspection/deleteMaintainancePlanMachine'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainancePlanMachineList',
  components: {
    ChooseMaintainancePlanMachine,
    DeleteMaintainancePlanMachine
  },
  data() {
    return {
      maintainancePlanMachineInfo: {
        machines: [],
        planId: '',
        routeName: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.maintainancePlanMachineInfo.planId = this.$route.query.planId
    this._listMaintainancePlanMachines(1, 10)
  },
  methods: {
    async _listMaintainancePlanMachines(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: getCommunityId(),
          planId: this.maintainancePlanMachineInfo.planId
        }
        const { data, records } = await listMaintainancePlanMachine(params)
        this.maintainancePlanMachineInfo.machines = data
        this.pagination.total = records
      } catch (error) {
        this.$message.error(this.$t('maintainancePlanMachine.fetchError'))
      }
    },
    _openAddMaintainancePlanMachineModal() {
      this.$refs.chooseMachineModal.open({
        planId: this.maintainancePlanMachineInfo.planId
      })
    },
    _openDeleteMaintainancePlanMachineModel(machine) {
      this.$refs.deleteMachineModal.open({
        ...machine,
        planId: this.maintainancePlanMachineInfo.planId
      })
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this._listMaintainancePlanMachines(this.pagination.current, this.pagination.size)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listMaintainancePlanMachines(this.pagination.current, val)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listMaintainancePlanMachines(val, this.pagination.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.maintainance-plan-machine-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-header-actions {
        display: flex;
      }
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>