<template>
  <el-dialog :visible.sync="dialogVisible" :title="$t('ownerMembers.title')" width="80%" top="5vh" @close="handleClose">
    <el-row>
      <el-col :span="24">
        <div class="table-container">
          <el-table :data="ownerMembersInfo.members" border stripe>
            <el-table-column prop="name" :label="$t('ownerMembers.name')" align="center" />
            <el-table-column prop="sex" :label="$t('ownerMembers.sex')" align="center">
              <template #default="{ row }">
                {{ row.sex == 0 ? $t('ownerMembers.male') : $t('ownerMembers.female') }}
              </template>
            </el-table-column>
            <el-table-column prop="age" :label="$t('ownerMembers.age')" align="center" />
            <el-table-column prop="ownerTypeName" :label="$t('ownerMembers.type')" align="center" />
            <el-table-column prop="idCard" :label="$t('ownerMembers.idCard')" align="center" />
            <el-table-column prop="link" :label="$t('ownerMembers.contact')" align="center" />
            <el-table-column prop="address" :label="$t('ownerMembers.address')" align="center" />
          </el-table>
        </div>
        <el-pagination background layout="prev, pager, next, sizes, total" class="margin-top" :total="totalCount" :page-size="pageSize"
          :current-page="currentPage" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { queryOwnerMembers } from '@/api/room/ownerMembersApi'

export default {
  name: 'OwnerMembers',
  data() {
    return {
      dialogVisible: false,
      ownerMembersInfo: {
        members: [],
        ownerId: ''
      },
      totalCount: 0,
      pageSize: 10,
      currentPage: 1
    }
  },
  methods: {
    open(ownerId) {
      this.ownerMembersInfo.ownerId = ownerId
      this.dialogVisible = true
      this._loadOwnerMemberInfo(1, this.pageSize)
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSizeChange(size) {
      this.pageSize = size
      this._loadOwnerMemberInfo(this.currentPage, size)
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this._loadOwnerMemberInfo(page, this.pageSize)
    },
    async _loadOwnerMemberInfo(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: this.getCommunityId(),
          ownerId: this.ownerMembersInfo.ownerId
        }

        const response = await queryOwnerMembers(params)
        this.ownerMembersInfo.members = response.data
        this.totalCount = response.total
        this.currentPage = page
      } catch (error) {
        console.error('加载业主成员信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.table-container {
  margin-top: 15px;
}
</style>