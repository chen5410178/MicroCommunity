<template>
  <el-dialog :title="$t('repairSetting.addTitle')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form ref="form" :model="formData" :rules="rules" label-width="150px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.typeName')" prop="repairTypeName">
            <el-input v-model="formData.repairTypeName" :placeholder="$t('repairSetting.repairTypeNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.settingType')" prop="repairSettingType">
            <el-select v-model="formData.repairSettingType"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.repairSettingTypePlaceholder')"
              style="width:100%">
              <el-option :label="$t('repairSetting.cleaningOrder')" value="100" />
              <el-option :label="$t('repairSetting.repairOrder')" value="200" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.dispatchMethod')" prop="repairWay">
            <el-select v-model="formData.repairWay"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.repairWayPlaceholder')" style="width:100%">
              <el-option :label="$t('repairSetting.grabOrder')" value="100" />
              <el-option :label="$t('repairSetting.assign')" value="200" />
              <el-option :label="$t('repairSetting.polling')" value="300" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.publicArea')" prop="publicArea">
            <el-select v-model="formData.publicArea"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.publicAreaPlaceholder')"
              style="width:100%">
              <el-option :label="$t('repairSetting.nonHouse')" value="T" />
              <el-option :label="$t('repairSetting.house')" value="F" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.ownerDisplay')" prop="isShow">
            <el-select v-model="formData.isShow" :placeholder="$t('repairSetting.ownerDisplayPlaceholder')"
              style="width:100%">
              <el-option :label="$t('repairSetting.yes')" value="Y" />
              <el-option :label="$t('repairSetting.no')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.notificationMethod')" prop="notifyWay">
            <el-select v-model="formData.notifyWay" :placeholder="$t('repairSetting.notificationMethodPlaceholder')"
              style="width:100%">
              <el-option :label="$t('repairSetting.sms')" value="SMS" />
              <el-option :label="$t('repairSetting.wechat')" value="WECHAT" />
              <el-option :label="$t('repairSetting.wechatWorkLicense')" value="WORK_LICENSE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.processingTime')" prop="doTime">
            <el-input v-model.number="formData.doTime" type="number"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.processingTimePlaceholder')">
              <template slot="append">{{ $t('repairSetting.processingTimeUnit') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.timeoutWarning')" prop="warningTime">
            <el-input v-model.number="formData.warningTime" type="number"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.timeoutWarningPlaceholder')">
              <template slot="append">{{ $t('repairSetting.timeoutWarningUnit') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('repairSetting.returnVisitSetting')" prop="returnVisitFlag">
            <el-select v-model="formData.returnVisitFlag"
              :placeholder="$t('repairSetting.required') + $t('repairSetting.returnVisitFlagPlaceholder')"
              style="width:100%">
              <el-option :label="$t('repairSetting.noFollowUp')" value="001" />
              <el-option :label="$t('repairSetting.followUpAfterEvaluation')" value="002" />
              <el-option :label="$t('repairSetting.followUp')" value="003" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('repairSetting.explanation')" prop="remark">
            <el-input v-model="formData.remark" type="textarea" :rows="3"
              :placeholder="$t('repairSetting.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('repairSetting.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('repairSetting.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveRepairSetting } from '@/api/work/repairSettingApi'

export default {
  name: 'AddRepairSetting',
  data() {
    return {
      visible: false,
      formData: {
        repairTypeName: '',
        repairWay: '200',
        repairSettingType: '200',
        remark: '',
        publicArea: 'F',
        payFeeFlag: 'F',
        returnVisitFlag: '003',
        isShow: 'Y',
        doTime: 24,
        warningTime: 30,
        notifyWay: 'WECHAT'
      },
      rules: {
        repairTypeName: [
          { required: true, message: this.$t('repairSetting.repairTypeNamePlaceholder'), trigger: 'blur' },
        ],
        repairSettingType: [
          { required: true, message: this.$t('repairSetting.settingTypeRequired'), trigger: 'change' }
        ],
        repairWay: [
          { required: true, message: this.$t('repairSetting.dispatchMethodRequired'), trigger: 'change' }
        ],
        publicArea: [
          { required: true, message: this.$t('repairSetting.publicAreaRequired'), trigger: 'change' }
        ],
        isShow: [
          { required: true, message: this.$t('repairSetting.ownerDisplayRequired'), trigger: 'change' }
        ],
        notifyWay: [
          { required: true, message: this.$t('repairSetting.notificationMethodRequired'), trigger: 'change' }
        ],
        returnVisitFlag: [
          { required: true, message: this.$t('repairSetting.returnVisitRequired'), trigger: 'change' }
        ],
        doTime: [
          { required: true, message: this.$t('repairSetting.processingTimeRequired'), trigger: 'blur' },
          { type: 'number', min: 1, message: this.$t('repairSetting.processingTimeMin'), trigger: 'blur' }
        ],
        warningTime: [
          { required: true, message: this.$t('repairSetting.timeoutWarningRequired'), trigger: 'blur' },
          { type: 'number', min: 1, message: this.$t('repairSetting.timeoutWarningMin'), trigger: 'blur' }
        ],
        remark: [
          { max: 500, message: this.$t('repairSetting.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },

    closeDialog() {
      this.visible = false
      this.resetForm()
    },

    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        repairTypeName: '',
        repairWay: '200',
        repairSettingType: '200',
        remark: '',
        publicArea: 'F',
        payFeeFlag: 'F',
        returnVisitFlag: '003',
        isShow: 'Y',
        doTime: 24,
        warningTime: 30,
        notifyWay: 'WECHAT'
      }
    },

    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveRepairSetting(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.closeDialog()
          } catch (error) {
            console.error('添加报修设置失败:', error)
            this.$message.error(this.$t('repairSetting.addFailed'))
          }
        }
      })
    }
  }
}
</script>