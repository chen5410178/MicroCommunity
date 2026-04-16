<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailConfigDemo.openComponent') }}</el-button>
    <fee-detail-config ref="feeDetailConfig"></fee-detail-config>
  </div>
</template>

<script>
import FeeDetailConfig from './feeDetailConfig'

export default {
  name: 'FeeDetailConfigDemo',
  components: {
    FeeDetailConfig
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailConfig.open({
        configId: '123'
      })
    }
  }
}
</script>