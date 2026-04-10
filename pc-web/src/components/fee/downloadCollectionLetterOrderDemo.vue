<template>
  <div>
    <el-button @click="openModal">{{ $t('downloadCollectionLetterOrderDemo.openModal') }}</el-button>
    <download-collection-letter-order ref="downloadCollectionLetterOrder"></download-collection-letter-order>
  </div>
</template>

<script>
import DownloadCollectionLetterOrder from './downloadCollectionLetterOrder'

export default {
  name: 'DownloadCollectionLetterOrderDemo',
  components: {
    DownloadCollectionLetterOrder
  },
  methods: {
    openModal() {
      this.$refs.downloadCollectionLetterOrder.open({
        roomId: '123' // Optional, remove if you want to select floor
      })
    }
  }
}
</script>