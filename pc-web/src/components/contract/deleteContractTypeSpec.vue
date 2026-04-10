<template>
  <el-dialog
    :title="$t('contractTypeSpecManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('contractTypeSpecManage.delete.confirmText') }}</p>
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
import { deleteContractTypeSpec } from '@/api/contract/contractTypeSpecManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteContractTypeSpec',
  data() {
    return {
      visible: false,
      formData: {
        specCd: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        specCd: data.specCd,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.formData = {
        specCd: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deleteContractTypeSpec(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('contractTypeSpecManage.delete.error'))
      }
    }
  }
}
</script>