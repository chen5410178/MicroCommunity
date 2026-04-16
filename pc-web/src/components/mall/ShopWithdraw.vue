<template>
  <el-dialog :title="$t('shopWithdraw.title')" :visible.sync="visible" width="500px" @close="handleClose">
    <el-form label-width="100px">
      <el-form-item :label="$t('shopWithdraw.remark')">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('shopWithdraw.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('shopWithdraw.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { auditShop } from '@/api/mall/shopManageApi'

export default {
  name: 'ShopWithdraw',
  data() {
    return {
      visible: false,
      form: {
        remark: '',
        state: '001'
      }
    }
  },
  methods: {
    open(shop) {
      this.form = {
        ...shop,
        remark: '',
        state: '001'
      }
      this.visible = true
    },
    handleClose() {
      this.form = {
        remark: '',
        state: '001'
      }
    },
    async handleSubmit() {
      if (!this.form.remark) {
        this.$message.warning(this.$t('shopWithdraw.remarkRequired'))
        return
      }

      try {
        await auditShop(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('shopWithdraw.error'))
      }
    }
  }
}
</script>