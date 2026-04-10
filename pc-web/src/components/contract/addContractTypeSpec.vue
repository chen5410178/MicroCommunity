<template>
  <el-dialog
    :title="$t('contractTypeSpecManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('contractTypeSpecManage.add.specName')"
        prop="specName"
      >
        <el-input
          v-model="formData.specName"
          :placeholder="$t('contractTypeSpecManage.add.specNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.specHoldplace')"
        prop="specHoldplace"
      >
        <el-input
          v-model="formData.specHoldplace"
          :placeholder="$t('contractTypeSpecManage.add.specHoldplacePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.required')"
        prop="required"
      >
        <el-select
          v-model="formData.required"
          :placeholder="$t('contractTypeSpecManage.add.requiredPlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="Y"
          />
          <el-option
            :label="$t('common.no')"
            value="N"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.specShow')"
        prop="specShow"
      >
        <el-select
          v-model="formData.specShow"
          :placeholder="$t('contractTypeSpecManage.add.specShowPlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="Y"
          />
          <el-option
            :label="$t('common.no')"
            value="N"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.specValueType')"
        prop="specValueType"
      >
        <el-select
          v-model="formData.specValueType"
          :placeholder="$t('contractTypeSpecManage.add.specValueTypePlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('contractTypeSpecManage.specValueType.string')"
            value="1001"
          />
          <el-option
            :label="$t('contractTypeSpecManage.specValueType.integer')"
            value="2002"
          />
          <el-option
            :label="$t('contractTypeSpecManage.specValueType.amount')"
            value="3003"
          />
          <el-option
            :label="$t('contractTypeSpecManage.specValueType.date')"
            value="4004"
          />
          <el-option
            :label="$t('contractTypeSpecManage.specValueType.time')"
            value="5005"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.specType')"
        prop="specType"
      >
        <el-select
          v-model="formData.specType"
          :placeholder="$t('contractTypeSpecManage.add.specTypePlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('contractTypeSpecManage.specType.input')"
            value="2233"
          />
          <el-option
            :label="$t('contractTypeSpecManage.specType.select')"
            value="3344"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('contractTypeSpecManage.add.listShow')"
        prop="listShow"
      >
        <el-select
          v-model="formData.listShow"
          :placeholder="$t('contractTypeSpecManage.add.listShowPlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('common.yes')"
            value="Y"
          />
          <el-option
            :label="$t('common.no')"
            value="N"
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
import { saveContractTypeSpec } from '@/api/contract/contractTypeSpecManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddContractTypeSpec',
  data() {
    return {
      visible: false,
      formData: {
        specName: '',
        specHoldplace: '',
        required: '',
        specShow: '',
        specValueType: '',
        specType: '',
        listShow: '',
        contractTypeId: '',
        communityId: ''
      },
      rules: {
        specName: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.specNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('contractTypeSpecManage.validate.specNameMaxLength'), trigger: 'blur' }
        ],
        specHoldplace: [
          { max: 200, message: this.$t('contractTypeSpecManage.validate.specHoldplaceMaxLength'), trigger: 'blur' }
        ],
        required: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.requiredRequired'), trigger: 'change' }
        ],
        specShow: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.specShowRequired'), trigger: 'change' }
        ],
        specValueType: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.specValueTypeRequired'), trigger: 'change' }
        ],
        specType: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.specTypeRequired'), trigger: 'change' }
        ],
        listShow: [
          { required: true, message: this.$t('contractTypeSpecManage.validate.listShowRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.formData.contractTypeId = data.contractTypeId
      this.formData.communityId = getCommunityId()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveContractTypeSpec(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('contractTypeSpecManage.add.error'))
          }
        }
      })
    }
  }
}
</script>