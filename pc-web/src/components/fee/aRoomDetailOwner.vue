<template>
  <div class="owner-detail-container">
    <el-table :data="owners" border>
      <el-table-column :label="$t('adminRoomFee.ownerFace')" align="center" width="100">
        <template slot-scope="scope">
          <el-image style="width: 60px; height: 60px;" :src="scope.row.url || '/img/noPhoto.jpg'"
            :preview-src-list="[scope.row.url || '/img/noPhoto.jpg']" fit="cover" class="border-radius" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('adminRoomFee.name')" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}({{ scope.row.link }})
        </template>
      </el-table-column>
      <el-table-column :label="$t('adminRoomFee.gender')" align="center" width="80">
        <template slot-scope="scope">
          {{ scope.row.sex == 0 ? $t('common.male') : $t('common.female') }}
        </template>
      </el-table-column>
      <el-table-column prop="idCard" :label="$t('adminRoomFee.idCard')" align="center" />
      <el-table-column prop="address" :label="$t('adminRoomFee.address')" align="center" />
      <el-table-column prop="roomCount" :label="$t('adminRoomFee.roomCount')" align="center" />
      <el-table-column prop="memberCount" :label="$t('adminRoomFee.memberCount')" align="center" />
      <el-table-column prop="carCount" :label="$t('adminRoomFee.carCount')" align="center" />
      <el-table-column prop="complaintCount" :label="$t('adminRoomFee.complaintCount')" align="center" />
      <el-table-column prop="repairCount" :label="$t('adminRoomFee.repairCount')" align="center" />
      <el-table-column prop="oweFee" :label="$t('adminRoomFee.oweFee')" align="center" />
      <el-table-column prop="contractCount" :label="$t('adminRoomFee.contractCount')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" @click="toDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="page.current" :page-size="page.size" :total="page.total"
      layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { queryAdminRoomOwner } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'ARoomDetailOwner',
  props: {
    roomId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      owners: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.roomId = params.roomId
      this.loadData()
    },
    async loadData() {
      const params = {
        roomId: this.roomId,
        ownerTypeCd: '1001',
        page: this.page.current,
        row: this.page.size
      }

      const res = await queryAdminRoomOwner(params)
      this.owners = res.data
      this.page.total = res.total

    },
    toDetail(owner) {
      window.open(`/#/views/aCommunity/adminOwnerDetail?ownerId=${owner.ownerId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.border-radius {
  border-radius: 50%;
}
</style>