<template>
  <simplify-shops-hire-log ref="simplifyShopsHireLog"></simplify-shops-hire-log>
</template>

<script>
import SimplifyShopsHireLog from '@/components/simplify/simplifyShopsHireLog'

export default {
  name: 'SimplifyShopsHireLogDemo',
  components: {
    SimplifyShopsHireLog
  },
  methods: {
    openSimplifyShopsHireLog(params) {
      this.$refs.simplifyShopsHireLog.$emit('switch', params)
    }
  }
}
</script>