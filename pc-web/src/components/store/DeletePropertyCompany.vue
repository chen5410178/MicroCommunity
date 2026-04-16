<template>
  <el-dialog
    :title="$t('propertyCompanyManage.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div>{{ $t('propertyCompanyManage.confirmDelete') }}</div>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteProperty } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'DeletePropertyCompany',

  data() {
    return {
      visible: false,
      storeId: '',
      storeTypeCd:''
    } 
  },
  methods: {
    open(row) {
      this.visible = true
      this.storeId = row.storeId
      this.storeTypeCd = row.storeTypeCd
    },
    async handleConfirm() {
      try {
        await deleteProperty({ storeId: this.storeId, storeTypeCd: this.storeTypeCd })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>