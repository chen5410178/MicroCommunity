<template>
  <div class="new-oa-workflow-detail-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('newOaWorkflowDetail.title') }}</span>
            <div class="float-right">
              <el-button type="primary" size="small" @click="_goBack">
                {{ $t('newOaWorkflowDetail.back') }}
              </el-button>
            </div>
          </div>

          <div v-if="newOaWorkflowDetailInfo.pools">
            <el-form>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item :label="$t('newOaWorkflowDetail.applicant')">
                    <span>{{ newOaWorkflowDetailInfo.pools.create_user_name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="$t('newOaWorkflowDetail.applyTime')">
                    <span>{{ newOaWorkflowDetailInfo.pools.create_time }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="$t('newOaWorkflowDetail.status')">
                    <span>{{ _getNewOaWorkflowDetailState(newOaWorkflowDetailInfo.pools) }}</span>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <template v-for="(item, index) in newOaWorkflowDetailInfo.formJson">
                  <el-col v-if="item.type != 'text' && item.type != 'button'" :key="index" :span="8">
                    <el-form-item :label="item.label">
                      <span>{{ newOaWorkflowDetailInfo.pools[item.key] }}</span>
                    </el-form-item>
                  </el-col>
                </template>
              </el-row>

              <el-row :gutter="20">
                <el-col v-for="(item, index) in newOaWorkflowDetailInfo.files" :key="index" :span="8">
                  <el-form-item>
                    <el-link :href="item.realFileName" target="_blank">{{ item.fileName }}</el-link>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('newOaWorkflowDetail.workflowProcess') }}</span>
          </div>

          <el-table :data="newOaWorkflowDetailInfo.comments" border style="width: 100%">
            <el-table-column prop="index" :label="$t('newOaWorkflowDetail.serialNumber')" width="80" align="center">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="orgName" :label="$t('newOaWorkflowDetail.department')" align="center">
              <template slot-scope="scope">
                {{ scope.row.orgName || $t('newOaWorkflowDetail.admin') }}
              </template>
            </el-table-column>
            <el-table-column prop="staffName" :label="$t('newOaWorkflowDetail.handler')" align="center" />
            <el-table-column :label="$t('newOaWorkflowDetail.status')" align="center">
              <template slot-scope="scope">
                {{ scope.row.endTime ? $t('newOaWorkflowDetail.processed') : $t('newOaWorkflowDetail.processing') }}
              </template>
            </el-table-column>
            <el-table-column prop="duration" :label="$t('newOaWorkflowDetail.timeConsuming')" align="center" />
            <el-table-column prop="context" :label="$t('newOaWorkflowDetail.comment')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row v-if="newOaWorkflowDetailInfo.action" :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('newOaWorkflowDetail.workflowHandle') }}</span>
          </div>

          <el-form label-width="120px" class="text-left">
            <el-form-item :label="$t('newOaWorkflowDetail.action')">
              <el-select v-model="newOaWorkflowDetailInfo.audit.auditCode" style="width:100%"
                :placeholder="$t('newOaWorkflowDetail.selectAction')">
                <el-option v-if="newOaWorkflowDetailInfo.nextAudit.next" value="1100"
                  :label="$t('newOaWorkflowDetail.handle')" />
                <el-option v-if="newOaWorkflowDetailInfo.nextAudit.back" value="1200"
                  :label="$t('newOaWorkflowDetail.return')" />
                <el-option v-if="newOaWorkflowDetailInfo.nextAudit.backIndex" value="1400"
                  :label="$t('newOaWorkflowDetail.returnToSubmitter')" />
                <el-option v-if="newOaWorkflowDetailInfo.nextAudit.exit" value="1500"
                  :label="$t('newOaWorkflowDetail.end')" />
                <el-option value="1300" :label="$t('newOaWorkflowDetail.transfer')" />
              </el-select>
            </el-form-item>

            <el-form-item :label="$t('newOaWorkflowDetail.workflowDescription')" class="margin-top">
              <el-input type="textarea" :rows="3" :placeholder="$t('newOaWorkflowDetail.descriptionRequired')"
                v-model="newOaWorkflowDetailInfo.audit.auditMessage" />
            </el-form-item>

            <!-- 办理时 判断是否指定 委托人 如果指定了 则不显示 -->
            <el-form-item
              v-if="newOaWorkflowDetailInfo.audit.auditCode == '1100' && newOaWorkflowDetailInfo.nextAudit.assignee == '-2'"
              :label="$t('newOaWorkflowDetail.nextHandler')" class="margin-top">
              <el-input v-model="newOaWorkflowDetailInfo.audit.staffName"
                :placeholder="$t('newOaWorkflowDetail.selectNextHandler')" disabled style="width:80%" />
              <el-button type="primary" @click="chooseStaff" style="margin-left:10px">
                {{ $t('newOaWorkflowDetail.select') }}
              </el-button>
            </el-form-item>

            <!-- 转单时 必须指定 委托人 -->
            <el-form-item v-if="newOaWorkflowDetailInfo.audit.auditCode == '1300'"
              :label="$t('newOaWorkflowDetail.nextHandler')" class="margin-top">
              <el-input v-model="newOaWorkflowDetailInfo.audit.staffName"
                :placeholder="$t('newOaWorkflowDetail.selectNextHandler')" disabled style="width:80%" />
              <el-button type="primary" @click="chooseStaff" style="margin-left:10px">
                {{ $t('newOaWorkflowDetail.select') }}
              </el-button>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" class="float-right" @click="_auditSubmit">
                {{ $t('newOaWorkflowDetail.submit') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('newOaWorkflowDetail.flowChart') }}</span>
          </div>
          <div class="text-center">
            <el-image :src="newOaWorkflowDetailInfo.imgData" style="height:300px" fit="contain" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <select-staff ref="selectStaff" @selectStaff="handleSelectStaff" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  queryOaWorkflowForm, queryOaWorkflowFormData, queryOaWorkflowUser,
  getNextTask, auditOaWorkflow, listRunWorkflowImage
} from '@/api/oa/newOaWorkflowDetailApi'
import SelectStaff from '@/components/staff/SelectStaff'

