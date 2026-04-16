<template>
  <el-dialog :title="$t('editParkingSpaceApply.title')" :visible.sync="visible" width="50%">
    <el-form :model="form" label-width="120px">
      <el-form-item :label="$t('editParkingSpaceApply.carNum')">
        <el-input v-model="form.carNum" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.carBrand')">
        <el-input v-model="form.carBrand"></el-input>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.carType')">
        <el-select v-model="form.carType" style="width:100%">
          <el-option v-for="item in carTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.carColor')">
        <el-input v-model="form.carColor"></el-input>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.startTime')">
        <el-date-picker v-model="form.startTime" type="datetime" style="width:100%" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.endTime')">
        <el-date-picker v-model="form.endTime" type="datetime" style="width:100%" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('editParkingSpaceApply.remark')">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateParkingSpaceApply } from '@/api/property/editParkingSpaceApplyApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditParkingSpaceApply',
  data() {
    return {
      visible: false,
      carTypes: [],
      form: {
        applyId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        startTime: '',
        endTime: '',
        remark: ''
      }
    }
  },
  created() {
    this.getCarTypes()
  },
  methods: {
    getCarTypes() {
      getDict('car_type').then(response => {
        this.carTypes = response.data.map(item => ({
          value: item.code,
          label: item.name
        }))
      })
    },
    open(data) {
      this.form = {
        applyId: data.applyId,
        carNum: data.carNum,
        carBrand: data.carBrand,
        carType: data.carType,
        carColor: data.carColor,
        startTime: data.startTime,
        endTime: data.endTime,
        remark: data.remark || ''
      }
      this.visible = true
    },
    handleSubmit() {
      updateParkingSpaceApply(this.form).then(response => {
        console.log(response)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      }).catch(error => {
        this.$message.error(error.message)
      })
    }
  }
}
</script>