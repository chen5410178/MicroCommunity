<template>
  <div class="print-repair-detail-container">
      <el-row :gutter="20" style="width:100%">
        <el-col :span="8">
          <span>{{ $t('printRepairDetail.repairId') }}</span>{{ printRepairDetailInfo.repairId }}
        </el-col>
        <el-col :span="8">
          <span></span>
        </el-col>
        <el-col :span="8">
          <span>{{ $t('printRepairDetail.printTime') }}</span>{{ nowTime }}
        </el-col>
      </el-row>

      <table border="1" class="margin-top" style="margin-bottom: 0;width:100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th colspan="6" style="text-align: center;">{{ $t('printRepairDetail.repairInfo') }}</th>
          </tr>
          <tr>
            <th>{{ $t('printRepairDetail.repairName') }}</th>
            <th>{{ $t('printRepairDetail.tel') }}</th>
            <th>{{ $t('printRepairDetail.location') }}</th>
            <th>{{ $t('printRepairDetail.context') }}</th>
            <th v-if="printRepairDetailInfo.maintenanceType">{{ $t('printRepairDetail.maintenanceType') }}</th>
            <th v-if="printRepairDetailInfo.maintenanceType === '1001'">{{ $t('printRepairDetail.repairMaterials') }}</th>
            <th v-if="printRepairDetailInfo.maintenanceType === '1001'" colspan="3">{{ $t('printRepairDetail.repairFee') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ printRepairDetailInfo.repairName }}</td>
            <td>{{ printRepairDetailInfo.tel }}</td>
            <td>{{ printRepairDetailInfo.location }}</td>
            <td>{{ printRepairDetailInfo.context }}</td>
            <td v-if="printRepairDetailInfo.maintenanceType">{{ printRepairDetailInfo.maintenanceType }}</td>
            <td v-if="printRepairDetailInfo.maintenanceType === '1001'">{{ printRepairDetailInfo.repairMaterials }}</td>
            <td v-if="printRepairDetailInfo.maintenanceType === '1001'" colspan="3">{{ printRepairDetailInfo.repairFee }}</td>
          </tr>
        </tbody>
      </table>

      <table border="1" style="width:100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th colspan="6" style="text-align: center;">{{ $t('printRepairDetail.workflow') }}</th>
          </tr>
          <tr>
            <th style="text-align: center;">{{ $t('printRepairDetail.serialNumber') }}</th>
            <th style="text-align: center;">{{ $t('printRepairDetail.handler') }}</th>
            <th style="text-align: center;">{{ $t('printRepairDetail.status') }}</th>
            <th style="text-align: center;">{{ $t('printRepairDetail.processTime') }}</th>
            <th style="text-align: center;">{{ $t('printRepairDetail.timeConsuming') }}</th>
            <th style="text-align: center;">{{ $t('printRepairDetail.comment') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in printRepairDetailInfo.repairUsers" :key="index">
            <td style="text-align: center;">{{ index + 1 }}</td>
            <td style="text-align: center;">{{ item.staffName }}</td>
            <td style="text-align: center;">{{ item.stateName }}</td>
            <td style="text-align: center;">{{ item.startTime }}</td>
            <td style="text-align: center;">{{ item.duration }}</td>
            <td style="text-align: center;">{{ item.context }}</td>
          </tr>
          <tr>
            <td colspan="6">{{ printRepairDetailInfo.description }}</td>
          </tr>
          <tr>
            <td colspan="2" style="height:60px; text-align: center;">{{ $t('printRepairDetail.repairerSign') }}</td>
            <td colspan="2" style="height:60px; text-align: center;">{{ $t('printRepairDetail.customerSign') }}</td>
          </tr>
        </tbody>
      </table>

      <div id="print-btn" style="margin-top:20px">
        <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv">
          {{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px" @click="_closePage">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerRepairs, listRepairStaffs } from '@/api/work/printRepairDetailApi'

export default {
  name: 'PrintRepairDetailList',
  data() {
    return {
      printRepairDetailInfo: {
        repairId: '',
        repairTypeName: '',
        appointmentTime: '',
        repairName: '',
        tel: '',
        repairObjName: '',
        context: '',
        repairUsers: [],
        maintenanceType: '',
        repairMaterials: '',
        repairFee: '',
        description: ''
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
  },
  methods: {
    _initPrintRepairDetailDateInfo() {
      const _repairId = this.$route.query.repairId
      const _repairType = this.$route.query.repairType
      const param = {
        page: 1,
        row: 1,
        repairId: _repairId,
        communityId: this.communityId,
        repairType: _repairType
      }

      listOwnerRepairs(param)
        .then(response => {
          const _repairDetailInfo = response.data[0]
          Object.assign(this.printRepairDetailInfo, _repairDetailInfo)
          this._loadRepairUser()
        })
        .catch(error => {
          console.error('请求失败处理:', error)
        })
    },
    _loadRepairUser() {
      const param = {
        page: 1,
        row: 100,
        communityId: this.communityId,
        repairId: this.printRepairDetailInfo.repairId
      }

      listRepairStaffs(param)
        .then(response => {
          this.printRepairDetailInfo.repairUsers = response.data
        })
        .catch(error => {
          console.error('请求失败处理:', error)
        })
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

<style lang="scss" scoped>
.print-repair-detail-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .float-right {
    float: right;
  }

  table {
    width: 100%;
    border: 1px solid #ebeef5;
    th, td {
      padding: 12px 0;
      border: 1px solid #ebeef5;
    }
  }
}
</style>