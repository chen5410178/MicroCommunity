<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="contractDetailOwnerInfo.owners" border style="width: 100%"
        :default-sort="{ prop: 'name', order: 'ascending' }">
        <el-table-column prop="url" :label="$t('contractDetailOwner.ownerFace')" width="100" align="center">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px;" class="border-radius"
              :src="scope.row.url || '/img/noPhoto.jpg'" :preview-src-list="[scope.row.url]" v-if="scope.row.url">
            </el-image>
            <img v-else style="width: 60px; height: 60px;" class="border-radius" src="/img/noPhoto.jpg" />
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('contractDetailOwner.name')" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column prop="sex" :label="$t('contractDetailOwner.sex')" width="80" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('common.male') : $t('common.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('contractDetailOwner.idCard')" width="180" align="center">
          <template slot-scope="scope">
            {{ scope.row.idCard || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('contractDetailOwner.address')" width="200" align="center">
          <template slot-scope="scope">
            {{ scope.row.address || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="roomCount" :label="$t('contractDetailOwner.roomCount')" width="100" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewOwnerRooms(scope.row)">
              {{ scope.row.roomCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" :label="$t('contractDetailOwner.memberCount')" width="100" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewOwnerMembers(scope.row)">
              {{ scope.row.memberCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="carCount" :label="$t('contractDetailOwner.carCount')" width="100" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewOwnerCars(scope.row)">
              {{ scope.row.carCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="complaintCount" :label="$t('contractDetailOwner.complaintCount')" width="100"
          align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewComplaints(scope.row)">
              {{ scope.row.complaintCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="repairCount" :label="$t('contractDetailOwner.repairCount')" width="100" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewRepairs(scope.row)">
              {{ scope.row.repairCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="oweFee" :label="$t('contractDetailOwner.oweFee')" width="120" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewOweFees(scope.row)">
              {{ scope.row.oweFee || '0.00' }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="contractCount" :label="$t('contractDetailOwner.contractCount')" width="120"
          align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewRoomContracts(scope.row)">
              {{ scope.row.contractCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="text-right">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
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
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import OwnerRooms from '@/components/owner/ownerRooms'
import OwnerMembers from '@/components/owner/ownerMembers'
import OwnerCars from '@/components/owner/ownerCars'
import OwnerComplaints from '@/components/owner/ownerComplaints'
import OwnerRepairs from '@/components/owner/ownerRepairs'
import OwnerOweFees from '@/components/owner/ownerOweFees'
import RoomContracts from '@/components/room/roomContracts'
import ViewImage from '@/components/system/viewImage'
import { queryOwners } from '@/api/contract/contractDetailOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailOwner',
  components: {
    OwnerRooms,
    OwnerMembers,
    OwnerCars,
    OwnerComplaints,
    OwnerRepairs,
    OwnerOweFees,
    RoomContracts,
    ViewImage
  },
  data() {
    return {
      contractDetailOwnerInfo: {
        owners: [],
        ownerId: ''
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
      this.contractDetailOwnerInfo.ownerId = params.ownerId
      this._loadContractDetailOwnerData(this.currentPage, this.pageSize)
    },
    _loadContractDetailOwnerData(page, row) {
      const param = {
        ownerId: this.contractDetailOwnerInfo.ownerId,
        communityId: this.communityId,
        ownerTypeCd: '1001',
        page: page,
        row: row
      }

      queryOwners(param)
        .then(response => {
          this.contractDetailOwnerInfo.owners = response.data
          this.total = response.total
          this.currentPage = page
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._loadContractDetailOwnerData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadContractDetailOwnerData(this.currentPage, this.pageSize)
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

<style scoped>
.margin-top {
  margin-top: 20px;
}

.border-radius {
  border-radius: 4px;
}
</style>