<template>
  <div class="reserve-catalog-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('reserveCatalogManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.name"
            :placeholder="$t('reserveCatalogManage.search.namePlaceholder')"
            clearable
          />
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.type"
            :placeholder="$t('reserveCatalogManage.search.typePlaceholder')"
            style="width:100%"
          >
            <el-option
              :label="$t('reserveCatalogManage.search.typeOptions.all')"
              value=""
            />
            <el-option
              :label="$t('reserveCatalogManage.search.typeOptions.dining')"
              value="1001"
            />
            <el-option
              :label="$t('reserveCatalogManage.search.typeOptions.service')"
              value="2002"
            />
          </el-select>
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
        <span>{{ $t('reserveCatalogManage.list.title') }}</span>
        <el-button
          type="primary"
          style="float: right;"
          @click="handleAdd"
        >
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="type"
          :label="$t('reserveCatalogManage.table.type')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.type === '1001' ? $t('reserveCatalogManage.table.typeOptions.dining') : $t('reserveCatalogManage.table.typeOptions.service') }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          :label="$t('reserveCatalogManage.table.name')"
          align="center"
        />
        <el-table-column
          prop="sort"
          :label="$t('reserveCatalogManage.table.sort')"
          align="center"
        />
        <el-table-column
          prop="state"
          :label="$t('reserveCatalogManage.table.state')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.state === '1001' ? $t('reserveCatalogManage.table.stateOptions.show') : $t('reserveCatalogManage.table.stateOptions.hide') }}
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('reserveCatalogManage.table.createTime')"
          align="center"
        />
        <el-table-column
          :label="$t('common.operation')"
          align="center"
          width="200"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              {{ $t('common.edit') }}
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 子组件 -->
    <add-reserve-catalog ref="addReserveCatalog" @success="handleSuccess" />
    <edit-reserve-catalog ref="editReserveCatalog" @success="handleSuccess" />
    <delete-reserve-catalog ref="deleteReserveCatalog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listReserveCatalog } from '@/api/scm/reserveCatalogManageApi'
import AddReserveCatalog from '@/components/scm/addReserveCatalog'
import EditReserveCatalog from '@/components/scm/editReserveCatalog'
import DeleteReserveCatalog from '@/components/scm/deleteReserveCatalog'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReserveCatalogManageList',
  components: {
    AddReserveCatalog,
    EditReserveCatalog,
    DeleteReserveCatalog
  },
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        type: '',
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
        const { data, total } = await listReserveCatalog(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('reserveCatalogManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addReserveCatalog.open()
    },
    handleEdit(row) {
      this.$refs.editReserveCatalog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteReserveCatalog.open(row)
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
.reserve-catalog-manage-container {
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