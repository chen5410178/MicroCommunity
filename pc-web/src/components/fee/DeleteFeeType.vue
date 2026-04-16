<template>
  <el-dialog
    :title="$t('deleteFeeType.title')"
    :visible.sync="dialogVisible"
    width="400px"
    :before-close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        :title="$t('deleteFeeType.confirmMessage')"
        type="warning"
        :closable="false"
        show-icon
      />
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('deleteFeeType.cancel') }}
      </el-button>
      <el-button type="primary" @click="deleteFeeType" :loading="loading">
        {{ $t('deleteFeeType.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteFeeType } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'DeleteFeeType',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      deleteFeeTypeInfo: {}
    }
  },
  methods: {
    open(params) {
      this.dialogVisible = true
      this.deleteFeeTypeInfo = { ...params }
    },
    
    handleClose() {
      this.dialogVisible = false
      this.deleteFeeTypeInfo = {}
    },
    
    async deleteFeeType() {
      try {
        this.loading = true
        
        const params = {
          ...this.deleteFeeTypeInfo,
          communityId: getCommunityId()
        }
        
        await deleteFeeType(params)
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        
        // 通知父组件刷新树
        this.$parent.$emit('listFeeTypeTree')
        
      } catch (error) {
        console.error('删除费用类型失败:', error)
        this.$message.error(error.message || this.$t('deleteFeeType.deleteError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>