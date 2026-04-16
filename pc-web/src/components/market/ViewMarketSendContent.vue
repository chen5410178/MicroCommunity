<template>
  <el-dialog
    :title="$t('marketLog.viewContent.title')"
    :visible.sync="visible"
    width="70%"
  >
    <el-form label-width="120px">
      <el-form-item :label="$t('marketLog.viewContent.sendContent')">
        <el-input
          v-model="form.sendContent"
          type="textarea"
          :rows="8"
          readonly
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.close') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ViewMarketSendContent',
  data() {
    return {
      visible: false,
      form: {
        sendContent: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form.sendContent = data.sendContent || ''
      this.visible = true
    }
  }
}
</script>