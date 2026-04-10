<template>
  <el-dialog
    :title="$t('maintainanceItem.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('maintainanceItem.delete.confirmText') }}</p>
      <p class="delete-item">{{ form.itemTitle }}</p>
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
import { deleteMaintainanceItem } from '@/api/inspection/maintainanceItemApi'

export default {
  name: 'DeleteMaintainanceItem',
  data() {
    return {
      visible: false,
      form: {
        itemId: '',
        itemTitle: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      if (data) {
        this.form = {
          itemId: data.itemId,
          itemTitle: data.itemTitle,
          communityId: data.communityId || ''
        }
      }
      this.visible = true
    },
    handleClose() {
      this.form = {
        itemId: '',
        itemTitle: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deleteMaintainanceItem({
          itemId: this.form.itemId,
          communityId: this.form.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  
  .delete-item {
    font-weight: bold;
    color: #f56c6c;
    margin-top: 10px;
  }
}
</style>