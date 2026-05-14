<template>
  <div class="transaction-out-log-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="border-radius treeview attendance-staff">
          <ul class="list-group text-center border-radius">
            <li v-for="(item, index) in logTypes" :key="index" class="list-group-item node-orgTree"
              :class="{ 'vc-node-selected': conditions.logType === item.value }" @click="swatchLogType(item)">
              {{ item.name }}
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('transactionOutLog.queryCondition') }}</span>
              </div>
              <el-row :gutter="20">
                <el-col :span="6">
                  <el-date-picker v-model="conditions.startTime" type="datetime"
                    :placeholder="$t('transactionOutLog.startTime')" class="w-100" />
                </el-col>
                <el-col :span="6">
                  <el-date-picker v-model="conditions.endTime" type="datetime"
                    :placeholder="$t('transactionOutLog.endTime')" class="w-100" />
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" size="small" @click="_queryLogMethod">
                    {{ $t('transactionOutLog.query') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>

        <el-row class="mt-20">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('transactionOutLog.logTitle') }}</span>
              </div>
              <el-table :data="logs" border style="width: 100%">
                <el-table-column prop="logId" :label="$t('transactionOutLog.serial')" align="center" width="200" />
                <el-table-column prop="requestUrl" :label="$t('transactionOutLog.address')" align="center" width="200" />
                <el-table-column prop="requestHeader" :label="$t('transactionOutLog.requestHeader')" align="center"
                  width="200" />
                <el-table-column prop="requestMessage" :label="$t('transactionOutLog.requestMessage')" align="center"
                  width="200" />
                <el-table-column prop="responseHeader" :label="$t('transactionOutLog.responseHeader')" align="center"
                  width="200" />
                <el-table-column prop="responseMessage" :label="$t('transactionOutLog.responseMessage')" align="center"
                  width="200" />
                <el-table-column prop="costTime" :label="$t('transactionOutLog.costTime')" align="center" width="200" />
                <el-table-column prop="state" :label="$t('transactionOutLog.status')" align="center" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.state === 'F' ? $t('transactionOutLog.fail') : $t('transactionOutLog.success') }}
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" :label="$t('transactionOutLog.callTime')" align="center" width="200" />
              </el-table>

              <el-row class="mt-20">
                <el-col :span="12">
                  <div class="tips">
                    <div>{{ $t('transactionOutLog.tip1') }}</div>
                    <div>{{ $t('transactionOutLog.tip2') }}</div>
                  </div>
                </el-col>
                <el-col :span="12" class="text-right">
                  <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                    :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" />
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryTransactionOutLog } from '@/api/log/transactionOutLogApi'

export default {
  name: 'TransactionOutLogList',
  data() {
    return {
      logTypes: [
        { name: this.$t('transactionOutLog.iot'), value: 'IOT' },
        { name: this.$t('transactionOutLog.wechat'), value: 'WECHAT' },
        { name: this.$t('transactionOutLog.wechatPay'), value: 'WECHAT_PAY' },
        { name: this.$t('transactionOutLog.oss'), value: 'OSS' },
        { name: this.$t('transactionOutLog.sms'), value: 'SMS' }
      ],
      conditions: {
        startTime: '',
        endTime: '',
        logType: 'IOT',
        page: 1,
        row: 10
      },
      logs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._listLogs()
  },
  methods: {
    async _listLogs() {
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryTransactionOutLog(params)
        this.logs = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('transactionOutLog.fetchError'))
      }
    },
    swatchLogType(item) {
      this.conditions.logType = item.value
      this._listLogs()
    },
    _queryLogMethod() {
      this.page.current = 1
      this._listLogs()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listLogs()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listLogs()
    }
  }
}
</script>

<style lang="scss" scoped>
.transaction-out-log-container {
  padding: 20px;

  .border-radius {
    border-radius: 4px;
  }

  .treeview {
    background: #fff;
    padding: 10px;

    .list-group-item {
      cursor: pointer;
      padding: 10px;
      margin-bottom: 5px;

      &:hover {
        background-color: #f5f7fa;
      }

      &.vc-node-selected {
        background-color: #409eff;
        color: #fff;
      }
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .tips {
    color: #909399;
    font-size: 12px;
    line-height: 1.5;
  }

  .text-right {
    text-align: right;
  }

  .w-100 {
    width: 100%;
  }
}
</style>