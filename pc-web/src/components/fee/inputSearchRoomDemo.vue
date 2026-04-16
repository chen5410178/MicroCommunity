<template>
  <div>
    <el-input 
      v-model="roomName" 
      :placeholder="$t('inputSearchRoomDemo.searchPlaceholder')"
      @input="handleSearch"
    ></el-input>
    <input-search-room ref="inputSearchRoom"></input-search-room>
  </div>
</template>

<script>
import InputSearchRoom from './inputSearchRoom'

export default {
  name: 'InputSearchRoomDemo',
  components: {
    InputSearchRoom
  },
  data() {
    return {
      roomName: ''
    }
  },
  methods: {
    handleSearch() {
      this.$refs.inputSearchRoom.$emit('searchRoom', {
        roomName: this.roomName,
        callComponent: 'yourComponentName'
      })
    }
  }
}
</script>