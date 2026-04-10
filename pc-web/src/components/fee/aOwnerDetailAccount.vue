<template>
  <div class="account-container">
    <el-table :data="accounts" border>
      <el-table-column prop="acctId" :label="$t('adminRoomFee.accountId')" align="center" />
      <el-table-column prop="acctName" :label="$t('adminRoomFee.accountName')" align="center" />
      <el-table-column prop="link" :label="$t('adminRoomFee.phone')" align="center" />
      <el-table-column prop="acctTypeName" :label="$t('adminRoomFee.accountType')" align="center" />
      <el-table-column prop="amount" :label="$t('adminRoomFee.accountAmount')" align="center" />
      <el-table-column prop="createTime" :label="$t('adminRoomFee.createTime')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" @click="toDetail(scope.row)">
            {{ $t('adminRoomFee.accountDetail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="page.current"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { queryAdminOwnerAccount } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'AOwnerDetailAccount',

  data() {
    return {
      accounts: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.ownerId = params.ownerId
      this.page.current = 1
      this.loadData()
    },
    loadData() {
      const params = {
        ownerId: this.ownerId,
        page: this.page.current,
        row: this.page.size
      }
      
      queryAdminOwnerAccount(params).then(res => {
        this.accounts = res.data
        this.page.total = res.total
      })
    },
    toDetail(account) {
      this.$router.push(`/views/account/adminAccountDetail?acctId=${account.acctId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>