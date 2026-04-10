<template>
  <simplify-owner-repair ref="simplifyOwnerRepair"></simplify-owner-repair>
</template>

<script>
import SimplifyOwnerRepair from '@/components/simplify/simplifyOwnerRepair'

export default {
  name: 'SimplifyOwnerRepairDemo',
  components: {
    SimplifyOwnerRepair
  },
  methods: {
    openSimplifyOwnerRepair(params) {
      this.$refs.simplifyOwnerRepair.$emit('switch', params)
    }
  }
}
</script>