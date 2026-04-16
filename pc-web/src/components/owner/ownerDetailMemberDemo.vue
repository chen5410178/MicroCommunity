<template>
  <owner-detail-member ref="ownerDetailMember" />
</template>

<script>
import OwnerDetailMember from './ownerDetailMember'

export default {
  components: {
    OwnerDetailMember
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailMember.ownerDetailMemberInfo.ownerId = ownerId
      this.$refs.ownerDetailMember._loadOwnerDetailMemberData(1, 10)
    }
  }
}
</script>