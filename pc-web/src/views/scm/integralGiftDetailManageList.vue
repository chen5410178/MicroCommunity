<template>
  <div class="integral-gift-detail-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('integralGiftDetailManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.detailId" :placeholder="$t('integralGiftDetailManage.search.detailId')"
            clearable @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.userNameLike" :placeholder="$t('integralGiftDetailManage.search.userNameLike')"
            clearable @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('integralGiftDetailManage.search.tel')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('integralGiftDetailManage.list.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="detailId" :label="$t('integralGiftDetailManage.table.detailId')" align="center" />
        <el-table-column prop="acctName" :label="$t('integralGiftDetailManage.table.acctName')" align="center" />
        <el-table-column prop="configName" :label="$t('integralGiftDetailManage.table.configName')" align="center" />
        <el-table-column prop="ruleName" :label="$t('integralGiftDetailManage.table.ruleName')" align="center" />
        <el-table-column prop="quantity" :label="$t('integralGiftDetailManage.table.quantity')" align="center" />
        <el-table-column prop="userName" :label="$t('integralGiftDetailManage.table.userName')" align="center" />
        <el-table-column prop="tel" :label="$t('integralGiftDetailManage.table.tel')" align="center" />
        <el-table-column prop="createTime" :label="$t('integralGiftDetailManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { listIntegralGiftDetail } from '@/api/scm/integralGiftDetailManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'IntegralGiftDetailManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        detailId: '',
        userNameLike: '',
        tel: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
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
        const { data, total } = await listIntegralGiftDetail(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('integralGiftDetailManage.fetchError'))
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
    },
    handleDetail(row) {
      // 查看详情逻辑
      console.log(row)  
    }
  }
}
</script>

<style lang="scss" scoped>
.integral-gift-detail-container {
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