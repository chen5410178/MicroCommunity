<template>
  <div class="schedule-classes-staff-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('scheduleClassesStaffManage.title') }}</span>
        <div class="header-tools">
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="openAddModal">
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="staffId" :label="$t('scheduleClassesStaffManage.staffId')" align="center">
        </el-table-column>
        <el-table-column prop="staffName" :label="$t('scheduleClassesStaffManage.staffName')" align="center">
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('scheduleClassesStaffManage.createTime')" align="center">
        </el-table-column>
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
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
      </el-pagination>
    </el-card>

    <add-schedule-classes-staff ref="addModal" @success="fetchData" />
    <delete-schedule-classes-staff ref="deleteModal" @success="fetchData" />
  </div>
</template>

<script>
import { listScheduleClassesStaff } from '@/api/org/scheduleClassesStaffManageApi'
import AddScheduleClassesStaff from '@/components/org/addScheduleClassesStaff'
import DeleteScheduleClassesStaff from '@/components/org/deleteScheduleClassesStaff'

export default {
  name: 'ScheduleClassesStaffManageList',
  components: {
    AddScheduleClassesStaff,
    DeleteScheduleClassesStaff
  },
  data() {
    return {
      loading: false,
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      queryParams: {
        scheduleId: ''
      }
    }
  },
  created() {
    this.queryParams.scheduleId = this.$route.query.scheduleId
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.queryParams
        }
        const { data, total } = await listScheduleClassesStaff(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },
    goBack() {
      this.$router.go(-1)
    },
    openAddModal() {
      this.$refs.addModal.open(this.queryParams.scheduleId)
    },
    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.schedule-classes-staff-manage-container {
  padding: 20px;

  .clearfix {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .header-tools {
    display: flex;
    gap: 10px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>