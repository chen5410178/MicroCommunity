<template>
  <div class="market-text-container">
    <el-card>
      <div class="search-wrapper">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model="searchForm.name" :placeholder="$t('marketText.name')" clearable
              @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="6">
            <el-select v-model="searchForm.sendRate" :placeholder="$t('marketText.sendRate')" clearable>
              <el-option v-for="item in sendRateOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleSearch">
              {{ $t('marketText.search') }}
            </el-button>
            <el-button type="primary" @click="handleAdd">
              {{ $t('marketText.add') }}
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="name" :label="$t('marketText.name')" align="center" />
        <el-table-column prop="sendRateName" :label="$t('marketText.sendRate')" align="center" />
        <el-table-column prop="textContent" :label="$t('marketText.content')" align="center" />
        <el-table-column prop="smsName" :label="$t('marketText.config')" align="center" />
        <el-table-column prop="createTime" :label="$t('marketText.createTime')" align="center" />
        <el-table-column :label="$t('marketText.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('marketText.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('marketText.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-market-text ref="addMarketText" @success="handleSuccess" />
    <edit-market-text ref="editMarketText" @success="handleSuccess" />
    <delete-market-text ref="deleteMarketText" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketText } from '@/api/market/marketWayApi'
import AddMarketText from './AddMarketText'
import EditMarketText from './EditMarketText'
import DeleteMarketText from './DeleteMarketText'

export default {
  name: 'MarketWayText',
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        sendRate: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      sendRateOptions: [
        { value: '1001', label: this.$t('marketText.sendRateOptions.daily') },
        { value: '2002', label: this.$t('marketText.sendRateOptions.monthly') },
        { value: '3003', label: this.$t('marketText.sendRateOptions.unlimited') }
      ]
    }
  },
  components: {
    AddMarketText,
    EditMarketText,
    DeleteMarketText
  },
  created() {
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
        const { data, total } = await listMarketText(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addMarketText.open()
    },
    handleEdit(row) {
      this.$refs.editMarketText.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMarketText.open(row)
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
    },
    refreshList() {
      this.pagination.current = 1
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.market-text-container {
  .search-wrapper {
    margin-bottom: 20px;

    .el-select {
      width: 100%;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>