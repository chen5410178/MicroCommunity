<template>
  <div class="add-maintainance-plan-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addMaintainancePlan.title') }}</span>
      </div>

      <el-form ref="form" :model="addMaintainancePlanInfo" label-width="120px" class="text-left">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addMaintainancePlan.planName')" prop="planName">
              <el-input v-model="addMaintainancePlanInfo.planName"
                :placeholder="$t('addMaintainancePlan.planNamePlaceholder')" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addMaintainancePlan.standard')" prop="standardId">
              <el-select v-model="addMaintainancePlanInfo.standardId"
                :placeholder="$t('addMaintainancePlan.standardPlaceholder')" style="width:100%" clearable>
                <el-option v-for="item in addMaintainancePlanInfo.standards" :key="item.standardId"
                  :label="item.standardName" :value="item.standardId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addMaintainancePlan.period')" prop="planPeriod">
              <el-select v-model="addMaintainancePlanInfo.planPeriod" @change="changeMaintainancePeriod"
                :placeholder="$t('addMaintainancePlan.periodPlaceholder')" style="width:100%" clearable>
                <el-option label="月/天" value="2020022" />
                <el-option label="固定(天)" value="2020024" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="addMaintainancePlanInfo.planPeriod === '2020022'">
          <el-col :span="24">
            <el-form-item :label="$t('addMaintainancePlan.month')">
              <el-checkbox-group v-model="addMaintainancePlanInfo.months">
                <el-checkbox v-for="index in 12" :key="index" :label="index">
                  {{ index }}{{ $t('addMaintainancePlan.monthUnit') }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="addMaintainancePlanInfo.planPeriod === '2020022'">
          <el-col :span="24">
            <el-form-item :label="$t('addMaintainancePlan.day')">
              <el-checkbox-group v-model="addMaintainancePlanInfo.days">
                <el-checkbox v-for="index in 31" :key="index" :label="index">
                  {{ index }}{{ $t('addMaintainancePlan.dayUnit') }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="addMaintainancePlanInfo.planPeriod === '2020024'">
          <el-col :span="12">
            <el-form-item :label="$t('addMaintainancePlan.fixedDays')">
              <el-input-number v-model="addMaintainancePlanInfo.everyDays" :min="1"
                :placeholder="$t('addMaintainancePlan.fixedDaysPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addMaintainancePlan.startDate')" prop="startDate">
              <el-date-picker v-model="addMaintainancePlanInfo.startDate" type="date"
                :placeholder="$t('addMaintainancePlan.startDatePlaceholder')" style="width:100%"
                value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addMaintainancePlan.endDate')" prop="endDate">
              <el-date-picker v-model="addMaintainancePlanInfo.endDate" type="date"
                :placeholder="$t('addMaintainancePlan.endDatePlaceholder')" style="width:100%"
                value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addMaintainancePlan.staff')">
              <select-staffs-div ref="selectStaffs" @selectStaffs="handleStaffSelected" />

            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addMaintainancePlan.machine')" prop="machineIds">
              <el-select v-model="addMaintainancePlanInfo.machineIds" multiple
                :placeholder="$t('addMaintainancePlan.machinePlaceholder')" style="width:100%">
                <el-option v-for="item in addMaintainancePlanInfo.machines" :key="item.machineId"
                  :label="`${item.machineName}(${item.machineCode})`" :value="item.machineId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="text-align: right;">
            <el-button type="warning" @click="goBack">
              {{ $t('common.back') }}
            </el-button>
            <el-button type="primary" @click="saveMaintainancePlanInfo">
              {{ $t('common.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaffsDiv from '@/components/staff/selectStaffsDiv'
import {
  saveMaintainancePlan,
  listMaintainanceStandard,
  listEquipmentAccount
} from '@/api/inspection/addMaintainancePlanApi'

export default {
  name: 'AddMaintainancePlanList',
  components: {
    SelectStaffsDiv
  },
  data() {
    return {
      addMaintainancePlanInfo: {
        planId: '',
        planName: '',
        standardId: '',
        standards: [],
        planPeriod: '',
        startDate: new Date().toISOString().split('T')[0],
        endDate: '2050-01-01',
        state: '2020025',
        remark: '',
        machineIds: [],
        months: [],
        days: [],
        everyDays: [],
        machines: []
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.addMaintainancePlanInfo.communityId = getCommunityId()
      await this.listMaintainanceStandards()
      await this.listEquipmentAccounts()
    },
    handleStaffSelected(staffs) {
      console.log(staffs)
      this.form.staffs = staffs
    },
    async listMaintainanceStandards() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addMaintainancePlanInfo.communityId
        }
        const { data } = await listMaintainanceStandard(params)
        this.addMaintainancePlanInfo.standards = data
      } catch (error) {
        console.error('获取保养标准失败:', error)
      }
    },
    async listEquipmentAccounts() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addMaintainancePlanInfo.communityId
        }
        const { data } = await listEquipmentAccount(params)
        this.addMaintainancePlanInfo.machines = data
      } catch (error) {
        console.error('获取设备列表失败:', error)
      }
    },
    changeMaintainancePeriod() {
      this.addMaintainancePlanInfo.months = []
      this.addMaintainancePlanInfo.days = []
      this.addMaintainancePlanInfo.everyDays = []
    },
    validateForm() {
      const requiredFields = [
        { field: 'planName', message: this.$t('addMaintainancePlan.validate.planNameRequired') },
        { field: 'standardId', message: this.$t('addMaintainancePlan.validate.standardRequired') },
        { field: 'planPeriod', message: this.$t('addMaintainancePlan.validate.periodRequired') },
        { field: 'startDate', message: this.$t('addMaintainancePlan.validate.startDateRequired') },
        { field: 'endDate', message: this.$t('addMaintainancePlan.validate.endDateRequired') }
      ]

      for (const { field, message } of requiredFields) {
        if (!this.addMaintainancePlanInfo[field]) {
          this.$message.error(message)
          return false
        }
      }

      if (this.addMaintainancePlanInfo.planPeriod === '2020022') {
        if (this.addMaintainancePlanInfo.months.length === 0) {
          this.$message.error(this.$t('addMaintainancePlan.validate.monthRequired'))
          return false
        }
        if (this.addMaintainancePlanInfo.days.length === 0) {
          this.$message.error(this.$t('addMaintainancePlan.validate.dayRequired'))
          return false
        }
      } else if (this.addMaintainancePlanInfo.planPeriod === '2020024') {
        if (!this.addMaintainancePlanInfo.everyDays) {
          this.$message.error(this.$t('addMaintainancePlan.validate.fixedDaysRequired'))
          return false
        }
      }

      if (this.addMaintainancePlanInfo.machineIds.length === 0) {
        this.$message.error(this.$t('addMaintainancePlan.validate.machineRequired'))
        return false
      }

      return true
    },
    async saveMaintainancePlanInfo() {
      if (!this.validateForm()) return

      try {
        const formData = {
          ...this.addMaintainancePlanInfo,
          maintainanceMonth: this.addMaintainancePlanInfo.months.join(','),
          maintainanceDay: this.addMaintainancePlanInfo.days.join(','),
          maintainanceEveryday: this.addMaintainancePlanInfo.everyDays,
          staffs: this.$refs.selectStaffs.getSelectedStaffs()
        }

        const { code, msg } = await saveMaintainancePlan(formData)
        if (code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(msg)
        }
      } catch (error) {
        console.error('保存保养计划失败:', error)
        this.$message.error(this.$t('addMaintainancePlan.saveError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-maintainance-plan-container {
  padding: 20px;

  .el-checkbox {
    margin-right: 15px;
    margin-bottom: 10px;
  }

  .el-date-editor,
  .el-select,
  .el-input {
    width: 100%;
  }
}
</style>