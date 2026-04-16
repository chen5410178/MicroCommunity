<template>
  <div class="margin-top">
    <el-table :data="ownerDetailRepairInfo.repairs" style="width: 100%">
      <el-table-column prop="repairId" :label="$t('ownerDetailRepair.repairId')" align="center"></el-table-column>
      <el-table-column prop="repairObjName" :label="$t('ownerDetailRepair.location')" align="center"></el-table-column>
      <el-table-column prop="repairTypeName" :label="$t('ownerDetailRepair.repairType')" align="center"></el-table-column>
      <el-table-column prop="repairName" :label="$t('ownerDetailRepair.reporter')" align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('ownerDetailRepair.contact')" align="center"></el-table-column>
      <el-table-column prop="appointmentTime" :label="$t('ownerDetailRepair.appointmentTime')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('ownerDetailRepair.status')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.state == '1800' && (scope.row.returnVisitFlag == '001' || scope.row.returnVisitFlag == '002')">
            {{scope.row.stateName}}({{$t('ownerDetailRepair.taskProcess')}})
          </span>
          <span v-else>
            {{scope.row.stateName}}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" @click="_openRepairDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
import { listOwnerRepairs } from '@/api/owner/ownerDetailRepairApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailRepair',
  data() {
    return {
      ownerDetailRepairInfo: {
        repairs: [],
        ownerId: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    _loadOwnerDetailRepairData(page, row) {
      const params = {
        communityId: getCommunityId(),
        ownerId: this.ownerDetailRepairInfo.ownerId,
        page: page,
        row: row
      }

      listOwnerRepairs(params).then(response => {
        this.ownerDetailRepairInfo.repairs = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _openRepairDetail(repairPool) {
      this.$router.push(`/property/ownerRepairDetail?repairId=${repairPool.repairId}`)
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailRepairData(val, this.pagination.pageSize)
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailRepairInfo.ownerId = data.ownerId
      this._loadOwnerDetailRepairData(1, this.pagination.pageSize)
    })
  }
}
</script>