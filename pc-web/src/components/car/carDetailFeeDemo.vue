<template>
  <car-detail-fee ref="carDetailFee"></car-detail-fee>
</template>

<script>
import CarDetailFee from '@/components/car/carDetailFee'

export default {
  components: {
    CarDetailFee
  },
  methods: {
    open(data) {
      this.$refs.carDetailFee.switch(data)
    }
  }
}
</script>