<template>
    <div class="print-purchase-apply">
      <div class="content-wrapper">
        <!-- 申请单信息 -->
        <div class="row info-row">
          <div class="col-sm-4">
            <span>{{ $t('purchaseApplyDetail.applyNo') }}{{ printPurchaseApplyInfo.applyOrderId }}</span>
          </div>
          <div class="col-sm-4">
            <span>{{ $t('purchaseApplyDetail.applicant') }}{{ printPurchaseApplyInfo.endUserName }}</span>
          </div>
          <div class="col-sm-4">
            <span>{{ $t('purchaseApplyDetail.contactPhone') }}{{ printPurchaseApplyInfo.endUserTel }}</span>
          </div>
        </div>
  
        <!-- 采购明细表格 -->
        <table class="table table-bordered margin-top">
          <thead>
            <tr>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.serialNumber') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemName') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemCode') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.purchasePrice') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.applyQuantity') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.purchaseQuantity') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.remark') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in printPurchaseApplyInfo.purchaseApplyDetailVo" :key="index">
              <th scope="row" class="text-center">{{ index + 1 }}</th>
              <td class="text-center">{{ item.resName }}</td>
              <td class="text-center">{{ item.resCode }}</td>
              <td class="text-center">{{ item.purchasePrice }}</td>
              <td class="text-center">{{ item.quantity }}</td>
              <td class="text-center">{{ item.purchaseQuantity ? item.purchaseQuantity : '-' }}</td>
              <td class="text-center">{{ item.remark }}</td>
            </tr>
            <tr>
              <th scope="row" class="text-center">{{ $t('purchaseApplyDetail.remark') }}</th>
              <td colspan="4">{{ printPurchaseApplyInfo.description }}</td>
              <td class="text-center">{{ $t('purchaseApplyDetail.actualPurchaseTotalPrice') }}</td>
              <td>{{ printPurchaseApplyInfo.purchaseTotalPrice }}</td>
            </tr>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">{{ $t('purchaseApplyDetail.manufacturerSignature') }}</td>
              <td colspan="2"></td>
              <td colspan="1" class="text-center" style="vertical-align:middle;">{{ $t('purchaseApplyDetail.time') }}</td>
              <td colspan="2"></td>
  
            </tr>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">{{ $t('purchaseApplyDetail.purchaserSignature') }}</td>
              <td colspan="2"></td>
              <td colspan="1" class="text-center" style="vertical-align:middle;">{{ $t('purchaseApplyDetail.time') }}</td>
              <td colspan="2"></td>
  
            </tr>
          </tbody>
        </table>
  
        <!-- 操作按钮 -->
        <div id="print-btn" class="button-wrapper">
          <el-button type="warning" @click="closePage" style="margin-right:20px;">
            {{ $t('purchaseApplyDetail.cancel') }}
          </el-button>
          <el-button type="primary" @click="printPurchaseApplyDiv">
            <i class="el-icon-printer"></i> {{ $t('purchaseApplyDetail.print') }}
          </el-button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { listPurchaseApplys } from '@/api/resource/purchaseApplyManageApi'
  import { messages } from './purchaseApplyDetailLang'
  
  export default {
    name: 'PrintPurchaseApply',
    i18n: {
      messages
    },
    data() {
      return {
        printPurchaseApplyInfo: {
          applyOrderId: '',
          endUserName: '',
          endUserTel: '',
          description: '',
          purchaseTotalPrice: '',
          purchaseApplyDetailVo: []
        },
        printFlag: '0'
      }
    },
    created() {
      this.initPrintPurchaseApplyDateInfo()
    },
    methods: {
      // 初始化采购申请数据
      initPrintPurchaseApplyDateInfo() {
        const applyOrderId = this.$route.query.applyOrderId
        const resOrderType = this.$route.query.resOrderType
  
        if (!applyOrderId) {
          this.$message.error('缺少申请单号参数')
          return
        }
  
        const params = {
          page: 1,
          row: 1,
          applyOrderId: applyOrderId,
          resOrderType: resOrderType
        }
  
        listPurchaseApplys(params).then(res => {
          if (res.purchaseApplys && res.purchaseApplys.length > 0) {
            this.printPurchaseApplyInfo = res.purchaseApplys[0]
          } else {
            this.$message.error('未找到采购申请信息')
          }
        }).catch(error => {
          console.error('获取采购申请信息失败:', error)
          this.$message.error('获取采购申请信息失败')
        })
      },
  
      // 打印
      printPurchaseApplyDiv() {
        this.printFlag = '1'
        const printBtn = document.getElementById('print-btn')
        if (printBtn) {
          printBtn.style.display = 'none'
        }
        
        window.print()
        
        // 打印后关闭窗口
        setTimeout(() => {
          window.opener = null
          window.close()
        }, 100)
      },
  
      // 关闭页面
      closePage() {
        window.opener = null
        window.close()
      }
    }
  }
  </script>
  
  <style scoped>
  .print-purchase-apply {
    padding: 20px;
    background: #fff;
  }
  
  .content-wrapper {
    margin: 0 auto;
  }
  
  .info-row {
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
  }
  
  .col-sm-4 {
    flex: 1;
    padding: 0 10px;
  }
  
  .col-sm-4 span {
    font-size: 14px;
    color: #333;
  }
  
  .margin-top {
    margin-top: 20px;
  }
  
  /* 原生表格样式 */
  .table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  .table-bordered {
    border: 1px solid #ddd;
  }
  
  .table-bordered th,
  .table-bordered td {
    border: 1px solid #ddd;
    padding: 8px;
  }
  
  .table thead th {
    background-color: #f5f7fa;
    font-weight: bold;
    color: #333;
  }
  
  .text-center {
    text-align: center;
  }
  
  .button-wrapper {
    text-align: right;
    margin-top: 30px;
    padding: 20px 0;
  }
  
  /* 打印样式 */
  @media print {
    .print-purchase-apply {
      padding: 0;
    }
  
    #print-btn {
      display: none !important;
    }
  
    .table-bordered th,
    .table-bordered td {
      page-break-inside: avoid;
    }
  }
  </style>
  
  