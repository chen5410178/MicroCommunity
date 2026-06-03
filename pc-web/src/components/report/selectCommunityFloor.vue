<template>
  <el-card class="type-card">
    <div class="type-list-wrapper">
      <div v-for="(item, index) in floors" :key="index"
        class="type-list-item"
        :class="{ 'selected': selectedFloorId === item.floorId }"
        @click="handleSelectFloor(item)">
        {{ item.floorNum }}
      </div>
    </div>
  </el-card>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFloors } from '@/api/report/reportFeeSummaryApi'

export default {
  name: 'SelectCommunityFloor',
  data() {
    return {
      floors: [],
      selectedFloorId: '',
      callBack: null
    }
  },
  methods: {
    open(params) {
      this.callBack = params.callBack
      this.loadCommunityFloors()
    },
    async loadCommunityFloors() {
      try {
        const communityId = await getCommunityId()
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100,
          communityId
        }

        const defaultFloor = {
          floorNum: this.$t('common.all'),
          floorId: ''
        }

        const data = await queryFloors(params)
        this.floors = [defaultFloor, ...data.apiFloorDataVoList]
        this.handleSelectFloor(defaultFloor)
      } catch (error) {
        console.error('Failed to load floors:', error)
      }
    },
    handleSelectFloor(floor) {
      this.selectedFloorId = floor.floorId
      if (this.callBack) {
        this.callBack(floor)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.type-card {
  .type-list-wrapper {
    max-height: 600px;
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: #dcdfe6;
      border-radius: 2px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }

    .type-list-item {
      padding: 10px;
      text-align: center;
      cursor: pointer;
      border-radius: 4px;
      margin-bottom: 5px;
      transition: all 0.3s;
      font-size: 14px;
      color: #606266;

      &:hover {
        background-color: #f5f7fa;
      }

      &.selected {
        background-color: #409eff;
        color: white;
      }
    }
  }
}
</style>