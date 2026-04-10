<template>
  <div class="print-equipment-account-label-container">
    <div>
      <div class="row">
        <div class="col-sm-4">
          <span>{{ $t('printEquipmentAccount.machineId') }}：{{ printEquipmentAccountInfo.machineId }}</span>
        </div>
        <div class="col-sm-6">
          <span></span>
        </div>
        <div class="col-sm-2">
          <span>{{ $t('printEquipmentAccount.printTime') }}：{{ nowTime }}</span>
        </div>
      </div>

      <table class="table table-bordered margin-top" style="margin-bottom: 0;">
        <thead>
          <tr>
            <th scope="col" colspan="6" class="text-center">
              <span style="font-size: 40px;">{{ $t('printEquipmentAccount.equipmentLabel') }}</span>
            </th>
          </tr>
        </thead>
        <tbody style="font-size: 28px;">
          <tr>
            <td>{{ $t('printEquipmentAccount.project') }}：{{ printEquipmentAccountInfo.yqName }}</td>
            <td>{{ $t('printEquipmentAccount.locationName') }}：{{ printEquipmentAccountInfo.locationDetail }}</td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.machineName') }}：{{ printEquipmentAccountInfo.machineName }}</td>
            <td>{{ $t('printEquipmentAccount.importanceLevel') }}：{{ printEquipmentAccountInfo.levelName }}</td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.systemName') }}：{{ printEquipmentAccountInfo.sysName }}</td>
            <td>{{ $t('printEquipmentAccount.enableDate') }}：{{ printEquipmentAccountInfo.firstEnableTime }}</td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.machineBrand') }}：{{ printEquipmentAccountInfo.brand }}</td>
            <td rowspan="4">
              <div id="qrcode" style="width:200px; height:200px;"></div>
            </td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.specificationModel') }}：{{ printEquipmentAccountInfo.model }}</td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.code') }}：{{ printEquipmentAccountInfo.machineCode }}</td>
          </tr>
          <tr>
            <td>{{ $t('printEquipmentAccount.responsiblePerson') }}：{{ printEquipmentAccountInfo.chargeUseName }}</td>
          </tr>
          <tr>
            <td colspan="2" class="text-center">设备标志 严禁损坏</td>
          </tr>
        </tbody>
      </table>

      <div id="print-btn" class="btn-row">
        <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv">
          <i class="el-icon-check"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right: 20px;" @click="_closePage">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getEquipmentAccountList } from '@/api/resource/printEquipmentAccountLabelApi'
import QRCode from 'qrcodejs2'
import { getCommunityId ,getCommunityName} from '@/api/community/communityApi'

export default {
  name: 'PrintEquipmentAccountLabelList',
  data() {
    return {
      printEquipmentAccountInfo: {
        machineId: '',
        machineName: '',
        machineCode: '',
        brand: '',
        model: '',
        locationDetail: '',
        firstEnableTime: '',
        warrantyDeadline: '',
        usefulLife: '',
        importanceLevel: '',
        levelName: '',
        state: '',
        purchasePrice: '',
        netWorth: '',
        useOrgId: '',
        useOrgName: '',
        useUserId: '',
        useUserName: '',
        useUseTel: '',
        chargeOrgId: '',
        chargeOrgName: '',
        chargeOrgTel: '',
        chargeUseId: '',
        chargeUseName: '',
        remark: '',
        yqName: '',
        url: ''
      },
      printFlag: '0',
      nowTime: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initPrintRepairDetailDateInfo()
    const myDate = new Date()
    this.nowTime = myDate.toLocaleDateString()
    this.printEquipmentAccountInfo.yqName = getCommunityName()
  },
  methods: {
    async _initPrintRepairDetailDateInfo() {
      const machineId = this.$route.query.machineId
      try {
        const res = await getEquipmentAccountList({
          page: 1,
          row: 1,
          machineId: machineId,
          communityId: this.communityId
        })
        const data = res.data[0]
        Object.assign(this.printEquipmentAccountInfo, data)

        document.getElementById('qrcode').innerHTML = ''
        new QRCode(document.getElementById('qrcode'), {
          text: this.printEquipmentAccountInfo.url,
          width: 200,
          height: 200,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _printPurchaseApplyDiv() {
      this.printFlag = '1'
      document.getElementById('print-btn').style.display = 'none'
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    }
  }
}
</script>

<style scoped>
.print-equipment-account-label-container {
  padding: 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}
.col-sm-4 { flex: 0 0 33.3333%; max-width: 33.3333%; }
.col-sm-6 { flex: 0 0 50%; max-width: 50%; }
.col-sm-2 { flex: 0 0 16.6667%; max-width: 16.6667%; }

.table {
  width: 100%;
  border-collapse: collapse;
}
.table-bordered {
  border: 1px solid #ebeef5;
}
.table-bordered th,
.table-bordered td {
  border: 1px solid #ebeef5;
  padding: 12px 10px;
}
.text-center { text-align: center; }
.margin-top { margin-top: 20px; }

.float-right {
  float: right;
}

.margin-top {
  margin-top: 20px;
}

.btn-row { margin-top: 20px; }

@media print {
  #print-btn {
    display: none !important;
  }
}
</style>