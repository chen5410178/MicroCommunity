<template>
  <div class="allocation-storehouse-detail-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseDetail.applyInfo') }}</span>
            <div class="card-header-actions">
              <el-button type="primary" size="small" @click="printAllocationStorehouse">
                <i class="el-icon-printer"></i>
                {{ $t('allocationStorehouseDetail.print') }}
              </el-button>
              <el-button type="primary" size="small" @click="callBackListPurchaseApply">
                <i class="el-icon-close"></i>
                {{ $t('common.back') }}
              </el-button>
            </div>
          </div>
          <el-form :model="allocationStorehouseDetailInfo" label-width="120px" class="text-left">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('allocationStorehouseDetail.applicant')">
                  <span>{{ allocationStorehouseDetailInfo.startUserName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('allocationStorehouseDetail.applyTime')">
                  <span>{{ allocationStorehouseDetailInfo.createTime }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('allocationStorehouseDetail.type')">
                  <span>{{ allocationStorehouseDetailInfo.applyTypeName }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('allocationStorehouseDetail.status')">
                  <span>{{ allocationStorehouseDetailInfo.stateName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item :label="$t('allocationStorehouseDetail.applyRemark')">
                  <span>{{ allocationStorehouseDetailInfo.remark }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseDetail.allocationGoods') }}</span>
          </div>
          <el-table :data="allocationStorehouseDetailInfo.resourceStores" border style="width: 100%">
            <el-table-column prop="resId" :label="$t('allocationStorehouseDetail.goodsId')" align="center" />
            <el-table-column :label="$t('allocationStorehouseDetail.goodsType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
              </template>
            </el-table-column>
            <el-table-column prop="resName" :label="$t('allocationStorehouseDetail.goodsName')" align="center" />
            <el-table-column :label="$t('allocationStorehouseDetail.goodsSpec')" align="center">
              <template slot-scope="scope">
                {{ scope.row.specName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="resCode" :label="$t('allocationStorehouseDetail.goodsCode')" align="center" />
            <el-table-column prop="isFixedName" :label="$t('allocationStorehouseDetail.fixedGoods')" align="center" />
            <el-table-column
              :label="allocationStorehouseDetailInfo.applyType === '10000' || allocationStorehouseDetailInfo.applyType === '30000' ? $t('allocationStorehouseDetail.transferredWarehouse') : $t('allocationStorehouseDetail.returnPerson')"
              align="center">
              <template slot-scope="scope">
                {{ allocationStorehouseDetailInfo.applyType === '10000' || allocationStorehouseDetailInfo.applyType ===
                  '30000' ? scope.row.shaName : allocationStorehouseDetailInfo.startUserName }}
              </template>
            </el-table-column>
            <el-table-column prop="shzName" :label="$t('allocationStorehouseDetail.targetWarehouse')" align="center" />
            <el-table-column
              :label="$t('allocationStorehouseDetail.originalStock') + '(' + (allocationStorehouseDetailInfo.applyType === '30000' ? $t('allocationStorehouseDetail.targetWarehouse') : $t('allocationStorehouseDetail.transferredWarehouse')) + ')'"
              align="center">
              <template slot-scope="scope">
                {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('allocationStorehouseDetail.allocationQuantity')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stock }}{{ scope.row.applyType === '20000' ? scope.row.miniUnitCodeName :
                  scope.row.unitCodeName }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20"
      v-if="allocationStorehouseDetailInfo.applyType === '10000' && allocationStorehouseDetailInfo.auditUsersCount > 0">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseDetail.workflow') }}</span>
          </div>
          <el-table :data="allocationStorehouseDetailInfo.auditUsers" border style="width: 100%">
            <el-table-column :label="$t('allocationStorehouseDetail.serialNumber')" align="center">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('allocationStorehouseDetail.handler')" align="center">
              <template slot-scope="scope">
                {{ scope.row.userName || scope.row.auditName }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('allocationStorehouseDetail.status')" align="center" />
            <el-table-column prop="auditTime" :label="$t('allocationStorehouseDetail.processTime')" align="center" />
            <el-table-column prop="duration" :label="$t('allocationStorehouseDetail.timeConsuming')" align="center" />
            <el-table-column prop="message" :label="$t('allocationStorehouseDetail.opinion')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <div class="mt-20">
      <allocation-audit-div v-if="allocationStorehouseDetailInfo.action === 'audit'" ref="auditDiv" />
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  listAllocationStorehouseApplys,
  listAllocationStorehouses,
  listWorkflowAuditInfo
  // auditAllocationStoreOrder
} from '@/api/resource/allocationStorehouseDetailApi'
import AllocationAuditDiv from '@/components/resource/allocationAuditDiv'

export default {
  name: 'AllocationStorehouseDetailList',
  components: {
    AllocationAuditDiv
  },
  data() {
    return {
      communityId: '',
      allocationStorehouseDetailInfo: {
        applyId: '',
        resourceStores: [],
        remark: '',
        startUserName: '',
        createTime: '',
        auditUsers: [],
        auditUsersCount: 0,
        stateName: '',
        applyType: '',
        applyTypeName: '',
        action: '',
        taskId: '',
        flowId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.allocationStorehouseDetailInfo.applyId = this.$route.query.applyId
    this.allocationStorehouseDetailInfo.applyType = this.$route.query.applyType
    this.allocationStorehouseDetailInfo.applyTypeName = this.$route.query.applyTypeName
    this.allocationStorehouseDetailInfo.action = this.$route.query.action
    this.allocationStorehouseDetailInfo.taskId = this.$route.query.taskId
    this.allocationStorehouseDetailInfo.flowId = this.$route.query.flowId
    this.listAllocationStorehouseApply()
    this.listPurchaseApply(1, 100)
  },
  methods: {
    async listPurchaseApply(page, rows) {
      try {
        const params = {
          page,
          row: rows,
          applyId: this.allocationStorehouseDetailInfo.applyId
        }
        const { data } = await listAllocationStorehouses(params)
        this.allocationStorehouseDetailInfo.resourceStores = data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async listAllocationStorehouseApply() {
      try {
        const params = {
          page: 1,
          row: 1,
          applyId: this.allocationStorehouseDetailInfo.applyId
        }
        const { data } = await listAllocationStorehouseApplys(params)
        const purchaseApply = data[0]
        Object.assign(this.allocationStorehouseDetailInfo, purchaseApply)

        if (this.allocationStorehouseDetailInfo.applyType === '10000') {
          this.loadAuditUser()
        }

        if (this.allocationStorehouseDetailInfo.action === 'audit') {
          this.$refs.auditDiv.open({
            createUserId: purchaseApply.startUserId,
            action: this.allocationStorehouseDetailInfo.action,
            taskId: this.allocationStorehouseDetailInfo.taskId,
            flowId: this.allocationStorehouseDetailInfo.flowId,
            url: '/resourceStore.auditAllocationStoreOrder',
            id: purchaseApply.applyId,
          })
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async loadAuditUser() {
      try {
        const params = {
          businessKey: this.allocationStorehouseDetailInfo.applyId,
          communityId: this.communityId,
        }
        const { data } = await listWorkflowAuditInfo(params)
        this.allocationStorehouseDetailInfo.auditUsers = data
        this.allocationStorehouseDetailInfo.auditUsersCount = data.length
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    callBackListPurchaseApply() {
      this.$router.go(-1)
    },
    printAllocationStorehouse() {
      window.open(`/#/pages/property/printAllocationStorehouse?applyId=${this.allocationStorehouseDetailInfo.applyId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-storehouse-detail-container {
  padding: 20px;

  .mt-20 {
    margin-top: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .el-form-item {
    margin-bottom: 0;

    span {
      display: inline-block;
      padding: 0 10px;
    }
  }
}
</style>