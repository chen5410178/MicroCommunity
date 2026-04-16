<template>
    <div class="staff-app-auth-container">
      <el-table :data="aStaffDetailAppAuthInfo.auths" border style="width: 100%" class="margin-top">
        <el-table-column prop="authId" :label="$t('staffDetailAppAuth.authId')" align="center" />
        <el-table-column prop="appName" :label="$t('staffDetailAppAuth.appName')" align="center" />
        <el-table-column prop="authTime" :label="$t('staffDetailAppAuth.authTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('staffDetailAppAuth.stateName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openAuthDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script>
  import { listAdminAppAuths } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailAppAuth',
    data() {
      return {
        aStaffDetailAppAuthInfo: {
          auths: [],
          staffId: ''
        },
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailAppAuthInfo.staffId = data.staffId
        this.loadAppAuthData(1, this.page.size)
      },
      async loadAppAuthData(page, row) {
        try {
          const { data, total } = await listAdminAppAuths({
            staffId: this.aStaffDetailAppAuthInfo.staffId,
            page,
            row
          })
          this.aStaffDetailAppAuthInfo.auths = data.auths
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailAppAuth.fetchError'))
        }
      },
      openAuthDetail(auth) {
        this.$router.push(`/views/auth/adminAppAuthDetail?authId=${auth.authId}`)
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadAppAuthData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadAppAuthData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-app-auth-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>