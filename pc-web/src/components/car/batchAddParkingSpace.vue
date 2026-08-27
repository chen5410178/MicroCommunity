<template>
  <el-dialog :title="$t('batchAddParkingSpace.title')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item :label="$t('batchAddParkingSpace.prefixNum')" prop="preNum">
        <el-input v-model="form.preNum" :placeholder="$t('batchAddParkingSpace.prefixNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('batchAddParkingSpace.startNum')" prop="startNum"
        :rules="[{ required: true, message: $t('batchAddParkingSpace.startNumPlaceholder'), trigger: 'blur' }]">
        <el-input v-model="form.startNum" type="number"
          :placeholder="$t('batchAddParkingSpace.startNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('batchAddParkingSpace.endNum')" prop="endNum"
        :rules="[{ required: true, message: $t('batchAddParkingSpace.endNumPlaceholder'), trigger: 'blur' }]">
        <el-input v-model="form.endNum" type="number"
          :placeholder="$t('batchAddParkingSpace.endNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('batchAddParkingSpace.parkingLot')" prop="paId"
        :rules="[{ required: true, message: $t('batchAddParkingSpace.parkingLotPlaceholder'), trigger: 'change' }]">
        <el-select v-model="form.paId" :placeholder="$t('batchAddParkingSpace.parkingLotPlaceholder')" style="width:100%"
          @change="handlePaIdChange">
          <el-option v-for="item in parkingAreas" :key="item.paId" :label="item.num" :value="item.paId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('batchAddParkingSpace.section')" prop="sectionId">
        <el-select v-model="form.sectionId" :placeholder="$t('batchAddParkingSpace.sectionPlaceholder')"
          style="width:100%" clearable>
          <el-option v-for="item in sections" :key="item.sectionId" :label="item.sectionName || item.sectionNum"
            :value="item.sectionId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('batchAddParkingSpace.parkingSpaceType')" prop="parkingType"
        :rules="[{ required: true, message: $t('batchAddParkingSpace.parkingSpaceTypePlaceholder'), trigger: 'change' }]">
        <el-select v-model="form.parkingType" :placeholder="$t('batchAddParkingSpace.parkingSpaceTypePlaceholder')"
          style="width:100%">
          <template slot="prepend" v-for="item in parkingTypes" >
            <el-option :label="item.name" :key="item.statusCd" :value="item.statusCd" v-if="item.statusCd !== '2'"></el-option>
          </template>
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('batchAddParkingSpace.cancel') }}</el-button>
      <el-button type="primary" @click="batchSaveParkingSpace" :loading="saving">
        {{ $t('batchAddParkingSpace.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { batchSaveParkingSpace, listParkingAreaSections } from '@/api/car/listParkingSpaceApi'
import { listParkingAreas } from '@/api/car/listParkingSpaceApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'BatchAddParkingSpace',
  data() {
    return {
      visible: false,
      saving: false,
      form: {
        preNum: '',
        startNum: '',
        endNum: '',
        paId: '',
        sectionId: '',
        parkingType: '1',
        area: '1',
        communityId: getCommunityId()
      },
      parkingTypes: [],
      parkingAreas: [],
      sections: []
    }
  },
  methods: {
    open(params) {
      this.form = {
        preNum: '',
        startNum: '',
        endNum: '',
        paId: '',
        sectionId: '',
        parkingType: '1',
        area: '1',
        communityId: getCommunityId()
      }
      if (params && params.paId) {
        this.form.paId = params.paId
      }
      if (params && params.sectionId) {
        this.form.sectionId = params.sectionId
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

    async batchSaveParkingSpace() {
      try {
        this.saving = true
        await this.$refs.form.validate()

        // 验证结束编号大于开始编号
        if (parseInt(this.form.endNum) < parseInt(this.form.startNum)) {
          this.$message.error(this.$t('batchAddParkingSpace.endNumPlaceholder'))
          return
        }

        const res = await batchSaveParkingSpace(this.form)
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
        preNum: '',
        startNum: '',
        endNum: '',
        paId: '',
        sectionId: '',
        parkingType: '1',
        area: '1',
        communityId: getCommunityId()
      }
    }
  }
}
</script>
