<template>
  <div class="asset-import-log-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('assetImportLog.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_queryData">
            <i class="el-icon-refresh"></i>{{ $t('common.refresh') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24"> 
          <el-table v-loading="loading" :data="assetImportLogInfo.logs" border style="width: 100%">
            <el-table-column prop="logId" :label="$t('assetImportLog.logId')" align="center" />
            <el-table-column prop="logTypeName" :label="$t('assetImportLog.logTypeName')" align="center" />
            <el-table-column prop="waitCount" :label="$t('assetImportLog.waitCount')" align="center" />
            <el-table-column prop="successCount" :label="$t('assetImportLog.successCount')" align="center" />
            <el-table-column prop="errorCount" :label="$t('assetImportLog.errorCount')" align="center" />
            <el-table-column prop="state" :label="$t('assetImportLog.state')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === 'W'">{{ $t('assetImportLog.stateW') }}</span>
                <span v-else-if="scope.row.state === 'C'">{{ $t('assetImportLog.stateC') }}</span>
                <span v-else>{{ $t('assetImportLog.stateP') }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('assetImportLog.createTime')" align="center" />
            <el-table-column prop="remark" :label="$t('assetImportLog.remark')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openDetail(scope.row)">{{ $t('common.detail') }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { queryAssetImportLog } from '@/api/system/assetImportLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AssetImportLogList',
  data() {
    return {
      loading: false,
      assetImportLogInfo: {
        logs: [],
        total: 0
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listAssetImportLogs(this.page.current, this.page.size)
  },
  methods: {
    async _listAssetImportLogs(page, size) {
      try {
        this.loading = true
        const params = {
          page,
          row: size,
          communityId: this.communityId
        }
        const { data, total } = await queryAssetImportLog(params)
        this.assetImportLogInfo.logs = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('assetImportLog.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryData() {
      this.page.current = 1
      this._listAssetImportLogs(this.page.current, this.page.size)
    },
    _openDetail(log) {
      this.$router.push({
        path: '/views/system/assetImportLogDetail',
        query: {
          logId: log.logId,
          logType: log.logType
        }
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAssetImportLogs(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAssetImportLogs(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-import-log-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-tools {
    float: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>