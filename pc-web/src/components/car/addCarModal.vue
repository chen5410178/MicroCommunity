<template>
  <el-dialog :title="$t('common.add')" :visible.sync="visible" width="50%">
    <el-form :model="addCarModelInfo" :rules="rules" ref="addCarForm">
      <el-form-item :label="$t('listOwnerCar.carNum')" prop="carNum">
        <el-input v-model="addCarModelInfo.carNum" :placeholder="$t('listOwnerCar.carNum')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.carBrand')" prop="carBrand">
        <el-input v-model="addCarModelInfo.carBrand" :placeholder="$t('listOwnerCar.carBrand')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.carType')" prop="carType">
        <el-select v-model="addCarModelInfo.carType" :placeholder="$t('listOwnerCar.carType')"
          style="width:100%">
          <el-option v-for="carType in carTypes" :key="carType.statusCd" :label="carType.name"
            :value="carType.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.color')" prop="carColor">
        <el-input v-model="addCarModelInfo.carColor" :placeholder="$t('listOwnerCar.color')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.remark')">
        <el-input type="textarea" v-model="addCarModelInfo.remark" :placeholder="$t('listOwnerCar.remark')"
          rows="3"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveAddCarInfo">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOwnerCarMember } from '@/api/car/addCarModalApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddCarModal',
  data() {
    return {
      visible: false,
      addCarModelInfo: {
        flowComponent: 'addCar',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        remark: "",
        carId: ''
      },
      carTypes: [],
      rules: {
        carNum: [
          { required: true, message: this.$t('common.required'), trigger: 'blur' },
          { min: 2, max: 12, message: this.$t('common.invalid'), trigger: 'blur' }
        ],
        carBrand: [
          { required: true, message: this.$t('common.required'), trigger: 'blur' },
          { max: 50, message: this.$t('common.invalid'), trigger: 'blur' }
        ],
        carType: [
          { required: true, message: this.$t('common.required'), trigger: 'change' }
        ],
        carColor: [
          { required: true, message: this.$t('common.required'), trigger: 'blur' },
          { max: 12, message: this.$t('common.invalid'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadCarTypes()
  },
  methods: {
    async loadCarTypes() {
      try {
        const data = await getDict('owner_car', 'car_type')
        this.carTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    open(data) {
      this.addCarModelInfo.carId = data.carId
      this.visible = true
      this.$nextTick(() => {
        this.$refs.addCarForm.resetFields()
      })
    },
    saveAddCarInfo() {
      this.$refs.addCarForm.validate(valid => {
        if (valid) {
          this.addCarModelInfo.communityId = this.communityId
          saveOwnerCarMember(this.addCarModelInfo).then(response => {
            if (response.code === 0) {
              this.visible = false
              this.clearAddCarModalInfo()
              this.$emit('notify')
              this.$message.success(this.$t('common.operationSuccess'))
            } else {
              this.$message.error(response.msg)
            }
          }).catch(error => {
            console.error('请求失败处理', error)
            this.$message.error(error)
          })
        }
      })
    },
    clearAddCarModalInfo() {
      this.addCarModelInfo = {
        flowComponent: 'addCar',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        remark: "",
        carNumType: '',
        carId: ''
      }
    }
  }
}
</script>