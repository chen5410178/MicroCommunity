<template>
  <div class="contract-change-detail-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractChangeDetail.contractInfo') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="openSelectContractInfoModel">
            <i class="el-icon-search"></i>
            {{ $t('contractChangeDetail.selectContract') }}
          </el-button>
          <el-button type="primary" size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <el-form>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.contractName')">
              <span>{{ contractChangeDetailInfo.contractName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.contractCode')">
              <span>{{ contractChangeDetailInfo.contractCode }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.contractType')">
              <span>{{ contractChangeDetailInfo.contractTypeName }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.partyA')">
              <span>{{ contractChangeDetailInfo.partyA }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.aContacts')">
              <span>{{ contractChangeDetailInfo.aContacts }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.aLink')">
              <span>{{ contractChangeDetailInfo.aLink }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.partyB')">
              <span>{{ contractChangeDetailInfo.partyB }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.bContacts')">
              <span>{{ contractChangeDetailInfo.bContacts }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.bLink')">
              <span>{{ contractChangeDetailInfo.bLink }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.operator')">
              <span>{{ contractChangeDetailInfo.operator }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.operatorLink')">
              <span>{{ contractChangeDetailInfo.operatorLink }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.amount')">
              <span>{{ contractChangeDetailInfo.amount }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.startTime')">
              <span>{{ contractChangeDetailInfo.startTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.endTime')">
              <span>{{ contractChangeDetailInfo.endTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contractChangeDetail.signingTime')">
              <span>{{ contractChangeDetailInfo.signingTime }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 主体变更 -->
    <contract-change-main-body v-if="contractChangeDetailInfo.param === 'contractChangeMainBody'"
      ref="contractChangeMainBody" @changeMainBody="handlerMainBody" />

    <!-- 租期调整 -->
    <contract-change-lease v-show="contractChangeDetailInfo.param === 'contractChangeLease'" ref="contractChangeLease"
      @changeNotify="handlerMainBody" />

    <!-- 资产调整 -->
    <contract-change-assets v-show="contractChangeDetailInfo.param === 'contractChangeAssets'"
      ref="contractChangeAssets" @changeNotify="handlerMainBody" />

    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractChangeDetail.changeRemark') }}</span>
      </div>
      <el-form>
        <el-form-item :label="$t('contractChangeDetail.changeRemark')">
          <el-input v-model="newContract.changeRemark" type="textarea" :rows="5"
            :placeholder="$t('contractChangeDetail.changeRemarkPlaceholder')"></el-input>
        </el-form-item>
      </el-form>
    </el-card>

    <purchase-approvers v-if="contractChangeDetailInfo.audit === '1001'" ref="purchaseApprovers"
      :call-back-listener="contractChangeDetail" :call-back-function="notify3" flow-type="60006" />

    <el-row>
      <el-col :span="22">
        <div class="submit-btn-wrapper">
          <el-button type="primary" @click="submitChangeContract">
            {{ $t('common.submit') }}
          </el-button>
        </div>
      </el-col>
    </el-row>

    <choose-contract ref="chooseContract" :emitChooseContract="contractChangeDetailInfo"
      @chooseContract="chooseContract" />
  </div>
</template>

<script>
import { saveContractChangePlan } from '@/api/contract/contractChangeDetailApi'
import ContractChangeMainBody from '@/components/contract/ContractChangeMainBody'
import ContractChangeLease from '@/components/contract/ContractChangeLease'
import ContractChangeAssets from '@/components/contract/ContractChangeAssets'
import PurchaseApprovers from '@/components/contract/purchaseApprovers'
import ChooseContract from '@/components/contract/ChooseContract'
import { getCommunityId } from '@/api/community/communityApi'
import { queryContractRoom } from '@/api/contract/addContractApi'


export default {
  name: 'ContractChangeDetailList',
  components: {
    ContractChangeMainBody,
    ContractChangeLease,
    ContractChangeAssets,
    PurchaseApprovers,
    ChooseContract
  },
  data() {
    return {
      contractChangeDetailInfo: {
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
        rooms: [],
        audit: '',
        staffName: '',
        nextUserId: ''
      },
      newContract: {
        changeRemark: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    const param = this.$route.query.param
    this.contractChangeDetailInfo.param = param
  },
  methods: {
    openSelectContractInfoModel() {
      this.$refs.chooseContract.open()
    },
    submitChangeContract() {
      if (!this.validateForm()) {
        return
      }

      this.newContract.nextUserId = this.contractChangeDetailInfo.nextUserId
      this.newContract.staffName = this.contractChangeDetailInfo.staffName

      saveContractChangePlan(this.newContract)
        .then(response => {
          if (response.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.goBack()
          } else {
            this.$message.error(response.msg)
          }
        })
        .catch(error => {
          console.error('Error submitting contract change:', error)
          this.$message.error(this.$t('common.submitFailed'))
        })
    },
    validateForm() {
      // 这里添加表单验证逻辑
      if (!this.newContract.changeRemark) {
        this.$message.error(this.$t('contractChangeDetail.changeRemarkRequired'))
        return false
      }
      return true
    },
    goBack() {
      this.$router.go(-1)
    },
    handlerMainBody(item) {
     // const changeRemark = this.newContract.changeRemark
      this.newContract = JSON.parse(JSON.stringify(this.contractChangeDetailInfo))
     // this.newContract.changeRemark = changeRemark
      if (item['rooms']) {
        this.newContract.rooms = item.rooms
      }
      Object.assign(this.newContract, item)
    },
    notify3(info) {
      this.contractChangeDetailInfo.nextUserId = info.staffId
      this.contractChangeDetailInfo.staffName = info.staffName
    },
    chooseContract(contract) {
      this.contractChangeDetailInfo.contractId = contract.contractId
      this.contractChangeDetailInfo.contractName = contract.contractName
      this.contractChangeDetailInfo.contractCode = contract.contractCode
      this.contractChangeDetailInfo.contractType = contract.contractType
      this.contractChangeDetailInfo.contractTypeName = contract.contractTypeName
      this.contractChangeDetailInfo.partyA = contract.partyA
      this.contractChangeDetailInfo.partyB = contract.partyB
      this.contractChangeDetailInfo.aContacts = contract.aContacts
      this.contractChangeDetailInfo.bContacts = contract.bContacts
      this.contractChangeDetailInfo.aLink = contract.aLink
      this.contractChangeDetailInfo.bLink = contract.bLink
      this.contractChangeDetailInfo.operator = contract.operator
      this.contractChangeDetailInfo.operatorLink = contract.operatorLink
      this.contractChangeDetailInfo.amount = contract.amount
      this.contractChangeDetailInfo.startTime = contract.startTime
      this.contractChangeDetailInfo.endTime = contract.endTime
      this.contractChangeDetailInfo.signingTime = contract.signingTime
      if(this.$refs.contractChangeAssets){
        this.$refs.contractChangeAssets.open(this.contractChangeDetailInfo)
      }
      this.loadContractRooms()
    },
    async loadContractRooms() {
      const res = await queryContractRoom({ contractId: this.contractChangeDetailInfo.contractId, page: 1, row: 500 })
      this.contractChangeDetailInfo.rooms = res.data
    },
  }
}
</script>

<style lang="scss" scoped>
.contract-change-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .submit-btn-wrapper {
    text-align: right;
    margin-top: 20px;
    margin-bottom: 20px;
  }

  .el-form-item {
    margin-bottom: 0;
  }
}
</style>