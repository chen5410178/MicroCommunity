<template>
  <div class="upload-image-container">
    <div v-for="(image, index) in photos" :key="index" class="image-item">
      <el-image 
        :src="image" 
        fit="cover" 
        style="width: 100px; height: 100px;"
        :preview-src-list="[image]"
      ></el-image>
      <i 
        class="el-icon-delete remove-icon" 
        @click="handleRemoveImage(image)"
      ></i>
    </div>

    <div 
      v-if="photos.length < imageCount" 
      class="upload-button" 
      @click="triggerUpload"
    >
      <i class="el-icon-plus"></i>
    </div>

    <input 
      type="file" 
      ref="fileInput" 
      accept="image/*" 
      hidden 
      @change="handleFileChange"
    >
  </div>
</template>

<script>
import { uploadFile } from '@/api/oa/addAttendanceClassesStaffApi'
import { getCommunityId } from '@/api/community/communityApi'

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
      photosUrl: [],
      communityId: getCommunityId()
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
        this.$message.error(this.$t('uploadImage.imageSizeLimit'))
        return
      }

      // 预览图片
      const reader = new FileReader()
      reader.onload = (e) => {
        this.photos.push(e.target.result)
      }
      reader.readAsDataURL(file)

      // 上传图片
      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        formData.append('communityId', this.communityId)

        const res = await uploadFile(formData)
        this.photosUrl.push(res.data)
        this.$emit('notifyUploadImage', this.photosUrl)
      } catch (error) {
        this.$message.error(this.$t('uploadImage.uploadFailed'))
      }

      event.target.value = null
    },
    handleRemoveImage(image) {
      const index = this.photos.indexOf(image)
      if (index > -1) {
        this.photos.splice(index, 1)
        this.photosUrl.splice(index, 1)
        this.$emit('notifyUploadImage', this.photosUrl)
      }
    },
    clearImages() {
      this.photos = []
      this.photosUrl = []
    },
    setImages(images) {
      this.clearImages()
      images.forEach(img => {
        if (img.indexOf('base64,') > -1) {
          this.photos.push(img)
          return
        }
        // 处理其他类型的图片URL
        this.photos.push(img)
        this.photosUrl.push({ url: img })
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
  }

  .remove-icon {
    position: absolute;
    top: -10px;
    right: -10px;
    color: #f56c6c;
    cursor: pointer;
    font-size: 16px;
    background: white;
    border-radius: 50%;
    padding: 2px;
  }

  .upload-button {
    width: 100px;
    height: 100px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    color: #8c939d;
    font-size: 28px;
    background-color: #fbfdff;

    &:hover {
      border-color: #409eff;
    }
  }
}
</style>