<template>
  <div class="workflow-setting-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workflowSettingManage.title') }}</span>
          </div>
          <el-form label-width="120px" class="text-left">
            <el-form-item :label="$t('workflowSettingManage.flowName')">
              <el-input v-model="workflowSettingInfo.flowName"
                :placeholder="$t('workflowSettingManage.flowNamePlaceholder')" disabled />
            </el-form-item>

            <el-form-item :label="$t('workflowSettingManage.description')">
              <el-input v-model="workflowSettingInfo.describle" type="textarea"
                :placeholder="$t('workflowSettingManage.descriptionPlaceholder')" :rows="3" />
            </el-form-item>

            <el-form-item :label="$t('workflowSettingManage.submitterEnd')">
              <el-select v-model="workflowSettingInfo.startNodeFinish" style="width:100%"
                :placeholder="$t('workflowSettingManage.submitterEndPlaceholder')">
                <el-option :label="$t('workflowSettingManage.yes')" value="Y" />
                <el-option :label="$t('workflowSettingManage.no')" value="N" />
              </el-select>
            </el-form-item>

            <el-form-item :label="$t('workflowSettingManage.flowSteps')">
              <div class="step-actions margin-bottom">
                <el-button type="primary" @click="addWorkflowStep">
                  {{ $t('workflowSettingManage.addStep') }}
                </el-button>
              </div>

              <div v-for="(item, index) in workflowSettingInfo.steps" :key="index" class="step-item">
                <div class="step-header">
                  <span>{{ $t('workflowSettingManage.step') }} {{ index + 1 }}</span>

                  <el-button type="text" @click="chooseStaff(item)">
                    {{ item.staffId ? item.staffName : $t('workflowSettingManage.selectStaff') }}
                  </el-button>

                  <div class="radio-group">
                    <label class="radio-item">
                      <input
                        type="radio"
                        :name="`step-type-${index}`"
                        value="2"
                        v-model="item.type"
                        @change="chooseType(item)"
                      />
                      <span>{{ $t('workflowSettingManage.normalFlow') }}</span>
                    </label>
                    <label class="radio-item" v-if="index !== 0">
                      <input
                        type="radio"
                        :name="`step-type-${index}`"
                        value="1"
                        v-model="item.type"
                        @change="chooseType(item)"
                      />
                      <span>{{ $t('workflowSettingManage.countersign') }}</span>
                    </label>
                  </div>

                  <el-button type="text" @click="deleteStep(item)">
                    {{ $t('workflowSettingManage.deleteStep') }}
                  </el-button>

                  <el-button v-if="item.type == 1" type="text" @click="addStaff(item)">
                    {{ $t('workflowSettingManage.addStaff') }}
                  </el-button>
                </div>

                <div v-for="(subItem, subIndex) in item.subStaff" :key="subIndex" class="sub-staff-item">
                  <el-button type="text" @click="chooseStaff(subItem)">
                    {{ subItem.staffId ? subItem.staffName : $t('workflowSettingManage.selectStaff') }}
                  </el-button>

                  <el-button type="text" @click="deleteStaff(item, subItem)">
                    {{ $t('workflowSettingManage.deleteStaff') }}
                  </el-button>
                </div>
              </div>
            </el-form-item>
          </el-form>

          <div class="form-actions">
            <el-button type="primary" @click="saveWorkflowSettingInfo">
              {{ $t('common.submit') }}
            </el-button>
            <el-button type="warning" @click="_goBack">
              {{ $t('common.back') }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workflowSettingManage.instructions') }}</span>
          </div>
          <div v-if="workflowSettingInfo.flowType === '70007'">
            <p>1. {{ $t('workflowSettingManage.instruction1') }}</p>
            <p>2. {{ $t('workflowSettingManage.instruction2') }}</p>
            <p>3. {{ $t('workflowSettingManage.instruction3') }}</p>
            <p>4. {{ $t('workflowSettingManage.instruction4') }}</p>
          </div>
          <!-- 其他流程类型的说明类似 -->
        </el-card>
      </el-col>
    </el-row>

    <select-staff ref="selectStaff"  @selectStaff="handleStaffSelected"/>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import {
  getWorkflowSteps,
  updateWorkflow
} from '@/api/system/workflowSettingManageApi'

export default {
  name: 'WorkflowSettingManageList',
  components: {
    SelectStaff
  },
  data() {
    return {
      communityId: '',
      workflowSettingInfo: {
        flowId: '',
        flowName: '',
        flowType: '',
        describle: '',
        startNodeFinish: 'Y',
        steps: []
      },
      currentSelectTarget: null
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initWorkflowSettingInfo()
  },
  methods: {
    generateId() {
      return `id-${Date.now()}-${Math.random().toString(36).substr(2, 6)}`
    },

    async _initWorkflowSettingInfo() {
      const flowId = this.$route.query.flowId

      if (!flowId) {
        this.$message.error(this.$t('workflowSettingManage.invalidOperation'))
        this.$router.go(-1)
        return
      }

      this.workflowSettingInfo.flowId = flowId
      this.workflowSettingInfo.flowName = this.$route.query.flowName
      this.workflowSettingInfo.flowType = this.$route.query.flowType
      this.workflowSettingInfo.startNodeFinish = this.$route.query.startNodeFinish || 'Y'

      try {
        const { data } = await getWorkflowSteps({
          communityId: this.communityId,
          flowId
        })
        this._freshResStep(data)
      } catch (error) {
        this.$message.error(error.message)
      }
    },

    _freshResStep(data) {
      if (!data) {
        this.workflowSettingInfo.steps = []
        return
      }

      this.workflowSettingInfo.describle = data.describle || ''
      if (data.startNodeFinish) {
        this.workflowSettingInfo.startNodeFinish = data.startNodeFinish
      }

      const workflowSteps = Array.isArray(data.workflowSteps) ? data.workflowSteps : []
      const steps = workflowSteps.map((step, index) => {
        const staffList = Array.isArray(step.workflowStepStaffs) ? step.workflowStepStaffs : []
        const mainStaff = staffList[0] || {}

        const subStaff = staffList.slice(1).map(staff => ({
          id: staff.id || this.generateId(),
          staffId: staff.staffId || staff.userId || '',
          staffName: staff.staffName || staff.userName || staff.name || '',
          staffRole: staff.staffRole || ''
        }))

        return {
          seq: index,
          staffId: mainStaff.staffId || mainStaff.userId || '',
          staffName: mainStaff.staffName || mainStaff.userName || mainStaff.name || '',
          staffRole: mainStaff.staffRole || '',
          type: String(step.type || '2'),
          subStaff
        }
      })

      this.workflowSettingInfo.steps = steps
    },

    addWorkflowStep() {
      const step = {
        seq: this.workflowSettingInfo.steps.length,
        staffId: '',
        staffName: '',
        type: '2',
        subStaff: []
      }
      this.workflowSettingInfo.steps.push(step)
    },

    chooseStaff(item) {
      item.from = this._getStaffFromType()
      this.currentSelectTarget = item
      this.$refs.selectStaff.open()
    },

    _getStaffFromType() {
      const type = this.workflowSettingInfo.flowType
      if (['30003', '40004', '70007', '80008'].includes(type)) {
        return 'purchase'
      }
      if (['50005', '60006'].includes(type)) {
        return 'contract'
      }
      return ''
    },

    _goBack() {
      this.$router.go(-1)
    },

    deleteStep(step) {
      this.workflowSettingInfo.steps = this.workflowSettingInfo.steps.filter(
        s => s.seq !== step.seq
      )
    },

    addStaff(step) {
      step.subStaff.push({
        id: this.generateId(),
        staffId: '',
        staffName: '',
        staffRole: '1001'
      })
    },

    deleteStaff(step, subStaff) {
      step.subStaff = step.subStaff.filter(s => s.id !== subStaff.id)
    },

    chooseType(item) {
      if (item.type === '1') {
        item.subStaff = []
      }
    },

    handleStaffSelected(staff) {
      if (!this.currentSelectTarget) {
        return
      }
      const target = this.currentSelectTarget
      this.$set(target, 'staffId', staff.staffId || staff.userId || '')
      this.$set(target, 'staffName', staff.staffName || staff.userName || staff.name || '')
      if (staff.staffRole) {
        this.$set(target, 'staffRole', staff.staffRole)
      }
      this.currentSelectTarget = null
    },

    async saveWorkflowSettingInfo() {
      try {
        await updateWorkflow({
          ...this.workflowSettingInfo,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this._goBack()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.workflow-setting-container {
  padding: 20px;

  .step-item {
    margin-bottom: 20px;
    padding: 15px;
    border: 1px solid #ebeef5;
    border-radius: 4px;

    .step-header {
      display: flex;
      align-items: center;
      margin-bottom: 10px;

      >* {
        margin-right: 15px;
      }

      .radio-group {
        display: inline-flex;
        align-items: center;
        margin-right: 15px;

        .radio-item {
          display: inline-flex;
          align-items: center;
          margin-right: 12px;

          input {
            margin-right: 4px;
          }
        }
      }
    }

    .sub-staff-item {
      margin-left: 40px;
      margin-bottom: 10px;

      >* {
        margin-right: 15px;
      }
    }
  }

  .form-actions {
    text-align: right;
    margin-top: 20px;
  }

  .mt-20 {
    margin-top: 20px;
  }
}
</style>