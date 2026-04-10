<template>
  <div class="room-fee-import-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('roomFeeImport.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.importFeeId" :placeholder="$t('roomFeeImport.search.importFeeId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.feeTypeCd" :placeholder="$t('roomFeeImport.search.feeTypeCd')"
            style="width:100%">
            <el-option v-for="item in filteredFeeTypeCds" :key="item.statusCd" :label="item.name"
              :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 费用导入列表 -->
    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('roomFeeImport.table.title') }}</span>
        <el-button type="primary" style="float: right;" @click="handleImport">
          {{ $t('roomFeeImport.button.import') }}
        </el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="importFeeId" :label="$t('roomFeeImport.table.importFeeId')" align="center" />
        <el-table-column prop="feeTypeCdName" :label="$t('roomFeeImport.table.feeTypeCd')" align="center" />
        <el-table-column prop="createTime" :label="$t('roomFeeImport.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('roomFeeImport.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 导入组件 -->
    <import-room-fee ref="importRoomFee" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryImportFee } from '@/api/fee/roomFeeImportApi'
import { getDict } from '@/api/community/communityApi'
import ImportRoomFee from '@/components/fee/importRoomFee'

export default {
  name: 'RoomFeeImportList',
  components: {
    ImportRoomFee
  },
  data() {
    return {
      loading: false,
      searchForm: {
        importFeeId: '',
        feeTypeCd: ''
      },
      tableData: [],
      feeTypeCds: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
    this.getList()
  },
  computed: {
    filteredFeeTypeCds() {
      return this.feeTypeCds.filter(item => item.statusCd !== '888800010008')
    }
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          importFeeId: this.searchForm.importFeeId,
          feeTypeCd: this.searchForm.feeTypeCd
        }
        const { data, total } = await queryImportFee(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('roomFeeImport.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
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
    handleImport() {
      this.$refs.importRoomFee.open()
    },
    handleDetail(row) {
      this.$router.push({
        path: '/fee/roomFeeImportDetail',
        query: { importFeeId: row.importFeeId }
      })
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.room-fee-import-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }
}
</style>