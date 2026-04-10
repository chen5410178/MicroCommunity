<template>
  <div class="padding">
  <div class="white-bg padding-lg padding-top border-radius">
    <div class="flex justify-between">
      <div class="text-title">{{ $t('contractDetail.title') }}</div>
      <div>
        <el-button type="primary" size="small" style="margin-left:10px" @click="$router.go(-1)">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
        <el-button type="primary" size="small" @click="_printContract">
          <i class="el-icon-printer"></i>{{ $t('common.print') }}
        </el-button>
      </div>
    </div>

    <!-- 合同信息 -->
    <div class="margin-top">
      <el-form class="text-left">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.contractName') + '：'">
              <span>{{ contractDetailInfo.contractName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.contractCode') + '：'">
              <span>{{ contractDetailInfo.contractCode }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.contractType') + '：'">
              <span>{{ contractDetailInfo.contractTypeName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.partyA') + '：'">
              <span>{{ contractDetailInfo.partyA }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.aContacts') + '：'">
              <span>{{ contractDetailInfo.aContacts }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.aLink') + '：'">
              <span>{{ contractDetailInfo.aLink }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.partyB') + '：'">
              <span>{{ contractDetailInfo.partyB }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.bContacts') + '：'">
              <span>{{ contractDetailInfo.bContacts }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.bLink') + '：'">
              <span>{{ contractDetailInfo.bLink }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.operator') + '：'">
              <span>{{ contractDetailInfo.operator }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.operatorLink') + '：'">
              <span>{{ contractDetailInfo.operatorLink }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.amount') + '：'">
              <span>{{ contractDetailInfo.amount }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.startTime') + '：'">
              <span>{{ contractDetailInfo.startTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.endTime') + '：'">
              <span>{{ contractDetailInfo.endTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.signingTime') + '：'">
              <span>{{ contractDetailInfo.signingTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.status') + '：'">
              <span>{{ contractDetailInfo.stateName }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="contractDetailInfo.contractParentId">
          <el-col :span="6">
            <el-form-item :label="$t('contractDetail.parentContract') + '：'">
              <span>{{ contractDetailInfo.contractParentName }}({{ contractDetailInfo.contractParentCode }})</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <divider />
    <div class="margin-top-sm">
      <el-tabs v-model="contractDetailInfo._currentTab" @tab-click="changeTab(contractDetailInfo._currentTab)">
        <el-tab-pane :label="$t('contractDetail.room')" name="contractDetailRoom"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.file')" name="contractDetailFile"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.fee')" name="contractDetailFee"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.hisFee')" name="contractDetailHisFee"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.roomFee')" name="ownerDetailRoomFee"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.hisRoomFee')" name="contractDetailHisRoomFee"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.ownerInfo')" name="contractDetailOwner"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.changeLog')" name="contractDetailChange"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.draftApproval')" name="contractDetailFlow"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.subContract')" name="contractDetailSub"></el-tab-pane>
        <el-tab-pane :label="$t('contractDetail.receipt')" name="contractDetailReceipt"></el-tab-pane>
      </el-tabs>
    </div>

    <component :is="contractDetailInfo._currentTab" :ref="contractDetailInfo._currentTab"
      :contractId="contractDetailInfo.contractId" :contractName="contractDetailInfo.contractName"
      :ownerId="contractDetailInfo.objId"></component>
  </div>
</div>
</template>

<script>
import ContractDetailRoom from '@/components/contract/contractDetailRoom'
import ContractDetailFile from '@/components/contract/contractDetailFile'
import ContractDetailFee from '@/components/contract/contractDetailFee'
import ContractDetailHisFee from '@/components/contract/contractDetailHisFee'
import OwnerDetailRoomFee from '@/components/owner/ownerDetailRoomFee'
import ContractDetailHisRoomFee from '@/components/contract/contractDetailHisRoomFee'
import ContractDetailChange from '@/components/contract/contractDetailChange'
import ContractDetailFlow from '@/components/contract/contractDetailFlow'
import ContractDetailSub from '@/components/contract/contractDetailSub'
import ContractDetailOwner from '@/components/contract/contractDetailOwner'
import ContractDetailReceipt from '@/components/contract/contractDetailReceipt'
import { queryContract } from '@/api/contract/contractDetailApi'
import Divider from '@/components/system/divider'

export default {
  name: 'ContractDetail',
  components: {
    ContractDetailRoom,
    ContractDetailFile,
    ContractDetailFee,
    ContractDetailHisFee,
    OwnerDetailRoomFee,
    ContractDetailHisRoomFee,
    ContractDetailChange,
    ContractDetailFlow,
    ContractDetailSub,
    ContractDetailOwner,
    ContractDetailReceipt,
    Divider
  },
  data() {
    return {
      contractDetailInfo: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractType: '',
        contractTypeName: '',
        partyA: '',
        partyB: '',
        aContacts: '',
        bContacts: '',
        aLink: '',
        bLink: '',
        operator: '',
        operatorLink: '',
        amount: '',
        startTime: '',
        endTime: '',
        signingTime: '',
        param: '',
        planType: '',
        stateName: '',
        contractParentId: '',
        contractParentName: '',
        contractParentCode: '',
        objId: '',
        files: [],
        _currentTab: 'contractDetailRoom'
      }
    }
  },
  created() {
    this.contractDetailInfo.contractId = this.$route.query.contractId
    if (!this.contractDetailInfo.contractId) {
      return
    }
    const currentTab = this.$route.query.currentTab
    if (currentTab) {
      this.contractDetailInfo._currentTab = currentTab
    }
    this._loadContractInfo()
  },
  methods: {
    async _loadContractInfo() {
      try {
        const res = await queryContract({
          page: 1,
          row: 1,
          contractId: this.contractDetailInfo.contractId
        })
        const contract = res.data[0]
        Object.assign(this.contractDetailInfo, contract)
        this.changeTab(this.contractDetailInfo._currentTab)
      } catch (error) {
        console.error('Failed to load contract info:', error)
      }
    },
    changeTab(tab) {
      this.contractDetailInfo._currentTab = tab
      setTimeout(() => {
        this.$refs[tab].open({
          contractId: this.contractDetailInfo.contractId,
          contractName: this.contractDetailInfo.contractName,
          ownerId: this.contractDetailInfo.objId
        })
      }, 500)
    },
    _printContract() {
      const contract = this.contractDetailInfo
      window.open(`/#/views/contract/printContract?contractTypeId=${contract.contractType}&contractId=${contract.contractId}`)
    }
  }
}
</script>

<style scoped>
.white-bg {
  background-color: #fff;
}

.padding-lg {
  padding: 20px;
}

.padding-top {
  padding-top: 20px;
}

.border-radius {
  border-radius: 4px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.vc-line-primary {
  border-top: 1px solid #409EFF;
}
</style>