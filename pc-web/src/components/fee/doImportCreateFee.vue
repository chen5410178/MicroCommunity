<template>
  <el-dialog :title="$t('doImportCreateFee.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form label-width="120px" class="text-left">
      <el-form-item :label="$t('doImportCreateFee.selectFile')">
        <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="handleFileChange"
          :show-file-list="false" accept=".xls,.xlsx">
          <el-button size="small" type="primary">{{ $t('doImportCreateFee.clickUpload') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('doImportCreateFee.fileTip') }}
          </div>
        </el-upload>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport" :disabled="!fileName">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { importData } from '@/api/fee/doImportCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DoImportCreateFee',
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

    handleFileChange(file) {
      if (!this.checkFileType(file.name)) {
        this.$message.error(this.$t('doImportCreateFee.validate.invalidFileType'))
        return false
      }

      if (!this.checkFileSize(file.size)) {
        this.$message.error(this.$t('doImportCreateFee.validate.fileTooLarge'))
        return false
      }

      this.file = file.raw
      this.fileName = file.name
    },

    checkFileType(filename) {
      const ext = filename.split('.').pop().toLowerCase()
      return ['xls', 'xlsx'].includes(ext)
    },

    checkFileSize(size) {
      return size <= 2 * 1024 * 1024 // 2MB
    },

    handleImport() {
      const formData = new FormData()
      formData.append('uploadFile', this.file)
      formData.append('communityId', this.communityId)
      formData.append('importAdapt', 'importCustomFee')

      importData(formData).then(response => {
        const res = response
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$router.push(`/views/system/assetImportLogDetail?logId=${res.data.logId}&logType=importCustomFee`)
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