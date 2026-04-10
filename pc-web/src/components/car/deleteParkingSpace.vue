<template>
  <el-dialog
    :title="$t('deleteParkingSpace.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div style="text-align:center">
      <p>{{ $t('deleteParkingSpace.confirmDelete') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('deleteParkingSpace.cancel') }}</el-button>
      <el-button type="danger" @click="confirmDelete" :loading="deleting">
        {{ $t('deleteParkingSpace.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteParkingSpace } from '@/api/car/listParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteParkingSpace',
  data() {
    return {
      visible: false,
      deleting: false,
      currentParkingSpace: null
    }
  },
  methods: {
    open(row) {
      this.currentParkingSpace = { ...row, communityId: getCommunityId() }
      this.visible = true
    },
    
    async confirmDelete() {
      try {
        this.deleting = true
        const res = await deleteParkingSpace(this.currentParkingSpace)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        } else {
          this.$message.error(res.msg || this.$t('common.deleteError'))
        }
      } catch (error) {
        this.$message.error(this.$t('common.deleteError'))
      } finally {
        this.deleting = false
      }
    }
  }
}
</script>