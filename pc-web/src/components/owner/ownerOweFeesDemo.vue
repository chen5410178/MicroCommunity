<template>
  <div>
    <el-button type="primary" @click="openModal">
      {{ $t('ownerOweFeesDemo.openBtn') }}
    </el-button>
    <owner-owe-fees ref="oweFeesRef" />
  </div>
</template>

<script>
import OwnerOweFees from './ownerOweFees'

export default {
  components: {
    OwnerOweFees
  },
  methods: {
    openModal() {
      // 模拟业主ID
      const ownerId = '12345'
      this.$refs.oweFeesRef.open(ownerId)
    }
  }
}
</script>