<template>
  <div class="share-reading-fee-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('shareReadingFee.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.roomName" :placeholder="$t('shareReadingFee.search.roomName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.ownerNameLike" :placeholder="$t('shareReadingFee.search.ownerName')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('shareReadingFee.list.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
          {{ $t('common.back') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="roomName" :label="$t('shareReadingFee.table.roomName')" align="center" />
        <el-table-column prop="ownerName" :label="$t('shareReadingFee.table.ownerName')" align="center" />
        <el-table-column prop="feeName" :label="$t('shareReadingFee.table.feeName')" align="center" />
        <el-table-column prop="degrees" :label="$t('shareReadingFee.table.degrees')" align="center" />
        <el-table-column prop="amount" :label="$t('shareReadingFee.table.amount')" align="center" />
        <el-table-column prop="remark" :label="$t('shareReadingFee.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
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
import { listFloorShareFee } from '@/api/fee/shareReadingFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ShareReadingFeeList',
  data() {
    return {
      loading: false,
      searchForm: {
        readingId: this.$route.query.readingId || '',
        roomName: '',
        ownerNameLike: '',
        communityId: ''
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
    this.searchForm.communityId = getCommunityId()
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
        const { data, total } = await listFloorShareFee(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('shareReadingFee.fetchError'))
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
    handleDetail(row) {
      this.$router.push({
        path: '/views/fee/feeDetail',
        query: { feeId: row.feeId }
      })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.share-reading-fee-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>