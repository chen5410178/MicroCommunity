<template>
  <el-dialog 
    :title="$t('supplierManage.config')" 
    :visible.sync="visible" 
    width="800px"
    @close="resetForm">
    <el-form ref="form" :model="form" label-width="200px">
      <el-form-item 
        v-for="(item, index) in form.configs" 
        :key="index"
        :label="item.name">
        <el-input 
          v-model="item.columnValue" 
          :placeholder="item.remark" 
          class="w-100" />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('supplierManage.cancel') }}</el-button>
      <el-button type="primary" @click="saveConfigSupplierInfo">{{ $t('supplierManage.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listSupplierConfig, saveSupplierConfig } from '@/api/scm/supplierManageApi'

export default {
  name: 'ConfigSupplier',
  data() {
    return {
      visible: false,
      form: {
        supplierId: '',
        configs: []
      }
    }
  },
  methods: {
    async open(row) {
      this.form.supplierId = row.supplierId
      this.visible = true
      await this.loadSupplierConfigs()
    },
    
    async loadSupplierConfigs() {
      if (!this.form.supplierId) return
      
      try {
        const res = await listSupplierConfig({
          supplierId: this.form.supplierId,
          page: 1,
          row: 100
        })
        this.form.configs = res.data || []
      } catch (error) {
        console.error('加载供应商配置失败:', error)
      }
    },
    
    async saveConfigSupplierInfo() {
      try {
        const res = await saveSupplierConfig(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
        } else {
          this.$message.error(res.msg || this.$t('common.saveFailed'))
        }
      } catch (error) {
        console.error('保存供应商配置失败:', error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    
    resetForm() {
      this.form = {
        supplierId: '',
        configs: []
      }
    }
  }
}
</script>

<style scoped>
.w-100 {
  width: 100%;
}
</style>