<template>
  <div class="mapping-container ">
    <div class="menu-group-wrapper">
      <div class="group-content">
        <el-menu :default-active="searchForm.domain" class="group-menu" @select="handleDomain">
          <el-menu-item v-for="(item,index) in domains" :key="index" :index="item.domain">
            <span>{{ item.domainName }}({{ item.domain }})</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>

    <!-- 右侧菜单列表 -->
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('mapping.search.title') }}</div>
        <div class="search-content">

          <el-input v-model="searchForm.name" :placeholder="$t('mapping.search.name')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.key" :placeholder="$t('mapping.search.key')" class="search-item"
            clearable @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.value" :placeholder="$t('mapping.search.value')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('mapping.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="searchForm.domain">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="id" :label="$t('mapping.table.id')" />
          <el-table-column prop="domain" :label="$t('mapping.table.domain')" />
          <el-table-column prop="name" :label="$t('mapping.table.name')" />
          <el-table-column prop="key" :label="$t('mapping.table.key')" />
          <el-table-column prop="value" :label="$t('mapping.table.value')" />
          <el-table-column prop="remark" :label="$t('mapping.table.remark')" />
          <!-- <el-table-column
          prop="createTime"
          :label="$t('mapping.table.createTime')"
        /> -->
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <AddMapping :visible.sync="addVisible" :domain="searchForm.domain" @success="handleSuccess" />

    <EditMapping :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelMapping :visible.sync="delVisible" :mapping-id="currentRow.id" @success="handleSuccess" />
  </div>
</template>

<script>
import { getMappingList } from '@/api/dev/mappingApi'
import { getMappingDomainList } from '@/api/dev/mappingDomainApi'

import AddMapping from '@/components/dev/AddMapping'
import EditMapping from '@/components/dev/EditMapping'
import DelMapping from '@/components/dev/DelMapping'

export default {
  name: 'mappingList',
  components: {
    AddMapping,
    EditMapping,
    DelMapping
  },
  data() {
    return {
      loading: false,
      domains:[],
      searchForm: {
        domain: '',
        name: '',
        key: '',
        value: '',
        remark: '',
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
    this.getDomainList()
    this.getList()
  },
  methods: {
    async getDomainList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 300,
        }
        const { data } = await getMappingDomainList(params)
        this.domains = data
      } catch (error) {
        this.$message.error(this.$t('mappingDomain.fetchError'))
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
          domain: this.searchForm.domain,
          name: this.searchForm.name,
          key: this.searchForm.key,
          value: this.searchForm.value,
        }
        const { mappings, total } = await getMappingList(params)
        this.tableData = mappings
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('mapping.fetchError'))
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
    handleDomain(domain) {
      this.searchForm.domain = domain
      this.getList()
    },
  }
}
</script>

<style lang="scss" scoped>
.mapping-container {
  padding: 15px;
  margin: 0;
  display: flex;
  background-color: #f0f2f5;

  .menu-group-wrapper {
    width: 250px;
    height: calc(100vh - 70px);
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