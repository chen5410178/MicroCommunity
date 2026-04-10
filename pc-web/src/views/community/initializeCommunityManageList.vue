<template>
  <div class="initialize-community-manage-container">
    <!-- 查询条件 -->
    <div class="ibox">
      <div class="ibox-title">
        <h5>{{ $t('initializeCommunityManage.search.title') }}</h5>
      </div>
      <div class="ibox-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model="searchForm.communityId" :placeholder="$t('initializeCommunityManage.search.communityId')"
              clearable @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="8">
            <el-input v-model="searchForm.name" :placeholder="$t('initializeCommunityManage.search.name')" clearable
              @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="handleSearch">
              <i class="el-icon-search"></i>
              {{ $t('common.search') }}
            </el-button>
            <el-button @click="handleReset">
              <i class="el-icon-refresh"></i>
              {{ $t('common.reset') }}
            </el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 初始化小区数据 -->
    <div class="ibox">
      <div class="ibox-title">
        <h5>{{ $t('initializeCommunityManage.list.title') }}</h5>
      </div>
      <div class="ibox-content">
        <el-table :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="communityId" :label="$t('initializeCommunityManage.table.communityId')" align="center" />
          <el-table-column prop="name" :label="$t('initializeCommunityManage.table.name')" align="center" />
          <el-table-column prop="nearbyLandmarks" :label="$t('initializeCommunityManage.table.nearbyLandmarks')"
            align="center" />
          <el-table-column prop="cityName" :label="$t('initializeCommunityManage.table.cityCode')" align="center" />
          <el-table-column prop="stateName" :label="$t('initializeCommunityManage.table.state')" align="center" />
          <el-table-column :label="$t('common.operation')" align="center" width="150">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleInitialize(scope.row)">
                {{ $t('initializeCommunityManage.button.initialize') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>

    <div class="form-group">
      <li style="color: red;">{{ msgData }}</li>
    </div>

    <choose-initialize-community :visible.sync="dialogVisible" :community-info="currentCommunity"
      @confirm="handleConfirmInitialize" />
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
  padding: 20px;

  .ibox {
    background: #fff;
    margin-bottom: 20px;
    border-radius: 4px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);

    .ibox-title {
      border-bottom: 1px solid #e7eaec;
      padding: 15px 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      h5 {
        margin: 0;
        font-size: 16px;
      }
    }

    .ibox-content {
      padding: 20px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>