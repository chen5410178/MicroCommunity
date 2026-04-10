<template>
  <div class="schedule-classes-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('scheduleClasses.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.nameLike" :placeholder="$t('scheduleClasses.search.nameLike')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('scheduleClasses.search.state')" style="width:100%">
            <el-option :label="$t('scheduleClasses.search.allState')" value="" />
            <el-option v-for="item in states" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 排班设置 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between ">
        <span>{{ $t('scheduleClasses.list.title') }}</span>
        <el-button type="primary" style="float: right;" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="name" :label="$t('scheduleClasses.table.name')" align="center" />
        <el-table-column prop="scheduleTypeName" :label="$t('scheduleClasses.table.scheduleType')" align="center" />
        <el-table-column prop="scheduleCycle" :label="$t('scheduleClasses.table.scheduleCycle')" align="center" />
        <el-table-column prop="computeTime" :label="$t('scheduleClasses.table.computeTime')" align="center" />
        <el-table-column :label="$t('scheduleClasses.table.staff')" align="center">
          <template slot-scope="scope">
            {{ scope.row.staffCount }} (
            <el-link type="primary" @click="handleStaff(scope.row)">
              {{ $t('scheduleClasses.table.staffLink') }}
            </el-link>
            )
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('scheduleClasses.table.state')" align="center" />
        <el-table-column prop="createTime" :label="$t('scheduleClasses.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
            <el-button v-if="scope.row.state === '1001'" size="mini" type="warning" @click="handleDisable(scope.row)">
              {{ $t('common.disabled') }}
            </el-button>
            <el-button v-else size="mini" type="success" @click="handleEnable(scope.row)">
              {{ $t('common.enabled') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 删除对话框 -->
    <delete-schedule-classes ref="deleteDialog" @success="handleSuccess" />

    <!-- 状态对话框 -->
    <schedule-classes-state ref="stateDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listScheduleClasses } from '@/api/org/scheduleClassesApi'
import {getDict} from '@/api/community/communityApi'
import DeleteScheduleClasses from '@/components/org/deleteScheduleClasses'
import ScheduleClassesState from '@/components/org/scheduleClassesState'

export default {
  name: 'ScheduleClassesList',
  components: {
    DeleteScheduleClasses,
    ScheduleClassesState
  },
  data() {
    return {
      loading: false,
      searchForm: {
        nameLike: '',
        state: ''
      },
      states: [],
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
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
          ...this.searchForm
        }
        const { data, total } = await listScheduleClasses(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('scheduleClasses.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        const data = await getDict('schedule_classes', 'state')
        this.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$router.push('/views/org/addScheduleClasses')
    },
    handleEdit(row) {
      this.$router.push(`/views/org/editScheduleClasses?scheduleId=${row.scheduleId}`)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleEnable(row) {
      this.$refs.stateDialog.open({
        scheduleId: row.scheduleId,
        stateName: this.$t('common.enabled'),
        state: '1001'
      })
    },
    handleDisable(row) {
      this.$refs.stateDialog.open({
        scheduleId: row.scheduleId,
        stateName: this.$t('common.disabled'),
        state: '2002'
      })
    },
    handleStaff(row) {
      this.$router.push(`/views/org/scheduleClassesStaffManage?scheduleId=${row.scheduleId}`)
    },
    handleSuccess() {
      this.getList()
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
.schedule-classes-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>