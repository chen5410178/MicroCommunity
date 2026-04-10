<template>
  <simplify-meter-water-log ref="simplifyMeterWaterLog"></simplify-meter-water-log>
</template>

<script>
import SimplifyMeterWaterLog from './simplifyMeterWaterLog'

export default {
  name: 'SimplifyMeterWaterLogDemo',
  components: {
    SimplifyMeterWaterLog
  },
  mounted() {
    this.$refs.simplifyMeterWaterLog.switchData({
      roomId: '123',
      roomName: 'Test Room',
      ownerName: 'Test Owner',
      floorNum: '1',
      unitNum: '2',
      roomNum: '101'
    })
  }
}
</script>