<template>
  <div class="hcApi-container">

    <!-- 左侧菜单组列表 -->
    <div class="menu-group-wrapper">
      <div class="group-content">
        <el-menu :default-active="typeId" class="group-menu" @select="handleApiType">
          <el-menu-item v-for="item in apiTypes" :key="item.typeId" :index="item.typeId">
            <span>{{ item.typeName }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>

    <!-- 右侧菜单列表 -->
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('hcApi.search.title') }}</div>
        <div class="search-content">

          <el-input v-model="searchForm.apiCode" :placeholder="$t('hcApi.search.apiCode')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.apiName" :placeholder="$t('hcApi.search.apiName')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
        
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('hcApi.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="typeId">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="apiId" :label="$t('hcApi.table.apiId')" />
          <el-table-column prop="apiCode" :label="$t('hcApi.table.apiCode')" />
          <el-table-column prop="apiName" :label="$t('hcApi.table.apiName')" />
          <el-table-column prop="typeName" :label="$t('hcApi.table.typeId')" />
          <el-table-column prop="seq" :label="$t('hcApi.table.seq')" />
          <el-table-column prop="docUrl" :label="$t('hcApi.table.docUrl')" />
          <el-table-column prop="remark" :label="$t('hcApi.table.remark')" />
          <el-table-column prop="createTime" :label="$t('hcApi.table.createTime')" />
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
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
    <AddHcApi :visible.sync="addVisible" @success="handleSuccess" :type-id="typeId"/>

    <EditHcApi :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelHcApi :visible.sync="delVisible" :api-id="currentRow.apiId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getHcApiList } from '@/api/tenant/hcApiApi'
import { getApiTypeList } from '@/api/tenant/apiTypeApi'

import AddHcApi from '@/components/tenant/AddHcApi'
import EditHcApi from '@/components/tenant/EditHcApi'
import DelHcApi from '@/components/tenant/DelHcApi'

export default {
  name: 'hcApiList',
  components: {
    AddHcApi,
    EditHcApi,
    DelHcApi
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
      apiTypes: [],
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
    this.getApiTypeList();
    this.getList()
  },
  methods: {
    async getApiTypeList() {
      this.loading = true
      const params = {
        page: 1,
        row: 100,
      }
      const { data } = await getApiTypeList(params)
      this.apiTypes = data.data
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
        const { data } = await getHcApiList(params)
        this.tableData = data.data
        this.page.total = data.total
      } catch (error) {
        this.$message.error(this.$t('hcApi.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.addVisible = true
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
    handleApiType(typeId){
      this.typeId = typeId
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.hcApi-container {
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