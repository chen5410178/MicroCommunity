<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" @click="_openAddParkingSpaceApplyModal">
          {{ $t('carDetailApplyParkingSpace.applyParkingSpace') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="carDetailApplyParkingSpaceInfo.parkingSpaceApplys" border style="width: 100%">
        <el-table-column prop="applyId" :label="$t('carDetailApplyParkingSpace.applyId')"
          align="center"></el-table-column>
        <el-table-column prop="carNum" :label="$t('carDetailApplyParkingSpace.carNum')"
          align="center"></el-table-column>
        <el-table-column :label="$t('carDetailApplyParkingSpace.parkingSpace')" align="center">
          <template slot-scope="scope">
            {{ scope.row.psId ? `${scope.row.areaNum}停车场 - ${scope.row.num}停车位` : $t('carDetailApplyParkingSpace.none') }}
          </template>
        </el-table-column>
        <el-table-column prop="carBrand" :label="$t('carDetailApplyParkingSpace.carBrand')"
          align="center"></el-table-column>
        <el-table-column :label="$t('carDetailApplyParkingSpace.carType')" align="center">
          <template slot-scope="scope">
            {{ _getParkingSpaceApplyCatType(scope.row.carType) }}
          </template>
        </el-table-column>
        <el-table-column prop="carColor" :label="$t('carDetailApplyParkingSpace.color')"
          align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailApplyParkingSpace.startTime')"
          align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('carDetailApplyParkingSpace.endTime')"
          align="center"></el-table-column>
        <el-table-column prop="applyPersonName" :label="$t('carDetailApplyParkingSpace.applicant')"
          align="center"></el-table-column>
        <el-table-column prop="applyPersonLink" :label="$t('carDetailApplyParkingSpace.phone')"
          align="center"></el-table-column>
        <el-table-column :label="$t('carDetailApplyParkingSpace.auditResult')" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStateTagType(scope.row.state)">
              {{ _getParkingSpaceApplyState(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailApplyParkingSpace.operation')" align="center">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-if="scope.row.state == '1001'" size="mini"
                @click="_openAuditParkingSpaceApplyModal(scope.row)">
                {{ $t('carDetailApplyParkingSpace.audit') }}
              </el-button>
              <el-button v-if="scope.row.state == '1001'" size="mini"
                @click="_openEditParkingSpaceApplyModel(scope.row)">
                {{ $t('carDetailApplyParkingSpace.edit') }}
              </el-button>
              <el-button size="mini" @click="_openDeleteParkingSpaceApplyModel(scope.row)">
                {{ $t('carDetailApplyParkingSpace.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

    <edit-parking-space-apply ref="editParkingSpaceApply"></edit-parking-space-apply>
    <delete-parking-space-apply ref="deleteParkingSpaceApply"></delete-parking-space-apply>
  </div>
</template>

<script>
import { listParkingSpaceApply } from '@/api/car/carDetailApplyParkingSpaceApi'
import EditParkingSpaceApply from '@/components/property/editParkingSpaceApply'
import DeleteParkingSpaceApply from '@/components/property/deleteParkingSpaceApply'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailApplyParkingSpace',
  components: {
    EditParkingSpaceApply,
    DeleteParkingSpaceApply
  },
  data() {
    return {
      carDetailApplyParkingSpaceInfo: {
        parkingSpaceApplys: [],
        carId: '',
        carNum: '',
        memberId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.carDetailApplyParkingSpaceInfo.carId = data.carId
      this.carDetailApplyParkingSpaceInfo.carNum = data.carNum
      this.carDetailApplyParkingSpaceInfo.memberId = data.memberId
      this._loadCarDetailApplyParkingSpaceData(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailApplyParkingSpaceInfo.carId = data.carId
      this.carDetailApplyParkingSpaceInfo.carNum = data.carNum
      this.carDetailApplyParkingSpaceInfo.memberId = data.memberId
      this._loadCarDetailApplyParkingSpaceData(this.currentPage, this.pageSize)
    },
    _loadCarDetailApplyParkingSpaceData(page, row) {
      const params = {
        communityId: getCommunityId(),
        carNum: this.carDetailApplyParkingSpaceInfo.carNum,
        page,
        row
      }

      listParkingSpaceApply(params).then(response => {
        this.carDetailApplyParkingSpaceInfo.parkingSpaceApplys = response.data
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadCarDetailApplyParkingSpaceData(val, this.pageSize)
    },
    _openAddParkingSpaceApplyModal() {
      this.$router.push('/property/addParkingSpaceApply')
    },
    _openAuditParkingSpaceApplyModal(apply) {
      this.$router.push(`/property/auditParkingSpaceApply?applyId=${apply.applyId}`)
    },
    _openEditParkingSpaceApplyModel(parkingSpaceApply) {
      this.$refs.editParkingSpaceApply.open(parkingSpaceApply)
    },
    _openDeleteParkingSpaceApplyModel(parkingSpaceApply) {
      this.$refs.deleteParkingSpaceApply.open(parkingSpaceApply)
    },
    _getParkingSpaceApplyState(state) {
      const states = {
        '1001': this.$t('carDetailApplyParkingSpace.pendingReview'),
        '2002': this.$t('carDetailApplyParkingSpace.pendingPayment'),
        '3003': this.$t('carDetailApplyParkingSpace.completed'),
        '4004': this.$t('carDetailApplyParkingSpace.auditFailed')
      }
      return states[state] || this.$t('carDetailApplyParkingSpace.abnormalStatus')
    },
    _getParkingSpaceApplyCatType(type) {
      const types = {
        '9901': this.$t('carDetailApplyParkingSpace.familyCar'),
        '9902': this.$t('carDetailApplyParkingSpace.bus'),
        '9903': this.$t('carDetailApplyParkingSpace.truck')
      }
      return types[type] || this.$t('carDetailApplyParkingSpace.abnormalVehicle')
    },
    getStateTagType(state) {
      const types = {
        '1001': 'info',
        '2002': 'warning',
        '3003': 'success',
        '4004': 'danger'
      }
      return types[state] || 'info'
    }
  }
}
</script>