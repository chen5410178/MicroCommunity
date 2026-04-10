<template>
  <el-dialog
    :title="$t('contractPartyaManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('contractPartyaManage.delete.confirmText') }}</p>
    </div>
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
import { deleteContractPartya } from '@/api/contract/contractPartyaManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteContractPartya',
  data() {
    return {
      visible: false,
      deleteData: {
        partyaId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.deleteData = {
        partyaId: data.partyaId,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.deleteData = {
        partyaId: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deleteContractPartya(this.deleteData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>