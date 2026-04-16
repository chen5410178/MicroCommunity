<template>
  <div class="upload-vedio-container">
    <div v-if="progress > 0" class="progress-container">
      <el-progress :percentage="progress" :stroke-width="2" />
      <div class="file-name">{{ fileName }}</div>
    </div>
    <el-button type="primary" @click="triggerUpload">
      {{ $t('common.upload') }}
    </el-button>
    <input
      ref="fileInput"
      type="file"
      accept="video/*"
      hidden
      @change="handleFileChange"
    />
  </div>
</template>

<script>
import { uploadVedio } from '@/api/community/listRoomDecorationRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UploadVedio',
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      vedio: {},
      fileName: '',
      realFileName: '',
      progress: 0
    }
  },
  watch: {
    vedio: {
      handler(newVal) {
        this.$emit('change', newVal)
      },
      deep: true
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
        this.$message.error(this.$t('uploadVedio.sizeLimit'))
        return
      }

      this.fileName = file.name
      await this.uploadFile(file)
      event.target.value = null
    },
    async uploadFile(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      formData.append('communityId', getCommunityId())

      try {
        const response = await uploadVedio(formData, {
          onUploadProgress: (progressEvent) => {
            const percentCompleted = Math.round(
              (progressEvent.loaded * 90) / progressEvent.total
            )
            this.progress = percentCompleted
          }
        })

        this.progress = 100
        this.realFileName = response.realFileName
        this.vedio = response
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('uploadVedio.failed'))
        this.progress = 0
      }
    },
    clear() {
      this.vedio = {}
      this.fileName = ''
      this.realFileName = ''
      this.progress = 0
    },
    notifyVedio(fileName) {
      this.fileName = fileName
      this.realFileName = fileName
      this.progress = 100
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-vedio-container {
  .progress-container {
    margin-bottom: 15px;

    .file-name {
      margin-top: 5px;
      font-size: 12px;
      color: #606266;
    }
  }
}
</style>