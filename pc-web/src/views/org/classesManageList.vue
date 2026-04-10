<template>
  <div class="classes-manage-container padding">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('classesManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('classesManage.search.namePlaceholder')" clearable />
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

    <!-- 班次信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between ">
        <span>{{ $t('classesManage.list.title') }}</span>
        <div style="float: right">

          <el-button type="primary" size="small" @click="handleAdd">
            <i class="el-icon-plus"></i>
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="classesId" :label="$t('classesManage.table.classesId')" align="center" />
        <el-table-column prop="name" :label="$t('classesManage.table.name')" align="center" />
        <el-table-column :label="$t('classesManage.table.times')" align="center">
          <template slot-scope="scope">
            <div v-for="(time, index) in scope.row.times" :key="index">
              {{ time.startTime }}~{{ time.endTime }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('classesManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === '1001' ? $t('common.enabled') : $t('common.disabled') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('classesManage.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('classesManage.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300" fixed="right">
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

    <!-- 组件 -->
    <add-classes ref="addClasses" @success="handleSuccess" />
    <edit-classes ref="editClasses" @success="handleSuccess" />
    <delete-classes ref="deleteClasses" @success="handleSuccess" />
    <classes-state ref="classesState" @success="handleSuccess" />
  </div>
</template>

<script>
import { listClasses } from '@/api/org/classesManageApi'
import AddClasses from '@/components/org/addClasses'
import EditClasses from '@/components/org/editClasses'
import DeleteClasses from '@/components/org/deleteClasses'
import ClassesState from '@/components/org/classesState'

export default {
  name: 'ClassesManageList',
  components: {
    AddClasses,
    EditClasses,
    DeleteClasses,
    ClassesState
  },
  data() {
    return {
      loading: false,
      searchForm: {
        name: ''
      },
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
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name
        }
        const { data, total } = await listClasses(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('classesManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        name: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addClasses.open()
    },
    handleEdit(row) {
      this.$refs.editClasses.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteClasses.open(row)
    },
    handleEnable(row) {
      this.$refs.classesState.open({
        classesId: row.classesId,
        stateName: this.$t('common.enabled'),
        state: '1001'
      })
    },
    handleDisable(row) {
      this.$refs.classesState.open({
        classesId: row.classesId,
        stateName: this.$t('common.disabled'),
        state: '2002'
      })
    },
    handleShowDocument() {
      // TODO: 显示文档逻辑
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
.classes-manage-container {

  margin: 0;

  .search-wrapper {
    margin-bottom: 20px;

    .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>