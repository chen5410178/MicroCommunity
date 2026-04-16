<template>
  <el-dialog :title="$t('repairSetting.confirmDeleteTitle')" :visible.sync="visible" width="30%" center>
    <div style="text-align: center">
      <p>{{ $t('repairSetting.confirmDeleteContent') }} [{{ currentRow.repairTypeName }}]?</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('repairSetting.cancelDelete') }}</el-button>
      <el-button type="danger" @click="confirmDelete">{{ $t('repairSetting.confirmDelete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteRepairSetting } from '@/api/work/repairSettingApi'

export default {
  name: 'DeleteRepairSetting',
  data() {
    return {
      visible: false,
      currentRow: {}
    }
  },
  methods: {
    open(row) {
      this.currentRow = { ...row }
      this.visible = true
    },

    closeDialog() {
      this.visible = false
      this.currentRow = {}
    },

    async confirmDelete() {
      try {
        await deleteRepairSetting(this.currentRow.settingId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.closeDialog()
      } catch (error) {
        console.error('删除报修设置失败:', error)
        this.$message.error(this.$t('repairSetting.deleteFailed'))
      }
    }
  }
}
</script>