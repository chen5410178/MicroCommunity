<template>
  <div class="padding">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex juistify-between">
        <span>{{ $t('inspectionItemTitleManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.titleId" :placeholder="$t('inspectionItemTitleManage.placeholderTitleId')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.itemTitle" :placeholder="$t('inspectionItemTitleManage.placeholderItemTitle')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.titleType" :placeholder="$t('inspectionItemTitleManage.placeholderTitleType')"
            style="width:100%">
            <el-option :label="$t('inspectionItemTitleManage.selectTitleType')" value="" />
            <el-option :label="$t('inspectionItemTitleManage.singleChoice')" value="1001" />
            <el-option :label="$t('inspectionItemTitleManage.multipleChoice')" value="2002" />
            <el-option :label="$t('inspectionItemTitleManage.shortAnswer')" value="3003" />
          </el-select>
        </el-col>
        <el-col :span="6">
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

    <!-- 列表 -->
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('inspectionItemTitleManage.listTitle') }}</span>
        <div style="float:right">
          <el-button size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="openAddModal">
            <i class="el-icon-plus"></i>
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table :data="tableData" border v-loading="loading" style="width:100%">
        <el-table-column prop="titleId" :label="$t('inspectionItemTitleManage.titleId')" align="center" />
        <el-table-column prop="itemTitle" :label="$t('inspectionItemTitleManage.title')" align="center" />
        <el-table-column prop="titleType" :label="$t('inspectionItemTitleManage.titleType')" align="center">
          <template slot-scope="scope">
            {{ getTitleTypeName(scope.row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="seq" :label="$t('inspectionItemTitleManage.seq')" align="center" />
        <el-table-column prop="createTime" :label="$t('inspectionItemTitleManage.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditModal(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handlePageChange" />
    </el-card>

    <!-- 组件 -->
    <add-inspection-item-title ref="addModal" @success="fetchList" />
    <edit-inspection-item-title ref="editModal" @success="fetchList" />
    <delete-inspection-item-title ref="deleteModal" @success="fetchList" />
  </div>
</template>

<script>
import { listInspectionItemTitle } from '@/api/inspection/inspectionItemTitleManageApi'
import AddInspectionItemTitle from '@/components/inspection/addInspectionItemTitle'
import EditInspectionItemTitle from '@/components/inspection/editInspectionItemTitle'
import DeleteInspectionItemTitle from '@/components/inspection/deleteInspectionItemTitle'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InspectionItemTitleManageList',
  components: {
    AddInspectionItemTitle,
    EditInspectionItemTitle,
    DeleteInspectionItemTitle
  },
  data() {
    return {
      searchForm: {
        titleId: '',
        itemTitle: '',
        titleType: '',
        itemId: this.$route.query.itemId || ''
      },
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchList()
  },
  methods: {
    async fetchList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId()
        }

        const { data, total } = await listInspectionItemTitle(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchList()
    },
    handleReset() {
      this.searchForm = {
        titleId: '',
        itemTitle: '',
        titleType: '',
        itemId: this.searchForm.itemId
      }
      this.handleSearch()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchList()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.fetchList()
    },
    getTitleTypeName(type) {
      const types = {
        '1001': this.$t('inspectionItemTitleManage.singleChoice'),
        '2002': this.$t('inspectionItemTitleManage.multipleChoice'),
        '3003': this.$t('inspectionItemTitleManage.shortAnswer')
      }
      return types[type] || type
    },
    goBack() {
      this.$router.go(-1)
    },
    openAddModal() {
      this.$refs.addModal.open({
        itemId: this.searchForm.itemId
      })
    },
    openEditModal(row) {
      this.$refs.editModal.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deleteModal.open(row)
    }
  }
}
</script>

<style scoped>
.search-wrapper {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>