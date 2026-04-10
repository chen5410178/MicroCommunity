<template>
  <el-dialog
    :title="$t('chooseMaintainancePlanMachine.title')"
    :visible.sync="visible"
    width="80%"
    @close="handleClose">

      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchForm.machineName"
            :placeholder="$t('chooseMaintainancePlanMachine.machineNamePlaceholder')"
            clearable
            @keyup.enter.native="queryMachines">
          </el-input>
        </el-col>
        <el-col :span="16">
          <el-button type="primary" @click="queryMachines">
            <i class="el-icon-search"></i>
            {{ $t('chooseMaintainancePlanMachine.search') }}
          </el-button>
          <el-button @click="resetMachines" style="margin-left: 10px;">
            <i class="el-icon-refresh"></i>
            {{ $t('chooseMaintainancePlanMachine.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table
        :data="tableData"
        border
        style="width: 100%; margin-top: 20px;">
        <el-table-column width="50">
          <template slot="header">
            <el-checkbox v-model="isAllChecked" @change="handleCheckAllChange"></el-checkbox>
          </template>
          <template slot-scope="scope">
            <el-checkbox
              v-model="scope.row.checked"
              @change="handleCheckChange(scope.row)">
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          prop="machineId"
          :label="$t('chooseMaintainancePlanMachine.machineId')"
          align="center">
        </el-table-column>
        <el-table-column
          prop="machineName"
          :label="$t('chooseMaintainancePlanMachine.machineName')"
          align="center">
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px;">
      </el-pagination>


    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('chooseMaintainancePlanMachine.cancel') }}</el-button>
      <el-button type="primary" @click="submitSelected">{{ $t('chooseMaintainancePlanMachine.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listEquipmentAccount, saveMaintainancePlanMachine } from '@/api/inspection/maintainancePlanMachineApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ChooseMaintainancePlanMachine',
  data() {
    return {
      visible: false,
      searchForm: {
        machineName: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      isAllChecked: false,
      selectedMachines: [],
      formData: {}
    }
  },
  methods: {
    open(params) {
      this.formData = { ...params }
      this.visible = true
      this.resetMachines()
    },
    async queryMachines() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          machineName: this.searchForm.machineName,
          communityId: getCommunityId()
        }
        const { data, records } = await listEquipmentAccount(params)
        this.tableData = data.map(item => ({
          ...item,
          checked: false
        }))
        this.pagination.total = records
        this.isAllChecked = false
      } catch (error) {
        this.$message.error(this.$t('chooseMaintainancePlanMachine.fetchError'))
      }
    },
    resetMachines() {
      this.searchForm.machineName = ''
      this.pagination.current = 1
      this.queryMachines()
    },
    handleCheckAllChange(val) {
      this.tableData.forEach(item => {
        item.checked = val
      })
      this.updateSelectedMachines()
    },
    handleCheckChange(row) {
      console.log(row)
      this.isAllChecked = this.tableData.every(item => item.checked)
      this.updateSelectedMachines()
    },
    updateSelectedMachines() {
      this.selectedMachines = this.tableData
        .filter(item => item.checked)
        .map(item => item.machineId)
    },
    async submitSelected() {
      if (this.selectedMachines.length === 0) {
        this.$message.warning(this.$t('chooseMaintainancePlanMachine.selectWarning'))
        return
      }

      try {
        const params = {
          communityId: getCommunityId(),
          planId: this.formData.planId,
          machines: this.selectedMachines
        }
        await saveMaintainancePlanMachine(params)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('chooseMaintainancePlanMachine.addError'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.queryMachines()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.queryMachines()
    },
    handleClose() {
      this.searchForm.machineName = ''
      this.tableData = []
      this.selectedMachines = []
      this.isAllChecked = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>