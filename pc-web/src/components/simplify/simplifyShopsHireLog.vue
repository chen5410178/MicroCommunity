<template>
  <div>
    <el-table :data="simplifyShopsHireLogInfo.owners" style="margin-top:10px" border>
      <el-table-column prop="name" :label="$t('simplifyShopsHireLog.ownerName')" align="center"></el-table-column>
      <el-table-column prop="link" :label="$t('simplifyShopsHireLog.ownerPhone')" align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('simplifyShopsHireLog.startTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('simplifyShopsHireLog.endTime')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('simplifyShopsHireLog.createTime')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyShopsHireLog.operation')" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="_openShopsOwnerFee(scope.row)">{{$t('simplifyShopsHireLog.viewFee')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      class="text-right"
      style="margin-top: 20px"
    ></el-pagination>
  </div>
</template>

<script>
import { queryShopsHireLog } from '@/api/simplify/simplifyShopsHireLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SimplifyShopsHireLog',
  data() {
    return {
      simplifyShopsHireLogInfo: {
        owners: [],
        ownerId: '',
        roomId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(params) {
      if (!params.roomId) return
      this.clearSimplifyShopsHireLogInfo()
      Object.assign(this.simplifyShopsHireLogInfo, params)
      this.listShopsHireLog()
    },
    async listShopsHireLog() {
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          communityId: this.communityId,
          roomId: this.simplifyShopsHireLogInfo.roomId
        }
        const res = await queryShopsHireLog(params)
        this.simplifyShopsHireLogInfo.owners = res.data
        this.total = res.data.total
      } catch (error) {
        console.error('Failed to load shops hire log:', error)
      }
    },
    _openShopsOwnerFee(owner) {
      this.$router.push({
        path: '/views/owner/ownerDetail',
        query: {
          roomId: owner.roomId,
          ownerId: owner.ownerId,
          hireOwnerFee: 1
        }
      })
    },
    clearSimplifyShopsHireLogInfo() {
      this.simplifyShopsHireLogInfo = {
        owners: [],
        ownerId: '',
        roomId: ''
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listShopsHireLog()
    }
  }
}
</script>

<style scoped>
.text-right {
  text-align: right;
}
</style>