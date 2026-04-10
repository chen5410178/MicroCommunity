<template>
  <simplify-owner-complaint ref="simplifyOwnerComplaint"></simplify-owner-complaint>
</template>

<script>
import SimplifyOwnerComplaint from '@/components/simplify/simplifyOwnerComplaint'

export default {
  name: 'SimplifyOwnerComplaintDemo',
  components: {
    SimplifyOwnerComplaint
  },
  methods: {
    openSimplifyOwnerComplaint(params) {
      this.$refs.simplifyOwnerComplaint.$emit('switch', params)
    }
  }
}
</script>