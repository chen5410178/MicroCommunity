<template>
  <div>
    <el-table
      :data="simplifyOwnerShopsInfo.shops"
      style="margin-top:10px"
      border
      stripe
    >
      <el-table-column prop="shopNum" :label="$t('simplifyOwnerShops.shopCode')" align="center"></el-table-column>
      <el-table-column prop="locationName" :label="$t('simplifyOwnerShops.location')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerShops.area')" align="center">
        <template slot-scope="scope">
          {{ scope.row.builtUpArea || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('simplifyOwnerShops.validity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime || '-' }}<br>~{{ scope.row.endTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyOwnerShops.shopStatus')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerShops.shopOweFee')" align="center">
        <template slot-scope="scope">
          {{ scope.row.oweFee || '0.00' }}
        </template>
      </el-table-column>
    </el-table>
    <el-row>
      <el-col :span="16"></el-col>
      <el-col :span="8" class="float-right">
        <span>{{ $t('simplifyOwnerShops.subtotalOweFee') }}：{{ simplifyOwnerShopsInfo.allOweFeeAmount }}</span>
      </el-col>
    </el-row>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerShops } from '@/api/shop/shopCreateFeeApi'

export default {
  name: 'SimplifyOwnerShops',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      simplifyOwnerShopsInfo: {
        shops: [],
        ownerId: '',
        currentShopId: '',
        total: 0,
        allOweFeeAmount: 0
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleSwitch(param) {
      if (!param.ownerId) return
      this.clearSimplifyOwnerShopsInfo()
      this.simplifyOwnerShopsInfo.ownerId = param.ownerId
      this.simplifyOwnerShopsInfo.currentShopId = param.shopId
      this._listOwnerShops(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this._listOwnerShops(val, this.DEFAULT_ROWS)
    },
    _listOwnerShops(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        ownerId: this.simplifyOwnerShopsInfo.ownerId
      }

      listOwnerShops(params).then(res => {
        this.simplifyOwnerShopsInfo.shops = res.data || []
        this.total = res.total || 0
        this._computeOwnerShopOweFeeAmount()
      }).catch(function(error) {
        console.error('Failed to load owner shops:', error)
      })
    },
    _toChooseShop(shop) {
      this.$emit('notifyShop', shop)
    },
    _computeOwnerShopOweFeeAmount() {
      let total = 0
      this.simplifyOwnerShopsInfo.allOweFeeAmount = 0
      if (!this.simplifyOwnerShopsInfo.shops || this.simplifyOwnerShopsInfo.shops.length < 1) return
      
      this.simplifyOwnerShopsInfo.shops.forEach(function(shop) {
        if (shop.oweFee) {
          total += parseFloat(shop.oweFee)
        }
      })
      this.simplifyOwnerShopsInfo.allOweFeeAmount = total.toFixed(2)
    },
    clearSimplifyOwnerShopsInfo() {
      this.simplifyOwnerShopsInfo = {
        shops: [],
        ownerId: '',
        currentShopId: '',
        total: 0,
        allOweFeeAmount: 0
      }
    },
    open(params) {
      this.handleSwitch(params)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.float-right {
  float: right;
}
.text-right {
  text-align: right;
}
</style>