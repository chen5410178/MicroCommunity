<template>
  <el-dialog 
    :title="$t('cityArea.addArea')" 
    :visible.sync="visible" 
    width="50%">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item 
        :label="$t('cityArea.areaName')" 
        prop="areaName"
        :rules="[{ required: true, message: $t('cityArea.areaNameRequired'), trigger: 'blur' }]">
        <el-input 
          v-model="formData.areaName" 
          :placeholder="$t('cityArea.areaNamePlaceholder')" />
      </el-form-item>
      <el-form-item 
        :label="$t('cityArea.areaCode')" 
        prop="areaCode"
        :rules="[{ required: true, message: $t('cityArea.areaCodeRequired'), trigger: 'blur' }]">
        <el-input 
          v-model="formData.areaCode" 
          :placeholder="$t('cityArea.areaCodePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('cityArea.parent')">
        <el-input 
          v-model="formData.parentAreaName" 
          disabled />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addArea } from '@/api/community/cityAreaApi'

export default {
  name: 'AddCityArea',
  data() {
    return {
      visible: false,
      formData: {
        areaCode: '',
        areaName: '',
        areaLevel: '',
        parentAreaCode: '',
        parentAreaName: '',
        lon: '',
        lat: ''
      }
    }
  },
  methods: {
    open(params) {
      this.formData = {
        ...this.formData,
        parentAreaCode: params.parentAreaCode,
        areaLevel: params.areaLevel,
        parentAreaName: params.parentAreaName
      }
      this.visible = true
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await addArea(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      } catch (error) {
        if (error !== 'validate') {
          this.$message.error(error.message || this.$t('common.saveFailed'))
        }
      }
    }
  }
}
</script>