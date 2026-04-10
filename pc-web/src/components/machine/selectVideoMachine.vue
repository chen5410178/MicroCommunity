<template>
  <el-dialog 
    :visible.sync="dialogVisible" 
    :title="$t('selectVideoMachine.title')"
    width="80%"
    @close="handleClose"
  >
    <el-row>
      <el-col :span="12" class="border-right">
        <div class="text-center">
          {{ $t('selectVideoMachine.monitorArea') }}
        </div>
        <div class="padding padding-top-xs">
          <div 
            v-for="(item,index) in selectVideoMachineInfo.areas" 
            :key="index"
            class="padding overflow-hidden"
            :class="{'select': selectVideoMachineInfo.maId === item.maId}"
            @click="_changeArea(item)"
          >
            <div>{{item.maName}}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="text-center">
          {{ $t('selectVideoMachine.camera') }}
        </div>
        <div class="padding padding-top-xs">
          <div 
            v-for="(item,index) in selectVideoMachineInfo.machines" 
            :key="index"
            class="padding overflow-hidden"
            :class="{'select': selectVideoMachineInfo.machineId === item.machineId}"
            @click="_changeMachine(item)"
          >
            <div>{{item.machineName}}</div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { getMonitorAreas, getMonitorMachines, getPlayVideoUrl } from '@/api/machine/videoControlApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SelectVideoMachine',
  data() {
    return {
      dialogVisible: false,
      selectVideoMachineInfo: {
        areas: [],
        machines: [],
        maId: '',
        machineId: '',
        curMachine: {}
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(machine) {
      this._clearSelectVideoMachine()
      this.selectVideoMachineInfo.curMachine = machine
      this._loadSelectAreas()
      this.dialogVisible = true
    },
    async getPlayVideoUrl(machineId) {
      const params = {
        page: 1,
        row: 1,
        communityId: this.communityId,
        machineId: machineId
      }
      const res = await getPlayVideoUrl(params)
      return res.data
    },
    async _loadSelectAreas() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const res = await getMonitorAreas(params)
        this.selectVideoMachineInfo.areas = res.data
        if (res.data.length > 0) {
          this._changeArea(res.data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('selectVideoMachine.loadAreasError'))
      }
    },
    async _loadSelectVideoMachines() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          maId: this.selectVideoMachineInfo.maId
        }
        const res = await getMonitorMachines(params)
        this.selectVideoMachineInfo.machines = res.data
      } catch (error) {
        this.$message.error(this.$t('selectVideoMachine.loadMachinesError'))
      }
    },
    _changeArea(area) {
      this.selectVideoMachineInfo.maId = area.maId
      this._loadSelectVideoMachines()
    },
    _changeMachine(machine) {
      this.selectVideoMachineInfo.machineId = machine.machineId
      this.selectVideoMachineInfo.curMachine.callback(machine)
      this.handleClose()
    },
    _clearSelectVideoMachine() {
      this.selectVideoMachineInfo = {
        areas: [],
        machines: [],
        maId: '',
        machineId: '',
        curMachine: {}
      }
    },
    handleClose() {
      this.dialogVisible = false
      this._clearSelectVideoMachine()
    }
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #dee2e6;
}
.text-center {
  text-align: center;
  font-weight: bold;
  margin-bottom: 10px;
}
.padding {
  padding: 10px;
}
.padding-top-xs {
  padding-top: 5px;
}
.overflow-hidden {
  overflow: hidden;
}
.select {
  background-color: #f5f7fa;
  color: #409eff;
  cursor: pointer;
}
</style>