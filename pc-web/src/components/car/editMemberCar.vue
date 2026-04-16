<template>
  <el-dialog :title="$t('common.edit')" :visible.sync="visible" width="50%">
    <el-form :model="editMemberCarInfo" :rules="rules" ref="editMemberCarForm">
      <el-form-item :label="$t('listOwnerCar.carNum')" prop="carNum">
        <el-input v-model="editMemberCarInfo.carNum" :placeholder="$t('listOwnerCar.licensePlateRequired')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.carBrand')">
        <el-input v-model="editMemberCarInfo.carBrand" :placeholder="$t('listOwnerCar.carBrandOptional')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.carType')" prop="carType">
        <el-select v-model="editMemberCarInfo.carType" :placeholder="$t('listOwnerCar.carTypeRequired')" style="width:100%">
          <el-option v-for="carType in carTypes" :key="carType.statusCd" 
                     :label="carType.name" :value="carType.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.color')">
        <el-input v-model="editMemberCarInfo.carColor" :placeholder="$t('listOwnerCar.colorOptional')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('listOwnerCar.remark')">
        <el-input type="textarea" v-model="editMemberCarInfo.remark" :placeholder="$t('listOwnerCar.remarkPlaceholder')" rows="3"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="_submitEditMemberCarInfo">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { editOwnerCar } from '@/api/car/listOwnerCarApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMemberCar',
  data() {
    return {
      visible: false,
      editMemberCarInfo: {
        carId: '',
        memberId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        remark: "",
        startTime: '',
        endTime: '',
        carNumType: '',
        leaseType: '',
      },
      carTypes: [],
      rules: {
        carNum: [
          { required: true, message: this.$t('editMemberCar.licensePlateRequired'), trigger: 'blur' },
          { min: 2, max: 12, message: this.$t('editMemberCar.licensePlateInvalid'), trigger: 'blur' }
        ],
        carType: [
          { required: true, message: this.$t('editMemberCar.carTypeRequired'), trigger: 'change' }
        ],
        memberId: [
          { required: true, message: this.$t('editMemberCar.carDataError'), trigger: 'blur' }
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
    open(carInfo) {
      this.editMemberCarInfo = Object.assign({}, carInfo)
      this.visible = true
      this.$nextTick(() => {
        this.$refs.editMemberCarForm.clearValidate()
      })
    },
    _submitEditMemberCarInfo() {
      this.$refs.editMemberCarForm.validate(valid => {
        if (valid) {
          this.editMemberCarInfo.communityId = this.communityId
          editOwnerCar(this.editMemberCarInfo).then(response => {
            if (response.code === 0) {
              this.visible = false
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
    }
  }
}
</script>