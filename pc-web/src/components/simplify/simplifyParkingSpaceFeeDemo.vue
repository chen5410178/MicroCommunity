<template>
  <simplify-parking-space-fee ref="simplifyParkingSpaceFee"></simplify-parking-space-fee>
</template>

<script>
import SimplifyParkingSpaceFee from '@/components/simplify/simplifyParkingSpaceFee'

export default {
  name: 'SimplifyParkingSpaceFeeDemo',
  components: {
    SimplifyParkingSpaceFee
  },
  methods: {
    openSimplifyParkingSpaceFee(params) {
      this.$refs.simplifyParkingSpaceFee.$emit('switch', params)
    }
  }
}
</script>
