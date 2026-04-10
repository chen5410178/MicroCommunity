<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailImportDemo.openComponent') }}</el-button>
    <fee-detail-import ref="feeDetailImport"></fee-detail-import>
  </div>
</template>

<script>
import FeeDetailImport from './feeDetailImport'

export default {
  name: 'FeeDetailImportDemo',
  components: {
    FeeDetailImport
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailImport.open({
        feeId: '123'
      })
    }
  }
}
</script>