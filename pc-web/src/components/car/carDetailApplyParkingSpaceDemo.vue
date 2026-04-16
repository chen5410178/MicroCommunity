<template>
  <car-detail-apply-parking-space ref="carDetailApplyParkingSpace"></car-detail-apply-parking-space>
</template>

<script>
import CarDetailApplyParkingSpace from '@/components/car/carDetailApplyParkingSpace'

export default {
  components: {
    CarDetailApplyParkingSpace
  },
  methods: {
    open(data) {
      this.$refs.carDetailApplyParkingSpace.switch(data)
    }
  }
}
</script>