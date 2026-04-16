<template>
    <div id="viewImage" v-show="viewImageInfo.showImage">
      <div class="image-container">
        <el-image
          :src="viewImageInfo.url"
          :style="{ width: viewImageInfo.imgWidth + 'px', height: viewImageInfo.imgHeight + 'px' }"
          @error="handleImageError"
        />
        <i class="el-icon-close close-icon" @click="closeImage" />
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ViewImage',
    data() {
      return {
        viewImageInfo: {
          url: '',
          showImage: false,
          imgWidth: 800,
          imgHeight: 800
        }
      }
    },
    methods: {
      open(param) {
        this.viewImageInfo.url = param.url
        this.viewImageInfo.showImage = true
        this.launchIntoFullscreen()
        const img = new Image()
        img.src = param.url
        img.onload = () => {
          const imgScale = img.width / img.height
          this.viewImageInfo.imgWidth = 800
          this.viewImageInfo.imgHeight = 800 / imgScale
        }
      },
      closeImage() {
        this.exitFullscreen()
        this.viewImageInfo.showImage = false
      },
      launchIntoFullscreen() {
        const full = document.getElementById('viewImage')
        if (full.requestFullscreen) full.requestFullscreen()
        else if (full.mozRequestFullScreen) full.mozRequestFullScreen()
        else if (full.webkitRequestFullscreen) full.webkitRequestFullscreen()
        else if (full.msRequestFullscreen) full.msRequestFullscreen()
      },
      exitFullscreen() {
        if (document.exitFullscreen) document.exitFullscreen()
        else if (document.mozCancelFullScreen) document.mozCancelFullScreen()
        else if (document.webkitExitFullscreen) document.webkitExitFullscreen()
      },
      handleImageError() {
        this.viewImageInfo.url = '/img/noPhoto.jpg'
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  #viewImage {
    .image-container {
      position: fixed;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      background-color: #fff;
    }
    .close-icon {
      position: absolute;
      right: 20px;
      top: 20px;
      font-size: 20px;
      color: red;
      cursor: pointer;
    }
  }
  </style>