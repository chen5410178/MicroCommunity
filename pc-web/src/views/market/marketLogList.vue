<template>
  <div class="market-log-container">
    <!-- Search Conditions -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketLog.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.communityName" :placeholder="$t('marketLog.search.communityName')" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.sendWay" :placeholder="$t('marketLog.search.sendWay')" clearable>
            <el-option v-for="item in sendWayOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.businessType" :placeholder="$t('marketLog.search.businessType')" clearable>
            <el-option v-for="item in businessTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.personNameLike" :placeholder="$t('marketLog.search.personNameLike')" clearable />
        </el-col>
     
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="4">
          <el-date-picker v-model="searchForm.startTime" type="datetime" :placeholder="$t('marketLog.search.startTime')"
            value-format="yyyy-MM-dd HH:mm:ss" />
        </el-col>
        <el-col :span="4">
          <el-date-picker v-model="searchForm.endTime" type="datetime" :placeholder="$t('marketLog.search.endTime')"
            value-format="yyyy-MM-dd HH:mm:ss" />
        </el-col>
      </el-row>
    </el-card>

    <!-- Market Log List -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketLog.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="logId" :label="$t('marketLog.table.logId')" align="center" />
        <el-table-column prop="ruleName" :label="$t('marketLog.table.ruleName')" align="center" />
        <el-table-column prop="communityName" :label="$t('marketLog.table.communityName')" align="center" />
        <el-table-column prop="personName" :label="$t('marketLog.table.personName')" align="center" />
        <el-table-column prop="personTel" :label="$t('marketLog.table.personTel')" align="center" />
        <el-table-column prop="openId" :label="$t('marketLog.table.openId')" align="center" />
        <el-table-column prop="sendWayName" :label="$t('marketLog.table.sendWayName')" align="center" />
        <el-table-column prop="businessTypeName" :label="$t('marketLog.table.businessTypeName')" align="center" />
        <el-table-column prop="createTime" :label="$t('marketLog.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('marketLog.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleViewContent(scope.row)">
              {{ $t('marketLog.table.viewContent') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <ViewMarketSendContent ref="viewContentDialog" />
  </div>
</template>

<script>
import { listMarketLog } from '@/api/market/marketLogApi'
import ViewMarketSendContent from '@/components/market/ViewMarketSendContent'

export default {
  name: 'MarketLogList',
  components: {
    ViewMarketSendContent
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityName: '',
        sendWay: '',
        businessType: '',
        personNameLike: '',
        startTime: '',
        endTime: ''
      },
      sendWayOptions: [
        { value: '1001', label: this.$t('marketLog.sendWay.sms') },
        { value: '2002', label: this.$t('marketLog.sendWay.wechat') },
        { value: '3003', label: this.$t('marketLog.sendWay.other') }
      ],
      businessTypeOptions: [
        { value: '1001', label: this.$t('marketLog.businessType.accessControl') },
        { value: '2002', label: this.$t('marketLog.businessType.vehicleGate') },
        { value: '3003', label: this.$t('marketLog.businessType.mobilePayment') },
        { value: '4004', label: this.$t('marketLog.businessType.repairOrder') }
      ],
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
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listMarketLog(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('marketLog.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
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
    handleViewContent(row) {
      this.$refs.viewContentDialog.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.market-log-container {
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