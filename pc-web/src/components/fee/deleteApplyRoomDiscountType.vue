<template>
  <el-dialog
    :title="$t('discountType.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('discountType.delete.confirmText') }}</p>
      <p class="delete-item">
        <span class="label">{{ $t('discountType.table.applyType') }}:</span>
        <span>{{ formData.applyType }}</span>
      </p>
      <p class="delete-item">
        <span class="label">{{ $t('discountType.table.typeName') }}:</span>
        <span>{{ formData.typeName }}</span>
      </p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="danger" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteApplyRoomDiscountType } from '@/api/fee/discountTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteApplyRoomDiscountType',
  data() {
    return {
      visible: false,
      formData: {
        applyType: '',
        typeName: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.formData = { ...row }
    },
    handleClose() {
      this.formData = {
        applyType: '',
        typeName: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        this.formData.communityId = getCommunityId()
        await deleteApplyRoomDiscountType(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  padding: 0 20px;
}
.delete-item {
  margin: 10px 0;
}
.label {
  font-weight: bold;
  margin-right: 10px;
}
</style>