<template>
  <div class="margin-top">
    <el-row>
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input v-model="shopDetailShopInfo.shopNum" :placeholder="$t('shopDetailShop.shopNumberPlaceholder')" />
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_queryOwnerShops">
          <i class="el-icon-search"></i>{{ $t('common.query') }}
        </el-button>
        <el-button type="primary" size="small" @click="_resetOwnerShops">
          <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="shopDetailShopInfo.shops" border style="width: 100%">
        <el-table-column prop="shopNum" :label="$t('shopDetailShop.shopNumber')" align="center">
          <template slot-scope="scope">
            <a href="javascript:void(0);" @click="goToShopDetail(scope.row)">{{ scope.row.shopNum }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="locationName" :label="$t('shopDetailShop.location')" align="center" />
        <el-table-column prop="ownerName" :label="$t('shopDetailShop.owner')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.ownerName">
              <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + scope.row.ownerId">{{ scope.row.ownerName }}</a>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="link" :label="$t('shopDetailShop.contact')" align="center">
          <template slot-scope="scope">
            {{ scope.row.link || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('shopDetailShop.area')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea || '-' }}{{ scope.row.builtUpArea ? '㎡' : '' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('shopDetailShop.state')" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state === '2008'" type="success" size="mini">{{ $t('shopDetailShop.idle') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '2006'" type="warning" size="mini">{{ $t('shopDetailShop.rented') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '2007'" type="info" size="mini">{{ $t('shopDetailShop.sold') }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="margin-top">
        <el-col :span="12" :offset="12">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next, jumper" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryShopsByOwner } from '@/api/shop/shopDetailApi'

export default {
  name: 'ShopDetailShop',
  data() {
    return {
      shopDetailShopInfo: {
        shops: [],
        ownerId: '',
        shopNum: '',
        currentShopId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, currentShopId) {
      this.shopDetailShopInfo.ownerId = ownerId
      this.shopDetailShopInfo.currentShopId = currentShopId
      this._loadOwnerShopsData()
    },
    async _loadOwnerShopsData() {
      try {
        const params = {
          ownerId: this.shopDetailShopInfo.ownerId,
          shopNum: this.shopDetailShopInfo.shopNum,
          page: 1,
          row: 100
        }

        const response = await queryShopsByOwner(params)
        var allShops = response.data || []
        if (this.shopDetailShopInfo.currentShopId) {
          allShops = allShops.filter(function(shop) {
            return shop.shopId !== this.shopDetailShopInfo.currentShopId
          }.bind(this))
        }
        this.pagination.total = allShops.length
        var start = (this.pagination.currentPage - 1) * this.pagination.pageSize
        var end = start + this.pagination.pageSize
        this.shopDetailShopInfo.shops = allShops.slice(start, end)
      } catch (error) {
        console.error('Failed to load owner shops:', error)
      }
    },
    _queryOwnerShops() {
      this.pagination.currentPage = 1
      this._loadOwnerShopsData()
    },
    _resetOwnerShops() {
      this.shopDetailShopInfo.shopNum = ''
      this._queryOwnerShops()
    },
    goToShopDetail(row) {
      this.$router.push({
        path: '/views/shop/shopDetail',
        query: { shopId: row.shopId }
      })
    },
    handleCurrentChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._loadOwnerShopsData()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 20px;
}

.padding-right-xl {
  padding-right: 20px;
}
</style>
