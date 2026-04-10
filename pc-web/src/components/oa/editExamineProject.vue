<template>
  <el-dialog :title="$t('examineProjectManage.edit.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" label-position="right" class="text-left">
      <el-form-item :label="$t('examineProjectManage.form.name')" prop="name">
        <el-input v-model="formData.name" :placeholder="$t('examineProjectManage.form.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('examineProjectManage.form.post')" prop="postCd">
        <el-select v-model="formData.postCd" :placeholder="$t('examineProjectManage.form.postPlaceholder')"
          style="width:100%">
          <el-option :label="$t('examineProjectManage.form.commonPost')" value="9999" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('examineProjectManage.form.weight')" prop="weight">
        <el-input v-model="formData.weight" :placeholder="$t('examineProjectManage.form.weightPlaceholder')"
          style="width:80%">
          <template slot="append">%</template>
        </el-input>
      </el-form-item>
      <el-form-item :label="$t('examineProjectManage.form.state')" prop="state">
        <el-select v-model="formData.state" :placeholder="$t('examineProjectManage.form.statePlaceholder')"
          style="width:100%">
          <el-option :label="$t('examineProjectManage.status.enable')" value="Y" />
          <el-option :label="$t('examineProjectManage.status.disable')" value="N" />
        </el-select>
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
import { updateExamineProject } from '@/api/oa/examineProjectManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditExamineProject',
  data() {
    return {
      visible: false,
      formData: {
        projectId: '',
        name: '',
        postCd: '',
        post: this.$t('examineProjectManage.form.commonPost'),
        weight: '',
        state: '',
        communityId: getCommunityId()
      },
      rules: {
        projectId: [
          { required: true, message: this.$t('examineProjectManage.validate.idRequired'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('examineProjectManage.validate.nameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('examineProjectManage.validate.nameMaxLength'), trigger: 'blur' }
        ],
        postCd: [
          { required: true, message: this.$t('examineProjectManage.validate.postRequired'), trigger: 'change' },
          { max: 30, message: this.$t('examineProjectManage.validate.postMaxLength'), trigger: 'blur' }
        ],
        weight: [
          { required: true, message: this.$t('examineProjectManage.validate.weightRequired'), trigger: 'blur' },
          { max: 12, message: this.$t('examineProjectManage.validate.weightMaxLength'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('examineProjectManage.validate.stateRequired'), trigger: 'change' },
          { max: 12, message: this.$t('examineProjectManage.validate.stateMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        ...data,
        communityId: getCommunityId()
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
            await updateExamineProject(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('examineProjectManage.edit.error'))
          }
        }
      })
    }
  }
}
</script>