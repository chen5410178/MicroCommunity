<template>
  <div class="asset-inventory-manage-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-wrapper">
            <div v-for="(item, index) in assetInventoryManageInfo.storehouses" :key="index"
              class="type-list-item"
              :class="{ 'selected': assetInventoryManageInfo.conditions.shId == item.shId }"
              @click="swatchStorehouse(item)">
              {{ item.shName }}
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('assetInventoryManage.search.title') }}</span>
              </div>
              <el-row :gutter="20">
                <el-col :span="4">
                  <el-input v-model.trim="assetInventoryManageInfo.conditions.staffName"
                    :placeholder="$t('assetInventoryManage.search.staffName')" clearable />
                </el-col>
                <el-col :span="4">
                  <el-select v-model="assetInventoryManageInfo.conditions.state"
                    :placeholder="$t('assetInventoryManage.search.state')" style="width:100%">
                    <el-option v-for="item in assetInventoryManageInfo.states" :key="item.statusCd" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-date-picker v-model="assetInventoryManageInfo.conditions.startTime" type="datetime"
                    :placeholder="$t('assetInventoryManage.search.startTime')" style="width:100%" />
                </el-col>
                <el-col :span="4">
                  <el-date-picker v-model="assetInventoryManageInfo.conditions.endTime" type="datetime"
                    :placeholder="$t('assetInventoryManage.search.endTime')" style="width:100%" />
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" @click="_queryAssetInventoryMethod">
                    <i class="el-icon-search"></i>
                    {{ $t('common.search') }}
                  </el-button>
                  <el-button @click="_resetAssetInventoryMethod">
                    <i class="el-icon-refresh"></i>
                    {{ $t('common.reset') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>

        <el-row class="margin-top">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('assetInventoryManage.list.title') }}</span>
                <div style="float: right;">
                  <el-button type="primary" size="small" @click="_openAddAssetInventoryModal">
                    <i class="el-icon-plus"></i>
                    {{ $t('assetInventoryManage.list.inventory') }}
                  </el-button>
                  <el-button type="primary" size="small" @click="_exportAssetInventoryManage">
                    <i class="el-icon-download"></i>
                    {{ $t('common.export') }}
                  </el-button>
                </div>
              </div>
              <el-table :data="assetInventoryManageInfo.assetInventorys" border style="width: 100%">
                <el-table-column prop="aiId" :label="$t('assetInventoryManage.table.aiId')" align="center" />
                <el-table-column prop="name" :label="$t('assetInventoryManage.table.name')" align="center" />
                <el-table-column prop="invTime" :label="$t('assetInventoryManage.table.invTime')" align="center" />
                <el-table-column prop="shName" :label="$t('assetInventoryManage.table.shName')" align="center" />
                <el-table-column prop="staffName" :label="$t('assetInventoryManage.table.staffName')" align="center" />
                <el-table-column prop="stateName" :label="$t('assetInventoryManage.table.state')" align="center" />
                <el-table-column prop="createTime" :label="$t('assetInventoryManage.table.createTime')" align="center" />
                <el-table-column :label="$t('common.operation')" align="center" width="300">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="_openInStockAssetInventoryModel(scope.row)">
                      {{ $t('common.detail') }}
                    </el-button>
                    <el-button
                      v-if="scope.row.state == '1000' && scope.row.staffId == assetInventoryManageInfo.curStaffId"
                      size="mini" type="primary" @click="_openEditAssetInventoryModel(scope.row)">
                      {{ $t('common.edit') }}
                    </el-button>
                    <el-button
                      v-if="scope.row.state == '2000' && scope.row.staffId == assetInventoryManageInfo.curStaffId"
                      size="mini" type="warning" @click="_openCancelAssetInventoryModel(scope.row)">
                      {{ $t('assetInventoryManage.list.cancelApply') }}
                    </el-button>
                    <el-button v-if="scope.row.state == '2000'" size="mini" type="success"
                      @click="_openAuditAssetInventoryModel(scope.row)">
                      {{ $t('assetInventoryManage.list.audit') }}
                    </el-button>
                    <el-button
                      v-if="scope.row.state != '4000' && scope.row.staffId == assetInventoryManageInfo.curStaffId"
                      size="mini" type="danger" @click="_openDeleteAssetInventoryModel(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <delete-asset-inventory ref="deleteAssetInventory" @success="handleSuccess" />
    <cancel-asset-inventory ref="cancelAssetInventory" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listAssetInventory, exportAssetInventory, listStorehouses } from '@/api/resource/assetInventoryManageApi'
