<template>
  <el-button @click="openModal">{{$t('writeOweFeeCallableDemo.openModal')}}</el-button>
  <write-owe-fee-callable ref="writeOweFeeCallable"></write-owe-fee-callable>
</template>

<script>
import WriteOweFeeCallable from './writeOweFeeCallable'

export default {
  name: 'WriteOweFeeCallableDemo',
  components: {
    WriteOweFeeCallable
  },
  methods: {
    openModal() {
      this.$refs.writeOweFeeCallable.open({
        roomId: '123',
        roomName: 'Test Room'
      })
    }
  }
}
</script>