<template>
  <div class="data-privilege-staff">

    <el-form :inline="true" :model="searchForm" class="text-right">
      <el-form-item >
        <el-input v-model="searchForm.staffName" :placeholder="$t('dataPrivilege.inputStaffName')" clearable>
        </el-input>
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchForm.tel" :placeholder="$t('dataPrivilege.inputPhone')" clearable>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
        <el-button @click="handleReset">
          <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
        </el-button>
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>{{ $t('dataPrivilege.associateStaff') }}
        </el-button>
      </el-form-item>
    </el-form>


    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="staffName" :label="$t('dataPrivilege.name')" align="center">
      </el-table-column>
      <el-table-column prop="tel" :label="$t('dataPrivilege.phone')" align="center">
      </el-table-column>
      <el-table-column prop="email" :label="$t('dataPrivilege.email')" align="center">
      </el-table-column>
      <el-table-column prop="address" :label="$t('dataPrivilege.address')" align="center">
      </el-table-column>
      <el-table-column :label="$t('dataPrivilege.gender')" align="center">
        <template slot-scope="scope">
          {{ scope.row.sex === 0 ? $t('dataPrivilege.male') : $t('dataPrivilege.female') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
          <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>

    <add-data-privilege-staff ref="addDataPrivilegeStaff" @success="loadData" />
    <delete-data-privilege-staff ref="deleteDataPrivilegeStaff" @success="loadData" />
  </div>
</template>

<script>
import { listDataPrivilegeStaff } from '@/api/org/dataPrivilegeManageApi'
import AddDataPrivilegeStaff from '@/components/org/addDataPrivilegeStaff'
import DeleteDataPrivilegeStaff from '@/components/org/deleteDataPrivilegeStaff'

export default {
  name: 'DataPrivilegeStaff',
  components: {
    AddDataPrivilegeStaff,
    DeleteDataPrivilegeStaff
  },
  data() {
    return {
      dpId: '',
      loading: false,
      searchForm: {
        staffName: '',
        tel: ''
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
          dpId: this.dpId,
          staffName: this.searchForm.staffName,
          tel: this.searchForm.tel
        }
        const { data, total } = await listDataPrivilegeStaff(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load data privilege staff:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData(this.dpId)
    },
    handleReset() {
      this.searchForm = {
        staffName: '',
        tel: ''
      }
      this.pagination.current = 1
      this.loadData(this.dpId)
    },
    handleAdd() {
      this.$refs.addDataPrivilegeStaff.open({ dpId: this.dpId })
    },
    handleDelete(row) {
      this.$refs.deleteDataPrivilegeStaff.open(row)
    },
    handleDetail(row) {
      this.$router.push(`/views/staff/staffDetail?staffId=${row.staffId}`)
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
.data-privilege-staff {
  padding: 20px;
  padding-top:10px;
}

.search-card {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 15px;
  text-align: right;
}
</style>