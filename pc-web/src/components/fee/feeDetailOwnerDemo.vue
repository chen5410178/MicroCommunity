<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailOwnerDemo.openComponent') }}</el-button>
    <fee-detail-owner ref="feeDetailOwner"></fee-detail-owner>
  </div>
</template>

<script>
import FeeDetailOwner from './feeDetailOwner'

export default {
  name: 'FeeDetailOwnerDemo',
  components: {
    FeeDetailOwner
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailOwner.open({
        ownerId: '123'
      })
    }
  }
}
</script>