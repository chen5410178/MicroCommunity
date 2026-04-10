<template>
  <div class="coupon-pool-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('couponPoolManage.search.title') }}</span>
      </div>
      <el-row :gutter="20" class="padding-bottom">
        <el-col :span="4">
          <el-input v-model="searchForm.couponName" :placeholder="$t('couponPoolManage.search.couponName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.validityDay" :placeholder="$t('couponPoolManage.search.validityDay')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 优惠券池信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('couponPoolManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          {{ $t('couponPoolManage.list.create') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="poolId" :label="$t('couponPoolManage.table.poolId')" align="center" />
        <el-table-column prop="couponTypeName" :label="$t('couponPoolManage.table.couponType')" align="center" />
        <el-table-column prop="couponName" :label="$t('couponPoolManage.table.couponName')" align="center" />
        <el-table-column prop="actualPrice" :label="$t('couponPoolManage.table.actualPrice')" align="center" />
        <el-table-column prop="buyPrice" :label="$t('couponPoolManage.table.buyPrice')" align="center" />
        <el-table-column prop="couponStock" :label="$t('couponPoolManage.table.couponStock')" align="center" />
        <el-table-column prop="validityDay" :label="$t('couponPoolManage.table.validityDay')" align="center" />
        <el-table-column prop="seq" :label="$t('couponPoolManage.table.seq')" align="center" />
        <el-table-column :label="$t('couponPoolManage.table.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('couponPoolManage.table.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-coupon-pool ref="addCouponPool" @success="handleSuccess" />
    <edit-coupon-pool ref="editCouponPool" @success="handleSuccess" />
    <delete-coupon-pool ref="deleteCouponPool" @success="handleSuccess" />
  </div>
</template>

<script>
import { listCouponPool } from '@/api/account/couponPoolManageApi'
import AddCouponPool from '@/components/account/AddCouponPool'
import EditCouponPool from '@/components/account/EditCouponPool'
import DeleteCouponPool from '@/components/account/DeleteCouponPool'

export default {
  name: 'CouponPoolManageList',
  components: {
    AddCouponPool,
    EditCouponPool,
    DeleteCouponPool
  },
  data() {
    return {
      loading: false,
      searchForm: {
        couponName: '',
        validityDay: ''
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
        const { data, total } = await listCouponPool(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('couponPoolManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addCouponPool.open()
    },
    handleEdit(row) {
      this.$refs.editCouponPool.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteCouponPool.open(row)
    },
    handleSuccess() {
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
.coupon-pool-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
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