<template>
  <el-dialog
    :title="$t('deleteOweFeeCallable.title')"
    :visible.sync="dialogVisible"
    width="30%"
    :before-close="handleClose"
  >
    <div align="center">
      <h4>{{ $t('deleteOweFeeCallable.confirmDelete') }}</h4>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDeleteOweFeeCallableModel">{{ $t('deleteOweFeeCallable.cancel') }}</el-button>
      <el-button type="primary" @click="deleteOweFeeCallable">{{ $t('deleteOweFeeCallable.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteOweFeeCallable } from '@/api/fee/deleteOweFeeCallableApi'

export default {
  name: 'DeleteOweFeeCallable',
  data() {
    return {
      dialogVisible: false,
      deleteOweFeeCallableInfo: {}
    }
  },
  created() {
    this._initEvent()
  },
  methods: {
    _initEvent() {
      this.$on('openDeleteOweFeeCallableModal', this.open)
    },
    open(params) {
      this.deleteOweFeeCallableInfo = params
      this.dialogVisible = true
    },
    handleClose(done) {
      this.closeDeleteOweFeeCallableModel()
      done()
    },
    async deleteOweFeeCallable() {
      try {
        const params = {
          ...this.deleteOweFeeCallableInfo,
          communityId: getCommunityId()
        }
        const res = await deleteOweFeeCallable(params)
        if (res.code === 0) {
          this.dialogVisible = false
          this.$emit('listOweFeeCallable', {})
          this.$emit('listOwnerData', {})
          this.$emit('success', {})
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('Request failed:', error)
        this.$message.error(error.message)
      }
    },
    closeDeleteOweFeeCallableModel() {
      this.dialogVisible = false
    }
  }
}
</script>