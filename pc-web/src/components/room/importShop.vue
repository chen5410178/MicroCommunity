<template>
  <el-dialog :title="$t('shops.importShop.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" class="text-left">
      <el-form-item :label="$t('shops.importShop.selectFile')">
        <el-upload ref="upload" action="" :auto-upload="false" :on-change="handleFileChange" accept=".xls,.xlsx"
          class="margin-left">
          <el-button size="small" type="primary">{{ $t('shops.importShop.clickUpload') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('shops.importShop.requiredFile') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('shops.importShop.downloadTemplate')">
        <span class="margin-left">{{ $t('shops.importShop.downloadFirst') }}</span>
        <el-link type="primary" :href="templateUrl" target="_blank">
          {{ $t('shops.importShop.shopTemplate') }}
        </el-link>
        <span>{{ $t('shops.importShop.prepareData') }}</span>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { importShop } from '@/api/room/importShopApi'

export default {
  name: 'ImportShop',
  data() {
    return {
      visible: false,
      form: {
        communityId: this.getCommunityId(),
        file: null
      },
      fileName: '',
      templateUrl: '/import/importShop.xlsx'
    }
  },
  methods: {
    getCommunityId() {
      let communityId = '';
      try {
        communityId = localStorage.getItem('communityId') || '';
      } catch (e) {
        console.error('Failed to get communityId:', e);
      }
      return communityId;
    },
    open() {
      this.visible = true
      this.resetForm()
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.form.file = null
      this.fileName = ''
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles()
      }
    },
    handleFileChange(file) {
      this.form.file = file.raw
      this.fileName = file.name
    },
    validate() {
      if (!this.form.file) {
        this.$message.error(this.$t('shops.importShop.fileRequired'))
        return false
      }

      const fileType = this.fileName.split('.').pop().toLowerCase()
      if (!['xls', 'xlsx'].includes(fileType)) {
        this.$message.error(this.$t('shops.importShop.invalidFormat'))
        return false
      }

      if (this.form.file.size > 2 * 1024 * 1024) {
        this.$message.error(this.$t('shops.importShop.fileSizeExceeded'))
        return false
      }

      return true
    },
    async handleImport() {
      if (!this.validate()) return

      try {
        const formData = new FormData()
        formData.append('uploadFile', this.form.file)
        formData.append('communityId', this.form.communityId)
        formData.append('importAdapt', 'importShop')

        const response = await importShop(formData)

        this.handleClose()
        this.$router.push({
          path: '/views/system/assetImportLogDetail',
          query: {
            logId: response.logId,
            logType: 'importShop'
          }
        })
      } catch (error) {
        this.$message.error(error)
      }
    },
  }
}
</script>
