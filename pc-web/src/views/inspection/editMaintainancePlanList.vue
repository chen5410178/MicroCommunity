<template>
  <div class="edit-maintainance-plan-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editMaintainancePlan.title') }}</span>
      </div>

      <el-form ref="form" :model="editMaintainancePlanInfo" label-width="120px" class="text-left">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editMaintainancePlan.planName')" prop="planName">
              <el-input v-model="editMaintainancePlanInfo.planName"
                :placeholder="$t('editMaintainancePlan.planNamePlaceholder')" clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editMaintainancePlan.standard')" prop="standardId">
              <el-select v-model="editMaintainancePlanInfo.standardId"
                :placeholder="$t('editMaintainancePlan.standardPlaceholder')" style="width:100%">
                <el-option v-for="item in editMaintainancePlanInfo.standards" :key="item.standardId"
                  :label="item.standardName" :value="item.standardId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editMaintainancePlan.period')" prop="planPeriod">
              <el-select v-model="editMaintainancePlanInfo.planPeriod"
                :placeholder="$t('editMaintainancePlan.periodPlaceholder')" @change="changeMaintainancePeriod"
                style="width:100%">
                <el-option label="月/天" value="2020022">
                </el-option>
                <el-option label="固定(天)" value="2020024">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="editMaintainancePlanInfo.planPeriod === '2020022'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="$t('editMaintainancePlan.month')">
                <el-checkbox-group v-model="editMaintainancePlanInfo.months">
                  <el-checkbox v-for="index in 12" :key="index" :label="index">
                    {{ index }}{{ $t('editMaintainancePlan.monthUnit') }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="$t('editMaintainancePlan.day')">
                <el-checkbox-group v-model="editMaintainancePlanInfo.days">
                  <el-checkbox v-for="index in 31" :key="index" :label="index">
                    {{ index }}{{ $t('editMaintainancePlan.dayUnit') }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template v-if="editMaintainancePlanInfo.planPeriod === '2020024'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item :label="$t('editMaintainancePlan.fixedDay')">
                <el-input-number v-model="editMaintainancePlanInfo.everyDays" :min="1"
                  :placeholder="$t('editMaintainancePlan.fixedDayPlaceholder')">
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editMaintainancePlan.startDate')" prop="startDate">
              <el-date-picker v-model="editMaintainancePlanInfo.startDate" type="date"
                :placeholder="$t('editMaintainancePlan.startDatePlaceholder')" style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editMaintainancePlan.endDate')" prop="endDate">
              <el-date-picker v-model="editMaintainancePlanInfo.endDate" type="date"
                :placeholder="$t('editMaintainancePlan.endDatePlaceholder')" style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('editMaintainancePlan.selectStaff')">
              <select-staffs-div ref="selectStaffs" @selectStaffs="handleStaffSelected" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="primary" @click="saveMaintainancePlanInfo">
              {{ $t('common.save') }}
            </el-button>
            <el-button @click="goBack">
              {{ $t('common.back') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { updateMaintainancePlan, listMaintainanceStandard, listMaintainancePlan, listMaintainancePlanStaff } from '@/api/inspection/editMaintainancePlanApi'
import SelectStaffsDiv from '@/components/staff/selectStaffsDiv'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMaintainancePlan',
  components: {
    SelectStaffsDiv
  },
  data() {
    return {
      editMaintainancePlanInfo: {
        planId: '',
        planName: '',
        standardId: '',
        standards: [],
        planPeriod: '',
        startDate: new Date(),
        endDate: '2050-01-01',
        state: '2020025',
        remark: '',
        months: [],
        days: [],
        everyDays: [],
        staffs: [],
        communityId: ''
      }
    }
  },
  created() {
    this.editMaintainancePlanInfo.communityId = getCommunityId()
    this.editMaintainancePlanInfo.planId = this.$route.query.planId
    this.initEditMaintainancePlanDateInfo()
    this.listEditMaintainanceStandards()
    this.listMaintainancePlans()
    this.listMaintainancePlanStaffs()
  },
  methods: {
    initEditMaintainancePlanDateInfo() {
      // 日期初始化逻辑
    },
    handleStaffSelected(staffs) {
      console.log(staffs)
      this.editMaintainancePlanInfo.staffs = staffs
    },
    changeMaintainancePeriod() {
      this.editMaintainancePlanInfo.months = []
      this.editMaintainancePlanInfo.days = []
      this.editMaintainancePlanInfo.everyDays = []

      if (this.editMaintainancePlanInfo.planPeriod === '2020022') {
        for (let month = 1; month < 13; month++) {
          this.editMaintainancePlanInfo.months.push(month)
        }
        for (let day = 1; day < 32; day++) {
          this.editMaintainancePlanInfo.days.push(day)
        }
      }
    },
    async listEditMaintainanceStandards() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.editMaintainancePlanInfo.communityId
        }
        const { data } = await listMaintainanceStandard(params)
        this.editMaintainancePlanInfo.standards = data
      } catch (error) {
        this.$message.error(this.$t('editMaintainancePlan.fetchStandardError'))
      }
    },
    async listMaintainancePlans() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.editMaintainancePlanInfo.communityId,
          planId: this.editMaintainancePlanInfo.planId
        }
        const { data } = await listMaintainancePlan(params)
        if (data && data.length > 0) {
          Object.assign(this.editMaintainancePlanInfo, data[0])
          this.editMaintainancePlanInfo.months = data[0].maintainanceMonth.split(',').map(Number)
          this.editMaintainancePlanInfo.days = data[0].maintainanceDay.split(',').map(Number)
          this.editMaintainancePlanInfo.everyDays = data[0].maintainanceEveryday
        }
        console.log(this.editMaintainancePlanInfo)
      } catch (error) {
        this.$message.error(this.$t('editMaintainancePlan.fetchPlanError'))
      }
    },
    async listMaintainancePlanStaffs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.editMaintainancePlanInfo.communityId,
          planId: this.editMaintainancePlanInfo.planId
        }
        const { data } = await listMaintainancePlanStaff(params)
        this.editMaintainancePlanInfo.staffs = data.map(item => ({
          userId: item.staffId,
          name: item.staffName
        }))
        this.$refs.selectStaffs.setStaffs(this.editMaintainancePlanInfo.staffs)
      } catch (error) {
        this.$message.error(this.$t('editMaintainancePlan.fetchStaffError'))
      }
    },
    validateForm() {
      // 表单验证逻辑
      return true
    },
    async saveMaintainancePlanInfo() {
      if (!this.validateForm()) return

      try {
        const params = {
          ...this.editMaintainancePlanInfo,
          maintainanceMonth: this.editMaintainancePlanInfo.months.join(','),
          maintainanceDay: this.editMaintainancePlanInfo.days.join(','),
          maintainanceEveryday: this.editMaintainancePlanInfo.everyDays,
          staffs: this.$refs.selectStaffs.getSelectedStaffs()
        }

        const res = await updateMaintainancePlan(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('editMaintainancePlan.saveError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-maintainance-plan-container {
  padding: 20px;

  .el-checkbox {
    margin-right: 15px;
    margin-bottom: 10px;
  }

  .text-right {
    text-align: right;
  }

  .el-date-editor,
  .el-select {
    width: 100%;
  }
}
</style>