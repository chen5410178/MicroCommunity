<template>
  <div class="edit-equipment-account-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editEquipmentAccount.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form label-position="right" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.machineName')">
                  <el-input v-model.trim="editEquipmentAccountInfo.machineName"
                    :placeholder="$t('editEquipmentAccount.machineNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.machineCode')">
                  <el-input v-model.trim="editEquipmentAccountInfo.machineCode"
                    :placeholder="$t('editEquipmentAccount.machineCodePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.importanceLevel')">
                  <el-select v-model="editEquipmentAccountInfo.importanceLevel"
                    :placeholder="$t('editEquipmentAccount.importanceLevelPlaceholder')" style="width:100%">
                    <el-option v-for="(item, index) in editEquipmentAccountInfo.importanceLevels" :key="index"
                      :label="item.name" :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.state')">
                  <el-select v-model="editEquipmentAccountInfo.state"
                    :placeholder="$t('editEquipmentAccount.statePlaceholder')" style="width:100%">
                    <el-option v-for="(item, index) in editEquipmentAccountInfo.useStatus" :key="index" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.brand')">
                  <el-input v-model.trim="editEquipmentAccountInfo.brand"
                    :placeholder="$t('editEquipmentAccount.brandPlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.model')">
                  <el-input v-model.trim="editEquipmentAccountInfo.model"
                    :placeholder="$t('editEquipmentAccount.modelPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.netWorth')">
                  <el-input v-model.trim="editEquipmentAccountInfo.netWorth"
                    :placeholder="$t('editEquipmentAccount.netWorthPlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.locationDetail')">
                  <el-input v-model.trim="editEquipmentAccountInfo.locationDetail"
                    :placeholder="$t('editEquipmentAccount.locationDetailPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.firstEnableTime')">
                  <el-date-picker v-model="editEquipmentAccountInfo.firstEnableTime" type="date" value-format="yyyy-MM-dd"
                    :placeholder="$t('editEquipmentAccount.firstEnableTimePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.warrantyDeadline')">
                  <el-date-picker v-model="editEquipmentAccountInfo.warrantyDeadline" type="date" value-format="yyyy-MM-dd"
                    :placeholder="$t('editEquipmentAccount.warrantyDeadlinePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.usefulLife')">
                  <el-input v-model.trim="editEquipmentAccountInfo.usefulLife"
                    :placeholder="$t('editEquipmentAccount.usefulLifePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.purchasePrice')">
                  <el-input v-model.trim="editEquipmentAccountInfo.purchasePrice"
                    :placeholder="$t('editEquipmentAccount.purchasePricePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.useUserName')">
                  <el-input v-model.trim="editEquipmentAccountInfo.useUserName"
                    :placeholder="$t('editEquipmentAccount.useUserNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editEquipmentAccount.chargeUseName')">
                  <el-input v-model.trim="editEquipmentAccountInfo.chargeUseName"
                    :placeholder="$t('editEquipmentAccount.chargeUseNamePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('editEquipmentAccount.remark')">
                  <el-input v-model.trim="editEquipmentAccountInfo.remark" type="textarea"
                    :placeholder="$t('editEquipmentAccount.remarkPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24" style="text-align: right">
                <el-button type="warning" @click="_goBack">
                  {{ $t('common.cancel') }}
                </el-button>
                <el-button type="primary" @click="saveEquipmentAccountInfo">
                  {{ $t('common.save') }}
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { updateEquipmentAccount, listEquipmentAccount } from '@/api/machine/editEquipmentAccountApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditEquipmentAccountList',
  data() {
    return {
      editEquipmentAccountInfo: {
        machineId: '',
        machineName: '',
        machineCode: '',
        brand: '',
        model: '',
        typeId: '',
        locationDetail: '',
        locationObjId: '',
        locationObjName: '',
        firstEnableTime: '',
        warrantyDeadline: '',
        usefulLife: '',
        importanceLevel: '',
        importanceLevels: [],
        state: '',
        purchasePrice: '',
        netWorth: '',
        useOrgId: '',
        useOrgName: '',
        useUserId: '',
        useUserName: '',
        useUseTel: '',
        chargeOrgId: '',
        chargeOrgName: '',
        chargeOrgTel: '',
        chargeUseId: '',
        chargeUseName: '',
        remark: '',
        useStatus: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.editEquipmentAccountInfo.machineId = this.$route.query.machineId
    this.getDictData()
    this._listEquipmentAccounts()
  },
  methods: {
    async getDictData() {
      try {
        const importanceLevels = await getDict('equipment_account', 'importance_level')
        const useStatus = await getDict('equipment_account', 'state')
        this.editEquipmentAccountInfo.importanceLevels = importanceLevels
        this.editEquipmentAccountInfo.useStatus = useStatus
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listEquipmentAccounts() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          machineId: this.editEquipmentAccountInfo.machineId
        }
        const { data } = await listEquipmentAccount(params)
        Object.assign(this.editEquipmentAccountInfo, data[0])
      } catch (error) {
        console.error('获取设备信息失败:', error)
      }
    },
    editEquipmentAccountValidate() {
      // 这里应该添加表单验证逻辑
      return true
    },
    async saveEquipmentAccountInfo() {
      if (!this.editEquipmentAccountValidate()) {
        return
      }
      try {
        this.editEquipmentAccountInfo.communityId = this.communityId
        const res = await updateEquipmentAccount(this.editEquipmentAccountInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('保存设备信息失败:', error)
        this.$message.error(this.$t('common.updateError'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-equipment-account-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
}
</style>