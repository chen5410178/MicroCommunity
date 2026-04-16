<template>
  <div class="notepad-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('notepadManage.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition">
          {{ notepadManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="notepadManageInfo.conditions.noteType" :placeholder="$t('notepadManage.search.noteType')"
            style="width:100%">
            <el-option :label="$t('common.select')" value="" />
            <el-option v-for="(item, index) in notepadManageInfo.noteTypes" :key="index" :label="item.name"
              :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="notepadManageInfo.conditions.roomName" :placeholder="$t('notepadManage.search.roomName')" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="notepadManageInfo.conditions.state" :placeholder="$t('notepadManage.search.state')"
            style="width:100%">
            <el-option :label="$t('common.select')" value="" />
            <el-option :label="$t('notepadManage.state.following')" value="W" />
            <el-option :label="$t('notepadManage.state.completed')" value="F" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="_queryNotepadMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetNotepadMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row v-show="notepadManageInfo.moreCondition" :gutter="20" style="margin-top:20px">
        <el-col :span="6">
          <el-input v-model="notepadManageInfo.conditions.createUserName"
            :placeholder="$t('notepadManage.search.createUserName')" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="notepadManageInfo.conditions.objName" :placeholder="$t('notepadManage.search.objName')" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('notepadManage.list.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="notepadManageInfo.notepads" border style="width: 100%">
        <el-table-column prop="noteTypeName" :label="$t('notepadManage.table.noteType')" align="center" />
        <el-table-column prop="roomName" :label="$t('notepadManage.table.roomName')" align="center" />
        <el-table-column prop="objName" :label="$t('notepadManage.table.objName')" align="center" />
        <el-table-column prop="link" :label="$t('notepadManage.table.link')" align="center" />
        <el-table-column :label="$t('notepadManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'F' ? $t('notepadManage.state.completed') : $t('notepadManage.state.following') }}
            <span v-if="scope.row.thridId">({{ $t('notepadManage.state.transferred') }})</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('notepadManage.table.createTime')" align="center" />
        <el-table-column prop="createUserName" :label="$t('notepadManage.table.createUserName')" align="center" />
        <el-table-column prop="title" :label="$t('notepadManage.table.title')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 'W'" size="mini" @click="_openAddNotepadDetailModal(scope.row)">
              {{ $t('notepadManage.operation.follow') }}
            </el-button>
            <el-button size="mini" @click="_openListNotepadDetailModal(scope.row)">
              {{ $t('notepadManage.operation.progress') }}
            </el-button>
            <el-button v-if="!scope.row.thridId" size="mini" @click="_openAddRepairModal(scope.row)">
              {{ $t('notepadManage.operation.transfer') }}
            </el-button>
            <el-button v-if="scope.row.thridId" size="mini" @click="_toRepairDetail(scope.row)">
              {{ $t('notepadManage.operation.repairDetail') }}
            </el-button>
            <el-button size="mini" @click="_openEditNotepadModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteNotepadModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="margin-top-xs">
        <div class="tip">{{ $t('notepadManage.tip') }}</div>
        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 组件 -->
    <edit-notepad ref="editNotepad" @success="handleSuccess" />
    <delete-notepad ref="deleteNotepad" @success="handleSuccess" />
    <add-notepad-detail ref="addNotepadDetail" @success="handleSuccess" />
    <notepad-detail ref="notepadDetail" />
    <notepad-owner-repair ref="notepadOwnerRepair" @success="handleSuccess" />
  </div>
</template>

<script>
import { listNotepad } from '@/api/oa/notepadManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import EditNotepad from '@/components/oa/editNotepad'
import DeleteNotepad from '@/components/oa/deleteNotepad'
import AddNotepadDetail from '@/components/oa/addNotepadDetail'
import NotepadDetail from '@/components/oa/notepadDetail'
import NotepadOwnerRepair from '@/components/oa/notepadOwnerRepair'

export default {
  name: 'NotepadManageList',
  components: {
    EditNotepad,
    DeleteNotepad,
    AddNotepadDetail,
    NotepadDetail,
    NotepadOwnerRepair
  },
  data() {
    return {
      loading: false,
      notepadManageInfo: {
        notepads: [],
        total: 0,
        records: 1,
        moreCondition: false,
        noteTypes: [],
        conditions: {
          noteType: '',
          roomName: '',
          objName: '',
          createUserName: '',
          state: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.notepadManageInfo.conditions.communityId = getCommunityId()
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      try {
        const data = await getDict('notepad', 'note_type')
        this.notepadManageInfo.noteTypes = data
        this._listNotepads(this.page.current, this.page.size)
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listNotepads(page, size) {
      this.loading = true
      try {
        const params = {
          page: page,
          row: size,
          ...this.notepadManageInfo.conditions
        }
        const { data, total } = await listNotepad(params)
        this.notepadManageInfo.notepads = data
        this.page.total = total
      } catch (error) {
        console.error('获取记事本列表失败:', error)
      } finally {
        this.loading = false
      }
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
    _queryNotepadMethod() {
      this.page.current = 1
      this._listNotepads(this.page.current, this.page.size)
    },
    _resetNotepadMethod() {
      this.notepadManageInfo.conditions = {
        noteType: '',
        roomName: '',
        state: '',
        createUserName: '',
        objName: '',
        communityId: this.notepadManageInfo.conditions.communityId
      }
      this._listNotepads(this.page.current, this.page.size)
    },
    _openAddRepairModal(notepad) {
      this.$refs.notepadOwnerRepair.open(notepad)
    },
    _toRepairDetail(notepad) {
      this.$router.push(`/views/work/repairDetail?repairId=${notepad.thridId}`)
    },
    _moreCondition() {
      this.notepadManageInfo.moreCondition = !this.notepadManageInfo.moreCondition
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listNotepads(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listNotepads(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listNotepads(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.notepad-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .margin-top-xs {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .tip {
      color: #999;
      font-size: 14px;
    }
  }
}
</style>