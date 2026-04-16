<template>
  <div class="contract-detail-change">
    <el-table :data="contractDetailChangeInfo.contracts" border style="width: 100%" v-loading="loading">
      <el-table-column prop="contractName" :label="$t('contractDetailChange.contractName')" align="center">
      </el-table-column>
      <el-table-column prop="contractCode" :label="$t('contractDetailChange.contractCode')" align="center">
      </el-table-column>
      <el-table-column prop="contractTypeName" :label="$t('contractDetailChange.contractTypeName')" align="center">
      </el-table-column>
      <el-table-column prop="partyA" :label="$t('contractDetailChange.partyA')" align="center">
      </el-table-column>
      <el-table-column prop="partyB" :label="$t('contractDetailChange.partyB')" align="center">
      </el-table-column>
      <el-table-column prop="planTypeName" :label="$t('contractDetailChange.planTypeName')" align="center">
      </el-table-column>
      <el-table-column prop="changePersonName" :label="$t('contractDetailChange.changePersonName')" align="center">
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('contractDetailChange.createTime')" align="center">
      </el-table-column>
      <el-table-column prop="remark" :label="$t('contractDetailChange.remark')" align="center">
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('contractDetailChange.stateName')" align="center">
      </el-table-column>
      <el-table-column :label="$t('contractDetailChange.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="_toContractDetails(scope.row)">
            {{ $t('contractDetailChange.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { queryContractChangePlan } from '@/api/system/operateDataLogApi'

export default {
  name: 'ContractDetailChange',
  data() {
    return {
      loading: false,
      contractDetailChangeInfo: {
        contracts: [],
        contractId: '',
        roomNum: '',
        totalArea: '0',
        logStartTime: '',
        logEndTime: '',
        contractCode: '',
        staffNameLike: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(conditions) {
      this.contractDetailChangeInfo = {
        ...this.contractDetailChangeInfo,
        ...conditions
      }
      this._loadContractDetailChangeData()
    },
    async _loadContractDetailChangeData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          contractId: this.contractDetailChangeInfo.contractId,
          logStartTime: this.contractDetailChangeInfo.logStartTime,
          logEndTime: this.contractDetailChangeInfo.logEndTime,
          contractCode: this.contractDetailChangeInfo.contractCode,
          staffNameLike: this.contractDetailChangeInfo.staffNameLike
        }

        const { data, total } = await queryContractChangePlan(params)
        this.contractDetailChangeInfo.contracts = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load contract change history:', error)
      } finally {
        this.loading = false
      }
    },
    _toContractDetails(contract) {
      this.$router.push({
        path: '/views/contract/contractChangeDetails',
        query: { planId: contract.planId }
      })
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadContractDetailChangeData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._loadContractDetailChangeData()
    }
  }
}
</script>

<style scoped>
.contract-detail-change {
  padding: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>