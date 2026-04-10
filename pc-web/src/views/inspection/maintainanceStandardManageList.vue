<template>
  <div class="maintainance-standard-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainanceStandard.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.standardName" :placeholder="$t('maintainanceStandard.search.standardName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainanceStandard.list.title') }}</span>
        <el-button type="primary" size="small" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="standardName" :label="$t('maintainanceStandard.table.standardName')" align="center" />
        <el-table-column :label="$t('maintainanceStandard.table.itemCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.itemCount }} (
            <el-link type="primary" @click="handleItem(scope.row)">
              {{ $t('maintainanceStandard.table.item') }}
            </el-link>)
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('maintainanceStandard.table.remark')" align="center" />
        <el-table-column prop="createTime" :label="$t('maintainanceStandard.table.createTime')" align="center" />
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

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <AddMaintainanceStandard ref="addDialog" @success="handleSuccess" />
    <EditMaintainanceStandard ref="editDialog" @success="handleSuccess" />
    <DeleteMaintainanceStandard ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import {
  listMaintainanceStandard
} from '@/api/inspection/maintainanceStandardManageApi'
import AddMaintainanceStandard from '@/components/inspection/addMaintainanceStandard'
import EditMaintainanceStandard from '@/components/inspection/editMaintainanceStandard'
import DeleteMaintainanceStandard from '@/components/inspection/deleteMaintainanceStandard'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainanceStandardManageList',
  components: {
    AddMaintainanceStandard,
    EditMaintainanceStandard,
    DeleteMaintainanceStandard
  },
  data() {
    return {
      loading: false,
      searchForm: {
        standardName: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
    this.getList()
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
        const { data, total } = await listMaintainanceStandard(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('maintainanceStandard.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
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
    handleItem(row) {
      this.$router.push(`/views/inspection/maintainanceStandardItem?standardId=${row.standardId}`)
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
.maintainance-standard-container {
  padding: 20px;
  margin: 0;

  .search-wrapper {
    margin-bottom: 20px;

    .search-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 20px;
      text-align: left;
      color: #333;
    }
  }

  .list-wrapper {
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .list-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>