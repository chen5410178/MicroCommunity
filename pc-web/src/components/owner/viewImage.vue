<template>
  <div class="image-viewer">
    <el-dialog 
      :visible.sync="dialogVisible" 
      fullscreen 
      :show-close="false"
      @close="closeDialog">
      <div class="image-container">
        <el-image 
          :src="imageUrl" 
          fit="contain"
          :style="{ width: imageWidth + 'px', height: imageHeight + 'px' }">
        </el-image>
        <i class="el-icon-close close-icon" @click="closeDialog"></i>
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
      imageWidth: 800,
      imageHeight: 800
    }
  },
  methods: {
    open(url) {
      this.imageUrl = url
      this.dialogVisible = true
      
      // 计算图片宽高比例
      const img = new Image()
      img.src = url
      img.onload = () => {
        const ratio = img.width / img.height
        this.imageWidth = 800
        this.imageHeight = 800 / ratio
      }
    },
    closeDialog() {
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.image-viewer {
  .image-container {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    padding: 20px;
    
    .close-icon {
      position: absolute;
      right: 20px;
      top: 20px;
      font-size: 24px;
      color: #F56C6C;
      cursor: pointer;
    }
  }
}
</style>