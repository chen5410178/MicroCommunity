<template>
  <owner-detail-repair ref="ownerDetailRepair" />
</template>

<script>
import OwnerDetailRepair from './ownerDetailRepair'

export default {
  components: {
    OwnerDetailRepair
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailRepair.ownerDetailRepairInfo.ownerId = ownerId
      this.$refs.ownerDetailRepair._loadOwnerDetailRepairData(1, 10)
    }
  }
}
</script>