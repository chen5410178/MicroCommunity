<template>
  <div class="view-image-container">
    <el-dialog
      :visible.sync="dialogVisible"
      fullscreen
      :show-close="false"
      custom-class="image-viewer-dialog"
    >
      <div class="image-wrapper">
        <img 
          :src="imageUrl" 
          :style="imageStyle"
          @error="handleImageError"
        />
        <i 
          class="el-icon-close close-icon" 
          @click="close"
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
      dialogVisible: false,
      imageUrl: '',
      imageStyle: {
        width: '800px',
        height: 'auto',
        display: 'block',
        margin: '0 auto'
      }
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.dialogVisible = true
      
      // 预加载图片获取实际尺寸
      const img = new Image()
      img.src = url
      img.onload = () => {
        const imgScale = img.width / img.height
        this.imageStyle.width = '800px'
        this.imageStyle.height = `${800 / imgScale}px`
      }
    },
    close() {
      this.dialogVisible = false
      this.imageUrl = ''
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    }
  }
}
</script>

<style lang="scss" scoped>
.view-image-container {
  .image-viewer-dialog {
    background-color: rgba(0, 0, 0, 0.8);
    
    .image-wrapper {
      position: relative;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .close-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 24px;
        color: #fff;
        cursor: pointer;
        z-index: 2001;
        
        &:hover {
          color: #f56c6c;
        }
      }
    }
  }
}
</style>