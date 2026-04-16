<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailCarDemo.openComponent') }}</el-button>
    <fee-detail-car ref="feeDetailCar"></fee-detail-car>
  </div>
</template>

<script>
import FeeDetailCar from './feeDetailCar'

export default {
  name: 'FeeDetailCarDemo',
  components: {
    FeeDetailCar
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailCar.open({
        payerObjId: '123'
      })
    }
  }
}
</script>