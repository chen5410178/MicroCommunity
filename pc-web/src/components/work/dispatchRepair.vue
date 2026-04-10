<template>
  <el-dialog :title="$t('repairPoolManage.dispatch')" :visible.sync="visible" width="50%" @close="resetForm">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item v-if="formData.action === 'BACK'" :label="$t('repairPoolManage.repairPerson')" prop="staffName">
        <el-input v-model="formData.staffName" disabled />
      </el-form-item>

      <el-form-item v-else :label="$t('repairPoolManage.repairPerson')" prop="staffId"
        :rules="[{ required: true, message: $t('repairPoolManage.selectRepairPerson'), trigger: 'blur' }]">
        <el-select v-model="formData.staffId" style="width:100%">
          <el-option v-for="item in repairTypeUsers" :key="item.staffId" :label="item.staffName" :value="item.staffId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('repairPoolManage.operationSuggestions')" prop="context"
        :rules="[{ required: true, message: $t('repairPoolManage.operationSuggestions'), trigger: 'blur' }]">
        <el-input v-model="formData.context" type="textarea" :rows="4"
          :placeholder="$t('repairPoolManage.operationSuggestions')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { dispatchRepair } from '@/api/work/repairPoolManageApi'
import {listRepairTypeUsers} from '@/api/work/repairTypeUserApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'  

export default {
  name: 'DispatchRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairId: '',
        repairType: '',
        staffId: '',
        staffName: '',
        context: '',
        action: '',
        repairTypeUsers: [],
        currentUserId: ''
      },
      repairTypeUsers: []
    }
  },
  methods: {
    open(repair) {
      this.formData = {
        ...this.formData,
        repairId: repair.repairId,
        repairType: repair.repairType,
        action: repair.action || 'DISPATCH',
        currentUserId: getUserId()
      }

      if (repair.action === 'BACK') {
        this.formData.staffId = repair.preStaffId
        this.formData.staffName = repair.preStaffName
      }

      this.listRepairTypeUsers()
      this.visible = true
    },

    async listRepairTypeUsers() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId(),
          repairType: this.formData.repairType,
          state: '9999'
        }

        const res = await listRepairTypeUsers(params)
        this.repairTypeUsers = res.data || []
      } catch (error) {
        console.error('Failed to fetch repair type users:', error)
      }
    },

    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const staff = this.repairTypeUsers.find(item => item.staffId === this.formData.staffId)
            if (staff) {
              this.formData.staffName = staff.staffName
            }

            if (this.formData.action === 'TRANSFER' && this.formData.currentUserId === this.formData.staffId) {
              this.$message.error(this.$t('repairPoolManage.cannotTransferSelf'))
              return
            }

            const data = {
              ...this.formData,
              communityId: getCommunityId()
            }

            await dispatchRepair(data)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Dispatch repair failed:', error)
            this.$message.error(error)
          }
        }
      })
    },

    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        repairId: '',
        repairType: '',
        staffId: '',
        staffName: '',
        context: '',
        action: '',
        repairTypeUsers: [],
        currentUserId: getUserId()
      }
    }
  }
}
</script>