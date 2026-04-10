<template>
  <el-dialog
    :title="$t('storeInfoManage.common.confirm')"
    :visible.sync="visible"
    width="30%"
  >
    <div>
      {{ $t('storeInfoManage.delete.confirmDelete') }} {{ storeInfo.name }}?
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('storeInfoManage.common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('storeInfoManage.common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteStoreInfo } from '@/api/mall/storeInfoManageApi'

export default {
  name: 'DeleteStoreInfo',
  props: {
    storeInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteStoreInfo({ storeInfoId: this.storeInfo.storeInfoId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('storeInfoManage.error.deleteFailed'))
      }
    }
  }
}
</script>