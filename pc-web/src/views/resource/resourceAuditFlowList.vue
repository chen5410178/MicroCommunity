<template>
  <div class="resource-audit-flow-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="text-left">
        <span>{{ $t('resourceAuditFlow.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.flowName" :placeholder="$t('resourceAuditFlow.flowNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.auditType" :placeholder="$t('resourceAuditFlow.flowTypePlaceholder')"
            style="width:100%" clearable>
            <el-option label="请选择流程类型" value="" />
            <el-option :label="$t('resourceAuditFlow.purchaseFlow')" value="10001" />
            <el-option :label="$t('resourceAuditFlow.receiveFlow')" value="10002" />
            <el-option :label="$t('resourceAuditFlow.transferFlow')" value="10003" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleQuery">
            <i class="el-icon-search"></i>
            {{ $t('resourceAuditFlow.query') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('resourceAuditFlow.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 审批流程列表 -->
    <el-card class="list-card">
      <div slot="header" class="list-header">
        <span>{{ $t('resourceAuditFlow.listTitle') }}</span>
        <el-button type="primary" size="small" @click="openAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('resourceAuditFlow.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="rafId" :label="$t('resourceAuditFlow.flowId')" align="center" width="80" />
        <el-table-column :label="$t('resourceAuditFlow.flowName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.flowName }} (<a href="javascript:void(0)" @click="settingFlow(scope.row)">{{
              $t('resourceAuditFlow.setFlow') }}</a>)
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceAuditFlow.flowType')" align="center" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.auditType === '10001'">{{ $t('resourceAuditFlow.purchaseFlow') }}</span>
            <span v-else-if="scope.row.auditType === '10002'">{{ $t('resourceAuditFlow.receiveFlow') }}</span>
            <span v-else>{{ $t('resourceAuditFlow.transferFlow') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceAuditFlow.flowStatus')" align="center" width="100">
          <template slot-scope="scope">
            {{ scope.row.state === 'C' ? $t('resourceAuditFlow.deployed') : $t('resourceAuditFlow.notDeployed') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('resourceAuditFlow.createTime')" align="center" width="180" />
        <el-table-column prop="remark" :label="$t('resourceAuditFlow.remark')" align="center" width="200">
          <template slot-scope="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('resourceAuditFlow.operations')" align="center" fixed="right" width="280">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 'W'" size="mini" type="primary" @click="openDeployWorkflow(scope.row)">
              {{ $t('resourceAuditFlow.deployFlow') }}
            </el-button>
            <el-button size="mini" type="primary" @click="openEditModal(scope.row)">
              {{ $t('resourceAuditFlow.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
              {{ $t('resourceAuditFlow.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="pagination-wrapper">
        <el-col :span="14" class="flow-tips text-left">
          <div v-for="(tip, index) in $t('resourceAuditFlow.flowConfigTips')" :key="index">
            {{ tip }}
          </div>
        </el-col>
        <el-col :span="10" class="pagination-container">
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 子组件 -->
    <add-resource-audit-flow ref="addModal" @success="fetchData" />
    <edit-resource-audit-flow ref="editModal" @success="fetchData" />
    <delete-resource-audit-flow ref="deleteModal" @success="fetchData" />
  </div>
</template>

<script>
import { listResourceAuditFlow,deployWorkflow } from '@/api/resource/resourceAuditFlowApi'
import AddResourceAuditFlow from '@/components/resource/AddResourceAuditFlow'
import EditResourceAuditFlow from '@/components/resource/EditResourceAuditFlow'
import DeleteResourceAuditFlow from '@/components/resource/DeleteResourceAuditFlow'

export default {
  name: 'ResourceAuditFlowList',
  components: {
    AddResourceAuditFlow,
    EditResourceAuditFlow,
    DeleteResourceAuditFlow
  },
  data() {
    return {
      loading: false,
      searchForm: {
        flowName: '',
        auditType: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const res = await listResourceAuditFlow(params)
        if (res.code === 0) {
          this.tableData = res.data
          this.pagination.total = res.total
        }
      } catch (error) {
        console.error('获取审批流程列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        flowName: '',
        auditType: ''
      }
      this.pagination.current = 1
      this.fetchData()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchData()
    },
    settingFlow(row) {
      window.open(`/bpmnjs/index.html?flowId=${row.flowId}&modelId=${row.modelId}`)
    },
    async openDeployWorkflow(row) {
      try {
        this.loading = true
        const res = await deployWorkflow({ modelId: row.modelId })
        if (res.code=== 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.fetchData()
        } else {
          this.$message.error(res.msg || this.$t('common.submitFailed'))
        }
      } catch (error) {
        console.error('部署流程失败:', error)
        this.$message.error(error)
      } finally {
        this.loading = false
      }
    },
    openAddModal() {
      this.$refs.addModal.open()
    },
    openEditModal(row) {
      this.$refs.editModal.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-audit-flow-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -10px;
    }

    .el-col {
      margin-bottom: 10px;
    }
  }

  .list-card {
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .pagination-wrapper {
      margin-top: 20px;
      display: flex;
      align-items: center;

      .flow-tips {
        font-size: 12px;
        color: #999;
        line-height: 1.5;
      }

      .pagination-container {
        display: flex;
        justify-content: flex-end;
      }
    }
  }
}
</style>