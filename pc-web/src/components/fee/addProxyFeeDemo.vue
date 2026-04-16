<template>
  <div>
    <el-button @click="openModal">{{ $t('addProxyFeeDemo.openModal') }}</el-button>
    <add-proxy-fee ref="addProxyFee" @success="handleSuccess"></add-proxy-fee>
  </div>
</template>

<script>
import AddProxyFee from './addProxyFee'

export default {
  name: 'AddProxyFeeDemo',
  components: {
    AddProxyFee
  },
  methods: {
    openModal() {
      this.$refs.addProxyFee.open({
        roomId: '123',
        roomName: '1-2-101',
        ownerName: 'Test Owner'
      })
    },
    handleSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>