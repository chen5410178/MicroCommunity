<template>
  <div class="businessDatabus-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('businessDatabus.search.title') }}</div>
      <div class="search-content">
        <el-input v-model="searchForm.databusName" :placeholder="$t('businessDatabus.search.databusName')"
          class="search-item" clearable @keyup.enter.native="handleSearch" />
        <el-input v-model="searchForm.businessTypeCd" :placeholder="$t('businessDatabus.search.businessTypeCd')"
          class="search-item" clearable @keyup.enter.native="handleSearch" />
        <el-select v-model="searchForm.state" :placeholder="$t('businessDatabus.search.state')" class="search-item">
          <el-option label="在用" value="1001" />
          <el-option label="停用" value="2002" />
        </el-select>

        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('businessDatabus.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="databusId" :label="$t('businessDatabus.table.databusId')" />
        <el-table-column prop="databusName" :label="$t('businessDatabus.table.databusName')" />
        <el-table-column prop="businessTypeCd" :label="$t('businessDatabus.table.businessTypeCd')" />
        <el-table-column prop="beanName" :label="$t('businessDatabus.table.beanName')" />
        <el-table-column prop="seq" :label="$t('businessDatabus.table.seq')" />
        <el-table-column prop="state" :label="$t('businessDatabus.table.state')" />
        <!-- <el-table-column prop="createTime" :label="$t('businessDatabus.table.createTime')" /> -->
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
    <AddBusinessDatabus :visible.sync="addVisible" @success="handleSuccess" />

    <EditBusinessDatabus :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelBusinessDatabus :visible.sync="delVisible" :databus-id="currentRow.databusId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getBusinessDatabusList } from '@/api/dev/businessDatabusApi'
import AddBusinessDatabus from '@/components/dev/AddBusinessDatabus'
import EditBusinessDatabus from '@/components/dev/EditBusinessDatabus'
import DelBusinessDatabus from '@/components/dev/DelBusinessDatabus'

export default {
  name: 'businessDatabusList',
  components: {
    AddBusinessDatabus,
    EditBusinessDatabus,
    DelBusinessDatabus
  },
  data() {
    return {
      loading: false,
      searchForm: {
        databusName: '',
        businessTypeCd: '',
        state: '',
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
          databusName: this.searchForm.databusName,
          businessTypeCd: this.searchForm.businessTypeCd,
          beanName: this.searchForm.beanName,
          seq: this.searchForm.seq,
          state: this.searchForm.state,
          createTime: this.searchForm.createTime,
        }
        const { data, total } = await getBusinessDatabusList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('businessDatabus.fetchError'))
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
.businessDatabus-container {
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