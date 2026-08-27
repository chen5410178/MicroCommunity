<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" @click="_addOwnerCarMember">
          {{ $t('common.add') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="">
      <el-table :data="carDetailMemberInfo.ownerCars" style="width: 100%">
        <el-table-column prop="carNum" :label="$t('listOwnerCar.carNum')" align="center"></el-table-column>
        <el-table-column prop="roomName" :label="$t('listOwnerCar.roomNum')" align="center"></el-table-column>
        <el-table-column prop="carTypeName" :label="$t('listOwnerCar.carType')" align="center"></el-table-column>
        <el-table-column :label="$t('listOwnerCar.owner')" align="center">
          <template #default="{ row }">
            {{ row.ownerName }}({{ row.link }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('listOwnerCar.parkingArea')" align="center">
          <template #default="{ row }">
            <span v-if="row.areaNum">
              {{ row.areaNum }}
            </span>
            <span v-else>
              {{ $t('listOwnerCar.none') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('listOwnerCar.startRentTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('listOwnerCar.endRentTime')" align="center"></el-table-column>
        <el-table-column :label="$t('listOwnerCar.status')" align="center">
          <template #default="{ row }">
            {{ row.stateName }}
            <span v-if="row.iotStateName">
              ({{ row.iotStateName }})
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center">
          <template #default="{ row }">
            <el-button type="text" @click="_openEditOwnerCar(row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button type="text" @click="_openDelOwnerCarModel(row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="prev, pager, next, jumper">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

    <add-car-modal ref="addCarModal" @notify="notify"></add-car-modal>
    <edit-member-car ref="editMemberCar" @notify="notify"></edit-member-car>
    <delete-owner-car ref="deleteOwnerCar" @notify="notify"></delete-owner-car>
  </div>
</template>

<script>
import { getOwnerCarDetail } from '@/api/car/carDetailApi'
import AddCarModal from '@/components/car/addCarModal'
import EditMemberCar from '@/components/car/editMemberCar'
import DeleteOwnerCar from '@/components/car/deleteOwnerCar'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailMember',
  components: {
    AddCarModal,
    EditMemberCar,
    DeleteOwnerCar
  },
  data() {
    return {
      carDetailMemberInfo: {
        ownerCars: [],
        carId: '',
        carNum: '',
        memberId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.carDetailMemberInfo.carId = data.carId
      this.carDetailMemberInfo.carNum = data.carNum
      this.carDetailMemberInfo.memberId = data.memberId
      this._loadCarDetailMemberData(1, this.pagination.pageSize)
    },
    switch(data) {
      this.carDetailMemberInfo.carId = data.carId
      this.carDetailMemberInfo.carNum = data.carNum
      this.carDetailMemberInfo.memberId = data.memberId
      this._loadCarDetailMemberData(1, this.pagination.pageSize)
    },
    notify() {
      this._loadCarDetailMemberData(this.pagination.currentPage, this.pagination.pageSize)
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadCarDetailMemberData(val, this.pagination.pageSize)
    },
    _loadCarDetailMemberData(page, row) {
      const params = {
        communityId: this.communityId,
        carId: this.carDetailMemberInfo.carId,
        carTypeCd: '1002',
        page: page,
        row: row
      }

      getOwnerCarDetail(params).then(response => {
        this.carDetailMemberInfo.ownerCars = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _addOwnerCarMember() {
      this.$refs.addCarModal.open({
        carId: this.carDetailMemberInfo.carId
      })
    },
    _openEditOwnerCar(car) {
      this.$refs.editMemberCar.open(car)
    },
    _openDelOwnerCarModel(car) {
      this.$refs.deleteOwnerCar.open(car)
    },
    _viewIotStateRemark(car) {
      const data = {
        "同步说明": car.iotRemark
      }
      this.$emit('viewData', {
        title: `${car.carNum} 同步物联网详情`,
        data: data
      })
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