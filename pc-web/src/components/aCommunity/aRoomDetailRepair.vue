<template>
  <div class="margin-top">
    <div class="margin-top">
      <el-table :data="aRoomDetailRepairInfo.repairs" style="width: 100%">
        <el-table-column prop="repairId" :label="$t('aRoomDetailRepair.repairId')" align="center"></el-table-column>
        <el-table-column prop="repairObjName" :label="$t('aRoomDetailRepair.location')" align="center"></el-table-column>
        <el-table-column prop="repairTypeName" :label="$t('aRoomDetailRepair.repairType')" align="center"></el-table-column>
        <el-table-column prop="repairName" :label="$t('aRoomDetailRepair.repairPerson')" align="center"></el-table-column>
        <el-table-column prop="tel" :label="$t('aRoomDetailRepair.contact')" align="center"></el-table-column>
        <el-table-column prop="appointmentTime" :label="$t('aRoomDetailRepair.appointmentTime')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('aRoomDetailRepair.state')" align="center">
          <template slot-scope="scope">
            {{scope.row.state == '1800' && (scope.row.returnVisitFlag == '001' || scope.row.returnVisitFlag == '002') ? 
              `${scope.row.stateName}(${$t('aRoomDetailRepair.taskProcessing')})` : scope.row.stateName}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openRepairDetail(scope.row)">
              {{$t('common.detail')}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminOwnerRepairs } from '@/api/aCommunity/aRoomDetailRepairApi'

export default {
  name: 'ARoomDetailRepair',
  data() {
    return {
      aRoomDetailRepairInfo: {
        repairs: [],
        ownerId: '',
        link: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    },
  methods: {
    open(data) {
      this.aRoomDetailRepairInfo.ownerId = data.ownerId
      this.aRoomDetailRepairInfo.link = data.link
      this._loadARoomDetailRepairData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadARoomDetailRepairData(val, this.pageSize)
    },
    async _loadARoomDetailRepairData(page, row) {
      try {
        const res = await listAdminOwnerRepairs({
          tel: this.aRoomDetailRepairInfo.link,
          page,
          row
        })
        this.aRoomDetailRepairInfo.repairs = res.data
        this.total = res.total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _qureyARoomDetailRepair() {
      this.currentPage = 1
      this._loadARoomDetailRepairData(1, this.pageSize)
    },
    _openRepairDetail(repairPool) {
      this.$router.push(`/pages/property/ownerRepairDetail?repairId=${repairPool.repairId}`)
    }
  }
}
</script>

<style scoped>
/* Same styles as previous components */
</style>