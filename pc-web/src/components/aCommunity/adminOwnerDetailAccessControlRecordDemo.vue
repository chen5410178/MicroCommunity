<template>
  <admin-owner-detail-access-control-record ref="accessControlRecordComponent" />
</template>

<script>
import AdminOwnerDetailAccessControlRecord from './adminOwnerDetailAccessControlRecord'

export default {
  components: {
    AdminOwnerDetailAccessControlRecord
  },
  methods: {
    open(data) {
      this.$refs.accessControlRecordComponent.handleSwitch(data)
    }
  }
}
</script>