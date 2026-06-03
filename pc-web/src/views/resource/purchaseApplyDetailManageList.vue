<template>
  <div class="purchase-apply-detail-manage-container">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
          <el-card class="type-card">
            <div class="type-list-wrapper">
              <div v-for="(item, index) in resOrderTypes" :key="index"
                class="type-list-item"
                :class="{ 'selected': conditions.resOrderType == item.statusCd }"
                @click="swatchResOrderTypes(item)">
                {{ item.name }}
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="21" class="right-panel">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('purchaseApplyDetailManage.search.title') }}</span>
            <div style="float: right;">
              <el-button type="text" @click="_moreCondition()" style="padding:0;">
                {{ moreCondition ? $t('purchaseApplyDetailManage.search.hide') :
                  $t('purchaseApplyDetailManage.search.more') }}
              </el-button>
            </div>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="conditions.applyOrderId"
                :placeholder="$t('purchaseApplyDetailManage.search.applyOrderId')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('purchaseApplyDetailManage.search.state')"
                style="width:100%" clearable>
                <el-option v-for="(item, index) in states" :key="index" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="conditions.name" :placeholder="$t('purchaseApplyDetailManage.search.name')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="conditions.endUserName"
                :placeholder="$t('purchaseApplyDetailManage.search.endUserName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="conditions.resName" :placeholder="$t('purchaseApplyDetailManage.search.resName')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryInspectionPlanMethod()">
                <i class="el-icon-search" />
                {{ $t('common.search') }}
              </el-button>
              <el-button type="primary" @click="_resetInspectionPlanMethod()">
                <i class="el-icon-refresh" />
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row v-show="moreCondition" :gutter="20" class="margin-bottom margin-top">
            <el-col :span="4">
              <el-select v-model="conditions.warehousingWay"
                :placeholder="$t('purchaseApplyDetailManage.search.warehousingWay')" style="width:100%" clearable>
                <el-option v-for="(item, index) in warehousingWays" :key="index" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('purchaseApplyDetailManage.search.startTime')" style="width:100%"  />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('purchaseApplyDetailManage.search.endTime')" style="width:100%"  />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.parentRstId"
                :placeholder="$t('purchaseApplyDetailManage.search.parentRstId')" style="width:100%" clearable
                @change="_listResourceStoreSonTypes">
                <el-option v-for="(item, index) in resourceStoreTypes" :key="index" :label="item.name"
                  :value="item.rstId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.rstId" :placeholder="$t('purchaseApplyDetailManage.search.rstId')"
                style="width:100%" clearable @change="_listResourceStoreSpecifications">
                <el-option v-for="(item, index) in resourceStoreSonTypes" :key="index" :label="item.name"
                  :value="item.rstId" />
              </el-select>
            </el-col>
          </el-row>
          <el-row v-show="moreCondition" :gutter="20">
            <el-col :span="4">
              <el-select v-model="conditions.rssId" :placeholder="$t('purchaseApplyDetailManage.search.rssId')"
                style="width:100%" clearable>
                <el-option v-for="(item, index) in resourceStoreSpecifications" :key="index" :label="item.specName"
                  :value="item.rssId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.rsId" :placeholder="$t('purchaseApplyDetailManage.search.rsId')"
                style="width:100%" clearable>
                <el-option v-for="(item, index) in resourceSuppliers" :key="index" :label="item.supplierName"
                  :value="item.rsId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.shId" :placeholder="$t('purchaseApplyDetailManage.search.shId')"
                style="width:100%" clearable>
                <el-option v-for="(item, index) in storehouses" :key="index" :label="item.shName" :value="item.shId" />
              </el-select>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('purchaseApplyDetailManage.list.title') }}</span>
            <div style="float: right;">
              <el-button type="primary" @click="_exportExcel()">
                <i class="el-icon-download" />
                {{ $t('common.export') }}
              </el-button>
            </div>
          </div>
          <el-table :data="purchaseApplyDetails" border style="width: 100%">
            <el-table-column prop="applyOrderId" :label="$t('purchaseApplyDetailManage.table.applyOrderId')"
              align="center" />
            <el-table-column prop="userName" :label="$t('purchaseApplyDetailManage.table.userName')" align="center" />
            <el-table-column prop="endUserName" :label="$t('purchaseApplyDetailManage.table.endUserName')"
              align="center" />
            <el-table-column prop="resOrderTypeName" :label="$t('purchaseApplyDetailManage.table.resOrderTypeName')"
              align="center" />
            <el-table-column :label="$t('purchaseApplyDetailManage.table.rstName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
              </template>
            </el-table-column>
            <el-table-column prop="resName" :label="$t('purchaseApplyDetailManage.table.resName')" align="center" />
            <el-table-column :label="$t('purchaseApplyDetailManage.table.specName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.specName || '--' }}
              </template>
            </el-table-column>
            <el-table-column prop="isFixedName" :label="$t('purchaseApplyDetailManage.table.isFixedName')"
              align="center" />
            <el-table-column :label="$t('purchaseApplyDetailManage.table.supplierName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.supplierName || '--' }}
              </template>
            </el-table-column>
            <el-table-column prop="shName" :label="$t('purchaseApplyDetailManage.table.shName')" align="center" />
            <el-table-column :label="$t('purchaseApplyDetailManage.table.warehousingWayName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.warehousingWayName }}{{ scope.row.resOrderTypeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('purchaseApplyDetailManage.table.quantity')" align="center">
              <template slot-scope="scope">
                {{ scope.row.quantity }}{{ scope.row.unitCodeName || '' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('purchaseApplyDetailManage.table.purchaseQuantity')" align="center">
              <template slot-scope="scope">
                {{ scope.row.purchaseQuantity ?
                  scope.row.purchaseQuantity + scope.row.unitCodeName : '--' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('purchaseApplyDetailManage.table.price')" align="center">
              <template slot-scope="scope">
                ￥{{ scope.row.price ? scope.row.price : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('purchaseApplyDetailManage.table.totalApplyPrice')" align="center">
              <template slot-scope="scope">
                ￥{{ scope.row.totalApplyPrice }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('purchaseApplyDetailManage.table.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark }}
                <el-popover v-if="scope.row.purchaseRemark" placement="top-start" width="200" trigger="hover">
                  <div>{{ scope.row.purchaseRemark }}</div>
                  <i slot="reference" class="el-icon-info" style="cursor:pointer" />
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('purchaseApplyDetailManage.table.stateName')" align="center" />
            <el-table-column prop="createTime" :label="$t('purchaseApplyDetailManage.table.createTime')"
              align="center" />
          </el-table>
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listPurchaseApplyDetails, exportData, listResourceSuppliers, listResourceStoreTypes, listResourceStoreSpecifications, listStorehouses } from '@/api/resource/purchaseApplyDetailManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'PurchaseApplyDetailManageList',
  components: {
    ViewImage
  },
  data() {
    return {
      purchaseApplyDetails: [],
      total: 0,
      records: 1,
      moreCondition: false,
      states: [],
      warehousingWays: [],
      resOrderTypes: [],
      resourceSuppliers: [],
      storehouses: [],
      conditions: {
        applyOrderId: '',
        state: '',
        userId: '',
        name: '',
        resOrderType: '',
        endUserName: '',
        resName: '',
        rsId: '',
        rssId: '',
        parentRstId: '',
        rstId: '',
        warehousingWay: '',
        startTime: '',
        endTime: '',
        shId: '',
        shName: '',
        communityId: ''
      },
      resourceStoreTypes: [],
      resourceStoreSonTypes: [],
      resourceStoreSpecifications: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.conditions.communityId = this.communityId
    this._initData()
  },
  methods: {
    async _initData() {
      await this._queryStorehouses()
      await this._listPurchaseApplyDetails(this.page.current, this.page.size)
      await this.getDictData()
      await this._listResourceStoreTypes()
      await this._listResourceStoreSpecifications()
      await this._listResourceSupplier()
    },
    async getDictData() {
      try {
        const stateData = await getDict('purchase_apply', 'state')
        this.states = stateData
        const warehousingWayData = await getDict('purchase_apply', 'warehousing_way')
        this.warehousingWays = warehousingWayData
        const resOrderTypeData = await getDict('purchase_apply', 'res_order_type')
        this.resOrderTypes = [{ statusCd: '', name: this.$t('common.all') }].concat(resOrderTypeData)
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listPurchaseApplyDetails(page, size) {
      try {
        this.conditions.page = page
        this.conditions.row = size
        const params = {
          params: this.conditions
        }
        const { data, total } = await listPurchaseApplyDetails(params)
        this.purchaseApplyDetails = data.map(item => {
          item.purchaseQuantity = item.purchaseQuantity ? item.purchaseQuantity : 0
          item.price = item.price ? item.price : 0
          item.totalApplyPrice = (item.purchaseQuantity * item.price).toFixed(2)
          return item
        })
        this.page.total = total
      } catch (error) {
        console.error('获取采购申请明细列表失败:', error)
      }
    },
    async _listResourceSupplier() {
      try {
        const params = {
          params: {
            page: 1,
            row: 100,
            communityId: this.communityId
          }
        }
        const { data } = await listResourceSuppliers(params)
        this.resourceSuppliers = data
      } catch (error) {
        console.error('获取供应商列表失败:', error)
      }
    },
    _openDetailPurchaseApplyDetailModel(purchaseApplyDetail) {
      this.$router.push(`/pages/common/purchaseApplyDetail?applyOrderId=${purchaseApplyDetail.applyOrderId}&resOrderType=10000`)
    },
    _queryInspectionPlanMethod() {
      this._listPurchaseApplyDetails(this.page.current, this.page.size)
    },
    _resetInspectionPlanMethod() {
      this.conditions = {
        applyOrderId: '',
        state: '',
        userId: '',
        name: '',
        resOrderType: '',
        endUserName: '',
        resName: '',
        rsId: '',
        rssId: '',
        parentRstId: '',
        rstId: '',
        warehousingWay: '',
        startTime: '',
        endTime: '',
        shId: '',
        shName: '',
        communityId: this.communityId
      }
      this.resourceStoreSonTypes = []
      this.resourceStoreSpecifications = []
      this._listPurchaseApplyDetails(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.moreCondition = !this.moreCondition
    },
    async _listResourceStoreTypes() {
      try {
        const params = {
          params: {
            page: 1,
            row: 100,
            communityId: this.communityId,
            parentId: '0'
          }
        }
        const { data } = await listResourceStoreTypes(params)
        this.resourceStoreTypes = data
      } catch (error) {
        console.error('获取物品类型列表失败:', error)
      }
    },
    async _listResourceStoreSonTypes() {
      this.conditions.rstId = ''
      this.resourceStoreSonTypes = []
      if (!this.conditions.parentRstId) return

      try {
        const params = {
          params: {
            page: 1,
            row: 100,
            communityId: this.communityId,
            parentId: this.conditions.parentRstId
          }
        }
        const { data } = await listResourceStoreTypes(params)
        this.resourceStoreSonTypes = data
      } catch (error) {
        console.error('获取二级分类列表失败:', error)
      }
    },
    async _listResourceStoreSpecifications() {
      this.conditions.rssId = ''
      this.resourceStoreSpecifications = []
      if (!this.conditions.rstId) return

      try {
        const params = {
          params: {
            page: 1,
            row: 100,
            communityId: this.communityId,
            rstId: this.conditions.rstId
          }
        }
        const { data } = await listResourceStoreSpecifications(params)
        this.resourceStoreSpecifications = data
      } catch (error) {
        console.error('获取物品规格列表失败:', error)
      }
    },
    async _queryStorehouses() {
      try {
        const params = {
          params: {
            page: 1,
            row: 50,
            communityId: this.communityId
          }
        }
        const { data } = await listStorehouses(params)
        this.storehouses = data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    _viewPurchaseApplyDetail(item) {
      this.$refs.viewImage.open({
        data: {
          "备注": item.purchaseRemark
        }
      })
    },
    async _exportExcel() {
      try {
        this.conditions.communityId = this.communityId
        this.conditions.pagePath = 'purchaseApplyDetailManage'
        const params = {
          params: this.conditions
        }
        const { code, msg } = await exportData(params)
        this.$message.success(msg)
        if (code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    swatchResOrderTypes(item) {
      this.conditions.resOrderType = item.statusCd
      this._listPurchaseApplyDetails(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listPurchaseApplyDetails(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listPurchaseApplyDetails(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.purchase-apply-detail-manage-container {
  padding: 20px;

  .left-panel {
    .type-card {
      .type-list-wrapper {
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

        .type-list-item {
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

  .right-panel .detail-wrapper {
    margin-bottom: 20px;
  }
  .right-panel .detail-item {
    margin-bottom: 16px;
  }
  .right-panel .detail-label {
    color: #606266;
    font-size: 14px;
    margin-bottom: 4px;
  }

  .box-card {
    margin-bottom: 20px;
  }
}
</style>