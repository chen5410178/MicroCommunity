<template>
  <div class="workflow-manage-container animated fadeInRight padding">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workflowManage.search.title') }}</span>
          </div>
          <div class="search-wrapper">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input v-model="searchForm.flowId" :placeholder="$t('workflowManage.search.flowId')" clearable
                  @keyup.enter.native="handleSearch" />
              </el-col>
              <el-col :span="8">
                <el-select v-model="searchForm.flowType" :placeholder="$t('workflowManage.search.flowType')"
                  style="width:100%" clearable>
                  <el-option v-for="item in flowTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col :span="6">
                <el-input v-model="searchForm.flowName" :placeholder="$t('workflowManage.search.flowName')" clearable
                  @keyup.enter.native="handleSearch" />
              </el-col>
              <el-col :span="4">
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
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('workflowManage.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="flowId" :label="$t('workflowManage.table.flowId')" align="center" />
            <el-table-column prop="flowName" :label="$t('workflowManage.table.flowName')" align="center" />
            <el-table-column prop="flowTypeName" :label="$t('workflowManage.table.flowType')" align="center" />
            <el-table-column prop="createTime" :label="$t('workflowManage.table.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
                  {{ $t('workflowManage.operation.setting') }}
                </el-button>
                <el-button size="mini" type="success" @click="handleViewImage(scope.row)">
                  {{ $t('workflowManage.operation.viewImage') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listWorkflows, listWorkflowImage } from '@/api/system/workflowManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'WorkflowManageList',
  components: {
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        flowId: '',
        flowName: '',
        flowType: ''
      },
      tableData: [],
      flowTypes: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
    this.getDictData()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          ...this.searchForm
        }
        const { data, total } = await listWorkflows(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('workflowManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        const data = await getDict('workflow', 'flow_type')
        this.flowTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        flowId: '',
        flowName: '',
        flowType: ''
      }
      this.getList()
    },
    handleEdit(row) {
      this.$router.push({
        path: '/pages/property/workflowSettingManage',
        query: row
      })
    },
    async handleViewImage(row) {
      try {
        const params = {
          communityId: this.communityId,
          flowId: row.flowId
        }
        const { data } = await listWorkflowImage(params)
        this.$refs.viewImage.open({
          url: 'data:image/png;base64,' + data
        })
      } catch (error) {
        console.error('获取流程图片失败:', error)
        this.$message.error(this.$t('workflowManage.imageError'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.workflow-manage-container {

  margin: 0;

  .box-card {
    margin-bottom: 20px;
  }

  .search-wrapper {
    .el-col {
      margin-bottom: 10px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>