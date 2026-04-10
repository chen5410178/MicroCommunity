<template>
  <div class="attrSpec-container">
    <div class="menu-group-wrapper" >
      <div class="group-header">
        <div class="header-title">选择属性表</div>
      </div>
      <div class="group-content">
        <el-menu :default-active="searchForm.tableName" class="group-menu" @select="handleTableName">
          <el-menu-item v-for="(item, index) in tableNames" :key="index" :index="item.value">
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('attrSpec.search.title') }}</div>
        <div class="search-content">
          <el-input v-model="searchForm.domain" :placeholder="$t('attrSpec.search.domain')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.specCd" :placeholder="$t('attrSpec.search.specCd')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-input v-model="searchForm.specName" :placeholder="$t('attrSpec.search.specName')" class="search-item"
            clearable @keyup.enter.native="handleSearch" />
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('attrSpec.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="searchForm.tableName">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">

          <el-table-column prop="domain" :label="$t('attrSpec.table.domain')" />
          <el-table-column prop="specCd" :label="$t('attrSpec.table.specCd')" />
          <el-table-column prop="tableName" :label="$t('attrSpec.table.tableName')" />
          <el-table-column prop="specName" :label="$t('attrSpec.table.specName')" />
          <el-table-column prop="specHoldplace" :label="$t('attrSpec.table.specHoldplace')" />
          <el-table-column  :label="$t('attrSpec.table.required')" >
            <template slot-scope="scope">
              <span v-if="scope.row.required == 'Y'">YES</span>
              <span v-else>NO</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('attrSpec.table.specShow')"  >
            <template slot-scope="scope">
              <span v-if="scope.row.specShow == 'Y'">YES</span>
              <span v-else>NO</span>
            </template>
          </el-table-column>
          <el-table-column  :label="$t('attrSpec.table.specValueType')">
            <template slot-scope="scope">
              <span v-if="scope.row.specValueType == '1001'">string</span>
              <span v-else-if="scope.row.specValueType == '2002'">int</span>
              <span v-else>money</span>
            </template>
          </el-table-column>
          <el-table-column  :label="$t('attrSpec.table.specType')" >
            <template slot-scope="scope">
              <span v-if="scope.row.specType == '2233'">input</span>
              <span v-else>select</span>
            </template>
          </el-table-column>
          <el-table-column  :label="$t('attrSpec.table.listShow')"  >
            <template slot-scope="scope">
              <span v-if="scope.row.listShow == 'Y'">YES</span>
              <span v-else>NO</span>
            </template>
          </el-table-column>
          <!-- <el-table-column
          prop="createTime"
          :label="$t('attrSpec.table.createTime')"
        /> -->
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
              <el-button size="mini" type="warn" @click="handleValue(scope.row)">{{ $t('attrSpec.value') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <AddAttrSpec :visible.sync="addVisible" :table-name="searchForm.tableName" @success="handleSuccess" />

    <EditAttrSpec :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelAttrSpec :visible.sync="delVisible" :spec-id="currentRow.specId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import AddAttrSpec from '@/components/dev/AddAttrSpec'
import EditAttrSpec from '@/components/dev/EditAttrSpec'
import DelAttrSpec from '@/components/dev/DelAttrSpec'

export default {
  name: 'attrSpecList',
  components: {
    AddAttrSpec,
    EditAttrSpec,
    DelAttrSpec
  },
  data() {
    return {
      loading: false,
      tableNames: [{
        label: '设备属性',
        value: 'machine_attr'
      }],
      searchForm: {
        domain: '',
        specCd: '',
        tableName: '',
        specName: '',
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
          specCd: this.searchForm.specCd,
          tableName: this.searchForm.tableName,
          specName: this.searchForm.specName,
        }
        const { data, total } = await getAttrSpecList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('attrSpec.fetchError'))
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
    handleTableName(_tableName){
      this.searchForm.tableName = _tableName
      this.getList()
    },
    handleValue(row){
      this.$router.push('/views/dev/attrValueList??specId='+row.specId+'&specName='+row.specName+'&domain='+row.domain)
    }
  }
}
</script>

<style lang="scss" scoped>
.attrSpec-container {
  padding: 0;
  margin: 0;
  display: flex;
  background-color: #f0f2f5;

  .menu-group-wrapper {
    width: 200px;
    height: calc(100vh - 300px);
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