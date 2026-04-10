<template>
  <el-dialog
    :title="$t('integralConfigManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('integralConfigManage.form.configName')"
        prop="configName"
      >
        <el-input
          v-model="formData.configName"
          :placeholder="$t('integralConfigManage.form.configNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('integralConfigManage.form.computingFormula')"
        prop="computingFormula"
      >
        <el-select
          v-model="formData.computingFormula"
          :placeholder="$t('integralConfigManage.form.computingFormulaPlaceholder')"
          style="width: 100%"
          @change="handleFormulaChange"
        >
          <el-option
            v-for="item in formulaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="formData.computingFormula === '1001'"
        :label="$t('integralConfigManage.form.amount')"
        prop="squarePrice"
      >
        <el-input
          v-model="formData.squarePrice"
          :placeholder="$t('integralConfigManage.form.amountPlaceholder')"
        />
      </el-form-item>

      <el-form-item
        v-else-if="formData.computingFormula === '2002'"
        :label="$t('integralConfigManage.form.month')"
        prop="squarePrice"
      >
        <el-input
          v-model="formData.squarePrice"
          :placeholder="$t('integralConfigManage.form.monthPlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('integralConfigManage.form.points')"
        prop="additionalAmount"
      >
        <el-input
          v-model="formData.additionalAmount"
          type="number"
          :placeholder="$t('integralConfigManage.form.pointsPlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('integralConfigManage.form.scale')"
        prop="scale"
      >
        <el-select
          v-model="formData.scale"
          :placeholder="$t('integralConfigManage.form.scalePlaceholder')"
          style="width: 100%"
        >
          <el-option
            v-for="item in scaleOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateIntegralConfig } from '@/api/scm/integralConfigManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditIntegralConfig',
  data() {
    return {
      visible: false,
      formData: {
        configId: '',
        configName: '',
        computingFormula: '',
        squarePrice: '',
        additionalAmount: '',
        scale: '',
        communityId: ''
      },
      formulaOptions: [
        { value: '1001', label: this.$t('integralConfigManage.formula.fullAmount') },
        { value: '2002', label: this.$t('integralConfigManage.formula.fullMonth') }
      ],
      scaleOptions: [
        { value: '3', label: this.$t('integralConfigManage.scale.roundUp') },
        { value: '4', label: this.$t('integralConfigManage.scale.roundDown') }
      ],
      rules: {
        configId: [
          { required: true, message: this.$t('integralConfigManage.validate.configIdRequired'), trigger: 'blur' }
        ],
        configName: [
          { required: true, message: this.$t('integralConfigManage.validate.configNameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('integralConfigManage.validate.configNameMaxLength'), trigger: 'blur' }
        ],
        computingFormula: [
          { required: true, message: this.$t('integralConfigManage.validate.computingFormulaRequired'), trigger: 'change' }
        ],
        squarePrice: [
          { required: true, message: this.$t('integralConfigManage.validate.squarePriceRequired'), trigger: 'blur' }
        ],
        additionalAmount: [
          { required: true, message: this.$t('integralConfigManage.validate.additionalAmountRequired'), trigger: 'blur' }
        ],
        scale: [
          { required: true, message: this.$t('integralConfigManage.validate.scaleRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData = {
        ...data,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleFormulaChange() {
      // 计算公式变化时清空金额/月份
      this.formData.squarePrice = ''
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateIntegralConfig(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('integralConfigManage.message.editFailed'))
          }
        }
      })
    }
  }
}
</script>