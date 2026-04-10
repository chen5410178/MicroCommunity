<template>
  <div class="simplify-notepad-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('simplifyNotepadManage.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="goBack()">
            <i class="el-icon-close"></i>
            <span>{{ $t('simplifyNotepadManage.back') }}</span>
          </el-button>
          <el-button type="primary" size="small" @click="_openAddNotepadModal()">
            <i class="el-icon-plus"></i>
            <span>{{ $t('simplifyNotepadManage.register') }}</span>
          </el-button>
        </div>
      </div>

      <el-table :data="simplifyNotepadManageInfo.notepads" border style="width: 100%" v-loading="loading">
        <el-table-column prop="noteTypeName" :label="$t('simplifyNotepadManage.type')" align="center" />
        <el-table-column prop="roomName" :label="$t('simplifyNotepadManage.room')" align="center" />
        <el-table-column prop="objName" :label="$t('simplifyNotepadManage.contact')" align="center" />
        <el-table-column prop="link" :label="$t('simplifyNotepadManage.phone')" align="center" />
        <el-table-column prop="state" :label="$t('simplifyNotepadManage.status')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'F' ? $t('simplifyNotepadManage.completed') : $t('simplifyNotepadManage.following')
            }}
            <span v-if="scope.row.thridId">({{ $t('simplifyNotepadManage.transferred') }})</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('simplifyNotepadManage.recordTime')" align="center" />
        <el-table-column prop="createUserName" :label="$t('simplifyNotepadManage.registrant')" align="center" />
        <el-table-column prop="title" :label="$t('simplifyNotepadManage.content')" align="center" />
        <el-table-column :label="$t('simplifyNotepadManage.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-if="scope.row.state === 'W'" size="mini" @click="_openAddNotepadDetailModal(scope.row)">
                {{ $t('simplifyNotepadManage.follow') }}
              </el-button>
              <el-button size="mini" @click="_openListNotepadDetailModal(scope.row)">
                {{ $t('simplifyNotepadManage.progress') }}
              </el-button>
              <el-button v-if="!scope.row.thridId" size="mini" @click="_openAddRepairModal(scope.row)">
                {{ $t('simplifyNotepadManage.transferRepair') }}
              </el-button>
              <el-button v-if="scope.row.thridId" size="mini" @click="_toRepairDetail(scope.row)">
                {{ $t('simplifyNotepadManage.repairDetail') }}
              </el-button>
              <el-button size="mini" @click="_openEditNotepadModel(scope.row)">
                {{ $t('simplifyNotepadManage.edit') }}
              </el-button>
              <el-button size="mini" type="danger" @click="_openDeleteNotepadModel(scope.row)">
                {{ $t('simplifyNotepadManage.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top-xs">
        <el-col :span="18">
          <div>{{ $t('simplifyNotepadManage.tip') }}</div>
        </el-col>
        <el-col :span="6">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total" />
        </el-col>
      </el-row>
    </el-card>

    <add-notepad ref="addNotepad" @success="handleSuccess" />
    <edit-notepad ref="editNotepad" @success="handleSuccess" />
    <delete-notepad ref="deleteNotepad" @success="handleSuccess" />
    <add-notepad-detail ref="addNotepadDetail" @success="handleSuccess" />
    <notepad-detail ref="notepadDetail" />
    <notepad-owner-repair ref="notepadOwnerRepair" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
//import { getDict } from '@/api/community/communityApi'
import { listNotepad } from '@/api/oa/simplifyNotepadManageApi'
import AddNotepad from '@/components/oa/addNotepad'
import EditNotepad from '@/components/oa/editNotepad'
import DeleteNotepad from '@/components/oa/deleteNotepad'
import AddNotepadDetail from '@/components/oa/addNotepadDetail'
import NotepadDetail from '@/components/oa/notepadDetail'
import NotepadOwnerRepair from '@/components/oa/notepadOwnerRepair'

export default {
  name: 'SimplifyNotepadManageList',
  components: {
    AddNotepad,
    EditNotepad,
    DeleteNotepad,
    AddNotepadDetail,
    NotepadDetail,
    NotepadOwnerRepair
  },
  data() {
    return {
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      roomId: '',
      ownerId: '',
      simplifyNotepadManageInfo: {
        notepads: [],
        conditions: {
          noteType: '',
          title: '',
          objName: '',
          createUserName: '',
          state: '',
          objId: '',
          communityId: ''
        }
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    const {roomId,ownerId} = this.$route.query
    this.simplifyNotepadManageInfo.roomId = roomId;
    this.simplifyNotepadManageInfo.ownerId = ownerId;
    this.simplifyNotepadManageInfo.conditions.objId = ownerId;
    this.simplifyNotepadManageInfo.conditions.communityId = this.communityId
    this._listNotepads(this.currentPage, this.pageSize)
  },
  methods: {
    async _listNotepads(page, rows) {
      this.loading = true
      try {
        const params = {
          page,
          row: rows,
          ...this.simplifyNotepadManageInfo.conditions
        }
        const { data, total } = await listNotepad(params)
        this.simplifyNotepadManageInfo.notepads = data
        this.total = total
      } catch (error) {
        console.error('Failed to fetch notepads:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listNotepads(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listNotepads(this.currentPage, this.pageSize)
    },
    _openAddNotepadModal() {
      this.$refs.addNotepad.open({
        roomId: this.simplifyNotepadManageInfo.roomId,
        ownerId: this.simplifyNotepadManageInfo.ownerId
      })
    },
    _openAddNotepadDetailModal(notepad) {
      this.$refs.addNotepadDetail.open(notepad)
    },
    _openListNotepadDetailModal(notepad) {
      this.$refs.notepadDetail.open(notepad)
    },
    _openEditNotepadModel(notepad) {
      this.$refs.editNotepad.open(notepad)
    },
    _openDeleteNotepadModel(notepad) {
      this.$refs.deleteNotepad.open(notepad)
    },
    _openAddRepairModal(notepad) {
      this.$refs.notepadOwnerRepair.open(notepad)
    },
    _toRepairDetail(notepad) {
      this.$router.push(`/views/work/repairDetail?repairId=${notepad.thridId}`)
    },
    handleSuccess() {
      this._listNotepads(this.currentPage, this.pageSize)
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.simplify-notepad-manage-container {
  padding: 20px;
}

.header-tools {
  float: right;
}

.margin-top-xs {
  margin-top: 10px;
}
</style>