<template>
  <el-dialog :title="$t('applyRoomDiscount.add.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('applyRoomDiscount.add.room')" prop="roomName">
        <el-input v-model.trim="form.roomName" :placeholder="$t('applyRoomDiscount.add.roomPlaceholder')"
          @blur="queryRoom" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.applyType')" prop="applyType">
        <el-select v-model="form.applyType" :placeholder="$t('applyRoomDiscount.add.applyTypePlaceholder')"
          style="width:100%">
          <el-option v-for="item in applyTypes" :key="item.applyType" :label="item.typeName" :value="item.applyType" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.feeItem')" prop="feeId">
        <el-select v-model="form.feeId" :placeholder="$t('applyRoomDiscount.add.feeItemPlaceholder')"
          style="width:100%">
          <el-option v-for="item in feeTypeCds" :key="item.feeId"
            :label="`${item.feeName}(${item.endTime}至${item.deadlineTime})`" :value="item.feeId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.createUser')" prop="createUserName">
        <el-input v-model.trim="form.createUserName" :placeholder="$t('applyRoomDiscount.add.createUserPlaceholder')"
          disabled />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.createUserTel')" prop="createUserTel">
        <el-input v-model.trim="form.createUserTel" :placeholder="$t('applyRoomDiscount.add.createUserTelPlaceholder')"
          disabled />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="date"
          :placeholder="$t('applyRoomDiscount.add.startTimePlaceholder')" value-format="yyyy-MM-dd" style="width:100%"
          @change="validateDate" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="date" :placeholder="$t('applyRoomDiscount.add.endTimePlaceholder')"
          value-format="yyyy-MM-dd" style="width:100%" @change="validateDate" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.createRemark')" prop="createRemark">
        <el-input v-model="form.createRemark" type="textarea"
          :placeholder="$t('applyRoomDiscount.add.createRemarkPlaceholder')" :rows="3" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.add.images')">
        <upload-image-url ref="uploadImage" :image-count="4" @change="handleImageChange" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { queryApplyRoomDiscountType, listRoomsWhereFeeSet,saveApplyRoomDiscount } from '@/api/fee/applyRoomDiscountManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'
import { listFee } from '@/api/fee/feeApi'

export default {
  name: 'AddApplyRoomDiscount',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        roomName: '',
        roomId: '',
        applyType: '',
        createUserName: '',
        createUserTel: '',
        startTime: '',
        endTime: '',
        createRemark: '',
        feeId: '',
        photos: [],
        communityId: ''
      },
      applyTypes: [],
      feeTypeCds: [],
      rules: {
        roomName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.roomRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('applyRoomDiscount.validate.roomFormat'), trigger: 'blur' }
        ],
        applyType: [
          { required: true, message: this.$t('applyRoomDiscount.validate.applyTypeRequired'), trigger: 'change' }
        ],
        feeId: [
          { required: true, message: this.$t('applyRoomDiscount.validate.feeIdRequired'), trigger: 'change' }
        ],
        createUserName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createUserRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('applyRoomDiscount.validate.createUserFormat'), trigger: 'blur' }
        ],
        createUserTel: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createUserTelRequired'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('applyRoomDiscount.validate.createUserTelFormat'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.endTimeRequired'), trigger: 'change' }
        ],
        createRemark: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createRemarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('applyRoomDiscount.validate.createRemarkFormat'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getApplyTypes()
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.$refs.uploadImage.close()
      this.form = {
        roomName: '',
        roomId: '',
        applyType: '',
        createUserName: '',
        createUserTel: '',
        startTime: '',
        endTime: '',
        createRemark: '',
        feeId: '',
        photos: [],
        communityId: ''
      }
    },
    async getApplyTypes() {
      try {
        const { data } = await queryApplyRoomDiscountType({
          page: 1,
          row: 50,
          communityId: getCommunityId()
        })
        this.applyTypes = data
      } catch (error) {
        console.error('获取申请类型失败:', error)
      }
    },
    async queryRoom() {
      if (!this.form.roomName) return

      const parts = this.form.roomName.split('-')
      if (parts.length !== 3) {
        this.$message.error(this.$t('applyRoomDiscount.validate.roomFormat'))
        this.form.roomName = ''
        return
      }

      try {
        const params = {
          page: 1,
          row: 1,
          communityId: getCommunityId(),
          flag: 0,
          floorNum: parts[0].trim(),
          unitNum: parts[1].trim(),
          roomNum: parts[2].trim()
        }

        const { rooms } = await listRoomsWhereFeeSet(params)
        if (rooms.length === 0) {
          this.$message.error(this.$t('applyRoomDiscount.validate.roomNotFound'))
          this.form.roomName = ''
          return
        }

        const room = rooms[0]
        this.form.roomId = room.roomId
        this.form.createUserName = room.ownerName
        this.form.createUserTel = room.link
        this.queryRoomFees()
      } catch (error) {
        console.error('查询房屋失败:', error)
      }
    },
    async queryRoomFees() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId(),
          payerObjId: this.form.roomId,
          state: '2008001'
        }

        const { fees } = await listFee(params)
        this.feeTypeCds = fees
      } catch (error) {
        console.error('查询费用项失败:', error)
      }
    },
    validateDate() {
      if (this.form.startTime && this.form.endTime) {
        const start = new Date(this.form.startTime)
        const end = new Date(this.form.endTime)
        if (start >= end) {
          this.$message.error(this.$t('applyRoomDiscount.validate.dateInvalid'))
          this.form.endTime = ''
        }
      }
    },
    handleImageChange(photos) {
      this.form.photos = photos
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveApplyRoomDiscount()
        }
      })
    },
    async saveApplyRoomDiscount() {
      try {
        this.form.communityId = getCommunityId()
        await saveApplyRoomDiscount(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('applyRoomDiscount.message.saveFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 22px;
}
</style>