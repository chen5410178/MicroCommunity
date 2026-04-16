<template>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header" class="clearfix">
          <h5>{{ $t('flowAudit.auditInfo') }}</h5>
        </div>
        <div class="ibox-content">
          <el-form label-width="120px">
            <el-form-item :label="$t('flowAudit.auditStatus')">
              <el-select v-model="flowAuditInfo.state" :placeholder="$t('flowAudit.pleaseAudit')" style="width:100%">
                <el-option :label="$t('flowAudit.agree')" value="1100" />
                <el-option :label="$t('flowAudit.reject')" value="1200" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('flowAudit.reason')">
              <el-input v-model="flowAuditInfo.remark" type="textarea" :placeholder="$t('flowAudit.requiredReason')"
                :rows="4" />
            </el-form-item>
            <el-form-item v-if="flowAuditInfo.state === '1100' && flowAuditInfo.assignee === '-2'"
              :label="$t('flowAudit.staff')">
              <el-col :span="18">
                <el-input v-model="flowAuditInfo.staffName" :placeholder="$t('flowAudit.selectNextHandler')" disabled />
              </el-col>
              <el-col :span="6">
                <el-button @click="chooseStaff">
                  {{ $t('flowAudit.select') }}
                </el-button>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="float-right" @click="_flowAuditSubmit">
                <i class="el-icon-check" /> {{ $t('common.submit') }}
              </el-button>
              <el-button type="warning" class="float-right" style="margin-right:20px;" @click="_goBack">
                <i class="el-icon-close" /> {{ $t('common.cancel') }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-col>
    <select-staff ref="selectStaff" />
  </el-row>
</template>

<script>
import { listWorkflowNextNode } from '@/api/contract/contractApplyAuditOrdersApi'
import SelectStaff from '@/components/staff/SelectStaff'

export default {
  name: 'FlowAudit',
  components: {
    SelectStaff
  },
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      flowAuditInfo: {
        state: '',
        remark: '',
        taskId: '',
        startUserId: '',
        assignee: '',
        staffId: '',
        staffName: ''
      }
    }
  },
  watch: {
    'flowAuditInfo.state': {
      handler(val) {
        if (val && this.flowAuditInfo.state === '1100') {
          this.flowAuditInfo.remark = this.$t('flowAudit.agree')
          this._listWorkflow()
        } else {
          this.flowAuditInfo.remark = ''
        }
      },
      deep: true
    }
  },
  methods: {
    open(auditOrder) {
      Object.assign(this.flowAuditInfo, auditOrder)
      this.flowAuditInfo.state = ''
    },
    flowAuditValidate() {
      if (!this.flowAuditInfo.state) {
        this.$message.error(this.$t('flowAudit.auditStatusRequired'))
        return false
      }
      if (!this.flowAuditInfo.remark) {
        this.$message.error(this.$t('flowAudit.reasonRequired'))
        return false
      }
      if (this.flowAuditInfo.remark.length > 200) {
        this.$message.error(this.$t('flowAudit.reasonMaxLength'))
        return false
      }
      return true
    },
    async _flowAuditSubmit() {
      if (!this.flowAuditValidate()) return

      if (this.flowAuditInfo.assignee === '-2' && !this.flowAuditInfo.staffId) {
        this.$message.error(this.$t('flowAudit.selectNextHandler'))
        return
      }

      if (this.callBackListener) {
        const _flowAuditInfo = {
          state: this.flowAuditInfo.state,
          remark: this.flowAuditInfo.state === '1200'
            ? `${this.$t('flowAudit.reject')}:${this.flowAuditInfo.remark}`
            : this.flowAuditInfo.remark,
          nextUserId: this.flowAuditInfo.staffId
        }
        this.$emit(this.callBackFunction, _flowAuditInfo)
        this.clearAddBasePrivilegeInfo()
      }
    },
    clearAddBasePrivilegeInfo() {
      this.flowAuditInfo = {
        state: '',
        remark: '',
        taskId: '',
        startUserId: '',
        assignee: '',
        staffId: '',
        staffName: ''
      }
    },
    async _listWorkflow() {
      if (this.flowAuditInfo.state !== '1100') return

      try {
        const params = {
          taskId: this.flowAuditInfo.taskId,
          startUserId: this.flowAuditInfo.startUserId
        }
        const { data } = await listWorkflowNextNode(params)
        if (data && data.length > 0) {
          this.flowAuditInfo.assignee = data[0].assignee
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.flowAuditInfo)
    },
    _goBack() {
      this.$emit(this.callBackListener, 'list', {})
    }
  }
}
</script>

<style scoped>
.float-right {
  float: right;
}
</style>