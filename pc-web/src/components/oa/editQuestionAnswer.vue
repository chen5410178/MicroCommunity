<template>
  <el-dialog
    :title="$t('questionAnswerManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('questionAnswerManage.edit.qaType')"
        prop="qaType"
      >
        <el-select
          v-model="formData.qaType"
          :placeholder="$t('questionAnswerManage.edit.qaTypePlaceholder')"
          style="width: 100%"
        >
          <el-option
            value="1001"
            :label="$t('questionAnswerManage.edit.qaType1')"
          />
          <el-option
            value="3003"
            :label="$t('questionAnswerManage.edit.qaType3')"
          />
          <el-option
            value="4004"
            :label="$t('questionAnswerManage.edit.qaType4')"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('questionAnswerManage.edit.qaName')"
        prop="qaName"
      >
        <el-input
          v-model="formData.qaName"
          :placeholder="$t('questionAnswerManage.edit.qaNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('questionAnswerManage.edit.startTime')"
        prop="startTime"
      >
        <el-date-picker
          v-model="formData.startTime"
          type="datetime"
          :placeholder="$t('questionAnswerManage.edit.startTimePlaceholder')"
          style="width: 100%"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>

      <el-form-item
        :label="$t('questionAnswerManage.edit.endTime')"
        prop="endTime"
      >
        <el-date-picker
          v-model="formData.endTime"
          type="datetime"
          :placeholder="$t('questionAnswerManage.edit.endTimePlaceholder')"
          style="width: 100%"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>

      <el-form-item :label="$t('questionAnswerManage.edit.content')">
        <el-input
          v-model="formData.content"
          type="textarea"
          :placeholder="$t('questionAnswerManage.edit.contentPlaceholder')"
          :rows="3"
        />
      </el-form-item>

      <el-form-item :label="$t('questionAnswerManage.edit.remark')">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :placeholder="$t('questionAnswerManage.edit.remarkPlaceholder')"
          :rows="3"
        />
      </el-form-item>

      <el-form-item :label="$t('questionAnswerManage.edit.photos')">
        <upload-image-url
          ref="uploadImage"
          :image-count="99"
          @change="handleImageChange"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateQuestionAnswer } from '@/api/oa/questionAnswerManageApi'
import UploadImageUrl from '@/components/oa/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditQuestionAnswer',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      formData: {
        qaId: '',
        qaType: '',
        qaName: '',
        startTime: '',
        endTime: '',
        content: '',
        remark: '',
        photos: []
      },
      rules: {
        qaType: [
          { required: true, message: this.$t('questionAnswerManage.validate.qaType'), trigger: 'blur' }
        ],
        qaName: [
          { required: true, message: this.$t('questionAnswerManage.validate.qaName'), trigger: 'blur' },
          { max: 256, message: this.$t('questionAnswerManage.validate.qaNameMax'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('questionAnswerManage.validate.startTime'), trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: this.$t('questionAnswerManage.validate.endTime'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.$nextTick(() => {
        this.resetForm()
        if (data) {
          Object.assign(this.formData, data)
          if (data.fileUrls) {
            this.$refs.uploadImage.setPhotos(data.fileUrls)
          }
        }
      })
    },
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
      this.$refs.uploadImage && this.$refs.uploadImage.clear()
      this.formData = {
        qaId: '',
        qaType: '',
        qaName: '',
        startTime: '',
        endTime: '',
        content: '',
        remark: '',
        photos: []
      }
    },
    handleClose() {
      this.resetForm()
    },
    handleImageChange(photos) {
      this.formData.photos = photos.map(item => item.fileId)
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          this.formData.communityId = getCommunityId()
          await updateQuestionAnswer(this.formData)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('questionAnswerManage.message.updateError'))
        }
      })
    }
  }
}
</script>