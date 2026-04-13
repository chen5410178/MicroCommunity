<template>
  <el-dialog :title="$t('doImportShopCreateFee.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form label-width="120px" class="text-left">
      
      <el-form-item :label="$t('doImportShopCreateFee.selectFile')">
        <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="handleFileChange"
          :show-file-list="false" accept=".xls,.xlsx">
          <el-button size="small" type="primary">{{ $t('doImportShopCreateFee.clickUpload') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('doImportShopCreateFee.fileTip') }}
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item :label="$t('doImportShopCreateFee.template')">
        <el-button size="small" type="success" @click="handleDownloadTemplate">
          <i class="el-icon-download"></i>
          {{ $t('doImportShopCreateFee.downloadTemplate') }}
        </el-button>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport" :disabled="!fileName">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { importData, downloadTemplate } from '@/api/fee/doImportShopCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DoImportShopCreateFee',
  data() {
    return {
      visible: false,
      file: null,
      fileName: ''
    }
  },
  computed: {
    communityId() {
      return getCommunityId()
    }
  },
  methods: {
    open() {
      this.visible = true
      this.reset()
    },

    handleDownloadTemplate() {
      const params = {
        type: '4444',
        pagePath: 'exportCreateShopFeeTemplate',
        communityId: this.communityId
      }
      downloadTemplate(params).then(response => {
        this.$message.success(response.msg || this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      }).catch(error => {
        this.$message.error(error.message || 'Failed to download template')
      })
    },

    handleFileChange(file) {
      if (!this.checkFileType(file.name)) {
        this.$message.error(this.$t('doImportShopCreateFee.validate.invalidFileType'))
        return false
      }

      if (!this.checkFileSize(file.size)) {
        this.$message.error(this.$t('doImportShopCreateFee.validate.fileTooLarge'))
        return false
      }

      this.file = file.raw
      this.fileName = file.name
    },

    checkFileType(filename) {
      var ext = filename.split('.').pop().toLowerCase()
      return ['xls', 'xlsx'].includes(ext)
    },

    checkFileSize(size) {
      return size <= 2 * 1024 * 1024
    },

    handleImport() {
      var formData = new FormData()
      formData.append('uploadFile', this.file)
      formData.append('communityId', this.communityId)
      formData.append('importAdapt', 'importCustomShopFee')

      importData(formData).then(response => {
        var res = response
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$router.push('/views/system/assetImportLogDetail?logId=' + res.data.logId + '&logType=importCustomShopFee')
        } else {
          this.$message.error(res.msg)
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },

    handleClose() {
      this.visible = false
      this.reset()
    },

    reset() {
      this.file = null
      this.fileName = ''
    }
  }
}
</script>
