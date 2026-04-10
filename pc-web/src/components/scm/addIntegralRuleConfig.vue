<template>
  <el-dialog :title="$t('integralRule.addConfigTitle')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('integralRule.configName')" prop="configId">
        <el-select v-model="form.configId" :placeholder="$t('integralRule.configPlaceholder')" style="width: 100%">
          <el-option v-for="item in configOptions" :key="item.configId" :label="item.configName"
            :value="item.configId" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveIntegralRuleConfig, listIntegralConfig } from '@/api/scm/integralRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddIntegralRuleConfig',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      form: {
        configId: '',
        ruleId: ''
      },
      rules: {
        configId: [
          { required: true, message: this.$t('integralRule.configRequired'), trigger: 'change' }
        ]
      },
      configOptions: []
    }
  },
  methods: {
    async open() {
      this.visible = true
      this.form.ruleId = this.ruleId
      await this.fetchConfigOptions()
    },
    async fetchConfigOptions() {
      try {
        const { data } = await listIntegralConfig({
          page: 1,
          row: 100,
          communityId: getCommunityId()
        })
        this.configOptions = data
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const {code,msg} = await saveIntegralRuleConfig(this.form)
          if(code != 0){
            this.$message.error(msg)
            return
          }
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('common.addFailed'))
        }
      })
    }
  }
}
</script>