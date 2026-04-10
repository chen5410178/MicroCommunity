<template>
  <div class="item-out-manage-container">
    <el-row class="wrapper" :gutter="10">
      <el-col :span="4" class="padding-r-0">
        <div class="border-radius">
          <div class="margin-xs-r treeview attendance-staff">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in itemOutManageInfo.states" :key="index" class="list-group-item node-orgTree"
                :class="{ 'vc-node-selected': itemOutManageInfo.conditions.state === item.statusCd }"
                @click="swatchState(item)">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemOutManage.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="itemOutManageInfo.conditions.applyOrderId"
                :placeholder="$t('itemOutManage.search.applyOrderId')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="itemOutManageInfo.conditions.applyUserName"
                :placeholder="$t('itemOutManage.search.applyUserName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="itemOutManageInfo.conditions.resName"
                :placeholder="$t('itemOutManage.search.resName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="itemOutManageInfo.conditions.startTime" type="datetime"
                :placeholder="$t('itemOutManage.search.startTime')" style="width: 100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="itemOutManageInfo.conditions.endTime" type="datetime"
                :placeholder="$t('itemOutManage.search.endTime')" style="width: 100%"
                :disabled="!itemOutManageInfo.conditions.startTime" :picker-options="endDateOptions" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryInspectionPlanMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetInspectionPlanMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('itemOutManage.list.title') }}</span>
            <div style="float: right">
              <el-button type="primary" size="small" @click="_openAddItemOutModal">
                <i class="el-icon-plus"></i>
                {{ $t('itemOutManage.list.apply') }}
              </el-button>
              <el-button type="primary" size="small" @click="_exportExcel">
                <i class="el-icon-download"></i>
                {{ $t('common.export') }}
              </el-button>
            </div>
          </div>
          <el-table :data="itemOutManageInfo.itemOuts" border style="width: 100%" v-loading="loading">
            <el-table-column prop="applyOrderId" :label="$t('itemOutManage.table.applyOrderId')" align="center" />
            <el-table-column prop="resourceNames" :label="$t('itemOutManage.table.resourceNames')" align="center" />
            <el-table-column prop="userName" :label="$t('itemOutManage.table.userName')" align="center" />
            <el-table-column prop="createUserName" :label="$t('itemOutManage.table.createUserName')" align="center" />
            <el-table-column prop="createTime" :label="$t('itemOutManage.table.createTime')" align="center" />
            <el-table-column prop="stateName" :label="$t('itemOutManage.table.stateName')" align="center" />
            <el-table-column :label="$t('itemOutManage.table.warehousingWay')" align="center">
              <template slot-scope="scope">
                {{ scope.row.warehousingWay === 10000 ? $t('itemOutManage.table.directOut') :
                  $t('itemOutManage.table.auditOut') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="220">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openDetailItemOutModel(scope.row)">
                  {{ $t('common.view') }}
                </el-button>
                <el-button v-if="scope.row.warehousingWay !== 10000" size="mini"
                  @click="_openRunWorkflowImage(scope.row)">
                  {{ $t('itemOutManage.table.flowChart') }}
                </el-button>
                <el-button v-if="scope.row.state === '1000' && itemOutManageInfo.currentUserId === scope.row.createUserId"
                  size="mini" type="danger" @click="_openDeleteItemOutModel(scope.row)">
                  {{ $t('itemOutManage.table.cancelApply') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination class="pagination" :current-page.sync="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <delete-item-out ref="deleteItemOut" @success="handleSuccess" />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listPurchaseApplys, exportData,listRunWorkflowImage } from '@/api/resource/itemOutManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteItemOut from '@/components/resource/deleteItemOut'
import ViewImage from '@/components/system/viewImage'
import {getUserId} from '@/api/user/userApi'

export default {
  name: 'ItemOutManageList',
  components: {
    DeleteItemOut,
    ViewImage
  },
  data() {
    return {
      loading: false,
      itemOutManageInfo: {
        itemOuts: [],
        states: [{ statusCd: '', name: this.$t('common.all') }],
        currentUserId: '',
        conditions: {
          applyOrderId: '',
          state: '',
          applyUserName: '',
          resOrderType: '20000',
          resName: '',
          startTime: '',
          endTime: '',
          communityId: ''
        }
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      endDateOptions: {
        disabledDate: (time) => {
          if (this.itemOutManageInfo.conditions.startTime) {
            return time.getTime() < new Date(this.itemOutManageInfo.conditions.startTime).getTime()
          }
          return false
        }
      }
    }
  },
  created() {
    this.itemOutManageInfo.currentUserId = getUserId()
    this.itemOutManageInfo.conditions.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      await this.getDictData()
      this._listItemOuts()
    },
    async getDictData() {
      try {
        const data = await getDict('purchase_apply', 'state')
        this.itemOutManageInfo.states = [{ statusCd: '', name: this.$t('common.all') }, ...data]
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listItemOuts() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          ...this.itemOutManageInfo.conditions
        }
        const { data, total } = await listPurchaseApplys(params)
        this.itemOutManageInfo.itemOuts = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    _openAddItemOutModal() {
      this.$router.push({
        path: '/views/resource/addItemOut',
        query: { resOrderType: this.itemOutManageInfo.conditions.resOrderType }
      })
    },
    _openDetailItemOutModel(itemOut) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: itemOut.applyOrderId,
          resOrderType: this.itemOutManageInfo.conditions.resOrderType
        }
      })
    },
    _openDeleteItemOutModel(itemOut) {
      this.$refs.deleteItemOut.open(itemOut)
    },
    _queryInspectionPlanMethod() {
      this.pagination.currentPage = 1
      this._listItemOuts()
    },
    _resetInspectionPlanMethod() {
      this.itemOutManageInfo.conditions = {
        applyOrderId: '',
        state: '',
        applyUserName: '',
        resOrderType: '20000',
        resName: '',
        startTime: '',
        endTime: '',
        communityId: getCommunityId()
      }
      this.pagination.currentPage = 1
      this._listItemOuts()
    },
    async _openRunWorkflowImage(itemOut) {
      try {
        const params = {
          businessKey: itemOut.applyOrderId,
          communityId: getCommunityId()
        }
        const { data } = await listRunWorkflowImage(params)
        this.$refs.viewImage.open({ url: 'data:image/png;base64,' + data })
      } catch (error) {
        console.error('获取流程图失败:', error)
      }
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.itemOutManageInfo.conditions,
          pagePath: 'itemOutManage'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=downloadCenter')
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    swatchState(item) {
      this.itemOutManageInfo.conditions.state = item.statusCd
      this._listItemOuts()
    },
    handleSuccess() {
      this._listItemOuts()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this._listItemOuts()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._listItemOuts()
    }
  }
}
</script>

<style lang="scss" scoped>
.item-out-manage-container {
  padding: 20px;

  .wrapper {
    width: 100%;
  }

  .border-radius {
    border-radius: 4px;
  }

  .margin-xs-r {
    margin-right: 5px;
  }

  .list-group {
    padding: 0;
    margin: 0;
    list-style: none;

    .list-group-item {
      padding: 10px 15px;
      margin-bottom: -1px;
      background-color: #fff;
      cursor: pointer;

      &:hover {
        background-color: #f5f5f5;
      }

      &.vc-node-selected {
        background-color: #409eff;
        color: #fff;
      }
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>