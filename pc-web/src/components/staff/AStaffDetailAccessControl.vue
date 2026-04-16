<template>
    <div class="staff-access-control-container">
      <el-table :data="aStaffDetailAccessControlInfo.accessControls" border style="width: 100%" class="margin-top">
        <el-table-column prop="accessId" :label="$t('staffDetailAccessControl.accessId')" align="center" />
        <el-table-column prop="deviceName" :label="$t('staffDetailAccessControl.deviceName')" align="center" />
        <el-table-column prop="syncTime" :label="$t('staffDetailAccessControl.syncTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('staffDetailAccessControl.stateName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openAccessControlDetail(scope.row)">
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
  import { listAdminAccessControls } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailAccessControl',
    data() {
      return {
        aStaffDetailAccessControlInfo: {
          accessControls: [],
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
        this.aStaffDetailAccessControlInfo.staffId = data.staffId
        this.loadAccessControlData(1, this.page.size)
      },
      async loadAccessControlData(page, row) {
        try {
          const { data, total } = await listAdminAccessControls({
            staffId: this.aStaffDetailAccessControlInfo.staffId,
            page,
            row
          })
          this.aStaffDetailAccessControlInfo.accessControls = data.accessControls
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailAccessControl.fetchError'))
        }
      },
      openAccessControlDetail(accessControl) {
        this.$router.push(`/views/access/adminAccessControlDetail?accessId=${accessControl.accessId}`)
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadAccessControlData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadAccessControlData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-access-control-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>