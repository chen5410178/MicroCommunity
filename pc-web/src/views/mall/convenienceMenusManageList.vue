<template>
  <div class="convenience-menus-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('convenienceMenus.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="openAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="convenienceMenusId" :label="$t('convenienceMenus.assetId')" align="center" />
        <el-table-column prop="name" :label="$t('convenienceMenus.menuName')" align="center" />
        <el-table-column :label="$t('convenienceMenus.imageUrl')" align="center">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 5px;" :src="scope.row.icon"
              :preview-src-list="[scope.row.icon]" fit="cover"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="url" :label="$t('convenienceMenus.pagePath')" align="center" />
        <el-table-column prop="seq" :label="$t('convenienceMenus.displayOrder')" align="center" />
        <el-table-column prop="remark" :label="$t('convenienceMenus.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-convenience-menus ref="addModal" @success="fetchData" />
    <delete-convenience-menus ref="deleteModal" @success="fetchData" />
  </div>
</template>

<script>
import { getConvenienceMenusList } from '@/api/mall/convenienceMenusManageApi'
import AddConvenienceMenus from '@/components/mall/AddConvenienceMenus'
import DeleteConvenienceMenus from '@/components/mall/DeleteConvenienceMenus'

export default {
  name: 'ConvenienceMenusManageList',
  components: {
    AddConvenienceMenus,
    DeleteConvenienceMenus
  },
  data() {
    return {
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      searchForm: {
        name: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await getConvenienceMenusList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('convenienceMenus.fetchError'))
      }
    },
    openAddModal() {
      this.$refs.addModal.open()
    },
    openEditModal(row) {
      this.$refs.editModal.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.convenience-menus-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>