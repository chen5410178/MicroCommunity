<template>
  <el-button @click="openModal">{{$t('deleteOweFeeCallableDemo.openModal')}}</el-button>
  <delete-owe-fee-callable ref="deleteOweFeeCallable"></delete-owe-fee-callable>
</template>

<script>
import DeleteOweFeeCallable from './deleteOweFeeCallable'

export default {
  name: 'DeleteOweFeeCallableDemo',
  components: {
    DeleteOweFeeCallable
  },
  methods: {
    openModal() {
      this.$refs.deleteOweFeeCallable.open({
        ofcId: '123'
      })
    }
  }
}
</script>