<template>
  <div class="print-allocation-storehouse-container">
    <div>
      <div>
        <div class="flex justify-between">
          <div class="col-sm-4">
            <span>
              <span>{{ $t('printAllocationStorehouse.applyId') }}</span>：{{ printAllocationStorehouseInfo.conditions.applyId }}
            </span>
          </div>
          <div class="col-sm-4">
            <span>
              <span>{{ $t('printAllocationStorehouse.startUserName') }}</span>：{{ printAllocationStorehouseInfo.conditions.startUserName }}
            </span>
          </div>
          <div class="col-sm-4">
            <span>
              <span>{{ $t('printAllocationStorehouse.createTime') }}</span>：{{ printAllocationStorehouseInfo.conditions.createTime }}
            </span>
          </div>
        </div>
        
        <table class="table table-bordered margin-top">
          <thead>
            <tr>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.number') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.resName') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.resType') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.specName') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.resCode') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.isFixedName') }}</span>
              </th>
              <th scope="col" class="text-center">
                {{ printAllocationStorehouseInfo.conditions.applyType == '10000' ||
                   printAllocationStorehouseInfo.conditions.applyType == '30000' ? 
                   $t('printAllocationStorehouse.sourceWarehouse') : 
                   $t('printAllocationStorehouse.returnPerson') }}
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.targetStorehouse') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.originalStock') }}</span>
              </th>
              <th scope="col" class="text-center">
                <span>{{ $t('printAllocationStorehouse.allocationQuantity') }}</span>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in printAllocationStorehouseInfo.resourceStores" :key="index">
              <th scope="row" class="text-center">{{ index + 1 }}</th>
              <td class="text-center">{{ item.resName }}</td>
              <td class="text-center">{{ item.parentRstName }} > {{ item.rstName }}</td>
              <td class="text-center">{{ item.specName ? item.specName : '-' }}</td>
              <td class="text-center">{{ item.resCode }}</td>
              <td class="text-center">{{ item.isFixedName }}</td>
              <td class="text-center">
                {{ printAllocationStorehouseInfo.conditions.applyType == '10000' ||
                   printAllocationStorehouseInfo.conditions.applyType == '30000' ? 
                   item.shaName : printAllocationStorehouseInfo.conditions.startUserName }}
              </td>
              <td class="text-center">{{ item.shzName }}</td>
              <td class="text-center">{{ item.originalStock }}{{ item.unitCodeName }}</td>
              <td class="text-center">
                {{ item.stock }}{{ item.applyType == 20000 ? item.miniUnitCodeName : item.unitCodeName }}
              </td>
            </tr>
            <tr>
              <th scope="row" class="text-center">
                <span>{{ $t('printAllocationStorehouse.remark') }}</span>
              </th>
              <td colspan="9" style="text-align:center">{{ printAllocationStorehouseInfo.conditions.remark }}</td>
            </tr>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">
                <span>{{ $t('printAllocationStorehouse.storeManagerSign') }}</span>
              </td>
              <td colspan="2"></td>
              <td class="text-center" style="vertical-align:middle;">
                <span>{{ $t('printAllocationStorehouse.time') }}</span>
              </td>
              <td colspan="5"></td>
            </tr>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">
                <span>{{ $t('printAllocationStorehouse.allocationStaffSign') }}</span>
              </td>
              <td colspan="2"></td>
              <td class="text-center" style="vertical-align:middle;">
                <span>{{ $t('printAllocationStorehouse.time') }}</span>
              </td>
              <td colspan="5"></td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <div id="print-btn">
        <el-button type="primary" class="float-right" @click="handlePrint">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px;" @click="handleCancel">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { listAllocationStorehouseApplys, listAllocationStorehouses } from '@/api/resource/printAllocationStorehouseApi'

export default {
  name: 'PrintAllocationStorehouseList',
  data() {
    return {
      printAllocationStorehouseInfo: {
        resourceStores: [],
        conditions: {}
      },
      printFlag: '0'
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      try {
        const applyId = this.$route.query.applyId
        const params = {
          page: 1,
          row: 1,
          applyId: applyId
        }
        
        const [applyRes, storeRes] = await Promise.all([
          listAllocationStorehouseApplys(params),
          listAllocationStorehouses(params)
        ])
        
        this.printAllocationStorehouseInfo.conditions = applyRes.data[0]
        this.printAllocationStorehouseInfo.resourceStores = storeRes.data
      } catch (error) {
        this.$message.error(this.$t('printAllocationStorehouse.fetchError'))
      }
    },
    handlePrint() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.close()
    },
    handleCancel() {
      window.close()
    }
  }
}
</script>

<style scoped>
.print-allocation-storehouse-container {
  padding: 20px;
}

.margin-top {
  margin-top: 20px;
}

.text-center {
  text-align: center;
}

.float-right {
  float: right;
}

/* Bootstrap 表格样式 */
.table {
  width: 100%;
  margin-bottom: 1rem;
  color: #212529;
  border-collapse: collapse;
}

.table-bordered {
  border: 1px solid #dee2e6;
}

.table-bordered th,
.table-bordered td {
  border: 1px solid #dee2e6;
}

.table th,
.table td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}

.table thead th {
  vertical-align: bottom;
  border-bottom: 2px solid #dee2e6;
  background-color: #f8f9fa;
  font-weight: 500;
}

/* Bootstrap 栅格系统 */
.row {
  display: flex;
  flex-wrap: wrap;
  margin-right: -15px;
  margin-left: -15px;
}

.col-sm-4 {
  flex: 0 0 33.333333%;
  max-width: 33.333333%;
  padding-right: 15px;
  padding-left: 15px;
}

/* 打印样式 */
@media print {
  .print-allocation-storehouse-container {
    padding: 0;
  }
  
  #print-btn {
    display: none !important;
  }
  
  .table {
    border: 1px solid #000;
  }
  
  .table th,
  .table td {
    border: 1px solid #000;
    padding: 8px;
  }
  
  .table thead th {
    background-color: #f0f0f0 !important;
  }
}
</style>