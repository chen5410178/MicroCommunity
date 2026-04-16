<template>
  <el-dialog :title="$t('common.edit')" :visible.sync="visible" width="800px" @close="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('enterCommunity.communityName')" prop="name">
        <el-input v-model="form.name" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.communityAddress')" prop="address">
        <el-input v-model="form.address" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.communityLandmark')" prop="nearbyLandmarks">
        <el-input v-model="form.nearbyLandmarks" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.cityCode')" prop="cityCode">
        <el-input v-model="form.cityCode" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.xCoordinate')" prop="mapX">
        <el-input v-model="form.mapX" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.yCoordinate')" prop="mapY">
        <el-input v-model="form.mapY" disabled />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.servicePhone')" prop="tel">
        <el-input v-model="form.tel" :placeholder="$t('enterCommunity.requiredPhone')" />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.serviceQR')" prop="qrCode">
        <upload-image-url ref="uploadImage" :limit="1" @notifyUploadCoverImage="handleQRUpdate" />
      </el-form-item>
      <el-form-item :label="$t('enterCommunity.communityArea')" prop="communityArea">
        <el-input v-model="form.communityArea" :placeholder="$t('enterCommunity.requiredArea')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('enterCommunity.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('enterCommunity.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateCommunity } from '@/api/community/enterCommunityApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl.vue'

export default {
  name: 'EditCommunityArea',
  components: { UploadImageUrl },
  data() {
    return {
      visible: false,
      form: {
        communityId: '',
        name: '',
        address: '',
        nearbyLandmarks: '',
        cityCode: '',
        mapX: '101.33',
        mapY: '101.33',
        tel: '',
        communityArea: '',
        qrCode: ''
      },
      rules: {
        tel: [
          { required: true, message: this.$t('enterCommunity.requiredPhone'), trigger: 'blur' }
        ],
        communityArea: [
          { required: true, message: this.$t('enterCommunity.requiredArea'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('enterCommunity.areaFormatError'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(community) {
      Object.keys(this.form).forEach(key => {
        if (community[key] !== undefined) {
          this.form[key] = community[key]
        }
      })
      this.visible = true
      setTimeout(() => {
        this.$refs.uploadImage.setImages([community.qrCode])
      }, 500)
    },
    handleQRUpdate(photosUrl) {
      this.form.qrCode = photosUrl.length > 0 ? photosUrl[0] : ''
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.updateCommunity()
        }
      })
    },
    async updateCommunity() {
      try {
        await updateCommunity(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('enterCommunity.updateError'))
      }
    },
    resetForm() {
      this.$refs.form.resetFields()
      Object.keys(this.form).forEach(key => {
        this.form[key] = key === 'mapX' || key === 'mapY' ? '101.33' : ''
      })
    }
  }
}
</script>