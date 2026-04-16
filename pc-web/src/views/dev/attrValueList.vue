<template>
  <div class="attrValue-container">
   

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('attrValue.list.title') }}</div>
        <div>
          <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
          <el-button type="warn" @click="handleBack">{{ $t('common.back') }}</el-button>

        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="valueId" :label="$t('attrValue.table.valueId')" />
        <!-- <el-table-column prop="specId" :label="$t('attrValue.table.specId')" /> -->
        <el-table-column prop="value" :label="$t('attrValue.table.value')" />
        <el-table-column prop="valueName" :label="$t('attrValue.table.valueName')" />
        <el-table-column :label="$t('attrValue.table.valueShow')" >
            <template slot-scope="scope">
              <span v-if="scope.row.valueShow == 'Y'">YES</span>
              <span v-else>NO</span>
            </template>
          </el-table-column>
        <!-- <el-table-column prop="createTime" :label="$t('attrValue.table.createTime')" /> -->
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
    <AddAttrValue :visible.sync="addVisible" :spec-id="searchForm.specId" @success="handleSuccess" />

    <EditAttrValue :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelAttrValue :visible.sync="delVisible" :spec-id="searchForm.specId" :value-id="currentRow.valueId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getAttrValueList } from '@/api/dev/attrValueApi'
import AddAttrValue from '@/components/dev/AddAttrValue'
import EditAttrValue from '@/components/dev/EditAttrValue'
import DelAttrValue from '@/components/dev/DelAttrValue'

export default {
  name: 'attrValueList',
  components: {
    AddAttrValue,
    EditAttrValue,
    DelAttrValue
  },
  data() {
    return {
      loading: false,
      searchForm: {
        specId: '',
        specName: '',
        domain: '',
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
    const {specId,specName,domain} = this.$route.query
    this.searchForm.specId = specId
    this.searchForm.specName = specName
    this.searchForm.domain = domain
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          valueId: this.searchForm.valueId,
          specId: this.searchForm.specId,
          value: this.searchForm.value,
          valueName: this.searchForm.valueName,
          valueShow: this.searchForm.valueShow,
          createTime: this.searchForm.createTime,
        }
        const { data, total } = await getAttrValueList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('attrValue.fetchError'))
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
    handleBack(){
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.attrValue-container {
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