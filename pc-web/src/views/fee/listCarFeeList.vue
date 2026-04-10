<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <div>{{ listCarFeeInfo.carNum }}
          <span>{{ $t('listCarFee.fee') }}</span>({{ listCarFeeInfo.parkingName }})
        </div>
        <div class="" style="top:10px;">
          <el-button type="primary" size="small" style="margin-left:10px" @click="_openCarCreateFeeAddModal">
            <i class="el-icon-plus"></i>
            <span>{{ $t('listCarFee.createFee') }}</span>
          </el-button>
          <el-button type="primary" size="small" style="margin-left:10px" @click="_goBack">
            <i class="el-icon-close"></i>
            <span>{{ $t('common.back') }}</span>
          </el-button>
        </div>
      </div>
      <div class="">
        <el-table :data="listCarFeeInfo.fees" border style="width: 100%">
          <el-table-column prop="feeName" :label="$t('listCarFee.feeItem')" align="center">
            <template slot-scope="scope">
              <span class="hand" @click="_viewCarFeeConfig(scope.row)">{{ scope.row.feeName }}
                <i class="el-icon-info"></i>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="feeFlagName" :label="$t('listCarFee.feeFlag')" align="center"></el-table-column>
          <el-table-column prop="feeTypeCdName" :label="$t('listCarFee.feeType')" align="center"></el-table-column>
          <el-table-column prop="amountOwed" :label="$t('listCarFee.amountOwed')" align="center"></el-table-column>
          <el-table-column prop="startTime" :label="$t('listCarFee.startTime')" align="center"></el-table-column>
          <el-table-column :label="$t('listCarFee.endTime')" align="center">
            <template slot-scope="scope">
              {{ _getEndTime(scope.row) }}~<br />{{ _getDeadlineTime(scope.row) }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('listCarFee.remark')" align="center" width="150">
            <template slot-scope="scope">
              <div v-if="scope.row.feeTypeCd == '888800010015' || scope.row.feeTypeCd == '888800010016'">
                <div>
                  <span>{{ $t('listCarFee.preDegrees') }}：</span>{{ scope.row.preDegrees }}
                </div>
                <div>
                  <span>{{ $t('listCarFee.curDegrees') }}：</span>{{ scope.row.curDegrees }}
                </div>
                <div>
                  <span>{{ $t('listCarFee.price') }}：</span>{{ scope.row.mwPrice ? scope.row.mwPrice :
                    scope.row.squarePrice
                  }}
                </div>
                <div>
                  <span>{{ $t('listCarFee.additionalAmount') }}：</span>{{ scope.row.additionalAmount }}
                </div>
              </div>
              <div v-else-if="scope.row.feeTypeCd == '888800010017'">
                <div>
                  <span>{{ $t('listCarFee.algorithm') }}：</span>{{ _getAttrValue(scope.row.feeAttrs, '390005') }}
                </div>
                <div>
                  <span>{{ $t('listCarFee.usage') }}：</span>{{ _getAttrValue(scope.row.feeAttrs, '390003') }}
                </div>
              </div>
              <div v-else>
                <div>
                  <span>{{ $t('listCarFee.price') }}：</span>{{ scope.row.squarePrice }}
                </div>
                <div>
                  <span>{{ $t('listCarFee.additionalAmount') }}：</span>{{ scope.row.additionalAmount }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('listCarFee.state')" align="center"></el-table-column>
          <el-table-column :label="$t('listCarFee.operation')" align="center">
            <template slot-scope="scope">
              <el-button type="text" v-if="scope.row.state != '2009001' && hasPrivilege('502020082314267912')"
                @click="_payFee(scope.row)">{{ $t('listCarFee.pay') }}</el-button>
              <el-button type="text" @click="_payFeeHis(scope.row)">{{ $t('listCarFee.history') }}</el-button>
              <el-button type="text" v-if="hasPrivilege('502020090604200029')" @click="_deleteFee(scope.row)">{{
                $t('listCarFee.cancel') }}</el-button>
              <el-button type="text" v-if="scope.row.state != '2009001' && hasPrivilege('502020090427190001')"
                @click="_editFee(scope.row)">{{ $t('listCarFee.change') }}</el-button>
              <el-button type="text" v-if="scope.row.feeFlag == '4012024' && scope.row.state == '2008001'"
                @click="_splitPayFee(scope.row)">{{ $t('listCarFee.split') }}</el-button>
              <el-button type="text" @click="_viewCarFee(scope.row)">{{ $t('listCarFee.detail') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row>
          <el-col :span="12" class="table-desc">
            <div> {{ $t('listCarFee.note1') }}</div>
            <div> {{ $t('listCarFee.note2') }}</div>
          </el-col>
          <el-col :span="12">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              layout="total, sizes, prev, pager, next, jumper" :total="page.total">
            </el-pagination>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 子组件 -->
    <delete-fee ref="deleteFee" @success="handleSuccess"></delete-fee>
    <edit-fee ref="editFee" @success="handleSuccess"></edit-fee>
    <split-fee ref="splitFee" @success="handleSuccess"></split-fee>
    <car-create-fee-add ref="carCreateFeeAdd" @success="handleSuccess"></car-create-fee-add>
    <!-- <add-meter-water ref="addMeterWater"></add-meter-water> -->
  </div>
</template>

<script>
import { listFee } from '@/api/fee/listCarFeeApi'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import SplitFee from '@/components/fee/splitFee'
import CarCreateFeeAdd from '@/components/fee/carCreateFeeAdd'
//import AddMeterWater from '@/components/fee/addMeterWater'
import { getCommunityId } from '@/api/community/communityApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'ListCarFee',
  components: {
    DeleteFee,
    EditFee,
    SplitFee,
    CarCreateFeeAdd,
    // AddMeterWater
  },
  data() {
    return {
      listCarFeeInfo: {
        fees: [],
        carNum: '',
        carId: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    if (this.$route.query.carNum) {
      this.listCarFeeInfo.carNum = this.$route.query.carNum
      this.listCarFeeInfo.carId = this.$route.query.carId
      this.listCarFeeInfo.areaNum = this.$route.query.areaNum
      this.listCarFeeInfo.num = this.$route.query.num
    }
    let parkingName = this.listCarFeeInfo.areaNum ? `${this.listCarFeeInfo.areaNum}停车场${this.listCarFeeInfo.num}车位` : '无车位'
    this.listCarFeeInfo.parkingName = parkingName
    this._loadlistCarFeeInfo(1, 10)
  },
  methods: {
    handleSuccess() {
      this._loadlistCarFeeInfo(1, 10)
    },
    async _loadlistCarFeeInfo(page, row) {
      const params = {
        page: page,
        row: row,
        communityId: this.communityId,
        payerObjId: this.listCarFeeInfo.carId
      }
      const { fees,total,records } = await listFee(params)
      this.listCarFeeInfo.fees = fees
      this.page.total = total
      this.page.records = records

    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadlistCarFeeInfo(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadlistCarFeeInfo(val, this.page.size)
    },
    _payFee(fee) {
      fee.roomName = this.listCarFeeInfo.carNum
      this.$router.push({ path: '/views/fee/payFeeOrder', query: { feeId: fee.feeId } })
    },
    _payFeeHis(fee) {
      this.$router.push({ path: '/views/fee/propertyFee', query: fee })
    },
    _editFee(fee) {
      this.$refs.editFee.open(fee)
    },
    _deleteFee(fee) {
      this.$refs.deleteFee.open({
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },
    _openCarCreateFeeAddModal() {
      this.$refs.carCreateFeeAdd.open({
        isMore: false,
        car: this.listCarFeeInfo
      })
    },
    _openAddMeterWaterModal() {
      this.$refs.addMeterWater.open({
        roomId: this.listCarFeeInfo.carId,
        roomName: this.listCarFeeInfo.carNum,
        ownerName: this.listCarFeeInfo.parkingName,
        objType: '6666'
      })
    },
    _goBack() {
      this.$router.go(-1)
    },
    _getDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime) || fee.deadlineTime
    },
    _getEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime) || fee.endTime
    },
    _viewCarFeeConfig(fee) {
      // 查看费用项配置逻辑
      console.log(fee)
    },
    _viewCarFee(fee) {
      // 查看费用详情逻辑
      window.open(`/#/views/fee/feeDetail?feeId=${fee.feeId}`)

    },
    _splitPayFee(fee) {
      this.$refs.splitFee.open(fee)
    },
    _getAttrValue(attrs, specCd) {
      const attr = attrs.find(item => item.specCd === specCd)
      return attr ? attr.value : ''
    },
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}
</style>