<template>
  <simplify-refund-deposit ref="simplifyRefundDeposit"></simplify-refund-deposit>
</template>

<script>
import SimplifyRefundDeposit from './simplifyRefundDeposit'

export default {
  name: 'SimplifyRefundDepositDemo',
  components: {
    SimplifyRefundDeposit
  },
  mounted() {
    this.$refs.simplifyRefundDeposit.switchData({
      roomId: '123',
      ownerId: '456',
      roomName: 'Test Room'
    })
  }
}
</script>