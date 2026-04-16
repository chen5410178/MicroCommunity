<template>
  <div class="work-type-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('workType.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.typeName" :placeholder="$t('workType.search.typeName')" clearable />
        </el-col>
        <el-col :span="6" >
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

    <!-- 工作单类型列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('workType.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right;" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="typeName" :label="$t('workType.table.typeName')" align="center" />
        <el-table-column prop="deduction" :label="$t('workType.table.deduction')" align="center">
          <template slot-scope="scope">
            {{ scope.row.deduction === 'Y' ? $t('workType.yes') : $t('workType.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="smsWay" :label="$t('workType.table.smsWay')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.smsWay === 'WECHAT'">{{ $t('workType.wechat') }}</span>
            <span v-else-if="scope.row.smsWay === 'ALI_SMS'">{{ $t('workType.aliSms') }}</span>
            <span v-else-if="scope.row.smsWay === 'WORK_LICENSE'">{{ $t('workType.workLicense') }}</span>
            <span v-else>{{ $t('workType.unknown') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('workType.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('workType.table.remark')" align="center">
          <template slot-scope="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
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

    <!-- 组件 -->
    <add-work-type ref="addWorkType" @success="handleSuccess" />
    <edit-work-type ref="editWorkType" @success="handleSuccess" />
    <delete-work-type ref="deleteWorkType" @success="handleSuccess" />
  </div>
</template>

<script>
import { listWorkType } from '@/api/oa/workTypeApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddWorkType from '@/components/oa/addWorkType'
import EditWorkType from '@/components/oa/editWorkType'
import DeleteWorkType from '@/components/oa/deleteWorkType'

export default {
  name: 'WorkTypeList',
  components: {
    AddWorkType,
    EditWorkType,
    DeleteWorkType
  },
  data() {
    return {
      loading: false,
      searchForm: {
        typeName: ''
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
          typeName: this.searchForm.typeName,
          communityId: this.communityId
        }
        const { data, total } = await listWorkType(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('workType.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        typeName: ''
      }
      this.getList()
    },
    handleAdd() {
      this.$refs.addWorkType.open()
    },
    handleEdit(row) {
      this.$refs.editWorkType.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteWorkType.open(row)
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
.work-type-container {
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