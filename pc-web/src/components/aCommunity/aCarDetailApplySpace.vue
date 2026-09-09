<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aCarDetailApplySpaceInfo.applys" style="width: 100%">
        <el-table-column prop="applyId" :label="$t('carDetailApply.applyId')" align="center"></el-table-column>
        <el-table-column prop="carNum" :label="$t('carDetailApply.carNum')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailApply.parkingSpace')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.psId != null && scope.row.psId != ''">
              {{scope.row.areaNum}}{{$t('carDetailApply.parkingLot')}} - {{scope.row.num}}{{$t('carDetailApply.space')}}
            </span>
            <span v-else>{{$t('carDetailApply.none')}}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailApply.carType')" align="center">
          <template slot-scope="scope">
            {{_getParkingSpaceApplyCatType(scope.row.carType)}}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailApply.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('carDetailApply.endTime')" align="center"></el-table-column>
        <el-table-column prop="applyPersonName" :label="$t('carDetailApply.applicant')" align="center"></el-table-column>
        <el-table-column prop="applyPersonLink" :label="$t('carDetailApply.phone')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailApply.result')" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state == '1001'" type="success">
              {{_getParkingSpaceApplyState(scope.row.state)}}
            </el-tag>
            <el-tag v-else-if="scope.row.state == '4004'" type="danger">
              {{_getParkingSpaceApplyState(scope.row.state)}}
            </el-tag>
            <el-tag v-else-if="scope.row.state == '2002'" type="info">
              {{_getParkingSpaceApplyState(scope.row.state)}}
            </el-tag>
            <el-tag v-else type="info">
              {{_getParkingSpaceApplyState(scope.row.state)}}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12" class="text-right">
          <el-pagination
            @current-change="handlePageChange"
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
import { listAdminParkingSpaceApply } from '@/api/aCommunity/aCarDetailApplySpaceApi'

export default {
  name: 'ACarDetailApplySpace',
  data() {
    return {
      aCarDetailApplySpaceInfo: {
        applys: [],
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
      this.handleSwitch(data)
    },
    handleSwitch(data) {
      this.aCarDetailApplySpaceInfo.carId = data.carId
      this.aCarDetailApplySpaceInfo.carNum = data.carNum
      this.aCarDetailApplySpaceInfo.memberId = data.memberId
      this._loadACarDetailApplySpaceData(1, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailApplySpaceData(page, this.pageSize)
    },
    async _loadACarDetailApplySpaceData(page, row) {
      try {
        const param = {
          carNum: this.aCarDetailApplySpaceInfo.carNum,
          page,
          row
        }
        const response = await listAdminParkingSpaceApply(param)
        this.aCarDetailApplySpaceInfo.applys = response.data
        this.total = response.total
      } catch (error) {
        console.error('Failed to load apply space data:', error)
      }
    },
    _getParkingSpaceApplyState(state) {
      if (state == '1001') {
        return this.$t('carDetailApply.pendingReview')
      } else if (state == '2002') {
        return this.$t('carDetailApply.pendingPayment')
      } else if (state == '3003') {
        return this.$t('carDetailApply.completed')
      } else if (state == '4004') {
        return this.$t('carDetailApply.reviewFailed')
      }
      return this.$t('carDetailApply.abnormalStatus')
    },
    _getParkingSpaceApplyCatType(type) {
      if (type == '9901') {
        return this.$t('carDetailApply.familyCar')
      } else if (type == '9902') {
        return this.$t('carDetailApply.bus')
      } else if (type == '9903') {
        return this.$t('carDetailApply.truck')
      }
      return this.$t('carDetailApply.abnormalCar')
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.text-right {
  text-align: right;
}
</style>