<template>
  <el-dialog
    :title="$t('chooseFeeCombo.title')"
    :visible.sync="dialogVisible"
    width="70%"
    @close="handleClose"
  >

      <div class="filter-container">
        <el-input
          v-model="currentFeeComboName"
          :placeholder="$t('chooseFeeCombo.searchPlaceholder')"
          class="filter-item"
          style="width: 300px; margin-right: 10px;"
          @keyup.enter.native="handleFilter"
        />
        <el-button
          type="primary"
          class="filter-item"
          @click="handleFilter"
        >
          {{ $t('common.search') }}
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="feeCombos"
        border
        fit
        highlight-current-row
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column
          :label="$t('chooseFeeCombo.comboName')"
          prop="comboName"
          align="center"
        />
        <el-table-column
          :label="$t('chooseFeeCombo.createTime')"
          prop="createTime"
          align="center"
        />
        <el-table-column
          :label="$t('chooseFeeCombo.remark')"
          prop="remark"
          align="center"
        />
        <el-table-column
          :label="$t('common.operation')"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="handleChoose(scope.row)"
            >
              {{ $t('common.choose') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listFeeCombo } from '@/api/fee/createFeeByComboApi'

export default {
  name: 'ChooseFeeCombo',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      feeCombos: [],
      currentFeeComboName: '',
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.resetSearch()
      this.getList()
    },
    handleClose() {
      this.dialogVisible = false
    },
    resetSearch() {
      this.currentFeeComboName = ''
      this.pagination.current = 1
    },
    handleFilter() {
      this.pagination.current = 1
      this.getList()
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId,
          name: this.currentFeeComboName
        }
        const { data, total } = await listFeeCombo(params)
        this.feeCombos = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load fee combos:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleChoose(row) {
      this.$emit('chooseFeeCombo', row)
      this.$emit('loadData', { feeComboId: row.feeComboId })
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>