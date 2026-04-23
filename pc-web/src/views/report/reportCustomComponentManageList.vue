<template>
  <div class="report-custom-component-container">
    <!-- 查询条件 -->
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('reportCustomComponent.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="searchForm.componentId" :placeholder="$t('reportCustomComponent.search.componentId')"
                clearable />
            </el-col>
            <el-col :span="8">
              <el-input v-model="searchForm.name" :placeholder="$t('reportCustomComponent.search.name')" clearable />
            </el-col>
            <el-col :span="6">
              <el-select v-model="searchForm.componentType"
                :placeholder="$t('reportCustomComponent.search.componentType')" clearable>
                <el-option v-for="item in componentTypeOptions" :key="item.value" :label="item.label"
                  :value="item.value" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报表组件列表 -->
    <el-row class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('reportCustomComponent.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right" @click="handleAdd">
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="componentId" :label="$t('reportCustomComponent.table.componentId')" align="center" />
            <el-table-column prop="name" :label="$t('reportCustomComponent.table.name')" align="center" />
            <el-table-column prop="componentType" :label="$t('reportCustomComponent.table.componentType')" align="center">
              <template slot-scope="scope">
                {{ formatComponentType(scope.row.componentType) }}
              </template>
            </el-table-column>
            <el-table-column prop="queryModel" :label="$t('reportCustomComponent.table.queryModel')" align="center">
              <template slot-scope="scope">
                {{ scope.row.queryModel === '1' ? 'SQL' : 'Java' }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('reportCustomComponent.table.remark')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="300">
              <template slot-scope="scope">
                <el-button v-if="scope.row.componentType === '1001'" size="mini" @click="handleSetCondition(scope.row)">
                  {{ $t('reportCustomComponent.button.setCondition') }}
                </el-button>
                <el-button v-if="scope.row.componentType === '1001'" size="mini" @click="handleSetFooter(scope.row)">
                  {{ $t('reportCustomComponent.button.setFooter') }}
                </el-button>
                <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加/编辑/删除组件 -->
    <add-report-custom-component :visible.sync="addVisible" @success="handleSuccess" />
    <edit-report-custom-component :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />
    <delete-report-custom-component :visible.sync="deleteVisible" :component-id="currentRow.componentId"
      @success="handleSuccess" />
  </div>
</template>

<script>
import {
  getReportCustomComponentList
} from '@/api/report/reportCustomComponentManageApi'
import AddReportCustomComponent from '@/components/report/AddReportCustomComponent'
import EditReportCustomComponent from '@/components/report/EditReportCustomComponent'
import DeleteReportCustomComponent from '@/components/report/DeleteReportCustomComponent'

export default {
  name: 'ReportCustomComponentManageList',
  components: {
    AddReportCustomComponent,
    EditReportCustomComponent,
    DeleteReportCustomComponent
  },
  data() {
    return {
      loading: false,
      searchForm: {
        componentId: '',
        name: '',
        componentType: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      componentTypeOptions: [
        { value: '1001', label: this.$t('reportCustomComponent.type.table') },
        { value: '2002', label: this.$t('reportCustomComponent.type.pie') },
        { value: '3003', label: this.$t('reportCustomComponent.type.input') },
        { value: '4004', label: this.$t('reportCustomComponent.type.select') },
        { value: '5005', label: this.$t('reportCustomComponent.type.date') }
      ],
      addVisible: false,
      editVisible: false,
      deleteVisible: false,
      currentRow: {}
    }
  },
  created() {
    this.getList()
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
        const { data, total } = await getReportCustomComponentList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('reportCustomComponent.fetchError'))
      } finally {
        this.loading = false
      }
    },
    formatComponentType(type) {
      const map = {
        '1001': this.$t('reportCustomComponent.type.table'),
        '2002': this.$t('reportCustomComponent.type.pie'),
        '3003': this.$t('reportCustomComponent.type.input'),
        '4004': this.$t('reportCustomComponent.type.select'),
        '5005': this.$t('reportCustomComponent.type.date')
      }
      return map[type] || type
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        componentId: '',
        name: '',
        componentType: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.deleteVisible = true
    },
    handleSetCondition(row) {
      this.$router.push({
        path: '/views/report/componentConditionManage',
        query: {
          componentId: row.componentId,
          componentName: row.name
        }
      })
    },
    handleSetFooter(row) {
      this.$router.push({
        path: '/views/report/reportCustomComponentFooterManage',
        query: {
          componentId: row.componentId,
          componentName: row.name
        }
      })
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.getList()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-custom-component-container {
  padding: 15px;

  .mt-20 {
    margin-top: 20px;
  }
}
</style>