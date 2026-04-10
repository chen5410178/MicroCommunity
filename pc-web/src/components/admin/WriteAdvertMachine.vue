<template>
  <el-dialog
    :title="$t('advertManage.machine.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose">
    <el-form label-width="120px">
      <el-form-item :label="$t('advertManage.machine.machineCode')">
        <el-input 
          v-model="machineCode" 
          :placeholder="$t('advertManage.machine.machineCodePlaceholder')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleView">{{ $t('advertManage.machine.view') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'WriteAdvertMachine',
  data() {
    return {
      visible: false,
      machineCode: ''
    }
  },
  methods: {
    open() {
      this.visible = true
      this.machineCode = ''
    },
    handleClose() {
      this.machineCode = ''
    },
    handleView() {
      if (!this.machineCode) {
        this.$message.error(this.$t('advertManage.validate.machineCodeRequired'))
        return
      }
      this.$router.push({
        path: '/advertVedio',
        query: {
          machineCode: this.machineCode,
          communityId: this.getCommunityId()
        }
      })
      this.visible = false
    }
  }
}
</script>