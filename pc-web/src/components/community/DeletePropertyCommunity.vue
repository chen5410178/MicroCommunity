<template>
  <el-dialog
    :title="$t('propertyCommunity.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('propertyCommunity.delete.confirm') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('propertyCommunity.delete.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('propertyCommunity.delete.confirmButton') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { quitCommunity } from '@/api/community/propertyCommunityApi'

export default {
  name: 'DeletePropertyCommunity',
  data() {
    return {
      visible: false,
      form: {}
    }
  },
  methods: {
    open(params) {
      this.form = { ...params }
      this.visible = true
    },
    async handleSubmit() {
      try {
        await quitCommunity(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    },
    handleClose() {
      this.form = {}
    }
  }
}
</script>