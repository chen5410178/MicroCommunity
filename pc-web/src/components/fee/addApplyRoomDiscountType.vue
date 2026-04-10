<template>
  <el-dialog :title="$t('discountType.add.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('discountType.form.typeName')" prop="typeName">
        <el-input v-model="formData.typeName" :placeholder="$t('discountType.form.typeNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('discountType.form.typeDesc')" prop="typeDesc">
        <el-input v-model="formData.typeDesc" type="textarea" :placeholder="$t('discountType.form.typeDescPlaceholder')"
          :rows="3" />
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
import { saveApplyRoomDiscountType } from '@/api/fee/discountTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddApplyRoomDiscountType',
  data() {
    return {
      visible: false,
      formData: {
        typeName: '',
        typeDesc: '',
        communityId: ''
      },
      rules: {
        typeName: [
          { required: true, message: this.$t('discountType.validate.typeNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('discountType.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        typeDesc: [
          { max: 512, message: this.$t('discountType.validate.typeDescMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
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
            this.formData.communityId = getCommunityId()
            await saveApplyRoomDiscountType(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error(error)
          }
        }
      })
    }
  }
}
</script>