<template>
  <div class="admin-owner-detail-access-control-record">
    <div class="margin-top">
      <el-table
        :data="accessControlRecordInfo.records"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="recordTypeName"
          :label="$t('adminOwnerDetailAccessControlRecord.recordType')"
          align="center"
        />
        <el-table-column
          prop="machineName"
          :label="$t('adminOwnerDetailAccessControlRecord.machineName')"
          align="center"
        />
        <el-table-column
          prop="userName"
          :label="$t('adminOwnerDetailAccessControlRecord.userName')"
          align="center"
        />
        <el-table-column
          prop="openTypeCdName"
          :label="$t('adminOwnerDetailAccessControlRecord.openType')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('adminOwnerDetailAccessControlRecord.createTime')"
          align="center"
        />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16"></el-col>
        <el-col :span="8">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
          />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminOwnerDetailAccessControlRecord',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      accessControlRecordInfo: {
        records: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    handleSwitch(data) {
      this.accessControlRecordInfo.ownerId = data.ownerId
      this._loadAccessControlRecordData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAccessControlRecordData(val, this.DEFAULT_ROWS)
    },
    async _loadAccessControlRecordData(page, row) {
      const param = {
        page,
        row,
        objId: this.accessControlRecordInfo.ownerId,
        objType: '8899'
      }

      try {
        const response = await this.$http.get('/machineRecord.listAdminMachineRecords', { params: param })
        const recordInfo = response.data
        this.accessControlRecordInfo.records = recordInfo.machineRecords
        this.total = recordInfo.total
        this.currentPage = page
      } catch (error) {
        console.error('请求失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>