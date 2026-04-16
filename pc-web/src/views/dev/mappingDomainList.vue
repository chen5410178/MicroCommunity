<template>
  <div class="mappingDomain-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('mappingDomain.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.domain"
          :placeholder="$t('mappingDomain.search.domain')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.domainName"
          :placeholder="$t('mappingDomain.search.domainName')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
       
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('mappingDomain.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="id"
          :label="$t('mappingDomain.table.id')"
        />
        <el-table-column
          prop="domain"
          :label="$t('mappingDomain.table.domain')"
        />
        <el-table-column
          prop="domainName"
          :label="$t('mappingDomain.table.domainName')"
        />
        <el-table-column
          prop="seq"
          :label="$t('mappingDomain.table.seq')"
        />
        <el-table-column
          prop="remark"
          :label="$t('mappingDomain.table.remark')"
        />
        <!-- <el-table-column
          prop="createTime"
          :label="$t('mappingDomain.table.createTime')"
        /> -->
        <el-table-column
          :label="$t('common.operation')"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >{{ $t('common.edit') }}</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <AddMappingDomain
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <EditMappingDomain
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <DelMappingDomain
      :visible.sync="delVisible"
      :id="currentRow.id"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getMappingDomainList } from '@/api/dev/mappingDomainApi'
import AddMappingDomain from '@/components/dev/AddMappingDomain'
import EditMappingDomain from '@/components/dev/EditMappingDomain'
import DelMappingDomain from '@/components/dev/DelMappingDomain'

export default {
  name: 'mappingDomainList',
  components: {
    AddMappingDomain,
    EditMappingDomain,
    DelMappingDomain
  },
  data() {
    return {
      loading: false,
      searchForm: {
        domain: '',
        domainName: '',
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          domain: this.searchForm.domain,
          domainName: this.searchForm.domainName,
          seq: this.searchForm.seq,
          remark: this.searchForm.remark,
          createTime: this.searchForm.createTime,
        }
        const { data,total } = await getMappingDomainList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('mappingDomain.fetchError'))
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
    }
  }
}
</script>

<style lang="scss" scoped>
.mappingDomain-container {
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