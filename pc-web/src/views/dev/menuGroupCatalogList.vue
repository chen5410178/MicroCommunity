<template>
  <div class="menuGroupCatalog-container">
    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title"> {{ catalogName + $t('menuGroupCatalog.list.title') }}</div>
        <div>
          <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
          <el-button type="warn" @click="handleBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="gcId" :label="$t('menuGroupCatalog.table.gcId')" />
        <el-table-column prop="mgName" :label="$t('menuGroupCatalog.table.mgName')" />
        <el-table-column prop="gId" :label="$t('menuGroupCatalog.table.gId')" />
        <!-- <el-table-column
          prop="storeType"
          :label="$t('menuGroupCatalog.table.storeType')"
        /> -->
        <!-- <el-table-column
          prop="createTime"
          :label="$t('menuGroupCatalog.table.createTime')"
        /> -->
        <el-table-column :label="$t('common.operation')" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
    <AddMenuGroupCatalog :visible.sync="addVisible" :ca-id="searchForm.caId" :store-type="storeType" @success="handleSuccess" />

    <DelMenuGroupCatalog :visible.sync="delVisible" :gc-id="currentRow.gcId"  :store-type="currentRow.storeType" @success="handleSuccess" />
  </div>
</template>

<script>
import { getMenuGroupCatalogList } from '@/api/dev/menuGroupCatalogApi'
import AddMenuGroupCatalog from '@/components/dev/AddMenuGroupCatalog'
import DelMenuGroupCatalog from '@/components/dev/DelMenuGroupCatalog'

export default {
  name: 'menuGroupCatalogList',
  components: {
    AddMenuGroupCatalog,
    DelMenuGroupCatalog
  },
  data() {
    return {
      loading: false,
      catalogName: '',
      storeType: '',
      searchForm: {
        caId: '',
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
    const { caId, catalogName, storeType } = this.$route.query
    this.searchForm.caId = caId
    this.catalogName = catalogName
    this.storeType = storeType
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          caId: this.searchForm.caId,
          gId: this.searchForm.gId,
          storeType: this.searchForm.storeType,
        }
        const { data, total } = await getMenuGroupCatalogList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('menuGroupCatalog.fetchError'))
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
    handleBack(){
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.menuGroupCatalog-container {
  padding: 0;
  margin: 0;

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
</style>