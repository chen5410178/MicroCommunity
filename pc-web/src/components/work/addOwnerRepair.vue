<template>
  <el-dialog :title="$t('addOwnerRepair.title')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('addOwnerRepair.repairScope')" prop="repairObjType" required>
        <el-select v-model="formData.repairObjType" style="width:100%" @change="changeRepairObjType">
          <el-option v-for="item in repairObjTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.repairObjType && formData.repairObjType !== '001'"
        :label="$t('addOwnerRepair.belongBuilding')">
        <el-select v-model="formData.floorId" style="width:100%" @change="handleFloorChange">
          <el-option v-for="item in floors" :key="item.floorId" :label="item.floorNum" :value="item.floorId" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.repairObjType === '003' || formData.repairObjType === '004'"
        :label="$t('addOwnerRepair.belongUnit')">
        <el-select v-model="formData.unitId" style="width:100%" @change="handleUnitChange">
          <el-option v-for="item in units" :key="item.unitId" :label="item.unitNum" :value="item.unitId" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.repairObjType === '004'" :label="$t('addOwnerRepair.belongRoom')">
        <el-select v-model="formData.roomId" style="width:100%" @change="handleRoomChange">
          <el-option v-for="item in rooms" :key="item.roomId" :label="item.roomName" :value="item.roomId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addOwnerRepair.repairType')" prop="repairType" required>
        <el-select v-model="formData.repairType" style="width:100%">
          <el-option v-for="item in repairSettings" :key="item.repairType" :label="item.repairTypeName"
            :value="item.repairType" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addOwnerRepair.repairName')" prop="repairName" required>
        <el-input v-model.trim="formData.repairName" :placeholder="$t('addOwnerRepair.placeholder.repairName')" />
      </el-form-item>

      <el-form-item :label="$t('addOwnerRepair.tel')" prop="tel" required>
        <el-input v-model.trim="formData.tel" type="tel" :placeholder="$t('addOwnerRepair.placeholder.tel')" />
      </el-form-item>

      <el-form-item :label="$t('addOwnerRepair.appointmentTime')" prop="appointmentTime" required>
        <el-date-picker v-model="formData.appointmentTime" type="datetime" style="width:100%"
          :placeholder="$t('addOwnerRepair.placeholder.appointmentTime')" value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>

      <el-form-item :label="$t('addOwnerRepair.context')" prop="context" required>
        <el-input v-model.trim="formData.context" type="textarea" rows="4" :placeholder="$t('addOwnerRepair.placeholder.context')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveOwnerRepairInfo">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import * as ownerRepairManageApi from '@/api/work/ownerRepairManageApi'
import { getCommunityName } from '@/api/community/communityApi'
import {getFloors, getUnits, queryRooms} from '@/api/room/roomApi'
//import {getDateYYYYMMDD} from '@/utils/dateUtil'

export default {
  name: 'AddOwnerRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '',
        repairObjId: '',
        repairObjName: '',
        repairChannel: 'T',
        floorId: '',
        unitId: '',
        roomId: ''
      },
      repairSettings: [],
      repairObjTypes: [
        { value: '001', label: this.$t('addOwnerRepair.community') },
        { value: '002', label: this.$t('addOwnerRepair.building') },
        { value: '003', label: this.$t('addOwnerRepair.unit') },
        { value: '004', label: this.$t('addOwnerRepair.room') }
      ],
      floors:[],
      units:[],
      rooms:[],
      rules: {
        repairObjType: [{ required: true, message: this.$t('addOwnerRepair.placeholder.repairScope'), trigger: 'change' }],
        repairType: [{ required: true, message: this.$t('addOwnerRepair.placeholder.repairType'), trigger: 'change' }],
        repairName: [{ required: true, message: this.$t('addOwnerRepair.placeholder.repairName'), trigger: 'blur' }],
        tel: [{ required: true, message: this.$t('addOwnerRepair.placeholder.tel'), trigger: 'blur' }],
        appointmentTime: [{ required: true, message: this.$t('addOwnerRepair.placeholder.appointmentTime'), trigger: 'change' }],
        context: [{ required: true, message: this.$t('addOwnerRepair.placeholder.context'), trigger: 'blur' }]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.resetForm()
     // this.formData.appointmentTime =getDateYYYYMMDD()
      this.listRepairSettings('T')
    },
    closeDialog() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.formData = {
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '',
        repairObjId: '',
        repairObjName: '',
        repairChannel: 'T',
        floorId: '',
        unitId: '',
        roomId: ''
      }
      this.$refs.form && this.$refs.form.resetFields()
    },
    handleFloorChange(floorId) {
      if (this.formData.repairObjType == '002') {
        this.formData.repairObjId = floorId
        this.formData.repairObjName = this.floors.find(item => item.floorId == floorId).floorNum + this.$t('addOwnerRepair.buildingSuffix')
      }else{
        this.listUnits()
      }
    },
    handleUnitChange(unitId) {
      if (this.formData.repairObjType == '003') {
        this.formData.repairObjId = unitId
        this.formData.repairObjName = this.units.find(item => item.unitId == unitId).unitNum + this.$t('addOwnerRepair.unitSuffix')
      }else{
        this.listRooms()
      }
    },
    handleRoomChange(roomId) {
      if (this.formData.repairObjType === '004') {
        this.formData.repairObjId = roomId
        this.formData.repairObjName =this.rooms.find(item => item.roomId == roomId).roomName + this.$t('addOwnerRepair.roomSuffix')
        this.formData.repairName = this.rooms.find(item => item.roomId == roomId).ownerName
        this.formData.tel = this.rooms.find(item => item.roomId == roomId).link
      }
    },
    changeRepairObjType() {
      this.formData.repairType = ''
      const publicArea = this.formData.repairObjType === '004' ? 'F' : 'T'
      this.listRepairSettings(publicArea)
      this.listFloors()
    },
    async listFloors() {
      const params = {
        communityId: this.getCommunityId(),
        page: 1,
        row: 200
      }
      const { apiFloorDataVoList } = await getFloors(params);
      this.floors = apiFloorDataVoList
    },
    async listUnits() {
      const params = {
        communityId: this.getCommunityId(),
        floorId: this.formData.floorId,
        page: 1,
        row: 200
      }
      const  data  = await getUnits(params);
      this.units = data
    },
    async listRooms() {
      const params = {
        communityId: this.getCommunityId(),
        unitId: this.formData.unitId,
        page: 1,
        row: 500
      }
      const { rooms } = await queryRooms(params);
      this.rooms = rooms
    },
    async listRepairSettings(publicArea) {
      const params = {
        page: 1,
        row: 50,
        publicArea: publicArea,
        communityId: this.getCommunityId()
      }
      const { data } = await ownerRepairManageApi.listRepairSettings(params);
      this.repairSettings = data
    },
    saveOwnerRepairInfo() {
      if (this.formData.repairObjType === '001') {
        this.formData.repairObjId = this.getCommunityId()
        this.formData.repairObjName = getCommunityName()
      }
      this.formData.communityId = this.getCommunityId()

      this.$refs.form.validate(valid => {
        if (valid) {
          ownerRepairManageApi.saveOwnerRepair(this.formData).then(res => {
            if (res.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.visible = false
              this.$emit('success')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>