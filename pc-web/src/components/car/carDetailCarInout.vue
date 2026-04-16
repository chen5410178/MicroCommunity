<template>
  <div>
    <div class="margin-top">
      <el-table :data="carDetailCarInoutInfo.carIns" border style="width: 100%">
        <el-table-column :label="$t('carDetailCarInout.entryImage')" align="center">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px;" :src="scope.row.photoJpg || '/img/noPhoto.jpg'"
              :preview-src-list="[scope.row.photoJpg]" fit="cover" class="border-radius">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="inoutId" :label="$t('carDetailCarInout.inoutId')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailCarInout.carStatus')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carInout === '3306' ? $t('carDetailCarInout.entry') :
              $t('carDetailCarInout.exit')}}({{ scope.row.stateName }})
          </template>
        </el-table-column>
        <el-table-column prop="carNum" :label="$t('carDetailCarInout.carNum')" align="center"></el-table-column>
        <el-table-column prop="paNum" :label="$t('carDetailCarInout.parkingLot')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailCarInout.billingRule')" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="_viewTempFeeConfigInOutCar(scope.row.configId)">
              {{ scope.row.feeName }} <i class="el-icon-info"></i>
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="carTypeName" :label="$t('carDetailCarInout.carType')" align="center"></el-table-column>
        <el-table-column prop="inTime" :label="$t('carDetailCarInout.entryTime')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailCarInout.exitTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carInout !== '3307' ? '-' : scope.row.outTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailCarInout.parkingTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.hours }}{{ $t('carDetailCarInout.hour') }}{{ scope.row.min }}{{ $t('carDetailCarInout.minute') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailCarInout.chargeAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carType !== 'T' ? '-' : scope.row.payCharge }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailCarInout.remark')" align="center"></el-table-column>
      </el-table>

      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCarInoutDetail } from '@/api/car/carDetailCarInoutApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailCarInout',
  data() {
    return {
      carDetailCarInoutInfo: {
        carIns: [],
        carId: '',
        memberId: '',
        carNum: '',
        areaNum: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.carDetailCarInoutInfo.carId = data.carId
      this.carDetailCarInoutInfo.carNum = data.carNum
      this.carDetailCarInoutInfo.areaNum = data.areaNum
      this.carDetailCarInoutInfo.memberId = data.memberId
      this._loadCarDetailCarInoutData(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailCarInoutInfo.carId = data.carId
      this.carDetailCarInoutInfo.carNum = data.carNum
      this.carDetailCarInoutInfo.areaNum = data.areaNum
      this.carDetailCarInoutInfo.memberId = data.memberId
      this._loadCarDetailCarInoutData(this.currentPage, this.pageSize)
    },
    _loadCarDetailCarInoutData(page, row) {
      const params = {
        communityId: getCommunityId(),
        carNum: this.carDetailCarInoutInfo.carNum,
        paNum: this.carDetailCarInoutInfo.areaNum,
        iotApiCode: 'listCarInoutDetailBmoImpl',
        page,
        row
      }

      getCarInoutDetail(params).then(response => {
        this.carDetailCarInoutInfo.carIns = response.data
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadCarDetailCarInoutData(val, this.pageSize)
    },
    _viewTempFeeConfigInOutCar(configId) {
      // 查看计费规则详情
      console.log(configId)
    },
    _carInoutOpenFile(fileUrl) {
      // 打开文件预览
      window.open(fileUrl)
    }
  }
}
</script>