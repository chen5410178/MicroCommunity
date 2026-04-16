<template>
  <el-dialog :title="$t('addParkingSpace.title')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item :label="$t('addParkingSpace.parkingSpaceNum')" prop="num"
        :rules="[{ required: true, message: $t('addParkingSpace.parkingSpaceNumPlaceholder'), trigger: 'blur' }]">
        <el-input v-model="form.num" :placeholder="$t('addParkingSpace.parkingSpaceNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('addParkingSpace.parkingLot')" prop="paId"
        :rules="[{ required: true, message: $t('addParkingSpace.parkingLotPlaceholder'), trigger: 'change' }]">
        <el-select v-model="form.paId" :placeholder="$t('addParkingSpace.parkingLotPlaceholder')" style="width:100%">
          <el-option v-for="item in parkingAreas" :key="item.paId" :label="item.num" :value="item.paId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addParkingSpace.parkingSpaceType')" prop="parkingType"
        :rules="[{ required: true, message: $t('addParkingSpace.parkingSpaceTypePlaceholder'), trigger: 'change' }]">
        <el-select v-model="form.parkingType" :placeholder="$t('addParkingSpace.parkingSpaceTypePlaceholder')"
          style="width:100%">
          <el-option v-for="item in parkingTypes" :key="item.statusCd" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addParkingSpace.area')" prop="area" :rules="[
        { required: true, message: $t('addParkingSpace.areaPlaceholder'), trigger: 'blur' },
        { pattern: /^\d+(\.\d{1,2})?$/, message: $t('addParkingSpace.areaPlaceholder'), trigger: 'blur' }
      ]">
        <el-input v-model="form.area" type="number" :placeholder="$t('addParkingSpace.areaPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('addParkingSpace.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('addParkingSpace.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('addParkingSpace.cancel') }}</el-button>
      <el-button type="primary" @click="saveParkingSpace" :loading="saving">
        {{ $t('addParkingSpace.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveParkingSpace } from '@/api/car/listParkingSpaceApi'
import { listParkingAreas } from '@/api/car/listParkingSpaceApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddParkingSpace',
  data() {
    return {
      visible: false,
      saving: false,
      form: {
        num: '',
        paId: '',
        parkingType: '1',
        area: '1',
        remark: '',
        communityId: getCommunityId()
      },
      parkingTypes: [],
      parkingAreas: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadParkingTypes()
      this.loadParkingAreas()
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
      } catch (error) {
        this.$message.error(this.$t('common.loadError'))
      }
    },

    async saveParkingSpace() {
      try {
        this.saving = true
        await this.$refs.form.validate()

        const res = await saveParkingSpace(this.form)
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
      this.$refs.form.resetFields()
      this.form = {
        num: '',
        paId: '',
        parkingType: '1',
        area: '1',
        remark: '',
        communityId: getCommunityId()
      }
    }
  }
}
</script>