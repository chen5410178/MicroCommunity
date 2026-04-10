<template>
  <el-dialog
    :title="$t('itemReleaseTypeManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('itemReleaseTypeManage.form.typeName')"
        prop="typeName"
      >
        <el-input
          v-model="form.typeName"
          :placeholder="$t('itemReleaseTypeManage.form.typeNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('itemReleaseTypeManage.form.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('itemReleaseTypeManage.form.remarkPlaceholder')"
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
import { saveItemReleaseType } from '@/api/work/itemReleaseTypeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddItemReleaseType',
  data() {
    return {
      visible: false,
      form: {
        typeName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        typeName: [
          { required: true, message: this.$t('itemReleaseTypeManage.validate.typeNameRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('itemReleaseTypeManage.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('itemReleaseTypeManage.validate.remarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('itemReleaseTypeManage.validate.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.form.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveItemReleaseType(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(this.$t('itemReleaseTypeManage.add.error'))
          }
        }
      })
    }
  }
}
</script>