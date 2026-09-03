<template>
  <div class="add-apply-room-discount-container">
    <!-- 申请信息 -->
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('applyRoomDiscount.add.title') }}</span>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="text-left">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('applyRoomDiscount.add.room')" prop="roomName">
              <el-input v-model.trim="form.roomName"
                :placeholder="$t('applyRoomDiscount.add.roomPlaceholder')" @blur="queryRoom" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('applyRoomDiscount.add.applyType')" prop="applyType">
              <el-select v-model="form.applyType"
                :placeholder="$t('applyRoomDiscount.add.applyTypePlaceholder')" style="width:100%">
                <el-option v-for="item in applyTypes" :key="item.applyType" :label="item.typeName"
                  :value="item.applyType" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('applyRoomDiscount.add.createUser')" prop="createUserName">
              <el-input v-model.trim="form.createUserName"
                :placeholder="$t('applyRoomDiscount.add.createUserPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('applyRoomDiscount.add.createUserTel')" prop="createUserTel">
              <el-input v-model.trim="form.createUserTel"
                :placeholder="$t('applyRoomDiscount.add.createUserTelPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('applyRoomDiscount.add.createRemark')" prop="createRemark">
              <el-input v-model="form.createRemark" type="textarea" :rows="3"
                :placeholder="$t('applyRoomDiscount.add.createRemarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="valid-tip">{{ $t('applyRoomDiscount.add.validTip') }}</div>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 优惠费用 -->
    <el-card class="margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('applyRoomDiscount.add.feeListTitle') }}</span>
        <div>
          <el-button size="mini" @click="handleSelectAll">{{ $t('applyRoomDiscount.add.selectAll') }}</el-button>
          <el-button size="mini" @click="handleClearAll">{{ $t('applyRoomDiscount.add.clearAll') }}</el-button>
        </div>
      </div>
      <el-table ref="feeTable" v-loading="feeLoading" :data="feeList" border style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="feeName" :label="$t('applyRoomDiscount.add.feeName')" align="center" />
        <el-table-column :label="$t('applyRoomDiscount.add.feePeriod')" align="center" :formatter="formatFeePeriod" />
        <el-table-column prop="oweAmount" :label="$t('applyRoomDiscount.add.oweAmount')" align="center" />
      </el-table>
      <div v-if="form.roomId && feeList.length === 0" class="no-fee-tip">
        {{ $t('applyRoomDiscount.add.noFeeTip') }}
      </div>
    </el-card>

    <div class="submit-wrapper">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" :loading="submitting" @click="submitForm">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  queryApplyRoomDiscountType,
  listRoomsWhereFeeSet,
  saveApplyRoomDiscount
} from '@/api/fee/applyRoomDiscountManageApi'
import { listFee } from '@/api/fee/feeApi'
import { dateFormat, getDateStr } from '@/utils/dateUtil'

