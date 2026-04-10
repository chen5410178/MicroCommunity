<template>
  <div class="reserve-params-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="text-left">
        <span>{{ $t('reserveParamsManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('reserveParamsManage.search.namePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('reserveParamsManage.list.title') }}</span>
        <el-button type="primary" size="mini" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="name" :label="$t('reserveParamsManage.table.name')" align="center" />
        <el-table-column prop="paramWay" :label="$t('reserveParamsManage.table.paramWay')" align="center">
          <template slot-scope="scope">
            {{ scope.row.paramWay === '1' ? $t('reserveParamsManage.day') : $t('reserveParamsManage.week') }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('reserveParamsManage.table.startTime')" align="center" />
        <el-table-column prop="maxQuantity" :label="$t('reserveParamsManage.table.maxQuantity')" align="center" />
        <el-table-column prop="hoursMaxQuantity" :label="$t('reserveParamsManage.table.hoursMaxQuantity')"
          align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleSetTime(scope.row)">
              {{ $t('reserveParamsManage.setTime') }}
            </el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">
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

    <!-- 子组件 -->
    <add-reserve-params ref="addReserveParams" @success="handleSuccess" />
    <edit-reserve-params ref="editReserveParams" @success="handleSuccess" />
    <delete-reserve-params ref="deleteReserveParams" @success="handleSuccess" />
    <edit-reserve-params-open-time ref="editReserveParamsOpenTime" @success="handleSuccess" />
  </div>
</template>

<script>
import { listReserveParams } from '@/api/scm/reserveParamsManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddReserveParams from '@/components/scm/addReserveParams'
import EditReserveParams from '@/components/scm/editReserveParams'
import DeleteReserveParams from '@/components/scm/deleteReserveParams'
import EditReserveParamsOpenTime from '@/components/scm/editReserveParamsOpenTime'

export default {
  name: 'ReserveParamsManageList',
  components: {
    AddReserveParams,
    EditReserveParams,
    DeleteReserveParams,
    EditReserveParamsOpenTime
  },
  data() {
    return {
      loading: false,
      searchForm: {
        name: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name,
          communityId: this.communityId
        }
        const { data, total } = await listReserveParams(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('reserveParamsManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addReserveParams.open()
    },
    handleEdit(row) {
      this.$refs.editReserveParams.open(row)
    },
    handleSetTime(row) {
      this.$refs.editReserveParamsOpenTime.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteReserveParams.open(row)
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
.reserve-params-manage-container {
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