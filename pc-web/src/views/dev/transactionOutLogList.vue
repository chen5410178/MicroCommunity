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
                <el-table-column :label="$t('transactionOutLog.requestHeader')" align="center" width="160"
                  show-overflow-tooltip>
                  <template slot-scope="scope">
                    <div class="log-preview">{{ scope.row.requestHeader }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('transactionOutLog.requestMessage')" align="center" width="160"
                  show-overflow-tooltip>
                  <template slot-scope="scope">
                    <div class="log-preview">{{ scope.row.requestMessage }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('transactionOutLog.responseHeader')" align="center" width="160"
                  show-overflow-tooltip>
                  <template slot-scope="scope">
                    <div class="log-preview">{{ scope.row.responseHeader }}</div>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('transactionOutLog.responseMessage')" align="center" width="160"
                  show-overflow-tooltip>
                  <template slot-scope="scope">
                    <div class="log-preview">{{ scope.row.responseMessage }}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="costTime" :label="$t('transactionOutLog.costTime')" align="center" width="120" />
                <el-table-column prop="state" :label="$t('transactionOutLog.status')" align="center" width="100">
                  <template slot-scope="scope">
                    {{ scope.row.state === 'F' ? $t('transactionOutLog.fail') : $t('transactionOutLog.success') }}
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" :label="$t('transactionOutLog.callTime')" align="center" width="160" />
                <el-table-column :label="$t('common.operation')" align="center" width="100">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="handleView(scope.row)">
                      {{ $t('transactionOutLog.view') }}
                    </el-button>
                  </template>
                </el-table-column>
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

    <el-dialog :visible.sync="detailVisible" :title="$t('transactionOutLog.detailTitle')" width="800px" top="5vh">
      <div class="log-base-info">
        <div class="log-base-title">{{ $t('transactionOutLog.baseInfo') }}</div>
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.serial') }}：</span>
              <span>{{ detailData.logId }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.requestMethod') }}：</span>
              <span>{{ detailData.requestMethod }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="24">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.address') }}：</span>
              <span class="log-base-url">{{ detailData.requestUrl }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.costTime') }}：</span>
              <span>{{ detailData.costTime }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.status') }}：</span>
              <el-tag :type="detailData.state === 'F' ? 'danger' : 'success'" size="small">
                {{ detailData.state === 'F' ? $t('transactionOutLog.fail') : $t('transactionOutLog.success') }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="log-base-item">
              <span class="log-base-label">{{ $t('transactionOutLog.callTime') }}：</span>
              <span>{{ detailData.createTime }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <div v-for="(block, index) in detailBlocks" :key="index" class="log-detail-block">
        <div class="log-detail-title">
          <span>{{ block.label }}</span>
          <el-button type="text" size="small" @click="copyMessage(block.content)">
            {{ $t('transactionOutLog.copy') }}
          </el-button>
        </div>
        <pre class="log-detail-content">{{ formatMessage(block.content) }}</pre>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryTransactionOutLog } from '@/api/dev/transactionOutLogApi'

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
      },
      detailVisible: false,
      detailData: {}
    }
  },
  computed: {
    detailBlocks() {
      if (!this.detailData) {
        return []
      }
      return [
        { label: this.$t('transactionOutLog.requestHeader'), content: this.detailData.requestHeader },
        { label: this.$t('transactionOutLog.requestMessage'), content: this.detailData.requestMessage },
        { label: this.$t('transactionOutLog.responseHeader'), content: this.detailData.responseHeader },
        { label: this.$t('transactionOutLog.responseMessage'), content: this.detailData.responseMessage }
      ]
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
    },
    handleView(row) {
      this.detailData = row
      this.detailVisible = true
    },
    formatMessage(msg) {
      if (!msg) {
        return ''
      }
      try {
        const parsed = JSON.parse(msg)
        if (typeof parsed === 'object') {
          return JSON.stringify(parsed, null, 2)
        }
        return msg
      } catch (e) {
        return msg
      }
    },
    copyMessage(content) {
      const text = content || ''
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => {
          this.$message.success(this.$t('transactionOutLog.copySuccess'))
        }).catch(() => {
          this.fallbackCopy(text)
        })
      } else {
        this.fallbackCopy(text)
      }
    },
    fallbackCopy(text) {
      const textarea = document.createElement('textarea')
      textarea.value = text
      textarea.style.position = 'fixed'
      textarea.style.opacity = '0'
      document.body.appendChild(textarea)
      textarea.select()
      try {
        document.execCommand('copy')
        this.$message.success(this.$t('transactionOutLog.copySuccess'))
      } catch (e) {
        this.$message.error(this.$t('transactionOutLog.copyFail'))
      } finally {
        document.body.removeChild(textarea)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.transaction-out-log-container {
  padding: 20px;

  ::v-deep .el-dialog__body {
    text-align: left;
  }

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

  .log-preview {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .log-base-info {
    background: #f5f7fa;
    border-radius: 4px;
    padding: 10px 15px;
    margin-bottom: 15px;

    .log-base-title {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 6px;
    }

    .log-base-item {
      line-height: 28px;
      font-size: 13px;
    }

    .log-base-label {
      color: #909399;
    }

    .log-base-url {
      word-break: break-all;
    }
  }

  .log-detail-block {
    margin-bottom: 15px;

    .log-detail-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 12px;
      background: #f0f2f5;
      border-radius: 4px 4px 0 0;
      font-size: 14px;
      font-weight: 600;
    }

    .log-detail-content {
      max-height: 240px;
      overflow: auto;
      white-space: pre-wrap;
      word-break: break-all;
      margin: 0;
      padding: 10px 12px;
      background: #fafafa;
      border: 1px solid #ebeef5;
      border-top: none;
      border-radius: 0 0 4px 4px;
      font-size: 12px;
      line-height: 1.6;
      font-family: Consolas, Monaco, 'Courier New', monospace;
    }
  }
}
</style>