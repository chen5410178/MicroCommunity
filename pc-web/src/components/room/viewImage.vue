<template>
  <div class="image-viewer" v-show="visible" @click.self="close">
    <div class="image-container">
      <el-image
        :src="imageUrl"
        fit="contain"
        :style="{width: imgWidth + 'px', height: imgHeight + 'px'}"
      >
        <div slot="error" class="image-error">
          <img src="/img/noPhoto.jpg" :style="{width: imgWidth + 'px', height: imgHeight + 'px'}">
        </div>
      </el-image>
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
      imgHeight: 600
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.visible = true
      this.calculateImageSize(url)
      document.body.style.overflow = 'hidden'
    },
    close() {
      this.visible = false
      this.imageUrl = ''
      document.body.style.overflow = ''
    },
    calculateImageSize(url) {
      const img = new Image()
      img.src = url
      img.onload = () => {
        const maxWidth = window.innerWidth * 0.8
        const maxHeight = window.innerHeight * 0.8
        const ratio = img.width / img.height
        
        if (img.width > maxWidth) {
          this.imgWidth = maxWidth
          this.imgHeight = maxWidth / ratio
        } else if (img.height > maxHeight) {
          this.imgHeight = maxHeight
          this.imgWidth = maxHeight * ratio
        } else {
          this.imgWidth = img.width
          this.imgHeight = img.height
        }
      }
      img.onerror = () => {
        this.imgWidth = 800
        this.imgHeight = 600
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
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;

  .image-container {
    position: relative;
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;
    max-width: 90vw;
    max-height: 90vh;

    .close-icon {
      position: absolute;
      top: 10px;
      right: 10px;
      font-size: 24px;
      color: #f56c6c;
      cursor: pointer;
      z-index: 1;
      background-color: rgba(255, 255, 255, 0.7);
      border-radius: 50%;
      padding: 5px;
    }
  }

  .image-error {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f7fa;
    width: 100%;
    height: 100%;
  }
}
</style>