<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="4">
        <el-select v-model="simplifyOwnerTransactionCarInfo.carId" @change="changeTransactionCar">
          <el-option
            v-for="(item,index) in simplifyOwnerTransactionCarInfo.ownerCars"
            :key="index"
            :label="item.carNum"
            :value="item.carId"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="20"></el-col>
    </el-row>
    <div>
      <el-table
        :data="simplifyOwnerTransactionCarInfo.machineTranslates"
        style="margin-top:10px"
        border
      >
        <el-table-column prop="machineTranslateId" :label="$t('simplifyOwnerTransactionCar.syncId')" align="center"></el-table-column>
        <el-table-column prop="typeCdName" :label="$t('simplifyOwnerTransactionCar.objectType')" align="center"></el-table-column>
        <el-table-column prop="objName" :label="$t('simplifyOwnerTransactionCar.objectName')" align="center"></el-table-column>
        <el-table-column prop="machineCmdName" :label="$t('simplifyOwnerTransactionCar.command')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('simplifyOwnerTransactionCar.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('simplifyOwnerTransactionCar.remark')" align="center" width="80px"></el-table-column>
        <el-table-column prop="updateTime" :label="$t('simplifyOwnerTransactionCar.syncTime')" align="center"></el-table-column>
        <!-- <el-table-column :label="$t('simplifyOwnerTransactionCar.operation')" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="_openEditCarTranslateModel(scope.row)"
            >{{$t('simplifyOwnerTransactionCar.resync')}}</el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
        class="text-right"
        style="margin-top: 20px"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import { listMachineTranslates, queryOwnerCars } from '@/api/simplify/simplifyOwnerTransactionCarApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SimplifyOwnerTransactionCar',
  data() {
    return {
      simplifyOwnerTransactionCarInfo: {
        machineTranslates: [],
        ownerId: '',
        carId: '',
        ownerCars: []
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(params) {
      if (!params.ownerId) return
      this.clearSimplifyOwnerTransactionCarInfo()
      Object.assign(this.simplifyOwnerTransactionCarInfo, params)
      this.listTransactionOwnerCar().then(() => {
        this.listMachineTranslates()
      })
    },
    async listMachineTranslates() {
      if(!this.simplifyOwnerTransactionCarInfo.carId) return
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          communityId: this.communityId,
          objId: this.simplifyOwnerTransactionCarInfo.carId,
          typeCd: '4455'
        }
        const res = await listMachineTranslates(params)
        this.simplifyOwnerTransactionCarInfo.machineTranslates = res.machineTranslates
        this.total = res.total
      } catch (error) {
        console.error('Failed to load machine translates:', error)
      }
    },
    async listTransactionOwnerCar() {
      try {
        const params = {
          page: 1,
          row: 50,
          ownerId: this.simplifyOwnerTransactionCarInfo.ownerId,
          communityId: this.communityId
        }
        const res = await queryOwnerCars(params)
        this.simplifyOwnerTransactionCarInfo.ownerCars = res.data
        if (res.data.length > 0) {
          this.simplifyOwnerTransactionCarInfo.carId = res.data[0].carId
        }
      } catch (error) {
        console.error('Failed to load owner cars:', error)
      }
    },
    changeTransactionCar() {
      const car = this.simplifyOwnerTransactionCarInfo.ownerCars.find(
        item => item.carId === this.simplifyOwnerTransactionCarInfo.carId
      )
      if (!car) return
      this.listMachineTranslates()
    },
    clearSimplifyOwnerTransactionCarInfo() {
      this.simplifyOwnerTransactionCarInfo = {
        machineTranslates: [],
        ownerId: '',
        carId: '',
        ownerCars: []
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listMachineTranslates()
    },
    _openEditCarTranslateModel(machineTranslate) {
      this.$emit('edit-machine-translate', machineTranslate)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.text-right {
  text-align: right;
}
</style>