<template>
  <div v-show="visible" class="image-viewer">
    <div class="image-container">
      <img
        :src="imageUrl"
        :style="{
          width: imageWidth + 'px',
          height: imageHeight + 'px'
        }"
        @error="handleImageError"
      />
      <i class="el-icon-close close-icon" @click="close"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewImage',
  data() {
    return {
      visible: false,
      imageUrl: '',
      imageWidth: 800,
      imageHeight: 800
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.visible = true
      this.enterFullscreen()
      
      // 计算图片宽高比例
      const img = new Image()
      img.src = url
      img.onload = () => {
        const ratio = img.width / img.height
        this.imageHeight = this.imageWidth / ratio
      }
    },
    close() {
      this.exitFullscreen()
      this.visible = false
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    },
    enterFullscreen() {
      const element = this.$el
      if (element.requestFullscreen) {
        element.requestFullscreen()
      } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen()
      } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen()
      } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen()
      }
    },
    exitFullscreen() {
      if (document.exitFullscreen) {
        document.exitFullscreen()
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen()
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.image-viewer {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;

  .image-container {
    position: relative;
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;

    img {
      max-width: 80vw;
      max-height: 80vh;
      object-fit: contain;
    }

    .close-icon {
      position: absolute;
      top: 10px;
      right: 10px;
      font-size: 24px;
      color: #f56c6c;
      cursor: pointer;
      z-index: 1;
    }
  }
}
</style>
