<template>
  <el-dialog :title="$t('marketRule.associateCommunity')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-form-item :label="$t('marketRule.communityName')" prop="communityId">
        <el-select v-model="form.communityId" :placeholder="$t('marketRule.selectCommunity')" style="width: 100%;" filterable>
          <el-option v-for="item in communitys" :key="item.communityId" :label="item.name" :value="item.communityId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('marketRule.remark')" prop="remark">
        <el-input type="textarea" v-model="form.remark" :placeholder="$t('marketRule.optional')" rows="4"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('marketRule.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import {
  saveMarketRuleCommunity,
  listCommunitys
} from '@/api/market/marketRuleApi'

export default {
  name: 'AddMarketRuleCommunity',
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
        ruleId: '',
        communityId: '',
        remark: ''
      },
      communitys: [],
      rules: {
        communityId: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'change' }
        ],
        remark: [
          { max: 512, message: this.$t('marketRule.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.form.ruleId = this.ruleId
      this.visible = true
      this.loadCommunitys()
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        ruleId: this.ruleId,
        communityId: '',
        remark: ''
      }
    },
    async loadCommunitys() {
      try {
        const { communitys } = await listCommunitys({ page: 1, row: 500 })
        this.communitys = communitys
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMarketRuleCommunity(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>