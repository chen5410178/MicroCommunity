<template>
  <div class="margin-top">
    <el-row :gutter="20" class="margin-top-lg">
      <el-col :span="2" class="padding-right-xs padding-left-xl"></el-col>
      <el-col :span="2" class="padding-right-xs padding-right-xl"></el-col>
      <el-col :span="8" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="contractDetailSubInfo.contracts" style="width: 100%" border stripe>
        <el-table-column prop="contractName" :label="$t('contractDetailSub.contractName')" align="center" />
        <el-table-column prop="contractCode" :label="$t('contractDetailSub.contractCode')" align="center" />
        <el-table-column :label="$t('contractDetailSub.parentContractCode')" align="center">
          <template #default="{ row }">
            {{ row.parentContractCode || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" :label="$t('contractDetailSub.contractType')" align="center" />
        <el-table-column :label="$t('contractDetailSub.operator')" align="center">
          <template #default="{ row }">
            {{ row.operator }}({{ row.operatorLink }})
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="$t('contractDetailSub.contractAmount')" align="center" />
        <el-table-column :label="$t('contractDetailSub.contractPartyB')" align="center">
          <template #default="{ row }">
            {{ row.partyB }}({{ row.bLink }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('contractDetailSub.validityPeriod')" align="center">
          <template #default="{ row }">
            {{ row.startTime }}<br>
            ~{{ row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('contractDetailSub.draftTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('contractDetailSub.status')" align="center" />
        <el-table-column :label="$t('contractDetailSub.operation')" align="center">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="_viewContract(row)">
              {{ $t('contractDetailSub.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="pagination" layout="total, sizes, prev, pager, next, jumper" :total="total"
        :page-size="pageSize" @current-change="handleCurrentChange" @size-change="handleSizeChange" />
    </div>
  </div>
</template>

<script>
import { queryContract } from '@/api/contract/contractDetailSubApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailSub',
  data() {
    return {
      contractDetailSubInfo: {
        contracts: [],
        contractId: '',
        roomNum: '',
        totalArea: '0',
        total: 0,
        records: 0
      },
      pageSize: 10,
      currentPage: 1,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.contractDetailSubInfo.contractId = data.contractId
      this._loadContractDetailSubInfoData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadContractDetailSubInfoData()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._loadContractDetailSubInfoData()
    },
    _loadContractDetailSubInfoData() {
      const params = {
        contractParentId: this.contractDetailSubInfo.contractId,
        communityId: this.communityId,
        page: this.currentPage,
        row: this.pageSize
      }

      queryContract(params).then(response => {
        this.contractDetailSubInfo.contracts = response.data
        this.contractDetailSubInfo.total = response.total
        this.contractDetailSubInfo.records = response.records
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _viewContract(contract) {
      this.$router.push(`/pages/contract/contractDetail?contractId=${contract.contractId}`)
    },
    switch(data) {
      this.contractDetailSubInfo.contractId = data.contractId
      this._loadContractDetailSubInfoData()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.padding-right-xs {
  padding-right: 10px;
}

.padding-left-xl {
  padding-left: 40px;
}

.padding-right-xl {
  padding-right: 40px;
}

.text-right {
  text-align: right;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>