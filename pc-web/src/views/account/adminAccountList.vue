<template>
  <div class="admin-account-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community :community-id="adminAccountInfo.conditions.communityId"
        @changeCommunity="handleChangeCommunity" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminAccount.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="adminAccountInfo.conditions.acctType" :placeholder="$t('adminAccount.search.acctType')"
                class="w-100">
                <el-option v-for="item in adminAccountInfo.acctTypes" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="adminAccountInfo.conditions.ownerName"
                :placeholder="$t('adminAccount.search.ownerName')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="adminAccountInfo.conditions.idCard" :placeholder="$t('adminAccount.search.idCard')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model.number="adminAccountInfo.conditions.link" :placeholder="$t('adminAccount.search.link')" />
            </el-col>
            <el-col :span="4" >
              <el-button type="primary" @click="handleQueryAccount">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminAccount.list.title') }}</span>
          </div>
          <el-table :data="adminAccountInfo.accounts" border style="width: 100%" v-loading="loading">
            <el-table-column prop="acctId" :label="$t('adminAccount.table.acctId')" align="center" />
            <el-table-column prop="acctName" :label="$t('adminAccount.table.acctName')" align="center" />
            <el-table-column prop="idCard" :label="$t('adminAccount.table.idCard')" align="center" />
            <el-table-column prop="link" :label="$t('adminAccount.table.link')" align="center" />
            <el-table-column prop="acctTypeName" :label="$t('adminAccount.table.acctTypeName')" align="center" />
            <el-table-column prop="amount" :label="$t('adminAccount.table.amount')" align="center" />
            <el-table-column prop="roomName" :label="$t('adminAccount.table.roomName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomName || $t('adminAccount.table.noRoom') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminAccount.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleAccountDetail(scope.row)">
                  {{ $t('adminAccount.btn.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="mt-20" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import {getDict} from '@/api/community/communityApi'
import {queryAdminOwnerAccount} from '@/api/account/adminAccountApi'

export default {
  name: 'AdminAccountList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      adminAccountInfo: {
        accounts: [],
        acctTypes: [],
        conditions: {
          acctType: '',
          ownerName: '',
          idCard: '',
          link: '',
          communityId: ''
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
    this.loadAcctTypes()
    this.loadAdminAccounts()
  },
  methods: {
    async loadAcctTypes() {
      try {
        const res = await getDict('account', 'acct_type')
        let _acctTypes = [{
          name: this.$t('common.all'),
          statusCd: ''
        }]
        _acctTypes = _acctTypes.concat(res)
        this.adminAccountInfo.acctTypes = _acctTypes
      } catch (error) {
        console.error('获取账户类型失败:', error)
      }
    },
    async loadAdminAccounts() {
      try {
        this.loading = true
        const params = {
          ...this.adminAccountInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const res = await queryAdminOwnerAccount(params)
        this.adminAccountInfo.accounts = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('获取账户列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleChangeCommunity(community) {
      this.adminAccountInfo.conditions.communityId = community.communityId
      this.pagination.current = 1
      this.loadAdminAccounts()
    },
    handleQueryAccount() {
      this.pagination.current = 1
      this.loadAdminAccounts()
    },
    handleResetAccount() {
      this.adminAccountInfo.conditions = {
        acctType: '',
        ownerName: '',
        idCard: '',
        link: '',
        communityId: this.adminAccountInfo.conditions.communityId
      }
      this.pagination.current = 1
      this.loadAdminAccounts()
    },
    handleAccountDetail(account) {
      this.$router.push({
        path: '/views/account/adminAccountDetail',
        query: { acctId: account.acctId }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadAdminAccounts()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadAdminAccounts()
    }
  }
}
</script>

<style scoped>
.admin-account-container {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.w-100 {
  width: 100%;
}

.text-right {
  text-align: right;
}
</style>