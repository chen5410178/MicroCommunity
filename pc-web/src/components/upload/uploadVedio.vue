<template>
  <div class="upload-vedio-container">
    <div v-if="progress > 0" class="progress-container">
      <el-progress :percentage="progress" :stroke-width="2"></el-progress>
      <div class="file-name">{{ fileName }}</div>
    </div>

    <el-button type="primary" size="small" @click="triggerUpload" :disabled="uploading">
      <i class="el-icon-upload"></i>
      {{ $t('uploadVedio.uploadButton') }}
    </el-button>
    <input type="file" ref="fileInput" accept="video/*" style="display: none" @change="handleFileChange" />
  </div>
</template>

<script>
import { uploadVedio } from '@/api/fee/listApplyRoomDiscountRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UploadVedio',
  data() {
    return {
      fileName: '',
      realFileName: '',
      progress: 0,
      uploading: false
    }
  },
  methods: {
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    async handleFileChange(event) {
      const file = event.target.files[0]
      if (!file) return

      if (file.size > 500 * 1024 * 1024) {
        this.$message.error(this.$t('uploadVedio.sizeError'))
        return
      }

      this.fileName = file.name
      this.uploading = true
      this.progress = 0

      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        formData.append('communityId', getCommunityId())

        const res = await uploadVedio(formData, {
          onUploadProgress: (progressEvent) => {
            this.progress = Math.round((progressEvent.loaded * 90) / progressEvent.total)
          }
        })

        if (res.realFileName) {
          this.progress = 100
          this.realFileName = res.realFileName
          this.$message.success(this.$t('common.operationSuccess'))
        }
      } catch (error) {
        this.$message.error(this.$t('uploadVedio.uploadFailed'))
      } finally {
        this.uploading = false
        event.target.value = null
      }
    },
    getVideoName() {
      return this.realFileName
    },
    clear() {
      this.fileName = ''
      this.realFileName = ''
      this.progress = 0
      this.uploading = false
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-vedio-container {
  .progress-container {
    margin-bottom: 10px;

    .file-name {
      font-size: 12px;
      color: #606266;
      margin-top: 5px;
      word-break: break-all;
    }
  }
}
</style>