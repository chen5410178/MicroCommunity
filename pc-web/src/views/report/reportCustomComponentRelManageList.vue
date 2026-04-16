<template>
  <div class="report-custom-component-rel-manage-container">
    <div v-if="currentComponent === 'list'">
      <el-row>
        <el-col :span="24">
          <el-card>
            <div slot="header" class="clearfix">
              <span>{{ $t('reportCustomComponentRelManage.title') }} - {{ title }}</span>
              <div class="card-header-actions">
                <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
                <el-button type="primary" size="small" icon="el-icon-plus" @click="openAddModal">
                  {{ $t('reportCustomComponentRelManage.associateComponent') }}
                </el-button>
              </div>
            </div>

            <el-table :data="tableData" border style="width: 100%" v-loading="loading">
              <el-table-column prop="relId" :label="$t('reportCustomComponentRelManage.relId')" align="center" />
              <el-table-column prop="componentName" :label="$t('reportCustomComponentRelManage.componentName')"
                align="center" />
              <el-table-column prop="customId" :label="$t('reportCustomComponentRelManage.customId')" align="center" />
              <el-table-column prop="seq" :label="$t('reportCustomComponentRelManage.seq')" align="center" />
              <el-table-column prop="createTime" :label="$t('reportCustomComponentRelManage.createTime')"
                align="center" />
              <el-table-column :label="$t('common.operation')" align="center" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
                    {{ $t('common.delete') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
              layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" class="pagination" />
          </el-card>
        </el-col>
      </el-row>
    </div>
    <div v-else>
      <add-report-custom-component-rel :custom-id="customId" @success="handleAddSuccess" @cancel="handleAddCancel" />
    </div>

    <delete-report-custom-component-rel ref="deleteDialog" @success="handleDeleteSuccess" />
  </div>
</template>

<script>
import { listReportCustomComponentRel } from '@/api/report/reportCustomComponentRelManageApi'
import AddReportCustomComponentRel from '@/components/report/AddReportCustomComponentRel'
import DeleteReportCustomComponentRel from '@/components/report/DeleteReportCustomComponentRel'

export default {
  name: 'ReportCustomComponentRelManageList',
  components: {
    AddReportCustomComponentRel,
    DeleteReportCustomComponentRel
  },
  data() {
    return {
      currentComponent: 'list',
      customId: '',
      title: '',
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.customId = this.$route.query.customId || ''
    this.title = this.$route.query.title || ''
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          customId: this.customId
        }
        const { data, records } = await listReportCustomComponentRel(params)
        this.tableData = data
        this.pagination.total = records
      } catch (error) {
        this.$message.error(this.$t('reportCustomComponentRelManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    openAddModal() {
      this.currentComponent = 'add'
    },
    openDeleteModal(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleAddSuccess() {
      this.currentComponent = 'list'
      this.getList()
    },
    handleAddCancel() {
      this.currentComponent = 'list'
    },
    handleDeleteSuccess() {
      this.getList()
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-custom-component-rel-manage-container {
  padding: 20px;

  .card-header-actions {
    float: right;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>