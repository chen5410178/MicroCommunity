<template>
  <el-dialog :title="$t('listOwnerCar.importCarTitle')" :visible.sync="visible" width="600px" @close="resetForm">
    <el-form :model="form" label-width="120px" class="text-left">
      <el-form-item :label="$t('listOwnerCar.selectFile')">
        <el-upload class="upload-demo" action="" :on-change="handleFileChange" :auto-upload="false"
          :show-file-list="false">
          <el-button size="small" type="primary">{{ $t('listOwnerCar.selectFile') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('listOwnerCar.fileRequired') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('listOwnerCar.downloadTemplate')">
        <div>
          {{ $t('listOwnerCar.downloadFirst') }}
          <el-link type="primary" href="/import/importCar.xlsx" target="_blank">
            {{ $t('listOwnerCar.importTemplate') }}
          </el-link>
          <span>{{ $t('listOwnerCar.prepareData') }}</span>
        </div>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="importData" :loading="loading">
        {{ $t('listOwnerCar.import') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importData } from '@/api/car/listOwnerCarApi'

export default {
  name: 'ImportOwnerCar',
  data() {
    return {
      visible: false,
      loading: false,
      file: null,
      fileName: ''
    }
  },
  methods: {
    open() {
      this.visible = true
    },

    handleFileChange(file) {
      this.file = file.raw
      this.fileName = file.name
    },

    async importData() {
      if (!this.file) {
        this.$message.warning(this.$t('listOwnerCar.fileRequired'))
        return
      }

      if (!this.checkFileType(this.fileName)) {
        this.$message.warning(this.$t('listOwnerCar.invalidExcel'))
        return
      }

      if (!this.checkFileSize(this.file.size)) {
        this.$message.warning(this.$t('listOwnerCar.fileSizeExceed'))
        return
      }

      this.loading = true
      try {
        const response = await importData({
          file: this.file,
          importAdapt: 'importOwnerCar'
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
        this.$router.push(`/views/system/assetImportLogDetail?logId=${response.logId}&logType=importOwnerCar`)
      } catch (error) {
        console.error('导入失败:', error)
        this.$message.error(error )
      } finally {
        this.loading = false
      }
    },

    checkFileType(fileName) {
      const ext = fileName.split('.').pop().toLowerCase()
      return ['xlsx', 'xls'].includes(ext)
    },

    checkFileSize(size) {
      const maxSize = 2 * 1024 * 1024 // 2MB
      return size <= maxSize
    },

    resetForm() {
      this.file = null
      this.fileName = ''
    }
  }
}
</script>