<template>
  <el-dialog
    :title="$t('addParkingBox.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item
        :label="$t('addParkingBox.boothName')"
        prop="boxName"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.boothName'), trigger: 'blur' },
          { max: 64, message: $t('addParkingBox.maxLength'), trigger: 'blur' }
        ]"
      >
        <el-input
          v-model="formData.boxName"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.boothName')"
        />
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.parkingLot')"
        prop="paId"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.parkingLot'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.paId"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.parkingLot')"
          style="width:100%"
        >
          <el-option
            v-for="item in parkingAreas"
            :key="item.paId"
            :label="item.num"
            :value="item.paId"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.tempCarIn')"
        prop="tempCarIn"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.tempCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.tempCarIn"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.tempCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.tempAuth')"
        prop="tempAuth"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.tempAuth'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.tempAuth"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.tempAuth')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.fee')"
        prop="fee"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.fee'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.fee"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.fee')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.blueCarIn')"
        prop="blueCarIn"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.blueCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.blueCarIn"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.blueCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('addParkingBox.canEnter')" />
          <el-option value="N" :label="$t('addParkingBox.cannotEnter')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('addParkingBox.yelowCarIn')"
        prop="yelowCarIn"
        :rules="[
          { required: true, message: $t('addParkingBox.required') + $t('addParkingBox.yelowCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.yelowCarIn"
          :placeholder="$t('addParkingBox.required') + $t('addParkingBox.yelowCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('addParkingBox.canExit')" />
          <el-option value="N" :label="$t('addParkingBox.cannotExit')" />
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('addParkingBox.remark')">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :placeholder="$t('addParkingBox.optional') + $t('addParkingBox.remark')"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('addParkingBox.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('addParkingBox.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addParkingBox, getParkingAreaList } from '@/api/car/parkingBoxManageApi'

export default {
  name: 'AddParkingBox',
  data() {
    return {
      visible: false,
      formData: {
        boxName: '',
        paId: '',
        tempCarIn: '',
        tempAuth: '',
        fee: '',
        blueCarIn: '',
        yelowCarIn: '',
        remark: ''
      },
      parkingAreas: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadParkingAreas()
    },
    
    async loadParkingAreas() {
      try {
        this.parkingAreas = await getParkingAreaList()
      } catch (error) {
        console.error('获取停车场列表失败:', error)
        this.$message.error(this.$t('parkingBoxManage.fetchParkingAreaError'))
      }
    },
    
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        boxName: '',
        paId: '',
        tempCarIn: '',
        tempAuth: '',
        fee: '',
        blueCarIn: '',
        yelowCarIn: '',
        remark: ''
      }
    },
    
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await addParkingBox(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加岗亭失败:', error)
            this.$message.error(this.$t('parkingBoxManage.addError'))
          }
        }
      })
    }
  }
}
</script>