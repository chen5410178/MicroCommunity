<template>
  <div class="shop-community-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('shopCommunity.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.shopName"
            :placeholder="$t('shopCommunity.search.shopName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="searchForm.communityName"
            :placeholder="$t('shopCommunity.search.communityName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.state"
            :placeholder="$t('shopCommunity.search.state')"
            clearable
          >
            <el-option
              :label="$t('shopCommunity.state.passed')"
              value="24002"
            />
            <el-option
              :label="$t('shopCommunity.state.rejected')"
              value="48004"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 小区商铺列表 -->
    <el-card class="list-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('shopCommunity.list.title') }}</span>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="communityId"
          :label="$t('shopCommunity.table.communityId')"
          align="center"
        />
        <el-table-column
          prop="communityName"
          :label="$t('shopCommunity.table.communityName')"
          align="center"
        />
        <el-table-column
          prop="shopName"
          :label="$t('shopCommunity.table.shopName')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('shopCommunity.table.createTime')"
          align="center"
        />
        <el-table-column
          prop="startTime"
          :label="$t('shopCommunity.table.startTime')"
          align="center"
        />
        <el-table-column
          prop="endTime"
          :label="$t('shopCommunity.table.endTime')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('shopCommunity.table.state')"
          align="center"
        />
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import { getShopCommunityList } from '@/api/mall/shopCommunityApi'

export default {
  name: 'ShopCommunityList',
  data() {
    return {
      loading: false,
      searchForm: {
        shopName: '',
        communityName: '',
        state: '24002',
        mallApiCode: 'queryShopCommunityBmoImpl'
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
        const { data, total, records } = await getShopCommunityList(params)
        this.tableData = data
        this.page.total = records || total
      } catch (error) {
        this.$message.error(this.$t('shopCommunity.fetchError'))
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
.shop-community-container {
  padding: 20px;

  .search-card {
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