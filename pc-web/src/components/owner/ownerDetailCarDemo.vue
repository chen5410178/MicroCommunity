<template>
  <owner-detail-car ref="ownerDetailCar" />
</template>

<script>
import OwnerDetailCar from '@/components/owner/ownerDetailCar'

export default {
  components: {
    OwnerDetailCar
  },
  methods: {
    open(ownerId, ownerName) {
      this.$refs.ownerDetailCar.ownerDetailCarInfo.ownerId = ownerId
      this.$refs.ownerDetailCar.ownerDetailCarInfo.ownerName = ownerName
      this.$refs.ownerDetailCar._loadOwnerDetailCarData(1, 10)
    }
  }
}
</script>