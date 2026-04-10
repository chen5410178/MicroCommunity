<template>
    <div class="appApi-container">
  
      <!-- 左侧菜单组列表 -->
      <div class="menu-group-wrapper">
        <div class="group-content">
          <el-menu :default-active="appId" class="group-menu" @select="handleApp">
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
          <div class="search-title">{{ $t('appApi.search.title') }}</div>
          <div class="search-content">
  
            <el-input v-model="searchForm.apiCode" :placeholder="$t('appApi.search.apiCode')" class="search-item" clearable
              @keyup.enter.native="handleSearch" />
            <el-input v-model="searchForm.apiName" :placeholder="$t('appApi.search.apiName')" class="search-item" clearable
              @keyup.enter.native="handleSearch" />
          
            <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          </div>
        </div>
  
        <div class="list-wrapper">
          <div class="list-header">
            <div class="list-title">{{ $t('appApi.list.title') }}</div>
            <el-button type="primary" @click="handleAdd" v-if="appId">{{ $t('common.add') }}</el-button>
          </div>
  
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="appName" :label="$t('appApi.table.appName')" />
            <el-table-column prop="apiId" :label="$t('appApi.table.apiId')" />
            <el-table-column prop="apiCode" :label="$t('appApi.table.apiCode')" />
            <el-table-column prop="apiName" :label="$t('appApi.table.apiName')" />
            <!-- <el-table-column prop="typeName" :label="$t('appApi.table.typeId')" /> -->
            <el-table-column prop="seq" :label="$t('appApi.table.seq')" />
            <el-table-column prop="docUrl" :label="$t('appApi.table.docUrl')" />
            <el-table-column prop="remark" :label="$t('appApi.table.remark')" />
            <el-table-column prop="createTime" :label="$t('appApi.table.createTime')" />
            <el-table-column :label="$t('common.operation')" width="200" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete')}}</el-button>
              </template>
            </el-table-column>
          </el-table>
  
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </div>
      </div>
  
      <DelAppApi :visible.sync="delVisible" :aa-id="currentRow.aaId" @success="handleSuccess" />
    </div>
  </template>
  
  <script>
  import { getAppApiList } from '@/api/tenant/appApiApi'
  import { getHcAppList } from '@/api/tenant/hcAppApi'
  

  import DelAppApi from '@/components/tenant/DelAppApi'
  
  export default {
    name: 'appApiList',
    components: {
      DelAppApi
    },
    data() {
      return {
        loading: false,
        searchForm: {
          apiId: '',
          apiCode: '',
          apiName: '',
          typeId: '',
          seq: '',
          docUrl: '',
          remark: '',
          createTime: '',
        },
        tableData: [],
        apps: [],
        appId:'',
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
      this.getAppList();
      this.getList()
    },
    methods: {
      async getAppList() {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
        }
        const { data } = await getHcAppList(params)
        this.apps = data.data
      },
      async getList() {
        try {
          this.loading = true
          const params = {
            page: this.page.current,
            row: this.page.size,
            apiId: this.searchForm.apiId,
            apiCode: this.searchForm.apiCode,
            apiName: this.searchForm.apiName,
            typeId: this.searchForm.typeId,
            seq: this.searchForm.seq,
            docUrl: this.searchForm.docUrl,
            remark: this.searchForm.remark,
            createTime: this.searchForm.createTime,
          }
          const { data } = await getAppApiList(params)
          this.tableData = data.data
          this.page.total = data.total
        } catch (error) {
          this.$message.error(this.$t('appApi.fetchError'))
        } finally {
          this.loading = false
        }
      },
      handleSearch() {
        this.page.current = 1
        this.getList()
      },
      handleAdd() {
        this.$router.push('/views/tenant/addAppApi?appId='+this.appId)
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
      handleApp(appId){
        this.appId = appId
        this.getList()
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .appApi-container {
    padding: 0;
    margin: 0;
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