<template>
  <div class="contract-manage-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-wrapper">
            <div class="type-list-item" :class="{ 'selected': selectedType === '' }"
              @click="switchType('')">
              {{ $t('contractManage.allTypes') }}
            </div>
            <div v-for="(item, index) in contractManageInfo.contractTypes" :key="index"
              class="type-list-item"
              :class="{ 'selected': selectedType === item.contractTypeId }"
              @click="switchType(item.contractTypeId)">
              {{ item.typeName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractManage.search.title') }}</span>
            <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
              {{ moreCondition ? $t('contractManage.search.hide') : $t('contractManage.search.more') }}
            </el-button>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="contractManageInfo.conditions.contractNameLike"
                :placeholder="$t('contractManage.contractNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="contractManageInfo.conditions.contractCodeLike"
                :placeholder="$t('contractManage.contractCodePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="contractManageInfo.conditions.queryStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('contractManage.startTimePlaceholder')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="contractManageInfo.conditions.queryEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('contractManage.endTimePlaceholder')" style="width:100%" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="queryContractMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="resetContractMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row v-if="moreCondition" :gutter="20" class="margin-top-xs">
            <el-col :span="4">
              <el-input v-model="contractManageInfo.conditions.partyBLike"
                :placeholder="$t('contractManage.partyBPlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="contractManageInfo.conditions.parentContractCodeLike"
                :placeholder="$t('contractManage.parentContractCodePlaceholder')" clearable />
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <el-table :data="contractManageInfo.contracts" border style="width: 100%" v-loading="loading">
            <el-table-column prop="contractName" :label="$t('contractManage.table.contractName')" align="center" />
            <el-table-column prop="contractCode" :label="$t('contractManage.table.contractCode')" align="center" />
            <el-table-column prop="parentContractCode" :label="$t('contractManage.table.parentContractCode')" align="center">
              <template slot-scope="scope">
                {{ scope.row.parentContractCode || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="contractTypeName" :label="$t('contractManage.table.contractType')" align="center" />
            <el-table-column prop="operator" :label="$t('contractManage.table.operator')" align="center">
              <template slot-scope="scope">
                {{ scope.row.operator }}({{ scope.row.operatorLink }})
              </template>
            </el-table-column>
            <el-table-column prop="amount" :label="$t('contractManage.table.amount')" align="center" />
            <el-table-column prop="partyB" :label="$t('contractManage.table.partyB')" align="center">
              <template slot-scope="scope">
                {{ scope.row.partyB }}({{ scope.row.bLink }})
              </template>
            </el-table-column>
            <el-table-column :label="$t('contractManage.table.validityPeriod')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('contractManage.table.createTime')" align="center" />
            <el-table-column prop="stateName" :label="$t('contractManage.table.status')" align="center" />
            <el-table-column :label="$t('contractManage.table.operation')" align="center" width="220">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" @click="viewContract(scope.row)">
                    {{ $t('contractManage.operation.view') }}
                  </el-button>
                  <el-button size="mini" @click="printContract(scope.row)">
                    {{ $t('contractManage.operation.print') }}
                  </el-button>
                  <el-button size="mini" type="danger" @click="openDeleteContractModel(scope.row)">
                    {{ $t('contractManage.operation.delete') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
            :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
            :total="page.total" style="margin-top:20px;text-align:right" />
        </el-card>
      </el-col>
    </el-row>

    <edit-contract ref="editContract" @success="handleSuccess" />
    <delete-contract ref="deleteContract" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryContract, queryContractType } from '@/api/contract/contractManageApi'
import EditContract from '@/components/contract/editContract'
import DeleteContract from '@/components/contract/deleteContract'

export default {
  name: 'ContractManageList',
  components: {
    EditContract,
    DeleteContract
  },
  data() {
    return {
      loading: false,
      moreCondition: false,
      selectedType: '',
      contractManageInfo: {
        contracts: [],
        conditions: {
          contractNameLike: '',
          contractCodeLike: '',
          contractType: '',
          partyBLike: '',
          queryStartTime: '',
          queryEndTime: '',
          parentContractCodeLike: ''
        },
        contractTypes: []
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.listContracts()
    this.listContractTypes()
  },
  methods: {
    async listContracts() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.contractManageInfo.conditions
        }
        const { data, total } = await queryContract(params)
        this.contractManageInfo.contracts = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async listContractTypes() {
      try {
        const params = {
          page: 1,
          row: 50
        }
        const { data } = await queryContractType(params)
        this.contractManageInfo.contractTypes = data
      } catch (error) {
        console.error('获取合同类型失败:', error)
      }
    },
    queryContractMethod() {
      this.page.current = 1
      this.listContracts()
    },
    toggleMoreCondition() {
      this.moreCondition = !this.moreCondition
    },
    resetContractMethod() {
      this.contractManageInfo.conditions = {
        contractNameLike: '',
        contractCodeLike: '',
        contractType: this.selectedType,
        partyBLike: '',
        queryStartTime: '',
        queryEndTime: '',
        parentContractCodeLike: ''
      }
      this.listContracts()
    },
    switchType(contractTypeId) {
      this.selectedType = contractTypeId
      this.contractManageInfo.conditions.contractType = contractTypeId
      this.page.current = 1
      this.listContracts()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listContracts()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listContracts()
    },
    openContractFee(contract) {
      this.$router.push({
        path: '/property/listContractFee',
        query: {
          contractId: contract.contractId,
          contractCode: contract.contractCode
        }
      })
    },
    viewContract(contract) {
      this.$router.push({
        path: '/views/contract/contractDetail',
        query: {
          contractId: contract.contractId
        }
      })
    },
    printContract(contract) {
      window.open(`/#/views/contract/printContract?contractTypeId=${contract.contractType}&contractId=${contract.contractId}`)
    },
    openDeleteContractModel(contract) {
      this.$refs.deleteContract.open(contract)
    },
    handleSuccess() {
      this.listContracts()
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-manage-container {
  padding: 20px;

  .left-panel {
    .type-card {
      .type-list-wrapper {
        max-height: 600px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .type-list-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .margin-top-xs {
    margin-top: 15px;
  }
}
</style>