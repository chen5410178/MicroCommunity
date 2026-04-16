<template>
  <div class="menu-user-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('menuUserManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.muId" :placeholder="$t('menuUserManage.search.muId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('menuUserManage.search.name')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.seq" :placeholder="$t('menuUserManage.search.seq')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 常用菜单列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('menuUserManage.list.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="handleAdd">
          <i class="el-icon-plus"></i> {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="muId" :label="$t('menuUserManage.table.muId')" align="center" />
        <el-table-column prop="name" :label="$t('menuUserManage.table.name')" align="center" />
        <el-table-column :label="$t('menuUserManage.table.icon')" align="center">
          <template slot-scope="scope">
            <i :class="scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column prop="seq" :label="$t('menuUserManage.table.seq')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
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

    <!-- 添加菜单弹窗 -->
    <add-menu-user ref="addMenuUser" @success="handleSuccess" />

    <!-- 删除菜单确认弹窗 -->
    <delete-menu-user ref="deleteMenuUser" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMenuUser } from '@/api/system/menuUserManageApi'
import AddMenuUser from '@/components/system/addMenuUser'
import DeleteMenuUser from '@/components/system/deleteMenuUser'

export default {
  name: 'MenuUserManageList',
  components: {
    AddMenuUser,
    DeleteMenuUser
  },
  data() {
    return {
      loading: false,
      searchForm: {
        muId: '',
        name: '',
        seq: ''
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
          ...this.searchForm
        }
        const { data, total } = await listMenuUser(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('menuUserManage.fetchError'))
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
        muId: '',
        name: '',
        seq: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addMenuUser.open()
    },
    handleDelete(row) {
      this.$refs.deleteMenuUser.open(row)
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
.menu-user-manage-container {
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