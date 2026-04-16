<template>
  <div class="item-release-type-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('itemReleaseTypeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.typeName" :placeholder="$t('itemReleaseTypeManage.search.typeNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 放行类型列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('itemReleaseTypeManage.list.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="typeId" :label="$t('itemReleaseTypeManage.table.typeId')" align="center" />
        <el-table-column prop="typeName" :label="$t('itemReleaseTypeManage.table.typeName')" align="center" />
        <el-table-column :label="$t('itemReleaseTypeManage.table.flowName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.flowName }}
            <el-link type="primary" @click="handleSettingFlow(scope.row)">
              ({{ $t('itemReleaseTypeManage.table.setFlow') }})
            </el-link>
          </template>
        </el-table-column>
        <el-table-column :label="$t('itemReleaseTypeManage.table.state')" align="center">
          <template slot-scope="scope">
            {{ scope.row.state === 'C' ? $t('itemReleaseTypeManage.table.deployed') :
              $t('itemReleaseTypeManage.table.pendingDeploy') }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('itemReleaseTypeManage.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="250">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 'W'" size="mini" type="primary" @click="handleDeploy(scope.row)">
              {{ $t('itemReleaseTypeManage.table.deploy') }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 添加弹窗 -->
    <add-item-release-type ref="addDialog" @success="handleSuccess" />

    <!-- 编辑弹窗 -->
    <edit-item-release-type ref="editDialog" @success="handleSuccess" />

    <!-- 删除弹窗 -->
    <delete-item-release-type ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listItemReleaseType, deployModel } from '@/api/work/itemReleaseTypeManageApi'
import AddItemReleaseType from '@/components/work/addItemReleaseType'
import EditItemReleaseType from '@/components/work/editItemReleaseType'
import DeleteItemReleaseType from '@/components/work/deleteItemReleaseType'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ItemReleaseTypeManageList',
  components: {
    AddItemReleaseType,
    EditItemReleaseType,
    DeleteItemReleaseType
  },
  data() {
    return {
      loading: false,
      searchForm: {
        typeName: '',
        communityId: ''
      },
      tableData: [],
      pagination: {
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
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await listItemReleaseType(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('itemReleaseTypeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.typeName = ''
      this.handleSearch()
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
    async handleDeploy(row) {
      try {
        await deployModel({ modelId: row.modelId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.getList()
      } catch (error) {
        this.$message.error(this.$t('itemReleaseTypeManage.deployError'))
      }
    },
    handleSettingFlow(row) {
      window.open(`/bpmnjs/index.html?flowId=${row.flowId}&modelId=${row.modelId}`)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.item-release-type-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>