<template>
  <div class="margin-top">
    <div class="margin-top-lg"></div>
    <div class="margin-top">
      <el-table :data="carDetailTransactionCarInfo.machineTranslates" border style="width: 100%">
        <el-table-column prop="machineTranslateId" :label="$t('carDetailTransactionCar.syncId')"
          align="center"></el-table-column>
        <el-table-column prop="machineCode" :label="$t('carDetailTransactionCar.deviceCode')"
          align="center"></el-table-column>
        <el-table-column prop="typeCdName" :label="$t('carDetailTransactionCar.objectType')"
          align="center"></el-table-column>
        <el-table-column prop="objName" :label="$t('carDetailTransactionCar.objectName')"
          align="center"></el-table-column>
        <el-table-column prop="machineCmdName" :label="$t('carDetailTransactionCar.command')"
          align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailTransactionCar.status')"
          align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailTransactionCar.remark')" align="center"
          width="80px"></el-table-column>
        <el-table-column prop="updateTime" :label="$t('carDetailTransactionCar.syncTime')"
          align="center"></el-table-column>
        <el-table-column :label="$t('carDetailTransactionCar.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openEditMachineTranslateModel(scope.row)">
              {{ $t('carDetailTransactionCar.resync') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

    <edit-machine-translate ref="editMachineTranslate"></edit-machine-translate>
  </div>
</template>

<script>
import { getMachineTranslates } from '@/api/car/carDetailTransactionCarApi'
import EditMachineTranslate from '@/components/property/editMachineTranslate'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailTransactionCar',
  components: {
    EditMachineTranslate
  },
  data() {
    return {
      carDetailTransactionCarInfo: {
        machineTranslates: [],
        ownerId: '',
        carId: '',
        memberId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.carDetailTransactionCarInfo.ownerId = data.ownerId
      this.carDetailTransactionCarInfo.carId = data.carId
      this.carDetailTransactionCarInfo.memberId = data.memberId
      this._loadCarDetailTransactionCarData(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailTransactionCarInfo.carId = data.carId
      this.carDetailTransactionCarInfo.memberId = data.memberId
      this._loadCarDetailTransactionCarData(this.currentPage, this.pageSize)
    },
    _loadCarDetailTransactionCarData(page, row) {
      const params = {
        page,
        row,
        communityId: getCommunityId(),
        objId: this.carDetailTransactionCarInfo.memberId,
        typeCd: '4455'
      }

      getMachineTranslates(params).then(response => {
        this.carDetailTransactionCarInfo.machineTranslates = response.machineTranslates
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadCarDetailTransactionCarData(val, this.pageSize)
    },
    _openEditMachineTranslateModel(machineTranslate) {
      this.$refs.editMachineTranslate.open(machineTranslate)
    }
  }
}
</script>