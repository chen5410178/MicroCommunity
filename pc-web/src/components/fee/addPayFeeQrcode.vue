<template>
  <el-dialog
    :title="$t('payFeeQrcode.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('payFeeQrcode.form.qrcodeName')"
        prop="qrcodeName"
      >
        <el-input
          v-model="form.qrcodeName"
          :placeholder="$t('payFeeQrcode.placeholder.qrcodeName')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('payFeeQrcode.form.queryWay')"
        prop="queryWay"
      >
        <el-select
          v-model="form.queryWay"
          :placeholder="$t('payFeeQrcode.placeholder.queryWay')"
          style="width:100%"
        >
          <el-option
            v-for="item in queryWayOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('payFeeQrcode.form.smsValidate')"
        prop="smsValidate"
      >
        <el-select
          v-model="form.smsValidate"
          :placeholder="$t('payFeeQrcode.placeholder.smsValidate')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="ON"
          />
          <el-option
            :label="$t('common.no')"
            value="OFF"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('payFeeQrcode.form.customFee')"
        prop="customFee"
      >
        <el-select
          v-model="form.customFee"
          :placeholder="$t('payFeeQrcode.placeholder.customFee')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="ON"
          />
          <el-option
            :label="$t('common.no')"
            value="OFF"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('payFeeQrcode.form.preFee')"
        prop="preFee"
      >
        <el-select
          v-model="form.preFee"
          :placeholder="$t('payFeeQrcode.placeholder.preFee')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="ON"
          />
          <el-option
            :label="$t('common.no')"
            value="OFF"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('payFeeQrcode.form.content')"
        prop="content"
      >
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="3"
          :placeholder="$t('payFeeQrcode.placeholder.content')"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="handleSubmit"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { savePayFeeQrcode } from '@/api/fee/payFeeQrcodeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddPayFeeQrcode',
  data() {
    return {
      visible: false,
      form: {
        qrcodeName: '',
        queryWay: '',
        smsValidate: 'OFF',
        customFee: 'OFF',
        preFee: 'OFF',
        content: '',
        state: 'ON',
        feeType: 'OWNER',
        communityId: ''
      },
      rules: {
        qrcodeName: [
          { required: true, message: this.$t('payFeeQrcode.rules.qrcodeName'), trigger: 'blur' },
          { max: 128, message: this.$t('payFeeQrcode.rules.qrcodeNameMax'), trigger: 'blur' }
        ],
        queryWay: [
          { required: true, message: this.$t('payFeeQrcode.rules.queryWay'), trigger: 'change' }
        ],
        smsValidate: [
          { required: true, message: this.$t('payFeeQrcode.rules.smsValidate'), trigger: 'change' }
        ],
        customFee: [
          { required: true, message: this.$t('payFeeQrcode.rules.customFee'), trigger: 'change' }
        ],
        preFee: [
          { required: true, message: this.$t('payFeeQrcode.rules.preFee'), trigger: 'change' }
        ],
        content: [
          { required: true, message: this.$t('payFeeQrcode.rules.content'), trigger: 'blur' }
        ]
      },
      queryWayOptions: [
        { value: '1001', label: this.$t('payFeeQrcode.queryWay.byPhone') },
        { value: '2002', label: this.$t('payFeeQrcode.queryWay.byHouse') },
        { value: '3003', label: this.$t('payFeeQrcode.queryWay.byBoth') }
      ]
    }
  },
  methods: {
    open() {
      this.visible = true
      this.form.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await savePayFeeQrcode(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('payFeeQrcode.message.addFailed'))
          }
        }
      })
    }
  }
}
</script>