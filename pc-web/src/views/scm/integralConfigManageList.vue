<template>
  <div class="integral-config-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('integralConfigManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.configName"
            :placeholder="$t('integralConfigManage.search.configNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 积分标准列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('integralConfigManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="configName" :label="$t('integralConfigManage.table.configName')" align="center" />
        <el-table-column prop="computingFormulaName" :label="$t('integralConfigManage.table.computingFormula')"
          align="center" />
        <el-table-column prop="squarePrice" :label="$t('integralConfigManage.table.squarePrice')" align="center" />
        <el-table-column prop="additionalAmount" :label="$t('integralConfigManage.table.additionalAmount')"
          align="center" />
        <el-table-column prop="scaleName" :label="$t('integralConfigManage.table.scale')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 添加/编辑/删除组件 -->
    <add-integral-config ref="addDialog" @success="handleSuccess" />
    <edit-integral-config ref="editDialog" @success="handleSuccess" />
    <delete-integral-config ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listIntegralConfig } from '@/api/scm/integralConfigManageApi'
import AddIntegralConfig from '@/components/scm/addIntegralConfig'
import EditIntegralConfig from '@/components/scm/editIntegralConfig'
import DeleteIntegralConfig from '@/components/scm/deleteIntegralConfig'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'IntegralConfigManageList',
  components: {
    AddIntegralConfig,
    EditIntegralConfig,
    DeleteIntegralConfig
  },
  data() {
    return {
      loading: false,
      searchForm: {
        configName: '',
        communityId: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listIntegralConfig(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('integralConfigManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
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
.integral-config-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>