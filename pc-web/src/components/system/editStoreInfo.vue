<template>
  <el-dialog :title="$t('editStoreInfo.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('editStoreInfo.storeName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('editStoreInfo.storeNamePlaceholder')" disabled />
      </el-form-item>
      <el-form-item :label="$t('editStoreInfo.storeAddress')" prop="address">
        <el-input v-model="form.address" :placeholder="$t('editStoreInfo.storeAddressPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editStoreInfo.contactNumber')" prop="tel">
        <el-input v-model="form.tel" :placeholder="$t('editStoreInfo.contactNumberPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editStoreInfo.nearbyLandmark')" prop="nearByLandmarks">
        <el-input v-model="form.nearByLandmarks" :placeholder="$t('editStoreInfo.nearbyLandmarkPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editStoreInfo.mapX')" prop="mapX">
        <el-input v-model="form.mapX" :placeholder="$t('editStoreInfo.mapXPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editStoreInfo.mapY')" prop="mapY">
        <el-input v-model="form.mapY" :placeholder="$t('editStoreInfo.mapYPlaceholder')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateStoreInfo } from '@/api/system/storeInfoManageApi'

export default {
  name: 'EditStoreInfo',
  data() {
    return {
      visible: false,
      form: {
        storeId: '',
        name: '',
        address: '',
        tel: '',
        nearByLandmarks: '',
        mapX: '',
        mapY: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('editStoreInfo.validate.nameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('editStoreInfo.validate.nameMaxLength'), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('editStoreInfo.validate.addressRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('editStoreInfo.validate.addressMaxLength'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('editStoreInfo.validate.telRequired'), trigger: 'blur' },
          { max: 11, message: this.$t('editStoreInfo.validate.telMaxLength'), trigger: 'blur' }
        ],
        nearByLandmarks: [
          { required: true, message: this.$t('editStoreInfo.validate.landmarkRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('editStoreInfo.validate.landmarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(storeInfo) {
      this.resetForm()
      this.form = {
        ...this.form,
        ...storeInfo
      }
      this.visible = true
    },
    resetForm() {
      this.form = {
        storeId: '',
        name: '',
        address: '',
        tel: '',
        nearByLandmarks: '',
        mapX: '',
        mapY: ''
      }
    },
    handleClose() {
      this.resetForm()
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateStoreInfo(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.saveFailed'))
          }
        }
      })
    }
  }
}
</script>