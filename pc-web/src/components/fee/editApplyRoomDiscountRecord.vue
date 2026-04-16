<template>
  <el-dialog
    :title="$t('applyRoomDiscount.editRecord.title')"
    :visible.sync="visible"
    width="60%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.ardId')"
        prop="ardId"
      >
        <el-input
          v-model="form.ardId"
          disabled
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.roomName')"
        prop="roomName"
      >
        <el-input
          v-model="form.roomName"
          disabled
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.applyTypeName')"
        prop="applyTypeName"
      >
        <el-input
          v-model="form.applyTypeName"
          disabled
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.createUserName')"
        prop="createUserName"
      >
        <el-input
          v-model="form.createUserName"
          disabled
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.createUserTel')"
        prop="createUserTel"
      >
        <el-input
          v-model="form.createUserTel"
          disabled
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.startTime')"
        prop="startTime"
      >
        <el-date-picker
          v-model="form.startTime"
          type="datetime"
          :placeholder="$t('applyRoomDiscount.editRecord.startTimePlaceholder')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width:100%"
          @change="validateDate"
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.endTime')"
        prop="endTime"
      >
        <el-date-picker
          v-model="form.endTime"
          type="datetime"
          :placeholder="$t('applyRoomDiscount.editRecord.endTimePlaceholder')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width:100%"
          @change="validateDate"
        />
      </el-form-item>

      <el-form-item
        :label="$t('applyRoomDiscount.editRecord.stateName')"
        prop="stateName"
      >
        <el-input
          v-model="form.stateName"
          disabled
        />
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
import { updateApplyRoomDiscount } from '@/api/fee/applyRoomDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditApplyRoomDiscountRecord',
  data() {
    return {
      visible: false,
      form: {
        ardId: '',
        roomName: '',
        roomId: '',
        discountId: '',
        discountName: '',
        applyTypeName: '',
        createUserName: '',
        createUserTel: '',
        startTime: '',
        endTime: '',
        stateName: '',
        state: '',
        communityId: ''
      },
      rules: {
        ardId: [
          { required: true, message: this.$t('applyRoomDiscount.validate.ardIdRequired'), trigger: 'blur' }
        ],
        roomName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.roomRequired'), trigger: 'blur' }
        ],
        applyTypeName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.applyTypeRequired'), trigger: 'blur' }
        ],
        createUserName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.createUserRequired'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.endTimeRequired'), trigger: 'change' }
        ],
        stateName: [
          { required: true, message: this.$t('applyRoomDiscount.validate.stateRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        ...data,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        ardId: '',
        roomName: '',
        roomId: '',
        discountId: '',
        discountName: '',
        applyTypeName: '',
        createUserName: '',
        createUserTel: '',
        startTime: '',
        endTime: '',
        stateName: '',
        state: '',
        communityId: ''
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
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.updateApplyRoomDiscount()
        }
      })
    },
    async updateApplyRoomDiscount() {
      try {
        await updateApplyRoomDiscount(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('applyRoomDiscount.message.updateFailed'))
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