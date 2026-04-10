<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix flex justify-between">
      <div>{{ $t('accountDetailManage.title') }}</div>
      <div class="header-tools">
        <el-button type="primary" size="small" @click="goBack" icon="el-icon-close">
          {{ $t('accountDetailManage.back') }}
        </el-button>
      </div>
    </div>

    <el-table :data="accountDetails" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="detailId" :label="$t('accountDetailManage.detailId')" align="center" min-width="120" />

      <el-table-column prop="orderId" :label="$t('accountDetailManage.transactionId')" align="center" min-width="150" />

      <el-table-column prop="acctName" :label="$t('accountDetailManage.accountName')" align="center" min-width="120" />

      <el-table-column :label="$t('accountDetailManage.detailType')" align="center" min-width="150">
        <template slot-scope="scope">
          <span v-if="scope.row.detailType === '1001'">
            {{ $t('accountDetailManage.deposit') }}
            <span v-if="hasPrivilege('502023032835101743')">
              (<el-link type="primary" @click="openCancelDialog(scope.row)">
                {{ $t('common.cancel') }}
              </el-link>)
            </span>
          </span>
          <span v-else-if="scope.row.detailType === '3003'">
            {{ $t('accountDetailManage.depositCancelled') }}
          </span>
          <span v-else>
            {{ $t('accountDetailManage.withdrawal') }}
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="amount" :label="$t('accountDetailManage.amountPoints')" align="center" min-width="120" />

      <el-table-column prop="createTime" :label="$t('accountDetailManage.transactionTime')" align="center"
        min-width="180" />

      <el-table-column :label="$t('accountDetailManage.description')" align="center" min-width="200">
        <template slot-scope="scope">
          {{ scope.row.remark || $t('common.none') }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="pagination-wrapper" :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handlePageChange" />

    <cancel-account-detail ref="cancelDialog" @success="fetchAccountDetails" />
  </el-card>
</template>

<script>
import { queryOwnerAccountDetail } from '@/api/account/accountDetailManageApi'
import CancelAccountDetail from '@/components/account/cancelAccountDetail'

export default {
  name: 'AccountDetailManageList',
  components: { CancelAccountDetail },
  data() {
    return {
      loading: false,
      accountDetails: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      queryParams: {
        acctId: '',
        detailType: '',
        orderId: ''
      }
    }
  },
  created() {
    this.queryParams.acctId = this.$route.query.acctId
    this.fetchAccountDetails()
  },
  methods: {
    async fetchAccountDetails() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.queryParams
        }

        const res = await queryOwnerAccountDetail(params)
        this.accountDetails = res.data || []
        this.pagination.total = res.total || 0
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    openCancelDialog(row) {
      this.$refs.cancelDialog.open(row)
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchAccountDetails()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.fetchAccountDetails()
    },
    goBack() {
      this.$router.go(-1)
    },
  }
}
</script>

<style scoped>
.header-tools {
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.box-card {
  margin: 20px;
}
</style>