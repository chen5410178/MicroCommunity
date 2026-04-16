<template>
  <el-dialog
    :title="$t('viewUnItemLog.title')"
    :visible.sync="visible"
    width="70%"
  >
    <el-form label-width="120px">
      <el-form-item :label="$t('viewUnItemLog.serviceName')">
        <el-input v-model="viewUnItemLogInfo.serviceName" disabled />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('viewUnItemLog.action')">
            <el-input v-model="viewUnItemLogInfo.action" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('viewUnItemLog.actionObj')">
            <el-input v-model="viewUnItemLogInfo.actionObj" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="$t('viewUnItemLog.preValue')">
        <el-input
          v-model="viewUnItemLogInfo.preValue"
          type="textarea"
          :rows="5"
          disabled
        />
      </el-form-item>
      <el-form-item :label="$t('viewUnItemLog.afterValue')">
        <el-input
          v-model="viewUnItemLogInfo.afterValue"
          type="textarea"
          :rows="5"
          disabled
        />
      </el-form-item>
      <el-form-item :label="$t('viewUnItemLog.logText')">
        <el-input
          v-model="viewUnItemLogInfo.logText"
          type="textarea"
          :rows="5"
          disabled
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="warning" @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listUnitemLog } from '@/api/admin/orderManageApi'

export default {
  name: 'ViewUnItemLog',
  data() {
    return {
      visible: false,
      viewUnItemLogInfo: {
        bId: '',
        serviceName: '',
        logText: '',
        preValue: '',
        afterValue: '',
        action: '',
        actionObj: ''
      }
    }
  },
  methods: {
    open(order) {
      this.clearLog()
      this.viewUnItemLogInfo.bId = order.bId
      
      if (order.action === 'ADD') {
        this.viewUnItemLogInfo.action = this.$t('viewUnItemLog.add')
      } else if (order.action === 'MOD') {
        this.viewUnItemLogInfo.action = this.$t('viewUnItemLog.modify')
      } else if (order.action === 'DEL') {
        this.viewUnItemLogInfo.action = this.$t('viewUnItemLog.delete')
      } else {
        this.viewUnItemLogInfo.action = '-'
      }
      
      this.viewUnItemLogInfo.actionObj = order.actionObj
      this.visible = true
      this._loadOrderUnitemLog()
    },
    async _loadOrderUnitemLog() {
      try {
        const res = await listUnitemLog({ bId: this.viewUnItemLogInfo.bId })
        Object.assign(this.viewUnItemLogInfo, res.data)

        const logText = JSON.parse(res.data.logText)
        this.viewUnItemLogInfo.preValue = JSON.stringify(logText.preValue, null, 2)
        this.viewUnItemLogInfo.afterValue = JSON.stringify(logText.afterValue, null, 2)
      } catch (error) {
        this.$message.error(this.$t('viewUnItemLog.fetchError'))
      }
    },
    clearLog() {
      this.viewUnItemLogInfo = {
        bId: '',
        serviceName: '',
        logText: '',
        preValue: '',
        afterValue: '',
        action: '',
        actionObj: ''
      }
    }
  }
}
</script>