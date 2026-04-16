<template>
  <a-fee-detail-import ref="feeImportComponent" />
</template>

<script>
import AFeeDetailImport from './aFeeDetailImport'

export default {
  components: {
    AFeeDetailImport
  },
  methods: {
    open(data) {
      this.$refs.feeImportComponent.handleSwitch(data)
    }
  }
}
</script>