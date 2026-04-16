<template>
  <div class="user-login-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="store-list-card">
          <div class="store-list">
            <ul class="store-ul">
              <li v-for="(item, index) in userLoginInfo.stores" :key="index" @click="swatchStore(item)"
                :class="{ 'selected-store': userLoginInfo.conditions.storeId == item.storeId }">
                {{ item.name }}<span v-if="item.storeTypeName">({{ item.storeTypeName }})</span>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('userLogin.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input :placeholder="$t('userLogin.search.name')" v-model="userLoginInfo.conditions.name" clearable />
            </el-col>
            <el-col :span="4">
              <el-input type="number" :placeholder="$t('userLogin.search.tel')" v-model="userLoginInfo.conditions.tel"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="userLoginInfo.conditions.startTime" type="datetime"
                :placeholder="$t('userLogin.search.startTime')" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="userLoginInfo.conditions.endTime" type="datetime"
                :placeholder="$t('userLogin.search.endTime')" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryUserLoginMethod">
                <i class="el-icon-search"></i> {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('userLogin.table.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="userLoginInfo.logs" border style="width: 100%">
            <el-table-column prop="loginId" :label="$t('userLogin.table.loginId')" align="center" />
            <el-table-column prop="parentOrgName" :label="$t('userLogin.table.parentOrgName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentOrgName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="orgName" :label="$t('userLogin.table.orgName')" align="center" />
            <el-table-column prop="userName" :label="$t('userLogin.table.userName')" align="center" />
            <el-table-column prop="loginTime" :label="$t('userLogin.table.loginTime')" align="center" />
            <el-table-column prop="userId" :label="$t('userLogin.table.userId')" align="center" />
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px;">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryUserLogin, listStores } from '@/api/user/userLoginApi'

export default {
  name: 'UserLoginList',
  data() {
    return {
      loading: false,
      userLoginInfo: {
        logs: [],
        stores: [],
        conditions: {
          name: '',
          tel: '',
          storeId: '',
          startTime: '',
          endTime: '',
          page: 1,
          row: 10
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
    this.loadData()
    this._listListStores()
  },
  methods: {
    async loadData() {
      try {
        this.loading = true
        const params = {
          ...this.userLoginInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryUserLogin(params)
        this.userLoginInfo.logs = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('userLogin.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async _listListStores() {
      try {
        const params = {
          page: 1,
          row: 100
        }
        const { data } = await listStores(params)
        this.userLoginInfo.stores = [
          { storeId: '', name: this.$t('userLogin.allStores') },
          ...data
        ]
      } catch (error) {
        this.$message.error(this.$t('userLogin.storeFetchError'))
      }
    },
    swatchStore(item) {
      this.userLoginInfo.conditions.storeId = item.storeId
      this.pagination.current = 1
      this.loadData()
    },
    _queryUserLoginMethod() {
      this.pagination.current = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.user-login-container {
  padding: 20px;

  .store-list-card {
    max-height: 80vh;
    overflow-y: auto;

    .store-list {
      .store-ul {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          padding: 10px;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          text-align: center;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected-store {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }
}
</style>