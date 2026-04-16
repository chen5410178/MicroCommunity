<template>
  <div>
    <el-button type="primary" @click="openOwnerRooms">查看业主房屋</el-button>
    <owner-rooms ref="ownerRooms" />
  </div>
</template>

<script>
import OwnerRooms from './ownerRooms'

export default {
  components: {
    OwnerRooms
  },
  methods: {
    openOwnerRooms() {
      this.$refs.ownerRooms.open({ ownerId: '123' })
    }
  }
}
</script>