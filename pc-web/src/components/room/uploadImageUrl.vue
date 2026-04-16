<template>
  <div class="upload-image-wrapper">
    <div v-for="(image, index) in photos" :key="index" class="image-preview">
      <el-image
        :src="getImageUrl(image)"
        fit="cover"
        class="preview-image"
        :preview-src-list="previewList"
      >
        <div slot="error" class="image-error">
          <img src="/img/noPhoto.jpg" class="error-image">
        </div>
      </el-image>
      <i class="el-icon-delete delete-icon" @click="removeImage(index)"></i>
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
      class="file-input"
      @change="handleFileChange"
    />
  </div>
</template>

<script>
import { uploadFile } from '@/api/common'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UploadImageUrl',
  props: {
    imageCount: {
      type: Number,
      default: 99
    }
  },
  data() {
    return {
      photos: [],    // 用于显示的图片数组
      photosUrl: []  // 包含fileId和url的对象数组
    }
  },
  computed: {
    previewList() {
      return this.photos.map(item => this.getImageUrl(item))
    }
  },
  methods: {
    getImageUrl(item) {
      if (typeof item === 'string') {
        if (item.startsWith('http') || item.startsWith('https') || item.startsWith('data:')) {
          return item
        }
        return `/callComponent/download/getFile/file?fileId=${item}&communityId=-1&time=${new Date()}`
      }
      return item.url || ''
    },
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    handleFileChange(event) {
      const file = event.target.files[0]
      if (!file) return

      if (file.size > 2 * 1024 * 1024) {
        this.$message.error(this.$t('upload.imageSizeLimit'))
        return
      }

      const reader = new FileReader()
      reader.onload = (e) => {
        const base64Image = e.target.result
        this.photos.push(base64Image)
        this.uploadFile(file)
      }
      reader.readAsDataURL(file)
      event.target.value = null
    },
    uploadFile(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      formData.append('communityId', getCommunityId())

      uploadFile(formData)
        .then(response => {
          const newPhoto = {
            fileId: response.data.fileId,
            url: response.data.url
          }
          this.photosUrl.push(newPhoto)
          this.$emit('change', this.photosUrl)
        })
        .catch(error => {
          this.$message.error(error.message || this.$t('upload.failed'))
          this.photos.pop()
        })
    },
    removeImage(index) {
      this.photos.splice(index, 1)
      this.photosUrl.splice(index, 1)
      this.$emit('change', this.photosUrl)
    },
    setPhotos(photos) {
      this.photos = [...photos]
      this.photosUrl = photos.map(photo => ({
        fileId: photo,
        url: `/callComponent/download/getFile/file?fileId=${photo}&communityId=-1&time=${new Date()}`
      }))
      this.$emit('change', this.photosUrl)
    },
    clear() {
      this.photos = []
      this.photosUrl = []
      this.$emit('change', this.photosUrl)
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-image-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;

  .image-preview {
    position: relative;
    width: 100px;
    height: 100px;

    .preview-image {
      width: 100%;
      height: 100%;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
    }

    .delete-icon {
      position: absolute;
      top: -8px;
      right: -8px;
      font-size: 16px;
      color: #f56c6c;
      background: #fff;
      border-radius: 50%;
      cursor: pointer;
    }
  }

  .upload-button {
    width: 100px;
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px dashed #c0ccda;
    border-radius: 4px;
    background-color: #fbfdff;
    color: #8c939d;
    font-size: 28px;
    cursor: pointer;

    &:hover {
      border-color: #409eff;
    }
  }

  .file-input {
    display: none;
  }

  .image-error {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f7fa;

    .error-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}
</style>