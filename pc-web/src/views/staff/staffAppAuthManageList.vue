<template>
  <div class="staff-app-auth-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('staffAppAuthManage.title') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_refreshStaffAppAuth">
            <i class="el-icon-refresh"></i> {{ $t('common.refresh') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-table :data="[staffAppAuthManageInfo]" border style="width: 100%" v-loading="loading">
            <el-table-column prop="staffName" :label="$t('staffAppAuthManage.staffName')" align="center" />
            <el-table-column prop="appType" :label="$t('staffAppAuthManage.authType')" align="center" />
            <el-table-column prop="openName" :label="$t('staffAppAuthManage.authName')" align="center" />
            <el-table-column prop="openId" :label="$t('staffAppAuthManage.authId')" align="center" />
            <el-table-column prop="stateName" :label="$t('staffAppAuthManage.authStatus')" align="center" />
            <el-table-column prop="createTime" :label="$t('staffAppAuthManage.authTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template >
                <el-button size="mini" type="primary" @click="_openAddStaffAppAuthModal">
                  {{ $t('staffAppAuthManage.auth') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

      <add-staff-app-auth ref="addStaffAppAuth" @success="_refreshStaffAppAuth" />
    </el-card>
  </div>
</template>

<script>
import { queryStaffAppAuth } from '@/api/staff/staffAppAuthManageApi'
import AddStaffAppAuth from '@/components/staff/addStaffAppAuth'

export default {
  name: 'StaffAppAuthManageList',
  components: {
    AddStaffAppAuth
  },
  data() {
    return {
      loading: false,
      staffAppAuthManageInfo: {
        staffName: '',
        appType: '',
        stateName: '',
        auId: '',
        openId: '',
        createTime: '',
        openName: ''
      }
    }
  },
  created() {
    this._listStaffAppAuths()
  },
  methods: {
    async _listStaffAppAuths() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 1
        }
        const { data } = await queryStaffAppAuth(params)
        this.staffAppAuthManageInfo = data
      } catch (error) {
        this.$message.error(this.$t('staffAppAuthManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddStaffAppAuthModal() {
      this.$refs.addStaffAppAuth.open()
    },
    _refreshStaffAppAuth() {
      this._listStaffAppAuths()
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-app-auth-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .el-table {
    margin-top: 20px;
  }
}
</style>