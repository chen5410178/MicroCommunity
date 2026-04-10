<template>
  <div class="">
    <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in floors" :key="index" @click="handleSelectFloor(item)"
                :class="{ 'vc-node-selected': selectedFloorId === item.floorId }"
                class="list-group-item node-orgTree">
                {{ item.floorNum }}
              </li>
            </ul>
          </div>
        </div>
  </div>
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
.select-community-floor-container {
  .border-radius {
    border-radius: 4px;
  }

  .margin-xs-r {
    margin-right: 5px;
  }

  .treeview {
    overflow-y: auto;
  }

  .attendance-staff {
    background-color: #fff;
    border: 1px solid #ebeef5;
  }

  .list-group {
    padding-left: 0;
    margin-bottom: 0;
    list-style: none;
  }

  .list-group-item {
    position: relative;
    display: block;
    padding: 10px 15px;
    margin-bottom: -1px;
    background-color: #fff;
    border: 1px solid #ebeef5;
    cursor: pointer;

    &:hover {
      background-color: #f5f7fa;
    }
  }

  .vc-node-selected {
    background-color: #ecf5ff;
    color: #409eff;
  }

  .text-center {
    text-align: center;
  }
}
</style>