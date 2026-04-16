<template>
  <div class="upload-image-container">
    <div class="image-preview">
      <div v-for="(image, index) in photos" :key="index" class="image-item">
        <el-image
          :src="image.url"
          fit="cover"
          :preview-src-list="previewList"
          style="width: 100px; height: 100px"
        ></el-image>
        <i class="el-icon-delete delete-icon" @click="removeImage(index)"></i>
      </div>
      <div v-if="photos.length < maxCount" class="upload-btn" @click="triggerUpload">
        <i class="el-icon-plus"></i>
      </div>
    </div>
    <input
      type="file"
      ref="fileInput"
      accept="image/*"
      style="display: none"
      @change="handleFileChange"
      multiple
    />
  </div>
</template>

<script>
import { uploadImage } from '@/api/fee/listApplyRoomDiscountRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UploadImageUrl',
  props: {
    maxCount: {
      type: Number,
      default: 99
    }
  },
  data() {
    return {
      photos: [],
      previewList: []
    }
  },
  methods: {
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    async handleFileChange(event) {
      const files = event.target.files
      if (!files || files.length === 0) return

      if (files.length + this.photos.length > this.maxCount) {
        this.$message.error(this.$t('uploadImage.maxCountError', { count: this.maxCount }))
        return
      }

      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        if (file.size > 2 * 1024 * 1024) {
          this.$message.error(this.$t('uploadImage.sizeError')))
          continue
        }

        try {
          const formData = new FormData()
          formData.append('uploadFile', file)
          formData.append('communityId', getCommunityId())

          const res = await uploadImage(formData)
          if (res.code === 0) {
            this.photos.push({
              fileId: res.data.fileId,
              url: res.data.url
            })
            this.previewList.push(res.data.url)
          }
        } catch (error) {
          this.$message.error(this.$t('uploadImage.uploadFailed')))
        }
      }
      event.target.value = null
    },
    removeImage(index) {
      this.photos.splice(index, 1)
      this.previewList.splice(index, 1)
    },
    getPhotos() {
      return this.photos.map(item => item.fileId)
    },
    clear() {
      this.photos = []
      this.previewList = []
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-image-container {
  .image-preview {
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

    .upload-btn {
      width: 100px;
      height: 100px;
      border: 1px dashed #dcdfe6;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      color: #909399;
      font-size: 24px;

      &:hover {
        border-color: #409eff;
        color: #409eff;
      }
    }
  }
}
</style>