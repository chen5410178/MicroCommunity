<template>
  <div class="animated fadeInRight ecommerce">
    <el-row :gutter="20">
      <el-col :span="4" class="padding-r-0">
        <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in states"  :key="index" @click="swatchState(item)"
                :class="{ 'vc-node-selected':  conditions.state == item.statusCd  }"
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
            <span>{{ $t('allocationStorehouseManage.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="conditions.startUserName"
                :placeholder="$t('allocationStorehouseManage.search.startUserName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.applyType" :placeholder="$t('allocationStorehouseManage.search.applyType')"
                style="width:100%">
                <el-option v-for="(item, index) in applyTypes" :key="index" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.startTime" type="datetime"
                :placeholder="$t('allocationStorehouseManage.search.startTime')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.endTime" type="datetime"
                :placeholder="$t('allocationStorehouseManage.search.endTime')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryAllocationStorehouseMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetAllocationStorehouseMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('allocationStorehouseManage.list.title') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="_openAllocationStorehouseApplyModal">
                <i class="el-icon-plus"></i>
                {{ $t('allocationStorehouseManage.list.allocation') }}
              </el-button>
              <el-button type="primary" size="small" @click="_exportExcel">
                <i class="el-icon-download"></i>
                {{ $t('common.export') }}
              </el-button>
            </div>
          </div>
          <el-table :data="resourceStores" border style="width: 100%" v-loading="loading">
            <el-table-column prop="applyId" :label="$t('allocationStorehouseManage.table.applyId')" align="center" />
            <el-table-column prop="applyCount" :label="$t('allocationStorehouseManage.table.applyCount')"
              align="center" />
            <el-table-column prop="startUserName" :label="$t('allocationStorehouseManage.table.startUserName')"
              align="center" />
            <el-table-column prop="stateName" :label="$t('allocationStorehouseManage.table.stateName')" align="center" />
            <el-table-column prop="applyTypeName" :label="$t('allocationStorehouseManage.table.applyTypeName')"
              align="center" />
            <el-table-column prop="createTime" :label="$t('allocationStorehouseManage.table.createTime')"
              align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state == '1200' && currentUserId == scope.row.startUserId" type="text"
                  size="small" @click="_openDeleteResourceStoreModel(scope.row)">
                  {{ $t('allocationStorehouseManage.operation.cancel') }}
                </el-button>
                <el-button type="text" size="small" @click="_toDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
                <el-button v-if="scope.row.applyType == '10000'" type="text" size="small"
                  @click="_openRunWorkflowImage(scope.row)">
                  {{ $t('allocationStorehouseManage.operation.flowChart') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" class="pagination" />
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage" />
    <allocation-storehouse ref="allocationStorehouse" />
    <delete-storehouse-manage ref="deleteStorehouseManage" />
  </div>
</template>

<script>
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { listAllocationStorehouseApplys, listStorehouses, exportData ,listRunWorkflowImage} from '@/api/resource/allocationStorehouseManageApi'
import ViewImage from '@/components/system/viewImage'
import AllocationStorehouse from '@/components/resource/allocationStorehouse'
import DeleteStorehouseManage from '@/components/resource/deleteStorehouseManage'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'AllocationStorehouseManageList',
  components: {
    ViewImage,
    AllocationStorehouse,
    DeleteStorehouseManage
  },
  data() {
    return {
      loading: false,
      resourceStores: [],
      states: [],
      applyTypes: [],
      currentUserId: '',
      conditions: {
        applyId: '',
        resId: '',
        resName: '',
        resCode: '',
        shIda: '',
        shIdz: '',
        startTime: '',
        endTime: '',
        startUserId: '',
        startUserName: '',
        state: '',
        applyType: '',
        userId: '',
        communityId: ''
      },
      storehouses: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.currentUserId = getUserId()
    this.conditions.userId = this.currentUserId
    this.conditions.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      try {
        // 获取字典数据
        const [states, applyTypes] = await Promise.all([
          getDict('allocation_storehouse_apply', 'state'),
          getDict('allocation_storehouse_apply', 'apply_type')
        ])

        this.states = [{ statusCd: '', name: this.$t('common.all') }, ...states]
        this.applyTypes = applyTypes

        // 获取仓库列表
        const storehouseRes = await listStorehouses({
          page: 1,
          row: 100,
          communityId: this.conditions.communityId
        })
        this.storehouses = storehouseRes.data

        // 获取调拨记录
        this._listAllocationStorehouses()
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async _listAllocationStorehouses() {
      try {
        this.loading = true
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }

        const res = await listAllocationStorehouseApplys(params)
        this.resourceStores = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('获取调拨记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    _queryAllocationStorehouseMethod() {
      this.page.current = 1
      this._listAllocationStorehouses()
    },
    _resetAllocationStorehouseMethod() {
      this.conditions = {
        applyId: '',
        resId: '',
        resName: '',
        resCode: '',
        shIda: '',
        shIdz: '',
        startTime: '',
        endTime: '',
        startUserId: '',
        startUserName: '',
        state: '',
        applyType: '',
        userId: this.currentUserId,
        communityId: this.conditions.communityId
      }
      this._listAllocationStorehouses()
    },
    _openDeleteResourceStoreModel(row) {
      this.$refs.deleteStorehouseManage.open(row)
    },
    _toDetail(item) {
      this.$router.push({
        path: '/pages/common/allocationStorehouseDetail',
        query: {
          applyId: item.applyId,
          applyType: item.applyType,
          applyTypeName: item.applyTypeName
        }
      })
    },
    _openAllocationStorehouseApplyModal() {
      this.$router.push('/pages/common/allocationStorehouseApply')
    },
    async _openRunWorkflowImage(purchaseApply) {
      try {
        const res = await listRunWorkflowImage({
          communityId: this.conditions.communityId,
          businessKey: purchaseApply.applyId
        })

        if (res.code !== '0') {
          this.$message.error(res.msg)
          return
        }

        this.$refs.viewImage.open({
          url: 'data:image/png;base64,' + res.data
        })
      } catch (error) {
        console.error('获取流程图失败:', error)
      }
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.conditions,
          pagePath: 'allocationStorehouseManage'
        }

        const res = await exportData(params)
        this.$message.success(res.msg)

        if (res.code === 0) {
          this.$router.push({
            path: '/pages/property/downloadTempFile',
            query: { tab: this.$t('common.downloadCenter') }
          })
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    swatchState(item) {
      this.conditions.state = item.statusCd
      this._listAllocationStorehouses()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAllocationStorehouses()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAllocationStorehouses()
    }
  }
}
</script>

<style lang="scss" scoped>
.ecommerce {
  padding: 20px;
}

.padding-r-0 {
  padding-right: 0 !important;
}

.border-radius {
  border-radius: 4px;
}

.margin-xs-r {
  margin-right: 5px;
}



.text-center {
  text-align: center;
}

.margin-top {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>