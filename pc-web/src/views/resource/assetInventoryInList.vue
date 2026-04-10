<template>
  <div class="asset-inventory-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('assetInventory.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">

            <el-form :model="assetInventoryInInfo" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventory.name')" prop="name">
                    <el-input v-model="assetInventoryInInfo.name" :placeholder="$t('assetInventory.namePlaceholder')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventory.staffName')" prop="staffName">
                    <el-input v-model="assetInventoryInInfo.staffName"
                      :placeholder="$t('assetInventory.staffNamePlaceholder')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventory.storehouse')" prop="shId">
                    <el-select v-model="assetInventoryInInfo.shId"
                      :placeholder="$t('assetInventory.storehousePlaceholder')" style="width:100%"
                      @change="selectResourceStores">
                      <el-option v-for="item in assetInventoryInInfo.storehouses" :key="item.shId" :label="item.shName"
                        :value="item.shId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventory.invTime')" prop="invTime">
                    <el-date-picker v-model="assetInventoryInInfo.invTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                      :placeholder="$t('assetInventory.invTimePlaceholder')" style="width:100%" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('assetInventory.remark')" prop="remark">
                    <el-input v-model="assetInventoryInInfo.remark" type="textarea" :rows="3"
                      :placeholder="$t('assetInventory.remarkPlaceholder')" maxlength="250" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>

        </el-col>
      </el-row>
    </el-card>
      <el-row :gutter="20" style="margin-top:20px">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('assetInventory.itemInfo') }}</span>
              <el-button v-if="assetInventoryInInfo.index != 2" type="primary" size="small" style="float:right"
                @click="_openSelectResourceStoreInfoModel">
                <i class="el-icon-search"></i>
                {{ $t('assetInventory.selectItem') }}
              </el-button>
            </div>

            <el-table :data="assetInventoryInInfo.resourceStores" border style="width:100%">
              <el-table-column :label="$t('assetInventory.itemType')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="resName" :label="$t('assetInventory.itemName')" align="center" />
              <el-table-column prop="rssName" :label="$t('assetInventory.itemSpec')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.rssName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="resCode" :label="$t('assetInventory.itemCode')" align="center" />
              <el-table-column prop="isFixedName" :label="$t('assetInventory.isFixed')" align="center" />
              <el-table-column :label="$t('assetInventory.referencePrice')" align="center">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.timesId" style="width:100%"
                    @change="_changeTimesId($event, scope.$index)">
                    <el-option value="" :label="$t('assetInventory.selectPrice')" />
                    <el-option v-for="time in scope.row.times" :key="time.timesId" :label="time.price"
                      :value="time.timesId" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventory.stock')" align="center">
                <template slot-scope="scope">
                  {{ _getTimesStock(scope.row) }}{{ scope.row.unitCodeName }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventory.afterQuantity')" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.quantity"
                    :placeholder="$t('assetInventory.afterQuantityPlaceholder')" style="width:70%" :min="1" :controls="false" />
                  <span style="margin-left:5px">{{ scope.row.unitCodeName }}</span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventory.remark')" align="center">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.remark" :placeholder="$t('assetInventory.remark')" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventory.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" @click="_removeSelectResourceStoreItem(scope.row.resId)">
                    <i class="el-icon-delete"></i>
                    {{ $t('assetInventory.remove') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin-top:20px">
        <el-col :span="24" style="text-align:right">
          <el-button type="warning" style="margin-right:20px" @click="_goBack">
            <i class="el-icon-close"></i>
            {{ $t('assetInventory.cancel') }}
          </el-button>
          <el-button type="primary" @click="_doAssetInventory">
            <i class="el-icon-check"></i>
            {{ $t('assetInventory.submit') }}
          </el-button>
        </el-col>
      </el-row>

      <choose-resource-store2 ref="chooseResourceStore2" @setSelectResourceStores="handleSetSelectResourceStores" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStore2 from '@/components/resource/chooseResourceStore2'
import { saveAssetInventory, listStorehouses } from '@/api/resource/assetInventoryInApi'

export default {
  name: 'AssetInventoryInList',
  components: {
    ChooseResourceStore2
  },
  data() {
    return {
      assetInventoryInInfo: {
        resourceStores: [],
        shId: '',
        shName: '',
        invTime: '',
        name: '',
        staffName: '',
        storehouses: [],
        remark: '',
        state: '2000',
        communityId: ''
      }
    }
  },
  created() {
    this.assetInventoryInInfo.communityId = getCommunityId()
    this._listShopHouses()
  },
  methods: {
    async _listShopHouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.assetInventoryInInfo.communityId
        }
        const { data } = await listStorehouses(params)
        this.assetInventoryInInfo.storehouses = data
      } catch (error) {
        console.error('Failed to fetch storehouses:', error)
      }
    },
    async _doAssetInventory() {
      if (!this.doAssetInventoryValidate()) {
        this.$message.error(this.$t('assetInventory.validateError'))
        return
      }

      if (this.assetInventoryInInfo.resourceStores.length < 1) {
        this.$message.error(this.$t('assetInventory.selectItemError'))
        return
      }

      for (const item of this.assetInventoryInInfo.resourceStores) {
        if (!item.timesId) {
          this.$message.error(this.$t('assetInventory.selectPriceError'))
          return
        }
        if (!item.quantity || parseInt(item.quantity) <= 0) {
          this.$message.error(this.$t('assetInventory.quantityError'))
          return
        }
      }

      try {
        const res = await saveAssetInventory(this.assetInventoryInInfo)
        if (res.code === 0) {
          this.$message.success(res.msg)
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('Failed to save asset inventory:', error)
        this.$message.error(this.$t('assetInventory.saveError'))
      }
    },
    doAssetInventoryValidate() {
      const requiredFields = [
        { field: 'name', message: this.$t('assetInventory.nameRequired') },
        { field: 'staffName', message: this.$t('assetInventory.staffNameRequired') },
        { field: 'shId', message: this.$t('assetInventory.storehouseRequired') },
        { field: 'invTime', message: this.$t('assetInventory.invTimeRequired') },
        { field: 'remark', message: this.$t('assetInventory.remarkRequired') }
      ]

      for (const { field, message } of requiredFields) {
        if (!this.assetInventoryInInfo[field]) {
          this.$message.error(message)
          return false
        }
      }
      return true
    },
    _goBack() {
      this.$router.go(-1)
    },
    _openSelectResourceStoreInfoModel() {
      if (!this.assetInventoryInInfo.shId) {
        this.$message.error(this.$t('assetInventory.selectStorehouseFirst'))
        return
      }
      this.$refs.chooseResourceStore2.open({
        shId: this.assetInventoryInInfo.shId,
        unEditFlag: true
      })
    },
    handleSetSelectResourceStores(resourceStores) {
      const oldList = this.assetInventoryInInfo.resourceStores
      const newList = resourceStores.filter(newItem =>
        !oldList.some(oldItem => oldItem.resId === newItem.resId)
      )
      this.assetInventoryInInfo.resourceStores = [...newList, ...oldList]
    },
    selectResourceStores() {
      this.assetInventoryInInfo.resourceStores = []
      const store = this.assetInventoryInInfo.storehouses.find(
        item => item.shId === this.assetInventoryInInfo.shId
      )
      if (store) {
        this.assetInventoryInInfo.shName = store.shName
      }
    },
    _removeSelectResourceStoreItem(resId) {
      this.assetInventoryInInfo.resourceStores = this.assetInventoryInInfo.resourceStores.filter(
        item => item.resId !== resId
      )
      this.$refs.chooseResourceStore2.removeSelectItem(resId)
    },
    _changeTimesId(value, index) {
      const times = this.assetInventoryInInfo.resourceStores[index].times
      const selectedTime = times.find(item => item.timesId === value)
      if (selectedTime) {
        this.assetInventoryInInfo.resourceStores[index].selectedStock = selectedTime.stock
      }
    },
    _getTimesStock(resourceStore) {
      if (!resourceStore.timesId) return "-"
      const time = resourceStore.times.find(item => item.timesId === resourceStore.timesId)
      return time ? time.stock : 0
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-row {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .clearfix {
    font-weight: bold;
    font-size: 16px;
  }
}
</style>