<template>
  <el-dialog
    :title="$t('applyRoomDiscount.delete.title')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('applyRoomDiscount.delete.confirmText') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button 
        type="primary" 
        @click="confirmDelete"
        :loading="loading"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteApplyRoomDiscount } from '@/api/fee/applyRoomDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteApplyRoomDiscount',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        ardId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ardId: data.ardId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.form = {
        ardId: '',
        communityId: ''
      }
      this.loading = false
    },
    async confirmDelete() {
      try {
        this.loading = true
        await deleteApplyRoomDiscount(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('applyRoomDiscount.message.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}

.dialog-footer {
  text-align: right;
}
</style>