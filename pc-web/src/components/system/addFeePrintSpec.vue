<template>
  <el-dialog :title="$t('feePrintSpecManage.addTitle')" :visible.sync="visible" width="50%" @close="resetForm">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('feePrintSpecManage.spec')" prop="specCd">
        <el-select v-model="formData.specCd" style="width:100%" :placeholder="$t('feePrintSpecManage.specPlaceholder')">
          <el-option v-for="item in specOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('feePrintSpecManage.name')" prop="printName">
        <el-input v-model="formData.printName" :placeholder="$t('feePrintSpecManage.namePlaceholder')">
        </el-input>
      </el-form-item>

      <el-form-item :label="$t('feePrintSpecManage.image')" prop="qrImg">
        <upload-image-url ref="uploader" :image-count="1" @notifyUploadCoverImage="handleImageChange">
        </upload-image-url>
      </el-form-item>

      <el-form-item :label="$t('feePrintSpecManage.content')">
        <el-input type="textarea" :rows="5" v-model="formData.content"
          :placeholder="$t('feePrintSpecManage.contentPlaceholder')">
        </el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm" :loading="submitting">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveFeePrintSpec } from '@/api/system/feePrintSpecManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddFeePrintSpec',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      submitting: false,
      formData: {
        specCd: '',
        printName: '',
        qrImg: '',
        content: '',
        communityId: ''
      },
      rules: {
        specCd: [
          { required: true, message: this.$t('feePrintSpecManage.specRequired'), trigger: 'blur' }
        ],
        printName: [
          { required: true, message: this.$t('feePrintSpecManage.nameRequired'), trigger: 'blur' },
          { max: 128, message: this.$t('feePrintSpecManage.nameMaxLength'), trigger: 'blur' }
        ]
      },
      specOptions: [
        { value: '1010', label: this.$t('feePrintSpecManage.reminderPrint') },
        { value: '2020', label: this.$t('feePrintSpecManage.receiptPrint') }
      ]
    }
  },
  methods: {
    open() {
      this.visible = true
      this.formData.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.uploader.clear()
      })
    },
    handleImageChange(images) {
      if (images && images.length > 0) {
        this.formData.qrImg = images[0]
      } else {
        this.formData.qrImg = ''
      }
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        this.submitting = true
        try {
          await saveFeePrintSpec(this.formData)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('feePrintSpecManage.addFailed'))
        } finally {
          this.submitting = false
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        specCd: '',
        printName: '',
        qrImg: '',
        content: '',
        communityId: ''
      }
    }
  }
}
</script>