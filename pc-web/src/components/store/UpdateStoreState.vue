<template>
  <el-dialog
    :title="$t('propertyCompanyManage.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div>{{ $t('propertyCompanyManage.confirmUpdateState') }} {{ stateText }}?</div>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateStoreState } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'UpdateStoreState',
  data() {
    return {
      visible: false,
      storeInfo: {},
      state: ''
    }
  },
  computed: {
    stateText() {
      return this.state === '48002' 
        ? this.$t('propertyCompanyManage.restrictLogin')
        : this.$t('propertyCompanyManage.restoreLogin')
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.storeInfo = row.storeInfo
      this.state = row.state
    },
    async handleConfirm() {
      try {
        await updateStoreState({
          storeId: this.storeInfo.storeId,
          state: this.state
        })
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