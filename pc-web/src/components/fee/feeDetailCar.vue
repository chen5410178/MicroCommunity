<template>
  <div>
    <div class="margin-top">
      <el-table :data="feeDetailCarInfo.cars" style="width: 100%">
        <el-table-column prop="carNum" :label="$t('feeDetailCar.licensePlate')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailCar.licenseType')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.leaseType == 'T'">{{$t('feeDetailCar.temporaryCar')}}</span>
            <span v-else>{{scope.row.leaseTypeName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="carTypeName" :label="$t('feeDetailCar.carType')" align="center"></el-table-column>
        <el-table-column prop="carColor" :label="$t('feeDetailCar.color')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailCar.owner')" align="center">
          <template slot-scope="scope">{{scope.row.ownerName}}({{scope.row.link}})</template>
        </el-table-column>
        <el-table-column :label="$t('feeDetailCar.parkingSpace')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.areaNum && scope.row.state == '1001'">
              {{scope.row.areaNum}}{{$t('feeDetailCar.parkingLot')}}{{scope.row.num}}{{$t('feeDetailCar.parkingSpace')}}
            </span>
            <span v-else>{{$t('feeDetailCar.spaceReleased')}}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeDetailCar.validityPeriod')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.leaseType == 'H'">
              {{scope.row.startTime}}<br>~{{scope.row.endTime}}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-size="pagination.pageSize"
            layout="total, prev, pager, next"
            :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryOwnerCars } from '@/api/fee/feeDetailCarApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailCar',
  data() {
    return {
      feeDetailCarInfo: {
        cars: [],
        memberId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailCarInfo.memberId = params.payerObjId
      this._loadFeeDetailCarData()
    },
    _loadFeeDetailCarData() {
      const params = {
        communityId: getCommunityId(),
        memberId: this.feeDetailCarInfo.memberId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      queryOwnerCars(params).then(res => {
        this.feeDetailCarInfo.cars = res.data
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load car details:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailCarData()
    }
  }
}
</script>