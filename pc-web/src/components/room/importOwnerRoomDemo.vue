<template>
  <div>
    <el-button @click="openImportModal">Open Import</el-button>
    <import-owner-room ref="importModal" @import-success="handleImportSuccess" />
  </div>
</template>

<script>
import ImportOwnerRoom from '../importOwnerRoom'

export default {
  components: {
    ImportOwnerRoom
  },
  methods: {
    openImportModal() {
      this.$refs.importModal.open()
    },
    handleImportSuccess() {
      console.log('Import successful, handle refresh logic here')
    }
  }
}
</script>