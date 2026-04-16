<template>
  <owner-detail-app-user ref="ownerDetailAppUser" />
</template>

<script>
import OwnerDetailAppUser from './ownerDetailAppUser'

export default {
  components: {
    OwnerDetailAppUser
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailAppUser.ownerDetailAppUserInfo.ownerId = ownerId
      this.$refs.ownerDetailAppUser._loadOwnerDetailAppUserData(1, 10)
    }
  }
}
</script>