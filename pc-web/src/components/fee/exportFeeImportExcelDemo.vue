<template>
  <div>
    <el-button @click="openModal">{{ $t('exportFeeImportExcelDemo.openModal') }}</el-button>
    <export-fee-import-excel ref="exportFeeImportExcel"></export-fee-import-excel>
  </div>
</template>

<script>
import ExportFeeImportExcel from './exportFeeImportExcel'

export default {
  name: 'ExportFeeImportExcelDemo',
  components: {
    ExportFeeImportExcel
  },
  methods: {
    openModal() {
      this.$refs.exportFeeImportExcel.open()
    }
  }
}
</script>