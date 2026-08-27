<template>
  <div class="simplify-parking-space-fee">
    <el-row :gutter="20" class="toolbar">
      <el-col :span="5">
        <el-select v-model="simplifyParkingSpaceFeeInfo.carId" @change="changeSimplifyParkingSpace" style="width:100%">
          <el-option v-for="(item, index) in simplifyParkingSpaceFeeInfo.ownerCars" :key="index" :label="item.carNum"
            :value="item.carId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="simplifyParkingSpaceFeeInfo.feeTypeCd" @change="changeSimplifyParkingSpaceFeeTypeCd"
          style="width:100%" :placeholder="$t('simplifyParkingSpaceFee.selectFeeType')">
          <el-option v-for="item in filteredFeeTypeCds" :key="item.statusCd" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="simplifyParkingSpaceFeeInfo.configId" @change="changeSimplifyParkingSpace"
          style="width:100%" :placeholder="$t('simplifyParkingSpaceFee.selectFeeItem')">
          <el-option v-for="item in simplifyParkingSpaceFeeInfo.feeConfigs" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="3" v-if="simplifyParkingSpaceFeeInfo.psId || simplifyParkingSpaceFeeInfo.ownerId">
        <el-select v-model="simplifyParkingSpaceFeeInfo.state" @change="changeSimplifyParkingSpace" style="width:100%">
          <el-option :label="$t('simplifyParkingSpaceFee.selectStatus')" value=""></el-option>
          <el-option :label="$t('simplifyParkingSpaceFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyParkingSpaceFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2" v-if="simplifyParkingSpaceFeeInfo.psId || simplifyParkingSpaceFeeInfo.ownerId">
        <el-button type="primary" size="small" @click="handleQuery">
          {{ $t('common.query') }}
        </el-button>
      </el-col>
      <el-col :span="6" class="text-right" v-if="simplifyParkingSpaceFeeInfo.psId || simplifyParkingSpaceFeeInfo.ownerId">
        <el-button type="primary" size="small" @click="_openBatchPayParkingSpaceFeeModal">
          {{ $t('simplifyRoomFee.batchPayment') }}
        </el-button>
        <el-button v-if="hasPrivilege('502020080570210194')" type="primary" size="small" @click="_toPrePayFeeOrder">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyRoomFee.prePayFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003') && simplifyParkingSpaceFeeInfo.from === 'simplifyAcceptance'"
          type="primary" size="small" @click="_openSimplpifyParkingSpaceTempFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyParkingSpaceFee.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyParkingSpaceFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyParkingSpaceFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyParkingSpaceFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyParkingSpaceFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyParkingSpaceFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyParkingSpaceFee.remark') }}</th>
            <th class="text-center">{{ $t('simplifyParkingSpaceFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyParkingSpaceFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyParkingSpaceFeeInfo.fees">
            <tr :key="'fee-' + index">
              <td class="text-center" style="color:#999;" @click="switchSimplifyParkingSpaceFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="_viewParkingSpaceFeeConfig(fee)">
                {{ fee.feeName }}
                <span class="fa fa-info-circle"></span>
              </td>
              <td class="text-center" @click="switchSimplifyParkingSpaceFeeFlag(fee)">{{ fee.feeFlagName }}</td>
              <td class="text-center" @click="switchSimplifyParkingSpaceFeeFlag(fee)">{{ fee.feeTypeCdName }}</td>
              <td class="text-center" @click="switchSimplifyParkingSpaceFeeFlag(fee)">{{ fee.oweAmount }}</td>
              <td class="text-center" @click="switchSimplifyParkingSpaceFeeFlag(fee)">{{ dateFormat(fee.startTime) }}<br />~{{
                dateFormat(fee.endTime) }}</td>
              <td class="text-center multiline-text">-</td>
              <td class="text-center">-</td>
              <td class="text-center"></td>
            </tr>
            <template v-if="fee.showFlag == 'Y'">
              <tr v-for="(feeSub, subIndex) in fee.childs" :key="'feeSub-' + index + '-' + subIndex"
                style="background-color:#f9f9f9;">
                <td class="text-center hand"></td>
                <td class="text-center hand">{{ feeSub.feeName }}</td>
                <td class="text-center">{{ feeSub.feeFlagName }}</td>
                <td class="text-center">{{ feeSub.feeTypeCdName }}</td>
                <td class="text-center">{{ feeSub.oweAmount }}</td>
                <td class="text-center">{{ dateFormat(feeSub.startTime) }}<br />~{{ dateFormat(feeSub.endTime) }}</td>
                <td class="text-center multiline-text">{{ feeSub.remark || '-' }}</td>
                <td class="text-center">{{ feeSub.stateName }}</td>
                <td class="text-center">
                  <el-button class="btn-link" type="text"
                    v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')"
                    @click="_simplifyParkingSpacePayFee(feeSub)">{{ $t('simplifyParkingSpaceFee.pay') }}</el-button>
                  <el-button class="btn-link" type="text" @click="_simplifyParkingSpacePayFeeHis(feeSub)">{{
                    $t('simplifyParkingSpaceFee.payHistory') }}</el-button>
                  <el-dropdown
                    v-if="hasPrivilege('502020090604200029') || hasPrivilege('502021070488970005') || hasPrivilege('502020090427190001')">
                    <span class="el-dropdown-link">{{ $t('simplifyParkingSpaceFee.more') }}<i
                        class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="hasPrivilege('502020090604200029')"
                        @click.native="_simplifyParkingSpaceDeleteFee(feeSub)">{{ $t('simplifyParkingSpaceFee.cancelFee')
                        }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')"
                        @click.native="_simplifyParkingSpaceEditFee(feeSub)">{{ $t('simplifyParkingSpaceFee.feeChange')
                        }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')"
                        @click.native="_splitParkingSpaceFee(feeSub)">{{ $t('simplifyParkingSpaceFee.splitFee') }}</el-dropdown-item>
                      <el-dropdown-item>
                        <a target="_blank" :href="'/#/pages/fee/feeDetail?feeId=' + feeSub.feeId">{{
                          $t('simplifyParkingSpaceFee.detail') }}</a>
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

    <el-row class="margin-top">
      <el-col :span="12" class="text-left table-desc">
        <div>{{ $t('simplifyParkingSpaceFee.note1') }}</div>
        <div>{{ $t('simplifyParkingSpaceFee.note2') }}</div>
      </el-col>
      <el-col :span="6">
        <span>
          <span>{{ $t('simplifyParkingSpaceFee.oweSubtotal') }}</span>{{ simplifyParkingSpaceFeeInfo.totalAmount }}
        </span>
      </el-col>
    </el-row>
    <div class="text-right" style="margin-top: 20px">
      <el-pagination :current-page.sync="currentPage" :page-size="pageSize" :total="total"
        layout="total, prev, pager, next" @current-change="handlePageChange" />
    </div>


    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
    <split-fee ref="splitFee" @success="handleNotify"></split-fee>
  </div>
</template>

<script>
import { getCommunityId, getDict } from '@/api/community/communityApi'
import { queryOwnerCars, listFeeConfigs } from '@/api/fee/parkingSpaceCreateFeeApi'
import { listFee } from '@/api/fee/feeApi'
import { dateFormat } from '@/utils/dateUtil'
import EditFee from '@/components/fee/editFee'
import DeleteFee from '@/components/fee/deleteFee'
import SplitFee from '@/components/fee/splitFee'

export default {
  name: 'SimplifyParkingSpaceFee',
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
      simplifyParkingSpaceFeeInfo: {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        psId: '',
        psName: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        state: '2008001',
        feeTypeCd: '',
        feeTypeCds: [],
        configId: '',
        feeConfigs: [],
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
          this.switchParkingSpaceFee()
        }
      }
    },
    carId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.simplifyParkingSpaceFeeInfo.carId = newVal
        }
      }
    }
  },
  computed: {
    filteredFeeTypeCds() {
      return this.simplifyParkingSpaceFeeInfo.feeTypeCds.filter(item => item.statusCd !== '888800010008')
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(param) {
      this.clearSimplifyParkingSpaceFeeInfo()
      Object.assign(this.simplifyParkingSpaceFeeInfo, param)
      this.switchParkingSpaceFee()
      getDict('pay_fee_config', 'fee_type_cd').then(data => {
        this.simplifyParkingSpaceFeeInfo.feeTypeCds = data
      }).catch(error => {
        console.error('Failed to fetch fee type dict:', error)
      })
    },
    async switchParkingSpaceFee() {
      await this._listParkingSpaceCar()
      await this._listSimplifyParkingSpaceFee(this.currentPage, this.pageSize)
    },

    clearSimplifyParkingSpaceFeeInfo() {
      const feeConfigs = this.simplifyParkingSpaceFeeInfo.feeConfigs
      this.simplifyParkingSpaceFeeInfo = {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        psId: '',
        psName: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        state: '2008001',
        feeTypeCd: '',
        feeTypeCds: [],
        configId: '',
        feeConfigs: feeConfigs,
        totalAmount: 0.0,
        from: ''
      }
    },

    async _listSimplifyParkingSpaceFee(page, row) {
      if (!this.simplifyParkingSpaceFeeInfo.psId && !this.simplifyParkingSpaceFeeInfo.ownerId) return

      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          ownerId: this.simplifyParkingSpaceFeeInfo.ownerId,
          payerObjType: "6666",
          state: this.simplifyParkingSpaceFeeInfo.state,
          feeTypeCd: this.simplifyParkingSpaceFeeInfo.feeTypeCd,
          configId: this.simplifyParkingSpaceFeeInfo.configId
        }
        if (this.simplifyParkingSpaceFeeInfo.psId) {
          params.payerObjId = this.simplifyParkingSpaceFeeInfo.psId
        }

        const response = await listFee(params)
        this.total = response.total || 0
        this.simplifyParkingSpaceFeeInfo.records = response.records || 0

        let totalAmount = 0.0
        response.fees.forEach(item => {
          totalAmount += parseFloat(item.oweAmount || 0)
        })
        this.simplifyParkingSpaceFeeInfo.totalAmount = totalAmount.toFixed(2)

        const mergedFees = this.simplifyParkingSpaceFeeMergeFees(response.fees || [])
        this.simplifyParkingSpaceFeeInfo.fees = mergedFees
      } catch (error) {
        console.error('获取车辆费用失败:', error)
        this.$message.error('获取车辆费用失败')
      }
    },

    async _listParkingSpaceCar() {
      try {
        const params = {
          page: 1,
          row: 50,
          ownerId: this.simplifyParkingSpaceFeeInfo.ownerId,
          carId: this.simplifyParkingSpaceFeeInfo.carId,
          carTypeCd: '1001',
          communityId: this.communityId
        }

        const response = await queryOwnerCars(params)
        this.simplifyParkingSpaceFeeInfo.ownerCars = response.data || []

        if (this.simplifyParkingSpaceFeeInfo.ownerCars.length > 0) {
          if (!this.simplifyParkingSpaceFeeInfo.carId) {
            this.simplifyParkingSpaceFeeInfo.carId = this.simplifyParkingSpaceFeeInfo.ownerCars[0].carId
            this.simplifyParkingSpaceFeeInfo.carNum = this.simplifyParkingSpaceFeeInfo.ownerCars[0].carNum
            this.simplifyParkingSpaceFeeInfo.num = this.simplifyParkingSpaceFeeInfo.ownerCars[0].num
            this.simplifyParkingSpaceFeeInfo.parkingName = this.simplifyParkingSpaceFeeInfo.ownerCars[0].num
              ? this.simplifyParkingSpaceFeeInfo.ownerCars[0].areaNum + '停车场' + this.simplifyParkingSpaceFeeInfo.ownerCars[0].num + '停车位'
              : (this.simplifyParkingSpaceFeeInfo.ownerCars[0].areaNum ? this.simplifyParkingSpaceFeeInfo.ownerCars[0].areaNum + '停车场' : '')
          }
        }
      } catch (error) {
        console.error('获取车主车辆失败:', error)
      }
    },

    changeSimplifyParkingSpace() {
      const car = this.simplifyParkingSpaceFeeInfo.ownerCars.find(item => item.carId === this.simplifyParkingSpaceFeeInfo.carId)
      if (car) {
        this.simplifyParkingSpaceFeeInfo.carNum = car.carNum
        this.simplifyParkingSpaceFeeInfo.num = car.num
        this.simplifyParkingSpaceFeeInfo.parkingName = car.num
          ? car.areaNum + '停车场' + car.num + '停车位'
          : (car.areaNum ? car.areaNum + '停车场' : '')
      }
      this._listSimplifyParkingSpaceFee(this.currentPage, this.pageSize)
    },

    changeSimplifyParkingSpaceFeeTypeCd(feeTypeCd) {
      this.simplifyParkingSpaceFeeInfo.configId = ''

      const params = {
        page: 1,
        row: 50,
        communityId: this.communityId,
        feeTypeCd: feeTypeCd,
        isDefault: '',
        valid: '1'
      }

      listFeeConfigs(params).then(res => {
        this.simplifyParkingSpaceFeeInfo.feeConfigs = res.feeConfigs
      }).catch(error => {
        console.error('Failed to fetch fee configs:', error)
      })
    },

    handleQuery() {
      this.currentPage = 1
      this._listSimplifyParkingSpaceFee(1, this.pageSize)
    },

    switchSimplifyParkingSpaceFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
      this.$forceUpdate()
    },

    simplifyParkingSpaceFeeMergeFees(fees) {
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

    dateFormat,

    handlePageChange(page) {
      this.currentPage = page
      this._listSimplifyParkingSpaceFee(page, this.pageSize)
    },

    _simplifyParkingSpacePayFee(fee) {
      fee.roomName = this.simplifyParkingSpaceFeeInfo.psName || this.simplifyParkingSpaceFeeInfo.carNum
      this.$router.push(`/views/fee/payFeeOrder?feeId=${fee.feeId}`)
    },

    _simplifyParkingSpacePayFeeHis(fee) {
      this.$router.push(`/views/fee/propertyFee?${this.objToGetParam(fee)}`)
    },

    _simplifyParkingSpaceEditFee(fee) {
      this.$refs.editFee.open(fee)
    },

    _simplifyParkingSpaceDeleteFee(fee) {
      this.$refs.deleteFee.open({
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },

    _splitParkingSpaceFee(fee) {
      this.$refs.splitFee.open(fee)
    },

    _toPrePayFeeOrder() {
      const payerObjId = this.simplifyParkingSpaceFeeInfo.psId || this.simplifyParkingSpaceFeeInfo.carId
      this.$router.push(`/views/fee/prePayFeeOrder?payerObjId=${payerObjId}&payerObjType=6666`)
    },

    _openSimplpifyParkingSpaceTempFeeModal() {
      this.$emit('tempFee', {
        roomId: this.simplifyParkingSpaceFeeInfo.psId || this.simplifyParkingSpaceFeeInfo.carId,
        roomName: this.simplifyParkingSpaceFeeInfo.psName || this.simplifyParkingSpaceFeeInfo.carNum,
        ownerName: this.simplifyParkingSpaceFeeInfo.name,
        objType: '6666'
      })
    },

    _openBatchPayParkingSpaceFeeModal() {
      const psId = this.simplifyParkingSpaceFeeInfo.psId || ''
      this.$router.push(`/views/fee/batchPayFeeOrder?ownerId=${this.simplifyParkingSpaceFeeInfo.ownerId}&payerObjType=6666&payerObjId=${psId}`)
    },

    _viewParkingSpaceFeeConfig(fee) {
      this.$emit('viewConfig', fee)
    },

    handleNotify() {
      this._listSimplifyParkingSpaceFee(this.currentPage, this.pageSize)
    },

    objToGetParam(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    }
  }
}
</script>

<style lang="scss" scoped>
.simplify-parking-space-fee {
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
    background-color: #fff !important;
    /* 白底表头 */
    color: #606266 !important;
    /* Element 默认文字色 */
    font-weight: 500;
    /* 更接近 el-table 的字重 */
    border-bottom: 1px solid #ebeef5 !important;
    height: 48px;
    /* 提高表头高度 */
    line-height: 48px;
    /* 垂直居中 */
  }

  ::v-deep .el-table tbody td {
    border-top: none;
    /* 避免形成双线 */
    border-bottom: 1px solid #ebeef5;
    /* 行分隔线与 el-table 接近 */
  }

  ::v-deep .el-table {
    border: 1px solid #ebeef5;
    /* 外框单线 */
    border-collapse: collapse;
    /* 合并边框，避免双线 */
  }

  .text-right {
    text-align: right;
  }

  .text-center {
    text-align: center;
  }
}
</style>