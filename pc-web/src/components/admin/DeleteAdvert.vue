<template>
  <el-dialog
    :title="$t('advertManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <p>{{ $t('advertManage.delete.confirm') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteAdvert } from '@/api/admin/advertManageApi'

export default {
  name: 'DeleteAdvert',
  data() {
    return {
      visible: false,
      advertId: ''
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.advertId = row.advertId
    },
    handleClose() {
      this.advertId = ''
    },
    async handleConfirm() {
      try {
        await deleteAdvert(this.advertId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>