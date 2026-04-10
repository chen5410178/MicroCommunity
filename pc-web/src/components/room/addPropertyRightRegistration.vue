<template>
  <el-dialog :title="$t('propertyRightRegistration.add.title')" :visible.sync="visible" width="70%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.floor')" prop="floorId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.floorRequired'), trigger: 'change' }]">
            <el-select v-model="formData.floorId" :placeholder="$t('propertyRightRegistration.add.floorPlaceholder')"
              style="width:100%" @change="handleFloorChange">
              <el-option v-for="item in floors" :key="item.floorId"
                :label="`${item.floorNum}${$t('propertyRightRegistration.add.floorUnit')}`" :value="item.floorId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.unit')" prop="unitId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.unitRequired'), trigger: 'change' }]">
            <el-select v-model="formData.unitId" :placeholder="$t('propertyRightRegistration.add.unitPlaceholder')"
              style="width:100%" @change="handleUnitChange">
              <el-option v-for="item in units" :key="item.unitId"
                :label="`${item.unitNum}${$t('propertyRightRegistration.add.unitUnit')}`" :value="item.unitId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.room')" prop="roomId"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.roomRequired'), trigger: 'change' }]">
            <el-select v-model="formData.roomId" :placeholder="$t('propertyRightRegistration.add.roomPlaceholder')"
              style="width:100%">
              <el-option v-for="item in rooms" :key="item.roomId" :label="item.roomNum" :value="item.roomId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.name')" prop="name" :rules="[
            { required: true, message: $t('propertyRightRegistration.add.nameRequired'), trigger: 'blur' },
            { min: 2, max: 64, message: $t('propertyRightRegistration.add.nameLength'), trigger: 'blur' }
          ]">
            <el-input v-model.trim="formData.name" :placeholder="$t('propertyRightRegistration.add.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.link')" prop="link"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.linkRequired'), trigger: 'blur' }]">
            <el-input v-model.trim="formData.link" :placeholder="$t('propertyRightRegistration.add.linkPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.idCard')" prop="idCard" :rules="[
            { required: true, message: $t('propertyRightRegistration.add.idCardRequired'), trigger: 'blur' },
          ]">
            <el-input v-model.trim="formData.idCard"
              :placeholder="$t('propertyRightRegistration.add.idCardPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item :label="$t('propertyRightRegistration.add.address')" prop="address" :rules="[
        { required: true, message: $t('propertyRightRegistration.add.addressRequired'), trigger: 'blur' },
        { max: 255, message: $t('propertyRightRegistration.add.addressMax'), trigger: 'blur' }
      ]">
        <el-input v-model.trim="formData.address"
          :placeholder="$t('propertyRightRegistration.add.addressPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('propertyRightRegistration.add.idCardPhotos')" prop="idCardPhotos"
        :rules="[{ required: true, message: $t('propertyRightRegistration.add.idCardPhotosRequired'), trigger: 'change' }]">
        <upload-image-url ref="idCardUpload" :image-count="2" @notifyUploadCoverImage="handleIdCardChange" />
        <p class="tip">{{ $t('propertyRightRegistration.add.idCardPhotosTip') }}</p>
      </el-form-item>

      <el-form-item :label="$t('propertyRightRegistration.add.housePurchasePhotos')" prop="housePurchasePhotos"
        :rules="[{ required: true, message: $t('propertyRightRegistration.add.housePurchasePhotosRequired'), trigger: 'change' }]">
        <upload-image-url ref="housePurchaseUpload" :image-count="10" @notifyUploadCoverImage="handleHousePurchaseChange" />
        <p class="tip">{{ $t('propertyRightRegistration.add.housePurchasePhotosTip') }}</p>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.isTrue')" prop="isTrue"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.isTrueRequired'), trigger: 'change' }]">
            <el-select v-model="formData.isTrue" :placeholder="$t('propertyRightRegistration.add.isTruePlaceholder')"
              style="width:100%" @change="handleIsTrueChange">
              <el-option :label="$t('common.yes')" value="true" />
              <el-option :label="$t('common.no')" value="false" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="formData.isTrue === 'true'" :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.repairPhotos')" prop="repairPhotos"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.repairPhotosRequired'), trigger: 'change' }]">
            <upload-image-url ref="repairUpload" :image-count="3" @notifyUploadCoverImage="handleRepairChange" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.flag')" prop="flag"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.flagRequired'), trigger: 'change' }]">
            <el-select v-model="formData.flag" :placeholder="$t('propertyRightRegistration.add.flagPlaceholder')"
              style="width:100%" @change="handleFlagChange">
              <el-option :label="$t('common.yes')" value="0" />
              <el-option :label="$t('common.no')" value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="formData.flag === '0'" :span="12">
          <el-form-item :label="$t('propertyRightRegistration.add.deedTaxPhotos')" prop="deedTaxPhotos"
            :rules="[{ required: true, message: $t('propertyRightRegistration.add.deedTaxPhotosRequired'), trigger: 'change' }]">
            <upload-image-url ref="deedTaxUpload" :image-count="3" @notifyUploadCoverImage="handleDeedTaxChange" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getFloors, getUnits, queryRooms } from '@/api/room/roomApi'
