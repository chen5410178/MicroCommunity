<template>
  <el-dialog :title="$t('locationManage.add')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('locationManage.locationName')" prop="locationName" :rules="[
        { required: true, message: $t('locationManage.locationNameRequired') },
        { max: 100, message: $t('locationManage.locationNameMaxLength') }
      ]">
        <el-input v-model="formData.locationName" :placeholder="$t('locationManage.enterLocationName')" />
      </el-form-item>

      <el-form-item :label="$t('locationManage.locationType')" prop="locationType" :rules="[
        { required: true, message: $t('locationManage.locationTypeRequired') }
      ]">
        <el-select v-model="formData.locationType" :placeholder="$t('locationManage.selectLocationType')"
          style="width:100%" @change="handleLocationTypeChange">
          <el-option v-for="item in locationTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
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
import { getCommunityId,getCommunityName } from '@/api/community/communityApi'
import { saveCommunityLocation } from '@/api/community/locationManageApi'


export default {
  name: 'AddLocation',
  components: {
  },
  data() {
    return {
      visible: false,
      formData: {
        locationName: '',
        locationType: '',
        locationObjId: '',
        locationObjName: '',
        communityId: getCommunityId()
      },
      locationTypeOptions: [
        { value: '1000', label: this.$t('locationManage.community') },
      ]
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        locationName: '',
        locationType: '',
        locationObjId: '',
        locationObjName: '',
        communityId: getCommunityId()
      }
    },
    handleLocationTypeChange(val) {
      console.log(val)
      this.formData.locationObjId = ''
      this.formData.locationObjName = ''
    },
    handleFloorChange(data) {
      if (data) {
        this.formData.locationObjId = data.floorId
        this.formData.locationObjName = data.floorNum
      } else {
        this.formData.locationObjId = ''
        this.formData.locationObjName = ''
      }
    },
    handleUnitChange(data) {
      if (data) {
        this.formData.locationObjId = data.unitId
        this.formData.locationObjName = `${data.floorNum}${data.unitNum}`
      } else {
        this.formData.locationObjId = ''
        this.formData.locationObjName = ''
      }
    },
    handleParkingBoxChange(data) {
      if (data) {
        this.formData.locationObjId = data.boxId
        this.formData.locationObjName = data.boxName
      } else {
        this.formData.locationObjId = ''
        this.formData.locationObjName = ''
      }
    },
    handleOrgChange(data) {
      if (data) {
        this.formData.locationObjId = data.orgId
        this.formData.locationObjName = data.allOrgName
      } else {
        this.formData.locationObjId = ''
        this.formData.locationObjName = ''
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        if (this.formData.locationType === '1000') {
          this.formData.locationObjId = getCommunityId()
          this.formData.locationObjName = getCommunityName()
        }

        await saveCommunityLocation(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>