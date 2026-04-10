<template>
    <div class="print-purchase-out-apply">
      <div class="content-wrapper">
        <!-- 申请单信息 -->
        <div class="row info-row">
          <div class="col-sm-3">
            <span>{{ $t('purchaseApplyDetail.applyNo') }}{{ printPurchaseOutApplyInfo.applyOrderId }}</span>
          </div>
          <div class="col-sm-3">
            <span>{{ $t('purchaseApplyDetail.applicant') }}{{ printPurchaseOutApplyInfo.userName }}</span>
          </div>
          <div class="col-sm-3">
            <span>{{ $t('purchaseApplyDetail.contactPhone') }}{{ printPurchaseOutApplyInfo.endUserTel }}</span>
          </div>
          <div class="col-sm-3">
            <span>{{ $t('purchaseApplyDetail.applyTime') }}{{ printPurchaseOutApplyInfo.createTime }}</span>
          </div>
        </div>
  
        <!-- 领用明细表格 -->
        <table class="table table-bordered margin-top">
          <thead>
            <tr>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.serialNumber') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemType') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemName') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemCode') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.warehouse') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.itemSpec') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.supplier') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.fixedItem') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.referencePrice') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.originalStock') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.currentStock') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.applyQuantity') }}</th>
              <th scope="col" class="text-center">{{ $t('purchaseApplyDetail.receiveQuantity') }}</th>
            </tr>
          </thead>
          <tbody>
            <template v-for="(item, index) in printPurchaseOutApplyInfo.purchaseApplyDetailVo">
              <tr :key="'detail-' + index">
                <th scope="row" class="text-center">{{ index + 1 }}</th>
                <td class="text-center">{{ item.parentRstName }} > {{ item.rstName }}</td>
                <td class="text-center">{{ item.resName }}</td>
                <td class="text-center">{{ item.resCode }}</td>
                <td class="text-center">{{ item.shName }}</td>
                <td class="text-center">{{ item.specName ? item.specName : '-' }}</td>
                <td class="text-center">{{ item.supplierName ? item.supplierName : '-' }}</td>
                <td class="text-center">{{ item.isFixedName }}</td>
                <td class="text-center">{{ item.consultPrice ? '￥' + item.consultPrice : '-' }}</td>
                <td class="text-center">{{ item.originalStock }}{{ item.unitCodeName }}</td>
                <td class="text-center">{{ item.stock ? item.stock : '0' }}{{ item.unitCodeName }}</td>
                <td class="text-center">{{ item.quantity }}{{ item.unitCodeName }}</td>
                <td class="text-center">
                  {{ item.purchaseQuantity ? item.purchaseQuantity + item.unitCodeName : '-' }}
                </td>
              </tr>
              <tr :key="'remark-' + index">
                <th scope="row" class="text-center">{{ $t('purchaseApplyDetail.applyRemark') }}</th>
                <td colspan="4" class="text-center">{{ item.remark }}</td>
                <th class="text-center">{{ $t('purchaseApplyDetail.receiveRemark') }}</th>
                <td colspan="7" class="text-center">{{ item.purchaseRemark ? item.purchaseRemark : '-' }}</td>
              </tr>
            </template>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">
                {{ $t('purchaseApplyDetail.reviewerSignature') }}
              </td>
              <td colspan="4"></td>
              <td class="text-center" style="vertical-align:middle;">
                {{ $t('purchaseApplyDetail.time') }}
              </td>
              <td colspan="6"></td>
            </tr>
            <tr height="60px">
              <td colspan="2" class="text-center" style="vertical-align:middle;">
                {{ $t('purchaseApplyDetail.applicantSignature') }}
              </td>
              <td colspan="4"></td>
              <td class="text-center" style="vertical-align:middle;">
                {{ $t('purchaseApplyDetail.time') }}
              </td>
              <td colspan="6"></td>
            </tr>
          </tbody>
        </table>
  
        <!-- 操作按钮 -->
        <div id="print-btn" class="button-wrapper">
          <el-button type="warning" @click="closePage" style="margin-right:20px;">
            {{ $t('purchaseApplyDetail.cancel') }}
          </el-button>
          <el-button type="primary" @click="printPurchaseOutApplyDiv">
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
    name: 'PrintPurchaseOutApply',
    i18n: {
      messages
    },
    data() {
      return {
        printPurchaseOutApplyInfo: {
          applyOrderId: '',
          userName: '',
          endUserTel: '',
          createTime: '',
          purchaseApplyDetailVo: []
        },
        printFlag: '0'
      }
    },
    created() {
      this.initPrintPurchaseOutApplyDateInfo()
    },
    methods: {
      // 初始化采购领用申请数据
      initPrintPurchaseOutApplyDateInfo() {
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
            this.printPurchaseOutApplyInfo = res.purchaseApplys[0]
          } else {
            this.$message.error('未找到领用申请信息')
          }
        }).catch(error => {
          console.error('获取领用申请信息失败:', error)
          this.$message.error('获取领用申请信息失败')
        })
      },
  
      // 打印
      printPurchaseOutApplyDiv() {
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
  .print-purchase-out-apply {
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
  
  .col-sm-3 {
    flex: 1;
    padding: 0 10px;
  }
  
  .col-sm-3 span {
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
    font-size: 12px;
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
    .print-purchase-out-apply {
      padding: 0;
    }
  
    #print-btn {
      display: none !important;
    }
  
    .table-bordered th,
    .table-bordered td {
      page-break-inside: avoid;
      font-size: 10px;
    }
  }
  </style>
  
  