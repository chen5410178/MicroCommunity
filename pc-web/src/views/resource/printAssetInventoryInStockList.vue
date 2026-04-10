<template>
  <div class="print-asset-inventory-container">

      <el-row :gutter="20">
        <el-col :span="6">
          <span>
            <span>{{ $t('printAssetInventoryInStock.inventoryName') }}</span>：
            {{ printAssetInventoryInStockInfo.conditions.name }}
          </span>
        </el-col>
        <el-col :span="6">
          <span>
            <span>{{ $t('printAssetInventoryInStock.inventoryPerson') }}</span>：
            {{ printAssetInventoryInStockInfo.conditions.staffName }}
          </span>
        </el-col>
        <el-col :span="6">
          <span>
            <span>{{ $t('printAssetInventoryInStock.warehouse') }}</span>：
            {{ printAssetInventoryInStockInfo.conditions.shName }}
          </span>
        </el-col>
        <el-col :span="6">
          <span>
            <span>{{ $t('printAssetInventoryInStock.inventoryDate') }}</span>：
            {{ printAssetInventoryInStockInfo.conditions.invTime }}
          </span>
        </el-col>
      </el-row>

      <table class="table table-bordered margin-top">
        <thead>
          <tr>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.warehouseName') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.itemType') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.itemName') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.specification') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.itemCode') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.isFixed') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.batchPrice') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.originalStock') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.afterInventory') }}
            </th>
            <th scope="col" class="text-center">
              {{ $t('printAssetInventoryInStock.remark') }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in printAssetInventoryInStockInfo.storehouses" :key="index">
            <td class="text-center">{{ item.shName }}</td>
            <td class="text-center">
              {{ item.parentRstName ? item.parentRstName : '-' }} > {{ item.rstName ? item.rstName : '-' }}
            </td>
            <td class="text-center">{{ item.resName }}</td>
            <td class="text-center">{{ item.specName ? item.specName : '-' }}</td>
            <td class="text-center">{{ item.resCode }}</td>
            <td class="text-center">{{ item.isFixedName }}</td>
            <td class="text-center">{{ item.timesPrice }}</td>
            <td class="text-center">{{ item.originalStock }}{{ item.unitCodeName }}</td>
            <td class="text-center">{{ item.quantity }}{{ item.unitCodeName }}</td>
            <td class="text-center">{{ item.remark }}</td>
          </tr>
          <tr>
            <th scope="row" class="text-center">
              {{ $t('printAssetInventoryInStock.remark') }}
            </th>
            <td colspan="9" style="text-align:center">
              {{ printAssetInventoryInStockInfo.conditions.remark }}
            </td>
          </tr>
          <tr height="60px">
            <td colspan="2" class="text-center" style="vertical-align:middle;">
              {{ $t('printAssetInventoryInStock.warehouseManagerSign') }}
            </td>
            <td colspan="2"></td>
            <td class="text-center" style="vertical-align:middle;">
              {{ $t('printAssetInventoryInStock.time') }}
            </td>
            <td colspan="5"></td>
          </tr>
          <tr height="60px">
            <td colspan="2" class="text-center" style="vertical-align:middle;">
              {{ $t('printAssetInventoryInStock.transferPersonSign') }}
            </td>
            <td colspan="2"></td>
            <td class="text-center" style="vertical-align:middle;">
              {{ $t('printAssetInventoryInStock.time') }}
            </td>
            <td colspan="5"></td>
          </tr>
        </tbody>
      </table>

      <div id="print-btn">
        <el-button type="primary" class="float-right" @click="_printAssetInventoryInStockDiv()">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px;" @click="_closePage()">
          {{ $t('common.cancel') }}
        </el-button>
      </div>

  </div>
</template>

<script>
import { getAssetInventoryList, getAssetInventoryDetailList } from '@/api/resource/printAssetInventoryInStockApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PrintAssetInventoryInStockList',
  data() {
    return {
      printAssetInventoryInStockInfo: {
        storehouses: [],
        conditions: {}
      },
      printFlag: '0',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initPrintAssetInventoryInStockInfo()
    this._listPrintAssetInventoryInStock(1, 100)
  },
  methods: {
    async _initPrintAssetInventoryInStockInfo() {
      try {
        const aiId = this.$route.query.aiId
        const params = {
          page: 1,
          row: 1,
          applyId: aiId,
          communityId: this.communityId
        }
        const { data } = await getAssetInventoryList(params)
        this.printAssetInventoryInStockInfo.conditions = data[0]
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listPrintAssetInventoryInStock(page, rows) {
      try {
        const aiId = this.$route.query.aiId
        const params = {
          page: page,
          row: rows,
          applyOrderId: aiId,
          communityId: this.communityId
        }
        const { data } = await getAssetInventoryDetailList(params)
        this.printAssetInventoryInStockInfo.storehouses = data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _printAssetInventoryInStockDiv() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    }
  }
}
</script>

<style scoped>
.print-asset-inventory-container {
  padding: 20px;
}

.table {
  width: 100%;
  margin-top: 20px;
  border-collapse: collapse;
}

.table th,
.table td {
  border: 1px solid #ebeef5;
  padding: 12px 15px;
}

.text-center {
  text-align: center;
}

.margin-top {
  margin-top: 20px;
}

.float-right {
  float: right;
}

#print-btn {
  margin-top: 20px;
  overflow: hidden;
}
</style>