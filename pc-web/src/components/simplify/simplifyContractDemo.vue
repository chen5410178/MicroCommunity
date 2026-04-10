<template>
  <simplify-contract ref="simplifyContract"></simplify-contract>
</template>

<script>
import SimplifyContract from '@/components/simplify/simplifyContract'

export default {
  name: 'SimplifyContractDemo',
  components: {
    SimplifyContract
  },
  methods: {
    openSimplifyContract(params) {
      this.$refs.simplifyContract.$emit('switch', params)
    }
  }
}
</script>