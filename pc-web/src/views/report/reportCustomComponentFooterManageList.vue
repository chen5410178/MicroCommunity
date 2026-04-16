<template>
  <div class="report-custom-component-footer-container">
    <el-row>
      <el-col :span="24">
        <div class="ibox">
          <div class="ibox-title">
            <h5>{{reportCustomComponentFooterManageInfo.componentName}}
              <span>{{ $t('reportCustomComponentFooterManage.statistics') }}</span>
            </h5>
            <div class="ibox-tools" style="top:10px;">
              <el-button type="primary" size="small" @click="_goBack">{{ $t('common.back') }}</el-button>
              <el-button type="primary" size="small" @click="_openAddReportCustomComponentFooterModal">
                <i class="el-icon-plus"></i>{{ $t('common.add') }}
              </el-button>
            </div>
          </div>
          <div class="ibox-content">
            <el-table :data="reportCustomComponentFooterManageInfo.reportCustomComponentFooters" border style="width: 100%">
              <el-table-column prop="footerId" :label="$t('reportCustomComponentFooterManage.id')" align="center" />
              <el-table-column prop="name" :label="$t('reportCustomComponentFooterManage.name')" align="center" />
              <el-table-column prop="queryModel" :label="$t('reportCustomComponentFooterManage.queryMethod')" align="center" />
              <el-table-column prop="remark" :label="$t('reportCustomComponentFooterManage.description')" align="center" />
              <el-table-column :label="$t('common.operation')" align="center" width="200">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button size="mini" type="primary" @click="_openEditReportCustomComponentFooterModel(scope.row)">
                      {{ $t('common.edit') }}
                    </el-button>
                    <el-button size="mini" type="danger" @click="_openDeleteReportCustomComponentFooterModel(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="reportCustomComponentFooterManageInfo.total">
            </el-pagination>
          </div>
        </div>
      </el-col>
    </el-row>

    <add-report-custom-component-footer 
      :visible.sync="addDialogVisible"
      :component-id="reportCustomComponentFooterManageInfo.conditions.componentId"
      @success="handleSuccess" />
      
    <edit-report-custom-component-footer 
      :visible.sync="editDialogVisible"
      :form-data="currentEditData"
      @success="handleSuccess" />
      
    <delete-report-custom-component-footer 
      :visible.sync="deleteDialogVisible"
      :footer-id="currentDeleteId"
      @success="handleSuccess" />
  </div>
</template>

<script>
import { listReportCustomComponentFooter } from '@/api/report/reportCustomComponentFooterManageApi'

export default {
  name: 'ReportCustomComponentFooterManageList',
  components: {
    'add-report-custom-component-footer': () => import('@/components/report/AddReportCustomComponentFooter'),
    'edit-report-custom-component-footer': () => import('@/components/report/EditReportCustomComponentFooter'),
    'delete-report-custom-component-footer': () => import('@/components/report/DeleteReportCustomComponentFooter')
  },
  data() {
    return {
      reportCustomComponentFooterManageInfo: {
        reportCustomComponentFooters: [],
        total: 0,
        componentName: '',
        conditions: {
          componentId: '',
          name: '',
          queryModel: ''
        }
      },
      currentPage: 1,
      pageSize: 10,
      addDialogVisible: false,
      editDialogVisible: false,
      deleteDialogVisible: false,
      currentEditData: {},
      currentDeleteId: ''
    }
  },
  created() {
    this.reportCustomComponentFooterManageInfo.conditions.componentId = this.$route.query.componentId
    this.reportCustomComponentFooterManageInfo.componentName = this.$route.query.componentName
    this._listReportCustomComponentFooters()
  },
  methods: {
    async _listReportCustomComponentFooters() {
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          componentId: this.reportCustomComponentFooterManageInfo.conditions.componentId
        }
        const { data, total } = await listReportCustomComponentFooter(params)
        this.reportCustomComponentFooterManageInfo.reportCustomComponentFooters = data
        this.reportCustomComponentFooterManageInfo.total = total
      } catch (error) {
        this.$message.error(this.$t('reportCustomComponentFooterManage.fetchError'))
      }
    },
    _openAddReportCustomComponentFooterModal() {
      this.addDialogVisible = true
    },
    _openEditReportCustomComponentFooterModel(row) {
      this.currentEditData = { ...row }
      this.editDialogVisible = true
    },
    _openDeleteReportCustomComponentFooterModel(row) {
      this.currentDeleteId = row.footerId
      this.deleteDialogVisible = true
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this._listReportCustomComponentFooters()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listReportCustomComponentFooters()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listReportCustomComponentFooters()
    }
  }
}
</script>

<style scoped>
.ibox {
  margin-bottom: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 1px rgba(0,0,0,.05);
}

.ibox-title {
  padding: 15px 15px 7px;
  min-height: 48px;
  border-bottom: 1px solid #e7eaec;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ibox-content {
  padding: 15px;
}

.el-pagination {
  margin-top: 15px;
  text-align: right;
}
</style>