export default {
  name: 'AddApplyRoomDiscountList',
  data() {
    return {
      communityId: '',
      submitting: false,
      feeLoading: false,
      applyTypes: [],
      feeList: [],
      selectedFees: [],
      form: {
        roomName: '',
        roomId: '',
        applyType: '',
        createUserName: '',
        createUserTel: '',
        createRemark: '',
        photos: []
      },
      rules: {
        roomName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.roomRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('applyRoomDiscount.validate.roomFormat'), trigger: 'blur' }
        ],
        applyType: [
          { required: true, message: this.$t('applyRoomDiscount.validate.applyTypeRequired'), trigger: 'change' }
        ],
        createUserName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createUserRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('applyRoomDiscount.validate.createUserFormat'), trigger: 'blur' }
        ],
        createUserTel: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createUserTelRequired'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('applyRoomDiscount.validate.createUserTelFormat'), trigger: 'blur' }
        ],
        createRemark: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createRemarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('applyRoomDiscount.validate.createRemarkFormat'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getApplyTypes()
  },
  methods: {
    async getApplyTypes() {
      try {
        const { data } = await queryApplyRoomDiscountType({
          page: 1,
          row: 50,
          communityId: this.communityId
        })
        this.applyTypes = data
      } catch (error) {
        console.error('获取申请类型失败:', error)
      }
    },
    async queryRoom() {
      if (!this.form.roomName) {
        this.feeList = []
        this.selectedFees = []
        return
      }
      const parts = this.form.roomName.split('-')
      if (parts.length !== 3) {
        this.$message.error(this.$t('applyRoomDiscount.validate.roomFormat'))
        this.form.roomName = ''
        this.form.roomId = ''
        this.feeList = []
        this.selectedFees = []
        return
      }
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          flag: 0,
          floorNum: parts[0].trim(),
          unitNum: parts[1].trim(),
          roomNum: parts[2].trim()
        }
        const { rooms } = await listRoomsWhereFeeSet(params)
        if (rooms.length === 0) {
          this.$message.error(this.$t('applyRoomDiscount.validate.roomNotFound'))
          this.form.roomName = ''
          this.form.roomId = ''
          this.feeList = []
          this.selectedFees = []
          return
        }
        const room = rooms[0]
        this.form.roomId = room.roomId
        this.form.createUserName = room.ownerName || this.form.createUserName
        this.form.createUserTel = room.link || this.form.createUserTel
        this.queryRoomFees()
      } catch (error) {
        console.error('查询房屋失败:', error)
      }
    },
    async queryRoomFees() {
      if (!this.form.roomId) {
        return
      }
      this.feeLoading = true
      try {
        const params = {
          page: 1,
          row: 200,
          communityId: this.communityId,
          payerObjId: this.form.roomId,
          state: '2008001'
        }
        const { fees } = await listFee(params)
        this.feeList = fees || []
        this.selectedFees = []
        if (this.$refs.feeTable) {
          this.$refs.feeTable.clearSelection()
        }
      } catch (error) {
        console.error('查询费用失败:', error)
        this.$message.error(this.$t('applyRoomDiscount.fetchError'))
      } finally {
        this.feeLoading = false
      }
    },
    handleSelectionChange(selection) {
      this.selectedFees = selection
    },
    formatFeePeriod(row) {
      return dateFormat(row.startTime) + '~' + dateFormat(row.endTime)
    },
    handleSelectAll() {
      if (this.$refs.feeTable) {
        this.$refs.feeTable.clearSelection()
        this.$refs.feeTable.toggleAllSelection()
      }
    },
    handleClearAll() {
      if (this.$refs.feeTable) {
        this.$refs.feeTable.clearSelection()
      }
      this.selectedFees = []
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        if (!this.selectedFees || this.selectedFees.length === 0) {
          this.$message.error(this.$t('applyRoomDiscount.validate.feeIdsRequired'))
          return
        }
        this.saveApplyRoomDiscount()
      })
    },
    async saveApplyRoomDiscount() {
      this.submitting = true
      try {
        const feeIds = this.selectedFees.map(fee => fee.feeId)
        const feeNames = this.selectedFees.map(fee => fee.feeName)
        const data = {
          communityId: this.communityId,
          roomId: this.form.roomId,
          roomName: this.form.roomName,
          applyType: this.form.applyType,
          createUserName: this.form.createUserName,
          createUserTel: this.form.createUserTel,
          createRemark: this.form.createRemark,
          feeIds: feeIds,
          feeNames: feeNames,
          startTime: getDateStr(),
          endTime: '2050-01-01 23:59:59',
          photos: []
        }
        await saveApplyRoomDiscount(data)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('applyRoomDiscount.message.saveFailed'))
      } finally {
        this.submitting = false
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-apply-room-discount-container {
  padding: 20px;

  .valid-tip {
    color: #999;
    font-size: 12px;
    line-height: 20px;
  }

  .no-fee-tip {
    color: #999;
    font-size: 13px;
    text-align: center;
    padding: 15px 0;
  }

  .submit-wrapper {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
