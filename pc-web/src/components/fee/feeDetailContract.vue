<template>
  <div>
    <div class="margin-top">
      <el-table :data="feeDetailContractInfo.contracts" style="width: 100%">
        <el-table-column prop="contractName" :label="$t('feeDetailContract.contractName')" align="center"></el-table-column>
        <el-table-column prop="contractCode" :label="$t('feeDetailContract.contractCode')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailContract.parentContractCode')" align="center">
          <template slot-scope="scope">
            {{scope.row.parentContractCode ? scope.row.parentContractCode : '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" :label="$t('feeDetailContract.contractType')" align="center"></el-table-column>
        <el-table-column prop="operator" :label="$t('feeDetailContract.operator')" align="center"></el-table-column>
        <el-table-column prop="amount" :label="$t('feeDetailContract.contractAmount')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('feeDetailContract.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('feeDetailContract.endTime')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('feeDetailContract.draftTime')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('feeDetailContract.status')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-size="pagination.pageSize"
            layout="total, prev, pager, next"
            :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryContract } from '@/api/fee/feeDetailContractApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailContract',
  data() {
    return {
      feeDetailContractInfo: {
        contracts: [],
        contractId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailContractInfo.contractId = params.payerObjId
      this._loadFeeDetailContractData()
    },
    _loadFeeDetailContractData() {
      const params = {
        communityId: getCommunityId(),
        contractId: this.feeDetailContractInfo.contractId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      queryContract(params).then(res => {
        this.feeDetailContractInfo.contracts = res.data
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load contract details:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailContractData()
    }
  }
}
</script>