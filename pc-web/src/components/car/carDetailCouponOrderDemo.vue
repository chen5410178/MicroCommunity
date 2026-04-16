<template>
  <car-detail-coupon-order ref="carDetailCouponOrder"></car-detail-coupon-order>
</template>

<script>
import CarDetailCouponOrder from '@/components/car/carDetailCouponOrder'

export default {
  components: {
    CarDetailCouponOrder
  },
  methods: {
    open(data) {
      this.$refs.carDetailCouponOrder.switch(data)
    }
  }
}
</script>