<template>
  <div class="store-order-cart-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('storeOrderCartManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.cartId" :placeholder="$t('storeOrderCartManage.search.cartId')" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.state" :placeholder="$t('storeOrderCartManage.search.state')" clearable>
            <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.prodName" :placeholder="$t('storeOrderCartManage.search.prodName')" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.shopId" :placeholder="$t('storeOrderCartManage.search.shop')" clearable>
            <el-option v-for="item in shops" :key="item.shopId" :label="item.shopName" :value="item.shopId" />
          </el-select>
        </el-col>
        <el-col :span="4" >
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 订单列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('storeOrderCartManage.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="cartId" :label="$t('storeOrderCartManage.table.cartId')" align="center" />
        <el-table-column prop="appName" :label="$t('storeOrderCartManage.table.appName')" align="center" />
        <el-table-column prop="shopName" :label="$t('storeOrderCartManage.table.shopName')" align="center" />
        <el-table-column :label="$t('storeOrderCartManage.table.cartType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.cartType === '3307' ? $t('storeOrderCartManage.table.service') :
              $t('storeOrderCartManage.table.goods') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('storeOrderCartManage.table.prodName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.prodName }}
            <span v-if="scope.row.cartType !== '3307'">({{ scope.row.specValue }})</span>
          </template>
        </el-table-column>
        <el-table-column prop="cartNum" :label="$t('storeOrderCartManage.table.cartNum')" align="center" />
        <el-table-column prop="personName" :label="$t('storeOrderCartManage.table.personName')" align="center" />
        <el-table-column prop="payPrice" :label="$t('storeOrderCartManage.table.payPrice')" align="center" />
        <el-table-column prop="stateName" :label="$t('storeOrderCartManage.table.stateName')" align="center" />
        <el-table-column prop="createTime" :label="$t('storeOrderCartManage.table.createTime')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { queryAdminStoreOrderCart, queryShopsByAdmin } from '@/api/mall/storeOrderCartManageApi'

export default {
  name: 'StoreOrderCartManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        cartId: '',
        state: '',
        shopId: '',
        prodName: '',
        mallApiCode: 'queryAdminStoreOrderCartBmoImpl'
      },
      stateOptions: [
        { value: '1001', label: this.$t('storeOrderCartManage.state.unpurchased') },
        { value: '2002', label: this.$t('storeOrderCartManage.state.waitingDelivery') },
        { value: '3003', label: this.$t('storeOrderCartManage.state.waitingReceive') },
        { value: '4004', label: this.$t('storeOrderCartManage.state.waitingComment') },
        { value: '5005', label: this.$t('storeOrderCartManage.state.applyRefund') },
        { value: '6006', label: this.$t('storeOrderCartManage.state.refundSuccess') },
        { value: '7007', label: this.$t('storeOrderCartManage.state.refunded') }
      ],
      shops: [],
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getShops()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryAdminStoreOrderCart(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('storeOrderCartManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getShops() {
      try {
        const params = {
          page: 1,
          row: 100,
          mallApiCode: 'queryShopsByAdminBmoImpl'
        }
        const { data } = await queryShopsByAdmin(params)
        this.shops = data
      } catch (error) {
        this.$message.error(this.$t('storeOrderCartManage.fetchShopsError'))
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.store-order-cart-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>