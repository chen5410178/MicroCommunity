<template>
  <el-dialog :title="$t('chooseResourceStore2.title')" :visible.sync="visible" width="80%" @close="handleClose">
    <el-row>
      <el-col :span="24">

        <el-row :gutter="20">
          <el-col :span="4">
            <el-select v-model="chooseResourceStoreInfo2.shId" disabled style="width:100%">
              <el-option selected value="" :label="$t('chooseResourceStore2.selectWarehouse')"></el-option>
              <el-option v-for="(item, index) in chooseResourceStoreInfo2.storehouses" :key="index" :label="item.shName"
                :value="item.shId">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="chooseResourceStoreInfo2.parentRstId" @change="_listResourceStoreSonTypes"
              style="width:100%">
              <el-option selected value="" :label="$t('chooseResourceStore2.selectItemType')"></el-option>
              <el-option v-for="(item, index) in chooseResourceStoreInfo2.resourceStoreTypes" :key="index"
                :label="item.name" :value="item.rstId">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="chooseResourceStoreInfo2.rstId" style="width:100%;margin-right:10px">
              <el-option selected value="" :label="$t('chooseResourceStore2.selectSubType')"></el-option>
              <el-option v-for="(item, index) in chooseResourceStoreInfo2.resourceStoreSonTypes" :key="index"
                :label="item.name" :value="item.rstId">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-input :placeholder="$t('chooseResourceStore2.inputItemName')"
              v-model.trim="chooseResourceStoreInfo2._currentResourceStoreName"></el-input>
          </el-col>
          <el-col :span="4" style="text-align:right">
            <el-button type="primary" @click="queryResourceStores" style="margin-right:10px">
              <i class="el-icon-search"></i>
              {{ $t('common.search') }}
            </el-button>
            <el-button type="primary" @click="resetResourceStores">
              <i class="el-icon-refresh"></i>
              {{ $t('common.reset') }}
            </el-button>
          </el-col>
        </el-row>

      </el-col>
    </el-row>

    <el-row style="margin-top:15px">
      <el-col :span="24">

        <el-table :data="chooseResourceStoreInfo2.resourceStores" border style="width:100%">
          <el-table-column width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="chooseResourceStoreInfo2.selectResourceStores"
                :label="scope.row.resId"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="shName" :label="$t('chooseResourceStore2.warehouse')" align="center"></el-table-column>
          <el-table-column prop="type" :label="$t('chooseResourceStore2.itemType')" align="center">
            <template slot-scope="scope">
              {{ scope.row.parentRstName ? scope.row.parentRstName : '-' }} >
              {{ scope.row.rstName ? scope.row.rstName : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="resName" :label="$t('chooseResourceStore2.itemName')" align="center"></el-table-column>
          <el-table-column prop="rssName" :label="$t('chooseResourceStore2.itemSpec')" align="center">
            <template slot-scope="scope">
              {{ scope.row.rssName ? scope.row.rssName : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="resCode" :label="$t('chooseResourceStore2.itemCode')" align="center"></el-table-column>
          <el-table-column prop="isFixedName" :label="$t('chooseResourceStore2.fixedItem')"
            align="center"></el-table-column>
          <el-table-column prop="price" :label="$t('chooseResourceStore2.itemPrice')" align="center"></el-table-column>
          <el-table-column prop="stock" :label="$t('chooseResourceStore2.itemStock')" align="center">
            <template slot-scope="scope">
              {{ scope.row.stock }}{{ scope.row.unitCodeName }}
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" style="margin-top:20px"></el-pagination>

        <div v-if="chooseResourceStoreInfo2.resourceStores.length > 0" style="margin-top:20px;text-align:right">
          <el-button type="primary" @click="getSelectResourceStores">
            <i class="el-icon-check"></i>
            {{ $t('common.submit') }}
          </el-button>
          <el-button type="warning" @click="handleClose" style="margin-right:20px">
            <i class="el-icon-close"></i>
            {{ $t('common.cancel') }}
          </el-button>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { listStorehouses, listResourceStoreTypes } from '@/api/resource/addPurchaseApplyApi'
import { listResourceStores } from '@/api/resource/resourceStoreManageApi'

export default {
  name: 'ChooseResourceStore2',
  data() {
    return {
      visible: false,
      chooseResourceStoreInfo2: {
        resourceStores: [],
        selectResourceStores: [],
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        shId: '',
        resOrderType: '',
        storehouses: [],
        resourceStoreTypes: [],
        resourceStoreSonTypes: []
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
      Object.assign(this.chooseResourceStoreInfo2, params)
      this._listStorehouses()
      this._listResourceStoreTypes()
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    handleClose() {
      this.visible = false
      this.chooseResourceStoreInfo2 = {
        resourceStores: [],
        selectResourceStores: [],
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        shId: '',
        resOrderType: '',
        storehouses: [],
        resourceStoreTypes: [],
        resourceStoreSonTypes: []
      }
    },
    async _loadAllResourceStoreInfo(_page, _row) {
      const _resOrderType = this.chooseResourceStoreInfo2.resOrderType
      let _shType = ''
      if (_resOrderType == '20000') {
        _shType = '2807'
      }

      try {
        const res = await listResourceStores({
          page: _page,
          row: _row,
          communityId: "",
          resOrderType: _resOrderType,
          shType: _shType,
          resName: this.chooseResourceStoreInfo2._currentResourceStoreName,
          parentRstId: this.chooseResourceStoreInfo2.parentRstId,
          rstId: this.chooseResourceStoreInfo2.rstId,
          shId: this.chooseResourceStoreInfo2.shId
        })
        const _resourceStoreInfo = res
        this.chooseResourceStoreInfo2.resourceStores = _resourceStoreInfo.resourceStores
        this.page.total = _resourceStoreInfo.records
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listStorehouses() {
      // const _resOrderType = this.chooseResourceStoreInfo2.resOrderType
      // let _shType = ''
      // if (_resOrderType == '20000') {
      //   _shType = '2807'
      // }

      try {
        const res = await listStorehouses({
          page: 1,
          row: 100,
          communityId: "",
          allowPurchase: 'ON'
        })
        this.chooseResourceStoreInfo2.storehouses = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listResourceStoreTypes() {
      try {
        const res = await listResourceStoreTypes({
          page: 1,
          row: 100,
          communityId: "",
          parentId: '0'
        })
        this.chooseResourceStoreInfo2.resourceStoreTypes = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listResourceStoreSonTypes() {
      this.chooseResourceStoreInfo2.rstId = ''
      this.chooseResourceStoreInfo2.resourceStoreSonTypes = []
      if (this.chooseResourceStoreInfo2.parentRstId == '') {
        return
      }

      try {
        const res = await listResourceStoreTypes({
          page: 1,
          row: 100,
          communityId: "",
          parentId: this.chooseResourceStoreInfo2.parentRstId
        })
        this.chooseResourceStoreInfo2.resourceStoreSonTypes = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    queryResourceStores() {
      this.page.current = 1
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    resetResourceStores() {
      this.chooseResourceStoreInfo2.parentRstId = ""
      this.chooseResourceStoreInfo2.rstId = ""
      this.chooseResourceStoreInfo2._currentResourceStoreName = ""
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    getSelectResourceStores() {
      const selectResourceStores = this.chooseResourceStoreInfo2.selectResourceStores
      const resourceStores = this.chooseResourceStoreInfo2.resourceStores
      if (selectResourceStores.length < 1) {
        this.$message.warning(this.$t('chooseResourceStore2.selectItemsRequired'))
        return
      }

      let _resourceStores = []
      for (let i = 0; i < selectResourceStores.length; i++) {
        for (let j = 0; j < resourceStores.length; j++) {
          if (selectResourceStores[i] == resourceStores[j].resId) {
            resourceStores[j].remark = ''
            _resourceStores.push(resourceStores[j])
          }
        }
      }

      this.$emit('setSelectResourceStores', _resourceStores)
      this.handleClose()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadAllResourceStoreInfo(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
</style>