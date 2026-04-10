<template>
  <div>
    <el-button @click="openOwnerComplaints">查看业主投诉</el-button>
    <owner-complaints ref="ownerComplaintsRef" />
  </div>
</template>

<script>
import OwnerComplaints from './ownerComplaints'

export default {
  components: {
    OwnerComplaints
  },
  methods: {
    openOwnerComplaints() {
      this.$refs.ownerComplaintsRef.open('789') // 传递业主ID
    }
  }
}
</script>