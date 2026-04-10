<template>
  <el-button @click="openModal">{{ $t('prestoreAccountDemo.openModal') }}</el-button>
  <prestore-account ref="prestoreAccount"></prestore-account>
</template>

<script>
import PrestoreAccount from './prestoreAccount'

export default {
  name: 'PrestoreAccountDemo',
  components: {
    PrestoreAccount
  },
  methods: {
    openModal() {
      this.$refs.prestoreAccount.open({
        tel: '13800138000',
        ownerId: '123',
        acctType: '2003',
        roomId: '456'
      })
    }
  }
}
</script>