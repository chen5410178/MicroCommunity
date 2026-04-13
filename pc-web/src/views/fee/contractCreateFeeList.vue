<template>
  <div class="contract-create-fee-container">
    <div class="main-card">
      <el-card v-if="contractCreateFeeInfo.contracts.length === 0" class="empty-page">
        <i class="el-icon-document"></i>
        <span>请先添加合同</span>
      </el-card>
      <el-row v-else :gutter="20">
        <el-col :span="4" class="left-panel">
          <el-card class="contract-list-card">
            <div class="contract-list-wrapper">
              <ul class="contract-list">
                <li v-for="(item, index) in contractCreateFeeInfo.contracts" :key="index" class="contract-item"
                  :class="{ 'selected': contractCreateFeeInfo.contractId === item.contractId }" @click="switchContract(item)">
                  {{ item.contractCode }}({{ item.contractName }})--{{ item.bContacts }}
                </li>
              </ul>
            </div>
          </el-card>
        </el-col>

        <el-col :span="20" class="right-panel">
          <el-card>
            <div slot="header" class="card-header">
              <span>{{ contractCreateFeeInfo.contractCode }}</span>
        
            </div>

            <div class="search-section">
              <el-row :gutter="20">
                <el-col :span="4">
                  <el-input :placeholder="$t('contractCreateFee.enterContractCode')"
                    v-model="contractCreateFeeInfo.conditions.contractCode" @keyup.enter.native="_queryContract"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-input :placeholder="$t('contractCreateFee.enterContractName')"
                    v-model="contractCreateFeeInfo.conditions.contractNameLike" @keyup.enter.native="_queryContract"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-input :placeholder="$t('contractCreateFee.enterContractType')"
                    v-model="contractCreateFeeInfo.conditions.contractType" @keyup.enter.native="_queryContract"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-input :placeholder="$t('contractCreateFee.enterPartyB')"
                    v-model="contractCreateFeeInfo.conditions.bContacts" @keyup.enter.native="_queryContract"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-select v-model="contractCreateFeeInfo.conditions.state"
                    :placeholder="$t('contractCreateFee.selectContractStatus')" style="width:100%">
                    <el-option :label="$t('contractCreateFee.selectContractStatus')" value=""></el-option>
                    <el-option v-for="(item, index) in contractCreateFeeInfo.states" :key="index" :label="item.name"
                      :value="item.statusCd"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" @click="_queryContract">
                    {{ $t('common.search') }}
                  </el-button>
                </el-col>
              </el-row>
            </div>

            <div class="tab-section">
              <el-tabs v-model="contractCreateFeeInfo._currentTab" @tab-click="changeTab(contractCreateFeeInfo._currentTab)">
                <el-tab-pane label="合同费用" name="simplifyContractFee">
                  <simplify-contract-fee v-if="contractCreateFeeInfo._currentTab === 'simplifyContractFee'" ref="simplifyContractFee"/>
                </el-tab-pane>

                <el-tab-pane label="缴费历史" name="simplifyHisFee">
                  <simplify-his-fee v-if="contractCreateFeeInfo._currentTab === 'simplifyHisFee'" ref="simplifyHisFee"/>
                </el-tab-pane>

                <el-tab-pane label="预付收款" name="prePayFeeDetail">
                  <pre-pay-fee-detail v-if="contractCreateFeeInfo._currentTab === 'prePayFeeDetail'" ref="prePayFeeDetail"/>
                </el-tab-pane>

                <el-tab-pane label="设置费用" name="feeDetailContractFeeRule">
                  <fee-detail-contract-fee-rule v-if="contractCreateFeeInfo._currentTab === 'feeDetailContractFeeRule'" ref="feeDetailContractFeeRule"/>
                </el-tab-pane>

                <el-tab-pane v-if="contractCreateFeeInfo.ownerId" label="业主" name="contractDetailOwner">
                  <car-detail-owner v-if="contractCreateFeeInfo._currentTab === 'contractDetailOwner'" ref="contractDetailOwner"/>
                </el-tab-pane>

                <el-tab-pane label="合同房屋" name="contractDetailRoom">
                  <contract-detail-room v-if="contractCreateFeeInfo._currentTab === 'contractDetailRoom'" ref="contractDetailRoom"/>
                </el-tab-pane>

                <el-tab-pane v-if="hasPrivilege('502020092373407363')" label="补打收据" name="simplifyFeeReceipt">
                  <simplify-fee-receipt v-if="contractCreateFeeInfo._currentTab === 'simplifyFeeReceipt'" ref="simplifyFeeReceipt"/>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 模态框组件 -->
    <delete-fee ref="deleteFee" @success="handleSuccess" />
    <edit-fee ref="editFee" @success="handleSuccess" />
    <split-fee ref="splitFee" @success="handleSuccess" />
    <contract-create-fee-add ref="contractCreateFeeAdd" @success="handleSuccess" />
    <download-collection-letter-order ref="downloadCollectionLetterOrder" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { queryContract } from '@/api/fee/contractCreateFeeApi'

// 导入组件
import SimplifyContractFee from '@/components/fee/simplifyContractFee'
import SimplifyHisFee from '@/components/fee/simplifyHisFee'
import PrePayFeeDetail from '@/components/fee/prePayFeeDetail'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import ContractDetailRoom from '@/components/contract/contractDetailRoom'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import FeeDetailContractFeeRule from '@/components/fee/feeDetailContractFeeRule'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import SplitFee from '@/components/fee/splitFee'

import ContractCreateFeeAdd from '@/components/fee/contractCreateFeeAdd'
import DownloadCollectionLetterOrder from '@/components/fee/downloadCollectionLetterOrder'

