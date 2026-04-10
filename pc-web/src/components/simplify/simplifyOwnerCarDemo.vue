<template>
  <simplify-owner-car ref="simplifyOwnerCar"></simplify-owner-car>
</template>

<script>
import SimplifyOwnerCar from '@/components/simplify/simplifyOwnerCar'

export default {
  name: 'SimplifyOwnerCarDemo',
  components: {
    SimplifyOwnerCar
  },
  methods: {
    openSimplifyOwnerCar(params) {
      this.$refs.simplifyOwnerCar.$emit('switch', params)
    }
  }
}
</script>