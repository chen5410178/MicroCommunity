<template>
  <div class="asset-inventory-in-stock-container">

    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('assetInventoryInStock.title') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_printAssetInventoryInStock">
            <i class="el-icon-printer"></i> {{ $t('assetInventoryInStock.print') }}
          </el-button>
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i> {{ $t('assetInventoryInStock.back') }}
          </el-button>
        </div>
      </div>

      <el-row>
        <el-col :span="24">
          <el-form label-width="120px">
            <el-row :gutter="20" class="margin-bottom">
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.inventoryName')">
                  <el-input v-model="assetInventoryInStockInfo.name" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.inventoryPerson')">
                  <el-input v-model="assetInventoryInStockInfo.staffName" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="margin-bottom">
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.warehouse')">
                  <el-input v-model="assetInventoryInStockInfo.shName" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.inventoryDate')">
                  <el-input v-model="assetInventoryInStockInfo.invTime" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="margin-bottom">
              <el-col :span="24">
                <el-form-item :label="$t('assetInventoryInStock.remark')">
                  <el-input type="textarea" v-model="assetInventoryInStockInfo.remark" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('assetInventoryInStock.status')">
                  <el-input v-model="assetInventoryInStockInfo.stateName" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-row class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('assetInventoryInStock.inventoryItems') }}</span>
          </div>

          <el-table :data="assetInventoryInStockInfo.storehouses" border style="width: 100%">
            <el-table-column prop="shName" :label="$t('assetInventoryInStock.warehouseName')"
              align="center"></el-table-column>
            <el-table-column :label="$t('assetInventoryInStock.itemType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="resName" :label="$t('assetInventoryInStock.itemName')"
              align="center"></el-table-column>
            <el-table-column prop="specName" :label="$t('assetInventoryInStock.specification')" align="center">
              <template slot-scope="scope">
                {{ scope.row.specName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="resCode" :label="$t('assetInventoryInStock.itemCode')"
              align="center"></el-table-column>
            <el-table-column prop="isFixedName" :label="$t('assetInventoryInStock.isFixedItem')"
              align="center"></el-table-column>
            <el-table-column prop="timesPrice" :label="$t('assetInventoryInStock.batchPrice')"
              align="center"></el-table-column>
            <el-table-column :label="$t('assetInventoryInStock.originalStock')" align="center">
              <template slot-scope="scope">
                {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('assetInventoryInStock.inventoryQuantity')" align="center">
              <template slot-scope="scope">
                {{ scope.row.quantity }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('assetInventoryInStock.remark')" align="center"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row v-if="assetInventoryInStockInfo.state === '3000' || assetInventoryInStockInfo.state === '4000'"
      class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('assetInventoryInStock.auditInfo') }}</span>
          </div>

          <el-form label-width="120px">
            <el-row class="margin-bottom">
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.auditor')">
                  <el-input v-model="assetInventoryInStockInfo.auditName" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.contactNumber')">
                  <el-input v-model="assetInventoryInStockInfo.auditTel" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="margin-bottom">
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.auditTime')">
                  <el-input v-model="assetInventoryInStockInfo.auditTime" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryInStock.auditOpinion')">
                  <el-input v-model="assetInventoryInStockInfo.opinion" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getAssetInventoryList, listAssetInventoryWholeDetail } from '@/api/resource/assetInventoryInStockApi'

export default {
  name: 'AssetInventoryInStockList',
  data() {
    return {
      assetInventoryInStockInfo: {
        aiId: '',
        shId: '',
        shName: '',
        name: '',
        staffName: '',
        invTime: '',
        storehouses: [],
        remark: '',
        stateName: '',
        opinion: '',
        auditId: '',
        auditName: '',
        auditTel: '',
        auditTime: '',
        state: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.assetInventoryInStockInfo.aiId = this.$route.query.aiId
    this._queryAssetInventory()
    this.queryAssetInventoryProduct()
  },
  methods: {
    _goBack() {
      this.$router.go(-1)
    },
    async _queryAssetInventory() {
      try {
        const params = {
          page: 1,
          row: 1,
          aiId: this.assetInventoryInStockInfo.aiId,
          communityId: this.communityId
        }
        const { data } = await getAssetInventoryList(params)
        if (data && data.length > 0) {
          Object.assign(this.assetInventoryInStockInfo, data[0])
        }
      } catch (error) {
        console.error('Failed to fetch asset inventory:', error)
      }
    },
    async queryAssetInventoryProduct() {
      try {
        const params = {
          page: 1,
          row: 100,
          applyOrderId: this.assetInventoryInStockInfo.aiId,
          communityId: this.communityId
        }
        const { data } = await listAssetInventoryWholeDetail(params)
        data.forEach(item => this._computeData(item))
        this.assetInventoryInStockInfo.storehouses = data
      } catch (error) {
        console.error('Failed to fetch inventory products:', error)
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
    _printAssetInventoryInStock() {
      window.open(`/#/pages/property/printAssetInventoryInStock?aiId=${this.assetInventoryInStockInfo.aiId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-in-stock-container {
  padding: 20px;

  .mt-20 {
    margin-top: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .el-form-item {
    margin-bottom: 0;
  }
}
</style>