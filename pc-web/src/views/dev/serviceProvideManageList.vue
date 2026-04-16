<template>
  <div class="service-provide-manage-container animated fadeInRight">

    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('serviceProvideManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.serviceCode" :placeholder="$t('serviceProvideManage.search.serviceCode')"
            clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.name" :placeholder="$t('serviceProvideManage.search.name')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.queryModel" :placeholder="$t('serviceProvideManage.search.queryModel')"
            clearable>
            <el-option v-for="item in queryModelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
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
    </el-card>


    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('serviceProvideManage.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
              <i class="el-icon-plus"></i>
              {{ $t('serviceProvideManage.list.developService') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="id" :label="$t('serviceProvideManage.table.provideId')" align="center" />
            <el-table-column prop="name" :label="$t('serviceProvideManage.table.name')" align="center" />
            <el-table-column prop="serviceCode" :label="$t('serviceProvideManage.table.serviceCode')" align="center" />
            <el-table-column prop="params" :label="$t('serviceProvideManage.table.params')" align="center" />
            <el-table-column prop="queryModel" :label="$t('serviceProvideManage.table.queryModel')" align="center">
              <template slot-scope="scope">
                {{ formatQueryModel(scope.row.queryModel) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleEdit(scope.row)">
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
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <edit-service-provide :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />
    <delete-service-provide :visible.sync="deleteVisible" :service-id="currentRow.id" @success="handleSuccess" />
  </div>
</template>

<script>
import { listServiceProvides } from '@/api/dev/serviceProvideManageApi'
import EditServiceProvide from '@/components/dev/EditServiceProvide'
import DeleteServiceProvide from '@/components/dev/DeleteServiceProvide'

export default {
  name: 'ServiceProvideManageList',
  components: {
    EditServiceProvide,
    DeleteServiceProvide
  },
  data() {
    return {
      loading: false,
      searchForm: {
        serviceCode: '',
        name: '',
        queryModel: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      queryModelOptions: [
        { value: '1', label: 'SQL方式' },
        { value: '2', label: this.$t('serviceProvideManage.options.proc') },
        { value: '3', label: 'Java方式' }
      ],
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
        const { serviceProvides, total } = await listServiceProvides(params)
        this.tableData = serviceProvides
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('serviceProvideManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    formatQueryModel(value) {
      switch (value) {
        case '1': return 'SQL方式'
        case '2': return this.$t('serviceProvideManage.options.proc')
        case '3': return 'Java方式'
        default: return ''
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        serviceCode: '',
        name: '',
        queryModel: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$router.push('/views/dev/devServiceProvide')
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.deleteVisible = true
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
.service-provide-manage-container {
  padding: 20px;
  background-color: #f5f7fa;

  .el-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>