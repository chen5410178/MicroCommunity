<template>
  <admin-owner-detail-charge-machine-order ref="chargeMachineOrderComponent" />
</template>

<script>
import AdminOwnerDetailChargeMachineOrder from './adminOwnerDetailChargeMachineOrder'

export default {
  components: {
    AdminOwnerDetailChargeMachineOrder
  },
  methods: {
    open(data) {
      this.$refs.chargeMachineOrderComponent.handleSwitch(data)
    }
  }
}
</script>