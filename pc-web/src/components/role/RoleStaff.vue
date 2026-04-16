<template>
  <div class="role-staff">
    <div class="search-wrapper">
      <el-input v-model="searchForm.staffName" :placeholder="$t('role.staffName')"
        style="width: 200px; margin-right: 10px" />
      <el-button type="primary" @click="queryStaffs">
        <i class="el-icon-search"></i>
        {{ $t('common.search') }}
      </el-button>
      <el-button type="primary" @click="openAddModal">
        <i class="el-icon-plus"></i>
        {{ $t('role.staff') }}
      </el-button>
    </div>

    <el-table :data="staffs" border style="width: 100%; margin-top: 15px">
      <el-table-column prop="name" :label="$t('role.staffName')" align="center" />
      <el-table-column prop="tel" :label="$t('role.tel')" align="center" />
      <el-table-column prop="email" :label="$t('role.email')" align="center" />
      <el-table-column prop="address" :label="$t('role.address')" align="center" />
      <el-table-column :label="$t('role.sex')" align="center">
        <template slot-scope="scope">
          {{ scope.row.sex == 0 ? $t('role.male') : $t('role.female') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('role.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
            {{ $t('role.delete') }}
          </el-button>
          <el-button size="mini" type="primary" @click="viewDetail(scope.row)">
            {{ $t('role.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <add-role-staff ref="addRoleStaff" :role-id="pgId" @success="loadStaffs" />
    <delete-role-staff ref="deleteRoleStaff" @success="loadStaffs" />
  </div>
</template>

<script>
import { listRoleStaff } from '@/api/role/roleApi'
import AddRoleStaff from './AddRoleStaff'
import DeleteRoleStaff from './DeleteRoleStaff'

export default {
  name: 'RoleStaff',
  components: {
    AddRoleStaff,
    DeleteRoleStaff
  },
  props: {
    pgId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      searchForm: {
        staffName: ''
      },
      staffs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadStaffs()
  },
  methods: {
    async loadStaffs() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          roleId: this.pgId,
          userName: this.searchForm.staffName
        }
        const res = await listRoleStaff(params)
        this.staffs = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    queryStaffs() {
      this.page.current = 1
      this.loadStaffs()
    },
    openAddModal() {
      this.$refs.addRoleStaff.show()
    },
    openDeleteModal(staff) {
      this.$refs.deleteRoleStaff.show(staff, this.pgId)
    },
    viewDetail(staff) {
      this.$router.push(`/views/staff/staffDetail?staffId=${staff.userId}`)
    },
    handleSizeChange(size) {
      this.page.size = size
      this.loadStaffs()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.loadStaffs()
    }
  }
}
</script>

<style scoped>
.role-staff {
  margin: 15px;

  .search-wrapper {
    margin-bottom: 15px;
    text-align: right;
  }

  .el-pagination {
    margin-top: 15px;
    margin-bottom: 15px;
    text-align: right;
  }
}
</style>