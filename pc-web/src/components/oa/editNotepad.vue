<template>
  <el-dialog
    :title="$t('notepadManage.edit.title')"
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
        :label="$t('notepadManage.edit.roomName')"
        prop="roomName"
      >
        <el-input
          v-model="formData.roomName"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.edit.objName')"
        prop="objName"
      >
        <el-input
          v-model="formData.objName"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.edit.link')"
        prop="link"
      >
        <el-input
          v-model="formData.link"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.edit.noteType')"
        prop="noteType"
      >
        <el-select
          v-model="formData.noteType"
          style="width:100%"
        >
          <el-option
            :label="$t('notepadManage.edit.selectNoteType')"
            disabled
            value=""
          />
          <el-option
            v-for="(item,index) in noteTypes"
            :key="index"
            :label="item.name"
            :value="item.statusCd"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('notepadManage.edit.title')"
        prop="title"
      >
        <el-input
          v-model="formData.title"
          type="textarea"
          rows="5"
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
import { updateNotepad } from '@/api/oa/notepadManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditNotepad',
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
        noteType: '',
        title: '',
        roomName: '',
        roomId: '',
        objId: '',
        objName: '',
        objType: '',
        link: '',
        communityId: ''
      },
      noteTypes: [],
      rules: {
        noteType: [
          { required: true, message: this.$t('notepadManage.validate.noteType'), trigger: 'blur' }
        ],
        title: [
          { required: true, message: this.$t('notepadManage.validate.title'), trigger: 'blur' },
          { max: 256, message: this.$t('notepadManage.validate.titleMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData = { ...data }
      this.formData.communityId = getCommunityId()
      this.getNoteTypes()
    },
    async getNoteTypes() {
      try {
        const data = await getDict('notepad', 'note_type')
        this.noteTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateNotepad(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新记事本失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>