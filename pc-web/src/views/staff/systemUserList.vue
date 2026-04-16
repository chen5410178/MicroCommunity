<template>
  <div class="system-user-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('systemUser.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="systemUserInfo.conditions.levelCd" :placeholder="$t('systemUser.search.levelCd')" clearable>
            <el-option label="员工" value="01"></el-option>
            <el-option label="用户" value="02"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="systemUserInfo.conditions.nameLike" :placeholder="$t('systemUser.search.nameLike')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input v-model="systemUserInfo.conditions.tel" :placeholder="$t('systemUser.search.tel')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="_querySystemUserMethod">{{ $t('common.search') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 用户列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('systemUser.list.title') }}</span>
      </div>
      <el-table :data="systemUserInfo.users" border style="width: 100%">
        <el-table-column prop="userId" :label="$t('systemUser.table.userId')" align="center"></el-table-column>
        <el-table-column prop="userName" :label="$t('systemUser.table.userName')" align="center"></el-table-column>
        <el-table-column prop="tel" :label="$t('systemUser.table.tel')" align="center"></el-table-column>
        <el-table-column prop="email" :label="$t('systemUser.table.email')" align="center">
          <template slot-scope="scope">
            {{ scope.row.email || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('systemUser.table.address')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('systemUser.table.createTime')" align="center"></el-table-column>
        <el-table-column :label="$t('systemUser.table.levelCd')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.levelCd === '00'">{{ $t('systemUser.level.admin') }}</span>
            <span v-if="scope.row.levelCd === '01'">{{ $t('systemUser.level.staff') }}</span>
            <span v-if="scope.row.levelCd === '02'">{{ $t('systemUser.level.user') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ownerCount" :label="$t('systemUser.table.ownerCount')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="250">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="_openDeleteSystemUserModel(scope.row)">{{ $t('common.delete') }}</el-button>
            <el-button size="mini" @click="_openResetSystemUserPwdModel(scope.row)">{{ $t('systemUser.resetPwd') }}</el-button>
            <el-button size="mini" type="primary" @click="_toUserDetailPage(scope.row)">{{ $t('common.detail') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        class="pagination-wrapper">
      </el-pagination>
    </el-card>

    <delete-system-user ref="deleteSystemUser" @success="handleSuccess"></delete-system-user>
    <reset-system-user-pwd ref="resetSystemUserPwd" @success="handleSuccess"></reset-system-user-pwd>
  </div>
</template>

<script>
import { listSystemUsers } from '@/api/staff/systemUserApi'
import DeleteSystemUser from '@/components/staff/DeleteSystemUser'
import ResetSystemUserPwd from '@/components/staff/ResetSystemUserPwd'

export default {
  name: 'SystemUserList',
  components: {
    DeleteSystemUser,
    ResetSystemUserPwd
  },
  data() {
    return {
      systemUserInfo: {
        users: [],
        conditions: {
          nameLike: '',
          tel: '',
          levelCd: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._listSystemUsers(this.page.current, this.page.size)
  },
  methods: {
    async _listSystemUsers(page, size) {
      try {
        const params = {
          page,
          row: size,
          ...this.systemUserInfo.conditions
        }
        const { data, total } = await listSystemUsers(params)
        this.systemUserInfo.users = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('systemUser.fetchError'))
      }
    },
    _querySystemUserMethod() {
      this.page.current = 1
      this._listSystemUsers(this.page.current, this.page.size)
    },
    _openDeleteSystemUserModel(user) {
      this.$refs.deleteSystemUser.open(user)
    },
    _openResetSystemUserPwdModel(user) {
      this.$refs.resetSystemUserPwd.open(user)
    },
    _toUserDetailPage(user) {
      this.$router.push(`/views/staff/systemUserDetail?userId=${user.userId}`)
    },
    handleSuccess() {
      this._listSystemUsers(this.page.current, this.page.size)
    },
    handleSizeChange(size) {
      this.page.size = size
      this._listSystemUsers(this.page.current, size)
    },
    handleCurrentChange(page) {
      this.page.current = page
      this._listSystemUsers(page, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.system-user-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-select, .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>