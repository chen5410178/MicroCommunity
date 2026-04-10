<template>
  <el-dialog
    :title="$t('notepadManage.detail.title')"
    :visible.sync="visible"
    width="70%"
  >
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        type="index"
        :label="$t('notepadManage.detail.index')"
        width="80"
        align="center"
      />
      <el-table-column
        prop="createUserName"
        :label="$t('notepadManage.detail.createUser')"
        align="center"
      />
      <el-table-column
        prop="createTime"
        :label="$t('notepadManage.detail.createTime')"
        align="center"
      />
      <el-table-column
        prop="content"
        :label="$t('notepadManage.detail.content')"
        align="center"
      />
      <el-table-column
        :label="$t('common.operation')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import { listNotepadDetail, deleteNotepadDetail } from '@/api/oa/notepadManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NotepadDetail',
  data() {
    return {
      visible: false,
      tableData: [],
      formData: {
        noteId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData.noteId = data.noteId
      this.formData.communityId = getCommunityId()
      this.loadDetails()
    },
    async loadDetails() {
      try {
        const params = {
          noteId: this.formData.noteId,
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const { data } = await listNotepadDetail(params)
        this.tableData = data
      } catch (error) {
        console.error('获取记事本详情失败:', error)
      }
    },
    async handleDelete(row) {
      try {
        await deleteNotepadDetail({
          detailId: row.detailId,
          communityId: this.formData.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.loadDetails()
      } catch (error) {
        console.error('删除记事本详情失败:', error)
      }
    }
  }
}
</script>