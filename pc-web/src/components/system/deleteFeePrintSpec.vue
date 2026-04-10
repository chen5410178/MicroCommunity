<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    center>
    <div class="text-center">
      <p>{{ $t('common.deleteConfirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button 
        type="primary" 
        @click="confirmDelete"
        :loading="deleting">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFeePrintSpec } from '@/api/system/feePrintSpecManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFeePrintSpec',
  data() {
    return {
      visible: false,
      deleting: false,
      currentItem: {
        printId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(item) {
      this.visible = true
      this.currentItem = {
        printId: item.printId,
        communityId: getCommunityId()
      }
    },
    async confirmDelete() {
      this.deleting = true
      try {
        await deleteFeePrintSpec(this.currentItem)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('feePrintSpec.deleteFailed'))
      } finally {
        this.deleting = false
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
  font-size: 16px;
  margin-bottom: 20px;
}
</style>