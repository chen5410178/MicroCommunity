<template>
  <div class="initialize-community-manage-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('initializeCommunityManage.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.communityId"
          :placeholder="$t('initializeCommunityManage.search.communityId')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.name"
          :placeholder="$t('initializeCommunityManage.search.name')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('initializeCommunityManage.list.title') }}</div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="communityId"
          :label="$t('initializeCommunityManage.table.communityId')"
          align="center"
        />
        <el-table-column
          prop="name"
          :label="$t('initializeCommunityManage.table.name')"
          align="center"
        />
        <el-table-column
          prop="nearbyLandmarks"
          :label="$t('initializeCommunityManage.table.nearbyLandmarks')"
          align="center"
        />
        <el-table-column
          prop="cityName"
          :label="$t('initializeCommunityManage.table.cityCode')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('initializeCommunityManage.table.state')"
          align="center"
        />
        <el-table-column
          :label="$t('common.operation')"
          width="150"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleInitialize(scope.row)"
            >{{ $t('initializeCommunityManage.button.initialize') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <div class="form-group">
      <li style="color: red;">{{ msgData }}</li>
    </div>

    <ChooseInitializeCommunity
      :visible.sync="dialogVisible"
      :community-info="currentCommunity"
      @confirm="handleConfirmInitialize"
    />
  </div>
</template>

<script>
import { listCommunitys, initializeCommunity } from '@/api/community/initializeCommunityManageApi'
import ChooseInitializeCommunity from '@/components/community/ChooseInitializeCommunity'

export default {
  name: 'InitializeCommunityManageList',
  components: {
    ChooseInitializeCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        name: '',
        cityCode: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      msgData: '',
      dialogVisible: false,
      currentCommunity: {}
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
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { communitys, total } = await listCommunitys(params)
        this.tableData = communitys
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('initializeCommunityManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        communityId: '',
        name: '',
        cityCode: ''
      }
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
    handleInitialize(row) {
      this.currentCommunity = { ...row }
      this.dialogVisible = true
    },
    async handleConfirmInitialize(data) {
      try {
        const res = await initializeCommunity(data)
        this.msgData = res.data
        this.$message.success(this.$t('common.operationSuccess'))
        this.getList()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.initialize-community-manage-container {
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

  .form-group {
    margin-top: 10px;
  }
}
</style>