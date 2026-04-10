<template>
  <el-dialog :title="$t('inspectionItemManage.addTitle')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('inspectionItemManage.itemNameLabel')" prop="itemName">
        <el-input v-model="formData.itemName" :placeholder="$t('inspectionItemManage.requiredItem')" />
      </el-form-item>
      <el-form-item :label="$t('inspectionItemManage.remarkLabel')" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="4"
          :placeholder="$t('inspectionItemManage.requiredRemark')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('inspectionItemManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ $t('inspectionItemManage.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveInspectionItem } from '@/api/inspection/inspectionItemManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddInspectionItem',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        itemName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        itemName: [
          { required: true, message: this.$t('inspectionItemManage.requiredItem'), trigger: 'blur' },
          { max: 256, message: this.$t('inspectionItemManage.maxLength256'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('inspectionItemManage.requiredRemark'), trigger: 'blur' },
          { max: 512, message: this.$t('inspectionItemManage.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.resetForm()
    },
    resetForm() {
      this.formData = {
        itemName: '',
        remark: '',
        communityId: getCommunityId()
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleClose() {
      this.resetForm()
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            await saveInspectionItem(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加巡检项目失败:', error)
            this.$message.error(error.message || this.$t('inspectionItemManage.addFailed'))
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>