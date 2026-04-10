<template>
  <a-owner-detail-coupon ref="couponComponent" />
</template>

<script>
import AOwnerDetailCoupon from './aOwnerDetailCoupon'

export default {
  components: {
    AOwnerDetailCoupon
  },
  methods: {
    open(data) {
      this.$refs.couponComponent.handleSwitch(data)
    }
  }
}
</script>