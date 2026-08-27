<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('updateParkingSpaceCar.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="updateParkingSpaceCarInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.parkingSpace')" prop="num">
              <el-input :value="updateParkingSpaceCarInfo.num" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.currentOwner')">
              <el-input :value="updateParkingSpaceCarInfo.currentOwnerName" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.currentCarNum')">
              <el-input :value="updateParkingSpaceCarInfo.currentCarNum" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="updateParkingSpaceCarInfo.state === 'H'">
            <el-form-item :label="$t('updateParkingSpaceCar.currentRentTime')">
              <el-input :value="updateParkingSpaceCarInfo.currentRentTime" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider></el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.newCarNum')" prop="carId">
              <div class="flex">
                <el-input :value="selectedCarNum" disabled
                  :placeholder="$t('updateParkingSpaceCar.carNumPlaceholder')"></el-input>
                <el-button type="primary" class="car-select-btn" @click="openCarDialog">{{ $t('updateParkingSpaceCar.selectCar') }}</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.owner')" prop="ownerName">
              <el-input :value="updateParkingSpaceCarInfo.ownerName" disabled
                :placeholder="$t('updateParkingSpaceCar.ownerPlaceholder')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="updateParkingSpaceCarInfo.state === 'H'" :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.startTime')" prop="startTime">
              <el-date-picker v-model="updateParkingSpaceCarInfo.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('updateParkingSpaceCar.startTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.endTime')" prop="endTime">
              <el-date-picker v-model="updateParkingSpaceCarInfo.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('updateParkingSpaceCar.endTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="submitUpdate" :loading="saving">
        {{ $t('common.submit') }}
      </el-button>
    </div>

    <el-dialog :title="$t('updateParkingSpaceCar.selectCar')" :visible.sync="carDialogVisible" width="800px"
      @close="handleCarDialogClose">
      <el-form :inline="true" class="margin-bottom-sm">
        <el-form-item :label="$t('updateParkingSpaceCar.carNum')">
          <el-input v-model="carQuery.carNum" clearable
            :placeholder="$t('updateParkingSpaceCar.carNumQueryPlaceholder')" @keyup.enter.native="searchCars"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchCars">{{ $t('common.search') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="carLoading" :data="carList" border highlight-current-row @row-click="selectCar">
        <el-table-column prop="carNum" :label="$t('updateParkingSpaceCar.carNum')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('updateParkingSpaceCar.owner')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" @click.stop="selectCar(scope.row)">{{ $t('common.select') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="text-right margin-top-sm"
        :current-page="carPagination.page"
        :page-size="carPagination.row"
        :total="carPagination.total"
        layout="total, prev, pager, next"
        @current-change="handleCarPageChange"
      />
    </el-dialog>
  </div>
</template>

<script>
import { updateParkingSpaceCar, queryOwnerCars, listParkingSpaces } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'UpdateParkingSpaceCarList',
  data() {
    return {
      saving: false,
      selectedCarNum: '',
      carDialogVisible: false,
      carLoading: false,
      carQuery: { carNum: '' },
      carList: [],
      carPagination: { page: 1, row: 10, total: 0 },
      updateParkingSpaceCarInfo: {
        psId: '',
        num: '',
        state: '',
        currentOwnerName: '',
        currentCarNum: '',
        currentRentTime: '',
        carId: '',
        ownerId: '',
        ownerName: '',
        startTime: '',
        endTime: ''
      },
      rules: {
        carId: [
          { required: true, message: this.$t('updateParkingSpaceCar.carNumRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('updateParkingSpaceCar.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('updateParkingSpaceCar.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.updateParkingSpaceCarInfo.psId = this.$route.query.psId || ''
    this.loadParkingSpace()
  },
  methods: {
    loadParkingSpace() {
      listParkingSpaces({
        psId: this.updateParkingSpaceCarInfo.psId,
        page: 1,
        row: 10
      }).then(res => {
        const parkingSpaces = res.parkingSpaces || []
        if (parkingSpaces.length < 1) {
          this.$message.error(this.$t('updateParkingSpaceCar.errorMessage'))
          return
        }
        const ps = parkingSpaces[0]
        this.updateParkingSpaceCarInfo.num = ps.num || ''
        this.updateParkingSpaceCarInfo.state = ps.state || ''
        this.updateParkingSpaceCarInfo.currentOwnerName = ps.ownerName || ''
        this.updateParkingSpaceCarInfo.currentCarNum = ps.carNum || ''
        if (ps.startTime && ps.endTime) {
          this.updateParkingSpaceCarInfo.currentRentTime = ps.startTime + ' ~ ' + ps.endTime
          this.updateParkingSpaceCarInfo.startTime = ps.startTime
          this.updateParkingSpaceCarInfo.endTime = ps.endTime
        }
      }).catch(error => {
        console.error('加载车位信息失败:', error)
        this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
      })
    },
    openCarDialog() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.carDialogVisible = true
      this.loadCars()
    },
    searchCars() {
      this.carPagination.page = 1
      this.loadCars()
    },
    loadCars() {
      this.carLoading = true
      const params = {
        page: this.carPagination.page,
        row: this.carPagination.row
      }
      if (this.carQuery.carNum) {
        params.carNumLike = this.carQuery.carNum
      }
      queryOwnerCars(params).then(res => {
        this.carList = res.data || []
        this.carPagination.total = res.total || 0
      }).catch(error => {
        console.error('查询车辆列表失败:', error)
        this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
      }).finally(() => {
        this.carLoading = false
      })
    },
    selectCar(row) {
      this.updateParkingSpaceCarInfo.carId = row.carId || ''
      this.updateParkingSpaceCarInfo.ownerId = row.ownerId || ''
      this.updateParkingSpaceCarInfo.ownerName = row.ownerName || ''
      this.selectedCarNum = row.carNum || ''
      this.carDialogVisible = false
    },
    handleCarPageChange(page) {
      this.carPagination.page = page
      this.loadCars()
    },
    handleCarDialogClose() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.carPagination.total = 0
    },
    goBack() {
      this.$router.go(-1)
    },
    submitUpdate() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.updateParkingSpaceCarInfo.state === 'H') {
          if (this.updateParkingSpaceCarInfo.endTime <= this.updateParkingSpaceCarInfo.startTime) {
            this.$message.error(this.$t('updateParkingSpaceCar.timeError'))
            return
          }
        }
        this.saving = true
        const params = {
          psId: this.updateParkingSpaceCarInfo.psId,
          carId: this.updateParkingSpaceCarInfo.carId,
          ownerId: this.updateParkingSpaceCarInfo.ownerId
        }
        if (this.updateParkingSpaceCarInfo.state === 'H') {
          params.startTime = this.updateParkingSpaceCarInfo.startTime
          params.endTime = this.updateParkingSpaceCarInfo.endTime
        }
        updateParkingSpaceCar(params).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        }).catch(error => {
          console.error('修改车位车辆失败:', error)
          this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
        }).finally(() => {
          this.saving = false
        })
      })
    }
  }
}
</script>

<style scoped>
.w-full {
  width: 100%;
}

.mt-20 {
  margin-top: 20px;
}

.car-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
