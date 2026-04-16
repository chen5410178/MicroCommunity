<template>
  <div class="dict-container">
    <div class="menu-group-wrapper">
      <div class="group-content">
        <el-menu :default-active="searchForm.specId" class="group-menu" @select="handleSpec">
          <el-menu-item v-for="(item, index) in specs" :key="index" :index="item.specId">
            <span>{{ item.specName }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>

    <!-- 右侧菜单列表 -->
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('dict.search.title') }}</div>
        <div class="search-content">
          <el-input v-model="searchForm.name" :placeholder="$t('dict.search.name')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('dict.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="searchForm.specId">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="name" :label="$t('dict.table.name')" />
          <el-table-column prop="tableName" :label="$t('dict.table.tableName')" />
          <el-table-column prop="tableColumns" :label="$t('dict.table.tableColumns')" />
          <el-table-column prop="statusCd" :label="$t('dict.table.statusCd')" />
          <el-table-column prop="description" :label="$t('dict.table.description')" />
          <!-- <el-table-column prop="createTime" :label="$t('dict.table.createTime')" /> -->


          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <AddDict :visible.sync="addVisible" :spec-id="searchForm.specId" @success="handleSuccess" />

    <EditDict :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelDict :visible.sync="delVisible" :id="currentRow.id" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDictList,getDictSpecList } from '@/api/dev/dictApi'
import AddDict from '@/components/dev/AddDict'
import EditDict from '@/components/dev/EditDict'
import DelDict from '@/components/dev/DelDict'

export default {
  name: 'dictList',
  components: {
    AddDict,
    EditDict,
    DelDict
  },
  data() {
    return {
      specs:[],
      loading: false,
      searchForm: {
        name: '',
        specId: '',
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      delVisible: false,
      currentRow: {}
    }
  },
  created() {
    this.getDictSpecList()
    this.getList()
  },
  methods: {
    async getDictSpecList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
        }
        const { data } = await getDictSpecList(params)
        this.specs = data
      } catch (error) {
        this.$message.error(this.$t('dict.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name,
          specId: this.searchForm.specId,
        }
        const { data, total } = await getDictList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('dict.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.delVisible = true
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
    },
    handleSpec(specId) {
      this.searchForm.specId = specId
      this.getList()
    },
  }
}
</script>

<style lang="scss" scoped>
.dict-container {
  padding: 0;
  margin: 0;
  display: flex;
  background-color: #f0f2f5;

  .menu-group-wrapper {
    width: 250px;
    height: calc(100vh - 70px);
    background-color: #fff;
    border-right: 1px solid #e6e6e6;
    display: flex;
    flex-direction: column;

    .group-header {
      padding: 20px;
      border-bottom: 1px solid #e6e6e6;

      .header-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .group-content {
      flex: 1;
      overflow-y: auto;

      .group-menu {
        border-right: none;
      }
    }
  }

  .menu-list-wrapper {
    flex: 1;
    padding-left: 10px;
    overflow-y: auto;

    .search-wrapper {
      background-color: #fff;
      padding: 20px;
      margin-bottom: 20px;
      border-radius: 4px;

      .search-title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 20px;
        text-align: left;
        color: #333;
      }

      .search-content {
        display: flex;
        align-items: center;
        gap: 20px;

        .search-item {
          width: 200px;
        }
      }
    }

    .list-wrapper {
      background-color: #fff;
      padding: 20px;
      border-radius: 4px;

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
}
</style>