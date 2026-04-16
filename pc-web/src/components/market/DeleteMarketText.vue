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
import { deleteMarketText } from '@/api/market/marketWayApi'

export default {
  name: 'DeleteMarketText',
  data() {
    return {
      visible: false,
      textId: ''
    }
  },
  methods: {
    open(row) {
      this.textId = row.textId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketText(this.textId)
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