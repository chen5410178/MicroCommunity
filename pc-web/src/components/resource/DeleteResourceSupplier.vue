<template>
  <el-dialog
    :title="$t('deleteResourceSupplier.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteResourceSupplier.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteResourceSupplier.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('deleteResourceSupplier.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteResourceSupplier } from '@/api/resource/resourceSupplierManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteResourceSupplier',
  data() {
    return {
      visible: false,
      form: {
        rsId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.form.rsId = row.rsId
    },
    handleClose() {
      this.form.rsId = ''
    },
    async handleConfirm() {
      try {
        const params = {
          rsId: this.form.rsId,
          communityId: getCommunityId()
        }
        await deleteResourceSupplier(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>