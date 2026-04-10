<template>
  <el-dialog :title="$t('maintainanceTaskTransfer.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('maintainanceTaskTransfer.transferTarget')" prop="staffId">
        <el-select v-model="form.staffId" :placeholder="$t('inspectionTaskTransfer.required')">
          <el-option v-for="item in staffs" :key="item.userId" :label="item.name" :value="item.userId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('maintainanceTaskTransfer.transferDesc')" prop="transferDesc">
        <el-input type="textarea" :rows="3" v-model="form.transferDesc"
          :placeholder="$t('maintainanceTaskTransfer.transferDescPlaceholder')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('maintainanceTaskTransfer.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('maintainanceTaskTransfer.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateMaintainanceTask } from '@/api/inspection/maintainanceTaskManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import { queryStaffInfos } from '@/api/staff/staffApi.js'
import { getUserId } from '@/api/user/userApi'


export default {
  name: 'MaintainanceTaskTransfer',
  components: {
  },
  data() {
    return {
      visible: false,
      staffs:[],
      form: {
        flowComponent: 'maintainanceTaskManage',
        transferDesc: '',
        staffId: '',
        staffName: '',
        communityId: '',
        actInsTime: '',
        actUserId: '',
        actUserName: '',
        maintainancePlanId: '',
        maintainancePlanName: '',
        planEndTime: '',
        planInsTime: '',
        planUserId: '',
        planUserName: '',
        signType: '',
        signTypeName: '',
        state: '',
        stateName: '',
        statusCd: '',
        taskId: '',
        taskType: 2000,
        currentUserId: '',
        orgId: '',
        parentId: ''
      },
      rules: {
        staffId: [
          { required: true, message: this.$t('maintainanceTaskTransfer.staffRequired'), trigger: 'blur' }
        ],
        transferDesc: [
          { required: true, message: this.$t('maintainanceTaskTransfer.descRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('maintainanceTaskTransfer.descMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        ...data,
        currentUserId: getUserId(),
        communityId: getCommunityId()
      }
      this.loadStaffs()
      this.visible = true
    },
    async loadStaffs() {
      const { staffs } = await queryStaffInfos({
        page: 1,
        row: 1000
      })
      this.staffs = staffs
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.$refs.orgTree.clearAll()
      this.$refs.staffSelect.clearStaff()
    },
    handleSwitchOrg(org) {
      this.form.orgId = org.orgId
      this.form.parentId = org.parentId
      this.$refs.staffSelect.setStaff(org)
    },
    handleStaffSelect(param) {
      if (param.staffId) {
        this.form.staffId = param.staffId
        this.form.staffName = param.staffName
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        if (this.form.staffId === this.form.planUserId) {
          this.$message.warning(this.$t('maintainanceTaskTransfer.sameUserError'))
          return
        }

        this.loading = true
        updateMaintainanceTask(this.form)
          .then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          })
          .catch(error => {
            this.$message.error(error.message || this.$t('maintainanceTaskTransfer.error'))
          })
          .finally(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>