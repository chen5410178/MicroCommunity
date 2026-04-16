<template>
  <div class="resource-enter-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceEnterManage.orderId') }}：{{ resourceEnterManageInfo.applyOrderId }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-table :data="resourceEnterManageInfo.purchaseApplyDetailVo" border style="width: 100%"
            class="table-wrapper" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="rstName" :label="$t('resourceEnterManage.itemType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.rstName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="resName" :label="$t('resourceEnterManage.itemName')" align="center" />
            <el-table-column prop="specName" :label="$t('resourceEnterManage.itemSpec')" align="center">
              <template slot-scope="scope">
                {{ scope.row.specName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="resCode" :label="$t('resourceEnterManage.itemCode')" align="center" />
            <el-table-column prop="stock" :label="$t('resourceEnterManage.itemStock')" align="center" />
            <el-table-column prop="standardPrice" :label="$t('resourceEnterManage.referencePrice')" align="center">
              <template slot-scope="scope">
                ￥{{ scope.row.standardPrice }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" :label="$t('resourceEnterManage.applyQuantity')" align="center" />
            <el-table-column :label="$t('resourceEnterManage.purchaseQuantity')" align="center">
              <template slot-scope="scope">
                <el-input v-model.number="scope.row.purchaseQuantity" type="number"
                  :placeholder="$t('resourceEnterManage.purchaseQuantityPlaceholder')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceEnterManage.purchasePrice')" align="center">
              <template slot-scope="scope">
                <el-input v-model.number="scope.row.price" type="number"
                  :placeholder="$t('resourceEnterManage.purchasePricePlaceholder')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceEnterManage.supplier')" align="center">
              <template slot-scope="scope">
                <el-select v-model="scope.row.rsId" :placeholder="$t('resourceEnterManage.selectSupplier')"
                  style="width:100%">
                  <el-option v-for="supplier in resourceEnterManageInfo.resourceSuppliers" :key="supplier.rsId"
                    :label="supplier.supplierName" :value="supplier.rsId" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column :label="$t('resourceEnterManage.remark')" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.purchaseRemark" type="text"
                  :placeholder="$t('resourceEnterManage.remarkPlaceholder')" />
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="footer-wrapper">
        <el-col :span="22" :offset="2">
          <el-button type="primary" @click="handleSubmit">{{ $t('resourceEnterManage.submit') }}</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { listPurchaseApplys, listResourceSuppliers, resourceEnter } from '@/api/resource/resourceEnterManageApi'

export default {
  name: 'ResourceEnterManageList',
  data() {
    return {
      resourceEnterManageInfo: {
        purchaseApplyDetailVo: [],
        resourceSuppliers: [],
        selectResIds: [],
        applyOrderId: '',
        taskId: '',
        resOrderType: ''
      }
    }
  },
  created() {
    this.resourceEnterManageInfo.applyOrderId = this.$route.query.applyOrderId
    this.resourceEnterManageInfo.resOrderType = this.$route.query.resOrderType
    this.resourceEnterManageInfo.taskId = this.$route.query.taskId
    this.loadData()
  },
  methods: {
    async loadData() {
      await this.listPurchaseApply()
      await this.loadResourceSuppliers()
    },
    async listPurchaseApply() {
      try {
        const params = {
          page: 1,
          row: 10,
          applyOrderId: this.resourceEnterManageInfo.applyOrderId,
          resOrderType: this.resourceEnterManageInfo.resOrderType
        }
        const data  = await listPurchaseApplys(params)
        const purchaseApply = data.purchaseApplys[0]
        Object.assign(this.resourceEnterManageInfo, purchaseApply)
        this.resourceEnterManageInfo.purchaseApplyDetailVo.forEach(item => {
          item.purchaseQuantity = ''
          item.price = ''
          item.purchaseRemark = ''
          item.rsId = ''
        })
      } catch (error) {
        console.error('Failed to load purchase apply:', error)
      }
    },
    async loadResourceSuppliers() {
      try {
        const params = { page: 1, row: 50 }
        const { data } = await listResourceSuppliers(params)
        this.resourceEnterManageInfo.resourceSuppliers = data
      } catch (error) {
        console.error('Failed to load resource suppliers:', error)
      }
    },
    handleSelectionChange(selection) {
      console.log(selection)
      this.resourceEnterManageInfo.selectResIds = selection.map(item => item.resId)
    },
    async handleSubmit() {
      // Validation
      let msg = ''
      const selectedItems = this.resourceEnterManageInfo.purchaseApplyDetailVo.filter(item =>
        this.resourceEnterManageInfo.selectResIds.includes(item.resId)
      )

      if (selectedItems.length < 1) {
        this.$message.error(this.$t('resourceEnterManage.selectItemError'))
        return
      }

      for (const item of selectedItems) {
        if (!item.purchaseQuantity || parseInt(item.purchaseQuantity) < 0) {
          msg = this.$t('resourceEnterManage.purchaseQuantityError')
          break
        }
        item.purchaseQuantity = parseInt(item.purchaseQuantity)

        if (!item.price || parseFloat(item.price) <= 0) {
          msg = this.$t('resourceEnterManage.purchasePriceError')
          break
        }
        item.price = parseFloat(item.price)
      }

      if (msg) {
        this.$message.error(msg)
        return
      }

      try {
        const response = await resourceEnter(this.resourceEnterManageInfo)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('Submit failed:', error)
        this.$message.error(this.$t('resourceEnterManage.submitError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-enter-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .table-wrapper {
    margin-top: 20px;
  }

  .footer-wrapper {
    margin-top: 20px;
    text-align: right;
  }
}
</style>