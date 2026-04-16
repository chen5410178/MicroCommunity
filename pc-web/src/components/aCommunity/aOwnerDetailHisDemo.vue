<template>
  <a-owner-detail-his ref="hisComponent" />
</template>

<script>
import AOwnerDetailHis from './aOwnerDetailHis'

export default {
  components: {
    AOwnerDetailHis
  },
  methods: {
    open(data) {
      this.$refs.hisComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.hisComponent.handleNotify()
    }
  }
}
</script>