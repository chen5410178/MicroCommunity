<template>
  <div class="market-sms-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketSms.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.smsName" :placeholder="$t('marketSms.search.smsName')" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.smsType" :placeholder="$t('marketSms.search.smsType')" clearable>
            <el-option v-for="item in smsTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
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
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketSms.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="smsId" :label="$t('marketSms.table.smsId')" align="center" />
        <el-table-column prop="smsName" :label="$t('marketSms.table.smsName')" align="center" />
        <el-table-column prop="smsType" :label="$t('marketSms.table.smsType')" align="center" />
        <el-table-column prop="createTime" :label="$t('marketSms.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('marketSms.table.remark')" align="center" />
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
    <add-market-sms ref="addMarketSms" @success="handleSuccess" />
    <edit-market-sms ref="editMarketSms" @success="handleSuccess" />
    <delete-market-sms ref="deleteMarketSms" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketSms } from '@/api/market/marketSmsManageApi'
import AddMarketSms from '@/components/market/AddMarketSms'
import EditMarketSms from '@/components/market/EditMarketSms'
import DeleteMarketSms from '@/components/market/DeleteMarketSms'

export default {
  name: 'MarketSmsManageList',
  components: {
    AddMarketSms,
    EditMarketSms,
    DeleteMarketSms
  },
  data() {
    return {
      loading: false,
      searchForm: {
        smsName: '',
        smsType: ''
      },
      tableData: [],
      smsTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getDictData()
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
        const { data, total } = await listMarketSms(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('marketSms.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        const data = await this.$store.dispatch('dict/getDict', {
          dictType: 'market_sms_key',
          dictName: 'sms_type'
        })
        this.smsTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addMarketSms.open()
    },
    handleEdit(row) {
      this.$refs.editMarketSms.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMarketSms.open(row)
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
.market-sms-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>