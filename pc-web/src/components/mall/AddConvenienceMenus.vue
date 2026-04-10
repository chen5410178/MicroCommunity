<template>
  <el-dialog :title="$t('convenienceMenus.addTitle')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('convenienceMenus.menuName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('convenienceMenus.menuNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('convenienceMenus.imageUrl')" prop="icon">
        <upload-image-url ref="uploadImage" @uploadCoverImage="handleImageChange" />
      </el-form-item>
      <el-form-item :label="$t('convenienceMenus.pagePath')" prop="url">
        <el-input v-model="form.url" :placeholder="$t('convenienceMenus.pagePathPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('convenienceMenus.displayOrder')" prop="seq">
        <el-input-number v-model="form.seq" :min="1" />
      </el-form-item>
      <el-form-item :label="$t('convenienceMenus.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addConvenienceMenus } from '@/api/mall/convenienceMenusManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'AddConvenienceMenus',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      visible: false,
      form: {
        name: '',
        icon: '',
        url: '',
        seq: 1,
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('convenienceMenus.menuNameRequired'), trigger: 'blur' },
          { max: 50, message: this.$t('convenienceMenus.menuNameMaxLength'), trigger: 'blur' }
        ],
        url: [
          { max: 100, message: this.$t('convenienceMenus.pagePathMaxLength'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('convenienceMenus.displayOrderRequired'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('convenienceMenus.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.$refs.uploadImage.clear()
    },
    handleImageChange(url) {
      this.form.icon = url
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await addConvenienceMenus(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>