<template>
  <el-dialog
    :title="$t('feeComboManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('feeComboManage.edit.comboName')"
        prop="comboName"
      >
        <el-input
          v-model="form.comboName"
          :placeholder="$t('feeComboManage.edit.comboNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item :label="$t('feeComboManage.edit.remark')">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('feeComboManage.edit.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateFeeCombo } from '@/api/fee/feeComboManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditFeeCombo',
  data() {
    return {
      visible: false,
      form: {
        comboId: '',
        comboName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        comboName: [
          {
            required: true,
            message: this.$t('feeComboManage.validate.comboNameRequired'),
            trigger: 'blur'
          },
          {
            max: 30,
            message: this.$t('feeComboManage.validate.comboNameMaxLength'),
            trigger: 'blur'
          }
        ],
        comboId: [
          {
            required: true,
            message: this.$t('feeComboManage.validate.comboIdRequired'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = {
        ...data,
        communityId: getCommunityId()
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateFeeCombo(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('feeComboManage.edit.error'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        comboId: '',
        comboName: '',
        remark: '',
        communityId: ''
      }
    }
  }
}
</script>