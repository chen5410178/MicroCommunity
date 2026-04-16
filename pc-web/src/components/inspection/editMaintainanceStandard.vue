<template>
  <el-dialog
    :title="$t('maintainanceStandard.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('maintainanceStandard.form.standardName')"
        prop="standardName"
      >
        <el-input
          v-model="formData.standardName"
          :placeholder="$t('maintainanceStandard.placeholder.standardName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('maintainanceStandard.form.remark')"
        prop="remark"
      >
        <el-input
          v-model="formData.remark"
          type="textarea"
          :placeholder="$t('maintainanceStandard.placeholder.remark')"
          :rows="3"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMaintainanceStandard } from '@/api/inspection/maintainanceStandardManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMaintainanceStandard',
  data() {
    return {
      visible: false,
      formData: {
        standardId: '',
        standardName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        standardName: [
          { required: true, message: this.$t('maintainanceStandard.rules.standardName'), trigger: 'blur' },
          { max: 256, message: this.$t('maintainanceStandard.rules.standardNameMax'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('maintainanceStandard.rules.remark'), trigger: 'blur' },
          { max: 512, message: this.$t('maintainanceStandard.rules.remarkMax'), trigger: 'blur' }
        ],
        standardId: [
          { required: true, message: this.$t('maintainanceStandard.rules.standardId'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.formData = {
        standardId: row.standardId,
        standardName: row.standardName,
        remark: row.remark,
        communityId: getCommunityId()
      }
      this.visible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    handleClose() {
      this.formData = {
        standardId: '',
        standardName: '',
        remark: '',
        communityId: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMaintainanceStandard(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('maintainanceStandard.message.editFailed'))
          }
        }
      })
    }
  }
}
</script>