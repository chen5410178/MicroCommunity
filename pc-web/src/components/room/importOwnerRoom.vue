<template>
  <el-dialog :title="$t('room.importOwnerRoom.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" class="text-left">
      <el-form-item :label="$t('room.importOwnerRoom.selectFile')">
        <el-upload ref="upload" action="" :auto-upload="false" :on-change="handleFileChange" accept=".xls,.xlsx"
          class="margin-left">
          <el-button size="small" type="primary">{{ $t('room.importOwnerRoom.clickUpload') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('room.importOwnerRoom.requiredFile') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('room.importOwnerRoom.downloadTemplate')">
        <span class="margin-left">{{ $t('room.importOwnerRoom.downloadFirst') }}</span>
        <el-link type="primary" :href="templateUrl" target="_blank">
          {{ $t('room.importOwnerRoom.propertyTemplate') }}
        </el-link>
        <span>{{ $t('room.importOwnerRoom.prepareData') }}</span>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { importOwnerRoom } from '@/api/room/importOwnerRoomApi'

export default {
  name: 'ImportOwnerRoom',
  data() {
    return {
      visible: false,
      form: {
        communityId: this.getCommunityId(),
        file: null
      },
      fileName: '',
      templateUrl: '/import/importRoom.xlsx'
    }
  },
  methods: {
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
        this.$message.error(this.$t('room.importOwnerRoom.fileRequired'))
        return false
      }

      const fileType = this.fileName.split('.').pop().toLowerCase()
      if (!['xls', 'xlsx'].includes(fileType)) {
        this.$message.error(this.$t('room.importOwnerRoom.invalidFormat'))
        return false
      }

      if (this.form.file.size > 2 * 1024 * 1024) {
        this.$message.error(this.$t('room.importOwnerRoom.fileSizeExceeded'))
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
        formData.append('importAdapt', 'importRoomOwner')

        const response = await importOwnerRoom(formData)

        this.handleClose()
        this.$router.push({
          path: '/views/system/assetImportLogDetail',
          query: {
            logId: response.logId,
            logType: 'importRoomOwner'
          }
        })
      } catch (error) {
        this.$message.error(error)
      }
    },
  }
}
</script>