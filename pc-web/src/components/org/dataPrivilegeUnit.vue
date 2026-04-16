<template>
  <div class="data-privilege-unit">
    <div class="action-bar text-right">
      <el-button type="primary" size="small" @click="handleAdd">
        <i class="el-icon-plus"></i>{{ $t('dataPrivilege.associateUnit') }}
      </el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="floorNum" :label="$t('dataPrivilege.building')" align="center">
      </el-table-column>
      <el-table-column prop="unitNum" :label="$t('dataPrivilege.unit')" align="center">
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>

    <add-data-privilege-unit ref="addDataPrivilegeUnit" @success="loadData" />
    <delete-data-privilege-unit ref="deleteDataPrivilegeUnit" @success="loadData" />
  </div>
</template>

<script>
import { listDataPrivilegeUnit } from '@/api/org/dataPrivilegeManageApi'
import AddDataPrivilegeUnit from '@/components/org/addDataPrivilegeUnit'
import DeleteDataPrivilegeUnit from '@/components/org/deleteDataPrivilegeUnit'

export default {
  name: 'DataPrivilegeUnit',
  components: {
    AddDataPrivilegeUnit,
    DeleteDataPrivilegeUnit
  },
  data() {
    return {
      dpId: '',
      loading: false,
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {

  },
  methods: {
    open(dpId) {
      this.dpId = dpId
      this.loadData()
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          dpId: this.dpId
        }
        const { data, total } = await listDataPrivilegeUnit(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load data privilege units:', error)
      } finally {
        this.loading = false
      }
    },
    handleAdd() {
      this.$refs.addDataPrivilegeUnit.open({ dpId: this.dpId })
    },
    handleDelete(row) {
      this.$refs.deleteDataPrivilegeUnit.open(row)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData(this.dpId)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData(this.dpId)
    }
  },
}
</script>

<style scoped>
.data-privilege-unit {
  padding: 20px;
  padding-top: 0;
}

.action-bar {
  margin-bottom: 15px;
}
</style>