<template>
  <el-dialog :title="$t('activitiesTypeManage.add.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('activitiesTypeManage.add.typeName')" prop="typeName">
        <el-input v-model="formData.typeName" :placeholder="$t('activitiesTypeManage.add.typeNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.add.typeDesc')" prop="typeDesc">
        <el-input v-model="formData.typeDesc" :placeholder="$t('activitiesTypeManage.add.typeDescPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.add.seq')" prop="seq">
        <el-input v-model="formData.seq" :placeholder="$t('activitiesTypeManage.add.seqPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('activitiesTypeManage.add.defaultShow')" prop="defaultShow">
        <el-select v-model="formData.defaultShow" :placeholder="$t('activitiesTypeManage.add.defaultShowPlaceholder')"
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
import { saveActivitiesType } from '@/api/oa/activitiesTypeManageApi'

export default {
  name: 'AddActivitiesType',
  data() {
    return {
      visible: false,
      formData: {
        typeName: '',
        typeDesc: '',
        seq: '',
        defaultShow: '',
        communityId: ''
      },
      rules: {
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
    open() {
      this.visible = true
      this.formData.communityId = getCommunityId()
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
            await saveActivitiesType({
              ...this.formData,
              seq: Number(this.formData.seq)
            })
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(this.$t('activitiesTypeManage.add.error'))
          }
        }
      })
    }
  }
}
</script>