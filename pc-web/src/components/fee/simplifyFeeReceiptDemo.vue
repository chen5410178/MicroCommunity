<template>
  <simplify-fee-receipt ref="simplifyFeeReceipt"></simplify-fee-receipt>
</template>

<script>
import SimplifyFeeReceipt from './simplifyFeeReceipt'

export default {
  name: 'SimplifyFeeReceiptDemo',
  components: {
    SimplifyFeeReceipt
  },
  mounted() {
    this.$refs.simplifyFeeReceipt.$emit('switch', {
      roomId: '123',
      ownerId: '456'
    })
  }
}
</script>