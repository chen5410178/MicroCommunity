<template>
  <el-dialog
    :title="$t('notepadManage.ownerRepair.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('notepadManage.ownerRepair.repairType')"
        prop="repairType"
      >
        <el-select
          v-model="formData.repairType"
          style="width:100%"
        >
          <el-option
            :label="$t('notepadManage.ownerRepair.selectRepairType')"
            disabled
            value=""
          />
          <el-option
            v-for="(item,index) in repairSettings"
            :key="index"
            :label="item.repairTypeName"
            :value="item.repairType"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.ownerRepair.repairObjName')"
        prop="repairObjName"
      >
        <el-input
          v-model="formData.repairObjName"
          :placeholder="$t('notepadManage.ownerRepair.repairObjNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.ownerRepair.repairName')"
        prop="repairName"
      >
        <el-input
          v-model="formData.repairName"
          :placeholder="$t('notepadManage.ownerRepair.repairNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.ownerRepair.tel')"
        prop="tel"
      >
        <el-input
          v-model="formData.tel"
          :placeholder="$t('notepadManage.ownerRepair.telPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.ownerRepair.appointmentTime')"
        prop="appointmentTime"
      >
        <el-date-picker
          v-model="formData.appointmentTime"
          type="datetime"
          style="width:100%"
          :placeholder="$t('notepadManage.ownerRepair.selectTime')"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.ownerRepair.context')"
        prop="context"
      >
        <el-input
          v-model="formData.context"
          type="textarea"
          rows="5"
          :placeholder="$t('notepadManage.ownerRepair.contextPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveOwnerRepair, listRepairSettings } from '@/api/oa/notepadManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NotepadOwnerRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '004',
        repairObjId: '',
        repairObjName: '',
        repairChannel: 'T',
        noteId: '',
        communityId: ''
      },
      repairSettings: [],
      rules: {
        repairType: [
          { required: true, message: this.$t('notepadManage.validate.repairType'), trigger: 'blur' }
        ],
        repairObjName: [
          { required: true, message: this.$t('notepadManage.validate.repairObjName'), trigger: 'blur' }
        ],
        repairName: [
          { required: true, message: this.$t('notepadManage.validate.repairName'), trigger: 'blur' },
          { min: 2, max: 10, message: this.$t('notepadManage.validate.repairNameLength'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('notepadManage.validate.tel'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('notepadManage.validate.telFormat'), trigger: 'blur' }
        ],
        appointmentTime: [
          { required: true, message: this.$t('notepadManage.validate.appointmentTime'), trigger: 'blur' }
        ],
        context: [
          { required: true, message: this.$t('notepadManage.validate.context'), trigger: 'blur' },
          { max: 2000, message: this.$t('notepadManage.validate.contextMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData = {
        ...this.formData,
        repairObjName: data.roomName,
        repairObjId: data.roomId,
        tel: data.link,
        repairName: data.objName,
        context: data.title,
        noteId: data.noteId,
        communityId: getCommunityId()
      }
      this.loadRepairSettings()
    },
    async loadRepairSettings() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.formData.communityId,
          publicArea: 'F'
        }
        const { data } = await listRepairSettings(params)
        this.repairSettings = data
      } catch (error) {
        console.error('获取报修设置失败:', error)
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveOwnerRepair(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('提交报修单失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '004',
        repairObjId: '',
        repairObjName: '',
        repairChannel: 'T',
        noteId: '',
        communityId: ''
      }
    }
  }
}
</script>