<template>
  <el-dialog :title="$t('workType.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('workType.form.typeName')" prop="typeName">
        <el-input v-model.trim="formData.typeName" :placeholder="$t('workType.placeholder.typeName')" />
      </el-form-item>

      <el-form-item :label="$t('workType.form.deduction')" prop="deduction">
        <el-select v-model="formData.deduction" :placeholder="$t('workType.placeholder.deduction')" style="width:100%">
          <el-option v-for="item in deductionOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="el-form-item__tip" v-if="formData.deduction === 'Y'">
          {{ $t('workType.tip.deduction') }}
        </div>
      </el-form-item>

      <el-form-item :label="$t('workType.form.smsWay')" prop="smsWay">
        <el-select v-model="formData.smsWay" :placeholder="$t('workType.placeholder.smsWay')" style="width:100%">
          <el-option v-for="item in smsWayOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('workType.form.remark')">
        <el-input v-model.trim="formData.remark" type="textarea" :rows="3"
          :placeholder="$t('workType.placeholder.remark')" maxlength="500" show-word-limit />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateWorkType } from '@/api/oa/workTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditWorkType',
  data() {
    return {
      visible: false,
      formData: {
        wtId: '',
        typeName: '',
        deduction: '',
        smsWay: '',
        remark: ''
      },
      deductionOptions: [
        { value: 'Y', label: this.$t('workType.yes') },
        { value: 'N', label: this.$t('workType.no') }
      ],
      smsWayOptions: [
        { value: 'WECHAT', label: this.$t('workType.wechat') },
        { value: 'ALI_SMS', label: this.$t('workType.aliSms') },
        { value: 'WORK_LICENSE', label: this.$t('workType.workLicense') }
      ],
      rules: {
        typeName: [
          { required: true, message: this.$t('workType.validate.typeName'), trigger: 'blur' },
          { max: 200, message: this.$t('workType.validate.typeNameMax'), trigger: 'blur' }
        ],
        deduction: [
          { required: true, message: this.$t('workType.validate.deduction'), trigger: 'change' }
        ],
        smsWay: [
          { required: true, message: this.$t('workType.validate.smsWay'), trigger: 'change' }
        ],
        wtId: [
          { required: true, message: this.$t('workType.validate.wtId'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.formData = {
        wtId: row.wtId,
        typeName: row.typeName,
        deduction: row.deduction,
        smsWay: row.smsWay,
        remark: row.remark
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const params = {
              ...this.formData,
              communityId: getCommunityId()
            }
            await updateWorkType(params)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('workType.edit.error'))
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.el-form-item__tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>