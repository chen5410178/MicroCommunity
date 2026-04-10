<!-- src/components/RichTextEditor.vue -->
<template>
  <div ref="editor" class="rich-text-editor" style="text-align: left;"></div>
</template>

<script>
import E from 'wangeditor'
import { getHeader } from '@/utils/header'

export default {
  name: 'RichTextEditor',
  props: {
    // Initial content for the editor
    content: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      editor: null
    }
  },
  mounted() {
    this.initEditor()
  },
  beforeDestroy() {
    // Destroy the editor to prevent memory leaks
    if (this.editor) {
      this.editor.destroy()
      this.editor = null
    }
  },
  methods: {
    initEditor() {
      this.editor = new E(this.$refs.editor)
      this.editor.config.zIndex = 100

      // Configure editor menus
      this.editor.config.menus = [
        'head',
        'bold',
        'italic',
        'underline',
        'strikeThrough',
        'foreColor',
        'backColor',
        'link',
        'list',
        'justify',
        'quote',
        'image',
        'table',
        'code',
        'undo',
        'redo'
      ]

      // Image upload configuration
      this.editor.config.uploadImgServer = '/callComponent/upload/uploadFile/uploadImage'
      let headers = getHeader()
      this.editor.config.uploadImgHeaders = headers
      this.editor.config.uploadFileName = 'uploadFile'
      this.editor.config.uploadImgMaxSize = 10 * 1024 * 1024 // 10M

      this.editor.config.customUploadImg = (resultFiles, insertImgFn) => {
        // 创建 FormData
        const formData = new FormData()
        formData.append('uploadFile', resultFiles[0])
        formData.append('communityId', '-1') // 添加 communityId 参数

        // 发送请求
        fetch('/callComponent/upload/uploadFile/uploadImage', {
          method: 'POST',
          headers: headers,
          body: formData
        })
          .then(response => response.json())
          .then(result => {
            console.log(result)
              const url = result.url 
              insertImgFn(url)
        
          })
          .catch(err => {
            this.$message.error(this.$t('upload.error'))
            console.error('Image upload error:', err)
          })
      }

      // Custom image upload callback
      // this.editor.config.uploadImgHooks = {
      //   customInsert: (insertImgFn, result) => {
      //     if (result.code === 0) {
      //       const url = result.data.url || result.data.path
      //       insertImgFn(url)
      //     } else {
      //       this.$message.error(result.msg || this.$t('upload.error'))
      //     }
      //   },
      //   error: (err) => {
      //     this.$message.error(this.$t('upload.error'))
      //     console.error('Image upload error:', err)
      //   }
      // }

      // Create the editor
      this.editor.create()

      // Initialize content
      if (this.content) {
        this.editor.txt.html(this.content)
      }

      // Emit content changes
      this.editor.config.onchange = (html) => {
        this.$emit('input', html)
        console.log('Editor content changed:', html)
        //this.$emit('update:content', html)
      }
    },
    // Method to clear the editor content
    clear() {
      if (this.editor) {
        this.editor.txt.clear()
      }
    },
    setContent(html) {
      if (this.editor) {
        this.editor.txt.html(html)
      }
    },
    // Method to get the current HTML content
    getContent() {
      return this.editor ? this.editor.txt.html() : ''
    }
  }
}
</script>

<style lang="scss" scoped>
.rich-text-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;

  &:hover {
    border-color: #c0c4cc;
  }

  &:focus {
    border-color: #409eff;
  }
}
</style>