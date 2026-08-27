<template>
  <div class="car-detail-his">
    <el-table :data="carDetailHisInfo.cars" border style="width: 100%" v-loading="loading">
      <el-table-column prop="carNum" :label="$t('carDetailHis.carNum')" align="center">
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.leaseType')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.leaseType === 'T'">{{ $t('carDetailHis.tempCar') }}</span>
          <span v-else>{{ scope.row.leaseTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.carType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.carTypeName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.owner')" align="center">
        <template slot-scope="scope">
          <div class="hand">
            {{ scope.row.ownerName }}({{ scope.row.link }})
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.parkingArea')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.areaNum">
            {{ scope.row.areaNum }}
          </span>
          <span v-else>
            {{ $t('carDetailHis.spaceReleased') }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.validPeriod')" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.leaseType === 'H'">
            {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
          </div>
          <div v-else>--</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          {{ _getHisOperate(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.operator')" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('carDetailHis.operateTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { queryHisOwnerCar } from '@/api/system/operateDataLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailHis',
  data() {
    return {
      loading: false,
      carDetailHisInfo: {
        cars: [],
        carId: '',
        memberId: '',
        carNum: '',
        carNumLike: '',
        logStartTime: '',
        logEndTime: '',
        paId: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(conditions) {
      this.carDetailHisInfo = {
        ...this.carDetailHisInfo,
        ...conditions
      }
      this._loadCarDetailHisData()
    },
    async _loadCarDetailHisData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          carNum: this.carDetailHisInfo.carNum,
          carNumLike: this.carDetailHisInfo.carNumLike,
          logStartTime: this.carDetailHisInfo.logStartTime,
          logEndTime: this.carDetailHisInfo.logEndTime,
          communityId: getCommunityId()
        }

        const { data, total } = await queryHisOwnerCar(params)
        this.carDetailHisInfo.cars = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load car history:', error)
      } finally {
        this.loading = false
      }
    },
    _getHisOperate(car) {
      const carCount = this.carDetailHisInfo.cars.filter(item => item.bId === car.bId).length

      if (carCount <= 1) {
        if (car.operate === 'ADD') return this.$t('carDetailHis.add')
        if (car.operate === 'DEL') return this.$t('carDetailHis.delete')
        return '-'
      }

      if (car.operate === 'ADD') return this.$t('carDetailHis.modifyNew')
      if (car.operate === 'DEL') return this.$t('carDetailHis.modifyOld')
      return '-'
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadCarDetailHisData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._loadCarDetailHisData()
    }
  }
}
</script>

<style scoped>
.car-detail-his {
  padding: 20px;
}

.hand {
  cursor: pointer;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>