import DeleteAssetInventory from '@/components/resource/deleteAssetInventory'
import CancelAssetInventory from '@/components/resource/cancelAssetInventory'
import {getUserId} from '@/api/user/userApi'

export default {
  name: 'AssetInventoryManageList',
  components: {
    DeleteAssetInventory,
    CancelAssetInventory
  },
  data() {
    return {
      assetInventoryManageInfo: {
        assetInventorys: [],
        storehouses: [],
        total: 0,
        records: 1,
        moreCondition: false,
        aiId: '',
        states: [],
        conditions: {
          shId: '',
          staffName: '',
          state: '',
          startTime: '',
          endTime: '',
          communityId: ''
        },
        curStaffId: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.assetInventoryManageInfo.curStaffId = getUserId()
      this.assetInventoryManageInfo.conditions.communityId = await getCommunityId()
      this._listShopHouses()
      this._listAssetInventorys(this.page.current, this.page.size)
      this.getDictData()
    },
    async getDictData() {
      try {
        const data = await getDict('asset_inventory', 'state')
        this.assetInventoryManageInfo.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listAssetInventorys(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          ...this.assetInventoryManageInfo.conditions
        }
        const { data, total } = await listAssetInventory(params)
        this.assetInventoryManageInfo.assetInventorys = data
        this.page.total = total
      } catch (error) {
        console.error('获取资产盘点列表失败:', error)
      }
    },
    async _listShopHouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.assetInventoryManageInfo.conditions.communityId
        }
        const { data } = await listStorehouses(params)
        this.assetInventoryManageInfo.storehouses = [
          { shName: this.$t('common.all'), shId: '' },
          ...data
        ]
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    _openAddAssetInventoryModal() {
      this.$router.push('/pages/property/assetInventoryIn')
    },
    _openEditAssetInventoryModel(assetInventory) {
      this.$router.push(`/pages/property/assetInventoryEdit?aiId=${assetInventory.aiId}`)
    },
    _openAuditAssetInventoryModel(assetInventory) {
      this.$router.push(`/pages/property/assetInventoryAudit?aiId=${assetInventory.aiId}`)
    },
    _openInStockAssetInventoryModel(assetInventory) {
      this.$router.push(`/pages/property/assetInventoryInStock?aiId=${assetInventory.aiId}`)
    },
    _openDeleteAssetInventoryModel(assetInventory) {
      this.$refs.deleteAssetInventory.open(assetInventory)
    },
    _openCancelAssetInventoryModel(assetInventory) {
      this.$refs.cancelAssetInventory.open(assetInventory)
    },
    _queryAssetInventoryMethod() {
      this.page.current = 1
      this._listAssetInventorys(this.page.current, this.page.size)
    },
    _resetAssetInventoryMethod() {
      this.assetInventoryManageInfo.conditions = {
        shId: '',
        staffName: '',
        state: '',
        startTime: '',
        endTime: '',
        communityId: this.assetInventoryManageInfo.conditions.communityId
      }
      this._listAssetInventorys(this.page.current, this.page.size)
    },
    async _exportAssetInventoryManage() {
      try {
        const params = {
          ...this.assetInventoryManageInfo.conditions,
          pagePath: 'assetInventoryManage'
        }
        await exportAssetInventory(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    swatchStorehouse(item) {
      this.assetInventoryManageInfo.conditions.shId = item.shId
      this._listAssetInventorys(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAssetInventorys(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAssetInventorys(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listAssetInventorys(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-manage-container {
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

  .right-panel {
    padding-left: 5px;
  }
}

.margin-top {
  margin-top: 20px;
}
</style>