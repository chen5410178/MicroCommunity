<template>
  <el-dialog 
    :title="$t('editParkingSpace.title')" 
    :visible.sync="visible"
    width="50%"
    @close="closeDialog"
  >
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item :label="$t('editParkingSpace.parkingSpaceNum')" prop="num"
        :rules="[{ required: true, message: $t('editParkingSpace.parkingSpaceNumPlaceholder'), trigger: 'blur' }]"
      >
        <el-input 
          v-model="form.num" 
          :placeholder="$t('editParkingSpace.parkingSpaceNumPlaceholder')"
          :disabled="form.parkingType === '2'"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpace.parkingLot')" prop="paId"
        :rules="[{ required: true, message: $t('editParkingSpace.parkingLotPlaceholder'), trigger: 'change' }]"
      >
        <el-select 
          v-model="form.paId" 
          :placeholder="$t('editParkingSpace.parkingLotPlaceholder')"
          style="width:100%"
          @change="handlePaIdChange"
        >
          <el-option 
            v-for="item in parkingAreas" 
            :key="item.paId"
            :label="item.num" 
            :value="item.paId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('editParkingSpace.section')" prop="sectionId">
        <el-select 
          v-model="form.sectionId" 
          :placeholder="$t('editParkingSpace.sectionPlaceholder')"
          style="width:100%"
          clearable
        >
          <el-option 
            v-for="item in sections" 
            :key="item.sectionId"
            :label="item.sectionName || item.sectionNum" 
            :value="item.sectionId"
          ></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpace.parkingSpaceType')" prop="parkingType"
        :rules="[{ required: true, message: $t('editParkingSpace.parkingSpaceTypePlaceholder'), trigger: 'change' }]"
      >
        <el-select 
          v-model="form.parkingType" 
          :placeholder="$t('editParkingSpace.parkingSpaceTypePlaceholder')"
          style="width:100%"
          :disabled="form.parkingType === '2'"
        >
          <el-option 
            v-for="item in parkingTypes" 
            :key="item.statusCd"
            :label="item.name" 
            :value="item.statusCd"
          ></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpace.area')" prop="area"
        :rules="[
          { required: true, message: $t('editParkingSpace.areaPlaceholder'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: $t('editParkingSpace.areaPlaceholder'), trigger: 'blur' }
        ]"
      >
        <el-input 
          v-model="form.area" 
          type="number"
          :placeholder="$t('editParkingSpace.areaPlaceholder')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpace.remark')" prop="remark">
        <el-input 
          v-model="form.remark" 
          type="textarea"
          :placeholder="$t('editParkingSpace.remarkPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('editParkingSpace.cancel') }}</el-button>
      <el-button type="primary" @click="editParkingSpace" :loading="saving">
        {{ $t('editParkingSpace.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { editParkingSpace, listParkingAreaSections } from '@/api/car/listParkingSpaceApi'
import { listParkingAreas } from '@/api/car/listParkingSpaceApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditParkingSpace',
  data() {
    return {
      visible: false,
      saving: false,
      form: {
        psId: '',
        num: '',
        paId: '',
        sectionId: '',
        parkingType: '',
        area: '',
        remark: '',
        communityId: getCommunityId()
      },
      parkingTypes: [],
      parkingAreas: [],
      sections: []
    }
  },
  methods: {
    open(row) {
      this.form = { ...row, communityId: getCommunityId() }
      if (!this.form.sectionId) {
        this.form.sectionId = ''
      }
      this.visible = true
      this.loadParkingTypes()
      this.loadParkingAreas()
    },

    handlePaIdChange(paId) {
      this.form.sectionId = ''
      if (paId) {
        this.loadSections(paId)
      } else {
        this.sections = []
      }
    },
    
    async loadParkingTypes() {
      try {
        const data = await getDict('parking_space', 'parking_type')
        this.parkingTypes = data
      } catch (error) {
        this.$message.error(this.$t('common.loadDictError'))
      }
    },
    
    async loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId()
        }
        const res = await listParkingAreas(params)
        this.parkingAreas = res.parkingAreas
        if (this.form.paId) {
          this.loadSections(this.form.paId)
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadError'))
      }
    },

    async loadSections(paId) {
      try {
        const params = {
          page: 1,
          row: 100,
          paId: paId,
          communityId: getCommunityId()
        }
        const res = await listParkingAreaSections(params)
        this.sections = res.data || []
      } catch (error) {
        this.$message.error(this.$t('common.loadError'))
      }
    },
    
    async editParkingSpace() {
      try {
        this.saving = true
        await this.$refs.form.validate()
        
        const res = await editParkingSpace(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        } else {
          this.$message.error(res.msg || this.$t('common.saveError'))
        }
      } catch (error) {
        console.error('Validation failed:', error)
      } finally {
        this.saving = false
      }
    },
    
    closeDialog() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = {
        psId: '',
        num: '',
        paId: '',
        sectionId: '',
        parkingType: '',
        area: '',
        remark: '',
        communityId: getCommunityId()
      }
    }
  }
}
</script>
