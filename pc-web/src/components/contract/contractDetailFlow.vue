<template>
  <div class="margin-top">
    <el-row :gutter="20" class="margin-top-lg">
      <el-col :span="2" class="padding-right-xs padding-left-xl"></el-col>
      <el-col :span="2" class="padding-right-xs padding-right-xl"></el-col>
      <el-col :span="8" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="contractDetailFlowInfo.auditUsers" style="width: 100%" border stripe>
        <el-table-column :label="$t('contractDetailFlow.serialNumber')" align="center">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('contractDetailFlow.processor')" align="center">
          <template #default="{ row }">
            {{ row.userName || row.auditName }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('contractDetailFlow.status')" align="center" />
        <el-table-column prop="auditTime" :label="$t('contractDetailFlow.processTime')" align="center" />
        <el-table-column prop="duration" :label="$t('contractDetailFlow.timeConsuming')" align="center" />
        <el-table-column prop="message" :label="$t('contractDetailFlow.opinion')" align="center" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { listWorkflowAuditInfo } from '@/api/contract/contractDetailFlowApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailFlow',
  data() {
    return {
      contractDetailFlowInfo: {
        auditUsers: [],
        contractId: '',
        roomNum: '',
        totalArea: '0'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.contractDetailFlowInfo.contractId = data.contractId
      this._loadContractDetailFlowInfoData()
    },
    _loadContractDetailFlowInfoData() {
      const params = {
        businessKey: this.contractDetailFlowInfo.contractId,
        communityId: this.communityId
      }

      listWorkflowAuditInfo(params).then(response => {
        this.contractDetailFlowInfo.auditUsers = response.data
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    switch(data) {
      this.contractDetailFlowInfo.contractId = data.contractId
      this._loadContractDetailFlowInfoData()
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
</style>