<template>
  <el-dialog
    :title="$t('wirteInvoiceEvent.title')"
    :visible.sync="visible"
    width="50%"
    @close="closeDialog"
  >
    <el-form label-width="120px">
      <el-form-item :label="$t('wirteInvoiceEvent.type')">
        <el-select
          v-model="form.eventType"
          :placeholder="$t('wirteInvoiceEvent.typeRequired')"
          style="width: 100%"
        >
          <el-option
            :label="$t('wirteInvoiceEvent.receive')"
            value="4004"
          />
          <el-option
            :label="$t('wirteInvoiceEvent.register')"
            value="5005"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('wirteInvoiceEvent.remark')">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('wirteInvoiceEvent.remarkRequired')"
        />
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button
        type="primary"
        @click="saveEvent"
      >{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { writeInvoiceEvent } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WirteInvoiceEvent',
  data() {
    return {
      visible: false,
      form: {
        applyId: '',
        eventType: '',
        remark: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        applyId: row.applyId,
        eventType: '',
        remark: ''
      }
      this.visible = true
    },
    closeDialog() {
      this.visible = false
    },
    async saveEvent() {
      if (!this.form.eventType) {
        this.$message.warning(this.$t('wirteInvoiceEvent.typeRequired'))
        return
      }
      if (!this.form.remark) {
        this.$message.warning(this.$t('wirteInvoiceEvent.remarkRequired'))
        return
      }
      
      try {
        const params = {
          ...this.form,
          communityId: getCommunityId()
        }
        await writeInvoiceEvent(params)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.closeDialog()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>