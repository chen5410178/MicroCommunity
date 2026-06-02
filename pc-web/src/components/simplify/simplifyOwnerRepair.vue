<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div v-if="simplifyOwnerRepairInfo.roomId != ''">
        <el-button type="primary" size="small" @click="_openAddOwnerRepairModal">
          <i class="el-icon-plus"></i>{{ $t('simplifyOwnerRepair.phoneRepair') }}
        </el-button>
      </div>
    </div>

    <el-table
      :data="simplifyOwnerRepairInfo.repairs"
      style="width: 100%; margin-top: 10px"
      border>
      <el-table-column prop="repairId" :label="$t('simplifyOwnerRepair.workOrderCode')" align="center"></el-table-column>
      <el-table-column prop="repairObjName" :label="$t('simplifyOwnerRepair.location')" align="center"></el-table-column>
      <el-table-column prop="repairTypeName" :label="$t('simplifyOwnerRepair.repairType')" align="center"></el-table-column>
      <el-table-column prop="repairName" :label="$t('simplifyOwnerRepair.reporter')" align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('simplifyOwnerRepair.contact')" align="center"></el-table-column>
      <el-table-column prop="appointmentTime" :label="$t('simplifyOwnerRepair.appointmentTime')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyOwnerRepair.status')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerRepair.operation')" align="center">
        <template #default="{row}">
          <el-button type="text" @click="_openRepairDetail(row)">
            {{ $t('simplifyOwnerRepair.details') }}
          </el-button>
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
      style="margin-top: 20px">
    </el-pagination>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerRepairs } from '@/api/simplify/simplifyOwnerRepairApi'

export default {
  name: 'SimplifyOwnerRepair',
  data() {
    return {
      simplifyOwnerRepairInfo: {
        repairs: [],
        ownerId: '',
        roomId: '',
        total: 0,
        records: 1,
        roomName: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      if (param.roomId == '') return
      this.clearSimplifyOwnerRepairInfo()
      Object.assign(this.simplifyOwnerRepairInfo, param)
      this.listSimplifyOwnerRepair(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listSimplifyOwnerRepair(val, this.pageSize)
    },
    listSimplifyOwnerRepair(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        repairObjId: this.simplifyOwnerRepairInfo.roomId
      }
      
      listOwnerRepairs(params).then(res => {
        this.simplifyOwnerRepairInfo.repairs = res.data
        this.total = res.total
      })
    },
    _openRepairDetail(repairPool) {
      this.$router.push(`/views/work/repairDetail?repairId=${repairPool.repairId}`)
    },
    clearSimplifyOwnerRepairInfo() {
      this.simplifyOwnerRepairInfo = {
        repairs: [],
        ownerId: '',
        roomId: '',
        total: 0,
        records: 1,
        roomName: ''
      }
    },
    _openAddOwnerRepairModal() {
      this.$router.push(`/views/work/addRoomRepair?roomId=${this.simplifyOwnerRepairInfo.roomId}&roomName=${this.simplifyOwnerRepairInfo.roomName}`)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 10px;
}
.text-right {
  text-align: right;
}
</style>