<template>
  <el-dialog
    :title="$t('common.deleteConfirm')"
    :visible.sync="visible"
    width="30%"
  >
    <span>{{ $t('common.deleteConfirm') }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMarketPic } from '@/api/market/marketWayApi'

export default {
  name: 'DeleteMarketPic',
  data() {
    return {
      visible: false,
      picId: ''
    }
  },
  methods: {
    open(row) {
      this.picId = row.picId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketPic(this.picId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>