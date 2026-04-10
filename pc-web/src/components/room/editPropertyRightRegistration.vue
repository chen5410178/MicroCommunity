<template>
  <el-dialog :title="$t('propertyRightRegistration.edit.title')" :visible.sync="visible" width="70%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.floor')" prop="floorId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.edit.floorRequired'), trigger: 'change' }]">
            <el-select v-model="formData.floorId" :placeholder="$t('propertyRightRegistration.edit.floorPlaceholder')"
              style="width:100%" @change="handleFloorChange">
              <el-option v-for="item in floors" :key="item.floorId"
                :label="`${item.floorNum}${$t('propertyRightRegistration.edit.floorUnit')}`" :value="item.floorId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.unit')" prop="unitId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.edit.unitRequired'), trigger: 'change' }]">
            <el-select v-model="formData.unitId" :placeholder="$t('propertyRightRegistration.edit.unitPlaceholder')"
              style="width:100%" @change="handleUnitChange">
              <el-option v-for="item in units" :key="item.unitId"
                :label="`${item.unitNum}${$t('propertyRightRegistration.edit.unitUnit')}`" :value="item.unitId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.room')" prop="roomId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.edit.roomRequired'), trigger: 'change' }]">
            <el-select v-model="formData.roomId" :placeholder="$t('propertyRightRegistration.edit.roomPlaceholder')"
              style="width:100%">
              <el-option v-for="item in rooms" :key="item.roomId" :label="item.roomNum" :value="item.roomId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.name')" prop="name" :rules="[
            { required: true, message: $t('propertyRightRegistration.edit.nameRequired'), trigger: 'blur' },
            { min: 2, max: 64, message: $t('propertyRightRegistration.edit.nameLength'), trigger: 'blur' }
          ]">
            <el-input v-model.trim="formData.name"
              :placeholder="$t('propertyRightRegistration.edit.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.link')" prop="link"
            :rules="[{ required: true, message: $t('propertyRightRegistration.edit.linkRequired'), trigger: 'blur' }]">
            <el-input v-model.trim="formData.link"
              :placeholder="$t('propertyRightRegistration.edit.linkPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.edit.idCard')" prop="idCard" :rules="[
            { required: true, message: $t('propertyRightRegistration.edit.idCardRequired'), trigger: 'blur' },
          ]">
            <el-input v-model.trim="formData.idCard"
              :placeholder="$t('propertyRightRegistration.edit.idCardPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item :label="$t('propertyRightRegistration.edit.address')" prop="address" :rules="[
        { required: true, message: $t('propertyRightRegistration.edit.addressRequired'), trigger: 'blur' },
        { max: 255, message: $t('propertyRightRegistration.edit.addressMax'), trigger: 'blur' }
      ]">
        <el-input v-model.trim="formData.address"
          :placeholder="$t('propertyRightRegistration.edit.addressPlaceholder')" />
      </el-form-item>

      <div class="dialog-footer">
        <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getFloors, getUnits, queryRooms } from '@/api/room/roomApi'
import { updatePropertyRightRegistration } from '@/api/room/propertyRightRegistrationManageApi'

export default {
  name: 'EditPropertyRightRegistration',
  data() {
    return {
      visible: false,
      formData: {
        prrId: '',
        roomId: '',
        floorId: '',
        unitId: '',
        name: '',
        link: '',
        idCard: '',
        address: '',
        communityId: getCommunityId()
      },
      floors: [],
      units: [],
      rooms: []
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData = {
        prrId: data.prrId,
        roomId: data.roomId,
        floorId: data.floorId,
        unitId: data.unitId,
        name: data.name,
        link: data.link,
        idCard: data.idCard,
        address: data.address,
        communityId: getCommunityId()
      }
      this.getFloors()
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async getFloors() {
      try {
        const params = {
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const data = await getFloors(params)
        this.floors = data.apiFloorDataVoList || []
        this.getUnits()
      } catch (error) {
        console.error('获取楼栋数据失败:', error)
      }
    },
    async getUnits() {
      try {
        const params = {
          floorId: this.formData.floorId,
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const data = await getUnits(params)
        this.units = data || []
        this.getRooms()
      } catch (error) {
        console.error('获取单元数据失败:', error)
      }
    },
    async getRooms() {
      try {
        const params = {
          unitId: this.formData.unitId,
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const data = await queryRooms(params)
        this.rooms = data.rooms || []
      } catch (error) {
        console.error('获取房间数据失败:', error)
      }
    },
    handleFloorChange(floorId) {
      this.formData.unitId = ''
      this.formData.roomId = ''
      this.units = []
      this.rooms = []
      if (floorId) {
        this.getUnits()
      }
    },
    handleUnitChange(unitId) {
      this.formData.roomId = ''
      this.rooms = []
      if (unitId) {
        this.getRooms()
      }
    },
   
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        const params = {
          ...this.formData,
          flag: '0' // 0表示修改操作
        }

        const res = await updatePropertyRightRegistration(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg || this.$t('propertyRightRegistration.edit.error'))
        }
      } catch (error) {
        if (error !== 'validate') {
          console.error('修改失败:', error)
          this.$message.error(this.$t('propertyRightRegistration.edit.error'))
        }
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>