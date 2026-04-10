<template>
  <owner-detail-account ref="ownerDetailAccount" />
</template>

<script>
import OwnerDetailAccount from './ownerDetailAccount'

export default {
  components: {
    OwnerDetailAccount
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailAccount.ownerDetailAccountInfo.ownerId = ownerId
      this.$refs.ownerDetailAccount._loadOwnerDetailAccountData(1, 10)
    }
  }
}
</script>