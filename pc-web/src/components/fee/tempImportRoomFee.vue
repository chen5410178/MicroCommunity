<template>
  <el-dialog :title="$t('tempImportRoomFee.title')" :visible.sync="dialogVisible" width="40%"
    @close="clearTempImportRoomFeeInfo">
    <el-form :model="form" label-width="120px" class="text-left">
      <el-form-item :label="$t('tempImportRoomFee.feeType')">
        <el-select v-model="form.feeTypeCd" @change="changeFeeTypeCd"
          :placeholder="$t('tempImportRoomFee.selectFeeType')">
          <el-option v-for="(item, index) in feeTypeCds" :key="index" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.feeObj')">
        <el-input v-model="form.objName" disabled :placeholder="$t('tempImportRoomFee.inputFeeObj')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.feeName')" v-if="form.feeNameFlag === 'S'">
        <el-select v-model="form.configId" :placeholder="$t('tempImportRoomFee.selectFeeItem')">
          <el-option v-for="(item, index) in feeConfigs" :key="index" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
        <span>{{ $t('tempImportRoomFee.feeItemDesc') }}</span>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.feeName')" v-else>
        <el-col :span="18">
          <el-input v-model="form.feeName" :placeholder="$t('tempImportRoomFee.inputFeeName')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button @click="toggleFeeNameFlag">{{ $t('tempImportRoomFee.select') }}</el-button>
        </el-col>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.amount')">
        <el-input v-model="form.amount" :placeholder="$t('tempImportRoomFee.inputAmount')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.startTime')">
        <el-date-picker v-model="form.startTime" type="datetime" :placeholder="$t('tempImportRoomFee.selectStartTime')"
          value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('tempImportRoomFee.endTime')">
        <el-date-picker v-model="form.endTime" type="datetime" :placeholder="$t('tempImportRoomFee.selectEndTime')"
          value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('tempImportRoomFee.cancel') }}</el-button>
      <el-button type="primary" @click="saveTempImportFeeInfo">{{ $t('tempImportRoomFee.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importTempData } from '@/api/fee/tempImportRoomFeeApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listFeeConfigs } from '@/api/fee/feeConfigManageApi'
export default {
  name: 'TempImportRoomFee',
  data() {
    return {
      dialogVisible: false,
      form: {
        communityId: '',
        feeTypeCd: '',
        objType: '',
        feeTypeCds: [],
        configId: '',
        objName: '',
        objId: '',
        amount: '',
        startTime: this.formatDate(new Date()),
        endTime: this.addMonth(new Date(), 1),
        feeConfigs: [],
        feeNameFlag: 'S'
      },
      feeTypeCds: [],
      feeConfigs: [],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadDictData()
  },
  methods: {
    open(room) {
      this.clearTempImportRoomFeeInfo()
      this.form.objId = room.roomId
      this.form.objName = room.roomName
      this.form.objType = room.objType
      this.dialogVisible = true
    },
    async loadDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeCds = data
        this.form.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async changeFeeTypeCd(feeTypeCd) {
      try {
        const res = await listFeeConfigs({
          page: 1,
          row: 500,
          communityId: this.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          computingFormula: '4004',
          valid: '1'
        })
        this.feeConfigs = res.feeConfigs
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },
    toggleFeeNameFlag() {
      this.form.feeNameFlag = this.form.feeNameFlag === 'S' ? 'I' : 'S'
    },
    validateForm() {
      if (!this.form.feeTypeCd) {
        this.$message.warning(this.$t('tempImportRoomFee.feeTypeRequired'))
        return false
      }
      if (!this.form.configId && this.form.feeNameFlag === 'S') {
        this.$message.warning(this.$t('tempImportRoomFee.feeNameRequired'))
        return false
      }
      if (!this.form.objId) {
        this.$message.warning(this.$t('tempImportRoomFee.feeObjRequired'))
        return false
      }
      if (!this.form.startTime) {
        this.$message.warning(this.$t('tempImportRoomFee.startTimeRequired'))
        return false
      }
      if (!this.form.endTime) {
        this.$message.warning(this.$t('tempImportRoomFee.endTimeRequired'))
        return false
      }
      if (new Date(this.form.startTime) >= new Date(this.form.endTime)) {
        this.$message.warning(this.$t('tempImportRoomFee.endTimeMustAfterStart'))
        return false
      }
      if (!this.form.amount) {
        this.$message.warning(this.$t('tempImportRoomFee.amountRequired'))
        return false
      }
      return true
    },
    async saveTempImportFeeInfo() {
      if (!this.validateForm()) return

      try {
        const res = await importTempData({
          ...this.form,
          communityId: this.communityId
        })
        if (res.code == 0) {
          this.dialogVisible = false
          this.$emit('success')
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(this.$t('tempImportRoomFee.createFailed'))
      }
    },
    clearTempImportRoomFeeInfo() {
      this.form = {
        communityId: this.communityId,
        feeTypeCd: '',
        feeTypeCds: this.feeTypeCds,
        configId: '',
        objName: '',
        objId: '',
        amount: '',
        startTime: this.formatDate(new Date()),
        endTime: this.addMonth(new Date(), 1),
        feeNameFlag: 'S',
        feeConfigs: []
      }
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    addMonth(date, months) {
      const newDate = new Date(date)
      newDate.setMonth(newDate.getMonth() + months)
      return this.formatDate(newDate)
    }
  }
}
</script>
<style scoped>
.el-form-item{
  width: 90%;
}
.el-date-editor{  
  width: 100%;
}
</style>