export default {
  name: 'ContractCreateFeeList',
  components: {
    SimplifyContractFee,
    SimplifyHisFee,
    PrePayFeeDetail,
    CarDetailOwner,
    ContractDetailRoom,
    SimplifyFeeReceipt,
    FeeDetailContractFeeRule,
    DeleteFee,
    EditFee,
    SplitFee,
    ContractCreateFeeAdd,
    DownloadCollectionLetterOrder
  },
  data() {
    return {
      communityId: '',
      contractCreateFeeInfo: {
        contracts: [],
        states: [],
        contractId: '',
        contractCode: '',
        contractName: '',
        ownerId: '',
        ownerName: '',
        total: 0,
        records: 1,
        _currentTab: 'simplifyContractFee',
        conditions: {
          contractCode: '',
          contractNameLike: '',
          contractType: '',
          bContacts: '',
          state: ''
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
    await this.loadDictData()
    await this.listContracts(1, 500)
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('contract', 'state')
        this.contractCreateFeeInfo.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async listContracts(page, row) {
      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          ...this.contractCreateFeeInfo.conditions
        }

        const response = await queryContract(params)
        this.contractCreateFeeInfo.contracts = response.data || []
        this.contractCreateFeeInfo.total = response.total || 0
        this.contractCreateFeeInfo.records = response.records || 0

        if (this.contractCreateFeeInfo.contracts.length > 0) {
          const lastSelected = localStorage.getItem('lastSelectedContract')
          if (lastSelected) {
            const selectedContract = JSON.parse(lastSelected)
            this.switchContract(selectedContract)
          } else {
            this.switchContract(this.contractCreateFeeInfo.contracts[0])
          }
        }
      } catch (error) {
        console.error('获取合同列表失败:', error)
        this.$message.error('获取合同列表失败')
      }
    },

    switchContract(contract) {
      this.contractCreateFeeInfo.contractId = contract.contractId
      this.contractCreateFeeInfo.contractCode = contract.contractCode
      this.contractCreateFeeInfo.contractName = contract.contractName
      this.contractCreateFeeInfo.ownerId = contract.objId
      this.contractCreateFeeInfo.ownerName = contract.ownerName
      this.changeTab(this.contractCreateFeeInfo._currentTab)
      localStorage.setItem('lastSelectedContract', JSON.stringify(contract))
    },

    changeTab(tabName) {
      this.contractCreateFeeInfo._currentTab = tabName
      setTimeout(() => {
        if (this.$refs[tabName]) {
          const params = {
            contractId: this.contractCreateFeeInfo.contractId,
            contractCode: this.contractCreateFeeInfo.contractCode,
            contractName: this.contractCreateFeeInfo.contractName,
            ownerId: this.contractCreateFeeInfo.ownerId,
            ownerName: this.contractCreateFeeInfo.ownerName,
            payerObjId: this.contractCreateFeeInfo.contractId,
            payerObjType: '7777'
          }
          
          // 根据不同组件传递不同的参数
          if (tabName === 'simplifyContractFee') {
            this.$refs[tabName].open(params)
          } else if (tabName === 'simplifyHisFee') {
            this.$refs[tabName].open({
              ownerId: params.ownerId,
              payerObjId: params.payerObjId
            })
          } else if (tabName === 'prePayFeeDetail') {
            this.$refs[tabName].open({
              payerObjId: params.payerObjId
            })
          } else if (tabName === 'feeDetailContractFeeRule') {
            this.$refs[tabName].open({
              contractId: params.contractId,
              contractCode: params.contractCode,
              ownerId: params.ownerId,
              ownerName: params.ownerName
            })
          } else if (tabName === 'contractDetailOwner') {
            this.$refs[tabName].open({
              ownerId: params.ownerId
            })
          } else if (tabName === 'contractDetailRoom') {
            this.$refs[tabName].open({
              contractId: params.contractId
            })
          } else if (tabName === 'simplifyFeeReceipt') {
            this.$refs[tabName].open({
              payerObjId: params.payerObjId,
              ownerId: params.ownerId,
              payerObjType: params.payerObjType
            })
          }
        }
      }, 500)
    },

    async _queryContract() {
      await this.listContracts(1, 500)
    },

    _openBatchCreate() {
      this.$refs.contractCreateFeeAdd.open(null, true)
    },

   

    handleSuccess() {
      // 处理成功回调
      this.listContracts(1, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-create-fee-container {
  padding: 20px;

  .main-card {
    min-height: 800px;
  }

  .empty-page {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 800px;
    color: #909399;
    font-size: 16px;

    i {
      font-size: 64px;
      margin-bottom: 20px;
    }
  }

  .left-panel {
    .contract-list-card {
      height: 700px;

      .contract-list-wrapper {
        height: 650px;
        overflow-y: auto;

        .contract-list {
          list-style: none;
          padding: 0;
          margin: 0;

          .contract-item {
            padding: 12px;
            margin-bottom: 8px;
            border: 1px solid #e6e6e6;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s;
            text-align: center;

            &:hover {
              background-color: #f5f7fa;
              border-color: #409eff;
            }

            &.selected {
              background-color: #ecf5ff;
              border-color: #409eff;
              color: #409eff;
              font-weight: bold;
            }
          }
        }
      }
    }
  }

  .right-panel {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        .el-button {
          margin-left: 10px;
        }
      }
    }

    .search-section {
      margin-bottom: 20px;

      .el-col {
        margin-bottom: 10px;
      }
    }

    .tab-section {
      margin-top: 20px;
    }
  }
}
</style>