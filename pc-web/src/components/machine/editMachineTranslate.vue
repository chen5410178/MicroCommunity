<template>
  <el-dialog
    :title="$t('machineTranslateManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('machineTranslateManage.edit.machineCode')">
        <el-input v-model="formData.machineCode" disabled />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.edit.machineId')">
        <el-input v-model="formData.machineId" disabled />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.edit.typeCd')">
        <el-select v-model="formData.typeCd" disabled style="width: 100%">
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.edit.objName')">
        <el-input v-model="formData.objName" disabled />
      </el-form-item>
      <el-form-item :label="$t('machineTranslateManage.edit.objId')">
        <el-input v-model="formData.objId" disabled />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('machineTranslateManage.button.resync') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { resendIot } from '@/api/machine/machineTranslateManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMachineTranslate',
  data() {
    return {
      visible: false,
      formData: {
        machineTranslateId: '',
        machineCode: '',
        machineId: '',
        typeCd: '',
        objName: '',
        objId: '',
        communityId: ''
      },
      typeOptions: [
        { value: '8899', label: this.$t('machineTranslateManage.type.owner') },
        { value: '9988', label: this.$t('machineTranslateManage.type.community') },
        { value: '7788', label: this.$t('machineTranslateManage.type.keyInfo') },
        { value: '6677', label: this.$t('machineTranslateManage.type.visitorInfo') },
        { value: '5566', label: this.$t('machineTranslateManage.type.staffFace') },
        { value: '4455', label: this.$t('machineTranslateManage.type.carInfo') },
        { value: '3344', label: this.$t('machineTranslateManage.type.deviceInfo') }
      ]
    }
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.formData.communityId = getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await resendIot(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('重新同步失败:', error)
      }
    }
  }
}
</script>