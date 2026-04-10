<template>
  <div class="resource-detail-item-out">
    <el-table v-loading="loading" :data="purchaseApplyDetails" border style="width: 100%">
      <el-table-column prop="applyOrderId" :label="$t('resourceDetail.itemOut.applyOrderId')" align="center" />
      <el-table-column prop="userName" :label="$t('resourceDetail.itemOut.applicant')" align="center" />
      <el-table-column prop="endUserName" :label="$t('resourceDetail.itemOut.endUser')" align="center" />
      <el-table-column :label="$t('resourceDetail.itemOut.itemType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column prop="resName" :label="$t('resourceDetail.itemOut.itemName')" align="center" />
      <el-table-column :label="$t('resourceDetail.itemOut.itemSpec')" align="center">
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="isFixedName" :label="$t('resourceDetail.itemOut.fixedItem')" align="center" />
      <el-table-column :label="$t('resourceDetail.itemOut.supplier')" align="center">
        <template slot-scope="scope">
          {{ scope.row.supplierName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="shName" :label="$t('resourceDetail.itemOut.warehouse')" align="center" />
      <el-table-column :label="$t('resourceDetail.itemOut.applyQuantity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.quantity }}{{ scope.row.unitCodeName || '' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.itemOut.quantity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.purchaseQuantity ? scope.row.purchaseQuantity + scope.row.unitCodeName : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.itemOut.purchasePrice')" align="center">
        <template slot-scope="scope">
          ￥{{ scope.row.price ? scope.row.price : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.itemOut.totalPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.totalApplyPrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('resourceDetail.itemOut.remark')" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
          <el-button v-if="scope.row.purchaseRemark" type="text" icon="el-icon-info"
            @click="viewPurchaseRemark(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('resourceDetail.itemOut.status')" align="center" />
      <el-table-column prop="createTime" :label="$t('resourceDetail.itemOut.createTime')" align="center" />
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
  name: 'ResourceDetailItemOut',
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
          resOrderType: 20000,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.purchaseApplyDetails = purchaseApplyDetails.map(item => {
          item.purchaseQuantity = item.purchaseQuantity || 0
          item.price = item.price || 0
          item.totalApplyPrice = (item.purchaseQuantity * item.price).toFixed(2)
          return item
        })
        this.page.total = total
      } catch (error) {
        console.error('Failed to load item out details:', error)
      } finally {
        this.loading = false
      }
    },
    viewPurchaseRemark(row) {
      this.$alert(row.purchaseRemark, this.$t('resourceDetail.itemOut.remark'), {
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
.resource-detail-item-out {
  padding: 20px;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>