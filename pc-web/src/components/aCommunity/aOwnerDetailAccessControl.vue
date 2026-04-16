<template>
  <div class="owner-detail-access-control">
    <div class="margin-top">
      <el-table :data="aOwnerDetailAccessControlInfo.translates" border style="width: 100%">
        <el-table-column prop="typeCdName" :label="$t('aOwnerDetailAccessControl.objectType')" align="center" />
        <el-table-column prop="objName" :label="$t('aOwnerDetailAccessControl.objectName')" align="center" />
        <el-table-column prop="machineCmdName" :label="$t('aOwnerDetailAccessControl.command')" align="center" />
        <el-table-column prop="stateName" :label="$t('aOwnerDetailAccessControl.status')" align="center" />
        <el-table-column prop="remark" :label="$t('aOwnerDetailAccessControl.description')" align="center">
          <template #default="{ row }">
            <div class="hc-td">{{ row.remark }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" :label="$t('aOwnerDetailAccessControl.syncTime')" align="center" />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16"></el-col>
        <el-col :span="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminMachineTranslates } from '@/api/aCommunity/aOwnerDetailAccessControlApi'
export default {
  name: 'AOwnerDetailAccessControl',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      aOwnerDetailAccessControlInfo: {
        translates: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aOwnerDetailAccessControlInfo.ownerId = data.ownerId
      this._loadAOwnerDetailAccessControlData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAOwnerDetailAccessControlData(val, this.DEFAULT_ROWS)
    },
    async _loadAOwnerDetailAccessControlData(page, row) {
      const param = {
        page,
        row,
        objId: this.aOwnerDetailAccessControlInfo.ownerId,
        typeCd: '8899'
      }

      try {
        const response = await listAdminMachineTranslates(param)
        this.aOwnerDetailAccessControlInfo.translates = response.machineTranslates
        this.total = response.total
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

.hc-td {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}
</style>