<template>
  <el-dialog :title="$t('sectionManage.editTitle')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <el-form ref="form" :model="editSectionInfo" :rules="rules" label-width="120px">
      <el-form-item :label="$t('sectionManage.parkingArea')" prop="paId">
        <el-input :value="editSectionInfo.paName" disabled></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.sectionNum')" prop="sectionNum">
        <el-input v-model="editSectionInfo.sectionNum" :placeholder="$t('sectionManage.sectionNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.sectionName')" prop="sectionName">
        <el-input v-model="editSectionInfo.sectionName" :placeholder="$t('sectionManage.sectionNamePlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('sectionManage.remark')">
        <el-input v-model="editSectionInfo.remark" type="textarea" :rows="3"
          :placeholder="$t('sectionManage.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('sectionManage.cancel') }}</el-button>
      <el-button type="primary" @click="editSection">{{ $t('sectionManage.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateParkingAreaSection } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'EditSection',
  data() {
    return {
      visible: false,
      editSectionInfo: {
        sectionId: '',
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
    open(row) {
      this.editSectionInfo = {
        sectionId: row.sectionId || '',
        paId: row.paId || '',
        paName: row.paName || '',
        sectionNum: row.sectionNum || '',
        sectionName: row.sectionName || '',
        remark: row.remark || '',
        communityId: this.getCommunityId()
      }
      this.visible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    handleClose() {
      this.visible = false
    },
    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    async editSection() {
      const isValid = await this.validateForm()
      if (!isValid) return

      try {
        await updateParkingAreaSection(this.editSectionInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')

        this.handleClose()
      } catch (error) {
        console.error('修改停车区域失败:', error)
        this.$message.error(error.message || this.$t('sectionManage.errorMessage'))
      }
    }
  }
}
</script>
