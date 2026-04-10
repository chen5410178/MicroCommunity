<template>
  <div class="fee-discount-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeDiscountManage.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition()">
          {{ feeDiscountManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="feeDiscountManageInfo.conditions.discountId"
            :placeholder="$t('feeDiscountManage.search.discountId')" clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="feeDiscountManageInfo.conditions.discountName"
            :placeholder="$t('feeDiscountManage.search.discountName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="feeDiscountManageInfo.conditions.discountType"
            :placeholder="$t('feeDiscountManage.search.discountType')" style="width:100%">
            <el-option v-for="item in feeDiscountManageInfo.discountTypes" :key="item.statusCd" :label="item.name"
              :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryFeeDiscountMethod()">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetFeeDiscountMethod()">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row v-show="feeDiscountManageInfo.moreCondition" :gutter="20">
        <el-col :span="6">
          <el-input v-model="feeDiscountManageInfo.conditions.ruleName"
            :placeholder="$t('feeDiscountManage.search.ruleName')" clearable />
        </el-col>
      </el-row>
    </el-card>

    <!-- 折扣信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeDiscountManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="_openAddFeeDiscountModal()">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </div>
      <el-table :data="feeDiscountManageInfo.feeDiscounts" border style="width: 100%" v-loading="loading">
        <el-table-column prop="discountId" :label="$t('feeDiscountManage.table.discountId')" align="center" />
        <el-table-column prop="discountName" :label="$t('feeDiscountManage.table.discountName')" align="center" />
        <el-table-column prop="discountTypeName" :label="$t('feeDiscountManage.table.discountType')" align="center" />
        <el-table-column prop="ruleName" :label="$t('feeDiscountManage.table.ruleName')" align="center" />
        <el-table-column :label="$t('feeDiscountManage.table.rule')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.feeDiscountSpecs" :key="index">
              {{ item.specName }}：{{ item.specValue }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('feeDiscountManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openEditFeeDiscountModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteFeeDiscountModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="page.total" style="margin-top: 20px;" />
    </el-card>

    <!-- 组件 -->
    <add-fee-discount ref="addFeeDiscount" @success="handleSuccess" />
    <edit-fee-discount ref="editFeeDiscount" @success="handleSuccess" />
    <delete-fee-discount ref="deleteFeeDiscount" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDict,getCommunityId } from '@/api/community/communityApi'
import { queryFeeDiscount } from '@/api/fee/feeDiscountManageApi'
import AddFeeDiscount from '@/components/fee/addFeeDiscount'
import EditFeeDiscount from '@/components/fee/editFeeDiscount'
import DeleteFeeDiscount from '@/components/fee/deleteFeeDiscount'

export default {
  name: 'FeeDiscountManageList',
  components: {
    AddFeeDiscount,
    EditFeeDiscount,
    DeleteFeeDiscount
  },
  data() {
    return {
      loading: false,
      feeDiscountManageInfo: {
        feeDiscounts: [],
        moreCondition: false,
        discountTypes: [],
        conditions: {
          discountId: '',
          discountName: '',
          discountType: '',
          ruleName: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getCommunityId()
    this.getDictData()
    this._listFeeDiscounts(this.page.current, this.page.size)
  },
  methods: {
    async getCommunityId() {
      try {
        const communityId = await getCommunityId()
        this.feeDiscountManageInfo.conditions.communityId = communityId
      } catch (error) {
        console.error('获取communityId失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('fee_discount', 'discount_type')
        this.feeDiscountManageInfo.discountTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listFeeDiscounts(page, size) {
      this.loading = true
      try {
        const params = {
          page,
          row: size,
          ...this.feeDiscountManageInfo.conditions
        }
        const { data, total } = await queryFeeDiscount(params)
        this.feeDiscountManageInfo.feeDiscounts = data
        this.page.total = total
      } catch (error) {
        console.error('获取折扣列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    _openAddFeeDiscountModal() {
      this.$refs.addFeeDiscount.open()
    },
    _openEditFeeDiscountModel(row) {
      this.$refs.editFeeDiscount.open(row)
    },
    _openDeleteFeeDiscountModel(row) {
      this.$refs.deleteFeeDiscount.open(row)
    },
    _queryFeeDiscountMethod() {
      this.page.current = 1
      this._listFeeDiscounts(this.page.current, this.page.size)
    },
    _resetFeeDiscountMethod() {
      this.feeDiscountManageInfo.conditions = {
        ...this.feeDiscountManageInfo.conditions,
        discountName: '',
        discountType: '',
        ruleName: '',
        discountId: ''
      }
      this._listFeeDiscounts(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.feeDiscountManageInfo.moreCondition = !this.feeDiscountManageInfo.moreCondition
    },
    handleSuccess() {
      this._listFeeDiscounts(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFeeDiscounts(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFeeDiscounts(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-discount-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 10px;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;
  }
}
</style>