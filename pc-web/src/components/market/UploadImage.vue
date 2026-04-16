<template>
  <div class="upload-image-wrapper">
    <div v-for="(image, index) in images" :key="index" class="image-item">
      <el-image
        style="width: 100px; height: 100px"
        :src="image"
        fit="cover"
        :preview-src-list="images">
      </el-image>
      <i class="el-icon-delete" @click="removeImage(index)"></i>
    </div>
    <div 
      v-if="images.length < imageCount" 
      class="upload-button" 
      @click="triggerUpload">
      <i class="el-icon-plus"></i>
    </div>
    <input 
      type="file" 
      ref="fileInput" 
      accept="image/*" 
      style="display: none" 
      @change="handleFileChange">
  </div>
</template>

<script>
export default {
  name: 'UploadImage',
  props: {
    imageCount: {
      type: Number,
      default: 1
    },
    defaultImages: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      images: []
    }
  },
  watch: {
    defaultImages: {
      immediate: true,
      handler(val) {
        if (val && val.length > 0) {
          this.images = [...val]
        }
      }
    }
  },
  methods: {
    triggerUpload() {
      this.$refs.fileInput.click()
    },
    handleFileChange(event) {
      const files = event.target.files
      if (!files || files.length === 0) return
      
      const file = files[0]
      if (file.size > 2 * 1024 * 1024) {
        this.$message.error(this.$t('uploadImage.sizeLimit'))
        return
      }

      const reader = new FileReader()
      reader.onload = (e) => {
        this.images.push(e.target.result)
        this.$emit('change', [...this.images])
      }
      reader.readAsDataURL(file)
      event.target.value = null
    },
    removeImage(index) {
      this.images.splice(index, 1)
      this.$emit('change', [...this.images])
    },
    setImages(images) {
      this.images = [...images]
    },
    clear() {
      this.images = []
    }
  }
}
</script>

<style scoped>
.upload-image-wrapper {
  display: flex;
  flex-wrap: wrap;
}

.image-item {
  position: relative;
  margin-right: 10px;
  margin-bottom: 10px;
}

.image-item .el-icon-delete {
  position: absolute;
  top: -10px;
  right: -10px;
  font-size: 18px;
  color: #F56C6C;
  cursor: pointer;
  background: #fff;
  border-radius: 50%;
}

.upload-button {
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  color: #8c939d;
  font-size: 28px;
}

.upload-button:hover {
  border-color: #409EFF;
}
</style>