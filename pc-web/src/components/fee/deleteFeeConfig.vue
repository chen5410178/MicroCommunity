<template>
  <el-dialog
    :title="$t('deleteFeeConfig.title')"
    :visible.sync="dialogVisible"
    width="400px"
    :before-close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        :title="confirmMessage"
        type="warning"
        :closable="false"
        show-icon
      />
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('deleteFeeConfig.cancel') }}
      </el-button>
      <el-button type="primary" @click="deleteFeeConfig" :loading="loading">
        {{ $t('deleteFeeConfig.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteFeeConfig } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'DeleteFeeConfig',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      deleteFeeConfigInfo: {}
    }
  },
  computed: {
    confirmMessage() {
      return this.$t('deleteFeeConfig.confirmMessage')
    }
  },
  methods: {
    open(params) {
      this.dialogVisible = true
      this.deleteFeeConfigInfo = { ...params }
    },
    
    handleClose() {
      this.dialogVisible = false
      this.deleteFeeConfigInfo = {}
    },
    
    async deleteFeeConfig() {
      try {
        this.loading = true
        
        const params = {
          ...this.deleteFeeConfigInfo,
          communityId: getCommunityId()
        }
        
        await deleteFeeConfig(params)
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        
        // 通知父组件刷新列表
        this.$parent.$emit('listFeeConfig')
        
      } catch (error) {
        console.error('删除费用配置失败:', error)
        this.$message.error(error.message || this.$t('deleteFeeConfig.deleteError'))
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