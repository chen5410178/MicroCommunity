<template>
  <div class="inspection-item-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('inspectionItemManage.searchCondition') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.itemId" :placeholder="$t('inspectionItemManage.itemIdPlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.itemName" :placeholder="$t('inspectionItemManage.itemNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('inspectionItemManage.searchBtn') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('inspectionItemManage.resetBtn') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 巡检项目列表 -->
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('inspectionItemManage.inspectionItems') }}</span>
        <div style="float: right;">
          <!-- <el-button type="primary" size="small" @click="handleShowDoc">
            <i class="el-icon-document"></i>
            {{ $t('inspectionItemManage.doc') }}
          </el-button> -->
          <el-button type="primary" size="small" @click="openAddModal">
            <i class="el-icon-plus"></i>
            {{ $t('inspectionItemManage.addBtn') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="itemId" :label="$t('inspectionItemManage.id')" align="center" />
        <el-table-column prop="itemName" :label="$t('inspectionItemManage.itemName')" align="center" />
        <el-table-column prop="createTime" :label="$t('inspectionItemManage.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('inspectionItemManage.remark')" align="center" />
        <el-table-column :label="$t('inspectionItemManage.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditModal(scope.row)">
              {{ $t('inspectionItemManage.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
              {{ $t('inspectionItemManage.delete') }}
            </el-button>
            <el-button size="mini" type="success" @click="handleToQuestion(scope.row)">
              {{ $t('inspectionItemManage.question') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handlePageChange" />
    </el-card>

    <!-- 子组件 -->
    <add-inspection-item ref="addModal" @success="handleSuccess" />
    <edit-inspection-item ref="editModal" @success="handleSuccess" />
    <delete-inspection-item ref="deleteModal" @success="handleSuccess" />
  </div>
</template>

<script>
import { listInspectionItem } from '@/api/inspection/inspectionItemManageApi'
import AddInspectionItem from '@/components/inspection/AddInspectionItem'
import EditInspectionItem from '@/components/inspection/EditInspectionItem'
import DeleteInspectionItem from '@/components/inspection/DeleteInspectionItem'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InspectionItemManageList',
  components: {
    AddInspectionItem,
    EditInspectionItem,
    DeleteInspectionItem
  },
  data() {
    return {
      loading: false,
      searchForm: {
        itemId: '',
        itemName: '',
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
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const res = await listInspectionItem(params)
        this.tableData = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('获取巡检项目列表失败:', error)
        this.$message.error(this.$t('inspectionItemManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.itemId = ''
      this.searchForm.itemName = ''
      this.handleSearch()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.getList()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.getList()
    },
    handleShowDoc() {
      // 显示文档逻辑
      console.log('显示文档')
    },
    handleToQuestion(item) {
      this.$router.push(`/views/inspection/inspectionItemTitleManage?itemId=${item.itemId}`)
    },
    openAddModal() {
      this.$refs.addModal.open()
    },
    openEditModal(item) {
      this.$refs.editModal.open(item)
    },
    openDeleteModal(item) {
      this.$refs.deleteModal.open(item)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-item-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>