<template>
  <div>
    <el-button type="primary" @click="openByOwner">
      {{ $t('roomContractsDemo.openByOwner') }}
    </el-button>
    <el-button type="primary" @click="openByRoom">
      {{ $t('roomContractsDemo.openByRoom') }}
    </el-button>
    <room-contracts ref="contractsRef" />
  </div>
</template>

<script>
import RoomContracts from './roomContracts'

export default {
  components: {
    RoomContracts
  },
  methods: {
    openByOwner() {
      // 模拟业主ID
      this.$refs.contractsRef.open({ ownerId: '12345' })
    },
    openByRoom() {
      // 模拟房屋ID
      this.$refs.contractsRef.open({ roomId: '67890' })
    }
  }
}
</script>