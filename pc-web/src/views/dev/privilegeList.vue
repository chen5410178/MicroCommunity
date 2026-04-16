<template>
  <div class="privilege-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('privilege.search.title') }}</div>
      <div class="search-content">
        <el-input v-model="searchForm.name" :placeholder="$t('privilege.search.name')" class="search-item" clearable
          @keyup.enter.native="handleSearch" />

        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('privilege.list.title') }}</div>
        <div>
          <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
          <el-button type="warn" @click="handleBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="pId" :label="$t('privilege.table.pId')" />
        <el-table-column prop="name" :label="$t('privilege.table.name')" />
        <el-table-column prop="resource" :label="$t('privilege.table.resource')" />
        <el-table-column prop="description" :label="$t('privilege.table.description')" />
        <el-table-column prop="createTime" :label="$t('privilege.table.createTime')" />
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
    <AddPrivilege :visible.sync="addVisible" :domain="domain" :m-id="mId" @success="handleSuccess" />

    <EditPrivilege :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelPrivilege :visible.sync="delVisible" :privilege-id="currentRow.pId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getPrivilegeList } from '@/api/dev/privilegeApi'
import AddPrivilege from '@/components/dev/AddPrivilege'
import EditPrivilege from '@/components/dev/EditPrivilege'
import DelPrivilege from '@/components/dev/DelPrivilege'

export default {
  name: 'privilegeList',
  components: {
    AddPrivilege,
    EditPrivilege,
    DelPrivilege
  },
  data() {
    return {
      loading: false,
      mId: '',
      domain: '',
      searchForm: {
        name: '',
        domain: '',
        resource: '',
        mId: '',
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
    const { mId, domain } = this.$route.query
    this.mId = mId;
    this.domain = domain;
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name,
          domain: this.domain,
          mId: this.mId,
        }
        const { basePrivileges, total } = await getPrivilegeList(params)
        this.tableData = basePrivileges
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('privilege.fetchError'))
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
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.privilege-container {
  padding: 0;
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
}
</style>