<template>
  <div class="store-order-cart-return-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('storeOrderCartReturn.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.cartId" :placeholder="$t('storeOrderCartReturn.search.cartId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('storeOrderCartReturn.search.state')" clearable>
            <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.prodName" :placeholder="$t('storeOrderCartReturn.search.prodName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 退货订单列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('storeOrderCartReturn.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="cartId" :label="$t('storeOrderCartReturn.table.cartId')" align="center" />
        <el-table-column prop="prodName" :label="$t('storeOrderCartReturn.table.prodName')" align="center" />
        <el-table-column prop="specValue" :label="$t('storeOrderCartReturn.table.specValue')" align="center" />
        <el-table-column prop="personName" :label="$t('storeOrderCartReturn.table.personName')" align="center" />
        <el-table-column prop="returnPayPrice" :label="$t('storeOrderCartReturn.table.returnPayPrice')" align="center" />
        <el-table-column prop="stateName" :label="$t('storeOrderCartReturn.table.stateName')" align="center" />
        <el-table-column prop="returnReason" :label="$t('storeOrderCartReturn.table.returnReason')" align="center" />
        <el-table-column prop="createTime" :label="$t('storeOrderCartReturn.table.createTime')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { getStoreOrderCartReturnList } from '@/api/mall/storeOrderCartReturnApi'

export default {
  name: 'StoreOrderCartReturnList',
  data() {
    return {
      loading: false,
      searchForm: {
        cartId: '',
        state: '',
        prodName: '',
        mallApiCode: 'queryStoreOrderCartReturnBmoImpl'
      },
      stateOptions: [
        { value: '', label: this.$t('storeOrderCartReturn.state.all') },
        { value: '5005', label: this.$t('storeOrderCartReturn.state.applyRefund') },
        { value: '6006', label: this.$t('storeOrderCartReturn.state.returnSuccess') },
        { value: '7007', label: this.$t('storeOrderCartReturn.state.refunded') }
      ],
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
        const { data, total } = await getStoreOrderCartReturnList(params)
        this.tableData = data.map(item => {
          let specValue = ''
          if (item.productSpecDetails) {
            item.productSpecDetails.forEach(detail => {
              specValue += (detail.detailValue + '/')
            })
          }
          return {
            ...item,
            specValue
          }
        })
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('storeOrderCartReturn.fetchError'))
      } finally {
        this.loading = false
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
.store-order-cart-return-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-select {
      width: 100%;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>