<template>
  <car-detail-transaction-car ref="carDetailTransactionCar"></car-detail-transaction-car>
</template>

<script>
import CarDetailTransactionCar from '@/components/car/carDetailTransactionCar'

export default {
  components: {
    CarDetailTransactionCar
  },
  methods: {
    open(data) {
      this.$refs.carDetailTransactionCar.switch(data)
    }
  }
}
</script>