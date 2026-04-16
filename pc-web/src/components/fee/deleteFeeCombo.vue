<template>
  <el-dialog
    :title="$t('feeComboManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('feeComboManage.delete.confirmMessage') }}</p>
      <p class="combo-name">{{ form.comboName }}</p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('feeComboManage.delete.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('feeComboManage.delete.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFeeCombo } from '@/api/fee/feeComboManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFeeCombo',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        comboId: '',
        comboName: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = {
        comboId: data.comboId,
        comboName: data.comboName,
        communityId: getCommunityId()
      }
    },
    async handleConfirm() {
      this.loading = true
      try {
        await deleteFeeCombo(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('feeComboManage.delete.error'))
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.form = {
        comboId: '',
        comboName: '',
        communityId: ''
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
}

.combo-name {
  font-weight: bold;
  color: #f56c6c;
  margin-top: 10px;
}
</style>