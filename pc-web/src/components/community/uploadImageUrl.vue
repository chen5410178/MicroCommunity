<template>
  <div class="upload-image-container">
    <div v-for="(image, index) in photos" :key="index" class="image-item">
      <el-image
        :src="getImageUrl(image)"
        fit="cover"
        style="width: 100px; height: 100px"
        :preview-src-list="[getImageUrl(image)]"
      />
      <i class="el-icon-delete delete-icon" @click="removeImage(index)" />
    </div>
    <div
      v-if="photos.length < imageCount"
      class="upload-button"
      @click="triggerUpload"
    >
      <i class="el-icon-plus" />
    </div>
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      hidden
      @change="handleFileChange"
    />
  </div>
</template>

<script>
import { uploadImage } from '@/api/community/listRoomDecorationRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UploadImageUrl',
  props: {
    imageCount: {
      type: Number,
      default: 99
    },
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
      photos: [],
      photosUrl: []
    }
  },
  watch: {
    photosUrl: {
      handler(newVal) {
        this.$emit('change', newVal)
      },
      deep: true
    }
  },
  methods: {
    getImageUrl(image) {
      if (image.indexOf('base64,') > -1) return image
      if (image.indexOf('http') > -1 || image.indexOf('https') > -1) return image
      return `/callComponent/download/getFile/file?fileId=${image}&communityId=${getCommunityId()}`
    },
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

      // Preview image
      const reader = new FileReader()
      reader.onload = (e) => {
        this.photos.push(e.target.result)
      }
      reader.readAsDataURL(file)

      // Upload image
      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        formData.append('communityId', getCommunityId())

        const response = await uploadImage(formData)
        this.photosUrl.push(response)
      } catch (error) {
        this.$message.error(this.$t('uploadImage.uploadFailed'))
      }

      event.target.value = null
    },
    removeImage(index) {
      this.photos.splice(index, 1)
      this.photosUrl.splice(index, 1)
    },
    clear() {
      this.photos = []
      this.photosUrl = []
    },
    notifyPhotos(photos) {
      this.clear()
      photos.forEach(photo => {
        if (photo.indexOf('base64,') > -1) {
          this.photos.push(photo)
          return
        }
        if (photo.indexOf('http') > -1 || photo.indexOf('https') > -1) {
          this.photos.push(photo)
          const fileId = this.getFileIdFromUrl(photo)
          if (fileId) {
            this.photosUrl.push({ fileId, url: photo })
          }
          return
        }
        this.photos.push(this.getImageUrl(photo))
        this.photosUrl.push({ fileId: photo, url: this.getImageUrl(photo) })
      })
    },
    getFileIdFromUrl(url) {
      const match = url.match(/fileId=([^&]+)/)
      return match ? match[1] : null
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
    width: 100px;
    height: 100px;

    .delete-icon {
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