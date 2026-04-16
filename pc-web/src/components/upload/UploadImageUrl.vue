<!-- components/staff/UploadImageUrl.vue -->
<template>
  <div class="upload-image-container">
    <div v-for="(image, index) in photos" :key="index" class="image-item">
      <el-image :src="image" fit="cover" style="width: 100px; height: 100px" :preview-src-list="photos" />
      <i class="el-icon-delete delete-icon" @click="removeImage(index)" />
    </div>
    <div v-if="photos.length < imageCount" class="upload-button" @click="triggerUpload">
      <i class="el-icon-plus" />
    </div>
    <input ref="fileInput" type="file" accept="image/*" hidden @change="handleFileChange" />
  </div>
</template>
  
<script>
import { uploadFile } from '@/api/staff/addStaffApi'

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
      photos: [],
      photosUrl: []
    }
  },
  methods: {
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    async handleFileChange(event) {
      const file = event.target.files[0]
      if (!file) return

      if (file.size > 2 * 1024 * 1024) {
        this.$message.error('图片大小不能超过2MB')
        return
      }

      const reader = new FileReader()
      reader.onload = (e) => {
        this.photos.push(e.target.result)
      }
      reader.readAsDataURL(file)

      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        formData.append('communityId', '-1')

        const { url } = await uploadFile(formData)

        this.photosUrl.push(url)
        this.$emit('notifyUploadCoverImage', this.photosUrl)
      } catch (error) {
        this.$message.error(error.message || '上传失败')
      }

      event.target.value = null
    },
    removeImage(index) {
      this.photos.splice(index, 1)
      this.photosUrl.splice(index, 1)
      this.$emit('notifyUploadCoverImage', this.photosUrl)
    },
    clearImages() {
      this.photos = []
      this.photosUrl = []
    },
    setImages(images) {
      this.clearImages()
      images.forEach(image => {
        if (image.indexOf('base64,') > -1) {
          this.photos.push(image)
          return
        }
        if (image.indexOf('http') > -1) {
          this.photos.push(image)
          this.photosUrl.push(image)
         
          return
        }
        this.photosUrl.push(image)
      })
    }
  }
}
</script>
  
<style lang="scss" scoped>
.upload-image-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;

  .image-item {
    position: relative;
    margin-right: 10px;
    margin-bottom: 10px;

    .delete-icon {
      position: absolute;
      top: 5px;
      right: 5px;
      color: #f56c6c;
      font-size: 16px;
      cursor: pointer;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 50%;
      padding: 3px;
    }
  }

  .upload-button {
    width: 100px;
    height: 100px;
    border: 1px dashed #dcdfe6;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    color: #909399;
    font-size: 24px;

    &:hover {
      border-color: #409eff;
      color: #409eff;
    }
  }
}
</style>