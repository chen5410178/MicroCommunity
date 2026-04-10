<template>
  <el-dialog :title="$t('marketSms.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketSms.form.smsName')" prop="smsName">
        <el-input v-model="form.smsName" />
      </el-form-item>
      <el-form-item :label="$t('marketSms.form.smsType')" prop="smsType">
        <el-select v-model="form.smsType" style="width: 100%" disabled>
          <el-option v-for="item in smsTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item>
      <el-form-item v-for="(item, index) in form.smsTypeValues" :key="index" :label="item.name">
        <el-input v-model="item.smsValue" :placeholder="item.remark" />
      </el-form-item>
      <el-form-item :label="$t('marketSms.form.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMarketSms } from '@/api/market/marketSmsManageApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditMarketSms',
  data() {
    return {
      visible: false,
      smsTypes: [],
      form: {
        smsId: '',
        smsName: '',
        smsType: '',
        remark: '',
        smsTypeValues: []
      },
      rules: {
        smsName: [
          { required: true, message: this.$t('marketSmsManage.rules.smsName'), trigger: 'blur' },
          { max: 64, message: this.$t('marketSmsManage.rules.smsNameMax'), trigger: 'blur' }
        ],
        smsType: [
          { required: true, message: this.$t('marketSmsManage.rules.smsType'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('marketSmsManage.rules.remark'), trigger: 'blur' },
          { max: 512, message: this.$t('marketSmsManage.rules.remarkMax'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getSmsTypes()
  },
  methods: {
    open(data) {
      this.form = {
        ...data,
        smsTypeValues: data.values || []
      }
      this.visible = true
    },
    async getSmsTypes() {
      try {
        const data = await getDict('market_sms_key', 'sms_type')
        this.smsTypes = data
      } catch (error) {
        console.error('Failed to get sms types:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        try {
          await updateMarketSms(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        } catch (error) {
          this.$message.error(error.message || this.$t('marketSmsManage.message.editFailed'))
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form.smsTypeValues = []
    }
  }
}
</script>