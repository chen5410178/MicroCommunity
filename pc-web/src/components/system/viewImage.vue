<template>
  <div class="view-image-container">
    <el-dialog
      :visible.sync="visible"
      :width="dialogWidth"
      :show-close="true"
      custom-class="image-viewer-dialog"
      @close="close"
      center
    >
      <div class="image-wrapper">
        <img
          :src="imageInfo.url"
          :style="{
            maxWidth: '100%',
            maxHeight: imageInfo.maxHeight + 'px'
          }"
          @error="handleImageError"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ViewImage',
  data() {
    return {
      visible: false,
      dialogWidth: '50%',
      imageInfo: {
        url: '',
        maxHeight: 600
      }
    }
  },
  methods: {
    open(params) {
      this.imageInfo.url = params.url
      this.visible = true
      
      // 根据屏幕尺寸动态调整对话框大小
      const screenWidth = window.innerWidth
      if (screenWidth < 768) {
        this.dialogWidth = '90%'
        this.imageInfo.maxHeight = 400
      } else if (screenWidth < 1200) {
        this.dialogWidth = '70%'
        this.imageInfo.maxHeight = 500
      } else {
        this.dialogWidth = '50%'
        this.imageInfo.maxHeight = 600
      }
    },
    close() {
      this.visible = false
      this.imageInfo = {
        url: '',
        maxHeight: 600
      }
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    }
  }
}
</script>

<style lang="scss" scoped>
.view-image-container {
  ::v-deep .image-viewer-dialog {
    .el-dialog__header {
      padding: 10px;
    }
    
    .el-dialog__body {
      padding: 20px;
      text-align: center;
    }
    
    .image-wrapper {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 200px;
      
      img {
        display: block;
        width: auto;
        height: auto;
        object-fit: contain;
        border-radius: 4px;
      }
    }
  }
}
</style>