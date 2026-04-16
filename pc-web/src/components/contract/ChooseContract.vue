<template>
  <el-dialog :title="$t('chooseContract.title')" :visible.sync="dialogVisible" width="80%">
    <div class="search-wrapper">
      <el-input v-model="chooseContractInfo._currentContractName" :placeholder="$t('chooseContract.searchPlaceholder')"
        class="search-input" clearable>
        <el-button slot="append" type="primary" @click="queryContracts">
          <i class="el-icon-search"></i>
          {{ $t('common.search') }}
        </el-button>
      </el-input>
    </div>

    <el-table :data="chooseContractInfo.contracts" border style="width: 100%">
      <el-table-column prop="contractName" :label="$t('chooseContract.contractName')" align="center" />
      <el-table-column prop="contractCode" :label="$t('chooseContract.contractCode')" align="center" />
      <el-table-column prop="contractTypeName" :label="$t('chooseContract.contractType')" align="center" />
      <el-table-column prop="partyA" :label="$t('chooseContract.partyA')" align="center" />
      <el-table-column prop="partyB" :label="$t('chooseContract.partyB')" align="center" />
      <el-table-column prop="signingTime" :label="$t('chooseContract.signingTime')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="chooseContract(scope.row)">
            {{ $t('common.select') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrapper">
      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </el-dialog>
</template>

<script>
import { queryContract } from '@/api/contract/contractChangeDetailApi'

export default {
  name: 'ChooseContract',

  data() {
    return {
      dialogVisible: false,
      chooseContractInfo: {
        contracts: [],
        _currentContractName: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.refreshChooseContractInfo()
      this.loadAllContractInfo(1, 10, '')
    },
    chooseContract(contract) {
      if (contract['name']) {
        contract.contractName = contract.name
      }
      this.$emit('chooseContract', contract)
      this.$emit('listContractData', {
        contractId: contract.contractId
      })
      this.dialogVisible = false
    },
    queryContracts() {
      this.loadAllContractInfo(1, 10, this.chooseContractInfo._currentContractName)
    },
    refreshChooseContractInfo() {
      this.chooseContractInfo._currentContractName = ''
    },
    loadAllContractInfo(page, row, name) {
      const param = {
        page,
        row,
        contractNameLike: name
      }

      queryContract(param)
        .then(response => {
          const contractInfo = response
          this.chooseContractInfo.contracts = contractInfo.data
          this.pagination.total = contractInfo.total
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadAllContractInfo(this.pagination.current, val)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadAllContractInfo(val, this.pagination.size)
    }
  }
}
</script>

<style scoped>
.search-wrapper {
  margin-bottom: 20px;
}

.search-input {
  width: 400px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>