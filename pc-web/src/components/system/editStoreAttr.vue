<template>
  <el-dialog :title="$t('editStoreAttr.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="form.name">
        <el-input v-model="form.value" :placeholder="$t('editStoreAttr.attributePlaceholder')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateStoreAttr } from '@/api/system/storeInfoManageApi'

export default {
  name: 'EditStoreAttr',
  data() {
    return {
      visible: false,
      form: {
        attrId: '',
        value: '',
        name: ''
      },
      rules: {
        value: [
          { required: true, message: this.$t('editStoreAttr.validate.valueRequired'), trigger: 'blur' },
          { max: 50, message: this.$t('editStoreAttr.validate.valueMaxLength'), trigger: 'blur' }
        ],
        attrId: [
          { required: true, message: this.$t('editStoreAttr.validate.attrIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(storeAttr) {
      this.resetForm()
      this.form = {
        ...this.form,
        ...storeAttr
      }
      this.visible = true
    },
    resetForm() {
      this.form = {
        attrId: '',
        value: '',
        name: ''
      }
    },
    handleClose() {
      this.resetForm()
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateStoreAttr(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.saveFailed'))
          }
        }
      })
    }
  }
}
</script>