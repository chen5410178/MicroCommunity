<template>
  <el-dialog :title="$t('shops.edit') + $t('shops.shopInfo')" :visible.sync="dialogVisible" width="50%">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('shops.shopId')">
        <el-input v-model="formData.roomId" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.shopNumber')" prop="roomNum">
        <el-input v-model="formData.roomNum" :placeholder="$t('shops.shopNumberRequired')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.shopFloor')" prop="layer">
        <el-input v-model="formData.layer" :placeholder="$t('shops.floorRequired')"></el-input>
      </el-form-item>
      <el-form-item v-if="formData.state === '2006'" :label="$t('shops.leaseStart')" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="date" :placeholder="$t('shops.leaseStartRequired')"
          value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item v-if="formData.state === '2006'" :label="$t('shops.leaseEnd')" prop="endTime">
        <el-date-picker v-model="formData.endTime" type="date" :placeholder="$t('shops.leaseEndRequired')"
          value-format="yyyy-MM-dd" :picker-options="endDateOptions" style="width: 100%"></el-date-picker>
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

      <!-- 动态属性 -->
      <div v-for="(item, index) in formData.attrs" :key="index">
        <el-form-item v-if="item.specType === '2233'" :label="item.specName">
          <el-input v-model="item.value" :placeholder="item.specHoldplace"></el-input>
        </el-form-item>
        <el-form-item v-if="item.specType === '3344'" :label="item.specName">
          <el-select v-model="item.value" :placeholder="item.specHoldplace" style="width: 100%">
            <el-option v-for="option in item.values" :key="option.value" :label="option.valueName"
              :value="option.value"></el-option>
          </el-select>
        </el-form-item>
      </div>

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
import { updateShops } from '@/api/room/shopsApi'

export default {
  name: 'EditShops',
  data() {
    const validateEndDate = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('shops.leaseEndRequired')))
      } else if (new Date(value) <= new Date(this.formData.startTime)) {
        callback(new Error(this.$t('shops.leaseEndRequired')))
      } else {
        callback()
      }
    }

    return {
      dialogVisible: false,
      formData: {
        roomId: '',
        roomNum: '',
        layer: '',
        builtUpArea: '',
        feeCoefficient: '',
        remark: '',
        roomRent: '',
        roomArea: '',
        startTime: '',
        endTime: '',
        state: '',
        attrs: []
      },
      rules: {
        roomNum: [
          { required: true, message: this.$t('shops.shopNumberRequired'), trigger: 'blur' }
        ],
        layer: [
          { required: true, message: this.$t('shops.floorRequired'), trigger: 'blur' }
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
        ],
        startTime: [
          { required: true, message: this.$t('shops.leaseStartRequired'), trigger: 'change' }
        ],
        endTime: [
          { validator: validateEndDate, trigger: 'change' }
        ]
      },
      endDateOptions: {
        disabledDate: (time) => {
          if (this.formData.startTime) {
            return time.getTime() <= new Date(this.formData.startTime).getTime()
          }
          return false
        }
      }
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.resetForm()
      this.formData = { ...row }

      // 处理日期格式
      if (this.formData.startTime) {
        this.formData.startTime = this.formData.startTime.split(' ')[0]
      }
      if (this.formData.endTime) {
        this.formData.endTime = this.formData.endTime.split(' ')[0]
      }

      // 加载属性数据
      this.loadAttributes()
    },
    resetForm() {
      this.formData = {
        roomId: '',
        roomNum: '',
        layer: '',
        builtUpArea: '',
        feeCoefficient: '',
        remark: '',
        roomRent: '',
        roomArea: '',
        startTime: '',
        endTime: '',
        state: '',
        attrs: []
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    loadAttributes() {
      // 模拟加载属性数据
      this.formData.attrs = [
      ]
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveData()
        }
      })
    },
    saveData() {
      updateShops(this.formData)
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
    }
  }
}
</script>