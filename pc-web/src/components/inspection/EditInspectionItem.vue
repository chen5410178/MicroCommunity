<template>
  <el-dialog
    :title="$t('inspectionItemManage.editTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('inspectionItemManage.itemNameLabel')" prop="itemName">
        <el-input 
          v-model="formData.itemName" 
          :placeholder="$t('inspectionItemManage.requiredItem')"
        />
      </el-form-item>
      <el-form-item :label="$t('inspectionItemManage.remarkLabel')" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="4"
          :placeholder="$t('inspectionItemManage.requiredRemark')"
        />
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
import { updateInspectionItem } from '@/api/inspection/inspectionItemManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditInspectionItem',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        itemId: '',
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
        ],
        itemId: [
          { required: true, message: this.$t('inspectionItemManage.requiredId'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(item) {
      this.formData = {
        ...item,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.formData = {
        itemId: '',
        itemName: '',
        remark: '',
        communityId: ''
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            await updateInspectionItem(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新巡检项目失败:', error)
            this.$message.error(error.message || this.$t('inspectionItemManage.editFailed'))
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>