<template>
  <el-dialog :title="$t('activitiesTypeManage.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('activitiesTypeManage.edit.typeCd')" prop="typeCd">
        <el-input v-model="formData.typeCd" disabled />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.edit.typeName')" prop="typeName">
        <el-input v-model="formData.typeName" :placeholder="$t('activitiesTypeManage.edit.typeNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.edit.typeDesc')" prop="typeDesc">
        <el-input v-model="formData.typeDesc" :placeholder="$t('activitiesTypeManage.edit.typeDescPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.edit.seq')" prop="seq">
        <el-input v-model="formData.seq" :placeholder="$t('activitiesTypeManage.edit.seqPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.edit.defaultShow')" prop="defaultShow">
        <el-select v-model="formData.defaultShow" :placeholder="$t('activitiesTypeManage.edit.defaultShowPlaceholder')"
          style="width:100%">
          <el-option :label="$t('common.yes')" value="Y" />
          <el-option :label="$t('common.no')" value="N" />
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
import { getCommunityId } from '@/api/community/communityApi'
import { updateActivitiesType } from '@/api/oa/activitiesTypeManageApi'

export default {
  name: 'EditActivitiesType',
  data() {
    return {
      visible: false,
      formData: {
        typeCd: '',
        typeName: '',
        typeDesc: '',
        seq: '',
        defaultShow: '',
        communityId: ''
      },
      rules: {
        typeCd: [
          { required: true, message: this.$t('activitiesTypeManage.validate.typeCdRequired'), trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: this.$t('activitiesTypeManage.validate.typeNameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('activitiesTypeManage.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        typeDesc: [
          { max: 500, message: this.$t('activitiesTypeManage.validate.typeDescMaxLength'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('activitiesTypeManage.validate.seqRequired'), trigger: 'blur' },
        ],
        defaultShow: [
          { required: true, message: this.$t('activitiesTypeManage.validate.defaultShowRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.formData = {
        ...row,
        communityId: getCommunityId()
      }
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
            await updateActivitiesType({
              ...this.formData,
              seq: Number(this.formData.seq)
            })
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(this.$t('activitiesTypeManage.edit.error'))
          }
        }
      })
    }
  }
}
</script>