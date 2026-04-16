<template>
  <owner-detail-access-control-record ref="ownerDetailAccessControlRecord" />
</template>

<script>
import OwnerDetailAccessControlRecord from '@/components/owner/ownerDetailAccessControlRecord'

export default {
  components: {
    OwnerDetailAccessControlRecord
  },
  methods: {
    open(ownerId, link) {
      this.$refs.ownerDetailAccessControlRecord.open(ownerId, link)
    }
  }
}
</script>