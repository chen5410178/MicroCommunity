<template>
  <el-dialog :title="$t('couponRuleFee.deleteConfirm')" :visible.sync="visible" width="30%" center @close="handleClose">
    <div class="text-center">
      <p>{{ $t('couponRuleFee.deletePrompt') }}</p>
      <p class="delete-item">{{ currentItem.feeConfigName }}</p>
      <p class="delete-info">
        {{ currentItem.payStartTime }} ~ {{ currentItem.payEndTime }}
      </p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponRuleFee } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCouponRuleFee',
  data() {
    return {
      visible: false,
      loading: false,
      currentItem: {
        crfId: '',
        feeConfigName: '',
        payStartTime: '',
        payEndTime: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.currentItem = {
        crfId: row.crfId,
        feeConfigName: row.feeConfigName,
        payStartTime: row.payStartTime,
        payEndTime: row.payEndTime,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.loading = false
    },
    async handleConfirm() {
      this.loading = true
      try {
        await deleteCouponRuleFee({
          crfId: this.currentItem.crfId,
          communityId: this.currentItem.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('common.deleteFailed'))
        console.error('Failed to delete coupon rule fee:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.text-center {
  text-align: center;

  .delete-item {
    font-weight: bold;
    color: #f56c6c;
    margin: 10px 0;
  }

  .delete-info {
    color: #909399;
    font-size: 12px;
  }
}
</style>