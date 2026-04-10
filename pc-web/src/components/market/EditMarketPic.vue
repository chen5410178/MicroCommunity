<template>
  <el-dialog :title="$t('marketPic.edit')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketPic.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('marketPic.name')" />
      </el-form-item>
      <el-form-item :label="$t('marketPic.image')" prop="picUrl">
        <upload-image-url ref="uploadImage" :image-count="1"  @notifyUploadCoverImage="handleImageChange" />
      </el-form-item>
      <el-form-item :label="$t('marketPic.url')" prop="picLink">
        <el-input v-model="form.picLink" :placeholder="$t('marketPic.url')" />
      </el-form-item>
      <el-form-item :label="$t('marketPic.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="4" :placeholder="$t('marketPic.remark')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMarketPic } from '@/api/market/marketWayApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'EditMarketPic',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        picId: '',
        name: '',
        picUrl: '',
        picLink: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('marketPic.name') + this.$t('common.required'), trigger: 'blur' },
          { max: 64, message: this.$t('marketPic.name') + this.$t('common.maxLength64'), trigger: 'blur' }
        ],
        picUrl: [
          { required: true, message: this.$t('marketPic.image') + this.$t('common.required'), trigger: 'change' }
        ],
        picLink: [
          { required: true, message: this.$t('marketPic.url') + this.$t('common.required'), trigger: 'blur' },
          { max: 512, message: this.$t('marketPic.url') + this.$t('common.maxLength512'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('marketPic.remark') + this.$t('common.required'), trigger: 'blur' },
          { max: 512, message: this.$t('marketPic.remark') + this.$t('common.maxLength512'), trigger: 'blur' }
        ],
        picId: [
          { required: true, message: 'ID' + this.$t('common.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      setTimeout(() => {
        this.$refs.uploadImage.setImages([row.picUrl])
      },500)

      this.visible = true
    },
    handleImageChange(urls) {
      this.form.picUrl = urls[0] || ''
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMarketPic(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.$refs.uploadImage.clear()
      this.form = {
        picId: '',
        name: '',
        picUrl: '',
        picLink: '',
        remark: ''
      }
    }
  }
}
</script>