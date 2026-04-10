<template>
  <div class="new-oa-workflow-doing-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between  ">
        <span>{{ $t('newOaWorkflowDoing.myPending') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_queryUndoOrderMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('newOaWorkflowDoing.refresh') }}
          </el-button>
        </div>
      </div>
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="4" v-for="(item, index) in pendingItems" :key="'pending-' + index">
            <div class="item-wrapper text-center">
              <el-link type="primary" :underline="true" @click="_toGo(item.url)">
                <span>{{ $t(`newOaWorkflowDoing.${item.key}`) }}</span>
                <span :class="{ 'red-text': item.count > 0 }">({{ item.count }})</span>
              </el-link>
            </div>
          </el-col>
          <el-col :span="4" v-for="(item, index) in newOaWorkflowDoingInfo.newOaWorkflows" :key="'workflow-' + index">
            <div class="item-wrapper text-center">
              <el-link type="primary" :underline="true" @click="_toGoA(item)">
                {{ item.flowName }}
                <span>{{ $t('newOaWorkflowDoing.pending') }}</span>
                <span :class="{ 'red-text': item.undoCount > 0 }">({{ item.undoCount }})</span>
              </el-link>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowDoing.myCompleted') }}</span>
      </div>
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="4" v-for="(item, index) in completedItems" :key="'completed-' + index">
            <div class="item-wrapper text-center">
              <el-link type="primary" :underline="true" @click="_toGo(item.url)">
                <span>{{ $t(`newOaWorkflowDoing.${item.key}`) }}</span>
              </el-link>
            </div>
          </el-col>
          <el-col :span="4" v-for="(item, index) in newOaWorkflowDoingInfo.newOaWorkflows"
            :key="'workflow-completed-' + index">
            <div class="item-wrapper text-center">
              <el-link type="primary" :underline="true" @click="_toGoB(item)">
                {{ item.flowName }}
                <span>{{ $t('newOaWorkflowDoing.completed') }}</span>
              </el-link>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getUndoInfo, listNewOaWorkflows } from '@/api/oa/newOaWorkflowDoingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NewOaWorkflowDoingList',
  data() {
    return {
      newOaWorkflowDoingInfo: {
        newOaWorkflows: [],
        repair: 0,
        complaint: 0,
        purchase: 0,
        collection: 0,
        contractApply: 0,
        contractChange: 0,
        allocation: 0,
        itemReleaseCount: 0,
        visitUndoCount: 0,
        ownerSettledApplyCount: 0
      },
      pendingItems: [
        { key: 'repairPending', url: '/pages/property/repairDispatchManage', count: 0 },
        { key: 'complaintPending', url: '/pages/complaint/uodoComplaints', count: 0 },
        { key: 'purchasePending', url: '/pages/admin/myAuditOrders', count: 0 },
        { key: 'collectionPending', url: '/pages/admin/myItemOutAuditOrders', count: 0 },
        { key: 'contractApplyPending', url: '/pages/admin/contractApplyAuditOrders', count: 0 },
        { key: 'contractChangePending', url: '/pages/admin/contractChangeAuditOrders', count: 0 },
        { key: 'allocationPending', url: '/pages/admin/allocationStorehouseAuditOrders', count: 0 },
        { key: 'itemReleasePending', url: '/pages/property/itemReleaseUndo', count: 0 },
        { key: 'visitPending', url: '/pages/property/visitUndo', count: 0 }
      ],
      completedItems: [
        { key: 'repairCompleted', url: '/pages/property/myRepairDispatchManage' },
        { key: 'complaintCompleted', url: '/pages/complaint/doHistoryComplaints' },
        { key: 'purchaseCompleted', url: '/pages/admin/myAuditHistoryOrders' },
        { key: 'collectionCompleted', url: '/pages/admin/myItemOutAuditHistoryOrders' },
        { key: 'contractApplyCompleted', url: '/pages/admin/contractApplyAuditHistoryOrders' },
        { key: 'contractChangeCompleted', url: '/pages/admin/contractChangeAuditHistoryOrders' },
        { key: 'allocationCompleted', url: '/pages/admin/allocationStorehouseHistoryAuditOrders' },
        { key: 'itemReleaseCompleted', url: '/pages/property/itemReleaseFinish' },
        { key: 'visitCompleted', url: '/pages/property/visitFinish' }
      ],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initMethod()
  },
  methods: {
    _initMethod() {
      this._listNewOaWorkflows()
      this._loadUndoInfo()
    },
    _toGo(url) {
      this.$router.push(url)
    },
    _toGoA(item) {
      this.$router.push(`/views/oa/newOaWorkflow?flowId=${item.flowId}&switchValue=newOaWorkflowUndo`)
    },
    _toGoB(item) {
      this.$router.push(`/views/oa/newOaWorkflow?flowId=${item.flowId}&switchValue=newOaWorkflowFinish`)
    },
    async _listNewOaWorkflows() {
      try {
        const params = {
          state: 'C',
          page: 1,
          row: 100,
          flowType: '1001'
        }
        const { data } = await listNewOaWorkflows(params)
        this.newOaWorkflowDoingInfo.newOaWorkflows = data
      } catch (error) {
        console.error('Failed to fetch workflows:', error)
      }
    },
    async _loadUndoInfo() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 10
        }
        const {data} = await getUndoInfo(params)
        Object.assign(this.newOaWorkflowDoingInfo, data)

        // Update counts in pendingItems
        this.pendingItems[0].count = this.newOaWorkflowDoingInfo.repair
        this.pendingItems[1].count = this.newOaWorkflowDoingInfo.complaint
        this.pendingItems[2].count = this.newOaWorkflowDoingInfo.purchase
        this.pendingItems[3].count = this.newOaWorkflowDoingInfo.collection
        this.pendingItems[4].count = this.newOaWorkflowDoingInfo.contractApply
        this.pendingItems[5].count = this.newOaWorkflowDoingInfo.contractChange
        this.pendingItems[6].count = this.newOaWorkflowDoingInfo.allocation
        this.pendingItems[7].count = this.newOaWorkflowDoingInfo.itemReleaseCount
        this.pendingItems[8].count = this.newOaWorkflowDoingInfo.visitUndoCount
      } catch (error) {
        console.error('Failed to fetch undo info:', error)
      }
    },
    _queryUndoOrderMethod() {
      this._listNewOaWorkflows()
      this._loadUndoInfo()
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-doing-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .header-tools {
      float: right;
    }

    .card-content {
      padding: 10px;
    }
  }

  .item-wrapper {
    padding: 10px 0;

    .red-text {
      color: #F56C6C;
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .text-center {
    text-align: center;
  }
}
</style>