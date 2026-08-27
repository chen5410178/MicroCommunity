<template>
  <el-dialog :title="$t('sectionManage.addTitle')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <el-form ref="form" :model="addSectionInfo" :rules="rules" label-width="120px">
      <el-form-item :label="$t('sectionManage.parkingArea')" prop="paId">
        <el-input :value="addSectionInfo.paName" disabled></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.sectionNum')" prop="sectionNum">
        <el-input v-model="addSectionInfo.sectionNum" :placeholder="$t('sectionManage.sectionNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.sectionName')" prop="sectionName">
        <el-input v-model="addSectionInfo.sectionName" :placeholder="$t('sectionManage.sectionNamePlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.remark')">
        <el-input v-model="addSectionInfo.remark" type="textarea" :rows="3"
          :placeholder="$t('sectionManage.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('sectionManage.cancel') }}</el-button>
      <el-button type="primary" @click="addSection">{{ $t('sectionManage.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveParkingAreaSection } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'AddSection',
  data() {
    return {
      visible: false,
      addSectionInfo: {
        paId: '',
        paName: '',
        sectionNum: '',
        sectionName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        sectionNum: [
          { required: true, message: this.$t('sectionManage.sectionNumRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.refreshAddSectionInfo()
      if (params && params.paId) {
        this.addSectionInfo.paId = params.paId
        this.addSectionInfo.paName = params.paName || ''
      }
      this.addSectionInfo.communityId = this.getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    refreshAddSectionInfo() {
      this.addSectionInfo = {
        paId: '',
        paName: '',
        sectionNum: '',
        sectionName: '',
        remark: '',
        communityId: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    async addSection() {
      const isValid = await this.validateForm()
      if (!isValid) return

      try {
        await saveParkingAreaSection(this.addSectionInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')

        this.handleClose()
      } catch (error) {
        console.error('添加停车区域失败:', error)
        this.$message.error(error.message || this.$t('sectionManage.errorMessage'))
      }
    }
  }
}
</script>
