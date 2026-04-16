<template>
  <div class="businessTableHis-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('businessTableHis.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.actionObj"
          :placeholder="$t('businessTableHis.search.actionObj')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.actionObjHis"
          :placeholder="$t('businessTableHis.search.actionObjHis')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.businessTypeCd"
          :placeholder="$t('businessTableHis.search.businessTypeCd')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />

        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('businessTableHis.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="hisId"
          :label="$t('businessTableHis.table.hisId')"
        />
        <el-table-column
          prop="action"
          :label="$t('businessTableHis.table.action')"
        />
        <el-table-column
          prop="actionObj"
          :label="$t('businessTableHis.table.actionObj')"
        />
        <el-table-column
          prop="actionObjHis"
          :label="$t('businessTableHis.table.actionObjHis')"
        />
        <el-table-column
          prop="businessTypeCd"
          :label="$t('businessTableHis.table.businessTypeCd')"
        />
        <el-table-column
          prop="remark"
          :label="$t('businessTableHis.table.remark')"
        />
        <!-- <el-table-column
          prop="createTime"
          :label="$t('businessTableHis.table.createTime')"
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
    <AddBusinessTableHis
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <EditBusinessTableHis
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <DelBusinessTableHis
      :visible.sync="delVisible"
      :his-id="currentRow.hisId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getBusinessTableHisList } from '@/api/dev/businessTableHisApi'
import AddBusinessTableHis from '@/components/dev/AddBusinessTableHis'
import EditBusinessTableHis from '@/components/dev/EditBusinessTableHis'
import DelBusinessTableHis from '@/components/dev/DelBusinessTableHis'

export default {
  name: 'businessTableHisList',
  components: {
    AddBusinessTableHis,
    EditBusinessTableHis,
    DelBusinessTableHis
  },
  data() {
    return {
      loading: false,
      searchForm: {
        action: '',
        actionObj: '',
        actionObjHis: '',
        businessTypeCd: '',
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          action: this.searchForm.action,
          actionObj: this.searchForm.actionObj,
          actionObjHis: this.searchForm.actionObjHis,
          businessTypeCd: this.searchForm.businessTypeCd,
          remark: this.searchForm.remark,
          createTime: this.searchForm.createTime,
        }
        const { data,total } = await getBusinessTableHisList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('businessTableHis.fetchError'))
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
.businessTableHis-container {
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