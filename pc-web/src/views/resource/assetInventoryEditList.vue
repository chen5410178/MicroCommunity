<template>
  <div class="asset-inventory-edit-container">


      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('assetInventoryEdit.inventoryInfo') }}</span>
              <div class="card-header-right">
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
            </div>
            <el-form label-width="120px">
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventoryEdit.inventoryName')">
                    <el-input v-model="assetInventoryEditInfo.name" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventoryEdit.inventoryPerson')">
                    <el-input v-model="assetInventoryEditInfo.staffName" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventoryEdit.warehouse')">
                    <el-input v-model="assetInventoryEditInfo.shName" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('assetInventoryEdit.inventoryDate')">
                    <el-input v-model="assetInventoryEditInfo.invTime" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('assetInventoryEdit.remark')">
                    <el-input type="textarea" v-model="assetInventoryEditInfo.remark" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('assetInventoryEdit.inventoryProducts') }}</span>
              <div class="card-header-right">
                <el-button type="primary" size="small" @click="_openChooseProductModal">
                  <i class="el-icon-plus"></i>{{ $t('common.select') }}{{ $t('assetInventoryEdit.products') }}
                </el-button>
              </div>
            </div>

            <el-table :data="assetInventoryEditInfo.resourceStores" border style="width: 100%">
              <el-table-column prop="parentRstName" :label="$t('assetInventoryEdit.productType')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="resName" :label="$t('assetInventoryEdit.productName')" align="center"></el-table-column>
              <el-table-column prop="rssName" :label="$t('assetInventoryEdit.specification')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.rssName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="resCode" :label="$t('assetInventoryEdit.productCode')" align="center"></el-table-column>
              <el-table-column prop="isFixedName" :label="$t('assetInventoryEdit.isFixed')" align="center"></el-table-column>
              <el-table-column :label="$t('assetInventoryEdit.referencePrice')" align="center">
                <template slot-scope="scope">
                  <el-select 
                    v-model="scope.row.timesId" 
                    @change="_changeTimesId($event, scope.$index)"
                    style="width: 100%"
                  >
                    <el-option :value="''" :label="$t('assetInventoryEdit.selectPrice')"></el-option>
                    <el-option
                      v-for="time in scope.row.times"
                      :key="time.timesId"
                      :value="time.timesId"
                      :label="time.price"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventoryEdit.stock')" align="center">
                <template slot-scope="scope">
                  {{ _getTimesStock(scope.row) }}{{ scope.row.unitCodeName }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventoryEdit.afterInventoryQty')" align="center">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.quantity"
                    :placeholder="$t('assetInventoryEdit.requiredQty')"
                    controls-position="right"
                    :min="1"
                    style="width: 80%"
                  ></el-input-number>
                  <span style="margin-left: 5px">{{ scope.row.unitCodeName }}</span>
                </template>
              </el-table-column>
              <el-table-column :label="$t('assetInventoryEdit.remark')" align="center">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remark"
                    :placeholder="$t('assetInventoryEdit.optionalRemark')"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button
                    type="danger"
                    size="mini"
                    @click="_removeSelectResourceStoreItem(scope.row.resId)"
                  >
                    <i class="el-icon-delete"></i>{{ $t('common.delete') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px;">
        <el-col :span="24" style="text-align: right">
          <el-button type="warning" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.cancel') }}
          </el-button>
          <el-button type="primary" @click="_doAssetInventory">
            <i class="el-icon-check"></i>{{ $t('common.submit') }}
          </el-button>
        </el-col>
      </el-row>

      <choose-resource-store2 
        ref="chooseResourceStore2"
        @setSelectResourceStores="handleSelectResourceStores"
      ></choose-resource-store2>

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { 
  getAssetInventoryDetail,
  updateAssetInventory,
  listAssetInventoryWholeDetail
} from '@/api/resource/assetInventoryEditApi'
import ChooseResourceStore2 from '@/components/resource/chooseResourceStore2'

export default {
  name: 'AssetInventoryEditList',
  components: {
    ChooseResourceStore2
  },
  data() {
    return {
      assetInventoryEditInfo: {
        aiId: '',
        shId: '',
        shName: '',
        name: '',
        staffName: '',
        invTime: '',
        shopHouses: [],
        resourceStores: [],
        state: '2000',
        remark: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.assetInventoryEditInfo.aiId = this.$route.query.aiId
    this._queryAssetInventory()
    this.queryAssetInventoryProduct()
  },
  methods: {
    async _queryAssetInventory() {
      try {
        const params = {
          page: 1,
          row: 1,
          aiId: this.assetInventoryEditInfo.aiId,
          shopId: this.communityId
        }
        const { data } = await getAssetInventoryDetail(params)
        Object.assign(this.assetInventoryEditInfo, data[0])
      } catch (error) {
        this.$message.error(this.$t('assetInventoryEdit.fetchError'))
      }
    },
    async queryAssetInventoryProduct() {
      try {
        const params = {
          page: 1,
          row: 100,
          applyOrderId: this.assetInventoryEditInfo.aiId
        }
        const { data } = await listAssetInventoryWholeDetail(params)
        data.forEach(item => {
          this._computeData(item)
        })
        this.assetInventoryEditInfo.resourceStores = data
      } catch (error) {
        this.$message.error(this.$t('assetInventoryEdit.fetchProductError'))
      }
    },
    _computeData(item) {
      item.invProfit = parseInt(item.invQuantity) - parseInt(item.quantity)
      if (item.invProfit < 0) item.invProfit = 0
      
      item.invLoss = parseInt(item.quantity) - parseInt(item.invQuantity)
      if (item.invLoss < 0) item.invLoss = 0
      
      item.invProfitMoney = item.invProfit * parseFloat(item.price)
      item.invLossMoney = item.invLoss * parseFloat(item.price)
    },
    _openChooseProductModal() {
      if (!this.assetInventoryEditInfo.shId) {
        this.$message.warning(this.$t('assetInventoryEdit.selectWarehouseFirst'))
        return
      }
      this.$refs.chooseResourceStore2.open({
        shId: this.assetInventoryEditInfo.shId,
        unEditFlag: true
      })
    },
    handleSelectResourceStores(resourceStores) {
      const oldList = this.assetInventoryEditInfo.resourceStores
      // 过滤重复选择的商品
      const newItems = resourceStores.filter(newItem => 
        !oldList.some(oldItem => oldItem.resId === newItem.resId)
      )
      // 合并已有商品和新添加商品
      this.assetInventoryEditInfo.resourceStores = [...newItems, ...oldList]
    },
    _removeSelectResourceStoreItem(resId) {
      this.assetInventoryEditInfo.resourceStores = this.assetInventoryEditInfo.resourceStores.filter(
        item => item.resId !== resId
      )
      this.$refs.chooseResourceStore2.removeSelectItem(resId)
    },
    _changeTimesId(value, index) {
      const times = this.assetInventoryEditInfo.resourceStores[index].times
      const selectedTime = times.find(item => item.timesId === value)
      if (selectedTime) {
        this.assetInventoryEditInfo.resourceStores[index].selectedStock = selectedTime.stock
      }
    },
    _getTimesStock(resourceStore) {
      if (!resourceStore.timesId) return "-"
      const selectedTime = resourceStore.times.find(item => item.timesId === resourceStore.timesId)
      return selectedTime ? selectedTime.stock : 0
    },
    async _doAssetInventory() {
      const products = this.assetInventoryEditInfo.resourceStores
      this.assetInventoryEditInfo.state = "2000"
      
      if (products.length < 1) {
        this.$message.warning(this.$t('assetInventoryEdit.selectProductsFirst'))
        return
      }

      for (const product of products) {
        if (!product.quantity || parseInt(product.quantity) <= 0) {
          this.$message.warning(this.$t('assetInventoryEdit.fillQty'))
          return
        }
        if (!product.timesId) {
          this.$message.warning(`${product.resName},${this.$t('assetInventoryEdit.selectPrice')}`)
          return
        }
      }

      try {
        const { msg } = await updateAssetInventory(this.assetInventoryEditInfo)
        this.$message.success(msg)
        this.$router.go(-1)
      } catch (error) {
        this.$message.error(error.message || this.$t('assetInventoryEdit.submitError'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-edit-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-right {
    float: right;
  }

  .el-form-item {
    margin-bottom: 0;
  }
}
</style>