<template>
  <div class="textarea-container">
    <div ref="editor" class="editor"></div>
  </div>
</template>

<script>
import { uploadImage } from '@/api/oa/editWorkApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'TextareaEditor',
  data() {
    return {
      content: ''
    }
  },
  mounted() {
    this._initEditor()
  },
  methods: {
    _initEditor() {
      $(this.$refs.editor).summernote({
        lang: 'zh-CN',
        height: 300,
        placeholder: this.$t('textarea.placeholder'),
        callbacks: {
          onImageUpload: (files) => {
            this._uploadImage(files[0])
          },
          onChange: (contents) => {
            this.content = contents.replace(/&nbsp;/g, '').replace(/\s/gi, '')
            this.$emit('change', this.content)
          }
        },
        toolbar: [
          ['style', ['style']],
          ['font', ['bold', 'italic', 'underline', 'clear']],
          ['fontname', ['fontname']],
          ['color', ['color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['height', ['height']],
          ['table', ['table']],
          ['insert', ['link', 'picture']]
        ]
      })
    },
    setText(content) {
      $(this.$refs.editor).summernote('code', content)
    },
    async _uploadImage(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      formData.append('communityId', getCommunityId())

      try {
        const res = await uploadImage(formData)
        $(this.$refs.editor).summernote('insertImage', res.url)
      } catch (error) {
        console.error('上传图片失败:', error)
        this.$message.error(this.$t('textarea.uploadFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.textarea-container {
  .editor {
    width: 100%;
  }
}
</style>