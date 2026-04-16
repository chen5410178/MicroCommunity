<template>
  <el-dialog :title="$t('meterTypeManage.addTitle')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('meterTypeManage.typeName')" prop="typeName">
        <el-input v-model="form.typeName" :placeholder="$t('meterTypeManage.typeNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('meterTypeManage.ratio')" prop="ratio">
        <el-input v-model.number="form.ratio" :placeholder="$t('meterTypeManage.ratioPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('meterTypeManage.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3"
          :placeholder="$t('meterTypeManage.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveMeterType } from '@/api/fee/meterTypeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddMeterType',
  data() {
    return {
      visible: false,
      form: {
        typeName: '',
        ratio: 1,
        remark: '',
        communityId: ''
      },
      rules: {
        typeName: [
          { required: true, message: this.$t('meterTypeManage.typeNameRequired'), trigger: 'blur' },
          { max: 12, message: this.$t('meterTypeManage.typeNameMaxLength'), trigger: 'blur' }
        ],
        ratio: [
          { required: true, message: this.$t('meterTypeManage.ratioRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('meterTypeManage.ratioNumber'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('meterTypeManage.remarkRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('meterTypeManage.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.form.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const { code, msg } = await saveMeterType(this.form)
            if (code != 0) {
              this.$message.error(msg)
              return;
            }
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('meterTypeManage.addFailed'))
          }
        }
      })
    }
  }
}
</script>