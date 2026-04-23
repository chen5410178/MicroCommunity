<template>
  <div class="cache-container ">
    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('cache.list.title') }}</div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="id" :label="$t('cache.table.cacheCode')" />
        <el-table-column prop="cacheCode" :label="$t('cache.table.serviceCode')" />
        <el-table-column prop="cacheName" :label="$t('cache.table.name')" />
        <el-table-column :label="$t('common.operation')" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleFlush(scope.row)">{{ $t('cache.flush') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>

  </div>
</template>

<script>
import { getCacheList, flushCache } from '@/api/dev/cacheApi'

export default {
  name: 'cacheList',
  components: {

  },
  data() {
    return {
      loading: false,
      searchForm: {
        cacheCode: '',
        serviceCode: '',
        name: '',
        seq: '',
        group: '',
        url: '',
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
        }
        const { caches, total } = await getCacheList(params)
        this.tableData = caches
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('cache.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },

    async handleFlush(row) {
      try {
        this.loading = true
        const params = {
          id: row.id,
        }
        const { code,msg } = await flushCache(params)
        if (code == 0) {
           this.$message.success(msg)
        } else {
          // 如果code不是0，显示错误消息
          this.$message.error(msg)
        }

      } catch (error) {
        console.log(error)
       // this.$message.error(this.$t('cache.fetchError'))
      } finally {
        this.loading = false
      }
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
.cache-container {
  padding: 15px;
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