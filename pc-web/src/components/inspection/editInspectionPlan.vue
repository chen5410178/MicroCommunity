<template>
  <el-dialog :title="$t('inspectionPlan.modify')" :visible.sync="visible" width="80%" @close="resetForm">
    <el-form ref="form" :model="formData" label-width="150px" class="text-left">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('inspectionPlan.planName')" prop="inspectionPlanName" required>
            <el-input v-model="formData.inspectionPlanName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('inspectionPlan.planRoute')" prop="inspectionRouteId" required>
            <el-select v-model="formData.inspectionRouteId" style="width:100%" @change="handleRouteChange">
              <el-option v-for="route in routeOptions" :key="route.inspectionRouteId" :label="route.routeName"
                :value="route.inspectionRouteId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('inspectionPlan.planCycle')" prop="inspectionPlanPeriod" required>
            <el-select v-model="formData.inspectionPlanPeriod" style="width:100%" @change="handlePeriodChange">
              <el-option label="月/天" value="2020022" />
              <el-option label="按周" value="2020023" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('inspectionPlan.taskAdvance')">
            <el-input-number v-model="formData.beforeTime" :min="1" />
            <span class="margin-left">{{ $t('editInspectionPlan.minutesGenerate') }}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 月/天选择 -->
      <template v-if="formData.inspectionPlanPeriod === '2020022'">
        <el-row>
          <el-form-item :label="$t('editInspectionPlan.month')">
            <el-checkbox-group v-model="formData.months">
              <el-checkbox v-for="month in 12" :key="month" :label="month">
                {{ month }}{{ $t('editInspectionPlan.month') }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item :label="$t('editInspectionPlan.day')">
            <el-checkbox-group v-model="formData.days">
              <el-checkbox v-for="day in 31" :key="day" :label="day">
                {{ day }}{{ $t('editInspectionPlan.day') }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-row>
      </template>

      <!-- 按周选择 -->
      <template v-if="formData.inspectionPlanPeriod === '2020023'">
        <el-row>
          <el-form-item :label="$t('editInspectionPlan.week')">
            <el-checkbox-group v-model="formData.workdays">
              <el-checkbox v-for="(day, index) in weekDays" :key="index" :label="index + 1">
                {{ day }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-row>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.startDate')" required>
            <el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.endDate')" required>
            <el-date-picker v-model="formData.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.startTime')" required>
            <el-time-picker v-model="formData.startTime" value-format="HH:mm" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.endTime')" required>
            <el-time-picker v-model="formData.endTime" value-format="HH:mm" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.signMethod')" required>
            <el-select v-model="formData.signType" style="width:100%">
              <el-option v-for="sign in signTypeOptions" :key="sign.statusCd" :label="sign.name"
                :value="sign.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPlan.allowRecheck')">
            <el-select v-model="formData.canReexamine" style="width:100%">
              <el-option :label="$t('addInspectionPlan.notAllowed')" value="1000"></el-option>
              <el-option :label="$t('addInspectionPlan.allowed')" value="2000"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-form-item :label="$t('editInspectionPlan.selectStaff')">
          <select-staff-div ref="selectStaffDiv" @selectStaffs="handleSelectStaffs" />
        </el-form-item>
      </el-row>
    </el-form>

    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="savePlan">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateInspectionPlan, listInspectionRoutes, listInspectionPlanStaffs } from '@/api/inspection/inspectionPlanApi'
import SelectStaffDiv from '@/components/staff/selectStaffsDiv'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditInspectionPlan',
  components: {
    SelectStaffDiv
  },
  data() {
    return {
      visible: false,
      formData: {
        inspectionPlanId: '',
        inspectionPlanName: '',
        inspectionRouteId: '',
        inspectionPlanPeriod: '',
        startDate: '',
        endDate: '',
        beforeTime: 30,
        startTime: '',
        endTime: '',
        signType: '',
        canReexamine: '1000',
        months: [],
        days: [],
        workdays: [],
        staffs: []
      },
      routeOptions: [],
      signTypeOptions: [],
      weekDays: [
        this.$t('addInspectionPlan.monday'),
        this.$t('addInspectionPlan.tuesday'),
        this.$t('addInspectionPlan.wednesday'),
        this.$t('addInspectionPlan.thursday'),
        this.$t('addInspectionPlan.friday'),
        this.$t('addInspectionPlan.saturday'),
        this.$t('addInspectionPlan.sunday')
      ]
    }
  },
  async created() {
    await this.loadDictData()
  },
  methods: {
    async loadDictData() {
      try {
        this.signTypeOptions = await getDict('inspection_plan', 'sign_type')
      } catch (error) {
        console.error('Failed to load sign types:', error)
      }
    },

    async loadRoutes() {
      try {
        const { inspectionRoutes } = await listInspectionRoutes({
          page: 1,
          row: 100
        })
        this.routeOptions = inspectionRoutes
      } catch (error) {
        console.error('Failed to load routes:', error)
      }
    },

    open(plan) {
      this.formData = {
        ...this.formData,
        ...plan,
        months: plan.inspectionMonth ? plan.inspectionMonth.split(',').map(Number) : [],
        days: plan.inspectionDay ? plan.inspectionDay.split(',').map(Number) : [],
        workdays: plan.inspectionWorkday ? plan.inspectionWorkday.split(',').map(Number) : []
      }
      this.loadStaffs(plan.inspectionPlanId)
      this.loadRoutes()

      this.visible = true
    },

    async loadStaffs(planId) {
      try {
        const { inspectionPlanStaffs } = await listInspectionPlanStaffs({ inspectionPlanId: planId, page: 1, row: 100 })
        this.formData.staffs = inspectionPlanStaffs.map(item => ({
          userId: item.staffId,
          name: item.staffName
        }))
        this.$refs.selectStaffDiv.setStaffs(this.formData.staffs)
      } catch (error) {
        console.error('Failed to load staffs:', error)
      }
    },

    handleSelectStaffs(staffs) {
      this.formData.staffs = staffs
    },

    handleRouteChange() {
      // 处理路线变更逻辑
    },

    handlePeriodChange() {
      // 处理周期变更逻辑
    },

    resetForm() {
      this.$refs.form.resetFields()
    },

    async savePlan() {
      try {
        // 获取选中的员工
        // 准备数据
        const payload = {
          ...this.formData,
          inspectionMonth: this.formData.months.join(','),
          inspectionDay: this.formData.days.join(','),
          inspectionWorkday: this.formData.workdays.join(',')
        }

        // 调用API
        await updateInspectionPlan(payload)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {

        this.$message.error(this.$t('inspectionPlan.updateError'))
      }
    }
  }
}
</script>

<style scoped>
.margin-left {
  margin-left: 10px;
}
</style>