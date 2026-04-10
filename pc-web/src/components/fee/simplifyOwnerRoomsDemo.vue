<template>
  <simplify-owner-rooms ref="simplifyOwnerRooms"></simplify-owner-rooms>
</template>

<script>
import SimplifyOwnerRooms from './simplifyOwnerRooms'

export default {
  name: 'SimplifyOwnerRoomsDemo',
  components: {
    SimplifyOwnerRooms
  },
  mounted() {
    this.$refs.simplifyOwnerRooms.$emit('switch', {
      ownerId: '123',
      roomId: '456'
    })
  }
}
</script>