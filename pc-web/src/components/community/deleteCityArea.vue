<template>
  <el-dialog 
    :title="$t('cityArea.confirmOperation')" 
    :visible.sync="visible" 
    width="30%">
    <div class="text-center">
      <p>{{ $t('cityArea.confirmDeleteArea') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('cityArea.cancelDelete') }}</el-button>
      <el-button type="primary" @click="handleDelete">{{ $t('cityArea.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteArea } from '@/api/community/cityAreaApi'

export default {
  name: 'DeleteCityArea',
  data() {
    return {
      visible: false,
      currentArea: {}
    }
  },
  methods: {
    open(params) {
      this.currentArea = { ...params }
      this.visible = true
    },
    async handleDelete() {
      try {
        await deleteArea(this.currentArea)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>