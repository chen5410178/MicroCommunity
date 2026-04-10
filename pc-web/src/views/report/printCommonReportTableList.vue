<template>
  <div class="print-common-report-table-container">

      <div class="top-1">
        <h1 class="text-center">{{ printCommonReportTableInfo.componentName }}</h1>
      </div>

      <div class="context-1">
        <table class="custom-table">
          <thead>
            <tr>
              <th v-for="(itemTh, indexTh) in printCommonReportTableInfo.th" :key="indexTh" 
                  style="color: #000; font-size: 20px; text-align: center; padding: 12px 8px; border: 1px solid #000;">
                {{ itemTh }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, rowIndex) in printCommonReportTableInfo.data" :key="rowIndex">
              <td v-for="(itemTh, indexTh) in printCommonReportTableInfo.th" :key="indexTh"
                  style="color: #000; font-size: 20px; text-align: center; padding: 12px 8px; border: 1px solid #000;">
                {{ row[itemTh] }}
              </td>
            </tr>
          </tbody>
        </table>

        <el-row v-if="printCommonReportTableInfo.footer" class="padding-left">
          <el-col v-for="(value, key) in printCommonReportTableInfo.footer" :key="key" :span="4">
            <div>{{ key }}: {{ value }}</div>
          </el-col>
        </el-row>

        <div class="text-right" id="print-btn">
          <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv">
            <i class="el-icon-printer"></i>&nbsp;{{ $t('printCommonReportTable.print') }}
          </el-button>
          <el-button type="warning" class="float-right" style="margin-right: 20px;" @click="_closePage">
            {{ $t('printCommonReportTable.cancel') }}
          </el-button>
        </div>
      </div>

  </div>
</template>

<script>
export default {
  name: 'PrintCommonReportTableList',
  data() {
    return {
      printCommonReportTableInfo: {}
    }
  },
  created() {
    this._initData()
  },
  methods: {
    _initData() {
      this.printCommonReportTableInfo = JSON.parse(localStorage.getItem('printCommonReportTableData'))
    },
    _printPurchaseApplyDiv() {
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

<style lang="scss" scoped>
.print-common-report-table-container {
  padding: 20px;

  .top-1 {
    margin-bottom: 20px;

    h1 {
      color: #000;
      font-weight: 400;
    }
  }

  .context-1 {
    .custom-table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
      
      th, td {
        border: 1px solid #000;
        background-color: #fff;
      }
      
      th {
        background-color: #f5f5f5;
        font-weight: bold;
      }
      
      tr:nth-child(even) {
        background-color: #fafafa;
      }
      
      tr:hover {
        background-color: #f0f0f0;
      }
    }

    .padding-left {
      padding-left: 20px;
      margin-top: 20px;
      font-size: 20px;
      color: #000;
    }

    .text-right {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>