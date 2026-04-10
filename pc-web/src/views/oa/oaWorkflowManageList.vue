<template>
  <div class="oa-workflow-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('oaWorkflowManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.flowName" :placeholder="$t('oaWorkflowManage.search.flowNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.flowType" :placeholder="$t('oaWorkflowManage.search.flowTypePlaceholder')"
            style="width:100%">
            <el-option :label="$t('oaWorkflowManage.search.selectAll')" value="" />
            <el-option v-for="item in flowTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 流程实例列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('oaWorkflowManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('oaWorkflowManage.list.addWorkflow') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="flowId" :label="$t('oaWorkflowManage.table.flowId')" align="center" />
        <el-table-column prop="flowName" :label="$t('oaWorkflowManage.table.flowName')" align="center" />
        <el-table-column prop="flowType" :label="$t('oaWorkflowManage.table.flowType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.flowType === '1001' ? $t('oaWorkflowManage.table.normalFlow') :
              $t('oaWorkflowManage.table.unknown') }}
          </template>
        </el-table-column>
        <el-table-column prop="modelId" :label="$t('oaWorkflowManage.table.modelId')" align="center" />
        <el-table-column prop="flowKey" label="KEY" align="center" />
        <el-table-column prop="state" :label="$t('oaWorkflowManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'C' ? $t('oaWorkflowManage.table.deployed') :
              $t('oaWorkflowManage.table.pendingDeploy') }}
          </template>
        </el-table-column>
        <el-table-column prop="describle" :label="$t('oaWorkflowManage.table.describle')" align="center" width="150" />
        <el-table-column prop="createTime" :label="$t('oaWorkflowManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleWorkflowEditor(scope.row)">
              {{ $t('oaWorkflowManage.table.workflow') }}
            </el-button>
            <el-button size="mini" type="text" @click="handleWorkflowForm(scope.row)">
              {{ $t('oaWorkflowManage.table.designForm') }}
            </el-button>
            <el-button type="text" v-if="scope.row.state === 'W'" size="mini"  @click="handleDeploy(scope.row)">
              {{ $t('oaWorkflowManage.table.deploy') }}
            </el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-oa-workflow ref="addWorkflow" @success="handleSuccess" />
    <edit-oa-workflow ref="editWorkflow" @success="handleSuccess" />
    <delete-oa-workflow ref="deleteWorkflow" @success="handleSuccess" />
  </div>
</template>

<script>
import { getOaWorkflowList, deployModel } from '@/api/oa/oaWorkflowManageApi'
import { getDict } from '@/api/community/communityApi'
import AddOaWorkflow from '@/components/oa/addOaWorkflow'
import EditOaWorkflow from '@/components/oa/editOaWorkflow'
import DeleteOaWorkflow from '@/components/oa/deleteOaWorkflow'

export default {
  name: 'OaWorkflowManageList',
  components: {
    AddOaWorkflow,
    EditOaWorkflow,
    DeleteOaWorkflow
  },
  data() {
    return {
      loading: false,
      searchForm: {
        flowName: '',
        flowType: '',
        state: ''
      },
      tableData: [],
      flowTypes: [],
      states: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getDictData()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await getOaWorkflowList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('oaWorkflowManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        this.flowTypes = await getDict('oa_workflow', 'flow_type')
        this.states = await getDict('oa_workflow', 'state')
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        flowName: '',
        flowType: '',
        state: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addWorkflow.open()
    },
    handleEdit(row) {
      this.$refs.editWorkflow.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteWorkflow.open(row)
    },
    handleWorkflowEditor(row) {
      window.open(`/bpmnjs/index.html?flowId=${row.flowId}&modelId=${row.modelId}`)
    },
    handleWorkflowForm(row) {
      window.open(`/formjs/editor.html?flowId=${row.flowId}&modelId=${row.modelId}`)
    },
    async handleDeploy(row) {
      try {
        this.loading = true
        const res = await deployModel({ modelId: row.modelId })
        this.$message.success(res.msg)
        this.getList()
      } catch (error) {
        this.$message.error(this.$t('oaWorkflowManage.deployError'))
      } finally {
        this.loading = false
      }
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.oa-workflow-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 10px;

    .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    margin-bottom: 10px;

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>