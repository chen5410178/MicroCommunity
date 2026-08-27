<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aCarDetailMemberInfo.ownerCars" style="width: 100%">
        <el-table-column prop="carNum" :label="$t('carDetailMember.carNum')" align="center"></el-table-column>
        <el-table-column prop="roomName" :label="$t('carDetailMember.roomNum')" align="center"></el-table-column>
        <el-table-column prop="carTypeName" :label="$t('carDetailMember.carType')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailMember.owner')" align="center">
          <template slot-scope="scope">
            {{scope.row.ownerName}}({{scope.row.link}})
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailMember.parkingSpace')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.areaNum">
              {{scope.row.areaNum}}
            </span>
            <span v-else>{{$t('carDetailMember.none')}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailMember.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('carDetailMember.endTime')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailMember.status')" align="center">
          <template slot-scope="scope">
            {{scope.row.stateName}}
            <span v-if="scope.row.iotStateName">
              ({{scope.row.iotStateName}})
            </span>
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
import { queryAdminOwnerCars } from '@/api/aCommunity/aCarDetailMemberApi'

export default {
  name: 'ACarDetailMember',
  data() {
    return {
      aCarDetailMemberInfo: {
        ownerCars: [],
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
      this.aCarDetailMemberInfo.carId = data.carId
      this.aCarDetailMemberInfo.carNum = data.carNum
      this.aCarDetailMemberInfo.memberId = data.memberId
      this._loadACarDetailMemberData(1, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailMemberData(page, this.pageSize)
    },
    async _loadACarDetailMemberData(page, row) {
      try {
        const param = {
          carId: this.aCarDetailMemberInfo.carId,
          carTypeCd: '1002',
          page,
          row
        }
        const response = await queryAdminOwnerCars(param)
        this.aCarDetailMemberInfo.ownerCars = response.data
        this.total = response.total
      } catch (error) {
        console.error('Failed to load member data:', error)
      }
    },
    _viewIotStateRemark(car) {
      // Implement view IoT remark logic
      console.log(car)
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