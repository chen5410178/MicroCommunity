<template>
  <el-dialog :title="$t('common.delete')" :visible.sync="dialogVisible" width="30%" @close="handleClose">
    <div class="confirm-content">
      <el-alert :title="$t('equipmentAccount.confirmDelete')" type="warning" :closable="false" show-icon>
      </el-alert>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteEquipmentAccount } from '@/api/machine/equipmentAccountApi'

export default {
  name: 'DeleteEquipmentAccount',
  data() {
    return {
      dialogVisible: false,
      communityId: '',
      equipmentData: {
        machineId: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.equipmentData = {
        machineId: data.machineId,
        communityId: this.communityId
      }
      this.dialogVisible = true
    },
    handleClose() {
      this.equipmentData = {
        machineId: '',
        communityId: this.communityId
      }
    },
    async handleConfirm() {
      try {
        await deleteEquipmentAccount(this.equipmentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除设备失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.confirm-content {
  text-align: center;
  margin-bottom: 20px;
}
</style>