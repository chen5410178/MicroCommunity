<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div class="text-right" v-if="contractInfo.ownerId">
        <el-button type="primary" size="small" @click="_openAddContractModal">
          <i class="el-icon-plus"></i>{{$t('simplifyContract.draftContract')}}
        </el-button>
      </div>
    </div>
    <div class="margin-top">
      <el-table :data="contractInfo.contracts" border>
        <el-table-column prop="contractName" :label="$t('simplifyContract.contractName')" align="center"></el-table-column>
        <el-table-column prop="contractCode" :label="$t('simplifyContract.contractCode')" align="center"></el-table-column>
        <el-table-column prop="parentContractCode" :label="$t('simplifyContract.parentContractCode')" align="center">
          <template slot-scope="scope">
            {{scope.row.parentContractCode || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" :label="$t('simplifyContract.contractType')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('simplifyContract.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('simplifyContract.endTime')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('simplifyContract.status')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyContract.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openContractFee(scope.row)">{{$t('simplifyContract.fee')}}</el-button>
            <el-button size="mini" @click="_viewContract(scope.row)">{{$t('simplifyContract.view')}}</el-button>
            <el-button size="mini" @click="_printContract(scope.row)">{{$t('simplifyContract.print')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
        class="pagination"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import { queryContract } from '@/api/simplify/simplifyContractApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SimplifyContract',
  data() {
    return {
      contractInfo: {
        ownerId: '',
        ownerName: '',
        contracts: [],
        total: 0,
        records: 1,
        moreCondition: false,
        contractId: '',
        conditions: {
          objId: '',
          contractCode: '',
          contractType: ''
        }
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(params) {
      if (!params.ownerId) return
      this.clearContractInfoInfo()
      Object.assign(this.contractInfo, params)
      this.contractInfo.conditions.objId = params.ownerId
      this.listContractInfo()
    },
    async listContractInfo() {
      try {
        const params = {
          ...this.contractInfo.conditions,
          page: this.currentPage,
          row: this.pageSize
        }
        const res = await queryContract(params)
        this.contractInfo.contracts = res.data
        this.total = res.data.total
      } catch (error) {
        console.error('Failed to load contracts:', error)
      }
    },
    _openAddContractModal() {
      this.$router.push('/pages/admin/newContractManage')
    },
    _printContract(contract) {
      window.open(`/#/views/contract/printContract?contractTypeId=${contract.contractType}&contractId=${contract.contractId}`)
    },
    _viewContract(contract) {
      this.$router.push(`/views/contract/contractDetail?contractId=${contract.contractId}`)
    },
    clearContractInfoInfo() {
      this.contractInfo = {
        ownerId: '',
        ownerName: '',
        contracts: [],
        total: 0,
        records: 1,
        moreCondition: false,
        contractId: '',
        conditions: {
          contractName: '',
          contractCode: '',
          contractType: ''
        }
      }
    },
    _openContractFee(contract) {
      this.$router.push({
        path: '/views/contract/contractDetail',
        query: {
          contractId: contract.contractId,
          contractCode: contract.contractCode,
          currentTab:'contractDetailFee'
        }
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listContractInfo()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.margin-top-lg {
  margin-top: 20px;
}
.pagination {
  margin-top: 15px;
  text-align: right;
}
.text-right {
  text-align: right;
}
</style>