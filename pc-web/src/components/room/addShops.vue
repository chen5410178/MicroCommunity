<template>
  <el-dialog :title="$t('shops.addShop')" :visible.sync="dialogVisible" width="40%">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('shops.shopNumber')" prop="roomNum">
        <el-input v-model="formData.roomNum" :placeholder="$t('shops.shopNumber')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.shopFloor')" prop="layer">
        <el-input v-model="formData.layer" :placeholder="$t('shops.floorRequired')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.building')" prop="floorId">
        <el-select v-model="formData.floorId" :placeholder="$t('shops.building')" style="width: 100%">
          <el-option v-for="item in floors" :key="item.floorId" :label="item.floorNum"
            :value="item.floorId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('shops.rent')" prop="roomRent">
        <el-input v-model="formData.roomRent" :placeholder="$t('shops.rentPlaceholder')" type="number"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.builtUpArea')" prop="builtUpArea">
        <el-input v-model="formData.builtUpArea" :placeholder="$t('shops.builtUpAreaPlaceholder')"
          type="number"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.roomArea')" prop="roomArea">
        <el-input v-model="formData.roomArea" :placeholder="$t('shops.roomAreaPlaceholder')" type="number"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.feeCoefficient')" prop="feeCoefficient">
        <el-input v-model="formData.feeCoefficient" :placeholder="$t('shops.feeCoefficientPlaceholder')"
          type="number"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.remark')">
        <el-input type="textarea" v-model="formData.remark" :placeholder="$t('shops.remark')"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveShops } from '@/api/room/shopsApi'
import { getFloors } from '@/api/room/roomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddShops',
  components: {
  },
  data() {
    return {
      dialogVisible: false,
      floors:[],
      formData: {
        roomNum: '',
        layer: '',
        floorId: '',
        roomRent: '',
        builtUpArea: '',
        roomArea: '',
        feeCoefficient: '1.00',
        remark: '',
        roomSubType: '120'
      },
      rules: {
        roomNum: [
          { required: true, message: this.$t('shops.shopNumberRequired'), trigger: 'blur' }
        ],
        layer: [
          { required: true, message: this.$t('shops.floorRequired'), trigger: 'blur' }
        ],
        floorId: [
          { required: true, message: this.$t('shops.buildingRequired'), trigger: 'change' }
        ],
        roomRent: [
          { required: true, message: this.$t('shops.rentRequired'), trigger: 'blur' }
        ],
        builtUpArea: [
          { required: true, message: this.$t('shops.builtUpAreaRequired'), trigger: 'blur' }
        ],
        roomArea: [
          { required: true, message: this.$t('shops.roomAreaRequired'), trigger: 'blur' }
        ],
        feeCoefficient: [
          { required: true, message: this.$t('shops.feeCoefficientRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.resetForm()
      this.searchFloors()
    },
    resetForm() {
      this.formData = {
        roomNum: '',
        layer: '',
        floorId: '',
        roomRent: '',
        builtUpArea: '',
        roomArea: '',
        feeCoefficient: '1.00',
        remark: '',
        roomSubType: '120'
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleFloorChange(floor) {
      this.formData.floorId = floor.floorId
      this.$refs.form.validateField('floorId')
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveData()
        }
      })
    },
    saveData() {
      saveShops(this.formData)
        .then(res => {
          if (res.code == 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } else {
            this.$message.error(res.msg)
          }
        })
        .catch(error => {
          console.error(error)
          this.$message.error(this.$t('common.saveFailed'))
        })
    },
    async searchFloors() {
      const { apiFloorDataVoList } = await getFloors({
        communityId: getCommunityId(),
        page: 1,
        row: 100
      })
      this.floors = apiFloorDataVoList
    },
  }
}
</script>