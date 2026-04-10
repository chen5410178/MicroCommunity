<template>
  <div class="coupon-property-pool-detail-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyPoolDetail.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.cppId" :placeholder="$t('couponPropertyPoolDetail.search.cppId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.couponName" :placeholder="$t('couponPropertyPoolDetail.search.couponName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.fromType" :placeholder="$t('couponPropertyPoolDetail.search.fromType')"
            style="width:100%">
            <el-option :label="$t('couponPropertyPoolDetail.search.fromTypePlaceholder')" value="" />
            <el-option :label="$t('couponPropertyPoolDetail.search.fromType1')" value="1001" />
            <el-option :label="$t('couponPropertyPoolDetail.search.fromType2')" value="2002" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top:20px">
        <el-col :span="6">
          <el-input v-model="searchForm.userName" :placeholder="$t('couponPropertyPoolDetail.search.userName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('couponPropertyPoolDetail.search.tel')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.toType" :placeholder="$t('couponPropertyPoolDetail.search.toType')"
            style="width:100%">
            <el-option :label="$t('couponPropertyPoolDetail.search.toTypePlaceholder')" value="" />
            <el-option :label="$t('couponPropertyPoolDetail.search.toType1')" value="1011" />
            <el-option :label="$t('couponPropertyPoolDetail.search.toType2')" value="3003" />
            <el-option :label="$t('couponPropertyPoolDetail.search.toType3')" value="4004" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 赠送记录列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('couponPropertyPoolDetail.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="cppId" :label="$t('couponPropertyPoolDetail.table.cppId')" align="center" />
        <el-table-column prop="couponName" :label="$t('couponPropertyPoolDetail.table.couponName')" align="center" />
        <el-table-column prop="value" :label="$t('couponPropertyPoolDetail.table.value')" align="center" />
        <el-table-column prop="sendCount" :label="$t('couponPropertyPoolDetail.table.sendCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sendCount }}{{ $t('couponPropertyPoolDetail.table.unit') }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('couponPropertyPoolDetail.table.userName')" align="center" />
        <el-table-column prop="tel" :label="$t('couponPropertyPoolDetail.table.tel')" align="center" />
        <el-table-column prop="createTime" :label="$t('couponPropertyPoolDetail.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleReturn(scope.row)">
              {{ $t('couponPropertyPoolDetail.btn.return') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 退回确认对话框 -->
    <delete-coupon-property-pool-detail ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listCouponPropertyPoolDetail } from '@/api/scm/couponPropertyPoolDetailApi'
import DeleteCouponPropertyPoolDetail from '@/components/scm/deleteCouponPropertyPoolDetail'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponPropertyPoolDetailList',
  components: {
    DeleteCouponPropertyPoolDetail
  },
  data() {
    return {
      loading: false,
      searchForm: {
        cppId: '',
        couponName: '',
        fromType: '',
        toType: '',
        userName: '',
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
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listCouponPropertyPoolDetail(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('couponPropertyPoolDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReturn(row) {
      this.$refs.deleteDialog.open(row)
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
.coupon-property-pool-detail-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-top: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>