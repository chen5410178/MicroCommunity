<template>
  <div>
    <el-row :gutter="20" class="filter-wrapper">
      <el-col :span="8">
        <el-select v-model="payerObjType" :placeholder="$t('feeConfigDetail.selectFeeObject')" class="filter-item">
          <el-option :label="$t('feeConfigDetail.house')" value="3333"></el-option>
          <el-option :label="$t('feeConfigDetail.car')" value="6666"></el-option>
          <el-option :label="$t('feeConfigDetail.contract')" value="7777"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="loadData">{{ $t('common.search') }}</el-button>
      </el-col>
    </el-row>

    <el-table :data="feeObjs" border style="width: 100%">
      <el-table-column prop="feeId" :label="$t('feeConfigDetail.feeId')" align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.objectType')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.payerObjType === '3333'">{{ $t('feeConfigDetail.house') }}</span>
          <span v-else-if="scope.row.payerObjType === '6666'">{{ $t('feeConfigDetail.car') }}</span>
          <span v-else>{{ $t('feeConfigDetail.contract') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="payerObjName" :label="$t('feeConfigDetail.objectName')" align="center"></el-table-column>
      <el-table-column prop="ownerName" :label="$t('feeConfigDetail.ownerName')" align="center"></el-table-column>
      <el-table-column prop="ownerTel" :label="$t('feeConfigDetail.phone')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('feeConfigDetail.createTime')" align="center"></el-table-column>
    </el-table>

    <el-pagination class="pagination-wrapper" @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>
  </div>
</template>

<script>
import { listConfigFeeObjs } from '@/api/fee/feeConfigDetailApi'

export default {
  name: 'FeeDetailFeeObj',
  data() {
    return {
      payerObjType: '',
      feeObjs: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      configId: ''
    }
  },
  methods: {
    open(params) {
      this.configId = params.configId
      this.loadData()
    },
    switchTab(params) {
      this.configId = params.configId
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          configId: this.configId,
          payerObjType: this.payerObjType,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.getCommunityId()
        }
        const response = await listConfigFeeObjs(params)
        this.feeObjs = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('Failed to load fee objects:', error)
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
.filter-wrapper {
  margin-bottom: 20px;
}

.filter-item {
  width: 100%;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>