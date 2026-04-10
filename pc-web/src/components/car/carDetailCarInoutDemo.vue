<template>
  <car-detail-car-inout ref="carDetailCarInout"></car-detail-car-inout>
</template>

<script>
import CarDetailCarInout from '@/components/car/carDetailCarInout'

export default {
  components: {
    CarDetailCarInout
  },
  methods: {
    open(data) {
      this.$refs.carDetailCarInout.switch(data)
    }
  }
}
</script>