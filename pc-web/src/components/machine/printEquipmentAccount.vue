<template>
  <el-dialog
    :title="$t('equipmentAccount.printLabel')"
    :visible.sync="dialogVisible"
    width="70%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="150px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.machineName')" prop="machineName">
            <el-input v-model="formData.machineName" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.machineCode')" prop="machineCode">
            <el-input v-model="formData.machineCode" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.brand')" prop="brand">
            <el-input v-model="formData.brand" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.model')" prop="model">
            <el-input v-model="formData.model" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.locationDetail')" prop="locationDetail">
            <el-input v-model="formData.locationDetail" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.firstEnableTime')" prop="firstEnableTime">
            <el-input v-model="formData.firstEnableTime" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.warrantyDeadline')" prop="warrantyDeadline">
            <el-input v-model="formData.warrantyDeadline" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.importanceLevel')" prop="importanceLevel">
            <el-input v-model="formData.levelName" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.state')" prop="state">
            <el-input v-model="formData.stateName" :disabled="true"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.purchasePrice')" prop="purchasePrice">
            <el-input v-model="formData.purchasePrice" :disabled="true">
              <template slot="append">{{ $t('common.yuan') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item :label="$t('equipmentAccount.printQty')" prop="printQty">
        <el-input-number 
          v-model="formData.printQty" 
          :min="1" 
          :max="10"
          label="打印数量">
        </el-input-number>
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handlePrint">{{ $t('equipmentAccount.printPreview') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getEquipmentAccountDetail } from '@/api/machine/equipmentAccountApi'

export default {
  name: 'PrintEquipmentAccount',
  data() {
    return {
      dialogVisible: false,
      communityId: '',
      formData: {
        machineId: '',
        machineName: '',
        machineCode: '',
        brand: '',
        model: '',
        locationDetail: '',
        firstEnableTime: '',
        warrantyDeadline: '',
        importanceLevel: '',
        levelName: '',
        state: '',
        stateName: '',
        purchasePrice: '',
        printQty: 1
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    async open(data) {
      try {
        const params = {
          machineId: data.machineId,
          communityId: this.communityId
        }
        const detail = await getEquipmentAccountDetail(params)
        this.formData = { 
          ...detail,
          printQty: 1
        }
        this.dialogVisible = true
      } catch (error) {
        console.error('获取设备详情失败:', error)
      }
    },
    handleClose() {
      this.formData.printQty = 1
      this.$refs.form.resetFields()
    },
    handlePrint() {
      window.open(`/#/pages/property/printEquipmentAccountLabel?machineId=${this.formData.machineId}&qty=${this.formData.printQty}`)
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.el-form-item {
  margin-bottom: 15px;
}
</style>