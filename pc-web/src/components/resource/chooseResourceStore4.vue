<template>
  <el-dialog :title="$t('chooseResourceStore4.title')" :visible.sync="visible" width="80%" @close="handleClose">
    <div class="filter-container">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-select v-model="chooseResourceStoreInfo4.shId" disabled style="width:100%">
            <el-option value="" :label="$t('chooseResourceStore4.selectWarehouse')" />
            <el-option v-for="(item, index) in chooseResourceStoreInfo4.storehouses" :key="index" :label="item.shName"
              :value="item.shId" />
          </el-select>
        </el-col>

        <el-col :span="4">
          <el-select v-model="chooseResourceStoreInfo4.parentRstId" @change="_listResourceStoreSonTypes"
            style="width:100%">
            <el-option value="" :label="$t('chooseResourceStore4.selectItemType')" />
            <el-option v-for="(item, index) in chooseResourceStoreInfo4.resourceStoreTypes" :key="index" :label="item.name"
              :value="item.rstId" />
          </el-select>
        </el-col>

        <el-col :span="4">
          <el-select v-model="chooseResourceStoreInfo4.rstId" style="width:100%;margin-right:10px">
            <el-option value="" :label="$t('chooseResourceStore4.selectSubType')" />
            <el-option v-for="(item, index) in chooseResourceStoreInfo4.resourceStoreSonTypes" :key="index"
              :label="item.name" :value="item.rstId" />
          </el-select>
        </el-col>
4
        <el-col :span="4">
          <el-input v-model.trim="chooseResourceStoreInfo4._currentResourceStoreName"
            :placeholder="$t('chooseResourceStore4.inputItemName')" />
        </el-col>
        <el-col :span="4" style="text-align:right">
          <el-button type="primary" @click="queryResourceStores">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="resetResourceStores">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="chooseResourceStoreInfo4.resourceStores" border style="width:100%;margin-top:15px">
      <el-table-column width="50">
        <template slot-scope="scope">
          <el-checkbox v-model="chooseResourceStoreInfo4.selectResourceStores" :label="scope.row.resId" />
        </template>
      </el-table-column>

      <el-table-column prop="shName" :label="$t('chooseResourceStore4.warehouse')" align="center" />

      <el-table-column :label="$t('chooseResourceStore4.itemType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
        </template>
      </el-table-column>

      <el-table-column prop="resName" :label="$t('chooseResourceStore4.itemName')" align="center" />

      <el-table-column prop="rssName" :label="$t('chooseResourceStore4.spec')" align="center">
        <template slot-scope="scope">
          {{ scope.row.rssName || '-' }}
        </template>
      </el-table-column>

      <el-table-column prop="resCode" :label="$t('chooseResourceStore4.code')" align="center" />

      <el-table-column prop="isFixedName" :label="$t('chooseResourceStore4.fixedItem')" align="center" />

      <el-table-column prop="price" :label="$t('chooseResourceStore4.price')" align="center" />

      <el-table-column :label="$t('chooseResourceStore4.stock')" align="center">
        <template slot-scope="scope">
          {{ scope.row.stock }}{{ scope.row.unitCodeName }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" style="margin-top:20px" />

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="getSelectResourceStores">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import * as api from '@/api/resource/urgentPurchaseApplyStepApi'

export default {
  name: 'ChooseResourceStore4',
  props: {
    resOrderType: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      chooseResourceStoreInfo4: {
        resourceStores: [],
        selectResourceStores: [],
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        shId: '',
        storehouses: [],
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        resOrderType: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.visible = true
      this.chooseResourceStoreInfo4.shId = params.shId
      this.chooseResourceStoreInfo4.resOrderType = this.resOrderType
      this._initData()
    },
    async _initData() {
      this.chooseResourceStoreInfo4._currentResourceStoreName = ''
      await this._listStorehouses()
      await this._listResourceStoreTypes()
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    handleClose() {
      this.chooseResourceStoreInfo4.selectResourceStores = []
    },
    async _loadAllResourceStoreInfo(page, size) {
      try {
        let shType = ''
        if (this.chooseResourceStoreInfo4.resOrderType === '20000') {
          shType = '2807'
        }

        const params = {
          page,
          row: size,
          resOrderType: this.chooseResourceStoreInfo4.resOrderType,
          shType,
          resName: this.chooseResourceStoreInfo4._currentResourceStoreName,
          parentRstId: this.chooseResourceStoreInfo4.parentRstId,
          rstId: this.chooseResourceStoreInfo4.rstId,
          shId: this.chooseResourceStoreInfo4.shId
        }

        const { resourceStores, total } = await api.listResourceStores(params)
        this.chooseResourceStoreInfo4.resourceStores = resourceStores
        this.page.total = total
      } catch (error) {
        console.error('获取物品列表失败:', error)
      }
    },
    async _listStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          allowPurchase: 'ON'
        }
        const { data } = await api.listStorehouses(params)
        this.chooseResourceStoreInfo4.storehouses = data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    async _listResourceStoreTypes() {
      try {
        const communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 100,
          communityId,
          parentId: '0'
        }
        const { data } = await api.listResourceStoreTypes(params)
        this.chooseResourceStoreInfo4.resourceStoreTypes = data
      } catch (error) {
        console.error('获取物品类型列表失败:', error)
      }
    },
    async _listResourceStoreSonTypes() {
      this.chooseResourceStoreInfo4.rstId = ''
      if (!this.chooseResourceStoreInfo4.parentRstId) {
        this.chooseResourceStoreInfo4.resourceStoreSonTypes = []
        return
      }

      try {
        const communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 100,
          communityId,
          parentId: this.chooseResourceStoreInfo4.parentRstId
        }
        const { data } = await api.listResourceStoreTypes(params)
        this.chooseResourceStoreInfo4.resourceStoreSonTypes = data
      } catch (error) {
        console.error('获取物品子类型列表失败:', error)
      }
    },
    queryResourceStores() {
      this.page.current = 1
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    resetResourceStores() {
      this.chooseResourceStoreInfo4._currentResourceStoreName = ''
      this.chooseResourceStoreInfo4.parentRstId = ''
      this.chooseResourceStoreInfo4.rstId = ''
      this.queryResourceStores()
    },
    getSelectResourceStores() {
      const selectResourceStores = this.chooseResourceStoreInfo4.selectResourceStores
      const resourceStores = this.chooseResourceStoreInfo4.resourceStores

      if (selectResourceStores.length < 1) {
        this.$message.warning(this.$t('chooseResourceStore4.selectItemsRequired'))
        return
      }

      const _resourceStores = []
      selectResourceStores.forEach(resId => {
        const item = resourceStores.find(r => r.resId === resId)
        if (item) {
          item.remark = ''
          _resourceStores.push(item)
        }
      })

      this.$emit('setSelectResourceStores', _resourceStores)
      this.visible = false
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadAllResourceStoreInfo(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadAllResourceStoreInfo(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}
</style>