<template>
  <el-dialog :title="$t('scheduleClassesStaffManage.addTitle')" :visible.sync="visible" width="70%"
    @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="24">
        <select-staffs-div ref="selectStaffs" @selectStaffs="handleSelectStaffs" />
      </el-col>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveScheduleClassesStaff } from '@/api/org/scheduleClassesStaffManageApi'
import SelectStaffsDiv from '@/components/staff/selectStaffsDiv'

export default {
  name: 'AddScheduleClassesStaff',
  components: {
    SelectStaffsDiv
  },
  data() {
    return {
      visible: false,
      scheduleId: '',
      staffs: []
    }
  },
  methods: {
    open(scheduleId) {
      this.scheduleId = scheduleId
      this.visible = true
    },
    async handleSave() {
      if (this.staffs.length === 0) {
        this.$message.warning(this.$t('scheduleClassesStaffManage.selectStaffWarning'))
        return
      }

      try {
        const params = {
          scheduleId: this.scheduleId,
          staffs: this.staffs
        }
       const {code,msg} = await saveScheduleClassesStaff(params)
       if(code === 0){
        this.$emit('success')
        this.visible = false
       }else{
        this.$message.error(msg)
       }
      } catch (error) {
        this.$message.error(this.$t('common.saveError'))
      }
    },
    handleClose() {
      this.$refs.selectStaffs.reset()
    },
    handleSelectStaffs(staffs) {
      this.staffs = staffs
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
</style>