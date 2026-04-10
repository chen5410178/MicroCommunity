<template>
  <el-dialog
    :title="$t('notepadManage.addDetail.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('notepadManage.addDetail.state')"
        prop="state"
      >
        <el-select
          v-model="formData.state"
          style="width:100%"
        >
          <el-option
            :label="$t('notepadManage.addDetail.selectState')"
            disabled
            value=""
          />
          <el-option
            :label="$t('notepadManage.state.following')"
            value="W"
          />
          <el-option
            :label="$t('notepadManage.state.completed')"
            value="F"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.addDetail.content')"
        prop="content"
      >
        <el-input
          v-model="formData.content"
          type="textarea"
          rows="5"
          :placeholder="$t('notepadManage.addDetail.contentPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveNotepadDetail } from '@/api/oa/notepadManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddNotepadDetail',
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      formData: {
        noteId: '',
        content: '',
        state: 'W',
        communityId: ''
      },
      rules: {
        state: [
          { required: true, message: this.$t('notepadManage.validate.state'), trigger: 'blur' }
        ],
        content: [
          { required: true, message: this.$t('notepadManage.validate.content'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData.noteId = data.noteId
      this.formData.communityId = getCommunityId()
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveNotepadDetail(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('保存记事本详情失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        noteId: '',
        content: '',
        state: 'W',
        communityId: ''
      }
    }
  }
}
</script>