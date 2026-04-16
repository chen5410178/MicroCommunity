<template>
  <el-dialog :title="$t('resourceStoreManage.editResourceType')" :visible.sync="dialogVisible" width="40%"
    :close-on-click-modal="false" @closed="handleClosed">
    <el-form ref="editForm" :model="formData" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('resourceStoreManage.typeName')" prop="name">
        <el-input v-model.trim="formData.name" :placeholder="$t('resourceStoreManage.enterTypeName')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('resourceStoreManage.description')">
        <el-input type="textarea" :placeholder="$t('resourceStoreManage.enterTypeDescription')"
          v-model.trim="formData.description"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="updateResourceStoreType" :loading="saving">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateResourceStoreType } from '@/api/resource/resourceStoreManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditResourceStoreType',
  data() {
    return {
      dialogVisible: false,
      saving: false,
      formData: {
        rstId: '',
        name: '',
        description: '',
        communityId: getCommunityId()
      },
      rules: {
        name: [
          { required: true, message: this.$t('resourceStoreManage.typeNameRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(typeData) {
      if (typeData) {
        this.formData = {
          rstId: typeData.rstId,
          name: typeData.name,
          parentId: typeData.parentId,
          description: typeData.description || '',
          communityId: getCommunityId()
        }
      }
      this.dialogVisible = true
    },

    async updateResourceStoreType() {
      this.$refs.editForm.validate(async valid => {
        if (valid) {
          try {
            this.saving = true
            await updateResourceStoreType(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新物品类型失败:', error)
            this.$message.error(this.$t('resourceStoreManage.updateTypeFailed'))
          } finally {
            this.saving = false
          }
        }
      })
    },

    handleClosed() {
      this.$refs.editForm.resetFields()
      this.formData = {
        rstId: '',
        name: '',
        description: '',
        communityId: getCommunityId()
      }
    }
  }
}
</script>

<style scoped>
.el-form-item__content {
  margin-left: 0 !important;
}
</style>