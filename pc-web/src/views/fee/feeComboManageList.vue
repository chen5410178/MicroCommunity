<template>
  <div class="fee-combo-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeComboManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.comboName" :placeholder="$t('feeComboManage.search.comboName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 费用套餐列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeComboManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="comboId" :label="$t('feeComboManage.table.comboId')" align="center" />
        <el-table-column prop="comboName" :label="$t('feeComboManage.table.comboName')" align="center" />
        <el-table-column prop="createTime" :label="$t('feeComboManage.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('feeComboManage.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleToComboMember(scope.row)">
              {{ $t('feeComboManage.operation.feeItems') }}
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

      <div class="remark-wrapper text-left">
        <p>{{ $t('feeComboManage.remark.line1') }}</p>
        <p>{{ $t('feeComboManage.remark.line2') }}</p>
      </div>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-fee-combo ref="addFeeCombo" @success="handleSuccess" />
    <edit-fee-combo ref="editFeeCombo" @success="handleSuccess" />
    <delete-fee-combo ref="deleteFeeCombo" @success="handleSuccess" />
  </div>
</template>

<script>
import { listFeeCombo } from '@/api/fee/feeComboManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddFeeCombo from '@/components/fee/addFeeCombo'
import EditFeeCombo from '@/components/fee/editFeeCombo'
import DeleteFeeCombo from '@/components/fee/deleteFeeCombo'

export default {
  name: 'FeeComboManageList',
  components: {
    AddFeeCombo,
    EditFeeCombo,
    DeleteFeeCombo
  },
  data() {
    return {
      loading: false,
      searchForm: {
        comboName: ''
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
          comboName: this.searchForm.comboName,
          communityId: this.communityId
        }
        const { data, total } = await listFeeCombo(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('feeComboManage.fetchError'))
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
        comboName: ''
      }
      this.getList()
    },
    handleAdd() {
      this.$refs.addFeeCombo.open()
    },
    handleEdit(row) {
      this.$refs.editFeeCombo.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteFeeCombo.open(row)
    },
    handleToComboMember(row) {
      this.$router.push({
        path: '/views/fee/feeComboMemberManage',
        query: {
          comboId: row.comboId,
          comboName: row.comboName
        }
      })
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
.fee-combo-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;

    .remark-wrapper {
      margin-top: 20px;
      color: #999;
      font-size: 14px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>