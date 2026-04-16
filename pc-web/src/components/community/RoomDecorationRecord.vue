<template>
  <el-dialog :title="$t('roomDecorationRecord.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="roomDecorationRecordInfo" label-width="120px" class="text-left" label-position="left">
      <el-form-item :label="$t('roomDecorationRecord.room')">
        <el-input v-model="roomDecorationRecordInfo.roomName" disabled
          :placeholder="$t('roomDecorationRecord.roomPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('roomDecorationRecord.status')">
        <el-input v-model="roomDecorationRecordInfo.stateName" disabled />
      </el-form-item>
      <el-form-item :label="$t('roomDecorationRecord.isViolation')" required>
        <el-select v-model="roomDecorationRecordInfo.isTrue" style="width: 100%"
          :placeholder="$t('roomDecorationRecord.isViolationPlaceholder')">
          <el-option value="" disabled :label="$t('roomDecorationRecord.isViolationPlaceholder')" />
          <el-option value="true" :label="$t('roomDecorationRecord.yes')" />
          <el-option value="false" :label="$t('roomDecorationRecord.no')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('roomDecorationRecord.remark')" required>
        <el-input v-model="roomDecorationRecordInfo.remark" type="textarea"
          :placeholder="$t('roomDecorationRecord.remarkPlaceholder')" :rows="3" />
      </el-form-item>
      <el-form-item :label="$t('roomDecorationRecord.uploadImage')">
        <upload-image-url ref="uploadImageUrl" :image-count="1" @notifyUploadCoverImage="handleImageChange" />
      </el-form-item>
      <el-form-item :label="$t('roomDecorationRecord.uploadVideo')">
        <upload-vedio ref="uploadVedio" @change="handleVideoChange" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveRoomDecorationRecordInfo">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRoomDecorationRecord } from '@/api/community/listRoomDecorationRecordApi'
import { getCommunityId } from '@/api/community/communityApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import UploadVedio from './UploadVedio'

export default {
  name: 'RoomDecorationRecord',
  components: {
    UploadImageUrl,
    UploadVedio
  },
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      roomDecorationRecordInfo: {
        rId: '',
        roomName: '',
        state: '',
        stateName: '',
        remark: '',
        examineRemark: '',
        roomId: '',
        photos: [],
        videoName: '',
        url: '',
        detailType: '1001',
        isTrue: '',
        isTrues: []
      }
    }
  },
  methods: {
    open(params) {
      this.clearRoomDecorationRecordInfo()
      this.roomDecorationRecordInfo.rId = params[0]
      this.roomDecorationRecordInfo.roomId = params[1]
      this.roomDecorationRecordInfo.roomName = params[2]
      this.roomDecorationRecordInfo.state = params[3]
      this.roomDecorationRecordInfo.stateName = params[4]
      this.visible = true
    },
    handleClose() {
      this.clearRoomDecorationRecordInfo()
    },
    handleImageChange(photosUrl) {
      this.roomDecorationRecordInfo.photos = photosUrl
    },
    handleVideoChange(videoInfo) {
      this.roomDecorationRecordInfo.videoName = videoInfo.realFileName
    },
    validateForm() {
      if (!this.roomDecorationRecordInfo.isTrue) {
        this.$message.error(this.$t('roomDecorationRecord.isViolationRequired'))
        return false
      }
      if (!this.roomDecorationRecordInfo.remark) {
        this.$message.error(this.$t('roomDecorationRecord.remarkRequired'))
        return false
      }
      return true
    },
    async saveRoomDecorationRecordInfo() {
      if (!this.validateForm()) return

      try {
        this.roomDecorationRecordInfo.communityId = getCommunityId()

      

        const {code,msg} = await updateRoomDecorationRecord(this.roomDecorationRecordInfo)
        if(code != 0){
          this.$message.error(msg);
          return;
        }
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    clearRoomDecorationRecordInfo() {
      setTimeout(() => {
      this.$refs.uploadImageUrl.clearImages()
      this.$refs.uploadVedio.clear()
      },100)
      this.roomDecorationRecordInfo = {
        rId: '',
        state: '',
        remark: '',
        examineRemark: '',
        roomId: '',
        photos: [],
        videoName: '',
        detailType: '1001',
        isTrue: '',
        isTrues: []
      }
    }
  }
}
</script>