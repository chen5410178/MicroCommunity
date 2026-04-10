<template>
  <div class="admin-account-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminAccountDetail.title') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="adminAccountDetailInfo.accountDetails"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="detailId"
          :label="$t('adminAccountDetail.detailId')"
          align="center"
        />
        <el-table-column
          prop="orderId"
          :label="$t('adminAccountDetail.orderId')"
          align="center"
        />
        <el-table-column
          prop="acctName"
          :label="$t('adminAccountDetail.acctName')"
          align="center"
        />
        <el-table-column
          :label="$t('adminAccountDetail.detailType')"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.detailType === '1001'">{{ $t('adminAccountDetail.detailType1001') }}</span>
            <span v-else-if="scope.row.detailType === '3003'">{{ $t('adminAccountDetail.detailType3003') }}</span>
            <span v-else>{{ $t('adminAccountDetail.detailTypeOther') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="amount"
          :label="$t('adminAccountDetail.amount')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('adminAccountDetail.createTime')"
          align="center"
        />
        <el-table-column
          :label="$t('adminAccountDetail.remark')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.remark || $t('adminAccountDetail.noRemark') }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import { queryAdminAccountDetail } from '@/api/account/adminAccountDetailApi'

export default {
  name: 'AdminAccountDetailList',
  data() {
    return {
      loading: false,
      adminAccountDetailInfo: {
        accountDetails: [],
        conditions: {
          acctId: '',
          detailType: '',
          orderId: ''
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.adminAccountDetailInfo.conditions.acctId = this.$route.query.acctId
    this._listAdminAccountDetails()
  },
  methods: {
    async _listAdminAccountDetails() {
      try {
        this.loading = true
        const params = {
          ...this.adminAccountDetailInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        
        const res = await queryAdminAccountDetail(params)
        this.adminAccountDetailInfo.accountDetails = res.data
        this.pagination.total = res.total
      } catch (error) {
        this.$message.error(this.$t('adminAccountDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listAdminAccountDetails()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listAdminAccountDetails()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-account-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-header-right {
        display: flex;
        align-items: center;
      }
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>