<template>
  <el-dialog :title="$t('editFee.title')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <div class="ibox-content">
      <el-row :gutter="20">
        <el-col :span="4">
          <label class="col-form-label">
            {{$t('editFee.startTime')}}
          </label>
        </el-col>
        <el-col :span="20">
          <input v-model="editFeeInfo.startTime" type="text"
            :placeholder="$t('editFee.startTimePlaceholder')" class="form-control editFeeStartTime">
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4">
          <label class="col-form-label">
            {{$t('editFee.endTime')}}
          </label>
        </el-col>
        <el-col :span="20">
          <input v-model="editFeeInfo.endTime" type="text"
            :placeholder="$t('editFee.endTimePlaceholder')" class="form-control editFeeEndTime">
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4">
          <label class="col-form-label">
            {{$t('editFee.oweAmount')}}
          </label>
        </el-col>
        <el-col :span="20">
          <input v-model="editFeeInfo.oweAmount" type="text"
            :placeholder="$t('editFee.oweAmountPlaceholder')" class="form-control">
        </el-col>
      </el-row>
      <div class="ibox-content">
        <button class="btn btn-primary float-right" type="button" @click="_doEidtFee">
          <i class="fa fa-check"></i>&nbsp;{{$t('common.submit')}}
        </button>
        <button type="button" class="btn btn-warning float-right" style="margin-right:20px;" @click="handleClose">
          <i class="fa fa-times"></i>&nbsp;{{$t('common.cancel')}}
        </button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { updateFee } from '@/api/fee/editFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditFee',
  data() {
    return {
      visible: false,
      editFeeInfo: {
        feeId: '',
        startTime: '',
        endTime: '',
        oweAmount: ''
      }
    }
  },
  methods: {
    open(fee) {
      this.clearAddFeeConfigInfo()
      this.editFeeInfo = { ...fee }
      if (fee.startTime.indexOf(":") == -1) {
        this.editFeeInfo.startTime = fee.startTime
      }
      this.editFeeInfo.endTime = fee.endTime.split(' ')[0]
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.clearAddFeeConfigInfo()
    },
    _initEditFeeDateInfo() {
      // 初始化日期选择器
      this.$nextTick(() => {
        if (this.$el.querySelector('.editFeeStartTime')) {
          // 这里可以集成日期选择器
        }
      })
    },
    editFeeValidate() {
      if (!this.editFeeInfo.startTime) {
        this.$message.error(this.$t('editFee.startTimeRequired'))
        return false
      }
      if (!this.editFeeInfo.endTime) {
        this.$message.error(this.$t('editFee.endTimeRequired'))
        return false
      }
      return true
    },
    async _doEidtFee() {
      if (!this.editFeeValidate()) return

      this.editFeeInfo.communityId = getCommunityId()

      try {
        const res = await updateFee(this.editFeeInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(this.$t('common.operateFail'))
      }
    },
    clearAddFeeConfigInfo() {
      this.editFeeInfo = {
        feeId: '',
        startTime: '',
        endTime: '',
        oweAmount: ''
      }
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 15px;
}
.col-form-label {
  padding-top: 7px;
  margin-bottom: 0;
  text-align: right;
}
.form-control {
  display: block;
  width: 100%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}
.form-control:focus {
  color: #495057;
  background-color: #fff;
  border-color: #80bdff;
  outline: 0;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}
.btn {
  display: inline-block;
  font-weight: 400;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  user-select: none;
  border: 1px solid transparent;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border-radius: 0.25rem;
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  cursor: pointer;
}
.btn-primary {
  color: #fff;
  background-color: #007bff;
  border-color: #007bff;
}
.btn-warning {
  color: #212529;
  background-color: #ffc107;
  border-color: #ffc107;
}
.float-right {
  float: right;
}
.ibox-content {
  padding: 15px 20px 20px 20px;
}
</style>