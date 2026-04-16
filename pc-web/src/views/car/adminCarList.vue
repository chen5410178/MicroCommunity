<template>
  <div class="admin-car-container">
    <el-row :gutter="20">
      <el-col :span="4" class="tree-col">
        <community-parking-tree ref="communityParkingTree" @selectCommunity="handleSelectCommunity" 
          @selectParkingArea="handleSelectParkingArea" @selectLeaseType="handleSelectLeaseType" />
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminCar.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="searchForm.carNumLike" :placeholder="$t('adminCar.search.carNum')" clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.num" :placeholder="$t('adminCar.search.parkingNum')" clearable />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminCar.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="carNum" :label="$t('adminCar.table.carNum')" align="center" />
            <el-table-column prop="memberCarCount" :label="$t('adminCar.table.memberCar')" align="center" />
            <el-table-column prop="roomName" :label="$t('adminCar.table.roomName')" align="center" width="150" />
            <el-table-column :label="$t('adminCar.table.leaseType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.leaseType === 'T' ? $t('adminCar.leaseType.temp') : scope.row.leaseTypeName }}
              </template>
            </el-table-column>
            <el-table-column prop="carTypeName" :label="$t('adminCar.table.carType')" align="center" />
            <el-table-column prop="carColor" :label="$t('adminCar.table.color')" align="center" />
            <el-table-column :label="$t('adminCar.table.owner')" align="center">
              <template slot-scope="scope">
                <el-link type="primary" :href="`/#/views/aCommunity/adminOwnerDetail?ownerId=${scope.row.ownerId}`" target="_blank">
                  {{ scope.row.ownerName }}({{ scope.row.link }})
                </el-link>
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminCar.table.parking')" align="center">
              <template slot-scope="scope">
                {{ scope.row.areaNum && scope.row.state === '1001' ? `${scope.row.areaNum}-${scope.row.num}` : $t('adminCar.parking.released') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminCar.table.validDate')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminCar.table.status')" align="center">
              <template slot-scope="scope">
                {{ getCarState(scope.row) }}
                <span v-if="scope.row.iotStateName">({{ scope.row.iotStateName }})</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('adminCar.table.remark')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleDetail(scope.row)">{{ $t('common.detail') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminOwnerCars } from '@/api/car/adminCarApi'
import CommunityParkingTree from '@/components/car/CommunityParkingTree'

export default {
  name: 'AdminCarList',
  components: {
    CommunityParkingTree
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        paId: '',
        leaseType: '',
        carNumLike: '',
        num: '',
        carTypeCd: '1001'
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    setTimeout(() => {
      this.$refs.communityParkingTree.open()
    }, 1000)
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total, records } = await queryAdminOwnerCars(params)
        this.tableData = data
        this.pagination.total = total
        this.pagination.records = records
      } catch (error) {
        this.$message.error(this.$t('adminCar.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleDetail(row) {
      this.$router.push({ path: `/pages/car/adminCarDetail?memberId=${row.carId}` })
    },
    handleSelectCommunity({ communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.paId = ''
      this.searchForm.leaseType = ''
      this.getList()
    },
    handleSelectParkingArea({ paId }) {
      this.searchForm.communityId = ''
      this.searchForm.paId = paId
      this.searchForm.leaseType = ''
      this.getList()
    },
    handleSelectLeaseType({ leaseType }) {
      this.searchForm.communityId = ''
      this.searchForm.paId = ''
      this.searchForm.leaseType = leaseType
      this.getList()
    },
    getCarState(car) {
      if (car.state === '3003') {
        return this.$t('adminCar.status.expired')
      }
      const endTime = new Date(car.endTime)
      return endTime.getTime() > Date.now() ? this.$t('adminCar.status.normal') : this.$t('adminCar.status.expired')
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-car-container {
  padding: 20px;

  .tree-col {
    padding-right: 0;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>