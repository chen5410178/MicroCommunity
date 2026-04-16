<template>
  <div class="item-release-detail-container">
    <!-- 放行详情 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseDetail.releaseDetails') }}</span>
            <div class="card-header-right">
              <el-button type="primary" size="small" @click="_goBack">{{ $t('itemReleaseDetail.back') }}</el-button>
            </div>
          </div>
          <div v-if="itemReleaseDetailInfo.pools">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.applyCompany') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.applyCompany }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.passType') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.typeName }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.applicant') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.applyPerson }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.idCard') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.idCard }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.phone') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.applyTel }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.applyTime') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.createTime }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.status') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.stateName }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.licensePlate') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.carNum }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.passTime') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.passTime }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="form-label">{{ $t('itemReleaseDetail.totalQuantity') }}</label>
                  <div class="form-content">{{ itemReleaseDetailInfo.pools.amount }}</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 放行物品 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseDetail.releaseItems') }}</span>
          </div>
          <el-table :data="itemReleaseDetailInfo.resNames" border style="width: 100%">
            <el-table-column prop="resName" :label="$t('itemReleaseDetail.itemName')" align="center" />
            <el-table-column prop="amount" :label="$t('itemReleaseDetail.quantity')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 工单流转 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseDetail.workflowProcess') }}</span>
          </div>
          <el-table :data="itemReleaseDetailInfo.comments" border style="width: 100%">
            <el-table-column type="index" :label="$t('itemReleaseDetail.serialNumber')" align="center" width="80" />
            <el-table-column prop="orgName" :label="$t('itemReleaseDetail.department')" align="center">
              <template slot-scope="scope">
                {{ scope.row.orgName || $t('itemReleaseDetail.administrator') }}
              </template>
            </el-table-column>
            <el-table-column prop="staffName" :label="$t('itemReleaseDetail.handler')" align="center" />
            <el-table-column :label="$t('itemReleaseDetail.status')" align="center">
              <template slot-scope="scope">
                {{ scope.row.endTime ? $t('itemReleaseDetail.processed') : $t('itemReleaseDetail.processing') }}
              </template>
            </el-table-column>
            <el-table-column prop="duration" :label="$t('itemReleaseDetail.timeConsumed')" align="center" />
            <el-table-column prop="context" :label="$t('itemReleaseDetail.comment')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 工单办理 -->
    <el-row v-if="itemReleaseDetailInfo.action">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseDetail.workOrderProcessing') }}</span>
          </div>
          <el-form label-width="120px">
            <el-form-item :label="$t('itemReleaseDetail.action')">
              <el-select v-model="itemReleaseDetailInfo.audit.auditCode" style="width:100%">
                <el-option :label="$t('itemReleaseDetail.pleaseSelect')" value="" disabled />
                <el-option v-if="itemReleaseDetailInfo.nextAudit.next || itemReleaseDetailInfo.nextAudit.exit"
                  value="1100" :label="$t('itemReleaseDetail.agree')" />
                <el-option v-if="itemReleaseDetailInfo.nextAudit.back" value="1200"
                  :label="$t('itemReleaseDetail.return')" />
                <el-option v-if="itemReleaseDetailInfo.nextAudit.backIndex" value="1400"
                  :label="$t('itemReleaseDetail.returnToSubmitter')" />
                <el-option value="1300" :label="$t('itemReleaseDetail.transfer')" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('itemReleaseDetail.workOrderDescription')">
              <el-input type="textarea" :placeholder="$t('itemReleaseDetail.requiredDescription')"
                v-model="itemReleaseDetailInfo.audit.auditMessage" />
            </el-form-item>
            <!-- 下一处理人 -->
            <el-form-item v-if="(itemReleaseDetailInfo.audit.auditCode == '1100' && itemReleaseDetailInfo.nextAudit.assignee == '-2') ||
              itemReleaseDetailInfo.audit.auditCode == '1300'" :label="$t('itemReleaseDetail.nextHandler')">
              <el-input v-model="itemReleaseDetailInfo.audit.staffName"
                :placeholder="$t('itemReleaseDetail.requiredNextHandler')" disabled style="width: calc(100% - 120px)" />
              <el-button style="width: 120px" @click="chooseStaff">{{ $t('itemReleaseDetail.select') }}</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="_auditSubmit">{{ $t('itemReleaseDetail.submit') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 流程图 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemReleaseDetail.flowChart') }}</span>
          </div>
          <div class="text-center">
            <img :src="itemReleaseDetailInfo.imgData" alt="" style="height:300px">
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 选择员工组件 -->
    <select-staff ref="selectStaff" @selectStaff="handleStaffSelect" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import {
  listItemReleaseRes,
  listItemRelease,
  auditUndoItemRelease,
  queryOaWorkflowUser,
  listRunWorkflowImage,
  queryNextDealUser
} from '@/api/work/itemReleaseDetailApi'

export default {
  name: 'ItemReleaseDetailList',
  components: {
    SelectStaff
  },
  data() {
    return {
      communityId: '',
      itemReleaseDetailInfo: {
        irId: '',
        flowId: '',
        pools: {},
        resNames: [],
        comments: [],
        action: '',
        audit: {
          auditCode: '1100',
          auditMessage: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        imgData: '',
        nextAudit: {},
        files: []
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    initData() {
      const irId = this.$route.query.irId
      if (!irId) {
        this.$message.error(this.$t('itemReleaseDetail.illegalOperation'))
        return
      }
      this.itemReleaseDetailInfo.irId = irId
      this.itemReleaseDetailInfo.flowId = this.$route.query.flowId
      this.itemReleaseDetailInfo.action = this.$route.query.action
      this.itemReleaseDetailInfo.audit.taskId = this.$route.query.taskId

      this._listOaWorkflowDetails()
      this._loadItemReleaseRes()
      this._loadComments()
      this._openNewOaWorkflowDetailImg()
    },
    async _loadItemReleaseRes() {
      try {
        const { data } = await listItemReleaseRes({
          page: 1,
          row: 500,
          communityId: this.communityId,
          irId: this.itemReleaseDetailInfo.irId
        })
        this.itemReleaseDetailInfo.resNames = data
      } catch (error) {
        console.error('获取放行物品失败:', error)
      }
    },
    async _listOaWorkflowDetails() {
      try {
        const { data } = await listItemRelease({
          page: 1,
          row: 1,
          irId: this.itemReleaseDetailInfo.irId,
          flowId: this.itemReleaseDetailInfo.flowId,
          communityId: this.communityId
        })
        this.itemReleaseDetailInfo.pools = data[0]
        if (this.itemReleaseDetailInfo.action) {
          this._loadNextAuditPerson()
        }
      } catch (error) {
        console.error('获取放行详情失败:', error)
      }
    },
   async _loadComments() {
      // TODO: 需要实现工单评论接口
      console.log('加载评论')
      const { data } = await queryOaWorkflowUser({
        page: 1,
        row: 50,
        flowId: this.itemReleaseDetailInfo.flowId,
        id: this.itemReleaseDetailInfo.irId,
        communityId: this.communityId
      })
      this.itemReleaseDetailInfo.comments = data
    },
    _goBack() {
      this.$router.go(-1)
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.itemReleaseDetailInfo.audit)
    },
    async _auditSubmit() {
      const _audit = this.itemReleaseDetailInfo.audit
      _audit.flowId = this.itemReleaseDetailInfo.flowId
      _audit.irId = this.itemReleaseDetailInfo.irId

      if (this.itemReleaseDetailInfo.nextAudit.assignee != '-2' && this.itemReleaseDetailInfo.nextAudit.assignee != '-1') {
        _audit.staffId = this.itemReleaseDetailInfo.nextAudit.assignee
      }

      if (!_audit.auditCode) {
        this.$message.error(this.$t('itemReleaseDetail.selectStatus'))
        return
      }
      if (!_audit.auditMessage) {
        this.$message.error(this.$t('itemReleaseDetail.fillDescription'))
        return
      }
      if (_audit.auditCode != '1200' && _audit.auditCode != '1400' && !_audit.staffId) {
        this.$message.error(this.$t('itemReleaseDetail.selectNextHandler'))
        return
      }

      try {
        await auditUndoItemRelease(_audit)
        this.$message.success(this.$t('common.operationSuccess'))
        this._goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('itemReleaseDetail.submitFailed'))
      }
    },
    async _loadNextAuditPerson() {
      // TODO: 需要实现获取下一处理人接口
      console.log('加载下一处理人')
      const { data } = await queryNextDealUser({
        startUserId: this.itemReleaseDetailInfo.pools.createUserId,
        taskId: this.itemReleaseDetailInfo.audit.taskId,
        communityId: this.communityId
      })
      this.itemReleaseDetailInfo.nextAudit = data[0]
    },
    async _openNewOaWorkflowDetailImg() {
      const { data } = await listRunWorkflowImage({
        businessKey: this.itemReleaseDetailInfo.irId,
        communityId: this.communityId
      })
      this.itemReleaseDetailInfo.imgData = 'data:image/png;base64,' + data
    },
    handleStaffSelect(staff) {
      this.itemReleaseDetailInfo.audit.staffId = staff.staffId
      this.itemReleaseDetailInfo.audit.staffName = staff.staffName
    }
  }
}
</script>

<style lang="scss" scoped>
.item-release-detail-container {
  padding: 20px;

  .el-row {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .form-group {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-start;

    .form-label {
      margin-bottom: 5px;
      color: #606266;
    }

    .form-content {
    }
  }

  .card-header-right {
    float: right;
  }

  .text-center {
    text-align: center;
  }
}
</style>