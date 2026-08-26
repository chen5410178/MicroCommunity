<template>
  <el-dialog :title="$t('searchOwner.title')" :visible.sync="visible" width="80%">

    <el-row :gutter="20">
      <el-col :span="8">
        <el-input v-model="searchOwnerInfo.roomName" :placeholder="$t('searchOwner.roomPlaceholder')"
          clearable></el-input>
      </el-col>
      <el-col :span="8">
        <el-input v-model="searchOwnerInfo._currentOwnerName" :placeholder="$t('searchOwner.ownerPlaceholder')"
          clearable></el-input>
      </el-col>
      <el-col :span="8">
        <el-button type="primary" @click="searchOwners">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
        <el-button type="primary" @click="resetOwners">
          {{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="table-responsive" style="margin-top:15px">
      <el-table :data="searchOwnerInfo.owners" border>
        <el-table-column prop="memberId" :label="$t('searchOwner.ownerId')" align="center"></el-table-column>
        <el-table-column prop="name" :label="$t('searchOwner.name')" align="center"></el-table-column>
        <el-table-column prop="personTypeName" :label="$t('searchOwner.personType')" align="center"></el-table-column>
        <el-table-column prop="personRoleName" :label="$t('searchOwner.personRole')" align="center"></el-table-column>
        <el-table-column prop="idCard" :label="$t('searchOwner.idCard')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('searchOwner.contact')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="100">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="chooseOwner(scope.row)">{{ $t('common.select') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total" style="margin-top: 20px;"></el-pagination>

  </el-dialog>
</template>

<script>
import { queryOwners } from '@/api/owner/ownerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SearchOwner',
  data() {
    return {
      visible: false,
      searchOwnerInfo: {
        owners: [],
        _currentOwnerName: '',
        roomName: '',
        ownerTypeCd: '1001'
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this._refreshSearchOwnerData()
      this._loadAllOwnerInfo(1, 10)
    },
    async _loadAllOwnerInfo(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: getCommunityId(),
          name: this.searchOwnerInfo._currentOwnerName.trim(),
          roomName: this.searchOwnerInfo.roomName.trim(),
          ownerTypeCd: this.searchOwnerInfo.ownerTypeCd
        }

        const res = await queryOwners(params)
        this.searchOwnerInfo.owners = res.data
        this.page.total = res.total
        this.page.current = page
        this.page.size = size
      } catch (error) {
        console.error('获取业主信息失败:', error)
      }
    },
    chooseOwner(owner) {
      this.$emit('chooseOwner', owner)
      this.visible = false
    },
    searchOwners() {
      this._loadAllOwnerInfo(1, this.page.size)
    },
    resetOwners() {
      this.searchOwnerInfo.roomName = ""
      this.searchOwnerInfo._currentOwnerName = ""
      this._loadAllOwnerInfo(1, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadAllOwnerInfo(1, val)
    },
    handleCurrentChange(val) {
      this._loadAllOwnerInfo(val, this.page.size)
    },
    _refreshSearchOwnerData() {
      this.searchOwnerInfo = {
        owners: [],
        _currentOwnerName: '',
        roomName: '',
        ownerTypeCd: '1001'
      }
      this.page = {
        current: 1,
        size: 10,
        total: 0
      }
    }
  }
}
</script>

<style scoped>
.table-responsive {
  overflow-x: auto;
}
</style>