<template>
  <el-dialog :title="$t('addCommunitySpacePerson.title')" :visible.sync="visible" width="800px" @close="closeDialog">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.personName')" prop="personName">
            <el-input v-model="formData.personName" :placeholder="$t('addCommunitySpacePerson.requiredPersonName')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.personTel')" prop="personTel">
            <el-input v-model="formData.personTel" :placeholder="$t('addCommunitySpacePerson.requiredPersonTel')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.receivableAmount')" prop="receivableAmount">
            <el-input v-model="formData.receivableAmount"
              :placeholder="$t('addCommunitySpacePerson.requiredReceivableAmount')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.receivedAmount')" prop="receivedAmount">
            <el-input v-model="formData.receivedAmount"
              :placeholder="$t('addCommunitySpacePerson.requiredReceivedAmount')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.payWay')" prop="payWay">
            <el-select v-model="formData.payWay" :placeholder="$t('addCommunitySpacePerson.selectPayWay')"
              style="width:100%">
              <el-option v-for="item in payOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addCommunitySpacePerson.appointmentTime')" prop="appointmentTime">
            <el-input v-model="formData.appointmentTime" disabled
              :placeholder="$t('addCommunitySpacePerson.requiredAppointmentTime')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('addCommunitySpacePerson.remark')" prop="remark">
            <el-input v-model="formData.remark" type="textarea"
              :placeholder="$t('addCommunitySpacePerson.requiredRemark')" :rows="3" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveCommunitySpacePerson } from '@/api/community/reportCommunitySpaceApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'


export default {
  name: 'AddCommunitySpacePerson',
  props: {
    communityId: {
      type: String,
      default: () => getCommunityId()
    }
  },
  data() {
    return {
      visible: false,
      formData: {
        spaceId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        receivableAmount: '',
        receivedAmount: '',
        payWay: '',
        remark: '',
        openTime: '',
        communityId: this.communityId,
        state: 'S'
      },
      rules: {
        personName: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredPersonName'), trigger: 'blur' },
          { max: 64, message: this.$t('addCommunitySpacePerson.maxPersonName'), trigger: 'blur' }
        ],
        personTel: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredPersonTel'), trigger: 'blur' },
          { max: 11, message: this.$t('addCommunitySpacePerson.maxPersonTel'), trigger: 'blur' }
        ],
        appointmentTime: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredAppointmentTime'), trigger: 'blur' }
        ],
        receivableAmount: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredReceivableAmount'), trigger: 'blur' },
          { max: 10, message: this.$t('addCommunitySpacePerson.maxReceivableAmount'), trigger: 'blur' }
        ],
        receivedAmount: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredReceivedAmount'), trigger: 'blur' },
          { max: 10, message: this.$t('addCommunitySpacePerson.maxReceivedAmount'), trigger: 'blur' }
        ],
        payWay: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredPayWay'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('addCommunitySpacePerson.requiredRemark'), trigger: 'blur' },
          { max: 512, message: this.$t('addCommunitySpacePerson.maxRemark'), trigger: 'blur' }
        ]
      },
      payOptions: [
        { value: '1', label: this.$t('addCommunitySpacePerson.cash') },
        { value: '2', label: this.$t('addCommunitySpacePerson.wechat') },
        { value: '3', label: this.$t('addCommunitySpacePerson.alipay') }
      ]
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      Object.assign(this.formData, data)
      this.formData.appointmentTime = dateFormat(this.formData.appointmentTime) + ` ${this.formData.openTime}:00`
      this.visible = true
    },
    closeDialog() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.formData = {
        spaceId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        receivableAmount: '',
        receivedAmount: '',
        payWay: '',
        remark: '',
        openTime: '',
        communityId: this.communityId,
        state: 'S'
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveData()
        }
      })
    },
    async saveData() {
      try {
        await saveCommunitySpacePerson(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveFailed'))
      }
    }
  }
}
</script>