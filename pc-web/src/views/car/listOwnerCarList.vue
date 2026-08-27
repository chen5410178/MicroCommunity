<template>
  <div class="list-owner-car-container">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li v-for="(item, index) in leaseTypes" :key="index" @click="switchLeaseType(item)"
                :class="{ 'selected': conditions.leaseType === item.statusCd }"
                class="parking-area-item">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="21" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('listOwnerCar.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="conditions.carNumLike" :placeholder="$t('listOwnerCar.inputCarNum')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.valid" :placeholder="$t('listOwnerCar.selectParkingSpaceStatus')" clearable>
                <el-option value="1" :label="$t('listOwnerCar.normal')"></el-option>
                <el-option value="3" :label="$t('listOwnerCar.expired')"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="conditions.ownerName" :placeholder="$t('listOwnerCar.inputOwnerName')" clearable />
            </el-col>
 
            <el-col :span="4">
              <el-input v-model.trim="conditions.link" :placeholder="$t('listOwnerCar.inputContact')" clearable />
            </el-col>
            <el-col :span="4" class="text-left">
              <el-button type="primary" @click="queryMethod" icon="el-icon-search">
                {{ $t('listOwnerCar.query') }}
              </el-button>
              <el-button @click="resetMethod" icon="el-icon-refresh">
                {{ $t('listOwnerCar.reset') }}
              </el-button>
            </el-col>
            <el-col :span="4" class="margin-top">
              <el-input v-model.trim="conditions.memberCarNumLike" :placeholder="$t('listOwnerCar.inputMemberCarNum')"
                clearable />
            </el-col>
           
          </el-row>
        </el-card>

        <el-card class="mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('listOwnerCar.carInfo') }}</span>
            <div class="header-buttons">
              <el-button v-if="hasPrivilege('502023032813991677')" type="primary" size="small" @click="addOwnerCar">
                {{ $t('listOwnerCar.add') }}
              </el-button>
              <el-button size="small" @click="openOwnerCarImport">
                <i class="el-icon-upload2"></i> {{ $t('listOwnerCar.importCar') }}
              </el-button>
              <el-button size="small" @click="exportExcel">
                <i class="el-icon-download"></i> {{ $t('listOwnerCar.export') }}
              </el-button>
            </div>
          </div>

          <el-table :data="ownerCars" border v-loading="loading">
            <el-table-column prop="carNum" :label="$t('listOwnerCar.carNum')" align="center" />
            <el-table-column prop="memberCarCount" :label="$t('listOwnerCar.memberCar')" align="center">
              <template slot-scope="scope">
                <a href="javascript:void(0)" @click="toCarMember(scope.row)">
                  {{ scope.row.memberCarCount || 0 }}
                </a>
              </template>
            </el-table-column>
            <el-table-column prop="roomName" :label="$t('listOwnerCar.roomNum')" align="center" width="150" />
            <el-table-column :label="$t('listOwnerCar.carPlateType')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.leaseType === 'T'">
                  {{ $t('listOwnerCar.temporaryCar') }}
                </span>
                <span v-else>{{ scope.row.leaseTypeName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="carTypeName" :label="$t('listOwnerCar.carType')" align="center" />
            <el-table-column :label="$t('listOwnerCar.owner')" align="center">
              <template slot-scope="scope">
                <router-link :to="`/views/owner/ownerDetail?ownerId=${scope.row.ownerId}`" target="_blank">
                  {{ scope.row.ownerName }}({{ scope.row.link }})
                </router-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('listOwnerCar.parkingArea')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.areaNum">{{ scope.row.areaNum }}</span>
                <span v-else>--</span>
                <span v-if="scope.row.psNum">（{{ scope.row.psNum }}）</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('listOwnerCar.validity')" align="center">
              <template slot-scope="scope">
                <div v-if="scope.row.leaseType === 'H' || scope.row.leaseType === 'S'">
                  {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
                </div>
                <div v-else>--</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('listOwnerCar.status')" align="center">
              <template slot-scope="scope">
                {{ getCarState(scope.row) }}
                <span v-if="scope.row.iotStateName">
                  (<a href="javascript:void(0)" @click="viewIotStateRemark(scope.row)">{{ scope.row.iotStateName }}</a>)
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('listOwnerCar.remark')" align="center" />
            <el-table-column :label="$t('listOwnerCar.operation')" align="center" width="300">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="toCarDetail(scope.row)">
                  {{ $t('listOwnerCar.detail') }}
                </el-button>
                <el-button v-if="scope.row.leaseType === 'H'" size="mini" @click="toBuyCarMonthCard(scope.row)">
                  {{ $t('listOwnerCar.buyMonthlyCard') }}
                </el-button>
                <el-button v-if="scope.row.carTypeCd !== '1003' && hasPrivilege('502023032804261679')" size="mini"
                  @click="openEditOwnerCar(scope.row)">
                  {{ $t('listOwnerCar.modify') }}
                </el-button>
                <el-button size="mini" type="primary" v-if="scope.row.carTypeCd !== '1003' && hasPrivilege('502023032804261679')"  @click="openChangeOwnerDialog(scope.row)">
                  {{ $t('listOwnerCar.changeOwner') }}
                </el-button>
                <el-button v-if="hasPrivilege('502023032822121680')"   size="mini" type="danger"
                  @click="openDelOwnerCarModel(scope.row)">
                  {{ $t('listOwnerCar.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="mt-20" :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <import-owner-car ref="importOwnerCar" @success="listOwnerCarData" />
    <edit-car ref="editCar" @success="listOwnerCarData" />
    <delete-owner-car ref="deleteOwnerCar" @success="listOwnerCarData" />
    <change-owner-dialog ref="changeOwnerDialog" @success="listOwnerCarData" />
  </div>
</template>

<script>
import { queryOwnerCars, exportData } from '@/api/car/listOwnerCarApi'
import ImportOwnerCar from '@/components/car/importOwnerCar'
import EditCar from '@/components/car/editCar'
import DeleteOwnerCar from '@/components/car/deleteOwnerCar'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ChangeOwnerDialog from '@/components/car/changeOwnerDialog'


export default {
  name: 'ListOwnerCar',
  components: { ImportOwnerCar, EditCar, DeleteOwnerCar, ChangeOwnerDialog },
  data() {
    return {
      loading: false,
      ownerCars: [],
      leaseTypes: [],
      conditions: {
        carNumLike: '',
        valid: '',
        leaseType: 'H',
        ownerName: '',
        link: '',
        memberCarNumLike: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
    this.listOwnerCarData()
  },
  methods: {
    async initData() {
      try {
        const leaseTypes = await getDict('owner_car', 'lease_type')
        //车牌类型仅保留 月租车（H）、内部车（I）、免费车（NM）
        const filterLeaseTypes = leaseTypes.filter(item => ['H', 'I', 'NM'].indexOf(item.statusCd) > -1)
        this.leaseTypes = [{ statusCd: '', name: this.$t('listOwnerCar.allCars') }, ...filterLeaseTypes,
        { statusCd: 'expireCar', name: this.$t('listOwnerCar.expiredCars') }]
      } catch (error) {
        console.error('获取租赁类型失败:', error)
      }
    },

    async listOwnerCarData() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.currentPage,
          row: this.pageSize,
          communityId: this.communityId
        }

        if (this.conditions.leaseType === 'expireCar') {
          params.leaseType = ''
          params.valid = 3
        }

        const response = await queryOwnerCars(params)
        this.ownerCars = response.data
        this.total = response.total
      } catch (error) {
        console.error('获取车辆列表失败:', error)
        this.$message.error(this.$t('listOwnerCar.fetchError'))
      } finally {
        this.loading = false
      }
    },

    switchLeaseType(item) {
      this.conditions.leaseType = item.statusCd
      this.currentPage = 1
      this.listOwnerCarData()
    },

    queryMethod() {
      this.currentPage = 1
      this.listOwnerCarData()
    },

    resetMethod() {
      this.conditions = {
        carNumLike: '',
        valid: '',
        leaseType: 'H',
        ownerName: '',
        link: '',
        memberCarNumLike: ''
      }
      this.currentPage = 1
      this.listOwnerCarData()
    },

    handleSizeChange(val) {
      this.pageSize = val
      this.listOwnerCarData()
    },

    handleCurrentChange(val) {
      this.currentPage = val
      this.listOwnerCarData()
    },

    addOwnerCar() {
      this.$router.push('/views/car/hireParkingSpace')
    },

    openEditOwnerCar(car) {
      this.$refs.editCar.open(car)
    },

    openDelOwnerCarModel(car) {
      this.$refs.deleteOwnerCar.open(car)
    },

    toBuyCarMonthCard(car) {
      this.$router.push(`/views/fee/buyCarMonthCard?carNum=${car.carNum}`)
    },

    toCarMember(car) {
      this.$router.push(`/views/car/carDetail?memberId=${car.carId}&tab=carDetailMember`)
    },

    toCarDetail(car) {
      window.open(`/views/car/carDetail?memberId=${car.memberId}`)
    },
    openChangeOwnerDialog(car) {
      this.$refs.changeOwnerDialog.open(car)
    },

    getCarState(car) {
      if (car.state === '3003') {
        return this.$t('listOwnerCar.expired')
      }
      const endTime = new Date(car.endTime)
      if (endTime > new Date()) {
        return this.$t('listOwnerCar.normal')
      }
      return this.$t('listOwnerCar.expired')
    },

    openOwnerCarImport() {
      this.$refs.importOwnerCar.open()
    },

    async exportExcel() {
      try {
        const params = {
          ...this.conditions,
          communityId: this.communityId,
          pagePath: 'exportOwnerCar'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(error.message || this.$t('listOwnerCar.exportError'))
      }
    },

    viewIotStateRemark(car) {
      this.$alert(car.iotRemark, `${car.carNum} ${this.$t('listOwnerCar.iotSyncDetail')}`, {
        confirmButtonText: this.$t('common.confirm')
      })
    },

  }
}
</script>

<style lang="scss" scoped>
.list-owner-car-container {
  padding: 20px;

  .right-panel {
    padding-left: 5px;
  }

  .left-panel {
    .parking-area-card {
      height: 700px;

      .parking-area-list-wrapper {
        height: 630px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .parking-area-list {
          list-style: none;
          padding: 0 5px;
          margin: 0;

          .parking-area-item {
            padding: 10px;
            text-align: center;
            cursor: pointer;
            border-radius: 4px;
            margin-bottom: 5px;
            transition: all 0.3s;
            font-size: 14px;
            color: #606266;

            &:hover {
              background-color: #f5f7fa;
            }

            &.selected {
              background-color: #409eff;
              color: white;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .header-buttons {
    float: right;
  }

  .mt-10 {
    margin-top: 10px;
  }

  .mt-20 {
    margin-top: 20px;
  }

  .text-right {
    text-align: right;
  }

  .el-table .expired-row {
    background-color: #fef0f0;
  }
}
</style>