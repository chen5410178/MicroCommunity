<template>
  <el-dialog :title="$t('addNotepad.title')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-form :model="addNotepadInfo" ref="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('addNotepad.room')" prop="roomName">
        <el-input v-model="addNotepadInfo.roomName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('addNotepad.contact')" prop="objName">
        <el-input v-model="addNotepadInfo.objName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('addNotepad.phone')" prop="link">
        <el-input v-model="addNotepadInfo.link" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('addNotepad.type')" prop="noteType">
        <el-select v-model="addNotepadInfo.noteType" style="width:100%" :placeholder="$t('addNotepad.typePlaceholder')">
          <el-option v-for="item in addNotepadInfo.noteTypes" :key="item.statusCd" :label="item.name"
            :value="item.statusCd" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('addNotepad.content')" prop="title">
        <el-input type="textarea" :rows="5" v-model="addNotepadInfo.title"
          :placeholder="$t('addNotepad.contentPlaceholder')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveNotepadInfo">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { saveNotepad } from '@/api/oa/simplifyNotepadManageApi'
import {queryRooms} from '@/api/room/roomApi'

export default {
  name: 'AddNotepad',
  data() {
    return {
      visible: false,
      addNotepadInfo: {
        noteId: '',
        noteType: '',
        title: '',
        roomName: '',
        roomId: '',
        objId: '',
        objName: '',
        objType: '3309',
        link: '',
        noteTypes: [],
        communityId: ''
      },
      rules: {
        roomName: [
          { required: true, message: this.$t('addNotepad.roomRequired'), trigger: 'blur' }
        ],
        objName: [
          { required: true, message: this.$t('addNotepad.contactRequired'), trigger: 'blur' }
        ],
        link: [
          { required: true, message: this.$t('addNotepad.phoneRequired'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('addNotepad.phoneFormat'), trigger: 'blur' }
        ],
        noteType: [
          { required: true, message: this.$t('addNotepad.typeRequired'), trigger: 'change' }
        ],
        title: [
          { required: true, message: this.$t('addNotepad.contentRequired'), trigger: 'blur' },
          { max: 256, message: this.$t('addNotepad.contentMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.visible = true
      this.addNotepadInfo = {
        ...this.addNotepadInfo,
        ...params,
        communityId: getCommunityId()
      }
      this.getNoteTypes()
      this.listNotepadRoom()
    },
    async getNoteTypes() {
      try {
        const data = await getDict('notepad', 'note_type')
        this.addNotepadInfo.noteTypes = data
      } catch (error) {
        console.error('Failed to get note types:', error)
      }
    },
    async listNotepadRoom() {
      const { rooms } = await queryRooms({
        communityId: this.addNotepadInfo.communityId,
        page:1,
        row:1,
        roomId:this.addNotepadInfo.roomId
      })
      Object.assign(this.addNotepadInfo,rooms[0])
      this.addNotepadInfo.objId = rooms[0].ownerId
      this.addNotepadInfo.objName = rooms[0].ownerName
    },
    async saveNotepadInfo() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          await saveNotepad(this.addNotepadInfo)
          this.$emit('success')
          this.visible = false
          this.$message.success(this.$t('common.operationSuccess'))
        } catch (error) {
          this.$message.error(error.message || this.$t('common.saveFailed'))
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>