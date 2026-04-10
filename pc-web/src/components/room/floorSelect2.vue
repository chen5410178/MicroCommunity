<template>
  <el-select v-model="selectedFloor" filterable remote :remote-method="searchFloors" :loading="loading"
    :placeholder="$t('shops.buildingPlaceholder')" style="width: 100%" @change="handleChange">
    <el-option v-for="item in floors" :key="item.floorId" :label="item.floorNum + $t('shops.building')"
      :value="item"></el-option>
  </el-select>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

import { getFloors } from '@/api/room/roomApi'

export default {
  name: 'FloorSelect2',
  data() {
    return {
      selectedFloor: null,
      floors: [],
      loading: false
    }
  },
  methods: {
    open() {
      this.searchFloors()
    },
    async searchFloors() {
        const {apiFloorDataVoList} = await getFloors({
          communityId: getCommunityId(),
          page:1,
          row:100
        })
        this.floors = apiFloorDataVoList
    },
    handleChange(value) {
      this.$emit('change', value)
    },
    clear() {
      this.selectedFloor = null
      this.floors = []
    }
  }
}
</script>