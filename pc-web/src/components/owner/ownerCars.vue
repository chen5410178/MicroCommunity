<template>
  <el-dialog :visible.sync="dialogVisible" :title="$t('ownerCars.title')" width="80%"  @close="handleClose">
    <el-row>
      <el-col :span="24">
        <div class="table-container">
          <el-table :data="ownerCarsInfo.cars" border stripe>
            <el-table-column prop="carNum" :label="$t('ownerCars.licensePlate')" align="center" />
            <el-table-column :label="$t('ownerCars.licenseType')" align="center">
              <template #default="{ row }">
                <span v-if="row.leaseType === 'T'">{{ $t('ownerCars.temporary') }}</span>
                <span v-else>{{ row.leaseTypeName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="carTypeName" :label="$t('ownerCars.carType')" align="center" />
            <el-table-column prop="carColor" :label="$t('ownerCars.color')" align="center" />
            <el-table-column :label="$t('ownerCars.owner')" align="center">
              <template #default="{ row }">
                {{ row.ownerName }}<br>({{ row.link }})
              </template>
            </el-table-column>
            <el-table-column :label="$t('ownerCars.parkingSpace')" align="center">
              <template #default="{ row }">
                <div v-if="row.areaNum && row.state === '1001'">
                  {{ row.areaNum }}{{ $t('ownerCars.park') }}{{ row.num }}{{ $t('ownerCars.space') }}
                </div>
                <div v-else>{{ $t('ownerCars.released') }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('ownerCars.validity')" align="center">
              <template #default="{ row }">
                <div v-if="row.leaseType === 'H'">
                  {{ row.startTime }}<br>~{{ row.endTime }}
                </div>
                <div v-else>-</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-pagination background layout="prev, pager, next, sizes, total" :total="totalCount" :page-size="pageSize"
          :current-page="currentPage" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { queryOwnerCars } from '@/api/room/ownerCarsApi'

export default {
  name: 'OwnerCars',
  data() {
    return {
      dialogVisible: false,
      ownerCarsInfo: {
        cars: [],
        ownerId: ''
      },
      totalCount: 0,
      pageSize: 10,
      currentPage: 1
    }
  },
  methods: {
    open(ownerId) {
      this.ownerCarsInfo.ownerId = ownerId
      this.dialogVisible = true
      this._loadOwnerCarInfo(1, this.pageSize)
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSizeChange(size) {
      this.pageSize = size
      this._loadOwnerCarInfo(this.currentPage, size)
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this._loadOwnerCarInfo(page, this.pageSize)
    },
    async _loadOwnerCarInfo(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: this.getCommunityId(),
          ownerId: this.ownerCarsInfo.ownerId
        }

        const response = await queryOwnerCars(params)
        this.ownerCarsInfo.cars = response.data
        this.totalCount = response.total
        this.currentPage = page
      } catch (error) {
        console.error('加载业主车辆信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.table-container {
  margin-top: 0px;
}
</style>