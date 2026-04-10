<template>
  <div>
    <el-button @click="openOwnerMembers">查看业主成员</el-button>
    <owner-members ref="ownerMembersRef" />
  </div>
</template>

<script>
import OwnerMembers from './ownerMembers'

export default {
  components: {
    OwnerMembers
  },
  methods: {
    openOwnerMembers() {
      this.$refs.ownerMembersRef.open('123') // 传递业主ID
    }
  }
}
</script>