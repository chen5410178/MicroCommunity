<template>
  <div class="merchant-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper ">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('merchantManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.storeId" :placeholder="$t('merchantManage.search.storeId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('merchantManage.search.name')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('merchantManage.search.tel')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 商家列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('merchantManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleManage">
          {{ $t('merchantManage.list.manage') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="storeId" :label="$t('merchantManage.table.storeId')" align="center" />
        <el-table-column prop="name" :label="$t('merchantManage.table.name')" align="center" />
        <el-table-column prop="address" :label="$t('merchantManage.table.address')" align="center" />
        <el-table-column prop="tel" :label="$t('merchantManage.table.tel')" align="center" />
        <el-table-column prop="createTime" :label="$t('merchantManage.table.createTime')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { queryMerchant } from '@/api/mall/merchantManageApi'

export default {
  name: 'MerchantManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        storeId: '',
        name: '',
        tel: '',
        mallApiCode: 'queryMerchantBmoImpl'
      },
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
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await queryMerchant(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('merchantManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        storeId: '',
        name: '',
        tel: '',
        mallApiCode: 'queryMerchantBmoImpl'
      }
      this.page.current = 1
      this.getList()
    },
    handleManage() {
      this.$router.push('/mall/merchantManage')
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
.merchant-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>