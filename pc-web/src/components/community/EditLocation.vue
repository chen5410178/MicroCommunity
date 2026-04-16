<template>
  <el-dialog :title="$t('locationManage.edit')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('locationManage.locationName')" prop="locationName" :rules="[
        { required: true, message: $t('locationManage.locationNameRequired') },
        { max: 100, message: $t('locationManage.locationNameMaxLength') }
      ]">
        <el-input v-model="formData.locationName" :placeholder="$t('locationManage.enterLocationName')" />
      </el-form-item>

      <el-form-item :label="$t('locationManage.locationType')" prop="locationType">
        <el-select v-model="formData.locationType" :placeholder="$t('locationManage.selectLocationType')"
          style="width:100%" disabled>
          <el-option v-for="item in locationTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('locationManage.locationObject')" prop="locationObjName">
        <el-input v-model="formData.locationObjName" disabled />
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
import { updateCommunityLocation } from '@/api/community/locationManageApi'


export default {
  name: 'EditLocation',
  components: {
  },
  data() {
    return {
      visible: false,
      formData: {
        locationId: '',
        locationName: '',
        locationType: '',
        locationObjId: '',
        locationObjName: '',
        floorId: '',
        unitId: '',
        boxId: '',
        paId: '',
        communityId: getCommunityId()
      },
      locationTypeOptions: [
        { value: '1000', label: this.$t('locationManage.community') },
      ]
    }
  },
  methods: {
    open(row) {
      this.formData = {
        ...row,
        communityId: getCommunityId()
      }

      // Set values for select components
     

      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        locationId: '',
        locationName: '',
        locationType: '',
        locationObjId: '',
        locationObjName: '',
        floorId: '',
        unitId: '',
        boxId: '',
        paId: '',
        communityId: getCommunityId()
      }
    },
    handleFloorChange(data) {
      if (data) {
        this.formData.locationObjId = data.floorId
        this.formData.locationObjName = data.floorNum + this.$t('locationManage.buildingUnit')
      }
    },
    handleUnitChange(data) {
      if (data) {
        this.formData.locationObjId = data.unitId
        this.formData.locationObjName = `${data.floorNum}${data.unitNum}`
      }
    },
    handleParkingBoxChange(data) {
      if (data) {
        this.formData.locationObjId = data.boxId
        this.formData.locationObjName = data.boxName
      }
    },
    handleParkingAreaChange(data) {
      if (data) {
        this.formData.locationObjId = data.paId
        this.formData.locationObjName = data.num + this.$t('locationManage.parkingLot')
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        if (this.formData.locationType === '1000') {
          this.formData.locationObjId = getCommunityId()
          this.formData.locationObjName = getCommunityName()
        }

        await updateCommunityLocation(this.formData)
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