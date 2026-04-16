<template>
  <div class="coupon-property-pool-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyPoolManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.cppId" :placeholder="$t('couponPropertyPoolManage.search.cppId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.couponName" :placeholder="$t('couponPropertyPoolManage.search.couponName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.fromType" :placeholder="$t('couponPropertyPoolManage.search.fromType')"
            style="width:100%" clearable>
            <el-option v-for="item in fromTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top:15px">
        <el-col :span="6">
          <el-select v-model="searchForm.toType" :placeholder="$t('couponPropertyPoolManage.search.toType')"
            style="width:100%" clearable>
            <el-option v-for="item in toTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyPoolManage.list.title') }}</span>
        <div style="float:right">
   
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('couponPropertyPoolManage.list.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="cppId" :label="$t('couponPropertyPoolManage.table.cppId')" align="center" />
        <el-table-column prop="couponName" :label="$t('couponPropertyPoolManage.table.couponName')" align="center" />
        <el-table-column prop="fromTypeName" :label="$t('couponPropertyPoolManage.table.fromType')" align="center" />
        <el-table-column prop="toTypeName" :label="$t('couponPropertyPoolManage.table.toType')" align="center" />
        <el-table-column prop="stock" :label="$t('couponPropertyPoolManage.table.stock')" align="center" />
        <el-table-column prop="validityDay" :label="$t('couponPropertyPoolManage.table.validityDay')" align="center">
          <template slot-scope="scope">
            {{ scope.row.validityDay }}{{ $t('couponPropertyPoolManage.table.day') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('couponPropertyPoolManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button v-if="scope.row.stock > 0" size="mini" type="text" @click="handleGift(scope.row)">
              {{ $t('couponPropertyPoolManage.table.gift') }}
            </el-button>
            <el-button v-if="scope.row.fromType === '2002'" size="mini" type="text" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button v-if="scope.row.fromType === '2002'" size="mini" type="text" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-coupon-property-pool ref="addCouponPropertyPool" @success="handleSuccess" />
    <edit-coupon-property-pool ref="editCouponPropertyPool" @success="handleSuccess" />
    <gift-coupon-property-pool ref="giftCouponPropertyPool" @success="handleSuccess" />
    <delete-coupon-property-pool ref="deleteCouponPropertyPool" @success="handleSuccess" />
  </div>
</template>

<script>
import { listCouponPropertyPool } from '@/api/scm/couponPropertyPoolManageApi'
import AddCouponPropertyPool from '@/components/scm/addCouponPropertyPool'
import EditCouponPropertyPool from '@/components/scm/editCouponPropertyPool'
import GiftCouponPropertyPool from '@/components/scm/giftCouponPropertyPool'
import DeleteCouponPropertyPool from '@/components/scm/deleteCouponPropertyPool'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponPropertyPoolManageList',
  components: {
    AddCouponPropertyPool,
    EditCouponPropertyPool,
    GiftCouponPropertyPool,
    DeleteCouponPropertyPool
  },
  data() {
    return {
      loading: false,
      searchForm: {
        cppId: '',
        couponName: '',
        fromType: '',
        toType: '',
        communityId: getCommunityId()
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      fromTypeOptions: [
        { value: '1001', label: this.$t('couponPropertyPoolManage.fromType.market') },
        { value: '2002', label: this.$t('couponPropertyPoolManage.fromType.self') }
      ],
      toTypeOptions: [
        { value: '1001', label: this.$t('couponPropertyPoolManage.toType.shopping') },
        { value: '3003', label: this.$t('couponPropertyPoolManage.toType.repair') },
        { value: '4004', label: this.$t('couponPropertyPoolManage.toType.parking') }
      ]
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
        const { data, total } = await listCouponPropertyPool(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('couponPropertyPoolManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addCouponPropertyPool.open()
    },
    handleEdit(row) {
      this.$refs.editCouponPropertyPool.open(row)
    },
    handleGift(row) {
      this.$refs.giftCouponPropertyPool.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteCouponPropertyPool.open(row)
    },
    handleShowDoc() {
      // 显示文档逻辑
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
.coupon-property-pool-manage-container {
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