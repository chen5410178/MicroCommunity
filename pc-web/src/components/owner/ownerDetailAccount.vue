<template>
  <div class="margin-top">
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" style="margin-left:10px"
          v-if="ownerDetailAccountInfo.accounts.length <1 && hasPrivilege('502023032809461736')"
          @click="_prestoreAccount1()">
          {{ $t('ownerDetailAccount.prestore') }}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="ownerDetailAccountInfo.accounts" style="width: 100%">
        <el-table-column prop="acctId" :label="$t('ownerDetailAccount.acctId')" align="center"></el-table-column>
        <el-table-column prop="acctName" :label="$t('ownerDetailAccount.acctName')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('ownerDetailAccount.phone')" align="center"></el-table-column>
        <el-table-column prop="acctTypeName" :label="$t('ownerDetailAccount.acctType')" align="center"></el-table-column>
        <el-table-column prop="amount" :label="$t('ownerDetailAccount.amount')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerDetailAccount.createTime')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_prestoreAccount(scope.row)"
                v-if="hasPrivilege('502023032809461736')">
                {{ $t('ownerDetailAccount.prestore') }}
              </el-button>
              <el-button size="mini" @click="_accountDetail(scope.row)">
                {{ $t('ownerDetailAccount.detail') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, prev, pager, next, jumper">
      </el-pagination>
    </div>

    <prestore-account ref="prestoreAccount" @success="notify"></prestore-account>
  </div>
</template>

<script>
import PrestoreAccount from '@/components/account/prestoreAccount'
import { queryCommunityOwnerAccount } from '@/api/owner/ownerDetailAccountApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailAccount',
  components: {
    PrestoreAccount
  },
  data() {
    return {
      ownerDetailAccountInfo: {
        accounts: [],
        ownerId: '',
        name: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, link) {
      this.ownerDetailAccountInfo.ownerId = ownerId
      this.ownerDetailAccountInfo.ownerName = ownerName
      this.ownerDetailAccountInfo.link = link
      this._loadOwnerDetailAccountData(1, this.pagination.pageSize)
    },
    notify(){
      this._loadOwnerDetailAccountData(1, this.pagination.pageSize)
    },
    _loadOwnerDetailAccountData(page, row) {
      const params = {
        communityId: getCommunityId(),
        ownerId: this.ownerDetailAccountInfo.ownerId,
        page: page,
        row: row
      }

      queryCommunityOwnerAccount(params).then(response => {
        this.ownerDetailAccountInfo.accounts = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _accountDetail(account) {
      this.$router.push(`/views/account/accountDetailManage?acctId=${account.acctId}`)
    },
    _prestoreAccount(account) {
      this.$refs.prestoreAccount.open({
        ownerId: this.ownerDetailAccountInfo.ownerId,
        acctType: account.acctType,
        tel: account.link,
        roomId: account.roomId
      })
    },
    _prestoreAccount1() {
      this.$refs.prestoreAccount.open({})
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailAccountData(val, this.pagination.pageSize)
    },
  },
  created() {
  }
}
</script>

<style scoped>
/* 样式同上一个组件 */
</style>