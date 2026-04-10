<template>
  <div v-show="visible" class="image-viewer-wrapper">
    <div class="image-viewer-mask" @click="close"></div>
    <div class="image-viewer-content">
      <img 
        :src="imageUrl" 
        :style="imageStyle"
        @error="handleImageError"
      />
      <span class="image-viewer-close" @click="close">
        <i class="el-icon-close"></i>
      </span>
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
      imageWidth: 0,
      imageHeight: 0
    }
  },
  computed: {
    imageStyle() {
      return {
        width: this.imageWidth ? `${this.imageWidth}px` : 'auto',
        height: this.imageHeight ? `${this.imageHeight}px` : 'auto',
        maxWidth: '90vw',
        maxHeight: '90vh'
      }
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.visible = true
      this.$nextTick(() => {
        this.calculateImageSize(url)
      })
    },
    close() {
      this.visible = false
      this.imageUrl = ''
      this.imageWidth = 0
      this.imageHeight = 0
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    },
    calculateImageSize(url) {
      const img = new Image()
      img.src = url
      img.onload = () => {
        const maxWidth = window.innerWidth * 0.8
        const maxHeight = window.innerHeight * 0.8
        const ratio = Math.min(maxWidth / img.width, maxHeight / img.height, 1)
        this.imageWidth = img.width * ratio
        this.imageHeight = img.height * ratio
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.image-viewer-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;

  .image-viewer-mask {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
  }

  .image-viewer-content {
    position: relative;
    z-index: 1;
    padding: 20px;
    background-color: #fff;
    border-radius: 4px;
    text-align: center;

    img {
      display: block;
      margin: 0 auto;
      object-fit: contain;
    }
  }

  .image-viewer-close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 24px;
    color: #f56c6c;
    cursor: pointer;
    z-index: 2;

    &:hover {
      color: #f78989;
    }
  }
}
</style>