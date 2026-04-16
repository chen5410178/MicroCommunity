<template>
  <el-dialog
    :title="$t('machineTypeTree.confirmOperation')"
    :visible.sync="dialogVisible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('machineTypeTree.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('machineTypeTree.cancelDelete') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('machineTypeTree.confirmDeleteBtn') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMachineType } from '@/api/machine/machineTypeTreeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMachineType',
  data() {
    return {
      dialogVisible: false,
      form: {
        typeId: '',
        communityId: ''
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.form.typeId = data.typeId
      this.dialogVisible = true
    },
    async handleConfirm() {
      try {
        await deleteMachineType(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除设备类型失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
  font-size: 16px;
  margin: 20px 0;
}
</style>