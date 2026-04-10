<template>
  <el-dialog
    :title="$t('project.dialog.delTitle')"
    :visible.sync="dialogVisible"
    width="400px"
    @close="handleClose"
  >
    <div class="del-dialog-content">
      <i class="el-icon-warning"></i>
      <span>{{ $t('project.delete.confirm') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('project.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('project.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteProject } from '@/api/project/projectApi'

export default {
  name: 'DelProject',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    projectId: {
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
        await deleteProject(this.projectId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('删除项目失败:', error)
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