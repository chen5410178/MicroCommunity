<template>
  <div class="asset-inventory-audit-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('assetInventoryAudit.inventoryInfo') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="_goBack()">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <el-row>
        <el-col :span="24">
          <el-form label-width="120px" class="text-left">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryAudit.inventoryName')">
                  <span>{{ assetInventoryAuditInfo.name }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryAudit.inventoryPerson')">
                  <span>{{ assetInventoryAuditInfo.staffName }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryAudit.warehouse')">
                  <span>{{ assetInventoryAuditInfo.shName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('assetInventoryAudit.inventoryDate')">
                  <span>{{ assetInventoryAuditInfo.invTime }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('assetInventoryAudit.remark')">
                  <span>{{ assetInventoryAuditInfo.remark }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>{{ $t('assetInventoryAudit.inventoryProducts') }}</span>
      </div>
      <el-table :data="assetInventoryAuditInfo.resourceStores" border style="width: 100%">
        <el-table-column prop="parentRstName" :label="$t('assetInventoryAudit.productType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName ? scope.row.parentRstName : '-' }} >
            {{ scope.row.rstName ? scope.row.rstName : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="resName" :label="$t('assetInventoryAudit.productName')" align="center">
        </el-table-column>
        <el-table-column prop="rssName" :label="$t('assetInventoryAudit.specification')" align="center">
          <template slot-scope="scope">
            {{ scope.row.rssName ? scope.row.rssName : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="resCode" :label="$t('assetInventoryAudit.productCode')" align="center">
        </el-table-column>
        <el-table-column prop="isFixedName" :label="$t('assetInventoryAudit.isFixed')" align="center">
        </el-table-column>
        <el-table-column prop="price" :label="$t('assetInventoryAudit.referencePrice')" align="center">
        </el-table-column>
        <el-table-column prop="originalStock" :label="$t('assetInventoryAudit.originalStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.originalStock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" :label="$t('assetInventoryAudit.afterInventory')" align="center">
          <template slot-scope="scope">
            {{ scope.row.quantity }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('assetInventoryAudit.remark')" align="center">
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>{{ $t('assetInventoryAudit.auditOpinion') }}</span>
      </div>
      <el-input type="textarea" :rows="4" v-model="assetInventoryAuditInfo.opinion"
        :placeholder="$t('assetInventoryAudit.opinionPlaceholder')" maxlength="250" show-word-limit>
      </el-input>
      <div style="margin-top: 20px; text-align: center;">
        <el-button type="primary" @click="_doAssetInventory('4000')">
          <i class="el-icon-check"></i>{{ $t('assetInventoryAudit.approve') }}
        </el-button>
        <el-button type="warning" style="margin-left: 20px;" @click="_doAssetInventory('3000')">
          <i class="el-icon-close"></i>{{ $t('assetInventoryAudit.reject') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAssetInventoryDetail, updateAssetInventory ,getAssetInventoryProduct} from '@/api/resource/assetInventoryAuditApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AssetInventoryAuditList',
  data() {
    return {
      assetInventoryAuditInfo: {
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
        opinion: '',
        communityId: getCommunityId()
      }
    }
  },
  created() {
    this.assetInventoryAuditInfo.aiId = this.$route.query.aiId
    this._queryAssetInventory()
    this.queryAssetInventoryProduct()
  },
  methods: {
    async _doAssetInventory(state) {
      if (!this.assetInventoryAuditInfo.opinion) {
        this.$message.error(this.$t('assetInventoryAudit.opinionRequired'))
        return
      }
      this.assetInventoryAuditInfo.state = state
      try {
        const res = await updateAssetInventory(this.assetInventoryAuditInfo)
        if (res.code === 0) {
          this.$message.success(res.msg)
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.requestFailed'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    async _queryAssetInventory() {
      try {
        const params = {
          page: 1,
          row: 1,
          aiId: this.assetInventoryAuditInfo.aiId,
          shopId: this.assetInventoryAuditInfo.communityId
        }
        const res = await getAssetInventoryDetail(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.assetInventoryAuditInfo, res.data[0])
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async queryAssetInventoryProduct() {
      try {
        const params = {
          page: 1,
          row: 100,
          applyOrderId: this.assetInventoryAuditInfo.aiId
        }
        const res = await getAssetInventoryProduct(params)
        if (res.data) {
          res.data.forEach(item => {
            this._computeData(item)
          })
          this.assetInventoryAuditInfo.resourceStores = res.data
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _computeData(item) {
      item.invProfit = parseInt(item.invQuantity) - parseInt(item.quantity)
      if (item.invProfit < 0) {
        item.invProfit = 0
      }
      item.invLoss = parseInt(item.quantity) - parseInt(item.invQuantity)
      if (item.invLoss < 0) {
        item.invLoss = 0
      }
      item.invProfitMoney = item.invProfit * parseFloat(item.price)
      item.invLossMoney = item.invLoss * parseFloat(item.price)
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-audit-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .el-form-item {
    margin-bottom: 0;

    span {
      display: inline-block;
      padding: 0 10px;
    }
  }
}
</style>