export default {
  name: 'NewOaWorkflowDetailList',
  components: {
    SelectStaff
  },
  data() {
    return {
      newOaWorkflowDetailInfo: {
        id: '',
        flowId: '',
        pools: {},
        formJson: [],
        comments: [],
        action: '',
        audit: {
          auditCode: '',
          auditMessage: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        imgData: '',
        nextAudit: {},
        files: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    const id = this.$route.query.id
    if (!id) {
      this.$message.error(this.$t('newOaWorkflowDetail.illegalOperation'))
      return
    }
    this.newOaWorkflowDetailInfo.id = id
    this.newOaWorkflowDetailInfo.flowId = this.$route.query.flowId
    this.newOaWorkflowDetailInfo.action = this.$route.query.action
    this.newOaWorkflowDetailInfo.audit.taskId = this.$route.query.taskId
    this._listOaWorkflowDetails()
    this._loadComments()
    if (this.newOaWorkflowDetailInfo.action) {
      this._loadNextAuditPerson()
    }
    this._openNewOaWorkflowDetailImg()
  },
  methods: {
    async _listOaWorkFlowDetailForm() {
      try {
        const params = {
          page: 1,
          row: 1,
          flowId: this.newOaWorkflowDetailInfo.flowId
        }
        const response = await queryOaWorkflowForm(params)
        this.newOaWorkflowDetailInfo.formJson = JSON.parse(response.data[0].formJson).components
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listOaWorkflowDetails() {
      try {
        const params = {
          page: 1,
          row: 1,
          id: this.newOaWorkflowDetailInfo.id,
          flowId: this.newOaWorkflowDetailInfo.flowId
        }
        const response = await queryOaWorkflowFormData(params)
        this.newOaWorkflowDetailInfo.pools = response.data[0]
        this.newOaWorkflowDetailInfo.files = response.data[0].files
        this._listOaWorkFlowDetailForm()
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _loadComments() {
      try {
        const params = {
          communityId: this.communityId,
          id: this.newOaWorkflowDetailInfo.id,
          flowId: this.newOaWorkflowDetailInfo.flowId,
          page: 1,
          row: 10
        }
        const response = await queryOaWorkflowUser(params)
        if (response.code !== 0) return
        this.newOaWorkflowDetailInfo.comments = response.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.newOaWorkflowDetailInfo.audit)
    },
    handleSelectStaff(staff) {
      this.newOaWorkflowDetailInfo.audit.staffId = staff.userId
      this.newOaWorkflowDetailInfo.audit.staffName = staff.userName
    },
    async _auditSubmit() {
      const audit = this.newOaWorkflowDetailInfo.audit
      audit.flowId = this.newOaWorkflowDetailInfo.flowId
      audit.id = this.newOaWorkflowDetailInfo.id

      if (this.newOaWorkflowDetailInfo.nextAudit.assignee != '-2') {
        audit.staffId = this.newOaWorkflowDetailInfo.nextAudit.assignee;
      }

      if (!audit.auditCode) {
        this.$message.error(this.$t('newOaWorkflowDetail.selectStatus'))
        return
      }
      if (!audit.auditMessage) {
        this.$message.error(this.$t('newOaWorkflowDetail.fillDescription'))
        return
      }
      if (audit.auditCode !== '1200' && audit.auditCode !== '1400' && !audit.staffId) {
        this.$message.error(this.$t('newOaWorkflowDetail.selectNextHandler'))
        return
      }

      try {
        const response = await auditOaWorkflow(audit)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error(error)
      }
    },
    async _loadNextAuditPerson() {
      try {
        const params = {
          communityId: this.communityId,
          taskId: this.newOaWorkflowDetailInfo.audit.taskId,
          flowId: this.newOaWorkflowDetailInfo.flowId,
          id: this.newOaWorkflowDetailInfo.id
        }
        const response = await getNextTask(params)
        if (response.code != 0) return
        this.newOaWorkflowDetailInfo.nextAudit = response.data[0]
        console.log(this.newOaWorkflowDetailInfo.nextAudit)
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _getNewOaWorkflowDetailState(pool) {
      if (!Object.prototype.hasOwnProperty.call(pool, 'state')) {
        return this.$t('newOaWorkflowDetail.unknown')
      }
      switch (pool.state) {
        case '1001':
          return this.$t('newOaWorkflowDetail.apply')
        case '1002':
          return this.$t('newOaWorkflowDetail.pendingReview')
        case '1003':
          return this.$t('newOaWorkflowDetail.returned')
        case '1004':
          return this.$t('newOaWorkflowDetail.transferred')
        case '1005':
          return this.$t('newOaWorkflowDetail.completed')
        default:
          return this.$t('newOaWorkflowDetail.unknown')
      }
    },
    async _openNewOaWorkflowDetailImg() {
      try {
        const params = {
          communityId: this.communityId,
          businessKey: this.newOaWorkflowDetailInfo.id
        }
        const response = await listRunWorkflowImage(params)
        if (response.code !== 0) return
        this.newOaWorkflowDetailInfo.imgData = 'data:image/png;base64,' + response.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-detail-container {
  padding: 20px;

  .mt-20 {
    margin-top: 20px;
  }

  .float-right {
    float: right;
  }

  .text-center {
    text-align: center;
  }

  .el-form-item {
    margin-bottom: 0;
  }
}
</style>