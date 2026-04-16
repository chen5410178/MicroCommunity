<template>
  <el-dialog :title="$t('applyRoomDiscount.edit.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('applyRoomDiscount.edit.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime"
          :placeholder="$t('applyRoomDiscount.edit.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
          style="width:100%" @change="validateDate" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.edit.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime"
          :placeholder="$t('applyRoomDiscount.edit.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
          style="width:100%" @change="validateDate" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.edit.state')" prop="state">
        <el-select v-model="form.state" :placeholder="$t('applyRoomDiscount.edit.statePlaceholder')" style="width:100%">
          <el-option :label="$t('applyRoomDiscount.status.checkPass')" value="2" />
          <el-option :label="$t('applyRoomDiscount.status.checkReject')" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.edit.createRemark')">
        <el-input v-model="form.createRemark" type="textarea" :rows="3" disabled />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.edit.checkRemark')" prop="checkRemark">
        <el-input v-model="form.checkRemark" type="textarea"
          :placeholder="$t('applyRoomDiscount.edit.checkRemarkPlaceholder')" :rows="3" />
      </el-form-item>

      <el-form-item :label="$t('applyRoomDiscount.edit.images')">
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
import { updateApplyRoomDiscount } from '@/api/fee/applyRoomDiscountManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditApplyRoomDiscount',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        ardId: '',
        startTime: '',
        endTime: '',
        checkRemark: '',
        createRemark: '',
        state: '',
        photos: [],
        urls: [],
        communityId: ''
      },
      rules: {
        startTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('applyRoomDiscount.validate.endTimeRequired'), trigger: 'change' }
        ],
        state: [
          { required: true, message: this.$t('applyRoomDiscount.validate.stateRequired'), trigger: 'change' }
        ],
        checkRemark: [
          { required: true, message: this.$t('applyRoomDiscount.validate.checkRemarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('applyRoomDiscount.validate.checkRemarkFormat'), trigger: 'blur' }
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
      setTimeout(() => {
        if (this.$refs.uploadImage) {
          this.$refs.uploadImage.setImages(this.form.urls || [])
        }
      }, 500)
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      if (this.$refs.uploadImage) {
        this.$refs.uploadImage.close()
      }
      this.form = {
        ardId: '',
        startTime: '',
        endTime: '',
        checkRemark: '',
        createRemark: '',
        state: '',
        photos: [],
        urls: [],
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
    handleImageChange(photos) {
      this.form.photos = photos
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