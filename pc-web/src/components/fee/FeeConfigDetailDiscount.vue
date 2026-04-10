<template>
  <div>
    <el-table :data="discounts" border style="width: 100%">
      <el-table-column prop="discountName" :label="$t('feeConfigDetail.discountName')" align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.rule')" align="center">
        <template slot-scope="scope">
          <div v-for="(item, index) in scope.row.feeDiscountSpecs" :key="index">
            {{ item.specName }}：{{ item.specValue }}
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetail.discountType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.discountType == '1001' ? $t('feeConfigDetail.discount') : $t('feeConfigDetail.breach') }}
        </template>
      </el-table-column>
      <el-table-column prop="startTime" :label="$t('feeConfigDetail.payStartTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('feeConfigDetail.payEndTime')" align="center"></el-table-column>
      <el-table-column prop="payMaxEndTime" :label="$t('feeConfigDetail.discountEndTime')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="openDeleteModel(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      class="pagination-wrapper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total">
    </el-pagination>
    
    <div class="tip-wrapper">
      <p>{{ $t('feeConfigDetail.tip1') }}</p>
      <p>{{ $t('feeConfigDetail.tip2') }}</p>
      <p>{{ $t('feeConfigDetail.tip3') }}</p>
      <p>{{ $t('feeConfigDetail.tip4') }}</p>
    </div>
    
    <el-dialog
      :title="$t('feeConfigDetail.confirmDelete')"
      :visible.sync="deleteDialogVisible"
      width="30%">
      <span>{{ $t('feeConfigDetail.confirmDeleteDiscount') }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="danger" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { queryPayFeeConfigDiscount, deletePayFeeConfigDiscount } from '@/api/fee/feeConfigDetailApi'

export default {
  name: 'FeeConfigDetailDiscount',
  data() {
    return {
      discounts: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      configId: '',
      deleteDialogVisible: false,
      currentDiscount: null
    }
  },
  methods: {
    switchTab(params) {
      this.configId = params.configId
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          configId: this.configId,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.getCommunityId()
        }
        const response = await queryPayFeeConfigDiscount(params)
        this.discounts = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('Failed to load discounts:', error)
      }
    },
    openDeleteModel(discount) {
      this.currentDiscount = discount
      this.deleteDialogVisible = true
    },
    async confirmDelete() {
      try {
        await deletePayFeeConfigDiscount({
          configDiscountId: this.currentDiscount.configDiscountId,
          communityId: this.getCommunityId()
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.loadData()
        this.deleteDialogVisible = false
      } catch (error) {
        console.error('Failed to delete discount:', error)
        this.$message.error(this.$t('feeConfigDetail.deleteFailed'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
.tip-wrapper {
  margin-top: 20px;
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
}
</style>