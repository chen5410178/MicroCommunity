<template>
  <el-dialog
    :title="$t('marketText.edit')"
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
        :label="$t('marketText.name')"
        prop="name"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('marketText.name')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('marketText.sendRate')"
        prop="sendRate"
      >
        <el-select
          v-model="form.sendRate"
          :placeholder="$t('marketText.sendRate')"
          style="width: 100%"
        >
          <el-option
            v-for="item in sendRateOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('marketText.content')"
        prop="textContent"
      >
        <el-input
          v-model="form.textContent"
          type="textarea"
          :rows="4"
          :placeholder="$t('marketText.content')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('marketText.config')"
        prop="smsId"
      >
        <el-select
          v-model="form.smsId"
          :placeholder="$t('marketText.config')"
          style="width: 100%"
        >
          <el-option
            v-for="item in marketSmss"
            :key="item.smsId"
            :label="item.smsName"
            :value="item.smsId"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMarketText, listMarketSms } from '@/api/market/marketWayApi'

export default {
  name: 'EditMarketText',
  data() {
    return {
      visible: false,
      form: {
        textId: '',
        name: '',
        sendRate: '',
        textContent: '',
        smsId: ''
      },
      marketSmss: [],
      sendRateOptions: [
        { value: '1001', label: this.$t('marketText.sendRateOptions.daily') },
        { value: '2002', label: this.$t('marketText.sendRateOptions.monthly') },
        { value: '3003', label: this.$t('marketText.sendRateOptions.unlimited') }
      ],
      rules: {
        name: [
          { required: true, message: this.$t('marketText.name') + this.$t('common.required'), trigger: 'blur' },
          { max: 64, message: this.$t('marketText.name') + this.$t('common.maxLength64'), trigger: 'blur' }
        ],
        sendRate: [
          { required: true, message: this.$t('marketText.sendRate') + this.$t('common.required'), trigger: 'change' }
        ],
        textContent: [
          { required: true, message: this.$t('marketText.content') + this.$t('common.required'), trigger: 'blur' },
          { max: 512, message: this.$t('marketText.content') + this.$t('common.maxLength512'), trigger: 'blur' }
        ],
        smsId: [
          { required: true, message: this.$t('marketText.config') + this.$t('common.required'), trigger: 'change' }
        ],
        textId: [
          { required: true, message: 'ID' + this.$t('common.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
      this.getMarketSmss()
    },
    async getMarketSmss() {
      try {
        const { data } = await listMarketSms({ page: 1, row: 100 })
        this.marketSmss = data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMarketText(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        textId: '',
        name: '',
        sendRate: '',
        textContent: '',
        smsId: ''
      }
    }
  }
}
</script>