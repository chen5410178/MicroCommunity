<template>
  <simplify-owner-access-contol ref="simplifyOwnerAccessContol"></simplify-owner-access-contol>
</template>

<script>
import SimplifyOwnerAccessContol from '@/components/simplify/simplifyOwnerAccessContol'

export default {
  name: 'SimplifyOwnerAccessContolDemo',
  components: {
    SimplifyOwnerAccessContol
  },
  methods: {
    openSimplifyOwnerAccessContol(params) {
      this.$refs.simplifyOwnerAccessContol.$emit('switch', params)
    }
  }
}
</script>