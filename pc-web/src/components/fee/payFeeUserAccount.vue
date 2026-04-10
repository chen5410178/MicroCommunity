<template>
  <div>
    <el-card v-if="accountList.length > 0" class="margin-bottom-sm">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeUserAccount.accountInfo') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="queryPayFeeUserAccount">
          <i class="el-icon-refresh"></i>
          {{ $t('payFeeUserAccount.refresh') }}
        </el-button>
      </div>

      <el-table :data="accountList" border style="width: 100%">
        <el-table-column align="center" :label="$t('payFeeUserAccount.select')" width="80">
          <template slot-scope="scope">
            <el-checkbox v-model="selectedAccounts" :value="scope.row.acctId"
              @change="(checked) => handleAccountChange(scope.row.acctId, checked)"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="acctTypeName" align="center" :label="$t('payFeeUserAccount.accountType')" />
        <el-table-column prop="acctName" align="center" :label="$t('payFeeUserAccount.accountName')" />
        <el-table-column prop="amount" align="center" :label="$t('payFeeUserAccount.accountAmount')">
          <template slot-scope="scope">
            {{ scope.row.amount }} {{ $t('payFeeUserAccount.yuan') }}
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('payFeeUserAccount.operation')" width="150">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openAddUserAmountModal(scope.row)">
              <i class="el-icon-plus"></i>
              {{ $t('payFeeUserAccount.prestore') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryCommunityOwnerAccount } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'PayFeeUserAccount',
  data() {
    return {
      accountList: [],
      feeId: '',
      ownerId: '',
      communityId: '',
      selectedAccounts: [] // 改为数组，支持多选
    }
  },
  methods: {
    open(params) {
      this.feeId = params.feeId
      this.ownerId = params.ownerId || ''
      this.listUserAccount()
    },
    close() {
      this.dialogVisible = false
    },
    async listUserAccount() {
      try {
        this.communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 20,
          feeId: this.feeId,
          ownerId: this.ownerId,
          communityId: this.communityId
        }

        const response = await queryCommunityOwnerAccount(params)
        this.accountList = response.data || []
      } catch (error) {
        console.error('查询用户账户失败:', error)
      }
    },
    queryPayFeeUserAccount() {
      this.listUserAccount()
    },
    openAddUserAmountModal(userAccount) {
      window.open(`/#/views/owner/ownerDetail?ownerId=${userAccount.objId}&currentTab=ownerDetailAccount`)
    },
    // 新增：处理账户选择变化
    handleAccountChange(acctId, checked) {
      console.log('选中的账户ID:', acctId, '是否选中:', checked)
      console.log('当前selectedAccounts:', this.selectedAccounts)
      
      // 确保 selectedAccounts 是数组
      if (!Array.isArray(this.selectedAccounts)) {
        this.selectedAccounts = []
      }
      
      if (checked) {
        // 添加到选中数组
        if (!this.selectedAccounts.includes(acctId)) {
          this.selectedAccounts.push(acctId)
        }
      } else {
        // 从选中数组中移除
        const index = this.selectedAccounts.indexOf(acctId)
        if (index > -1) {
          this.selectedAccounts.splice(index, 1)
        }
      }
      
      console.log('更新后的selectedAccounts:', this.selectedAccounts)
      this.computeFeeUserAmount()
    },
    // 修复：computeFeeUserAmount 方法
    computeFeeUserAmount() {
      console.log('计算费用，选中的账户:', this.selectedAccounts)
      
      let totalUserAmount = 0.0
      let selectAccount = []

      // 确保 selectedAccounts 是数组
      if (!Array.isArray(this.selectedAccounts)) {
        this.selectedAccounts = []
      }

      this.accountList.forEach(item => {
        if (this.selectedAccounts.includes(item.acctId) && item.amount != 0) {
          totalUserAmount += parseFloat(item.amount)
          selectAccount.push(item)
        }
      })

      console.log('计算的总金额:', totalUserAmount, '选中的账户列表:', selectAccount)

      this.$emit('changeUserAmountPrice', {
        totalUserAmount,
        accountList: this.accountList,
        integralAmount: 0,
        cashAmount: totalUserAmount,
        couponAmount: 0,
        selectAccount
      })
    },
    handleClose() {
      this.accountList = []
      this.selectedAccounts = [] // 清空选中的账户
      this.feeId = ''
      this.ownerId = ''
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 20px;
}
</style>