import { savePropertyRightRegistration } from '@/api/room/propertyRightRegistrationManageApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'

export default {
  name: 'AddPropertyRightRegistration',
  components: {
    UploadImageUrl
  },
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
        isTrue: '',
        flag: '',
        state: '0',
        idCardPhotos: [],
        housePurchasePhotos: [],
        repairPhotos: [],
        deedTaxPhotos: [],
        communityId: getCommunityId()
      },
      floors: [],
      units: [],
      rooms: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.getFloors()
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
        this.$refs.idCardUpload && this.$refs.idCardUpload.clearImages()
        this.$refs.housePurchaseUpload && this.$refs.housePurchaseUpload.clearImages()
        this.$refs.repairUpload && this.$refs.repairUpload.clearImages()
        this.$refs.deedTaxUpload && this.$refs.deedTaxUpload.clearImages()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        prrId: '',
        roomId: '',
        floorId: '',
        unitId: '',
        name: '',
        link: '',
        idCard: '',
        address: '',
        isTrue: '',
        flag: '',
        state: '0',
        idCardPhotos: [],
        housePurchasePhotos: [],
        repairPhotos: [],
        deedTaxPhotos: [],
        communityId: getCommunityId()
      }
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
      } catch (error) {
        console.error('获取楼栋数据失败:', error)
      }
    },
    async handleFloorChange(floorId) {
      try {
        const params = {
          floorId,
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const data = await getUnits(params)
        this.units = data || []
        this.formData.unitId = ''
        this.rooms = []
        this.formData.roomId = ''
      } catch (error) {
        console.error('获取单元数据失败:', error)
      }
    },
    async handleUnitChange(unitId) {
      try {
        const params = {
          unitId,
          communityId: this.formData.communityId,
          page: 1,
          row: 50
        }
        const data = await queryRooms(params)
        this.rooms = data.rooms || []
        this.formData.roomId = ''
      } catch (error) {
        console.error('获取房间数据失败:', error)
      }
    },
    handleIsTrueChange(val) {
      if (val !== 'true') {
        this.formData.repairPhotos = []
        this.$refs.repairUpload && this.$refs.repairUpload.clearImages()
      }
    },
    handleFlagChange(val) {
      if (val !== '0') {
        this.formData.deedTaxPhotos = []
        this.$refs.deedTaxUpload && this.$refs.deedTaxUpload.clearImages()
      }
    },
    handleIdCardChange(photos) {
      this.formData.idCardPhotos = photos
    },
    handleHousePurchaseChange(photos) {
      this.formData.housePurchasePhotos = photos
    },
    handleRepairChange(photos) {
      this.formData.repairPhotos = photos
    },
    handleDeedTaxChange(photos) {
      this.formData.deedTaxPhotos = photos
    },

    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const params = {
          ...this.formData,
          idCardPhotos: this.formData.idCardPhotos,
          housePurchasePhotos: this.formData.housePurchasePhotos,
          repairPhotos: this.formData.repairPhotos,
          deedTaxPhotos: this.formData.deedTaxPhotos
        }

        await savePropertyRightRegistration(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('保存失败:', error)
        if (error !== 'validate') {
          this.$message.error(this.$t('propertyRightRegistration.add.error'))
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.tip {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}
</style>