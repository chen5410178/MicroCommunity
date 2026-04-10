<template>
  <div>
    <el-button @click="openSearchFloor">打开搜索楼栋</el-button>
    <search-floor
      ref="searchFloor"
      emit-choose-floor="handleChooseFloor"
      emit-load-data="handleLoadData"
      @choose-floor="handleChooseFloor"
    />
  </div>
</template>

<script>
import SearchFloor from '@/components/room/searchFloor'

export default {
  components: {
    SearchFloor
  },
  methods: {
    openSearchFloor() {
      this.$refs.searchFloor.open()
    },
    handleChooseFloor(floor) {
      console.log('选择了楼栋', floor)
    },
    handleLoadData(data) {
      console.log('加载数据', data)
    }
  }
}
</script>