<template>
  <div class="purchase-apply-detail-container">
    <!-- 申请信息 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('purchaseApplyDetail.applyInfo') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_printPurchaseApply">
            <i class="el-icon-printer"></i>
            {{ $t('purchaseApplyDetail.print') }}
          </el-button>
          <el-button type="primary" size="small" @click="_callBackListPurchaseApply">
            <i class="el-icon-close"></i>
            {{ $t('purchaseApplyDetail.back') }}
          </el-button>
        </div>
      </div>
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.applyNo') }}</label>
              <div>{{ purchaseApplyDetailInfo.applyOrderId }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.applicant') }}</label>
              <div>{{ purchaseApplyDetailInfo.userName }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.endUser') }}</label>
              <div>{{ purchaseApplyDetailInfo.endUserName }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.contactPhone') }}</label>
              <div>{{ purchaseApplyDetailInfo.endUserTel }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.applyTime') }}</label>
              <div>{{ purchaseApplyDetailInfo.createTime }}</div>
            </div>
          </el-col>
          <el-col v-if="purchaseApplyDetailInfo.resOrderType == 10000 && purchaseApplyDetailInfo.warehousingWay == 20000"
            :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.referenceTotalPrice') }}</label>
              <div>￥{{ purchaseApplyDetailInfo.totalPrice }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.approvalStatus') }}</label>
              <div>{{ purchaseApplyDetailInfo.stateName }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ purchaseApplyDetailInfo.resOrderType == 10000 ? $t('purchaseApplyDetail.storageMethod') :
                $t('purchaseApplyDetail.outboundMethod') }}</label>
              <div>
                {{ purchaseApplyDetailInfo.resOrderType == 10000 ?
                  (purchaseApplyDetailInfo.warehousingWay == 10000 ? $t('purchaseApplyDetail.directStorage') :
                    purchaseApplyDetailInfo.warehousingWay == 20000 ? $t('purchaseApplyDetail.purchaseStorage') :
                      $t('purchaseApplyDetail.emergencyPurchase')) :
                  (purchaseApplyDetailInfo.warehousingWay == 10000 ? $t('purchaseApplyDetail.directOutbound') :
                    $t('purchaseApplyDetail.auditOutbound')) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.description') }}</label>
              <div>{{ purchaseApplyDetailInfo.description }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="purchaseApplyDetailInfo.resOrderType == 10000" :gutter="20">
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.referencePurchaseTotalPrice') }}</label>
              <div>{{ purchaseApplyDetailInfo.totalPrice > 0 ? '￥' + purchaseApplyDetailInfo.totalPrice : '-' }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-item">
              <label>{{ $t('purchaseApplyDetail.actualPurchaseTotalPrice') }}</label>
              <div>{{ purchaseApplyDetailInfo.purchaseTotalPrice > 0 ? '￥' + purchaseApplyDetailInfo.purchaseTotalPrice :
                '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 申请物资 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('purchaseApplyDetail.applyMaterials') }}</span>
      </div>
      <div class="card-content">
        <el-table :data="purchaseApplyDetailInfo.purchaseApplyDetailVo" border style="width: 100%">
          <el-table-column prop="parentRstName" :label="$t('purchaseApplyDetail.itemType')" align="center">
            <template slot-scope="scope">
              {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
            </template>
          </el-table-column>
          <el-table-column prop="resName" :label="$t('purchaseApplyDetail.itemName')" align="center"></el-table-column>
          <el-table-column prop="shName" :label="$t('purchaseApplyDetail.warehouse')" align="center"></el-table-column>
          <el-table-column prop="specName" :label="$t('purchaseApplyDetail.itemSpec')" align="center">
            <template slot-scope="scope">
              {{ scope.row.specName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="supplierName" :label="$t('purchaseApplyDetail.supplier')" align="center">
            <template slot-scope="scope">
              {{ scope.row.supplierName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="resCode" :label="$t('purchaseApplyDetail.itemCode')" align="center"></el-table-column>
          <el-table-column prop="isFixedName" :label="$t('purchaseApplyDetail.fixedItem')"
            align="center"></el-table-column>
          <el-table-column prop="consultPrice" :label="$t('purchaseApplyDetail.referencePrice')" align="center">
            <template slot-scope="scope">
              {{ scope.row.consultPrice ? '￥' + scope.row.consultPrice : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="originalStock" :label="$t('purchaseApplyDetail.originalStock')" align="center">
            <template slot-scope="scope">
              {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
            </template>
          </el-table-column>
          <el-table-column prop="stock" :label="$t('purchaseApplyDetail.currentStock')" align="center">
            <template slot-scope="scope">
              {{ scope.row.stock ? scope.row.stock : '0' }}{{ scope.row.unitCodeName }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" :label="$t('purchaseApplyDetail.applyQuantity')" align="center">
            <template slot-scope="scope">
              {{ scope.row.quantity }}{{ scope.row.unitCodeName }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" :label="$t('purchaseApplyDetail.applyRemark')" align="center"></el-table-column>
          <el-table-column v-if="purchaseApplyDetailInfo.resOrderType == 10000" prop="purchasePrice"
            :label="$t('purchaseApplyDetail.purchasePrice')" align="center">
            <template slot-scope="scope">
              {{ scope.row.purchasePrice ? '￥' + scope.row.purchasePrice : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="purchaseQuantity"
            :label="purchaseApplyDetailInfo.resOrderType == 10000 ? $t('purchaseApplyDetail.purchaseQuantity') : $t('purchaseApplyDetail.receiveQuantity')"
            align="center">
            <template slot-scope="scope">
              {{ scope.row.purchaseQuantity ? scope.row.purchaseQuantity + scope.row.unitCodeName : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="purchaseRemark"
            :label="purchaseApplyDetailInfo.resOrderType == 10000 ? $t('purchaseApplyDetail.purchaseRemark') : $t('purchaseApplyDetail.receiveRemark')"
            align="center">
            <template slot-scope="scope">
              {{ scope.row.purchaseRemark || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 工单流转 -->
    <el-card v-if="purchaseApplyDetailInfo.warehousingWay == 20000" class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('purchaseApplyDetail.workOrderFlow') }}</span>
      </div>
      <div class="card-content">
        <el-table :data="purchaseApplyDetailInfo.auditUsers" border style="width: 100%">
          <el-table-column type="index" :label="$t('purchaseApplyDetail.serialNumber')" align="center"
            width="80"></el-table-column>
          <el-table-column prop="userName" :label="$t('purchaseApplyDetail.handler')" align="center">
            <template slot-scope="scope">
              {{ scope.row.userName || scope.row.auditName }}
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('purchaseApplyDetail.status')" align="center"></el-table-column>
          <el-table-column prop="auditTime" :label="$t('purchaseApplyDetail.handleTime')"
            align="center"></el-table-column>
          <el-table-column prop="duration" :label="$t('purchaseApplyDetail.timeConsuming')"
            align="center"></el-table-column>
          <el-table-column prop="message" :label="$t('purchaseApplyDetail.opinion')" align="center"></el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 审核组件 -->
    <audit-div v-if="purchaseApplyDetailInfo.action == 'audit'" ref="auditDiv"></audit-div>
  </div>
</template>

<script>
import { listPurchaseApplys, listWorkflowAuditInfo } from '@/api/resource/purchaseApplyDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import AuditDiv from '@/components/resource/auditDiv'

export default {
  name: 'PurchaseApplyDetailList',
  components: {
    AuditDiv
  },
  data() {
    return {
      purchaseApplyDetailInfo: {
        resourceNames: '',
        state: '',
        totalPrice: '',
        purchaseTotalPrice: '',
        applyOrderId: '',
        description: '',
        createTime: '',
        userName: '',
        endUserName: '',
        endUserTel: '',
        stateName: '',
        resOrderType: '',
        purchaseApplyDetailVo: [],
        auditUsers: [],
        warehousingWay: '',
        action: '',
        taskId: '',
        flowId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.purchaseApplyDetailInfo.applyOrderId = this.$route.query.applyOrderId
    this.purchaseApplyDetailInfo.resOrderType = this.$route.query.resOrderType
    this.purchaseApplyDetailInfo.action = this.$route.query.action
    this.purchaseApplyDetailInfo.taskId = this.$route.query.taskId
    this.purchaseApplyDetailInfo.flowId = this.$route.query.flowId
    this._listPurchaseApply()
  },
  methods: {
    async _listPurchaseApply() {
      try {
        const params = {
          applyOrderId: this.purchaseApplyDetailInfo.applyOrderId,
          resOrderType: this.purchaseApplyDetailInfo.resOrderType,
          page: 1,
          row: 1,
          communityId: this.communityId
        }
        const res = await listPurchaseApplys(params)
        const purchaseApply = res.purchaseApplys[0]
        Object.assign(this.purchaseApplyDetailInfo, purchaseApply)

        if (this.purchaseApplyDetailInfo.warehousingWay == 20000) {
          this._loadAuditUser()
        }

        if (this.purchaseApplyDetailInfo.action == 'audit') {
          this.$refs.auditDiv.open({
            createUserId: purchaseApply.createUserId,
            action: this.purchaseApplyDetailInfo.action,
            taskId: this.purchaseApplyDetailInfo.taskId,
            flowId: this.purchaseApplyDetailInfo.flowId,
            url: '/purchaseApply.auditApplyOrder',
            id: purchaseApply.applyOrderId,
          })
        }
      } catch (error) {
        console.error('获取采购申请详情失败:', error)
      }
    },
    async _loadAuditUser() {
      try {
        const params = {
          businessKey: this.purchaseApplyDetailInfo.applyOrderId,
          communityId: this.communityId
        }
        const res = await listWorkflowAuditInfo(params)
        this.purchaseApplyDetailInfo.auditUsers = res.data
      } catch (error) {
        console.error('获取审核流程信息失败:', error)
      }
    },
    _callBackListPurchaseApply() {
      this.$router.go(-1)
    },
    _printPurchaseApply() {
      if (this.purchaseApplyDetailInfo.resOrderType == '10000') {
        window.open(`/#/pages/property/printPurchaseApply?applyOrderId=${this.purchaseApplyDetailInfo.applyOrderId}&resOrderType=${this.purchaseApplyDetailInfo.resOrderType}`)
      } else if (this.purchaseApplyDetailInfo.resOrderType == '20000') {
        window.open(`/#/pages/property/printPurchaseOutApply?applyOrderId=${this.purchaseApplyDetailInfo.applyOrderId}&resOrderType=${this.purchaseApplyDetailInfo.resOrderType}`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.purchase-apply-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-content {

      .form-item {
        margin-bottom: 15px;
        display: flex;
        justify-self: start;

        label {
          margin-bottom: 5px;
        }

        div {
          margin-left: 5px;
        }
      }
    }
  }

  .el-table {
    margin-top: 20px;
  }
}</style>