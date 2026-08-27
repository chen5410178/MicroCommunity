<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div v-if="simplifyOwnerCarInfo.ownerId">
        <el-button type="primary" size="small" @click="_addOwnerCar">
          <i class="el-icon-plus"></i>{{ $t('simplifyOwnerCar.addCar') }}
        </el-button>
      </div>
    </div>

    <el-table
      :data="simplifyOwnerCarInfo.ownerCars"
      style="width: 100%; margin-top: 10px"
      border>
      <el-table-column :label="$t('simplifyOwnerCar.plateNumber')" align="center">
        <template #default="{row}">
          <router-link :to="`/views/car/carDetail?memberId=${row.memberId}`" target="_blank">
            {{row.carNum}}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="carTypeName" :label="$t('simplifyOwnerCar.carType')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerCar.owner')" align="center">
        <template #default="{row}">
          {{row.contact || row.ownerName}}({{row.tel || row.link}})
        </template>
      </el-table-column>
      <el-table-column :label="$t('simplifyOwnerCar.parkingSpace')" align="center">
        <template #default="{row}">
          <span v-if="row.areaNum">
            {{row.areaNum}}
          </span>
          <span v-else>{{ $t('simplifyOwnerCar.none') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" :label="$t('simplifyOwnerCar.startTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('simplifyOwnerCar.endTime')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyOwnerCar.status')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerCar.operation')" align="center">
        <template #default="{row}">
          <el-button type="text" @click="_openEditOwnerCar(row)" v-if="hasPrivilege('502023032804261679')">
            {{ $t('simplifyOwnerCar.edit') }}
          </el-button>
          <el-button type="text" @click="_openDelOwnerCarModel(row)" v-if="hasPrivilege('502023032822121680')">
            {{ $t('simplifyOwnerCar.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      class="text-right"
      style="margin-top: 20px">
    </el-pagination>

    <edit-car ref="editCar" @listOwnerCarData="listSimplifyOwnerCar"></edit-car>
    <delete-owner-car ref="deleteOwnerCar" @listOwnerCarData="listSimplifyOwnerCar"></delete-owner-car>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwnerCars } from '@/api/simplify/simplifyOwnerCarApi'
import EditCar from '@/components/car/editCar'
import DeleteOwnerCar from '@/components/car/deleteOwnerCar'

export default {
  name: 'SimplifyOwnerCar',
  components: {
    EditCar,
    DeleteOwnerCar
  },
  data() {
    return {
      simplifyOwnerCarInfo: {
        ownerCars: [],
        ownerId: '',
        ownerName: '',
        total: 0,
        records: 1
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      if (param.ownerId == '') return
      this.clearSimplifyOwnerCarInfo()
      Object.assign(this.simplifyOwnerCarInfo, param)
      this.listSimplifyOwnerCar(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listSimplifyOwnerCar(val, this.pageSize)
    },
    listSimplifyOwnerCar(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        ownerId: this.simplifyOwnerCarInfo.ownerId,
        carTypeCd: '1001'
      }
      
      queryOwnerCars(params).then(res => {
        this.simplifyOwnerCarInfo.ownerCars = res.data
        this.total = res.total
      })
    },
    _addOwnerCar() {
      this.$router.push(`/views/car/hireParkingSpace?ownerId=${this.simplifyOwnerCarInfo.ownerId}&ownerName=${this.simplifyOwnerCarInfo.ownerName}`)
    },
    _openEditOwnerCar(car) {
      this.$refs.editCar.open(car)
    },
    _openDelOwnerCarModel(car) {
      this.$refs.deleteOwnerCar.open(car)
    },
    clearSimplifyOwnerCarInfo() {
      this.simplifyOwnerCarInfo = {
        ownerCars: [],
        ownerId: '',
        ownerName: ''
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 10px;
}
.text-right {
  text-align: right;
}
</style>