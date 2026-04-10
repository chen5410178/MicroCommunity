<template>
  <simplify-car-fee ref="simplifyCarFee"></simplify-car-fee>
</template>

<script>
import SimplifyCarFee from '@/components/simplify/simplifyCarFee'

export default {
  name: 'SimplifyCarFeeDemo',
  components: {
    SimplifyCarFee
  },
  methods: {
    openSimplifyCarFee(params) {
      this.$refs.simplifyCarFee.$emit('switch', params)
    }
  }
}
</script>