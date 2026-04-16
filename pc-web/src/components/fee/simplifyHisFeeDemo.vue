<template>
  <div>
    <simplify-his-fee ref="simplifyHisFee"></simplify-his-fee>
  </div>
</template>

<script>
import SimplifyHisFee from './simplifyHisFee'

export default {
  name: 'SimplifyHisFeeDemo',
  components: {
    SimplifyHisFee
  },
  mounted() {
    this.$refs.simplifyHisFee.$emit('switch', {
      ownerId: '123'
    })
  }
}
</script>