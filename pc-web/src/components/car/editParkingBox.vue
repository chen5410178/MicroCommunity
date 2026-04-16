<template>
  <el-dialog
    :title="$t('editParkingBox.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item
        :label="$t('editParkingBox.boothName')"
        prop="boxName"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.boothName'), trigger: 'blur' },
          { max: 64, message: $t('editParkingBox.maxLength'), trigger: 'blur' }
        ]"
      >
        <el-input
          v-model="formData.boxName"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.boothName')"
        />
      </el-form-item>
      
      <el-form-item
        :label="$t('editParkingBox.parkingLot')"
        prop="paId"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.parkingLot'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.paId"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.parkingLot')"
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
        :label="$t('editParkingBox.tempCarIn')"
        prop="tempCarIn"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.tempCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.tempCarIn"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.tempCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('editParkingBox.tempAuth')"
        prop="tempAuth"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.tempAuth'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.tempAuth"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.tempAuth')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('editParkingBox.fee')"
        prop="fee"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.fee'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.fee"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.fee')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
          <el-option value="N" :label="$t('parkingBoxManage.no')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('editParkingBox.blueCarIn')"
        prop="blueCarIn"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.blueCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.blueCarIn"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.blueCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('editParkingBox.canEnter')" />
          <el-option value="N" :label="$t('editParkingBox.cannotEnter')" />
        </el-select>
      </el-form-item>
      
      <el-form-item
        :label="$t('editParkingBox.yelowCarIn')"
        prop="yelowCarIn"
        :rules="[
          { required: true, message: $t('editParkingBox.required') + $t('editParkingBox.yelowCarIn'), trigger: 'change' }
        ]"
      >
        <el-select
          v-model="formData.yelowCarIn"
          :placeholder="$t('editParkingBox.required') + $t('editParkingBox.yelowCarIn')"
          style="width:100%"
        >
          <el-option value="Y" :label="$t('editParkingBox.canExit')" />
          <el-option value="N" :label="$t('editParkingBox.cannotExit')" />
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('editParkingBox.remark')">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :placeholder="$t('editParkingBox.optional') + $t('editParkingBox.remark')"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('editParkingBox.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('editParkingBox.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateParkingBox, getParkingAreaList } from '@/api/car/parkingBoxManageApi'

export default {
  name: 'EditParkingBox',
  data() {
    return {
      visible: false,
      formData: {
        boxId: '',
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
    open(row) {
      this.formData = { ...row }
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
    },
    
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateParkingBox(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('更新岗亭失败:', error)
            this.$message.error(this.$t('parkingBoxManage.updateError'))
          }
        }
      })
    }
  }
}
</script>