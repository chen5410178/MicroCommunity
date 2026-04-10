<template>
  <el-dialog
    :title="$t('businessTableHis.dialog.delTitle')"
    :visible.sync="dialogVisible"
    width="400px"
    @close="handleClose"
  >
    <div class="del-dialog-content">
      <i class="el-icon-warning"></i>
      <span>{{ $t('businessTableHis.delete.confirm') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('businessTableHis.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('businessTableHis.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteBusinessTableHis } from '@/api/dev/businessTableHisApi'

export default {
  name: 'DelBusinessTableHis',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    hisId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dialogVisible: false
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      try {
        await deleteBusinessTableHis(this.hisId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('删除失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.del-dialog-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;

  i {
    font-size: 24px;
    color: #E6A23C;
    margin-right: 10px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>