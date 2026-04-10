<template>
  <el-dialog
    :title="$t('purchaseApplyManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('purchaseApplyManage.edit.state')" prop="state">
        <el-select v-model="form.state" style="width:100%">
          <el-option
            :label="$t('purchaseApplyManage.edit.stateRequired')"
            value=""
            disabled
          ></el-option>
          <el-option
            :label="$t('purchaseApplyManage.edit.stateOption1')"
            value="1001"
          ></el-option>
          <el-option
            :label="$t('purchaseApplyManage.edit.stateOption2')"
            value="2002"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePurchaseApply } from '@/api/resource/purchaseApplyManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditPurchaseApply',
  data() {
    return {
      visible: false,
      form: {
        applyOrderId: '',
        state: '',
        communityId: ''
      },
      rules: {
        state: [
          { required: true, message: this.$t('purchaseApplyManage.edit.stateRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('purchaseApplyManage.edit.stateFormatError'), trigger: 'blur' }
        ],
        applyOrderId: [
          { required: true, message: this.$t('purchaseApplyManage.edit.applyOrderIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...data,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitForm()
        }
      })
    },
    async submitForm() {
      try {
        await updatePurchaseApply(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('保存失败:', error)
      }
    }
  }
}
</script>