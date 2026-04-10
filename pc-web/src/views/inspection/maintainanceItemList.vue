<template>
  <div class="maintainance-item-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainanceItem.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.itemTitle" :placeholder="$t('maintainanceItem.search.itemTitle')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.titleType" :placeholder="$t('maintainanceItem.search.titleType')"
            style="width:100%" clearable>
            <el-option v-for="item in titleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表区域 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainanceItem.list.title') }}</span>
        <div style="float: right;">
          <!-- <el-button type="primary" size="small" @click="handleGoBack">
            {{ $t('common.back') }}
          </el-button> -->
          <!-- <el-button type="primary" size="small" @click="handleShowDocument">
            {{ $t('common.document') }}
          </el-button> -->
          <el-button type="primary" size="small" @click="handleAdd">
            <i class="el-icon-plus" /> {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="itemTitle" :label="$t('maintainanceItem.table.itemTitle')" align="center" />
        <el-table-column prop="titleType" :label="$t('maintainanceItem.table.titleType')" align="center">
          <template slot-scope="scope">
            {{ getTitleTypeName(scope.row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="seq" :label="$t('maintainanceItem.table.seq')" align="center" />
        <el-table-column prop="createTime" :label="$t('maintainanceItem.table.createTime')" align="center" />
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

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-maintainance-item ref="addDialog" @success="handleSuccess" />
    <edit-maintainance-item ref="editDialog" @success="handleSuccess" />
    <delete-maintainance-item ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMaintainanceItem } from '@/api/inspection/maintainanceItemApi'
import AddMaintainanceItem from '@/components/inspection/addMaintainanceItem'
import EditMaintainanceItem from '@/components/inspection/editMaintainanceItem'
import DeleteMaintainanceItem from '@/components/inspection/deleteMaintainanceItem'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainanceItemList',
  components: {
    AddMaintainanceItem,
    EditMaintainanceItem,
    DeleteMaintainanceItem
  },
  data() {
    return {
      loading: false,
      searchForm: {
        itemTitle: '',
        titleType: '',
        communityId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      titleTypeOptions: [
        { value: '1001', label: this.$t('maintainanceItem.titleType.single') },
        { value: '2002', label: this.$t('maintainanceItem.titleType.multiple') },
        { value: '3003', label: this.$t('maintainanceItem.titleType.shortAnswer') }
      ]
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
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listMaintainanceItem(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('maintainanceItem.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        itemTitle: '',
        titleType: '',
        communityId: getCommunityId()
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addDialog.open({
        communityId: this.searchForm.communityId
      })
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
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleGoBack() {
      this.$router.go(-1)
    },
    handleShowDocument() {
      // 显示文档逻辑
    },
    getTitleTypeName(type) {
      const option = this.titleTypeOptions.find(item => item.value === type)
      return option ? option.label : ''
    }
  }
}
</script>

<style lang="scss" scoped>
.maintainance-item-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;

      &:last-child {
        margin-bottom: 0;
      }
    }
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