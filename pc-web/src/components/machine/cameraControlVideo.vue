<template>
  <div class="camera-control-video">
    <el-row>
      <el-col :span="12" class="text-left">
        <h3>{{ $t('cameraControlVideo.camera') }}</h3>
      </el-col>
      <el-col :span="12" class="text-right">
        <el-button-group>
          <el-button size="small" :type="cameraControlVideoInfo.cameraCount === 4 ? 'primary' : ''"
            @click="_changeCount(4)">
            {{ $t('cameraControlVideo.fourWay') }}
          </el-button>
          <el-button size="small" :type="cameraControlVideoInfo.cameraCount === 6 ? 'primary' : ''"
            @click="_changeCount(6)">
            {{ $t('cameraControlVideo.sixWay') }}
          </el-button>
        </el-button-group>
      </el-col>
    </el-row>
    <el-row class="margin-top">
      <template v-if="cameraControlVideoInfo.cameraCount === 4">
        <el-col v-for="(item, index) in cameraControlVideoInfo.machines" :key="index" :span="12">
          <div class="form-group">
            <div :id="item.id" style="border: 1px solid #dee2e6;">
              <img width="100%" src="/img/init.jpg" height="300px" style="border: 1;" />
            </div>
            <div class="flex justify-between margin-top-sm" style="font-size: 14px;">
              <div class="margin-left-sm">
                <i class="el-icon-bell" style="color: #007bff;"></i>{{ item.machineName }}
              </div>
              <el-button size="small" type="primary" @click="_openSelectVideo(item)">
                {{ $t('common.select') }}
              </el-button>
            </div>
          </div>
        </el-col>
      </template>
      <template v-else-if="cameraControlVideoInfo.cameraCount === 6">
        <el-col v-for="(item, index) in cameraControlVideoInfo.machines" :key="index" :span="8">
          <div class="form-group">
            <div :id="item.id" style="border: 1px solid #dee2e6;">
              <img width="100%" src="/img/init.jpg" height="300px" style="border: 1;" />
            </div>
            <div class="flex justify-between margin-top-sm" style="font-size: 14px;">
              <div class="margin-left-sm">
                <i class="el-icon-bell" style="color: #007bff;"></i>{{ item.machineName }}
              </div>
              <el-button size="small" type="primary" @click="_openSelectVideo(item)">
                {{ $t('common.select') }}
              </el-button>
            </div>
          </div>
        </el-col>
      </template>
    </el-row>
    <select-video-machine ref="selectVideoMachine" />
  </div>
</template>

<script>
import SelectVideoMachine from './selectVideoMachine.vue'

export default {
  name: 'CameraControlVideo',
  components: {
    SelectVideoMachine
  },
  data() {
    return {
      cameraControlVideoInfo: {
        machines: [],
        cameraCount: 4
      }
    }
  },
  created() {
    const cameraCount = this.$route.query.cameraCount
    if (cameraCount) {
      this.cameraControlVideoInfo.cameraCount = parseInt(cameraCount)
    }
    this._initCamera()
  },
  methods: {
    _initCamera() {
      const machines = []
      const cameraCount = this.cameraControlVideoInfo.cameraCount
      for (let i = 0; i < cameraCount; i++) {
        machines.push({
          id: 'cameraVideo' + (i + 1) + 'Div',
          machineName: '',
          url: ''
        })
      }
      this.cameraControlVideoInfo.machines = machines
    },
    _openSelectVideo(item) {
      item.callback = (machine) => {
        item.machineName = machine.machineName
        if (item.jessibuca) {
          try {
            item.jessibuca.destroy()
          } catch (err) {
            console.error(err)
          }
        }
        this.$refs.selectVideoMachine.getPlayVideoUrl(machine.machineId)
          .then(url => {
            const image = document.getElementById(item.id)
            const jessibuca = new window.Jessibuca({
              container: image,
              videoBuffer: 0.2,
              isResize: false,
              text: "",
              loadingText: "",
              useMSE: false,
              debug: false,
              isNotMute: false,
              supportDblclickFullscreen: true,
              operateBtns: {
                fullscreen: true,
                screenshot: true,
                play: true,
                audio: false,
                recorder: false
              },
            })
            item.jessibuca = jessibuca
            jessibuca.play(url)
          })
          .catch(error => {
            console.error(error)
            this.$message.error(this.$t('cameraControlVideo.getVideoUrlError'))
          })
      }
      this.$refs.selectVideoMachine.open(item)
    },
    _changeCount(count) {
      this.cameraControlVideoInfo.cameraCount = count
      this.$router.push({
        path: this.$route.path,
        query: { cameraCount: count }
      })
      this._initCamera()
    }
  }
}
</script>

<style scoped>
.camera-control-video {
  padding: 20px;
  padding-top: 0;
}

.text-right {
  text-align: right;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.margin-left-sm {
  margin-left: 10px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.form-group {
  margin-bottom: 15px;
}
</style>