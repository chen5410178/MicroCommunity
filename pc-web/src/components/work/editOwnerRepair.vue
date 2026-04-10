<template>
  <el-dialog :title="$t('repairPoolManage.modify')" :visible.sync="visible" width="50%" @close="resetForm">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('repairPoolManage.repairType')" prop="repairType"
        :rules="[{ required: true, message: $t('repairPoolManage.repairTypeRequired'), trigger: 'change' }]">
        <el-select v-model="formData.repairType" style="width:100%">
          <el-option v-for="item in repairSettings" :key="item.repairType" :label="item.repairTypeName"
            :value="item.repairType" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('repairPoolManage.repairPerson')" prop="repairName"
        :rules="[{ required: true, message: $t('repairPoolManage.repairPersonRequired'), trigger: 'blur' }]">
        <el-input v-model="formData.repairName" />
      </el-form-item>

      <el-form-item :label="$t('repairPoolManage.contact')" prop="tel" :rules="[
        { required: true, message: $t('repairPoolManage.contactRequired'), trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: $t('repairPoolManage.contactFormat'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.tel" type="tel" />
      </el-form-item>

      <el-form-item :label="$t('repairPoolManage.appointmentTime')" prop="appointmentTime"
        :rules="[{ required: true, message: $t('repairPoolManage.appointmentTimeRequired'), trigger: 'blur' }]">
        <el-date-picker v-model="formData.appointmentTime" type="datetime" style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('repairPoolManage.selectAppointmentTime')" />
      </el-form-item>

      <el-form-item :label="$t('repairPoolManage.repairContent')" prop="context"
        :rules="[{ required: true, message: $t('repairPoolManage.repairContentRequired'), trigger: 'blur' }]">
        <el-input v-model="formData.context" type="textarea" :rows="4"
          :placeholder="$t('repairPoolManage.repairContentPlaceholder')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateOwnerRepair,listRepairSettings } from '@/api/work/ownerRepairManageApi'
import { getCommunityId,getCommunityName } from '@/api/community/communityApi'

export default {
  name: 'EditOwnerRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairId: '',
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '',
        repairObjId: '',
        repairObjName: ''
      },
      repairSettings: []
    }
  },
  methods: {
    open(repair) {
      this.formData = { ...repair }
      this.changeRepairObjType()
      this.listRepairSettings()
      this.visible = true
    },

    async listRepairSettings() {
      try {
        const publicArea = this.formData.repairObjType === '004' ? 'F' : 'T'
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId(),
          publicArea
        }

        const res = await listRepairSettings(params)
        this.repairSettings = res.data || []
      } catch (error) {
        console.error('Failed to fetch repair settings:', error)
      }
    },

    changeRepairObjType() {
      // 根据报修对象类型设置公共区域标志
      this.listRepairSettings()
    },

    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.formData.repairObjType === '001') {
              this.formData.repairObjId = getCommunityId()
              this.formData.repairObjName = getCommunityName()
            }

            const data = {
              ...this.formData,
              communityId: getCommunityId()
            }

            await updateOwnerRepair(data)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Update owner repair failed:', error)
          }
        }
      })
    },

    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        repairId: '',
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '',
        repairObjId: '',
        repairObjName: ''
      }
    }
  }
}
</script>