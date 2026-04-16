<template>
  <el-dialog :title="$t('uploadInvoicePhoto.title')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form label-width="120px">
      <el-form-item :label="$t('uploadInvoicePhoto.invoiceCode')">
        <el-input v-model="form.invoiceCode" :placeholder="$t('uploadInvoicePhoto.codeRequired')" />
      </el-form-item>
      <el-form-item :label="$t('uploadInvoicePhoto.invoice')">
        <upload-image-url ref="uploadImage" @notifyUploadCoverImage="handleImageUpload" />
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveUploadInvoicePhoto">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { uploadInvoicePhoto } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'UploadInvoicePhoto',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        applyId: '',
        invoiceCode: '',
        photos: []
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        applyId: row.applyId,
        invoiceCode: '',
        photos: []
      }
      if (this.$refs.uploadImage) {
        this.$refs.uploadImage.clearImages()
      }
      this.visible = true
    },
    closeDialog() {
      this.visible = false
    },
    handleImageUpload(photos) {
      this.form.photos = photos
    },
    async saveUploadInvoicePhoto() {
      if (!this.form.invoiceCode) {
        this.$message.warning(this.$t('uploadInvoicePhoto.codeRequired'))
        return
      }
      if (this.form.photos.length === 0) {
        this.$message.warning(this.$t('uploadInvoicePhoto.photoRequired'))
        return
      }

      try {
        const params = {
          ...this.form,
          communityId: getCommunityId()
        }
        await uploadInvoicePhoto(params)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.closeDialog()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>