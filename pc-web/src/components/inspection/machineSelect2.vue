<template>
  <el-select v-model="selectedMachine" :placeholder="$t('machineSelect2.placeholder')" filterable remote
    :remote-method="remoteMethod" :loading="loading" style="width: 100%;" @change="handleChange">
    <el-option v-for="item in machineOptions" :key="item.id" :label="item.text" :value="item.id" />
  </el-select>
</template>

<script>
import { listEquipmentAccount } from '@/api/inspection/addMaintainancePlanApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MachineSelect2',
  props: {
    parentModal: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      selectedMachine: '',
      machineOptions: [],
      loading: false,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true
        this.searchMachines(query)
      } else {
        this.machineOptions = []
      }
    },

    async searchMachines(query) {
      try {
        const params = {
          machineNameLike: query,
          page: 1,
          row: 100,
          communityId: this.communityId
        }

        const response = await listEquipmentAccount(params)
        this.machineOptions = this._filterMachineData(response.data)
      } catch (error) {
        console.error('搜索设备失败:', error)
        this.$message.error(this.$t('machineSelect2.fetchError'))
      } finally {
        this.loading = false
      }
    },

    _filterMachineData(machines) {
      if (!machines) return []

      return machines.map(machine => ({
        id: machine.machineId,
        text: `${machine.machineName}(${machine.machineCode})`
      }))
    },

    handleChange(value) {
      const selected = this.machineOptions.find(item => item.id === value)
      if (selected) {
        this.$emit('notify', {
          machineId: selected.id,
          machineName: selected.text.split('(')[0]
        })
      } else {
        this.$emit('notify', {
          machineId: '',
          machineName: ''
        })
      }
    },

    setMachine(machine) {
      if (machine && machine.machineId) {
        this.selectedMachine = machine.machineId
        this.machineOptions = [{
          id: machine.machineId,
          text: `${machine.machineName}(${machine.machineCode || ''})`
        }]
      }
    },

    clearMachine() {
      this.selectedMachine = ''
      this.machineOptions = []
      this.$emit('notify', {
        machineId: '',
        machineName: ''
      })
    }
  }
}
</script>