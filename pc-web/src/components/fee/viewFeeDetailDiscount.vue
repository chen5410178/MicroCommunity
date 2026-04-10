<template>
  <el-dialog
    :title="$t('viewFeeDetailDiscount.discount')"
    :visible.sync="visible"
    width="70%"
    center
  >
    <el-table
      :data="viewFeeDetailDiscountInfo.feeDetailDiscounts"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column 
        prop="discountName" 
        :label="$t('viewFeeDetailDiscount.discountName')" 
        align="center"
      />
      <el-table-column 
        :label="$t('viewFeeDetailDiscount.discountType')" 
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.discountType === '1001' ? $t('viewFeeDetailDiscount.discount') : $t('viewFeeDetailDiscount.breach') }}
        </template>
      </el-table-column>
      <el-table-column 
        prop="ruleName" 
        :label="$t('viewFeeDetailDiscount.ruleName')" 
        align="center"
      />
      <el-table-column 
        :label="$t('viewFeeDetailDiscount.discountRule')" 
        align="center"
      >
        <template slot-scope="scope">
          <div v-for="(item,index) in scope.row.feeDiscountSpecs" :key="index">
            {{item.specName}}：{{item.specValue}}
          </div>
        </template>
      </el-table-column>
      <el-table-column 
        prop="discountPrice" 
        :label="$t('viewFeeDetailDiscount.discountAmount')" 
        align="center"
      />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeDetailDiscount } from '@/api/fee/propertyFeeApi'

export default {
  name: 'ViewFeeDetailDiscount',
  data() {
    return {
      visible: false,
      viewFeeDetailDiscountInfo: {
        feeDetailDiscounts: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.visible = true
      this._loadAllFeeDetailDiscountInfo(params)
    },
    async _loadAllFeeDetailDiscountInfo(params) {
      try {
        const res = await queryFeeDetailDiscount({
          page: 1,
          row: 30,
          communityId: this.communityId,
          detailId: params.detailId
        })
        
        this.viewFeeDetailDiscountInfo.feeDetailDiscounts = res.data || []
      } catch (error) {
        console.error('加载折扣信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 15px;
}
</style>