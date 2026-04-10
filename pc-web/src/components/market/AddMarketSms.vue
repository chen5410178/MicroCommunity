<template>
  <el-dialog :title="$t('marketSms.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketSms.form.smsName')" prop="smsName">
        <el-input v-model="form.smsName" :placeholder="$t('marketSms.placeholder.smsName')" />
      </el-form-item>
      <el-form-item :label="$t('marketSms.form.smsType')" prop="smsType">
        <el-select v-model="form.smsType" :placeholder="$t('marketSms.placeholder.smsType')" style="width: 100%"
          @change="handleSmsTypeChange">
          <el-option v-for="item in smsTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item>
      <el-form-item v-for="(item, index) in smsTypeValues" :key="index" :label="item.name">
        <el-input v-model="item.smsValue" :placeholder="item.remark" />
      </el-form-item>
      <el-form-item :label="$t('marketSms.form.remark')" prop="remark">
        <el-input v-model="form.remark" :placeholder="$t('marketSms.placeholder.remark')" type="textarea" rows="3" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveMarketSms, listMarketSmsKey } from '@/api/market/marketSmsManageApi'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'AddMarketSms',
  data() {
    return {
      visible: false,
      form: {
        smsName: '',
        smsType: '',
        remark: ''
      },
      smsTypes: [],
      smsTypeValues: [],
      rules: {
        smsName: [
          { required: true, message: this.$t('marketSms.validate.smsName'), trigger: 'blur' },
          { max: 64, message: this.$t('marketSms.validate.smsNameMax'), trigger: 'blur' }
        ],
        smsType: [
          { required: true, message: this.$t('marketSms.validate.smsType'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('marketSms.validate.remark'), trigger: 'blur' },
          { max: 512, message: this.$t('marketSms.validate.remarkMax'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('market_sms_key', 'sms_type')
        this.smsTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async handleSmsTypeChange(value) {
      if (!value) return
      try {
        const { data } = await listMarketSmsKey({
          smsType: value,
          page: 1,
          row: 100
        })
        this.smsTypeValues = data
      } catch (error) {
        console.error('获取短信类型值失败:', error)
      }
    },
    open() {
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.smsTypeValues = []
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const formData = {
            ...this.form,
            smsTypeValues: this.smsTypeValues
          }
          await saveMarketSms(formData)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message)
        }
      })
    }
  }
}
</script>