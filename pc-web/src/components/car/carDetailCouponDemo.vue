<template>
  <car-detail-coupon ref="carDetailCoupon"></car-detail-coupon>
</template>

<script>
import CarDetailCoupon from '@/components/car/carDetailCoupon'

export default {
  components: {
    CarDetailCoupon
  },
  methods: {
    open(data) {
      this.$refs.carDetailCoupon.switch(data)
    }
  }
}
</script>