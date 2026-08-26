<template>
  <el-dialog :title="$t('propertyCompanyManage.edit')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('propertyCompanyManage.name')" prop="name" required>
        <el-input v-model="formData.name" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.address')" prop="address" required>
        <el-input v-model="formData.address" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.tel')" prop="tel" required>
        <el-input v-model="formData.tel" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.corporation')" prop="corporation" required>
        <el-input v-model="formData.corporation" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.foundingTime')" prop="foundingTime" required>
        <el-date-picker v-model="formData.foundingTime" type="date" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.landmark')" prop="nearByLandmarks" required>
        <el-input v-model="formData.nearByLandmarks" />
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateProperty } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'EditPropertyCompany',
  data() {
    return {
      visible: false,
      formData: {}
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.formData = { ...row }
    },
    async handleSubmit() {
      try {
        await updateProperty(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>