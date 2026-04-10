<template>
  <el-dialog
    :title="$t('contractTypeManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        type="warning"
        :closable="false"
        show-icon
      >
        {{ $t('contractTypeManage.delete.confirmText') }}
      </el-alert>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="danger" @click="deleteContractType">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteContractType } from '@/api/contract/contractTypeManageApi'

export default {
  name: 'DeleteContractType',
  data() {
    return {
      visible: false,
      form: {
        contractTypeId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        contractTypeId: row.contractTypeId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async deleteContractType() {
      try {
        const res = await deleteContractType(this.form)
        
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    },
    handleClose() {
      this.form = {
        contractTypeId: '',
        communityId: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  margin-bottom: 20px;
  
  .el-alert {
    padding: 15px;
  }
}
</style>