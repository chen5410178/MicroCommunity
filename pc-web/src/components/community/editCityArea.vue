<template>
  <el-dialog :title="$t('cityArea.editArea')" :visible.sync="visible" width="50%">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('cityArea.areaName')" prop="areaName"
        :rules="[{ required: true, message: $t('cityArea.areaNameRequired'), trigger: 'blur' }]">
        <el-input v-model="formData.areaName" :placeholder="$t('cityArea.areaNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('cityArea.areaCode')">
        <el-input v-model="formData.areaCode" disabled />
      </el-form-item>
      <el-form-item :label="$t('cityArea.parent')">
        <el-input v-model="formData.parentAreaName" disabled />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateArea, getAreaDetail } from '@/api/community/cityAreaApi'

export default {
  name: 'EditCityArea',
  data() {
    return {
      visible: false,
      formData: {
        id: '',
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
    async open(params) {
      this.formData = { ...params }
      try {
        const { areas } = await getAreaDetail({ id: params.id, page: 1, row: 1 })
        if (areas && areas.length > 0) {
          this.formData = { ...this.formData, ...areas[0] }
        }
        this.visible = true
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateArea(this.formData)
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