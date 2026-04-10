<template>
  <el-dialog
    :title="$t('meterTypeManage.editTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('meterTypeManage.typeId')" prop="typeId">
        <el-input v-model="form.typeId" disabled />
      </el-form-item>
      <el-form-item :label="$t('meterTypeManage.typeName')" prop="typeName">
        <el-input
          v-model="form.typeName"
          :placeholder="$t('meterTypeManage.typeNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('meterTypeManage.ratio')" prop="ratio">
        <el-input
          v-model.number="form.ratio"
          :placeholder="$t('meterTypeManage.ratioPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('meterTypeManage.remark')" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('meterTypeManage.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateMeterType } from '@/api/fee/meterTypeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMeterType',
  data() {
    return {
      visible: false,
      form: {
        typeId: '',
        typeName: '',
        ratio: undefined,
        remark: '',
        communityId: ''
      },
      rules: {
        typeId: [
          { required: true, message: this.$t('meterTypeManage.typeIdRequired'), trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: this.$t('meterTypeManage.typeNameRequired'), trigger: 'blur' },
          { max: 12, message: this.$t('meterTypeManage.typeNameMaxLength'), trigger: 'blur' }
        ],
        ratio: [
          { required: true, message: this.$t('meterTypeManage.ratioRequired'), trigger: 'blur' },
        ],
        remark: [
          { required: true, message: this.$t('meterTypeManage.remarkRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('meterTypeManage.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        ...row,
        communityId: getCommunityId()
      }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMeterType(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('meterTypeManage.editFailed'))
          }
        }
      })
    }
  }
}
</script>