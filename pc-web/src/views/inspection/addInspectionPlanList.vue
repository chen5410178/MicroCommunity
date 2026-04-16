<template>
  <div class="add-inspection-plan-container">
    <el-card class="box-card">
      <div slot="header" class="text-left">
        <span>{{ $t('addInspectionPlan.title') }}</span>
      </div>

      <el-form ref="form" :model="form" label-width="150px" label-position="right" class="text-left">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.planName')" prop="inspectionPlanName" required>
              <el-input v-model="form.inspectionPlanName"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.planName')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.inspectionRoute')" prop="inspectionRouteId" required>
              <el-select v-model="form.inspectionRouteId"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.inspectionRoute')"
                style="width:100%" @change="changeInspectionPeriod">
                <el-option v-for="item in inspectionRoutes" :key="item.inspectionRouteId" :label="item.routeName"
                  :value="item.inspectionRouteId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.inspectionPeriod')" prop="inspectionPlanPeriod" required>
              <el-select v-model="form.inspectionPlanPeriod"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.inspectionPeriod')"
                style="width:100%" @change="changeInspectionPeriod">
                <el-option v-for="item in inspectionPlanPeriods" :key="item.id" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.taskAdvance')">
              <el-input v-model="form.beforeTime" :placeholder="$t('addInspectionPlan.minutesGenerate')"
                style="width: calc(100% - 100px); margin-right: 10px;"></el-input>
              <span>{{ $t('addInspectionPlan.minutesGenerate') }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <template v-if="form.inspectionPlanPeriod === '2020022'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="$t('addInspectionPlan.month')">
                <el-checkbox-group v-model="form.months">
                  <el-checkbox v-for="index in 12" :key="index" :label="index">{{ index }}{{ $t('addInspectionPlan.month')
                  }}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="$t('addInspectionPlan.day')">
                <el-checkbox-group v-model="form.days">
                  <el-checkbox v-for="index in 31" :key="index" :label="index">{{ index }}{{ $t('addInspectionPlan.day')
                  }}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template v-if="form.inspectionPlanPeriod === '2020023'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item :label="$t('addInspectionPlan.week')">
                <el-checkbox-group v-model="form.workdays">
                  <el-checkbox :label="1">{{ $t('addInspectionPlan.monday') }}</el-checkbox>
                  <el-checkbox :label="2">{{ $t('addInspectionPlan.tuesday') }}</el-checkbox>
                  <el-checkbox :label="3">{{ $t('addInspectionPlan.wednesday') }}</el-checkbox>
                  <el-checkbox :label="4">{{ $t('addInspectionPlan.thursday') }}</el-checkbox>
                  <el-checkbox :label="5">{{ $t('addInspectionPlan.friday') }}</el-checkbox>
                  <el-checkbox :label="6">{{ $t('addInspectionPlan.saturday') }}</el-checkbox>
                  <el-checkbox :label="7">{{ $t('addInspectionPlan.sunday') }}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.startDate')" prop="startDate" required>
              <el-date-picker v-model="form.startDate" type="date"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.startDate')"
                value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.endDate')" prop="endDate" required>
              <el-date-picker v-model="form.endDate" type="date"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.endDate')"
                value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.startTime')" prop="startTime" required>
              <el-time-picker v-model="form.startTime"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.startTime')" value-format="HH:mm"
                style="width:100%"></el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.endTime')" prop="endTime" required>
              <el-time-picker v-model="form.endTime"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.endTime')" value-format="HH:mm"
                style="width:100%"></el-time-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.signMethod')" prop="signType" required>
              <el-select v-model="form.signType"
                :placeholder="$t('addInspectionPlan.required') + $t('addInspectionPlan.signMethod')" style="width:100%">
                <el-option v-for="item in signTypes" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addInspectionPlan.allowRecheck')">
              <el-select v-model="form.canReexamine" style="width:100%">
                <el-option :label="$t('addInspectionPlan.notAllowed')" value="1000"></el-option>
                <el-option :label="$t('addInspectionPlan.allowed')" value="2000"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addInspectionPlan.selectStaff')">
              <select-staffs-div ref="selectStaffs" @selectStaffs="handleStaffSelected" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="goBack">
              <i class="el-icon-close"></i> {{ $t('addInspectionPlan.back') }}
            </el-button>
            <el-button type="primary" @click="saveInspectionPlan">
              <i class="el-icon-check"></i> {{ $t('addInspectionPlan.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { listInspectionRoutes, saveInspectionPlan } from '@/api/inspection/addInspectionPlanApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaffsDiv from '@/components/staff/selectStaffsDiv'

export default {
  name: 'AddInspectionPlan',
  components: {
    SelectStaffsDiv
  },
  data() {
    return {
      form: {
        inspectionPlanId: '',
        inspectionPlanName: '',
        inspectionRouteId: '',
        inspectionPlanPeriod: '',
        startDate: this.formatDate(new Date()),
        endDate: '2050-01-01',
        beforeTime: '30',
        startTime: '',
        endTime: '',
        signType: '',
        canReexamine: '1000',
        state: '2020025',
        remark: '',
        months: Array.from({ length: 12 }, (_, i) => i + 1),
        days: Array.from({ length: 31 }, (_, i) => i + 1),
        workdays: Array.from({ length: 7 }, (_, i) => i + 1),
        staffs: []
      },
      inspectionRoutes: [],
      inspectionPlanPeriods: [],
      signTypes: [],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadData()
  },
  methods: {
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    async loadData() {
      try {
        // 加载巡检路线
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        this.inspectionRoutes = await listInspectionRoutes( params )

        // 加载字典数据
        this.inspectionPlanPeriods = await getDict('inspection_plan', 'inspection_plan_period')
        this.signTypes = await getDict('inspection_plan', 'sign_type')
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error(this.$t('addInspectionPlan.fetchDataError'))
      }
    },
    changeInspectionPeriod() {
      console.log(this.form.inspectionPlanPeriod)
      if (this.form.inspectionPlanPeriod === '2020022') {
        this.form.months = Array.from({ length: 12 }, (_, i) => i + 1)
        this.form.days = Array.from({ length: 31 }, (_, i) => i + 1)
      } else if (this.form.inspectionPlanPeriod === '2020023') {
        this.form.workdays = Array.from({ length: 7 }, (_, i) => i + 1)
      }
    },
    handleStaffSelected(staffs) {
      console.log(staffs)
      this.form.staffs = staffs
    },
    validateForm() {
      const requiredFields = [
        'inspectionPlanName',
        'inspectionRouteId',
        'inspectionPlanPeriod',
        'startDate',
        'endDate',
        'startTime',
        'endTime',
        'signType'
      ]

      for (const field of requiredFields) {
        if (!this.form[field]) {
          this.$message.warning(this.$t('addInspectionPlan.required') + this.$t(`addInspectionPlan.${field}`))
          return false
        }
      }

      if (new Date(this.form.startDate) > new Date(this.form.endDate)) {
        this.$message.warning(this.$t('addInspectionPlan.startDateMustBeforeEndDate'))
        return false
      }

      return true
    },
    async saveInspectionPlan() {
      if (!this.validateForm()) return

      try {
        const formData = {
          ...this.form,
          communityId: this.communityId,
          inspectionMonth: this.form.months.join(','),
          inspectionDay: this.form.days.join(','),
          inspectionWorkday: this.form.workdays.join(','),
          staffs: this.form.staffs
        }

        await saveInspectionPlan(formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('保存巡检计划失败:', error)
        this.$message.error(error.message || this.$t('addInspectionPlan.saveError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    getWorkDay(index) {
      const days = [
        this.$t('addInspectionPlan.monday'),
        this.$t('addInspectionPlan.tuesday'),
        this.$t('addInspectionPlan.wednesday'),
        this.$t('addInspectionPlan.thursday'),
        this.$t('addInspectionPlan.friday'),
        this.$t('addInspectionPlan.saturday'),
        this.$t('addInspectionPlan.sunday')
      ]
      return days[index - 1] || ''
    }
  }
}
</script>

<style scoped>
.add-inspection-plan-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.text-right {
  text-align: right;
}

.el-checkbox {
  margin-right: 15px;
}
</style>