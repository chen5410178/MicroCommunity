<template>
  <div class="upload-image-container">
    <div v-for="(image, index) in images" :key="index" class="image-item">
      <el-image
        :src="image"
        fit="cover"
        style="width: 100px; height: 100px;"
        :preview-src-list="[image]"
      ></el-image>
      <i class="el-icon-delete" @click="removeImage(index)"></i>
    </div>
    <el-upload
      v-if="images.length < maxCount"
      action=""
      :show-file-list="false"
      :before-upload="beforeUpload"
      :http-request="handleUpload"
    >
      <div class="upload-btn">
        <i class="el-icon-plus"></i>
      </div>
    </el-upload>
  </div>
</template>

<script>
import { uploadFile } from '@/api/mall/convenienceMenusManageApi'

export default {
  name: 'UploadImage',
  props: {
    maxCount: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      images: []
    }
  },
  methods: {
    beforeUpload(file) {
      const isImage = file.type.includes('image/')
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
        const { url } = await uploadFile(formData)
        this.images.push(url)
        this.$emit('change', url)
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    removeImage(index) {
      this.images.splice(index, 1)
      this.$emit('change', this.images[0] || '')
    },
    clear() {
      this.images = []
      this.$emit('change', '')
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

    .el-icon-delete {
      position: absolute;
      top: 5px;
      right: 5px;
      color: #f56c6c;
      cursor: pointer;
      font-size: 16px;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 50%;
      padding: 5px;
    }
  }

  .upload-btn {
    width: 100px;
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    background-color: #fbfdff;

    &:hover {
      border-color: #409eff;
    }

    i {
      font-size: 28px;
      color: #8c939d;
    }
  }
}
</style>