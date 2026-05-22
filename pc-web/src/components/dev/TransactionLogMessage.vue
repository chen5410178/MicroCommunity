<template>
  <el-dialog
    :title="$t('transactionLogMessage.title')"
    :visible.sync="visible"
    width="80%"
    top="5vh"
  >
    <el-form label-position="left" label-width="120px">
      <el-form-item :label="$t('transactionLogMessage.requestHeader')">
        <el-input
          v-model="form.requestHeader"
          type="textarea"
          :rows="3"
          readonly
        />
      </el-form-item>
      <el-form-item :label="$t('transactionLogMessage.requestMessage')">
        <el-input
          v-model="form.requestMessage"
          type="textarea"
          :rows="4"
          readonly
        />
      </el-form-item>
      <el-form-item :label="$t('transactionLogMessage.responseHeader')">
        <el-input
          v-model="form.responseHeader"
          type="textarea"
          :rows="3"
          readonly
        />
      </el-form-item>
      <el-form-item :label="$t('transactionLogMessage.responseMessage')">
        <el-input
          v-model="form.responseMessage"
          type="textarea"
          :rows="4"
          readonly
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
import { queryTransactionLogMessage } from '@/api/dev/transactionLogManageApi'

export default {
  name: 'TransactionLogMessage',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    logId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      form: {
        requestHeader: '',
        requestMessage: '',
        responseHeader: '',
        responseMessage: ''
      }
    }
  },
  watch: {
    logId(newVal) {
      if (newVal) {
        this.getLogMessage(newVal)
      }
    }
  },
  methods: {
    async getLogMessage(logId) {
      try {
        const { data } = await queryTransactionLogMessage({ logId })
        if (data && data.length > 0) {
          this.form = data[0]
        } else {
          this.resetForm()
        }
      } catch (error) {
        this.$message.error(this.$t('transactionLogMessage.fetchError'))
        this.resetForm()
      }
    },
    resetForm() {
      this.form = {
        requestHeader: '',
        requestMessage: '',
        responseHeader: '',
        responseMessage: ''
      }
    }
  }
}
</script>
