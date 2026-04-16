<template>
  <div class="add-equipment-account-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addEquipmentAccount.title') }}</span>
      </div>

      <el-form ref="form" :model="addEquipmentAccountInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.machineName')" prop="machineName" required>
                  <el-input v-model.trim="addEquipmentAccountInfo.machineName"
                    :placeholder="$t('addEquipmentAccount.machineNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.machineCode')" prop="machineCode" required>
                  <el-input v-model.trim="addEquipmentAccountInfo.machineCode"
                    :placeholder="$t('addEquipmentAccount.machineCodePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.importanceLevel')" prop="importanceLevel" required>
                  <el-select v-model="addEquipmentAccountInfo.importanceLevel"
                    :placeholder="$t('addEquipmentAccount.importanceLevelPlaceholder')" style="width:100%">
                    <el-option v-for="(item, index) in addEquipmentAccountInfo.importanceLevels" :key="index"
                      :label="item.name" :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.state')" prop="state" required>
                  <el-select v-model="addEquipmentAccountInfo.state"
                    :placeholder="$t('addEquipmentAccount.statePlaceholder')" style="width:100%">
                    <el-option v-for="(item, index) in addEquipmentAccountInfo.useStatus" :key="index"
                      :label="item.name" :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.brand')">
                  <el-input v-model.trim="addEquipmentAccountInfo.brand"
                    :placeholder="$t('addEquipmentAccount.brandPlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.model')">
                  <el-input v-model.trim="addEquipmentAccountInfo.model"
                    :placeholder="$t('addEquipmentAccount.modelPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.netWorth')">
                  <el-input v-model.trim="addEquipmentAccountInfo.netWorth"
                    :placeholder="$t('addEquipmentAccount.netWorthPlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.locationDetail')">
                  <el-input v-model.trim="addEquipmentAccountInfo.locationDetail"
                    :placeholder="$t('addEquipmentAccount.locationDetailPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.firstEnableTime')">
                  <el-date-picker v-model="addEquipmentAccountInfo.firstEnableTime" type="date"
                    value-format="yyyy-MM-dd" :placeholder="$t('addEquipmentAccount.firstEnableTimePlaceholder')"
                    style="width:100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.warrantyDeadline')">
                  <el-date-picker v-model="addEquipmentAccountInfo.warrantyDeadline" type="date"
                    value-format="yyyy-MM-dd" :placeholder="$t('addEquipmentAccount.warrantyDeadlinePlaceholder')"
                    style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.usefulLife')">
                  <el-input v-model.trim="addEquipmentAccountInfo.usefulLife"
                    :placeholder="$t('addEquipmentAccount.usefulLifePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.purchasePrice')">
                  <el-input v-model.trim="addEquipmentAccountInfo.purchasePrice"
                    :placeholder="$t('addEquipmentAccount.purchasePricePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.useUserName')" prop="useUserName" required>
                  <el-input v-model.trim="addEquipmentAccountInfo.useUserName"
                    :placeholder="$t('addEquipmentAccount.useUserNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addEquipmentAccount.chargeUseName')" prop="chargeUseName" required>
                  <el-input v-model.trim="addEquipmentAccountInfo.chargeUseName"
                    :placeholder="$t('addEquipmentAccount.chargeUseNamePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('addEquipmentAccount.remark')">
                  <el-input v-model.trim="addEquipmentAccountInfo.remark" type="textarea"
                    :placeholder="$t('addEquipmentAccount.remarkPlaceholder')" :rows="3" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" style="text-align: right; margin-top: 20px;">
            <el-button type="warning" @click="handleCancel">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="handleSave">
              {{ $t('common.save') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { saveEquipmentAccount } from '@/api/machine/addEquipmentAccountApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddEquipmentAccountList',
  data() {
    return {
      addEquipmentAccountInfo: {
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
      rules: {
        machineName: [{ required: true, message: this.$t('addEquipmentAccount.machineNamePlaceholder'), trigger: 'blur' }],
        machineCode: [{ required: true, message: this.$t('addEquipmentAccount.machineCodePlaceholder'), trigger: 'blur' }],
        importanceLevel: [{ required: true, message: this.$t('addEquipmentAccount.importanceLevelPlaceholder'), trigger: 'change' }],
        state: [{ required: true, message: this.$t('addEquipmentAccount.statePlaceholder'), trigger: 'change' }],
        useUserName: [{ required: true, message: this.$t('addEquipmentAccount.useUserNamePlaceholder'), trigger: 'blur' }],
        chargeUseName: [{ required: true, message: this.$t('addEquipmentAccount.chargeUseNamePlaceholder'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.addEquipmentAccountInfo.typeId = this.$route.query.typeId
      this.communityId = await getCommunityId()

      try {
        const importanceLevels = await getDict('equipment_account', 'importance_level')
        const useStatus = await getDict('equipment_account', 'state')
        this.addEquipmentAccountInfo.importanceLevels = importanceLevels
        this.addEquipmentAccountInfo.useStatus = useStatus
      } catch (error) {
        console.error('Failed to fetch dict data:', error)
      }
    },
    async handleSave() {
      try {
        // 表单验证
        await this.$refs.form.validate()

        const params = {
          ...this.addEquipmentAccountInfo,
          communityId: this.communityId
        }
        const res = await saveEquipmentAccount(params)

        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        if (error === false) {
          // 表单验证失败
          return
        }
        console.error('Save equipment account failed:', error)
        this.$message.error(this.$t('addEquipmentAccount.saveFailed'))
      }
    },
    handleCancel() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-equipment-account-container {
  padding: 20px;

  .box-card {
    width: 100%;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
}
</style>