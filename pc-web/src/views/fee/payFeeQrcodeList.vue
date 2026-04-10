<template>
  <div class="pay-fee-qrcode-container">
    <!-- Search Condition -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeQrcode.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.qrcodeName" :placeholder="$t('payFeeQrcode.search.qrcodeName')" clearable />
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

    <!-- Data List -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeQrcode.list.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="qrcodeName" :label="$t('payFeeQrcode.table.qrcodeName')" align="center" />
        <el-table-column prop="queryWay" :label="$t('payFeeQrcode.table.queryWay')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.queryWay === '1001'">
              {{ $t('payFeeQrcode.queryWay.byPhone') }}
            </span>
            <span v-else-if="scope.row.queryWay === '2002'">
              {{ $t('payFeeQrcode.queryWay.byHouse') }}
            </span>
            <span v-else>
              {{ $t('payFeeQrcode.queryWay.byBoth') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="smsValidate" :label="$t('payFeeQrcode.table.smsValidate')" align="center">
          <template slot-scope="scope">
            {{ scope.row.smsValidate === 'ON' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="customFee" :label="$t('payFeeQrcode.table.customFee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.customFee === 'ON' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="feeType" :label="$t('payFeeQrcode.table.feeType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.feeType === 'OWNER' ? $t('payFeeQrcode.feeType.owner') : $t('payFeeQrcode.feeType.house') }}
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('payFeeQrcode.table.state')" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.state === 'ON' ? 'success' : 'danger'">
              {{ scope.row.state === 'ON' ? $t('common.enabled') : $t('common.disabled') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createStaffName" :label="$t('payFeeQrcode.table.createStaffName')" align="center" />
        <el-table-column prop="createTime" :label="$t('payFeeQrcode.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">
              {{ $t('payFeeQrcode.operation.viewQrcode') }}
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

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- Components -->
    <add-pay-fee-qrcode ref="addDialog" @success="handleSuccess" />
    <edit-pay-fee-qrcode ref="editDialog" @success="handleSuccess" />
    <delete-pay-fee-qrcode ref="deleteDialog" @success="handleSuccess" />
    <view-pay-fee-qrcode ref="viewDialog" />
  </div>
</template>

<script>
import { listPayFeeQrcode } from '@/api/fee/payFeeQrcodeApi'
import AddPayFeeQrcode from '@/components/fee/addPayFeeQrcode'
import EditPayFeeQrcode from '@/components/fee/editPayFeeQrcode'
import DeletePayFeeQrcode from '@/components/fee/deletePayFeeQrcode'
import ViewPayFeeQrcode from '@/components/fee/viewPayFeeQrcode'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PayFeeQrcodeList',
  components: {
    AddPayFeeQrcode,
    EditPayFeeQrcode,
    DeletePayFeeQrcode,
    ViewPayFeeQrcode
  },
  data() {
    return {
      loading: false,
      searchForm: {
        qrcodeName: '',
        customFee: '',
        preFee: ''
      },
      tableData: [],
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          qrcodeName: this.searchForm.qrcodeName,
          communityId: this.communityId
        }
        const { data, total } = await listPayFeeQrcode(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('payFeeQrcode.fetchError'))
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
        qrcodeName: '',
        customFee: '',
        preFee: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleView(row) {
      this.$refs.viewDialog.open(row)
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
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-qrcode-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-input {
      width: 100%;
    }
  }

  .list-wrapper {
    .float-right {
      float: right;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>