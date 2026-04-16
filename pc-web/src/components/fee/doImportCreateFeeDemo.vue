<template>
  <div>
    <el-button @click="openModal">{{ $t('doImportCreateFeeDemo.openModal') }}</el-button>
    <do-import-create-fee ref="doImportCreateFee"></do-import-create-fee>
  </div>
</template>

<script>
import DoImportCreateFee from './doImportCreateFee'

export default {
  name: 'DoImportCreateFeeDemo',
  components: {
    DoImportCreateFee
  },
  methods: {
    openModal() {
      this.$refs.doImportCreateFee.open()
    }
  }
}
</script>