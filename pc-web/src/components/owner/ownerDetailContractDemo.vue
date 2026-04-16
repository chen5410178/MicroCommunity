<template>
  <owner-detail-contract ref="ownerDetailContract" />
</template>

<script>
import OwnerDetailContract from './ownerDetailContract'

export default {
  components: {
    OwnerDetailContract
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailContract.ownerDetailContractInfo.ownerId = ownerId
      this.$refs.ownerDetailContract._loadOwnerDetailContractData(1, 10)
    }
  }
}
</script>