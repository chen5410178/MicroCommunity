<template>
  <div class="upload-image-container">
    <div v-for="(image, index) in photos" :key="index" class="image-item">
      <el-image
        :src="image"
        fit="cover"
        style="width: 100px; height: 100px"
        :preview-src-list="photos"
      >
        <div slot="error" class="image-slot">
          <i class="el-icon-picture-outline"></i>
        </div>
      </el-image>
      <i
        class="el-icon-delete remove-icon"
        @click="removeImage(image)"
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
    />
  </div>
</template>

<script>
import { uploadFile } from '@/api/scm/editReserveServiceApi'
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
      photos: [], // 用于显示的图片数组(base64)
      photosUrl: [], // 用于传递给父组件的图片数组({fileId, url})
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    notifyPhotos(photos) {
      this.photos = []
      this.photosUrl = []
      
      photos.forEach(photo => {
        if (photo.indexOf('base64,') > -1) {
          this.photos.push(photo)
          return
        }
        
        if (photo.indexOf('http') > -1) {
          this.photos.push(photo)
          this.photosUrl.push({ url: photo })
          return
        }
        
        const url = `/callComponent/download/getFile/file?fileId=${photo}&communityId=-1`
        this.photos.push(url)
        this.photosUrl.push({ fileId: photo, url })
      })
    },
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    async handleFileChange(event) {
      const files = event.target.files
      if (!files || files.length === 0) return
      
      const file = files[0]
      if (file.size > 2 * 1024 * 1024) {
        this.$message.error(this.$t('uploadImage.sizeLimit'))
        return
      }
      
      // 预览图片
      const reader = new FileReader()
      reader.onload = (e) => {
        this.photos.push(e.target.result)
        this.$emit('notify-upload-cover-image', this.photosUrl)
      }
      reader.readAsDataURL(file)
      
      // 上传图片
      try {
        const formData = new FormData()
        formData.append('uploadFile', file)
        formData.append('communityId', this.communityId)
        
        const { data } = await uploadFile(formData)
        this.photosUrl.push(data)
        this.$emit('notify-upload-cover-image', this.photosUrl)
      } catch (error) {
        console.error('上传失败:', error)
        this.$message.error(this.$t('uploadImage.uploadError'))
      } finally {
        event.target.value = null
      }
    },
    removeImage(image) {
      const index = this.photos.indexOf(image)
      if (index > -1) {
        this.photos.splice(index, 1)
        this.photosUrl.splice(index, 1)
        this.$emit('notify-upload-cover-image', this.photosUrl)
      }
    },
    clearImages() {
      this.photos = []
      this.photosUrl = []
      this.$emit('notify-upload-cover-image', this.photosUrl)
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
    
    .remove-icon {
      position: absolute;
      top: -10px;
      right: -10px;
      color: #f56c6c;
      font-size: 18px;
      cursor: pointer;
      background: white;
      border-radius: 50%;
      z-index: 1;
      
      &:hover {
        color: #f78989;
      }
    }
  }
  
  .upload-button {
    width: 100px;
    height: 100px;
    border: 1px dashed #dcdfe6;
    border-radius: 4px;
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
  
  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
  }
}
</style>