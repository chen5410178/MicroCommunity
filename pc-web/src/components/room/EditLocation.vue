<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('shops.editLocation')"
    width="500px"
    @close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item :label="$t('shops.locationName')" prop="locationName">
        <el-input v-model="form.locationName"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.parentLocation')">
        <el-input :value="parentName" readonly></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.seq')" prop="seq">
        <el-input-number v-model="form.seq" :min="1" :max="100"></el-input-number>
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
import { updateShopLocation } from '@/api/room/shopLocationApi'

export default {
  name: 'EditLocation',
  props: {
    parentName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        locationId: '',
        locationName: '',
        parentId: '-1',
        seq: 1,
        remark: ''
      },
      rules: {
        locationName: [{ required: true, message: this.$t('shops.locationNameRequired'), trigger: 'blur' }]
      }
    }
  },
  methods: {
    open(locationData) {
      this.form = {
        locationId: locationData.locationId,
        locationName: locationData.locationName,
        parentId: locationData.parentId,
        seq: locationData.seq || 1,
        remark: locationData.remark || ''
      }
      this.visible = true
    },
    handleClose() {
      this.visible = false
      if (this.$refs.formRef) this.$refs.formRef.resetFields()
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          await updateShopLocation(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          console.error(error)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>