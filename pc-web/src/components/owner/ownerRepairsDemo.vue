<template>
  <div>
    <el-button @click="openOwnerRepairs">查看业主报修</el-button>
    <owner-repairs ref="ownerRepairsRef" />
  </div>
</template>

<script>
import OwnerRepairs from './ownerRepairs'

export default {
  components: {
    OwnerRepairs
  },
  methods: {
    openOwnerRepairs() {
      this.$refs.ownerRepairsRef.open('101') // 传递业主ID
    }
  }
}
</script>