<template>
  <div class="video-control-container">
    <el-card class="box-card">

      <el-row :gutter="20">
        <el-col :span="24">
          <camera-control-video ref="cameraControl" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import CameraControlVideo from '@/components/machine/cameraControlVideo.vue'

export default {
  name: 'VideoControlList',
  components: {
    CameraControlVideo
  },
  data() {
    return {
      communityId: ''
    }
  },
  created() {
    this.getCommunityId()
  },
  methods: {
    async getCommunityId() {
      try {
        const { getCommunityId } = await import('@/api/community/communityApi')
        this.communityId = getCommunityId()
      } catch (error) {
        console.error('Failed to get communityId:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.video-control-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix {
    font-size: 18px;
    font-weight: bold;
  }
}
</style>