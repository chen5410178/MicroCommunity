<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('shops.editShop')"
    width="600px"
    @close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item :label="$t('shops.location')" prop="locationId">
        <el-input v-model="form.locationName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.shopNumber')" prop="shopNum">
        <el-input v-model="form.shopNum"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.builtUpArea')" prop="builtUpArea">
        <el-input v-model="form.builtUpArea"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.usedArea')" prop="shopArea">
        <el-input v-model="form.shopArea"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.rent')" prop="shopRent">
        <el-input v-model="form.shopRent"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.remark')">
        <el-input type="textarea" v-model="form.remark" rows="3"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateBuildingShop } from '@/api/room/buildingShopApi'

export default {
  name: 'EditShop',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        shopId: '',
        shopNum: '',
        locationId: '',
        locationName: '',
        builtUpArea: '',
        shopArea: '',
        shopRent: '',
        remark: ''
      },
      rules: {
        shopNum: [{ required: true, message: this.$t('shops.shopNumberRequired'), trigger: 'blur' }],
        builtUpArea: [{ required: true, message: this.$t('shops.builtUpAreaRequired'), trigger: 'blur' }],
        shopArea: [{ required: true, message: this.$t('shops.usedAreaRequired'), trigger: 'blur' }],
        shopRent: [{ required: true, message: this.$t('shops.rentRequired'), trigger: 'blur' }]
      }
    }
  },
  methods: {
    open(shopData) {
      this.form.shopId = shopData.shopId
      this.form.shopNum = shopData.shopNum
      this.form.locationId = shopData.locationId
      this.form.locationName = shopData.locationName || ''
      this.form.builtUpArea = shopData.builtUpArea || ''
      this.form.shopArea = shopData.shopArea || ''
      this.form.shopRent = shopData.shopRent || ''
      this.form.remark = shopData.remark || ''
      this.visible = true
    },
    handleClose() {
      this.visible = false
      if (this.$refs.formRef) this.$refs.formRef.resetFields()
    },
    handleSubmit() {
      this.$refs.formRef.validate((valid) => {
        if (!valid) return
        this.loading = true
        updateBuildingShop(this.form)
          .then(() => {
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.handleClose()
          })
          .catch(console.error)
          .finally(() => { this.loading = false })
      })
    }
  }
}
</script>