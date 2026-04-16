<template>
  <el-select
    v-model="selectedFloor"
    :placeholder="$t('meterWater.selectBuilding')"
    filterable
    clearable
    style="width: 100%"
    @change="handleChange"
  >
    <el-option
      v-for="item in floors"
      :key="item.floorId"
      :label="`${item.floorNum}栋`"
      :value="item.floorId"
    />
  </el-select>
</template>

<script>
import { queryFloors } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FloorSelect2',
  props: {
    value: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      floors: [],
      selectedFloor: this.value,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadFloors()
  },
  methods: {
    async loadFloors() {
      try {
        const { data } = await queryFloors({
          communityId: this.communityId,
          page: 1,
          row: 500
        })
        this.floors = data
      } catch (error) {
        console.error('Failed to load floors:', error)
      }
    },
    handleChange(value) {
      const floor = this.floors.find(item => item.floorId === value)
      this.$emit('change', {
        floorId: value,
        floorNum: floor ? floor.floorNum : ''
      })
    }
  },
  watch: {
    value(newVal) {
      this.selectedFloor = newVal
    }
  }
}
</script>