<template>
  <div class="housekeeping-sj-serv-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('housekeepingSjServ.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.servName" :placeholder="$t('housekeepingSjServ.search.servName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.hktId" :placeholder="$t('housekeepingSjServ.search.hktId')" clearable>
            <el-option v-for="item in housekeepingTypes" :key="item.hktId" :label="item.hktName" :value="item.hktId" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 上架服务列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('housekeepingSjServ.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="listHousekeepingServsToDay">
            <i class="el-icon-plus"></i>
            {{ $t('housekeepingSjServ.list.today') }}
          </el-button>
          <el-button type="primary" size="small" style="margin-left: 10px" @click="listHousekeepingServsAll">
            {{ $t('housekeepingSjServ.list.all') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="servId" :label="$t('housekeepingSjServ.table.servId')" align="center" />
        <el-table-column prop="servName" :label="$t('housekeepingSjServ.table.servName')" align="center" />
        <el-table-column prop="servDesc" :label="$t('housekeepingSjServ.table.servDesc')" align="center" />
        <el-table-column prop="hktName" :label="$t('housekeepingSjServ.table.hktName')" align="center" />
        <el-table-column :label="$t('housekeepingSjServ.table.repairWay')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.repairWay === '100'">{{ $t('housekeepingSjServ.repairWay.grab') }}</span>
            <span v-else-if="scope.row.repairWay === '200'">{{ $t('housekeepingSjServ.repairWay.assign') }}</span>
            <span v-else-if="scope.row.repairWay === '300'">{{ $t('housekeepingSjServ.repairWay.roundRobin') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('housekeepingSjServ.table.returnVisitFlag')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.returnVisitFlag === '001'">{{ $t('housekeepingSjServ.returnVisit.none') }}</span>
            <span v-else-if="scope.row.returnVisitFlag === '002'">{{ $t('housekeepingSjServ.returnVisit.ratedNoVisit')
            }}</span>
            <span v-else-if="scope.row.returnVisitFlag === '003'">{{ $t('housekeepingSjServ.returnVisit.allVisit')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" :label="$t('housekeepingSjServ.table.sales')" align="center" />
        <el-table-column prop="defaultFee" :label="$t('housekeepingSjServ.table.defaultFee')" align="center" />
        <el-table-column prop="sort" :label="$t('housekeepingSjServ.table.sort')" align="center" />
        <el-table-column :label="$t('housekeepingSjServ.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === '1001' ? $t('housekeepingSjServ.state.notOnShelf') :
              $t('housekeepingSjServ.state.onShelf') }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { queryHousekeepingServ, queryHousekeepingType } from '@/api/mall/housekeepingSjServApi'

export default {
  name: 'HousekeepingSjServList',
  data() {
    return {
      loading: false,
      searchForm: {
        servName: '',
        hktId: '',
        state: '2002',
        createTime: '',
        mallApiCode: 'queryHousekeepingServBmoImpl'
      },
      tableData: [],
      housekeepingTypes: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getHousekeepingTypes()
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
        const { data, total } = await queryHousekeepingServ(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('housekeepingSjServ.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getHousekeepingTypes() {
      try {
        const params = {
          page: 1,
          row: 50,
          typeCd: 1001,
          shopId: ''
        }
        const { data } = await queryHousekeepingType(params)
        this.housekeepingTypes = data
      } catch (error) {
        this.$message.error(this.$t('housekeepingSjServ.fetchTypeError'))
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    listHousekeepingServsToDay() {
      this.searchForm.createTime = this.$dayjs().format('YYYY-MM-DD HH:mm:ss')
      this.handleSearch()
    },
    listHousekeepingServsAll() {
      this.searchForm.createTime = ''
      this.handleSearch()
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
.housekeeping-sj-serv-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-select {
      width: 100%;
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