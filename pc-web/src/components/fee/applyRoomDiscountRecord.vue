<template>
  <el-dialog :title="$t('applyRoomDiscountRecord.title')" :visible.sync="visible" width="60%"
    :before-close="handleClose">
    <el-form :model="form" label-width="120px" label-position="left" class="text-left">
      <el-form-item :label="$t('applyRoomDiscountRecord.room')">
        <el-input v-model="form.roomName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('applyRoomDiscountRecord.status')">
        <el-input v-model="form.stateName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('applyRoomDiscountRecord.isViolation')" prop="isTrue" required>
        <el-select v-model="form.isTrue" style="width:100%">
          <el-option :value="null" :label="$t('applyRoomDiscountRecord.selectViolation')" disabled></el-option>
          <el-option :value="true" :label="$t('common.yes')"></el-option>
          <el-option :value="false" :label="$t('common.no')"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('applyRoomDiscountRecord.remark')" prop="remark" required>
        <el-input type="textarea" v-model="form.remark"
          :placeholder="$t('applyRoomDiscountRecord.remarkPlaceholder')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('applyRoomDiscountRecord.uploadImages')">
        <upload-image-url ref="uploadImageUrl" @notifyUploadCoverImage="handleNotifyUploadCoverImage" />
      </el-form-item>
      <el-form-item :label="$t('applyRoomDiscountRecord.uploadVideo')">
        <upload-vedio ref="uploadVedio" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveApplyRoomDiscountRecordInfo">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addApplyRoomDiscountRecord } from '@/api/fee/listApplyRoomDiscountRecordApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import UploadVedio from '@/components/upload/uploadVedio'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ApplyRoomDiscountRecord',
  components: {
    UploadImageUrl,
    UploadVedio
  },
  props: {
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      form: {
        ardId: '',
        roomName: '',
        state: '',
        stateName: '',
        remark: '',
        roomId: '',
        photos: [],
        videoName: '',
        url: '',
        detailType: '1001',
        isTrue: null,
        communityId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.resetForm()
      this.form.ardId = params[0]
      this.form.state = params[1]
      this.form.stateName = params[2]
      this.form.roomId = params[3]
      this.form.roomName = params[4]
      this.form.communityId = getCommunityId()
      this.visible = true
    },
    resetForm() {
      this.form = {
        ardId: '',
        roomName: '',
        state: '',
        stateName: '',
        remark: '',
        roomId: '',
        photos: [],
        videoName: '',
        url: '',
        detailType: '1001',
        isTrue: null,
        communityId: ''
      }
      setTimeout(() => {
        this.$refs.uploadImageUrl.clearImages()
        this.$refs.uploadVedio.clear()
      }, 500)
    },
    handleClose() {
      this.visible = false
    },
    validateForm() {
   
      if (!this.form.remark) {
        this.$message.error(this.$t('applyRoomDiscountRecord.remarkRequired'))
        return false
      }
      return true
    },
    async saveApplyRoomDiscountRecordInfo() {
      if (!this.validateForm()) return

      try {
        this.form.videoName = this.$refs.uploadVedio.getVideoName()

        const res = await addApplyRoomDiscountRecord(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.log(error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    handleNotifyUploadCoverImage(photosUrl) {
      this.form.photos = photosUrl
    }
  }
}
</script>