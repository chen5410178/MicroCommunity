<template>
  <div class="shop-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('shopManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.shopName" :placeholder="$t('shopManage.search.shopName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.shopType" style="width: 100%;" :placeholder="$t('shopManage.search.storeType')" clearable>
            <el-option v-for="item in shopTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" style="width: 100%;" :placeholder="$t('shopManage.search.state')" clearable>
            <el-option :label="$t('shopManage.state.passed')" value="002" />
            <el-option :label="$t('shopManage.state.rejected')" value="003" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 店铺列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('shopManage.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column :label="$t('shopManage.table.shopLogo')" align="center" width="100">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 5px;"
              :src="scope.row.shopLogo || '/img/noPhoto.jpg'" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="shopName" :label="$t('shopManage.table.shopName')" align="center" />
        <el-table-column prop="shopTypeName" :label="$t('shopManage.table.shopType')" align="center" />
        <el-table-column prop="storeName" :label="$t('shopManage.table.storeName')" align="center" />
        <el-table-column prop="storeAddress" :label="$t('shopManage.table.storeAddress')" align="center" />
        <el-table-column prop="storeTel" :label="$t('shopManage.table.storeTel')" align="center" />
        <el-table-column prop="stateName" :label="$t('shopManage.table.state')" align="center" />
        <el-table-column prop="createTime" :label="$t('shopManage.table.createTime')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <shop-withdraw ref="shopWithdraw" @success="handleSuccess" />
  </div>
</template>

<script>
import { getShopList } from '@/api/mall/shopManageApi'
import {getDict} from '@/api/community/communityApi'
import ShopWithdraw from '@/components/mall/ShopWithdraw'

export default {
  name: 'ShopManageList',
  components: {
    ShopWithdraw
  },
  data() {
    return {
      loading: false,
      searchForm: {
        shopName: '',
        shopType: '',
        state: '',
        mallApiCode: 'queryShopsByAdminBmoImpl'
      },
      shopTypes: [],
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getShopTypes()
    this.getList()
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
        const { data, total } = await getShopList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('shopManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getShopTypes() {
      try {
        const data = await getDict('s_shop','stop_type')
        this.shopTypes = data
      } catch (error) {
        this.$message.error(this.$t('shopManage.fetchShopTypesError'))
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
    },
    handleSuccess() {
      this.getList()
    },
    openShopWithdraw(shop) {
      this.$refs.shopWithdraw.open(shop)
    }
  }
}
</script>

<style lang="scss" scoped>
.shop-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
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