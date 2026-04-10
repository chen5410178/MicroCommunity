<template>
  <simplify-owner-transaction-car ref="simplifyOwnerTransactionCar"></simplify-owner-transaction-car>
</template>

<script>
import SimplifyOwnerTransactionCar from '@/components/simplify/simplifyOwnerTransactionCar'

export default {
  name: 'SimplifyOwnerTransactionCarDemo',
  components: {
    SimplifyOwnerTransactionCar
  },
  methods: {
    openSimplifyOwnerTransactionCar(params) {
      this.$refs.simplifyOwnerTransactionCar.$emit('switch', params)
    }
  }
}
</script>