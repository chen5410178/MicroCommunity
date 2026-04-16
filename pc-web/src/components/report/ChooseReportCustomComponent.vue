<template>
  <el-dialog :title="$t('chooseReportCustomComponent.title')" :visible.sync="visible" width="80%" top="5vh">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-input v-model="searchText" :placeholder="$t('chooseReportCustomComponent.searchPlaceholder')"
          style="width: 300px; margin-right: 10px" />
        <el-button type="primary" @click="handleSearch">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="tableData" border style="width: 100%; margin-top: 20px" v-loading="loading">
      <el-table-column prop="componentId" :label="$t('chooseReportCustomComponent.componentId')" align="center" />
      <el-table-column prop="name" :label="$t('chooseReportCustomComponent.componentName')" align="center" />
      <el-table-column prop="componentType" :label="$t('chooseReportCustomComponent.componentType')" align="center" />
      <el-table-column prop="queryModel" :label="$t('chooseReportCustomComponent.queryModel')" align="center" />
      <el-table-column prop="remark" :label="$t('chooseReportCustomComponent.remark')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleSelect(scope.row)">
            {{ $t('common.select') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px" />
  </el-dialog>
</template>

<script>
import { listReportCustomComponent } from '@/api/report/reportCustomComponentRelManageApi'

export default {
  name: 'ChooseReportCustomComponent',
  data() {
    return {
      visible: false,
      searchText: '',
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.searchText = ''
      this.getList()
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          name: this.searchText
        }
        const { data, total } = await listReportCustomComponent(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('chooseReportCustomComponent.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleSelect(row) {
      this.$emit('selected', row)
      this.visible = false
    }
  }
}
</script>