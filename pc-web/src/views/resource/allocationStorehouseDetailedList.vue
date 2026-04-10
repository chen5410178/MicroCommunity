<template>
  <div class="animated fadeInRight ecommerce padding">
    <el-row :gutter="20">
      <el-col :span="4">

        <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in allocationStorehousesInfo.applyTypes" :key="index"
                @click="swatchApplyType(item)"
                :class="{ 'vc-node-selected': allocationStorehousesInfo.conditions.applyType == item.statusCd }"
                class="list-group-item node-orgTree">
                {{ item.name }}
              </li>
            </ul>
          </div>

        </div>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseDetailed.queryCondition') }}</span>
            <div class="ibox-tools">
              <el-button type="text" style="margin-right:10px;" @click="_moreCondition()">
                {{ allocationStorehousesInfo.moreCondition == true ? $t('allocationStorehouseDetailed.hide') :
                  $t('allocationStorehouseDetailed.more') }}
              </el-button>
            </div>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="allocationStorehousesInfo.conditions.resId"
                :placeholder="$t('allocationStorehouseDetailed.resIdPlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="allocationStorehousesInfo.conditions.resName"
                :placeholder="$t('allocationStorehouseDetailed.resNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.parentRstId"
                :placeholder="$t('allocationStorehouseDetailed.resTypePlaceholder')" style="width:100%"
                @change="_listResourceStoreSonTypes">
                <el-option v-for="(item, index) in allocationStorehousesInfo.resourceStoreTypes" :key="index"
                  :label="item.name" :value="item.rstId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.rstId"
                :placeholder="$t('allocationStorehouseDetailed.secondTypePlaceholder')" style="width:100%"
                @change="_listResourceStoreSpecifications">
                <el-option v-for="(item, index) in allocationStorehousesInfo.resourceStoreSonTypes" :key="index"
                  :label="item.name" :value="item.rstId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.rssId"
                :placeholder="$t('allocationStorehouseDetailed.specPlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in allocationStorehousesInfo.resourceStoreSpecifications" :key="index"
                  :label="item.specName" :value="item.rssId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryAllocationStorehouseMethod()">
                <i class="el-icon-search"></i>
                {{ $t('allocationStorehouseDetailed.query') }}
              </el-button>
              <el-button type="primary" @click="_resetAllocationStorehouseMethod()">
                <i class="el-icon-refresh"></i>
                {{ $t('allocationStorehouseDetailed.reset') }}
              </el-button>
            </el-col>
          </el-row>

          <el-row v-show="allocationStorehousesInfo.moreCondition == true" :gutter="20" class="margin-top margin-bottom">
            <el-col :span="4">
              <el-input v-model.trim="allocationStorehousesInfo.conditions.applyId"
                :placeholder="$t('allocationStorehouseDetailed.applyIdPlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.shIda"
                :placeholder="$t('allocationStorehouseDetailed.sourceStorePlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in allocationStorehousesInfo.storehouses" :key="index"
                  :label="item.shName" :value="item.shId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.shIdz"
                :placeholder="$t('allocationStorehouseDetailed.targetStorePlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in allocationStorehousesInfo.storehouses" :key="index"
                  :label="item.shName" :value="item.shId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="allocationStorehousesInfo.conditions.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('allocationStorehouseDetailed.startTimePlaceholder')" style="width:100%"  />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="allocationStorehousesInfo.conditions.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('allocationStorehouseDetailed.endTimePlaceholder')" style="width:100%"  />
            </el-col>
          </el-row>

          <el-row v-show="allocationStorehousesInfo.moreCondition == true" :gutter="20">
            <el-col :span="4">
              <el-select v-model="allocationStorehousesInfo.conditions.state"
                :placeholder="$t('allocationStorehouseDetailed.statePlaceholder')" style="width:100%">
                <el-option v-for="(item, index) in allocationStorehousesInfo.states" :key="index" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card" style="margin-top:20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseDetailed.allocationDetail') }}</span>
            <div class="ibox-tools">
              <el-button type="primary" size="small" @click="_exportExcel()">
                <i class="el-icon-download"></i>
                {{ $t('allocationStorehouseDetailed.export') }}
              </el-button>
            </div>
          </div>
          <el-table :data="allocationStorehousesInfo.resourceStores" border style="width: 100%">
            <el-table-column prop="applyId" :label="$t('allocationStorehouseDetailed.table.applyId')" align="center" />
            <el-table-column prop="resId" :label="$t('allocationStorehouseDetailed.table.resId')" align="center" />
            <el-table-column :label="$t('allocationStorehouseDetailed.table.resType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
              </template>
            </el-table-column>
            <el-table-column prop="resName" :label="$t('allocationStorehouseDetailed.table.resName')" align="center" />
            <el-table-column :label="$t('allocationStorehouseDetailed.table.specName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.specName ? scope.row.specName : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="isFixedName" :label="$t('allocationStorehouseDetailed.table.isFixedName')"
              align="center" />
            <el-table-column :label="$t('allocationStorehouseDetailed.table.originalStock')" align="center">
              <template slot-scope="scope">
                {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('allocationStorehouseDetailed.table.stock')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stock }}{{ scope.row.applyType == 20000 ? scope.row.miniUnitCodeName :
                  scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('allocationStorehouseDetailed.table.shaName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.applyType == '20000' ? scope.row.startUserName : scope.row.shaName }}
              </template>
            </el-table-column>
            <el-table-column prop="shzName" :label="$t('allocationStorehouseDetailed.table.shzName')" align="center" />
            <el-table-column prop="startUserName" :label="$t('allocationStorehouseDetailed.table.startUserName')"
              align="center" />
            <el-table-column prop="remark" :label="$t('allocationStorehouseDetailed.table.remark')" align="center" />
            <el-table-column :label="$t('allocationStorehouseDetailed.table.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stateName }}({{ scope.row.applyTypeName }})
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('allocationStorehouseDetailed.table.createTime')"
              align="center" />
          </el-table>
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAllocationStorehouses, listStorehouses, listResourceStoreTypes, listResourceStoreSpecifications, exportData } from '@/api/resource/allocationStorehouseDetailedApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'AllocationStorehouseDetailedList',
  data() {
    return {
      allocationStorehousesInfo: {
        resourceStores: [],
        storehouses: [],
        total: 0,
        records: 1,
        moreCondition: false,
        states: [],
        applyTypes: [],
        conditions: {
          asId: '',
          applyId: '',
          resId: '',
          resName: '',
          resCode: '',
          rssId: '',
          parentRstId: '',
          rstId: '',
          shIda: '',
          shIdz: '',
          stock: '',
          startUserId: '',
          state: '',
          applyType: '',
          startTime: '',
          endTime: '',
          userId: '',
          communityId: ''
        },
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        resourceStoreSpecifications: []
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      try {
        // 获取字典数据
        const stateData = await getDict('allocation_storehouse_apply', 'state')
        const applyTypeData = await getDict('allocation_storehouse_apply', 'apply_type')

        this.allocationStorehousesInfo.states = stateData
        this.allocationStorehousesInfo.applyTypes = [{
          statusCd: '',
          name: this.$t('allocationStorehouseDetailed.all')
        }].concat(applyTypeData)

        this._initAllocationStorehouseDetailedInfo()
        this._listAllocationStores(this.page.current, this.page.size)
        this._listStorehouses()
        this._listResourceStoreTypes()
        this._listResourceStoreSpecifications()
      } catch (error) {
        console.error('初始化失败:', error)
      }
    },
    _initAllocationStorehouseDetailedInfo() {
      this.allocationStorehousesInfo.conditions.userId = getUserId()
      this.allocationStorehousesInfo.conditions.communityId = this.communityId
    },
    async _listAllocationStores(_page, _rows) {
      try {
        this.allocationStorehousesInfo.conditions.page = _page
        this.allocationStorehousesInfo.conditions.row = _rows
        const params = {
          ...this.allocationStorehousesInfo.conditions,
          resId: this.allocationStorehousesInfo.conditions.resId.trim(),
          resName: this.allocationStorehousesInfo.conditions.resName.trim(),
          applyId: this.allocationStorehousesInfo.conditions.applyId.trim(),
          startUserId: this.allocationStorehousesInfo.conditions.startUserId.trim()
        }

        const { data, total } = await listAllocationStorehouses(params)
        this.allocationStorehousesInfo.resourceStores = data
        this.allocationStorehousesInfo.total = total
        this.page.total = total
      } catch (error) {
        console.error('获取调拨明细失败:', error)
      }
    },
    async _listStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listStorehouses(params)
        this.allocationStorehousesInfo.storehouses = data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    async _listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: '0'
        }
        const { data } = await listResourceStoreTypes(params)
        this.allocationStorehousesInfo.resourceStoreTypes = data
      } catch (error) {
        console.error('获取物品类型失败:', error)
      }
    },
    async _listResourceStoreSonTypes() {
      try {
        this.allocationStorehousesInfo.conditions.rstId = ''
        this.allocationStorehousesInfo.resourceStoreSonTypes = []

        if (!this.allocationStorehousesInfo.conditions.parentRstId) return

        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          parentId: this.allocationStorehousesInfo.conditions.parentRstId
        }
        const { data } = await listResourceStoreTypes(params)
        this.allocationStorehousesInfo.resourceStoreSonTypes = data
      } catch (error) {
        console.error('获取二级分类失败:', error)
      }
    },
    async _listResourceStoreSpecifications() {
      try {
        this.allocationStorehousesInfo.resourceStoreSpecifications = []
        this.allocationStorehousesInfo.conditions.rssId = ''

        if (!this.allocationStorehousesInfo.conditions.rstId) return

        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          rstId: this.allocationStorehousesInfo.conditions.rstId
        }
        const { data } = await listResourceStoreSpecifications(params)
        this.allocationStorehousesInfo.resourceStoreSpecifications = data
      } catch (error) {
        console.error('获取物品规格失败:', error)
      }
    },
    _queryAllocationStorehouseMethod() {
      this.page.current = 1
      this._listAllocationStores(this.page.current, this.page.size)
    },
    _resetAllocationStorehouseMethod() {
      this.allocationStorehousesInfo.conditions = {
        ...this.allocationStorehousesInfo.conditions,
        applyId: "",
        shIda: "",
        shIdz: "",
        startUserId: "",
        resId: "",
        resName: "",
        parentRstId: "",
        rstId: "",
        rssId: "",
        state: "",
        applyType: "",
        startTime: "",
        endTime: ""
      }
      this.allocationStorehousesInfo.resourceStoreSonTypes = []
      this.allocationStorehousesInfo.resourceStoreSpecifications = []
      this.page.current = 1
      this._listAllocationStores(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.allocationStorehousesInfo.moreCondition = !this.allocationStorehousesInfo.moreCondition
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.allocationStorehousesInfo.conditions,
          pagePath: 'allocationStorehouseDetailed'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        // 这里可以添加跳转到下载页面的逻辑
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        this.$message.error(this.$t('allocationStorehouseDetailed.exportError'))
      }
    },
    swatchApplyType(item) {
      this.allocationStorehousesInfo.conditions.applyType = item.statusCd
      this._listAllocationStores(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAllocationStores(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAllocationStores(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.padding-r-0 {
  padding-right: 0 !important;
}

.border-radius {
  border-radius: 4px;
}

.margin-xs-r {
  margin-right: 5px;
}

</style>