<template>
  <div class="route-container padding">
    <!-- 左侧菜单组列表 -->
    <div class="menu-group-wrapper">
      <div class="group-content">
        <el-menu :default-active="searchForm.appId" class="group-menu" @select="handleApp">
          <el-menu-item v-for="item in apps" :key="item.appId" :index="item.appId">
            <span>{{ item.name }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>

    <!-- 右侧菜单列表 -->
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('route.search.title') }}</div>
        <div class="search-content">
          <el-input v-model="searchForm.serviceCode" :placeholder="$t('route.search.serviceCode')" class="search-item"
            clearable @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.serviceName" :placeholder="$t('route.search.serviceName')" class="search-item"
            clearable @keyup.enter.native="handleSearch" />
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('route.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="searchForm.appId">{{ $t('route.register') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="appId" :label="$t('route.table.appId')" />
          <el-table-column prop="appName" :label="$t('route.table.appName')" />
          <el-table-column prop="serviceId" :label="$t('route.table.serviceId')" />
          <el-table-column prop="serviceName" :label="$t('route.table.serviceName')" />
          <el-table-column prop="serviceCode" :label="$t('route.table.serviceCode')" />
          <el-table-column prop="orderTypeCd" :label="$t('route.table.orderTypeCd')" />
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete')
                }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>

    <DelRoute :visible.sync="delVisible" :route-id="currentRow.id" @success="handleSuccess" />
  </div>
</template>

<script>
import { getRouteList } from '@/api/dev/routeApi'
import { getAppList } from '@/api/dev/appApi'

import DelRoute from '@/components/dev/DelRoute'

export default {
  name: 'routeList',
  components: {
    DelRoute
  },
  data() {
    return {
      loading: false,
      apps: [],
      searchForm: {
        appId: '',
        serviceName: '',
        serviceCode: '',
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
    this.getApps()

  },
  methods: {
    async getApps() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
        }
        const { apps } = await getAppList(params)
        this.apps = apps
        if (apps && apps.length > 0) {
          this.searchForm.appId = apps[0].appId
          this.getList()
        }
      } catch (error) {
        this.$message.error(this.$t('app.fetchError'))
      } finally {
        this.loading = false
      }
    },

    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          appId: this.searchForm.appId,
          serviceId: this.searchForm.serviceId,
          orderTypeCd: this.searchForm.orderTypeCd,
        }
        const { serviceRegisters, total } = await getRouteList(params)
        this.tableData = serviceRegisters
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('route.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$router.push('/views/dev/addRoute?appId=' + this.searchForm.appId)
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.delVisible = true
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
    },
    handleApp(appId) {
      this.searchForm.appId = appId
      this.getList()
    },
  }
}
</script>

<style lang="scss" scoped>
.route-container {

  display: flex;
  background-color: #f0f2f5;

  .menu-group-wrapper {
    width: 200px;
    background-color: #fff;
    border-right: 1px solid #e6e6e6;
    display: flex;
    flex-direction: column;

    .group-header {
      padding: 20px;
      border-bottom: 1px solid #e6e6e6;

      .header-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .group-content {
      flex: 1;
      overflow-y: auto;

      .group-menu {
        border-right: none;
      }
    }
  }

  .menu-list-wrapper {
    flex: 1;
    padding-left: 10px;
    overflow-y: auto;

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
}
</style>