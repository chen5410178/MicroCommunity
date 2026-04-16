<!-- src/components/upload/FileUpload.vue -->
<template>
  <div>
    <el-upload :action="uploadUrl" :headers="headers" :on-success="handleSuccess" :on-error="handleError"
      :show-file-list="false" :before-upload="beforeUpload" name="uploadFile">
      <el-button size="small" type="primary">{{ $t('common.upload') }}</el-button>
      <span v-if="value" style="margin-left: 10px">
        {{ fileName }}
        <el-button type="text" @click.stop="downloadFile">
          <i class="el-icon-download"></i>
        </el-button>
        <el-button type="text" @click.stop="removeFile">
          <i class="el-icon-delete"></i>
        </el-button>
      </span>
    </el-upload>
  </div>
</template>

<script>
import { getHeader } from '@/utils/header'
export default {
  name: 'FileUpload',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      uploadUrl: '/upload/uploadFile'
    }
  },
  computed: {
    fileName() {
      return this.value ? this.value.split('/').pop() : ''
    },
    headers() {
      return getHeader()
    }
  },
  methods: {
    handleSuccess(response, file) {
      console.log('Upload file:', file)
      if (response.code == 0) {
        const fileUrl = response.url || response.path
        this.$emit('input', fileUrl)
        this.$emit('success', fileUrl)
        this.$message.success(this.$t('common.operationSuccess'))
      } else {
        this.$message.error(response.msg || this.$t('upload.error'))
      }
    },
    handleError(err) {
      console.error('Upload error:', err)
      this.$message.error(this.$t('upload.error'))
    },
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error(this.$t('upload.sizeLimit'))
      }
      return isLt10M
    },
    removeFile() {
      this.$confirm(this.$t('common.confirmDelete'), this.$t('common.tip'), {
        type: 'warning'
      }).then(() => {
        this.$emit('input', '')
        this.$emit('remove')
      }).catch(() => { })
    },
    downloadFile() {
      window.open(this.value, '_blank')
    }
  }
}
</script>