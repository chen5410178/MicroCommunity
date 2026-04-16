<template>
  <div>
    <el-input 
      v-model="ownerName" 
      :placeholder="$t('inputSearchRoomByOwnerDemo.searchPlaceholder')"
      @input="handleSearch"
    ></el-input>
    <input-search-room-by-owner ref="inputSearchRoomByOwner"></input-search-room-by-owner>
  </div>
</template>

<script>
import InputSearchRoomByOwner from './inputSearchRoomByOwner'

export default {
  name: 'InputSearchRoomByOwnerDemo',
  components: {
    InputSearchRoomByOwner
  },
  data() {
    return {
      ownerName: ''
    }
  },
  methods: {
    handleSearch() {
      this.$refs.inputSearchRoomByOwner.$emit('searchRoom', {
        ownerName: this.ownerName,
        callComponent: 'yourComponentName'
      })
    }
  }
}
</script>