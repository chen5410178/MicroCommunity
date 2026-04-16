<template>
  <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%" :close-on-click-modal="false"
    @closed="handleClosed">
    <el-form ref="addForm" :model="formData" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('resourceStoreManage.typeName')" prop="name">
        <el-input v-model.trim="formData.name" :placeholder="$t('resourceStoreManage.enterTypeName')"
          maxlength="20"></el-input>
      </el-form-item>

      <el-form-item :label="$t('resourceStoreManage.description')">
        <el-input type="textarea" :placeholder="$t('resourceStoreManage.enterTypeDescription')"
          v-model.trim="formData.description"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveResourceStoreType" :loading="saving">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveResourceStoreType } from '@/api/resource/resourceStoreManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddResourceStoreType',
  data() {
    return {
      dialogVisible: false,
      saving: false,
      level: 1,
      formData: {
        name: '',
        description: '',
        parentId: '0',
        communityId: getCommunityId()
      },
      rules: {
        name: [
          { required: true, message: this.$t('resourceStoreManage.typeNameRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return !this.formData.parentId
        ? this.$t('resourceStoreManage.addFirstLevelType')
        : this.$t('resourceStoreManage.addSecondLevelType')
    }
  },
  methods: {
    open(rstId) {

      this.formData.parentId = rstId

      this.dialogVisible = true
    },

    async saveResourceStoreType() {
      this.$refs.addForm.validate(async valid => {
        if (valid) {
          try {
            this.saving = true
            await saveResourceStoreType(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加物品类型失败:', error)
            this.$message.error(this.$t('resourceStoreManage.addTypeFailed'))
          } finally {
            this.saving = false
          }
        }
      })
    },

    handleClosed() {
      this.$refs.addForm.resetFields()
      this.formData = {
        name: '',
        description: '',
        parentId: '0',
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