<template>
  <div class="resource-detail-purchase">
    <el-table v-loading="loading" :data="purchaseApplyDetails" border style="width: 100%">
      <el-table-column prop="applyOrderId" :label="$t('resourceDetail.purchase.applyOrderId')" align="center" />
      <el-table-column prop="userName" :label="$t('resourceDetail.purchase.applicant')" align="center" />
      <el-table-column prop="endUserName" :label="$t('resourceDetail.purchase.endUser')" align="center" />
      <el-table-column :label="$t('resourceDetail.purchase.itemType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column prop="resName" :label="$t('resourceDetail.purchase.itemName')" align="center" />
      <el-table-column :label="$t('resourceDetail.purchase.itemSpec')" align="center">
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="isFixedName" :label="$t('resourceDetail.purchase.fixedItem')" align="center" />
      <el-table-column :label="$t('resourceDetail.purchase.supplier')" align="center">
        <template slot-scope="scope">
          {{ scope.row.supplierName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="shName" :label="$t('resourceDetail.purchase.warehouse')" align="center" />
      <el-table-column :label="$t('resourceDetail.purchase.applyQuantity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.quantity }}{{ scope.row.unitCodeName || '' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.purchase.actualQuantity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.purchaseQuantity ? scope.row.purchaseQuantity + scope.row.unitCodeName : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.purchase.purchasePrice')" align="center">
        <template slot-scope="scope">
          ￥{{ scope.row.price ? scope.row.price : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.purchase.totalPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.totalPrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.purchase.remark')" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
          <el-button v-if="scope.row.purchaseRemark" type="text" icon="el-icon-info"
            @click="viewPurchaseRemark(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('resourceDetail.purchase.status')" align="center" />
      <el-table-column prop="createTime" :label="$t('resourceDetail.purchase.createTime')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { getPurchaseApplyDetailList } from '@/api/resource/resourceDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceDetailPurchase',
  data() {
    return {
      loading: false,
      purchaseApplyDetails: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      resId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    async loadData(params) {
      this.resId = params.resId
      await this.getList()
    },
    async getList() {
      try {
        this.loading = true
        const { purchaseApplyDetails, total } = await getPurchaseApplyDetailList({
          resId: this.resId,
          resOrderType: 10000,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.purchaseApplyDetails = purchaseApplyDetails
        this.page.total = total
      } catch (error) {
        console.error('Failed to load purchase details:', error)
      } finally {
        this.loading = false
      }
    },
    viewPurchaseRemark(row) {
      this.$alert(row.purchaseRemark, this.$t('resourceDetail.purchase.remark'), {
        confirmButtonText: this.$t('common.confirm')
      })
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
.resource-detail-purchase {
  padding: 20px;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>