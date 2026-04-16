<template>
  <simplify-callable ref="simplifyCallable"></simplify-callable>
</template>

<script>
import SimplifyCallable from './simplifyCallable'

export default {
  name: 'SimplifyCallableDemo',
  components: {
    SimplifyCallable
  },
  mounted() {
    this.$refs.simplifyCallable.$emit('switch', {
      roomId: '123',
      roomName: 'Test Room',
      ownerId: '456'
    })
  }
}
</script>