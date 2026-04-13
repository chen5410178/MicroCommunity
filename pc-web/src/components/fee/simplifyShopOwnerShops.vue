<template>
  <div>
 
    <div>
      <el-table
        :data="simplifyShopOwnerShopsInfo.shops"
        style="margin-top:10px"
        border
        stripe
      >
        <el-table-column prop="shopNum" :label="$t('simplifyShopOwnerShops.shopCode')" align="center"></el-table-column>
        <el-table-column prop="locationName" :label="$t('simplifyShopOwnerShops.location')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyShopOwnerShops.area')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyShopOwnerShops.validity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime || '-' }}<br>~{{ scope.row.endTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('simplifyShopOwnerShops.shopStatus')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyShopOwnerShops.shopOweFee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.oweFee || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyShopOwnerShops.actions')" align="center">
          <template slot-scope="scope">
            <el-button-group v-if="simplifyShopOwnerShopsInfo.currentShopId != scope.row.shopId">
              <el-button size="mini" @click="_toChooseShop(scope.row)">
                {{ $t('simplifyShopOwnerShops.choose') }}
              </el-button>
            </el-button-group>
            <div v-else>
              {{ $t('simplifyShopOwnerShops.currentShop') }}
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16"></el-col>
        <el-col :span="8" class="float-right">
          <span>{{ $t('simplifyShopOwnerShops.subtotalOweFee') }}：{{ simplifyShopOwnerShopsInfo.allOweFeeAmount }}</span>
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
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerShops } from '@/api/shop/shopCreateFeeApi'

export default {
  name: 'SimplifyShopOwnerShops',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      simplifyShopOwnerShopsInfo: {
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
      this.clearSimplifyShopOwnerShopsInfo()
      this.simplifyShopOwnerShopsInfo.ownerId = param.ownerId
      this.simplifyShopOwnerShopsInfo.currentShopId = param.shopId
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
        ownerId: this.simplifyShopOwnerShopsInfo.ownerId
      }

      listOwnerShops(params).then(res => {
        this.simplifyShopOwnerShopsInfo.shops = res.data || []
        this.total = res.total || 0
        this._computeOwnerShopOweFeeAmount()
      }).catch(error => {
        console.error('Failed to load owner shops:', error)
      })
    },
    _toChooseShop(shop) {
      this.$emit('notifyShop', shop)
    },
    _computeOwnerShopOweFeeAmount() {
      let total = 0
      this.simplifyShopOwnerShopsInfo.allOweFeeAmount = 0
      if (!this.simplifyShopOwnerShopsInfo.shops || this.simplifyShopOwnerShopsInfo.shops.length < 1) return
      
      this.simplifyShopOwnerShopsInfo.shops.forEach(shop => {
        if (shop.oweFee) {
          total += parseFloat(shop.oweFee)
        }
      })
      this.simplifyShopOwnerShopsInfo.allOweFeeAmount = total.toFixed(2)
    },
    clearSimplifyShopOwnerShopsInfo() {
      this.simplifyShopOwnerShopsInfo = {
        shops: [],
        ownerId: '',
        currentShopId: '',
        total: 0,
        allOweFeeAmount: 0
      }
    },
    _openBatchPayShopFeeModal() {
      this.$router.push({
        path: '/views/fee/batchPayFeeOrder',
        query: {
          ownerId: this.simplifyShopOwnerShopsInfo.ownerId,
          payerObjType: '4444'
        }
      })
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
