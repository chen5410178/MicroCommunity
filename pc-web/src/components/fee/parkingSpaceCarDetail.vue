<template>
  <div class="parking-space-car-detail">
    <el-table v-loading="loading" :data="cars" border style="width: 100%; margin-top: 10px">
      <el-table-column :label="$t('parkingSpaceCarDetail.carNum')" prop="carNum" align="center" />
      <el-table-column :label="$t('parkingSpaceCarDetail.carType')" prop="carTypeName" align="center" />
      <el-table-column :label="$t('parkingSpaceCarDetail.owner')" align="center">
        <template slot-scope="scope">
          <el-button type="text" class="btn-link" @click="_toOwnerDetail(scope.row)">{{ scope.row.ownerName
          }}</el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('parkingSpaceCarDetail.leasePeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime || parkingSpaceCarDetailInfo.startTime }} ~ {{ scope.row.endTime ||
            parkingSpaceCarDetailInfo.endTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('parkingSpaceCarDetail.status')" prop="stateName" align="center" />
    </el-table>
    <div v-if="!loading && cars.length === 0" class="empty-tip">
      <i class="el-icon-info"></i>
      {{ $t('parkingSpaceCarDetail.noCarTip') }}
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwnerCars } from '@/api/fee/parkingSpaceCreateFeeApi'

export default {
  name: 'ParkingSpaceCarDetail',
  data() {
    return {
      communityId: '',
      loading: false,
      cars: [],
      parkingSpaceCarDetailInfo: {
        psId: '',
        carId: '',
        carNum: '',
        ownerId: '',
        ownerName: '',
        startTime: '',
        endTime: ''
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(param) {
      this.cars = []
      this.parkingSpaceCarDetailInfo.psId = param.psId || ''
      this.parkingSpaceCarDetailInfo.carId = param.carId || ''
      this.parkingSpaceCarDetailInfo.carNum = param.carNum || ''
      this.parkingSpaceCarDetailInfo.ownerId = param.ownerId || ''
      this.parkingSpaceCarDetailInfo.ownerName = param.ownerName || ''
      this.parkingSpaceCarDetailInfo.startTime = param.startTime || ''
      this.parkingSpaceCarDetailInfo.endTime = param.endTime || ''
      this._listParkingSpaceCar()
    },
    async _listParkingSpaceCar() {
      if (!this.parkingSpaceCarDetailInfo.carId) {
        return
      }
      this.loading = true
      try {
        const params = {
          page: 1,
          row: 10,
          communityId: this.communityId,
          carId: this.parkingSpaceCarDetailInfo.carId,
          carTypeCd: '1001'
        }
        const response = await queryOwnerCars(params)
        this.cars = response.data || []
      } catch (error) {
        console.error('获取车位车辆失败:', error)
        this.$message.error(this.$t('parkingSpaceCarDetail.loadError'))
      } finally {
        this.loading = false
      }
    },
    _toOwnerDetail(row) {
      window.open(`/#/views/owner/ownerDetail?ownerId=${row.ownerId || this.parkingSpaceCarDetailInfo.ownerId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.parking-space-car-detail {
  .empty-tip {
    padding: 30px 0;
    text-align: center;
    color: #909399;
    font-size: 14px;

    i {
      margin-right: 4px;
    }
  }
}
</style>
