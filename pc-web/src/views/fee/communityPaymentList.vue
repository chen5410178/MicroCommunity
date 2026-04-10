<template>
  <div class="community-payment-container">
    <el-row :gutter="20">
      <el-col :span="4" class="">
        <select-admin-community :community-id="searchForm.communityId"
        @changeCommunity="handleChangeCommunity" />
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('communityPayment.search') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="text-left">
            <el-form-item>
              <el-input
                v-model="searchForm.paymentName"
                :placeholder="$t('communityPayment.paymentName')"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="searchForm.state"
                :placeholder="$t('communityPayment.selectStatus')"
                clearable
              >
                <el-option value="" :label="$t('communityPayment.selectStatus')" />
                <el-option value="Y" :label="$t('communityPayment.enabled')" />
                <el-option value="N" :label="$t('communityPayment.disabled')" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                {{ $t('communityPayment.search') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="list-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('communityPayment.title') }}</span>
            <el-button
              v-if="searchForm.communityId"
              type="primary"
              size="mini"
              style="float: right"
              @click="openAddDialog"
            >
              {{ $t('communityPayment.add') }}
            </el-button>
          </div>
          <el-table :data="payments" border stripe>
            <el-table-column prop="paymentName" :label="$t('communityPayment.paymentName')" align="center" />
            <el-table-column prop="communityName" :label="$t('communityPayment.communityName')" align="center" />
            <el-table-column prop="paymentTypeName" :label="$t('communityPayment.paymentVendor')" align="center" />
            <el-table-column :label="$t('communityPayment.paymentScope')" align="center">
              <template slot-scope="{ row }">
                <span v-if="row.payType === '1001'">{{ $t('communityPayment.communityFee') }}</span>
                <span v-else-if="row.payType === '2002'">{{ $t('communityPayment.tempParkingFee') }}</span>
                <span v-else>{{ $t('communityPayment.specificFee') }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('communityPayment.status')" align="center">
              <template slot-scope="{ row }">
                {{ row.state === 'Y' ? $t('communityPayment.enabled') : $t('communityPayment.disabled') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('communityPayment.createTime')" align="center" />
            <el-table-column prop="remark" :label="$t('communityPayment.instruction')" align="center" />
            <el-table-column :label="$t('communityPayment.operation')" align="center" width="180">
              <template slot-scope="{ row }">
                <el-button size="mini" @click="openEditDialog(row)">{{ $t('communityPayment.edit') }}</el-button>
                <el-button size="mini" type="danger" @click="openDeleteDialog(row)">
                  {{ $t('communityPayment.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            :current-page="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
          />
        </el-card>
      </el-col>
    </el-row>

    <add-community-payment ref="addDialog" @success="handleSuccess" />
    <edit-community-payment ref="editDialog" @success="handleSuccess" />
    <delete-community-payment ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import AddCommunityPayment from '@/components/fee/AddCommunityPayment'
import EditCommunityPayment from '@/components/fee/EditCommunityPayment'
import DeleteCommunityPayment from '@/components/fee/DeleteCommunityPayment'
import { listAdminPayment } from '@/api/fee/communityPaymentApi'

export default {
  name: 'CommunityPaymentList',
  components: {
    SelectAdminCommunity,
    AddCommunityPayment,
    EditCommunityPayment,
    DeleteCommunityPayment
  },
  data() {
    return {
      searchForm: {
        paymentName: '',
        state: '',
        communityId: ''
      },
      payments: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchPayments()
  },
  methods: {
    async fetchPayments() {
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const res = await listAdminPayment(params)
        this.payments = res.data || []
        this.pagination.total = res.total || 0
      } catch (error) {
        console.error('Failed to fetch payments:', error)
        this.$message.error(this.$t('communityPayment.fetchError'))
      }
    },
    handleChangeCommunity(community) {
      this.searchForm.communityId = community.communityId
      this.fetchPayments()
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchPayments()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchPayments()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.fetchPayments()
    },
    openAddDialog() {
      this.$refs.addDialog.open({
        communityId: this.searchForm.communityId
      })
    },
    openEditDialog(row) {
      this.$refs.editDialog.open(row)
    },
    openDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSuccess() {
      this.fetchPayments()
    }
  }
}
</script>

<style lang="scss" scoped>
.community-payment-container {
  height: calc(100vh - 84px);
  
  .tree-container {
    height: calc(100vh - 140px);
    overflow-y: auto;
  }
  
  .search-card, .list-card {
    margin-bottom: 20px;
  }
}
</style>