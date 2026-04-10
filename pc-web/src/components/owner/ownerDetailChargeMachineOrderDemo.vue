<template>
  <owner-detail-charge-machine-order ref="ownerDetailChargeMachineOrder" />
</template>

<script>
import OwnerDetailChargeMachineOrder from '@/components/owner/ownerDetailChargeMachineOrder'

export default {
  components: {
    OwnerDetailChargeMachineOrder
  },
  methods: {
    open(ownerId, link) {
      this.$refs.ownerDetailChargeMachineOrder.open(ownerId, link)
    }
  }
}
</script>