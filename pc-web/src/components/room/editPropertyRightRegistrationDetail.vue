<template>
  <el-dialog :title="$t('propertyRightDetail.edit.title')" :visible.sync="visible" width="70%"
    :before-close="handleClose">
    <el-form ref="form" :model="editPropertyRightRegistrationDetailInfo" label-width="120px">
      <el-form-item :label="$t('propertyRightDetail.edit.materialType')">
        <el-input v-model="editPropertyRightRegistrationDetailInfo.securitiesName"
          :placeholder="$t('propertyRightDetail.edit.materialTypePlaceholder')" disabled />
      </el-form-item>

      <template v-if="editPropertyRightRegistrationDetailInfo.securities == '001'">
        <el-form-item :label="$t('propertyRightDetail.edit.idCardPhoto')">
          <upload-image-url ref="idCardUpload" :image-count="2" @notifyUploadCoverImage="handleIdCardImageChange" />
          <p class="help-block">*{{ $t('propertyRightDetail.edit.idCardPhotoTip') }}*</p>
        </el-form-item>
      </template>

      <template v-if="editPropertyRightRegistrationDetailInfo.securities == '002'">
        <el-form-item :label="$t('propertyRightDetail.edit.houseContract')">
          <upload-image-url ref="housePurchaseUpload" :image-count="10"
            @notifyUploadCoverImage="handleHousePurchaseImageChange" />
          <p class="help-block">*{{ $t('propertyRightDetail.edit.houseContractTip') }}*</p>
        </el-form-item>
      </template>

      <template v-if="editPropertyRightRegistrationDetailInfo.securities == '003'">
        <el-form-item :label="$t('propertyRightDetail.edit.repairFund')">
          <el-select v-model="editPropertyRightRegistrationDetailInfo.isTrue" style="width: 100%"
            :placeholder="$t('propertyRightDetail.edit.repairFundPlaceholder')">
            <el-option value="" disabled :label="$t('propertyRightDetail.edit.repairFundPlaceholder')" />
            <el-option :label="$t('common.yes')" value="true" />
            <el-option :label="$t('common.no')" value="false" />
          </el-select>
        </el-form-item>

        <template v-if="editPropertyRightRegistrationDetailInfo.isTrue == 'true'">
          <el-form-item :label="$t('propertyRightDetail.edit.repairFundPhoto')">
            <upload-image-url ref="repairUpload" :image-count="3" @notifyUploadCoverImage="handleRepairImageChange" />
          </el-form-item>
        </template>
      </template>

      <template v-if="editPropertyRightRegistrationDetailInfo.securities == '004'">
        <el-form-item :label="$t('propertyRightDetail.edit.deedTax')">
          <el-select v-model="editPropertyRightRegistrationDetailInfo.isTrue" style="width: 100%"
            :placeholder="$t('propertyRightDetail.edit.deedTaxPlaceholder')">
            <el-option value="" disabled :label="$t('propertyRightDetail.edit.deedTaxPlaceholder')" />
            <el-option :label="$t('common.yes')" value="true" />
            <el-option :label="$t('common.no')" value="false" />
          </el-select>
        </el-form-item>

        <template v-if="editPropertyRightRegistrationDetailInfo.isTrue == 'true'">
          <el-form-item :label="$t('propertyRightDetail.edit.deedTaxPhoto')">
            <upload-image-url ref="deedTaxUpload" :image-count="3" @notifyUploadCoverImage="handleDeedTaxImageChange" />
          </el-form-item>
        </template>
      </template>

      <el-form-item class="dialog-footer">
        <el-button type="primary" @click="editPropertyRightRegistrationDetail">
          <i class="el-icon-check"></i>
          {{ $t('common.save') }}
        </el-button>
        <el-button @click="handleClose">
          <i class="el-icon-close"></i>
          {{ $t('common.cancel') }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { updatePropertyRightRegistrationDetail } from '@/api/room/listPropertyRightRegistrationDetailApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditPropertyRightRegistrationDetail',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      editPropertyRightRegistrationDetailInfo: {
        prrdId: '',
        prrId: '',
        securities: '',
        securitiesName: '',
        idCardUrl: '',
        housePurchaseUrl: '',
        repairUrl: '',
        deedTaxUrl: '',
        isTrue: '',
        idCardPhotos: [],
        housePurchasePhotos: [],
        repairPhotos: [],
        deedTaxPhotos: [],
        communityId: '',

      }
    }
  },
  methods: {
    open(row) {
      this.resetForm()
      this.editPropertyRightRegistrationDetailInfo = { ...row }
      this.editPropertyRightRegistrationDetailInfo.communityId = getCommunityId()
      this.loadPhotos()
      this.visible = true
    },
    loadPhotos() {
      setTimeout(() => {
        if (this.editPropertyRightRegistrationDetailInfo.securities == '001' && this.editPropertyRightRegistrationDetailInfo.idCardUrl) {
          const urls = this.editPropertyRightRegistrationDetailInfo.idCardUrl
          if (urls) {
            this.$refs.idCardUpload.setImages(urls)
          }
        }
        if (this.editPropertyRightRegistrationDetailInfo.securities == '002' && this.editPropertyRightRegistrationDetailInfo.housePurchaseUrl) {
          const urls = this.editPropertyRightRegistrationDetailInfo.housePurchaseUrl
          if (urls) {
            this.$refs.housePurchaseUpload.setImages(urls)
          }
        }
        if (this.editPropertyRightRegistrationDetailInfo.securities == '003' && this.editPropertyRightRegistrationDetailInfo.repairUrl) {
          const urls = this.editPropertyRightRegistrationDetailInfo.repairUrl
          if (urls) {
            this.$refs.repairUpload.setImages(urls)
          }
        }
        if (this.editPropertyRightRegistrationDetailInfo.securities == '004' && this.editPropertyRightRegistrationDetailInfo.deedTaxUrl) {
          const urls = this.editPropertyRightRegistrationDetailInfo.deedTaxUrl
          if (urls) {
            this.$refs.deedTaxUpload.setImages(urls)
          }
        }
      }, 1000)
    },
    handleIdCardImageChange(photos) {
      this.editPropertyRightRegistrationDetailInfo.idCardPhotos = photos
    },
    handleHousePurchaseImageChange(photos) {
      this.editPropertyRightRegistrationDetailInfo.housePurchasePhotos = photos
    },
    handleRepairImageChange(photos) {
      this.editPropertyRightRegistrationDetailInfo.repairPhotos = photos
    },
    handleDeedTaxImageChange(photos) {
      this.editPropertyRightRegistrationDetailInfo.deedTaxPhotos = photos
    },
    editPropertyRightRegistrationDetail() {
      if (!this.validateForm()) {
        return
      }

      const params = { ...this.editPropertyRightRegistrationDetailInfo }
      if (params.securities == '001') {
        params.idCardUrl = params.idCardPhotos && params.idCardPhotos.length > 0 ? params.idCardPhotos.join(',') : ''
      } else if (params.securities == '002') {
        params.housePurchaseUrl = params.housePurchasePhotos && params.housePurchasePhotos.length > 0 ? params.housePurchasePhotos.join(',') : ''
      } else if (params.securities == '003') {
        params.repairUrl = params.repairPhotos && params.repairPhotos.length > 0 ? params.repairPhotos.join(',') : ''
      } else if (params.securities == '004') {
        params.deedTaxUrl = params.deedTaxPhotos && params.deedTaxPhotos.length > 0 ? params.deedTaxPhotos.join(',') : ''
      }

      updatePropertyRightRegistrationDetail(params)
        .then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        })
        .catch(error => {
          this.$message.error(error.message || this.$t('common.updateFailed'))
        })
    },
    validateForm() {
      if (!this.editPropertyRightRegistrationDetailInfo.securities) {
        this.$message.error(this.$t('propertyRightDetail.edit.materialTypeRequired'))
        return false
      }
      return true
    },
    handleClose() {
      this.visible = false
    },
    resetForm() {
      this.editPropertyRightRegistrationDetailInfo = {
        prrdId: '',
        prrId: '',
        securities: '',
        securitiesName: '',
        idCardUrl: '',
        housePurchaseUrl: '',
        repairUrl: '',
        deedTaxUrl: '',
        isTrue: '',
        idCardPhotos: [],
        housePurchasePhotos: [],
        repairPhotos: [],
        deedTaxPhotos: [],
        communityId: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}

.help-block {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}
</style>