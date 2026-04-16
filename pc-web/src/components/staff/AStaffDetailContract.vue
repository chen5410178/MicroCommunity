<template>
    <div class="staff-contract-container">
      <el-table :data="aStaffDetailContractInfo.contracts" border style="width: 100%" class="margin-top">
        <el-table-column prop="contractId" :label="$t('staffDetailContract.contractId')" align="center" />
        <el-table-column prop="contractNo" :label="$t('staffDetailContract.contractNo')" align="center" />
        <el-table-column prop="contractName" :label="$t('staffDetailContract.contractName')" align="center" />
        <el-table-column prop="partyA" :label="$t('staffDetailContract.partyA')" align="center" />
        <el-table-column prop="partyB" :label="$t('staffDetailContract.partyB')" align="center" />
        <el-table-column :label="$t('staffDetailContract.contractPeriod')" align="center">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startDate) }}~{{ formatDate(scope.row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('staffDetailContract.stateName')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openContractDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script>
  import { listAdminContracts } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailContract',
    data() {
      return {
        aStaffDetailContractInfo: {
          contracts: [],
          staffId: ''
        },
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailContractInfo.staffId = data.staffId
        this.loadContractData(1, this.page.size)
      },
      async loadContractData(page, row) {
        try {
          const { data, total } = await listAdminContracts({
            staffId: this.aStaffDetailContractInfo.staffId,
            page,
            row
          })
          this.aStaffDetailContractInfo.contracts = data.contracts
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailContract.fetchError'))
        }
      },
      openContractDetail(contract) {
        this.$router.push(`/views/contract/adminContractDetail?contractId=${contract.contractId}`)
      },
      formatDate(date) {
        return date || '-' // Implement date formatting if needed
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadContractData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadContractData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-contract-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>