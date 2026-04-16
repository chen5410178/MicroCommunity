<template>
  <div class="margin-top">
    <div class="flex justify-end">
      <div :span="2" class="padding-right-xs padding-left-xl"></div>
      <div :span="2" class="padding-right-xs padding-right-xl"></div>
      <div :span="8" class="text-right"></div>
    </div>
    <div class="margin-top">
      <el-table :data="contractDetailChangeInfo.contracts" style="width: 100%" border stripe>
        <el-table-column prop="contractName" :label="$t('contractDetailChange.contractName')" align="center" />
        <el-table-column prop="contractCode" :label="$t('contractDetailChange.contractCode')" align="center" />
        <el-table-column prop="contractTypeName" :label="$t('contractDetailChange.contractType')" align="center" />
        <el-table-column prop="partyA" :label="$t('contractDetailChange.partyA')" align="center" />
        <el-table-column prop="partyB" :label="$t('contractDetailChange.partyB')" align="center" />
        <el-table-column prop="planTypeName" :label="$t('contractDetailChange.changeType')" align="center" />
        <el-table-column prop="changePersonName" :label="$t('contractDetailChange.changePerson')" align="center" />
        <el-table-column prop="createTime" :label="$t('contractDetailChange.applyTime')" align="center" />
        <el-table-column prop="remark" :label="$t('contractDetailChange.description')" align="center" />
        <el-table-column prop="stateName" :label="$t('contractDetailChange.status')" align="center" />
        <el-table-column :label="$t('contractDetailChange.operation')" align="center">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="_toContractDetails(row)">
              {{ $t('contractDetailChange.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="4"></el-col>
        <el-col :span="8">
          <el-pagination class="pagination" layout="total, sizes, prev, pager, next, jumper" :total="total"
            :page-size="pageSize" @current-change="handleCurrentChange" @size-change="handleSizeChange" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryContractChangePlan } from '@/api/contract/contractDetailChangeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailChange',
  data() {
    return {
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
      this.contractDetailChangeInfo.contractId = data.contractId
      this.contractDetailChangeInfo.logStartTime = data.logStartTime
      this.contractDetailChangeInfo.logEndTime = data.logEndTime
      this.contractDetailChangeInfo.contractCode = data.contractCode
      this._loadContractDetailChangeData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadContractDetailChangeData()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._loadContractDetailChangeData()
    },
    _loadContractDetailChangeData() {
      const params = {
        contractId: this.contractDetailChangeInfo.contractId,
        logStartTime: this.contractDetailChangeInfo.logStartTime,
        logEndTime: this.contractDetailChangeInfo.logEndTime,
        contractCode: this.contractDetailChangeInfo.contractCode,
        staffNameLike: this.contractDetailChangeInfo.staffNameLike,
        page: this.currentPage,
        row: this.pageSize
      }

      queryContractChangePlan(params).then(response => {
        this.contractDetailChangeInfo.contracts = response.data
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _qureyContractDetailChange() {
      this.currentPage = 1
      this._loadContractDetailChangeData()
    },
    _toContractDetails(contract) {
      this.$router.push(`/pages/admin/contractChangeDetails?planId=${contract.planId}`)
    },
    switch(data) {
      this.contractDetailChangeInfo.contractId = data.contractId
      this.contractDetailChangeInfo.logStartTime = data.logStartTime
      this.contractDetailChangeInfo.logEndTime = data.logEndTime
      this.contractDetailChangeInfo.contractCode = data.contractCode
      this._loadContractDetailChangeData()
    },
    notify() {
      this._loadContractDetailChangeData()
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
}
</style>