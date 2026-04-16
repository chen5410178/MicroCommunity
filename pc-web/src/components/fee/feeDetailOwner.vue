<template>
  <div>
    <div class="margin-top">
      <el-table :data="feeDetailOwnerInfo.owners" border style="width: 100%">
        <el-table-column :label="$t('feeDetailOwner.ownerFace')" align="center">
          <template #default="{ row }">
            <el-image style="width: 60px; height: 60px;" class="border-radius" :src="row.url || '/img/noPhoto.jpg'"
              :preview-src-list="[row.url]" v-if="row.url"></el-image>
            <el-image style="width: 60px; height: 60px;" class="border-radius" src="/img/noPhoto.jpg" v-else></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('feeDetailOwner.name')" align="center">
          <template #default="{ row }">
            {{ row.name }}({{ row.link }})
          </template>
        </el-table-column>
        <el-table-column prop="sex" :label="$t('feeDetailOwner.gender')" align="center">
          <template #default="{ row }">
            {{ row.sex === 0 ? $t('feeDetailOwner.male') : $t('feeDetailOwner.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('feeDetailOwner.idCard')" align="center"></el-table-column>
        <el-table-column prop="address" :label="$t('feeDetailOwner.address')" align="center"></el-table-column>
        <el-table-column prop="roomCount" :label="$t('feeDetailOwner.roomCount')" align="center">
          <template #default="{ row }">
              {{ row.roomCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" :label="$t('feeDetailOwner.memberCount')" align="center">
          <template #default="{ row }">
              {{ row.memberCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="carCount" :label="$t('feeDetailOwner.carCount')" align="center">
          <template #default="{ row }">
              {{ row.carCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="complaintCount" :label="$t('feeDetailOwner.complaintCount')" align="center">
          <template #default="{ row }">
              {{ row.complaintCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="repairCount" :label="$t('feeDetailOwner.repairCount')" align="center">
          <template #default="{ row }">
              {{ row.repairCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="oweFee" :label="$t('feeDetailOwner.oweFee')" align="center">
          <template #default="{ row }">
              {{ row.oweFee || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="contractCount" :label="$t('feeDetailOwner.contractCount')" align="center">
          <template #default="{ row }">
              {{ row.contractCount || 0 }}
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total"></el-pagination>
        </el-col>
      </el-row>
    </div>

    <owner-rooms ref="ownerRooms"></owner-rooms>
    <owner-members ref="ownerMembers"></owner-members>
    <owner-cars ref="ownerCars"></owner-cars>
    <owner-complaints ref="ownerComplaints"></owner-complaints>
    <owner-repairs ref="ownerRepairs"></owner-repairs>
    <owner-owe-fees ref="ownerOweFees"></owner-owe-fees>
    <room-contracts ref="roomContracts"></room-contracts>
  </div>
</template>

<script>
import { queryOwners } from '@/api/fee/feeDetailOwnerApi'
import OwnerRooms from '@/components/owner/ownerRooms'
import OwnerMembers from '@/components/owner/ownerMembers'
import OwnerCars from '@/components/owner/ownerCars'
import OwnerComplaints from '@/components/owner/ownerComplaints'
import OwnerRepairs from '@/components/owner/ownerRepairs'
import OwnerOweFees from '@/components/owner/ownerOweFees'
import RoomContracts from '@/components/room/roomContracts'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailOwner',
  components: {
    OwnerRooms,
    OwnerMembers,
    OwnerCars,
    OwnerComplaints,
    OwnerRepairs,
    OwnerOweFees,
    RoomContracts
  },
  data() {
    return {
      feeDetailOwnerInfo: {
        owners: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(params) {
      if (!params.ownerId) return
      this.feeDetailOwnerInfo.ownerId = params.ownerId
      this._loadFeeDetailOwnerData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadFeeDetailOwnerData(this.currentPage, this.pageSize)
    },
    async _loadFeeDetailOwnerData(page, row) {
      try {
        const params = {
          ownerId: this.feeDetailOwnerInfo.ownerId,
          communityId: getCommunityId(),
          ownerTypeCd: '1001',
          page,
          row
        }

        const res = await queryOwners(params)
        this.feeDetailOwnerInfo.owners = res.data
        this.total = res.total
      } catch (error) {
        console.error('Failed to load owner data:', error)
      }
    },
    _viewOwnerRooms(owner) {
      this.$refs.ownerRooms.open(owner)
    },
    _viewOwnerMembers(owner) {
      this.$refs.ownerMembers.open(owner)
    },
    _viewOwnerCars(owner) {
      this.$refs.ownerCars.open(owner)
    },
    _viewComplaints(owner) {
      this.$refs.ownerComplaints.open(owner)
    },
    _viewRepairs(owner) {
      this.$refs.ownerRepairs.open(owner)
    },
    _viewOweFees(owner) {
      this.$refs.ownerOweFees.open(owner)
    },
    _viewRoomContracts(owner) {
      this.$refs.roomContracts.open(owner)
    }
  }
}
</script>