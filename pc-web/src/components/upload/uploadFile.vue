<template>
  <div class="upload-file-wrapper">
    <div class="progress" v-if="progress > 0">
      <el-progress :percentage="progress" :stroke-width="2" />
    </div>
    <div class="file-name" v-if="fileName">
      {{ fileName }}
    </div>
    <el-upload
      class="upload-demo"
      action=""
      :auto-upload="false"
      :show-file-list="false"
      :on-change="handleFileChange"
    >
      <el-button size="small" type="primary">
        {{ $t('common.upload') }}
      </el-button>
    </el-upload>
  </div>
</template>

<script>
import { uploadFile } from '@/api/system/paymentPoolApi'

export default {
  name: 'UploadFile',
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
      progress: 0,
      fileName: '',
      realFileName: ''
    }
  },
  methods: {
    clear() {
      this.progress = 0
      this.fileName = ''
      this.realFileName = ''
    },
    handleFileChange(file) {
      if (file.size > 20 * 1024 * 1024) {
        this.$message.error(this.$t('uploadFile.sizeLimit'))
        return false
      }
      
      this.fileName = file.name
      this.doUpload(file.raw)
    },
    setFilePath(fileName) {
      this.fileName = fileName
      this.realFileName = fileName
      this.progress = 100
    },
    async doUpload(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      
      try {
        const config = {
          onUploadProgress: progressEvent => {
            const percent = Math.round(
              (progressEvent.loaded * 100) / progressEvent.total
            )
            this.progress = percent < 90 ? percent : 90
          }
        }
        
        const  data  = await uploadFile(formData, config)
        this.progress = 100
        this.realFileName = data.realFileName
        this.$emit('notify', data)
        
        if (this.callBackListener && this.callBackFunction) {
          this.$emit(this.callBackFunction, data)
        }
        
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('uploadFile.error'))
        console.error('上传文件失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-file-wrapper {
  .progress {
    margin-bottom: 10px;
  }
  
  .file-name {
    margin-bottom: 10px;
    font-size: 14px;
    color: #606266;
  }
}
</style>