<template>
  <simplify-owner-member ref="simplifyOwnerMember"></simplify-owner-member>
</template>

<script>
import SimplifyOwnerMember from '@/components/simplify/simplifyOwnerMember'

export default {
  name: 'SimplifyOwnerMemberDemo',
  components: {
    SimplifyOwnerMember
  },
  methods: {
    openSimplifyOwnerMember(params) {
      this.$refs.simplifyOwnerMember.$emit('switch', params)
    }
  }
}
</script>