<template>
  <div class="simplify-car-fee">
    <el-row :gutter="20" class="toolbar">
      <el-col :span="6">
        <el-select v-model="simplifyCarFeeInfo.carId" @change="changeSimplifyCar" style="width:100%">
          <el-option v-for="(item, index) in simplifyCarFeeInfo.ownerCars" :key="index" :label="item.carNum"
            :value="item.carId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="3" v-if="simplifyCarFeeInfo.carId">
        <el-select v-model="simplifyCarFeeInfo.state" @change="changeSimplifyCar" style="width:100%">
          <el-option :label="$t('simplifyCarFee.selectStatus')" value=""></el-option>
          <el-option :label="$t('simplifyCarFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyCarFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="15" class="text-right" v-if="simplifyCarFeeInfo.carId">
        <el-button type="primary" size="small" @click="_toBuyCarMonthCard">
          {{ $t('simplifyCarFee.buyMonthCard') }}
        </el-button>
        <el-button type="primary" size="small" @click="_openBatchPayCarFeeModal">
          {{ $t('simplifyRoomFee.batchPayment') }}
        </el-button>
        <el-button v-if="hasPrivilege('502020080570210194')" type="primary" size="small" @click="_toPrePayFeeOrder">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyRoomFee.prePayFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003') && simplifyCarFeeInfo.from === 'simplifyAcceptance'"
          type="primary" size="small" @click="_openSimplpifyCarTempFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyCarFee.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyCarFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyCarFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyCarFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyCarFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyCarFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyCarFee.remark') }}</th>
            <th class="text-center">{{ $t('simplifyCarFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyCarFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyCarFeeInfo.fees">
            <tr :key="'fee-'+index">
              <td class="text-center" style="color:#999;" @click="switchSimplifyCarFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="_viewCarFeeConfig(fee)">
                {{ fee.feeName }}
                <span class="fa fa-info-circle"></span>
              </td>
              <td class="text-center" @click="switchSimplifyCarFeeFlag(fee)">{{ fee.feeFlagName }}</td>
              <td class="text-center" @click="switchSimplifyCarFeeFlag(fee)">{{ fee.feeTypeCdName }}</td>
              <td class="text-center" @click="switchSimplifyCarFeeFlag(fee)">{{ fee.oweAmount }}</td>
              <td class="text-center" @click="switchSimplifyCarFeeFlag(fee)">{{ formatDate(fee.startTime) }}<br/>~{{ formatDate(fee.endTime) }}</td>
              <td class="text-center multiline-text">-</td>
              <td class="text-center">-</td>
              <td class="text-center"></td>
            </tr>
            <template v-if="fee.showFlag == 'Y'">
              <tr v-for="(feeSub, subIndex) in fee.childs" :key="'feeSub-'+index+'-'+subIndex" style="background-color:#f9f9f9;">
                <td class="text-center hand"></td>
                <td class="text-center hand">{{ feeSub.feeName }}</td>
                <td class="text-center">{{ feeSub.feeFlagName }}</td>
                <td class="text-center">{{ feeSub.feeTypeCdName }}</td>
                <td class="text-center">{{ feeSub.oweAmount }}</td>
                <td class="text-center">{{ formatDate(feeSub.startTime) }}<br/>~{{ formatDate(feeSub.endTime) }}</td>
                <td class="text-center multiline-text">{{ feeSub.remark || '-' }}</td>
                <td class="text-center">{{ feeSub.stateName }}</td>
                <td class="text-center">
                  <el-button class="btn-link" type="text" v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')" @click="_simplifyCarPayFee(feeSub)">{{ $t('simplifyCarFee.pay') }}</el-button>
                  <el-button class="btn-link" type="text" @click="_simplifyCarPayFeeHis(feeSub)">{{ $t('simplifyCarFee.payHistory') }}</el-button>
                  <el-dropdown v-if="hasPrivilege('502020090604200029') || hasPrivilege('502021070488970005') || hasPrivilege('502020090427190001')">
                    <span class="el-dropdown-link">{{ $t('simplifyCarFee.more') }}<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="hasPrivilege('502020090604200029')" @click.native="_simplifyCarDeleteFee(feeSub)">{{ $t('simplifyCarFee.cancelFee') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="_simplifyCarEditFee(feeSub)">{{ $t('simplifyCarFee.feeChange') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="_splitCarFee(feeSub)">{{ $t('simplifyCarFee.splitFee') }}</el-dropdown-item>
                      <el-dropdown-item>
                        <a target="_blank" :href="'/#/pages/fee/feeDetail?feeId=' + feeSub.feeId">{{ $t('simplifyCarFee.detail') }}</a>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>
    </div>

    <el-row class="footer-info">
      <el-col :span="12">
        <div class="notice">
          <div>{{ $t('simplifyCarFee.note1') }}</div>
          <div>{{ $t('simplifyCarFee.note2') }}</div>
        </div>
      </el-col>
      <el-col :span="4" class="text-center">
        <span>{{ $t('simplifyCarFee.oweSubtotal') }}：{{ simplifyCarFeeInfo.totalAmount }}</span>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-pagination :current-page.sync="currentPage" :page-size="pageSize" :total="total"
          layout="total, prev, pager, next" @current-change="handlePageChange" />
      </el-col>
    </el-row>

    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
    <split-fee ref="splitFee" @success="handleNotify"></split-fee>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwnerCars } from '@/api/fee/carCreateFeeApi'
import { listFee } from '@/api/fee/feeApi'
import { formatDate } from '@/utils/dateUtil'
import EditFee from '@/components/fee/editFee'
import DeleteFee from '@/components/fee/deleteFee'
import SplitFee from '@/components/fee/splitFee'

export default {
  name: 'SimplifyCarFee',
  components: {
    EditFee,
    DeleteFee,
    SplitFee
  },  
  data() {
    return {
      ownerId: '',
      carId: '',
      carNum: '',
      ownerName: '',
      communityId: '',
      currentPage: 1,
      pageSize: 100,
      total: 0,
      simplifyCarFeeInfo: {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        state: '2008001',
        totalAmount: 0.0,
        from: ''
      }
    }
  },
  watch: {
    ownerId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.switchCarFee()
        }
      }
    },
    carId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.simplifyCarFeeInfo.carId = newVal
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(param) {
      this.clearSimplifyCarFeeInfo()
      Object.assign(this.simplifyCarFeeInfo, param)
      this.switchCarFee()
    },
    async switchCarFee() {
      await this._listOwnerCar()
      await this._listSimplifyCarFee(this.currentPage, this.pageSize)
    },

    clearSimplifyCarFeeInfo() {
      this.simplifyCarFeeInfo = {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        state: '2008001',
        totalAmount: 0.0,
        from: ''
      }
    },

    async _listSimplifyCarFee(page, row) {
      if (!this.simplifyCarFeeInfo.carId) return

      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          ownerId: this.simplifyCarFeeInfo.ownerId,
          payerObjType: "6666",
          state: this.simplifyCarFeeInfo.state
        }

        const response = await listFee(params)
        this.total = response.total || 0
        this.simplifyCarFeeInfo.records = response.records || 0

        let totalAmount = 0.0
        response.fees.forEach(item => {
          totalAmount += parseFloat(item.oweAmount || 0)
        })
        this.simplifyCarFeeInfo.totalAmount = totalAmount.toFixed(2)

        const mergedFees = this.simplifyCarFeeMergeFees(response.fees || [])
        this.simplifyCarFeeInfo.fees = mergedFees
      } catch (error) {
        console.error('获取车辆费用失败:', error)
        this.$message.error('获取车辆费用失败')
      }
    },

    async _listOwnerCar() {
      try {
        const params = {
          page: 1,
          row: 50,
          ownerId: this.simplifyCarFeeInfo.ownerId,
          carId: this.simplifyCarFeeInfo.carId,
          carTypeCd: '1001',
          communityId: this.communityId
        }

        const response = await queryOwnerCars(params)
        this.simplifyCarFeeInfo.ownerCars = response.data || []

        if (this.simplifyCarFeeInfo.ownerCars.length > 0) {
          if (!this.simplifyCarFeeInfo.carId) {
            this.simplifyCarFeeInfo.carId = this.simplifyCarFeeInfo.ownerCars[0].carId
            this.simplifyCarFeeInfo.carNum = this.simplifyCarFeeInfo.ownerCars[0].carNum
            this.simplifyCarFeeInfo.num = this.simplifyCarFeeInfo.ownerCars[0].num
            this.simplifyCarFeeInfo.parkingName = this.simplifyCarFeeInfo.ownerCars[0].areaNum + '停车场' + this.simplifyCarFeeInfo.ownerCars[0].num + '停车位'
          }
        }
      } catch (error) {
        console.error('获取车主车辆失败:', error)
      }
    },

    changeSimplifyCar() {
      const car = this.simplifyCarFeeInfo.ownerCars.find(item => item.carId === this.simplifyCarFeeInfo.carId)
      if (!car) return

      this.simplifyCarFeeInfo.carNum = car.carNum
      this.simplifyCarFeeInfo.num = car.num
      this.simplifyCarFeeInfo.parkingName = car.areaNum + '停车场' + car.num + '停车位'
      this._listSimplifyCarFee(this.currentPage, this.pageSize)
    },

    switchSimplifyCarFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
      this.$forceUpdate()
    },

    simplifyCarFeeMergeFees(fees) {
      const mergedMap = new Map()

      fees.forEach(fee => {
        const key = `${fee.feeName}|${fee.payerObjName}`
        if (!mergedMap.has(key)) {
          mergedMap.set(key, {
            feeName: fee.feeName,
            configId: fee.configId,
            payerObjName: fee.payerObjName,
            feeFlagName: fee.feeFlagName,
            feeTypeCdName: fee.feeTypeCdName,
            oweAmount: fee.oweAmount,
            startTime: fee.startTime,
            endTime: fee.endTime,
            remark: fee.remark,
            stateName: fee.stateName,
            showFlag: 'N',
            childs: [fee]
          })
        } else {
          const mergedItem = mergedMap.get(key)
          mergedItem.oweAmount = (parseFloat(mergedItem.oweAmount) + parseFloat(fee.oweAmount)).toFixed(4)
          if (new Date(fee.startTime) < new Date(mergedItem.startTime)) {
            mergedItem.startTime = fee.startTime
          }
          if (new Date(fee.endTime) > new Date(mergedItem.endTime)) {
            mergedItem.endTime = fee.endTime
          }
          mergedItem.childs.push(fee)
        }
      })

      const mergedFees = Array.from(mergedMap.values())
      if (mergedFees.length > 0) {
        mergedFees[0].showFlag = 'Y'
      }
      return mergedFees
    },

    formatDate,

    handlePageChange(page) {
      this.currentPage = page
      this._listSimplifyCarFee(page, this.pageSize)
    },

    _simplifyCarPayFee(fee) {
      fee.roomName = this.simplifyCarFeeInfo.carNum
      this.$router.push(`/views/fee/payFeeOrder?feeId=${fee.feeId}`)
    },

    _simplifyCarPayFeeHis(fee) {
      this.$router.push(`/views/fee/propertyFee?${this.objToGetParam(fee)}`)
    },

    _simplifyCarEditFee(fee) {
      this.$refs.editFee.open(fee)
    },

    _simplifyCarDeleteFee(fee) {
      this.$refs.deleteFee.open({
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },

    _splitCarFee(fee) {
      this.$refs.splitFee.open(fee)
    },

    _toBuyCarMonthCard() {
      this.$router.push(`/views/fee/buyCarMonthCard?carNum=${this.simplifyCarFeeInfo.carNum}`)
    },

    _toPrePayFeeOrder() {
      this.$router.push(`/views/fee/prePayFeeOrder?payerObjId=${this.simplifyCarFeeInfo.carId}&payerObjType=6666`)
    },

    _openSimplpifyCarTempFeeModal() {
      this.$emit('tempFee', {
        roomId: this.simplifyCarFeeInfo.carId,
        roomName: this.simplifyCarFeeInfo.carNum,
        ownerName: this.simplifyCarFeeInfo.name,
        objType: '6666'
      })
    },

    _openBatchPayCarFeeModal() {
      this.$router.push(`/views/fee/batchPayFeeOrder?ownerId=${this.simplifyCarFeeInfo.ownerId}&payerObjType=6666`)
    },

    _viewCarFeeConfig(fee) {
      this.$emit('viewConfig', fee)
    },

    handleNotify() {
      this._listSimplifyCarFee(this.currentPage, this.pageSize)
    },

    objToGetParam(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    }
  }
}
</script>

<style lang="scss" scoped>
.simplify-car-fee {
  .toolbar {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 15px;
  }

  .hand {
    cursor: pointer;
  }

  .text-center {
    text-align: center;
  }

  .multiline-text {
    white-space: pre-wrap;
  }

  /* table UI 优化：贴近 el-table 风格 */
  ::v-deep .el-table thead th {
    background-color: #fff !important;     /* 白底表头 */
    color: #606266 !important;            /* Element 默认文字色 */
    font-weight: 500;                      /* 更接近 el-table 的字重 */
    border-bottom: 1px solid #ebeef5 !important;
    height: 48px;                          /* 提高表头高度 */
    line-height: 48px;                     /* 垂直居中 */
  }

  ::v-deep .el-table tbody td {
    border-top: none;                      /* 避免形成双线 */
    border-bottom: 1px solid #ebeef5;      /* 行分隔线与 el-table 接近 */
  }

  ::v-deep .el-table {
    border: 1px solid #ebeef5;             /* 外框单线 */
    border-collapse: collapse;              /* 合并边框，避免双线 */
  }

  .footer-info {
    margin-top: 20px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .notice {
      color: #666;
      font-size: 12px;
    }
  }

  .text-right {
    text-align: right;
  }

  .text-center {
    text-align: center;
  }
}
</style>