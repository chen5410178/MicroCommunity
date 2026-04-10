<template>
  <div class="upload-image-container">
    <div class="image-preview" v-for="(image, index) in images" :key="index">
      <el-image
        style="width: 100px; height: 100px; margin-right: 10px;"
        :src="image.url"
        fit="cover"
        :preview-src-list="previewList">
      </el-image>
      <i class="el-icon-delete delete-icon" @click="removeImage(index)"></i>
    </div>
    
    <el-upload
      v-if="images.length < imageCount"
      class="upload-button"
      action=""
      :show-file-list="false"
      :before-upload="beforeUpload"
      :http-request="handleUpload">
      <i class="el-icon-plus"></i>
    </el-upload>
  </div>
</template>

<script>
import { uploadFile } from '@/api/common'

export default {
  name: 'UploadImageUrl',
  props: {
    imageCount: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      images: [],
      previewList: []
    }
  },
  methods: {
    beforeUpload(file) {
      const isImage = file.type.indexOf('image/') === 0
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error(this.$t('upload.imageTypeError'))
      }
      if (!isLt2M) {
        this.$message.error(this.$t('upload.imageSizeError'))
      }
      return isImage && isLt2M
    },
    async handleUpload({ file }) {
      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        
        const response = await uploadFile(formData)
        this.images.push(response.data)
        this.previewList = this.images.map(img => img.url)
        this.$emit('change', this.images)
      } catch (error) {
        this.$message.error(error.message || this.$t('upload.uploadFailed'))
      }
    },
    removeImage(index) {
      this.images.splice(index, 1)
      this.previewList = this.images.map(img => img.url)
      this.$emit('change', this.images)
    },
    clear() {
      this.images = []
      this.previewList = []
      this.$emit('change', [])
    },
    setImages(urls) {
      this.images = urls.map(url => ({ url }))
      this.previewList = this.images.map(img => img.url)
      this.$emit('change', this.images)
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-image-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  
  .image-preview {
    position: relative;
    margin-right: 10px;
    margin-bottom: 10px;
    
    .delete-icon {
      position: absolute;
      top: -10px;
      right: 0;
      color: #f56c6c;
      font-size: 18px;
      cursor: pointer;
      background: white;
      border-radius: 50%;
      padding: 2px;
    }
  }
  
  .upload-button {
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    
    &:hover {
      border-color: #409EFF;
    }
    
    i {
      font-size: 28px;
      color: #8c939d;
    }
  }
}
</style>