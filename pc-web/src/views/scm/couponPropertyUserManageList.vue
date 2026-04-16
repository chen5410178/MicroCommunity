<template>
  <div class="coupon-property-user-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyUserManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.couponId" :placeholder="$t('couponPropertyUserManage.search.couponId')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.couponName" :placeholder="$t('couponPropertyUserManage.search.couponName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.validityDay" :placeholder="$t('couponPropertyUserManage.search.validityDay')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.couponUserName" :placeholder="$t('couponPropertyUserManage.search.userName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.tel" :placeholder="$t('couponPropertyUserManage.search.tel')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 15px;">

        <el-col :span="4">
          <el-select v-model="searchForm.toType" :placeholder="$t('couponPropertyUserManage.search.toType')"
            style="width:100%">
            <el-option :label="$t('couponPropertyUserManage.search.selectToType')" value="" />
            <el-option :label="$t('couponPropertyUserManage.search.shopping')" value="1001" />
            <el-option :label="$t('couponPropertyUserManage.search.payment')" value="2002" />
            <el-option :label="$t('couponPropertyUserManage.search.repair')" value="3003" />
            <el-option :label="$t('couponPropertyUserManage.search.parking')" value="4004" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.state" :placeholder="$t('couponPropertyUserManage.search.state')"
            style="width:100%">
            <el-option :label="$t('couponPropertyUserManage.search.selectState')" value="" />
            <el-option :label="$t('couponPropertyUserManage.search.unused')" value="1001" />
            <el-option :label="$t('couponPropertyUserManage.search.used')" value="2002" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyUserManage.list.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="couponId" :label="$t('couponPropertyUserManage.table.couponId')" align="center" />
        <el-table-column prop="couponName" :label="$t('couponPropertyUserManage.table.couponName')" align="center" />
        <el-table-column prop="value" :label="$t('couponPropertyUserManage.table.value')" align="center" />
        <el-table-column prop="validityDay" :label="$t('couponPropertyUserManage.table.validityDay')" align="center" />
        <el-table-column prop="userName" :label="$t('couponPropertyUserManage.table.userName')" align="center" />
        <el-table-column prop="tel" :label="$t('couponPropertyUserManage.table.tel')" align="center" />
        <el-table-column prop="toTypeName" :label="$t('couponPropertyUserManage.table.toType')" align="center" />
        <el-table-column prop="stock" :label="$t('couponPropertyUserManage.table.quantity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ $t('couponPropertyUserManage.table.sheet') }}
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('couponPropertyUserManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === '1001' ? $t('couponPropertyUserManage.table.unused') :
              $t('couponPropertyUserManage.table.used') }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('couponPropertyUserManage.table.startTime')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { listCouponPropertyUser } from '@/api/scm/couponPropertyUserManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponPropertyUserManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        couponId: '',
        couponName: '',
        validityDay: '',
        couponUserName: '',
        tel: '',
        toType: '',
        state: '',
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
        const { data, total, records } = await listCouponPropertyUser(params)
        this.tableData = data
        this.page.total = records || total
      } catch (error) {
        this.$message.error(this.$t('couponPropertyUserManage.fetchError'))
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
.coupon-property-user-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 15px;
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