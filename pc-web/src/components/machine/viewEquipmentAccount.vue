<template>
  <el-dialog
    :title="$t('equipmentAccount.viewEquipment')"
    :visible.sync="dialogVisible"
    width="70%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="150px" :disabled="true">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.machineName')" prop="machineName">
            <el-input v-model="formData.machineName"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.machineCode')" prop="machineCode">
            <el-input v-model="formData.machineCode"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.brand')" prop="brand">
            <el-input v-model="formData.brand"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.model')" prop="model">
            <el-input v-model="formData.model"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.locationDetail')" prop="locationDetail">
            <el-input v-model="formData.locationDetail"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.firstEnableTime')" prop="firstEnableTime">
            <el-input v-model="formData.firstEnableTime"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.warrantyDeadline')" prop="warrantyDeadline">
            <el-input v-model="formData.warrantyDeadline"></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.usefulLife')" prop="usefulLife">
            <el-input v-model="formData.usefulLife">
              <template slot="append">{{ $t('common.month') }}</template>
            </el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.importanceLevel')" prop="importanceLevel">
            <el-input v-model="formData.levelName"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.state')" prop="state">
            <el-input v-model="formData.stateName"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.purchasePrice')" prop="purchasePrice">
            <el-input v-model="formData.purchasePrice">
              <template slot="append">{{ $t('common.yuan') }}</template>
            </el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.netWorth')" prop="netWorth">
            <el-input v-model="formData.netWorth">
              <template slot="append">{{ $t('common.yuan') }}</template>
            </el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.useOrgName')" prop="useOrgName">
            <el-input v-model="formData.useOrgName"></el-input>
          </el-form-item>
          
          <el-form-item :label="$t('equipmentAccount.useUserName')" prop="useUserName">
            <el-input v-model="formData.useUserName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.chargeOrgName')" prop="chargeOrgName">
            <el-input v-model="formData.chargeOrgName"></el-input>
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item :label="$t('equipmentAccount.chargeUseName')" prop="chargeUseName">
            <el-input v-model="formData.chargeUseName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item :label="$t('equipmentAccount.remark')" prop="remark">
        <el-input 
          v-model="formData.remark" 
          type="textarea"
          :rows="3">
        </el-input>
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.close') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getEquipmentAccountDetail } from '@/api/machine/equipmentAccountApi'

export default {
  name: 'ViewEquipmentAccount',
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
        usefulLife: '',
        importanceLevel: '',
        levelName: '',
        state: '',
        stateName: '',
        purchasePrice: '',
        netWorth: '',
        useOrgName: '',
        useUserName: '',
        chargeOrgName: '',
        chargeUseName: '',
        remark: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    async open(machineId) {
      try {
        const params = {
          machineId,
          communityId: this.communityId
        }
        const data = await getEquipmentAccountDetail(params)
        this.formData = { ...data }
        this.dialogVisible = true
      } catch (error) {
        console.error('获取设备详情失败:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.el-form-item {
  margin-bottom: 15px;
}
</style>