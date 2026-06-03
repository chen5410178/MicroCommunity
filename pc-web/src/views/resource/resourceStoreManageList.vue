<template>
  <div class="animated fadeInRight ecommerce">
    <el-row :gutter="20">
      <el-col :span="4" class="tree-container">
        <div class="tree-header">
          <el-button type="text" size="small" @click="openAddResourceStoreTypeModal(1)">
            {{ $t('resourceStoreManage.addFirstLevel') }}
          </el-button>
          <el-button type="text" size="small" @click="openAddResourceStoreTypeModal(2)">
            {{ $t('resourceStoreManage.addSecondLevel') }}
          </el-button>
          <el-button type="text" size="small" @click="openEditResourceStoreTypeModel">
            {{ $t('resourceStoreManage.edit') }}
          </el-button>
          <el-button type="text" size="small" @click="openDeleteResourceStoreTypeModel">
            {{ $t('resourceStoreManage.delete') }}
          </el-button>
        </div>
        <resource-store-type-tree ref="resourceStoreTypeTree" @switchParent="switchParent" @switchRst="switchRst" />
      </el-col>
      <el-col :span="20">
        <el-card class="margin-top-xs">
          <div slot="header" class="text-left">
            <span>{{ $t('resourceStoreManage.queryCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="resourceStoreManageInfo.conditions.shId" style="width:100%">
                <el-option :label="$t('resourceStoreManage.selectStorehouse')" value=""></el-option>
                <el-option v-for="(item, index) in resourceStoreManageInfo.storehouses" :key="index"
                  :label="item.shName" :value="item.shId"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input :placeholder="$t('resourceStoreManage.enterResourceName')"
                v-model.trim="resourceStoreManageInfo.conditions.resName"></el-input>
            </el-col>
            <el-col :span="4">
              <el-input :placeholder="$t('resourceStoreManage.enterResourceCode')"
                v-model.trim="resourceStoreManageInfo.conditions.resCode"></el-input>
            </el-col>
            <el-col :span="4">
              <el-select v-model="resourceStoreManageInfo.conditions.rssId" style="width:100%">
                <el-option :label="$t('resourceStoreManage.selectSpec')" value=""></el-option>
                <el-option v-for="(item, index) in resourceStoreManageInfo.resourceStoreSpecifications" :key="index"
                  :label="item.specName" :value="item.rssId"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="resourceStoreManageInfo.conditions.isFixed" style="width:100%">
                <el-option :label="$t('resourceStoreManage.selectIsFixed')" value=""></el-option>
                <el-option v-for="(item, index) in resourceStoreManageInfo.isFixeds" :key="index" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" size="small" @click="queryResourceStoreMethod">
                <i class="el-icon-search"></i>
                {{ $t('resourceStoreManage.query') }}
              </el-button>
              <el-button type="primary" size="small" @click="resetResourceStoreMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('resourceStoreManage.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="margin-top-xs">
          <div slot="header" class="card-header">
            <span>{{ $t('resourceStoreManage.resourceInfo') }}</span>
            <div>
              <el-button type="primary" size="small" @click="openAddResourceStoreModal"
                v-if="hasPrivilege('502022081709270004')">
                <i class="el-icon-plus"></i>
                {{ $t('resourceStoreManage.add') }}
              </el-button>
              <el-button type="primary" size="small" @click="jump2InPage" v-if="hasPrivilege('502021042137960001')">
                <i class="el-icon-plus"></i>
                {{ $t('resourceStoreManage.directPurchase') }}
              </el-button>
              <el-button type="primary" size="small" @click="jump2OutPage" v-if="hasPrivilege('502021042137960001')">
                <i class="el-icon-plus"></i>
                {{ $t('resourceStoreManage.directOut') }}
              </el-button>
              <el-button type="primary" size="small" @click="importResourceStoreModal"
                v-if="hasPrivilege('502022081750470005')">
                <i class="el-icon-upload"></i>
                {{ $t('resourceStoreManage.import') }}
              </el-button>
              <el-button type="primary" size="small" @click="exportExcel">
                <i class="el-icon-download"></i>
                {{ $t('resourceStoreManage.export') }}
              </el-button>
              <el-button type="primary" size="small" @click="goBack"
                v-if="resourceStoreManageInfo.conditions.flag == '1'">
                <i class="el-icon-close"></i>
                {{ $t('resourceStoreManage.back') }}
              </el-button>
            </div>
          </div>
          <el-table :data="resourceStoreManageInfo.resourceStores" border style="width:100%">
            <el-table-column prop="resId" :label="$t('resourceStoreManage.resourceId')"
              align="center"></el-table-column>
            <el-table-column :label="$t('resourceStoreManage.resourceNameCode')" align="center">
              <template slot-scope="scope">
                {{ scope.row.resName }}({{ scope.row.resCode }})
              </template>
            </el-table-column>
            <el-table-column prop="shName" :label="$t('resourceStoreManage.storeName')"
              align="center"></el-table-column>
            <el-table-column :label="$t('resourceStoreManage.resourceType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentRstName }}
                {{ scope.row.rstName ? ' > ' + scope.row.rstName : '' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.resourceSpec')" align="center">
              <template slot-scope="scope">
                {{ scope.row.rssName ? scope.row.rssName : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="isFixedName" :label="$t('resourceStoreManage.fixedResource')"
              align="center"></el-table-column>
            <el-table-column :label="$t('resourceStoreManage.referencePrice')" align="center">
              <template slot-scope="scope">
                {{ '￥' + scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.chargeStandard')" align="center">
              <template slot-scope="scope">
                {{ scope.row.outHighPrice == scope.row.outLowPrice
                  ? '￥' + scope.row.outLowPrice
                  : '￥' + scope.row.outLowPrice + '-￥' + scope.row.outHighPrice }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.stock')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stock }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.minUnit')" align="center">
              <template slot-scope="scope">
                1{{ scope.row.unitCodeName }}={{ scope.row.miniUnitStock }}{{ scope.row.miniUnitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.minTotal')" align="center">
              <template slot-scope="scope">
                {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.avgPrice')" align="center">
              <template slot-scope="scope">
                {{ scope.row.averagePrice ? '￥' + scope.row.averagePrice : '￥0' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.totalPrice')" align="center">
              <template slot-scope="scope">
                <el-link type="primary" @click="queryTotalPrice(scope.row)">{{ $t('resourceStoreManage.query')
                }}</el-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceStoreManage.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" type="primary" v-if="hasPrivilege('502022081701700001')"
                    @click="openEditResourceStoreModel(scope.row)">
                    {{ $t('resourceStoreManage.edit') }}
                  </el-button>
                  <el-button size="mini" type="danger" v-if="hasPrivilege('502022081741410002')"
                    @click="openDeleteResourceStoreModel(scope.row)">
                    {{ $t('resourceStoreManage.delete') }}
                  </el-button>
                  <el-button size="mini" type="info" @click="toResourceStoreDetail(scope.row)">
                    {{ $t('resourceStoreManage.detail') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="margin-top">
            <el-col :span="12" class="table-desc">
              <div>
                <b>{{ $t('resourceStoreManage.subTotal') }}</b>
                <span class="margin-left">
                  {{ $t('resourceStoreManage.totalPrice') }}：{{ resourceStoreManageInfo.subTotalPrice }}
                  {{ $t('resourceStoreManage.yuan') }}
                </span>
              </div>
              <div class="margin-top-xs">
                <b>{{ $t('resourceStoreManage.grandTotal') }}</b>
                <span class="margin-left">
                  {{ $t('resourceStoreManage.totalPrice') }}：{{ resourceStoreManageInfo.highTotalPrice }}
                  {{ $t('resourceStoreManage.yuan') }}
                </span>
              </div>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange" @current-change="handlePageChange"></el-pagination>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <add-resource-store ref="addResourceStore" @success="handleResourceStoreSuccess" />
    <edit-resource-store ref="editResourceStore" @success="handleResourceStoreSuccess" />
    <delete-resource-store ref="deleteResourceStore" @success="handleResourceStoreSuccess" />
    <resource-store-times ref="resourceStoreTimes" />
    <import-resource-store ref="importResourceStore" @success="handleResourceStoreSuccess" />
    <add-resource-store-type ref="addResourceStoreType" @success="handleResourceStoreTypeSuccess" />
    <edit-resource-store-type ref="editResourceStoreType" @success="handleResourceStoreTypeSuccess" />
    <delete-resource-store-type ref="deleteResourceStoreType" @success="handleResourceStoreTypeSuccess" />
  </div>
</template>

<script>
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { listResourceStores, exportData, listResourceStoreSpecifications, listStorehouses, listResourceStoreTypes } from '@/api/resource/resourceStoreManageApi'
//import {  } from '@/api/resource/resourceStoreTypeApi'
import ResourceStoreTypeTree from '@/components/resource/resourceStoreTypeTree'
import AddResourceStore from '@/components/resource/addResourceStore'
import EditResourceStore from '@/components/resource/editResourceStore'
import DeleteResourceStore from '@/components/resource/deleteResourceStore'
import ResourceStoreTimes from '@/components/resource/resourceStoreTimes'
import ImportResourceStore from '@/components/resource/importResourceStore'
import AddResourceStoreType from '@/components/resource/addResourceStoreType'
import EditResourceStoreType from '@/components/resource/editResourceStoreType'
import DeleteResourceStoreType from '@/components/resource/deleteResourceStoreType'

export default {
  name: 'ResourceStoreManageList',
  components: {
    ResourceStoreTypeTree,
    AddResourceStore,
    EditResourceStore,
    DeleteResourceStore,
    ResourceStoreTimes,
    ImportResourceStore,
    AddResourceStoreType,
    EditResourceStoreType,
    DeleteResourceStoreType
  },
  data() {
    return {
      resourceStoreManageInfo: {
        resourceStores: [],
        total: 0,
        records: 1,
        subTotalPrice: 0.0,
        highTotalPrice: 0.0,
        isFixeds: [],
        conditions: {
          resId: '',
          resName: '',
          resCode: '',
          shId: '',
          parentRstId: '',
          rstId: '',
          rssId: '',
          isFixed: '',
          flag: '',
          userId: '',
          communityId: '',
          page: 1,
          row: 10
        },
        curType: {},
        storehouses: [],
        resourceStoreSpecifications: []
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.resourceStoreManageInfo.conditions.communityId = this.communityId
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'))

    const flag = this.$route.query.flag
    if (flag) {
      this.resourceStoreManageInfo.conditions.flag = flag
    }

    this.initData()
  },
  methods: {
    async initData() {
      await this.loadDictData()
      await this.listResourceStores()
      await this.listStorehouses()
      await this.listResourceStoreSpecifications()
    },

    async loadDictData() {
      try {
        const data = await getDict('resource_store', 'is_fixed')
        this.resourceStoreManageInfo.isFixeds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async listResourceStores() {
      try {
        this.resourceStoreManageInfo.conditions.page = this.pagination.current
        this.resourceStoreManageInfo.conditions.row = this.pagination.size

        const response = await listResourceStores(this.resourceStoreManageInfo.conditions)
        const data = response

        this.resourceStoreManageInfo.resourceStores = data.resourceStores || []
        this.resourceStoreManageInfo.subTotalPrice = data.subTotal || 0
        this.resourceStoreManageInfo.highTotalPrice = data.totalPrice || 0
        this.pagination.total = data.total || 0
      } catch (error) {
        console.error('获取物品列表失败:', error)
      }
    },

    async listStorehouses() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        const response = await listStorehouses(params)
        this.resourceStoreManageInfo.storehouses = response.data || []
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },

    async listResourceStoreSpecifications() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        const response = await listResourceStoreSpecifications(params)
        this.resourceStoreManageInfo.resourceStoreSpecifications = response.data || []
      } catch (error) {
        console.error('获取物品规格列表失败:', error)
      }
    },

    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this.listResourceStores()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.listResourceStores()
    },

    queryResourceStoreMethod() {
      this.pagination.current = 1
      this.listResourceStores()
    },

    resetResourceStoreMethod() {
      this.resourceStoreManageInfo.conditions = {
        ...this.resourceStoreManageInfo.conditions,
        resId: '',
        resName: '',
        resCode: '',
        shId: '',
        rstId: '',
        parentRstId: '',
        rssId: '',
        isFixed: ''
      }
      this.pagination.current = 1
      this.listResourceStores()
    },

    openAddResourceStoreModal() {
      this.$refs.addResourceStore.open()
    },

    openEditResourceStoreModel(row) {
      this.$refs.editResourceStore.open(row)
    },

    openDeleteResourceStoreModel(row) {
      this.$refs.deleteResourceStore.open(row)
    },

    openAddResourceStoreTypeModal(level) {
      let _curType = this.resourceStoreManageInfo.curType;
      if (level == 1) {
        this.resourceStoreManageInfo.curType = {}
        this.$refs.addResourceStoreType.open('')
        return;
      }
      if (!_curType || !_curType.rstId) {
        this.$message.error(this.$t('resourceStoreManage.selectType'))
        return;
      }
      this.$refs.addResourceStoreType.open(_curType.rstId)


    },

    openDeleteResourceStoreTypeModel() {
      let _curType = this.resourceStoreManageInfo.curType;
      if (!_curType.rstId) {

        return;
      }
      this.$refs.deleteResourceStoreType.open(_curType)
    },

    openEditResourceStoreTypeModel() {
      let _curType = this.resourceStoreManageInfo.curType;
      if (!_curType.rstId) {
        this.$message.error(this.$t('resourceStoreManage.selectType'))
        return;
      }
      this.$refs.editResourceStoreType.open(_curType)
    },

    importResourceStoreModal() {
      this.$refs.importResourceStore.open()
    },

    queryTotalPrice(row) {
      this.$refs.resourceStoreTimes.open(row)
    },

    switchParent(param) {
      console.log(param)
      this.resourceStoreManageInfo.conditions.parentRstId = param.parentRstId
      this.resourceStoreManageInfo.conditions.rstId = ''
      this.listResourceStoreType(param.parentRstId)
      this.listResourceStores()
    },

    switchRst(param) {
      this.resourceStoreManageInfo.conditions.parentRstId = ''
      this.resourceStoreManageInfo.conditions.rstId = param.rstId
      this.listResourceStoreType(param.rstId)
      this.listResourceStores()
    },
    async listResourceStoreType(rstId) {
      const { data } = await listResourceStoreTypes({ page: 1, row: 1, rstId: rstId, communityId: this.getCommunityId() })
      this.resourceStoreManageInfo.curType = data[0]
    },

    handleResourceStoreSuccess() {
      this.listResourceStores()
    },

    handleResourceStoreTypeSuccess() {
      this.$refs.resourceStoreTypeTree.refreshTree()
    },

    async exportExcel() {
      try {
        this.resourceStoreManageInfo.conditions.pagePath = 'resourceStoreManage'
        const res = await exportData(this.resourceStoreManageInfo.conditions)
        this.$message.success(this.$t('common.operationSuccess'))
        if (res && res.code === 0) {
          window.open('/#/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('resourceStoreManage.exportFailed'))
      }
    },

    jump2InPage() {
      this.$router.push({
        path: '/views/resource/addPurchaseApply',
        query: {
          resOrderType: '10000',
          purchaseSwitch: 'OFF'
        }
      })
    },

    jump2OutPage() {
      this.$router.push({
        path: '/views/resource/addItemOut',
        query: {
          resOrderType: '20000',
          from: 'resourceStore'
        }
      })
    },

    toResourceStoreDetail(resource) {
      window.open(`/#/views/resource/resourceDetail?resId=${resource.resId}`)
    },

    goBack() {
      this.$router.go(-1)
    },
  }
}
</script>

<style scoped>
.animated {
  padding: 15px;
}

.tree-container {
  padding-right: 0;
  height: 85vh;
  overflow-y: auto;
  border-right: 1px solid #ebeef5;
}



.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-xs {
  margin-top: 10px;
}

.margin-left {
  margin-left: 10px;
}

.text-left {
  text-align: left;
}

.text-right {
  text-align: right;
}
</style>