<template>
  <el-dialog :title="$t('feeDiscountManage.edit.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="editFeeDiscountInfo" label-width="120px" :rules="rules">
      <el-form-item :label="$t('feeDiscountManage.edit.discountName')" prop="discountName">
        <el-input v-model.trim="editFeeDiscountInfo.discountName"
          :placeholder="$t('feeDiscountManage.edit.discountNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.edit.discountType')" prop="discountType">
        <el-select v-model="editFeeDiscountInfo.discountType"
          :placeholder="$t('feeDiscountManage.edit.discountTypePlaceholder')" style="width:100%"
          @change="_changeEditFeeDiscountType">
          <el-option v-for="item in editFeeDiscountInfo.discountTypes" :key="item.statusCd" :label="item.name"
            :value="item.statusCd" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.edit.rule')" prop="ruleId">
        <el-select v-model="editFeeDiscountInfo.ruleId" :placeholder="$t('feeDiscountManage.edit.rulePlaceholder')"
          style="width:100%" @change="_changeEditFeeDiscountRule">
          <el-option v-for="item in editFeeDiscountInfo.rules" :key="item.ruleId" :label="item.ruleName"
            :value="item.ruleId" />
        </el-select>
      </el-form-item>
      <el-form-item v-for="(item, index) in editFeeDiscountInfo.feeDiscountRuleSpecs" :key="index" :label="item.specName"
        :prop="'feeDiscountRuleSpecs.' + index + '.specValue'" :rules="{
          required: true,
          message: $t('feeDiscountManage.edit.specValueRequired', { specName: item.specName }),
          trigger: 'blur'
        }">
        <el-input v-model.trim="item.specValue" type="number" :placeholder="item.remark" />
      </el-form-item>
      <el-form-item :label="$t('feeDiscountManage.edit.discountDesc')">
        <el-input v-model.trim="editFeeDiscountInfo.discountDesc" type="textarea"
          :placeholder="$t('feeDiscountManage.edit.discountDescPlaceholder')" :rows="3" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="editFeeDiscount">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { updateFeeDiscount, queryFeeDiscountRule } from '@/api/fee/feeDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditFeeDiscount',
  data() {
    return {
      visible: false,
      editFeeDiscountInfo: {
        discountId: '',
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
          { required: true, message: this.$t('feeDiscountManage.edit.discountNameRequired'), trigger: 'blur' },
          { max: 256, message: this.$t('feeDiscountManage.edit.discountNameMaxLength'), trigger: 'blur' }
        ],
        discountType: [
          { required: true, message: this.$t('feeDiscountManage.edit.discountTypeRequired'), trigger: 'change' }
        ],
        ruleId: [
          { required: true, message: this.$t('feeDiscountManage.edit.ruleRequired'), trigger: 'change' }
        ],
        discountId: [
          { required: true, message: this.$t('feeDiscountManage.edit.discountIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.getCommunityId()
      this.getDictData()
      this.$nextTick(() => {
        this.editFeeDiscountInfo = {
          ...row,
          discountTypes: this.editFeeDiscountInfo.discountTypes,
          rules: this.editFeeDiscountInfo.rules,
          feeDiscountRuleSpecs: row.feeDiscountSpecs || [],
          communityId: this.editFeeDiscountInfo.communityId
        }
              this._loadEditFeeDiscountRules()

      })
    },
    async getCommunityId() {
      try {
        const communityId = await getCommunityId()
        this.editFeeDiscountInfo.communityId = communityId
      } catch (error) {
        console.error('获取communityId失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('fee_discount', 'discount_type')
        this.editFeeDiscountInfo.discountTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _loadEditFeeDiscountRules() {
      if (!this.editFeeDiscountInfo.discountType) return

      try {
        const params = {
          page: 1,
          row: 100,
          discountType: this.editFeeDiscountInfo.discountType
        }
        const { data } = await queryFeeDiscountRule(params)
        this.editFeeDiscountInfo.rules = data
      } catch (error) {
        console.error('获取规则列表失败:', error)
      }
    },
    _changeEditFeeDiscountRule() {
      const selectedRule = this.editFeeDiscountInfo.rules.find(
        item => item.ruleId === this.editFeeDiscountInfo.ruleId
      )
      if (selectedRule) {
        this.editFeeDiscountInfo.feeDiscountRuleSpecs = selectedRule.feeDiscountRuleSpecs.map(item => ({
          ...item,
          specValue: ''
        }))
      }
    },
    _changeEditFeeDiscountType() {
      this.editFeeDiscountInfo.ruleId = ''
      this.editFeeDiscountInfo.feeDiscountRuleSpecs = []
      this._loadEditFeeDiscountRules()
    },
    async editFeeDiscount() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          await updateFeeDiscount(this.editFeeDiscountInfo)
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
          this.resetForm()
        } catch (error) {
          console.error('更新折扣信息失败:', error)
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.editFeeDiscountInfo = {
        discountId: '',
        discountName: '',
        discountType: '',
        discountTypes: this.editFeeDiscountInfo.discountTypes,
        ruleId: '',
        discountDesc: '',
        rules: [],
        feeDiscountRuleSpecs: [],
        communityId: this.editFeeDiscountInfo.communityId
      }
    },
    handleClose() {
      this.resetForm()
    }
  }
}
</script>