<template>
  <div class="activities-type-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('activitiesTypeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.typeCd" :placeholder="$t('activitiesTypeManage.search.typeCd')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.typeName" :placeholder="$t('activitiesTypeManage.search.typeName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.defaultShow" :placeholder="$t('activitiesTypeManage.search.defaultShow')"
            style="width:100%">
            <el-option :label="$t('activitiesTypeManage.search.all')" value="" />
            <el-option :label="$t('activitiesTypeManage.search.yes')" value="Y" />
            <el-option :label="$t('activitiesTypeManage.search.no')" value="N" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('activitiesTypeManage.list.title') }}</span>
        <el-button type="primary" style="float: right;" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="typeCd" :label="$t('activitiesTypeManage.table.typeCd')" align="center" />
        <el-table-column prop="typeName" :label="$t('activitiesTypeManage.table.typeName')" align="center" />
        <el-table-column prop="typeDesc" :label="$t('activitiesTypeManage.table.typeDesc')" align="center" />
        <el-table-column prop="seq" :label="$t('activitiesTypeManage.table.seq')" align="center" />
        <el-table-column prop="defaultShow" :label="$t('activitiesTypeManage.table.defaultShow')" align="center">
          <template slot-scope="scope">
            {{ scope.row.defaultShow === 'Y' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
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
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-activities-type ref="addDialog" @success="handleSuccess" />
    <edit-activities-type ref="editDialog" @success="handleSuccess" />
    <delete-activities-type ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryActivitiesType } from '@/api/oa/activitiesTypeManageApi'
import AddActivitiesType from '@/components/oa/addActivitiesType'
import EditActivitiesType from '@/components/oa/editActivitiesType'
import DeleteActivitiesType from '@/components/oa/deleteActivitiesType'

export default {
  name: 'ActivitiesTypeManageList',
  components: {
    AddActivitiesType,
    EditActivitiesType,
    DeleteActivitiesType
  },
  data() {
    return {
      loading: false,
      searchForm: {
        typeCd: '',
        typeName: '',
        defaultShow: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
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
        const { data, total } = await queryActivitiesType(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('activitiesTypeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        typeCd: '',
        typeName: '',
        defaultShow: '',
        communityId: getCommunityId()
      }
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
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
.activities-type-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
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