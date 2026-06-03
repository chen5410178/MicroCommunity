<template>
  <div class="purchase-apply-manage-container">

    <div class="wrapper wrapper-content animated fadeInRight ecommerce">
      <el-row :gutter="20">
        <el-col :span="4" class="left-panel">
          <el-card class="state-card">
            <div class="state-list-wrapper">
              <div v-for="(item, index) in purchaseApplyManageInfo.states" :key="index" @click="swatchState(item)"
                :class="{ 'selected': purchaseApplyManageInfo.conditions.state === item.statusCd }"
                class="state-list-item">
                {{ item.name }}
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="20" class="right-panel">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('purchaseApplyManage.search.title') }}</span>
            </div>
            <el-row :gutter="20">
              <el-col :span="4">
                <el-input :placeholder="$t('purchaseApplyManage.search.applyOrderId')"
                  v-model.trim="purchaseApplyManageInfo.conditions.applyOrderId" clearable />
              </el-col>
              <el-col :span="4">
                <el-input :placeholder="$t('purchaseApplyManage.search.name')"
                  v-model.trim="purchaseApplyManageInfo.conditions.name" clearable />
              </el-col>
              <el-col :span="4">
                <el-input :placeholder="$t('purchaseApplyManage.search.resName')"
                  v-model.trim="purchaseApplyManageInfo.conditions.resName" clearable />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="purchaseApplyManageInfo.conditions.startTime" type="datetime"
                  :placeholder="$t('purchaseApplyManage.search.startTime')" style="width: 100%" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="purchaseApplyManageInfo.conditions.endTime" type="datetime"
                  :placeholder="$t('purchaseApplyManage.search.endTime')" style="width: 100%" />
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="_queryPurchaseApplyMethod()" icon="el-icon-search">
                  {{ $t('common.search') }}
                </el-button>
                <el-button type="default" @click="_resetInspectionPlanMethod()" icon="el-icon-refresh">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </el-card>

          <el-card class="margin-top">
            <div slot="header" class="flex justify-between">
              <div>{{ $t('purchaseApplyManage.list.title') }}</div>
              <div class="ibox-tools" style="top: 10px">
                <el-button type="primary" size="small" @click="_openAddPurchaseApplyModal()" icon="el-icon-plus">
                  {{ $t('purchaseApplyManage.button.purchase') }}
                </el-button>
                <el-button type="primary" size="small" v-if="hasPrivilege('502021071043650029')"
                  @click="_openUrgentPurchaseApplyModal()" icon="el-icon-plus">
                  {{ $t('purchaseApplyManage.button.urgentPurchase') }}
                </el-button>
                <el-button type="primary" size="small" @click="_exportExcel()" icon="el-icon-download">
                  {{ $t('common.export') }}
                </el-button>
              </div>
            </div>
            <el-table :data="purchaseApplyManageInfo.purchaseApplys" border style="width: 100%" v-loading="loading">
              <el-table-column prop="applyOrderId" :label="$t('purchaseApplyManage.table.applyOrderId')"
                align="center" />
              <el-table-column prop="userName" :label="$t('purchaseApplyManage.table.userName')" align="center" />
              <el-table-column prop="endUserName" :label="$t('purchaseApplyManage.table.endUserName')" align="center" />
              <el-table-column prop="createUserName" :label="$t('purchaseApplyManage.table.createUserName')"
                align="center" />
              <el-table-column prop="resourceNames" :label="$t('purchaseApplyManage.table.resourceNames')"
                align="center" />
              <el-table-column prop="createTime" :label="$t('purchaseApplyManage.table.createTime')" align="center" />
              <el-table-column :label="$t('purchaseApplyManage.table.warehousingWay')" align="center">
                <template slot-scope="scope">
                  {{
                    scope.row.warehousingWay == 10000
                      ? $t('purchaseApplyManage.warehousingWay.direct')
                      : scope.row.warehousingWay == 20000
                        ? $t('purchaseApplyManage.warehousingWay.purchase')
                        : $t('purchaseApplyManage.warehousingWay.urgent')
                  }}
                </template>
              </el-table-column>
              <el-table-column prop="stateName" :label="$t('purchaseApplyManage.table.stateName')" align="center" />
              <el-table-column :label="$t('common.operation')" align="center" width="300">
                <template slot-scope="scope">
                  <el-button size="mini" @click="_openDetailPurchaseApplyModel(scope.row)">
                    {{ $t('common.view') }}
                  </el-button>
                  <el-button size="mini" v-if="scope.row.warehousingWay != 10000 &&
                    scope.row.warehousingWay != 30000
                  " @click="_openRunWorkflowImage(scope.row)">
                    {{ $t('purchaseApplyManage.button.flowChart') }}
                  </el-button>
                  <el-button size="mini" type="danger" v-if="scope.row.state == '1000' &&
                    purchaseApplyManageInfo.currentUserId ==
                    scope.row.createUserId
                  " @click="_openDeletePurchaseApplyModel(scope.row)">
                    {{ $t('purchaseApplyManage.button.cancelApply') }}
                  </el-button>
                  <el-button size="mini" type="danger" v-else-if="scope.row.state == '1000' &&
                    hasPrivilege('502022032383620003')
                  " @click="_openDeletePurchaseApplyModel(scope.row)">
                    {{ $t('purchaseApplyManage.button.cancelApply') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination class="margin-top" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
              :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange" @current-change="handleCurrentChange" />
          </el-card>
        </el-col>
      </el-row>
    </div>

    <view-image ref="viewImage" />
    <edit-purchase-apply ref="editPurchaseApply" />
    <delete-purchase-apply ref="deletePurchaseApply" />
  </div>
</template>

<script>
import { listPurchaseApplys, exportData, getWorkflowImage } from '@/api/resource/purchaseApplyManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'
import EditPurchaseApply from '@/components/resource/editPurchaseApply'
import DeletePurchaseApply from '@/components/resource/deletePurchaseApply'

export default {
  name: 'PurchaseApplyManageList',
  components: {
    ViewImage,
    EditPurchaseApply,
    DeletePurchaseApply
  },
  data() {
    return {
      loading: false,
      purchaseApplyManageInfo: {
        purchaseApplys: [],
        total: 0,
        records: 1,
        moreCondition: false,
        states: [],
        currentUserId: '',
        conditions: {
          state: '',
          applyOrderId: '',
          userName: '',
          name: '',
          resOrderType: '10000',
          resName: '',
          startTime: '',
          endTime: '',
          userId: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.purchaseApplyManageInfo.currentUserId = ''
    this.purchaseApplyManageInfo.conditions.userId = ''
    this.purchaseApplyManageInfo.conditions.communityId = getCommunityId()
    this._listPurchaseApplys(this.page.current, this.page.size)
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('purchase_apply', 'state')
        this.purchaseApplyManageInfo.states = [
          {
            statusCd: '',
            name: this.$t('common.all')
          },
          ...data
        ]
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listPurchaseApplys(page, size) {
      this.loading = true
      try {
        const params = {
          ...this.purchaseApplyManageInfo.conditions,
          page,
          row: size
        }
        const { purchaseApplys, total } = await listPurchaseApplys(params)
        this.purchaseApplyManageInfo.purchaseApplys = purchaseApplys
        this.page.total = total
      } catch (error) {
        console.error('获取采购申请列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    _openAddPurchaseApplyModal() {
      this.$router.push({
        path: '/views/resource/addPurchaseApply',
        query: {
          resOrderType: this.purchaseApplyManageInfo.conditions.resOrderType
        }
      })
    },
    _openUrgentPurchaseApplyModal() {
      this.$router.push({
        path: '/views/resource/urgentPurchaseApplyStep',
        query: {
          resOrderType: this.purchaseApplyManageInfo.conditions.resOrderType
        }
      })
    },
    _openDetailPurchaseApplyModel(purchaseApply) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: purchaseApply.applyOrderId,
          resOrderType: '10000'
        }
      })
    },
    _openDeletePurchaseApplyModel(purchaseApply) {
      this.$refs.deletePurchaseApply.open(purchaseApply)
    },
    _queryPurchaseApplyMethod() {
      this.page.current = 1
      this._listPurchaseApplys(this.page.current, this.page.size)
    },
    _resetInspectionPlanMethod() {
      this.purchaseApplyManageInfo.conditions = {
        ...this.purchaseApplyManageInfo.conditions,
        applyOrderId: '',
        name: '',
        resName: '',
        startTime: '',
        endTime: ''
      }
      this._listPurchaseApplys(this.page.current, this.page.size)
    },
    async _openRunWorkflowImage(purchaseApply) {
      try {
        const params = {
          businessKey: purchaseApply.applyOrderId,
          communityId: getCommunityId()
        }
        const { data } = await getWorkflowImage(params)
        this.$refs.viewImage.open({
          url: 'data:image/png;base64,' + data
        })
      } catch (error) {
        console.error('获取流程图失败:', error)
      }
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.purchaseApplyManageInfo.conditions,
          pagePath: 'purchaseApplyManage'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    swatchState(item) {
      this.purchaseApplyManageInfo.conditions.state = item.statusCd
      this._listPurchaseApplys(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listPurchaseApplys(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listPurchaseApplys(this.page.current, this.page.size)
    },

  }
}
</script>

<style lang="scss" scoped>
.purchase-apply-manage-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .left-panel {
    .state-card {
      .state-list-wrapper {
        max-height: 600px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .state-list-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }
}
</style>