<template>
  <div class="account-manage-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="tree-card">
          <ul class="account-type-list">
            <li v-for="(item, index) in accountTypes" :key="index" class="account-type-item"
              :class="{ 'active': conditions.acctType === item.statusCd }" @click="switchAccountType(item)">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>

      <el-col :span="21">
        <el-card class="search-card">
          <div slot="header" class="text-left">
            <span>{{ $t('accountManage.searchTitle') }}</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="conditions.acctName" :placeholder="$t('accountManage.placeholderName')" clearable />
            </el-col>

            <el-col :span="6">
              <el-input v-model="conditions.idCard" :placeholder="$t('accountManage.placeholderIdCard')" clearable />
            </el-col>

            <el-col :span="6">
              <el-input v-model="conditions.link" :placeholder="$t('accountManage.placeholderLink')" clearable />
            </el-col>

            <el-col :span="6">
              <el-button type="primary" @click="queryAccounts">
                {{ $t('accountManage.query') }}
              </el-button>
              <el-button @click="resetConditions">
                {{ $t('accountManage.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="mt-20">
          <div slot="header" class="flex-between">
            <span>{{ $t('accountManage.ownerAccount') }}</span>
            <el-button type="primary" size="small" @click="openPrestoreDialog">
              {{ $t('accountManage.prestore') }}
            </el-button>
          </div>

          <el-table :data="accounts" border v-loading="loading">
            <el-table-column prop="acctId" :label="$t('accountManage.accountId')" align="center" />

            <el-table-column prop="acctName" :label="$t('accountManage.accountName')" align="center" />

            <el-table-column prop="idCard" :label="$t('accountManage.idCard')" align="center" />

            <el-table-column prop="link" :label="$t('accountManage.phone')" align="center" />

            <el-table-column prop="acctTypeName" :label="$t('accountManage.accountType')" align="center" />

            <el-table-column prop="amount" :label="$t('accountManage.accountAmount')" align="center" />

            <el-table-column prop="roomName" :label="$t('accountManage.deductionRoom')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomName || $t('common.none') }}
              </template>
            </el-table-column>

            <el-table-column prop="createTime" :label="$t('accountManage.createTime')" align="center" />

            <el-table-column :label="$t('accountManage.operations')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="viewAccountDetail(scope.row)">
                  {{ $t('accountManage.accountDetail') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteDialog(scope.row)">
                  {{ $t('accountManage.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="mt-20" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>

    <prestore-account ref="prestoreDialog" @success="fetchAccounts" />
    <delete-account ref="deleteDialog" @success="fetchAccounts" />
  </div>
</template>

<script>
import { queryCommunityOwnerAccount } from '@/api/account/accountManageApi'
import PrestoreAccount from '@/components/account/prestoreAccount'
import DeleteAccount from '@/components/account/deleteAccount'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AccountManageList',
  components: {
    PrestoreAccount,
    DeleteAccount
  },
  data() {
    return {
      loading: false,
      accounts: [],
      accountTypes: [],
      conditions: {
        acctType: '',
        acctName: '',
        idCard: '',
        link: '',
        page: 1,
        row: 10
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadAccountTypes()
    this.fetchAccounts()
  },
  methods: {
    async loadAccountTypes() {
      try {
        const data = await getDict('account', 'acct_type')
        this.accountTypes = [{ name: this.$t('common.all'), statusCd: '' }, ...data]
      } catch (error) {
        console.error('Failed to load account types:', error)
      }
    },

    async fetchAccounts() {
      this.loading = true
      try {
        const res = await queryCommunityOwnerAccount(this.conditions)
        this.accounts = res.data || []
        this.pagination.total = res.total || 0
        this.pagination.size = this.conditions.row
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },

    queryAccounts() {
      this.conditions.page = 1
      this.fetchAccounts()
    },

    resetConditions() {
      this.conditions = {
        ...this.conditions,
        ownerName: '',
        idCard: '',
        link: '',
        acctType: '',
        page: 1
      }
      this.fetchAccounts()
    },

    switchAccountType(item) {
      this.conditions.acctType = item.statusCd
      this.queryAccounts()
    },

    openPrestoreDialog() {
      this.$refs.prestoreDialog.open()
    },

    openDeleteDialog(account) {
      this.$refs.deleteDialog.open(account)
    },

    viewAccountDetail(account) {
      this.$router.push(`/views/account/accountDetailManage?acctId=${account.acctId}`)
    },

    handleSizeChange(size) {
      this.conditions.row = size
      this.fetchAccounts()
    },

    handlePageChange(page) {
      this.conditions.page = page
      this.fetchAccounts()
    }
  }
}
</script>

<style scoped>
.account-manage-container {
  padding: 20px;
}

.tree-card {
  height: 100%;
}

.account-type-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.account-type-item {
  padding: 12px 15px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
}

.account-type-item:hover {
  background-color: #f5f7fa;
}

.account-type-item.active {
  background-color: #409eff;
  color: white;
  border-color: #409eff;
}

.search-card {
  margin-bottom: 20px;
}

.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mt-20 {
  margin-top: 20px;
}
</style>