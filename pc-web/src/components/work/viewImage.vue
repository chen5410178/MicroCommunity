<template>
  <div v-show="visible" class="image-viewer">
    <div class="image-container">
      <img :src="imageUrl" :style="{ width: imgWidth + 'px', height: imgHeight + 'px' }" />
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
      imgWidth: 800,
      imgHeight: 800
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.visible = true
      this.enterFullscreen()
      
      const img = new Image()
      img.src = url
      img.onload = () => {
        const imgScale = img.width / img.height
        this.imgWidth = 800
        this.imgHeight = 800 / imgScale
      }
    },
    close() {
      this.exitFullscreen()
      this.visible = false
    },
    enterFullscreen() {
      const element = document.documentElement
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
  },
  mounted() {
    document.addEventListener('fullscreenchange', () => {
      if (!document.fullscreenElement) {
        this.visible = false
      }
    })
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
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  
  .image-container {
    position: relative;
    
    img {
      max-width: 90vw;
      max-height: 90vh;
      object-fit: contain;
    }
    
    .close-icon {
      position: absolute;
      top: 10px;
      right: 10px;
      font-size: 24px;
      color: #f56c6c;
      cursor: pointer;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 50%;
      padding: 5px;
      
      &:hover {
        color: #f00;
      }
    }
  }
}
</style>