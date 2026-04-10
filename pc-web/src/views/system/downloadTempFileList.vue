<template>
  <div class="download-temp-file-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('downloadTempFile.title') }}</span>
        <el-button type="primary" size="mini" style="float: right;" @click="_queryDownloadTempFileMethod">
          {{ $t('downloadTempFile.refresh') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="downloadTempFileInfo.files" border style="width: 100%">
        <el-table-column prop="name" :label="$t('downloadTempFile.name')" align="center" />
        <el-table-column prop="fileTypeName" :label="$t('downloadTempFile.fileType')" align="center" />
        <el-table-column prop="downloadUserName" :label="$t('downloadTempFile.downloadUser')" align="center" />
        <el-table-column prop="createTime" :label="$t('downloadTempFile.downloadTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('downloadTempFile.status')" align="center" />
        <el-table-column prop="remark" :label="$t('downloadTempFile.remark')" align="center" />
        <el-table-column :label="$t('downloadTempFile.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '3003'" size="mini" type="primary" @click="_downLoadFile(scope.row)">
              {{ $t('downloadTempFile.download') }}
            </el-button>
            <el-button v-if="scope.row.state === '3003' || scope.row.state === '4004'" size="mini" type="danger"
              @click="_openDeleteFileModel(scope.row)">
              {{ $t('downloadTempFile.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <delete-download-temp-file ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listUserDownloadFile } from '@/api/system/downloadTempFileApi'
import DeleteDownloadTempFile from '@/components/system/deleteDownloadTempFile'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DownloadTempFileList',
  components: {
    DeleteDownloadTempFile
  },
  data() {
    return {
      loading: false,
      downloadTempFileInfo: {
        files: [],
        conditions: {
          communityId: '',
          page: 1,
          row: 10
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
    this.downloadTempFileInfo.conditions.communityId = getCommunityId()
    this._listFiles(this.page.current, this.page.size)
  },
  methods: {
    async _listFiles(page, size) {
      try {
        this.loading = true
        this.downloadTempFileInfo.conditions.page = page
        this.downloadTempFileInfo.conditions.row = size

        const { data, total } = await listUserDownloadFile(this.downloadTempFileInfo.conditions)
        this.downloadTempFileInfo.files = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('downloadTempFile.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _downLoadFile(file) {
      if (!file.tempUrl) {
        this.$message.error(this.$t('downloadTempFile.downloadFailed'))
        return
      }
      window.open(file.downloadUrl, '_blank')
    },
    _openDeleteFileModel(file) {
      this.$refs.deleteDialog.open(file)
    },
    _queryDownloadTempFileMethod() {
      this.page.current = 1
      this._listFiles(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listFiles(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFiles(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFiles(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.download-temp-file-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>