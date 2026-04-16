<template>
    <el-dialog
      :title="$t('closeOrder.closeOrderInfo')"
      :visible.sync="visible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form ref="form" :model="closeOrderInfo" label-width="120px">
        <el-form-item :label="$t('closeOrder.closeStatus')" prop="state" required>
          <el-select
            v-model="closeOrderInfo.state"
            :placeholder="$t('closeOrder.selectCloseStatus')"
            style="width:100%;"
          >
            <el-option
              :label="$t('closeOrder.closeOrder')"
              value="1100"
            />
            <el-option
              :label="$t('closeOrder.returnOrder')"
              value="1200"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="$t('closeOrder.reason')" prop="remark">
          <el-input
            v-model="closeOrderInfo.remark"
            type="textarea"
            :placeholder="$t('closeOrder.reasonPlaceholder')"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="_closeOrderSubmit">{{ $t('common.submit') }}</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  export default {
    name: 'CloseOrder',
    data() {
      return {
        visible: false,
        closeOrderInfo: {
          state: '',
          remark: ''
        }
      }
    },
    watch: {
      'closeOrderInfo.state': {
        handler(val) {
          if (val === '1100') {
            this.closeOrderInfo.remark = this.$t('closeOrder.processed')
          } else {
            this.closeOrderInfo.remark = ''
          }
        },
        deep: true
      }
    },
    methods: {
      open() {
        this.visible = true
        this.resetForm()
      },
      
      handleClose() {
        this.visible = false
        this.resetForm()
      },
      
      resetForm() {
        this.closeOrderInfo = {
          state: '',
          remark: ''
        }
        if (this.$refs.form) {
          this.$refs.form.resetFields()
        }
      },
      
      _closeOrderSubmit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.$emit('success', this.closeOrderInfo)
            this.handleClose()
          }
        })
      }
    }
  }
  </script>
  
  <style scoped>
  .dialog-footer {
    text-align: right;
  }
  </style>