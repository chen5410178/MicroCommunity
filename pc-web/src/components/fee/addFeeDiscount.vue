<template>
  <el-dialog :title="$t('feeDiscountManage.add.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="addFeeDiscountInfo" label-width="120px" :rules="rules">
      <el-form-item :label="$t('feeDiscountManage.add.discountName')" prop="discountName">
        <el-input v-model.trim="addFeeDiscountInfo.discountName"
          :placeholder="$t('feeDiscountManage.add.discountNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.add.discountType')" prop="discountType">
        <el-select v-model="addFeeDiscountInfo.discountType"
          :placeholder="$t('feeDiscountManage.add.discountTypePlaceholder')" style="width:100%"
          @change="_changeAddFeeDiscountType">
          <el-option v-for="item in addFeeDiscountInfo.discountTypes" :key="item.statusCd" :label="item.name"
            :value="item.statusCd" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.add.rule')" prop="ruleId">
        <el-select v-model="addFeeDiscountInfo.ruleId" :placeholder="$t('feeDiscountManage.add.rulePlaceholder')"
          style="width:100%" @change="_changeAddFeeDiscountRule">
          <el-option v-for="item in addFeeDiscountInfo.rules" :key="item.ruleId" :label="item.ruleName"
            :value="item.ruleId" />
        </el-select>
      </el-form-item>
      <el-form-item v-for="(item, index) in addFeeDiscountInfo.feeDiscountRuleSpecs" :key="index" :label="item.specName"
        :prop="'feeDiscountRuleSpecs.' + index + '.specValue'" :rules="{
          required: true,
          message: $t('feeDiscountManage.add.specValueRequired', { specName: item.specName }),
          trigger: 'blur'
        }">
        <el-input v-model.trim="item.specValue" type="number" :placeholder="item.remark" />
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.add.discountDesc')">
        <el-input v-model.trim="addFeeDiscountInfo.discountDesc" type="textarea"
          :placeholder="$t('feeDiscountManage.add.discountDescPlaceholder')" :rows="3" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveFeeDiscountInfo">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { saveFeeDiscount, queryFeeDiscountRule } from '@/api/fee/feeDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddFeeDiscount',
  data() {
    return {
      visible: false,
      addFeeDiscountInfo: {
        discountName: '',
        discountType: '',
        discountTypes: [],
        ruleId: '',
        discountDesc: '',
        rules: [],
        feeDiscountRuleSpecs: [],
        communityId: ''
      },
      rules: {
        discountName: [
          { required: true, message: this.$t('feeDiscountManage.add.discountNameRequired'), trigger: 'blur' },
          { max: 256, message: this.$t('feeDiscountManage.add.discountNameMaxLength'), trigger: 'blur' }
        ],
        discountType: [
          { required: true, message: this.$t('feeDiscountManage.add.discountTypeRequired'), trigger: 'change' }
        ],
        ruleId: [
          { required: true, message: this.$t('feeDiscountManage.add.ruleRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getCommunityId()
      this.getDictData()
    },
    async getCommunityId() {
      try {
        const communityId = await getCommunityId()
        this.addFeeDiscountInfo.communityId = communityId
      } catch (error) {
        console.error('获取communityId失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('fee_discount', 'discount_type')
        this.addFeeDiscountInfo.discountTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _loadAddFeeDiscountRules() {
      if (!this.addFeeDiscountInfo.discountType) return

      try {
        const params = {
          page: 1,
          row: 100,
          discountType: this.addFeeDiscountInfo.discountType
        }
        const { data } = await queryFeeDiscountRule(params)
        this.addFeeDiscountInfo.rules = data
      } catch (error) {
        console.error('获取规则列表失败:', error)
      }
    },
    _changeAddFeeDiscountRule() {
      const selectedRule = this.addFeeDiscountInfo.rules.find(
        item => item.ruleId === this.addFeeDiscountInfo.ruleId
      )
      if (selectedRule) {
        this.addFeeDiscountInfo.feeDiscountRuleSpecs = selectedRule.feeDiscountRuleSpecs.map(item => ({
          ...item,
          specValue: ''
        }))
      }
    },
    _changeAddFeeDiscountType() {
      this.addFeeDiscountInfo.ruleId = ''
      this.addFeeDiscountInfo.feeDiscountRuleSpecs = []
      this._loadAddFeeDiscountRules()
    },
    async saveFeeDiscountInfo() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          await saveFeeDiscount(this.addFeeDiscountInfo)
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
          this.resetForm()
        } catch (error) {
          console.error('保存折扣信息失败:', error)
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.addFeeDiscountInfo = {
        discountName: '',
        discountType: '',
        discountTypes: this.addFeeDiscountInfo.discountTypes,
        ruleId: '',
        discountDesc: '',
        rules: [],
        feeDiscountRuleSpecs: [],
        communityId: this.addFeeDiscountInfo.communityId
      }
    },
    handleClose() {
      this.resetForm()
    }
  }
}
</script>