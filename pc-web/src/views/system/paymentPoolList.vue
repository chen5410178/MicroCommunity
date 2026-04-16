<template>
  <div class="payment-pool-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('paymentPool.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.paymentName" :placeholder="$t('paymentPool.search.paymentName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.paymentType" :placeholder="$t('paymentPool.search.paymentType')"
            style="width:100%">
            <el-option v-for="item in paymentTypes" :key="item.paymentType" :label="item.name"
              :value="item.paymentType" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('paymentPool.search.state')" style="width:100%">
            <el-option :label="$t('paymentPool.search.allStatus')" value="" />
            <el-option :label="$t('paymentPool.search.enable')" value="Y" />
            <el-option :label="$t('paymentPool.search.disable')" value="N" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('paymentPool.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="paymentName" :label="$t('paymentPool.table.paymentName')" align="center" />
        <el-table-column prop="paymentTypeName" :label="$t('paymentPool.table.paymentType')" align="center" />
        <el-table-column :label="$t('paymentPool.table.payRange')" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.payType === '1001'">
              {{ $t('paymentPool.table.communityFee') }}
            </div>
            <div v-else-if="scope.row.payType === '2002'">
              {{ $t('paymentPool.table.tempCarFee') }}
            </div>
            <div v-else>
              {{ $t('paymentPool.table.specifiedFee') }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('paymentPool.table.state')" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.state === 'Y' ? 'success' : 'danger'">
              {{ scope.row.state === 'Y' ? $t('paymentPool.table.enable') : $t('paymentPool.table.disable') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('paymentPool.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('paymentPool.table.remark')" align="center" />
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

      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-payment-pool ref="addPaymentPool" @success="handleSuccess" />
    <edit-payment-pool ref="editPaymentPool" @success="handleSuccess" />
    <delete-payment-pool ref="deletePaymentPool" @success="handleSuccess" />
  </div>
</template>

<script>
import { listPaymentPool, listPaymentAdapt } from '@/api/system/paymentPoolApi'
import AddPaymentPool from '@/components/system/addPaymentPool'
import EditPaymentPool from '@/components/system/editPaymentPool'
import DeletePaymentPool from '@/components/system/deletePaymentPool'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PaymentPoolList',
  components: {
    AddPaymentPool,
    EditPaymentPool,
    DeletePaymentPool
  },
  data() {
    return {
      loading: false,
      searchForm: {
        paymentName: '',
        paymentType: '',
        state: ''
      },
      tableData: [],
      paymentTypes: [],
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
    this.getPaymentTypes()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          ...this.searchForm
        }
        const { data, total } = await listPaymentPool(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('paymentPool.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getPaymentTypes() {
      try {
        const { data } = await listPaymentAdapt({
          page: 1,
          row: 100,
        })
        this.paymentTypes = data
      } catch (error) {
        console.error('获取支付类型失败:', error)
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addPaymentPool.open()
    },
    handleEdit(row) {
      this.$refs.editPaymentPool.open(row)
    },
    handleDelete(row) {
      this.$refs.deletePaymentPool.open(row)
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
.payment-pool-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>