<template>
  <div class="upload-file-container">
    <div class="progress" v-if="uploadFileInfo.progress > 0">
      <el-progress 
        :percentage="uploadFileInfo.progress" 
        :stroke-width="2" 
        :show-text="false" />
    </div>
    <div class="file-info" v-if="uploadFileInfo.progress > 0">
      <span>{{ uploadFileInfo.fileName }}</span>
    </div>
    <div class="upload-btn">
      <el-button 
        type="primary" 
        @click="_uploadFile">
        {{ $t('uploadFile.uploadBtn') }}
      </el-button>
      <input 
        type="file" 
        class="file-input" 
        accept="*" 
        id="uploadFile" 
        hidden 
        @change="_chooseFile($event)" />
    </div>
  </div>
</template>

<script>
import { uploadVedio } from '@/api/oa/newOaWorkflowFormEditApi'

export default {
  name: 'UploadFile',
  props: {
    callBackListener: {
      type: String,
      required: true
    },
    callBackFunction: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      uploadFileInfo: {
        vedio: {},
        fileName: '',
        realFileName: '',
        progress: 0
      }
    }
  },
  watch: {
    uploadFileInfo: {
      deep: true,
      handler(newVal) {
        this.$emit(this.callBackListener, this.callBackFunction, newVal)
      }
    }
  },
  created() {
    this.$on('clearVedio', this.clearVedio)
    this.$on('notifyVedio', this.notifyVedio)
  },
  methods: {
    _uploadFile() {
      document.getElementById('uploadFile').click()
    },
    _chooseFile(event) {
      const files = event.target.files
      if (files && files.length > 0) {
        const file = files[0]
        if (file.size > 1024 * 1024 * 20) {
          this.$message.error(this.$t('uploadFile.sizeLimit'))
          return false
        }
        this.uploadFileInfo.fileName = file.name
        this._doUploadFile(file)
      }
    },
    async _doUploadFile(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)

      try {
        const res = await uploadVedio(formData, {
          onUploadProgress: progressEvent => {
            const rate = progressEvent.loaded / progressEvent.total
            if (rate < 0.9) {
              this.uploadFileInfo.progress = Math.floor(rate * 100)
            }
          }
        })

        this.uploadFileInfo.progress = 100
        this.$message.success(this.$t('common.operationSuccess'))
        this.uploadFileInfo.fileName = res.fileName
        this.uploadFileInfo.realFileName = res.realFileName
        this.$emit(this.callBackListener, this.callBackFunction, res)
      } catch (error) {
        console.error('上传文件失败:', error)
        this.$message.error(this.$t('uploadFile.uploadError'))
      }
    },
    clearVedio() {
      this.uploadFileInfo = {
        vedio: {},
        fileName: '',
        realFileName: '',
        progress: 0
      }
    },
    notifyVedio(fileName) {
      this.uploadFileInfo.fileName = fileName
      this.uploadFileInfo.realFileName = fileName
      this.uploadFileInfo.progress = 100
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-file-container {
  margin-bottom: 20px;

  .progress {
    margin-bottom: 10px;
  }

  .file-info {
    margin-bottom: 10px;
    font-size: 14px;
    color: #606266;
  }

  .file-input {
    display: none;
  }
}
</style>