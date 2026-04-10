<template>
  <el-dialog :title="$t('addReserveServicePerson.reserveService')" :visible.sync="dialogVisible" width="70%"
    @close="handleClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.personName')" prop="personName">
            <el-input v-model="form.personName" :placeholder="$t('addReserveServicePerson.personNamePlaceholder')">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.personTel')" prop="personTel">
            <el-input v-model="form.personTel" :placeholder="$t('addReserveServicePerson.personTelPlaceholder')">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.payWay')" prop="payWay">
            <el-select v-model="form.payWay" :placeholder="$t('addReserveServicePerson.payWayPlaceholder')"
              style="width:100%">
              <el-option :label="$t('addReserveServicePerson.cash')" value="1">
              </el-option>
              <el-option :label="$t('addReserveServicePerson.wechat')" value="2">
              </el-option>
              <el-option :label="$t('addReserveServicePerson.alipay')" value="3">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.receivableAmount')">
            <el-input v-model="form.receivableAmount" disabled
              :placeholder="$t('addReserveServicePerson.receivableAmountPlaceholder')">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.receivedAmount')" prop="receivedAmount">
            <el-input v-model="form.receivedAmount"
              :placeholder="$t('addReserveServicePerson.receivedAmountPlaceholder')">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.appointmentTime')" prop="appointmentTime">
            <el-date-picker v-model="form.appointmentTime" type="date"
              :placeholder="$t('addReserveServicePerson.appointmentTimePlaceholder')" style="width:100%">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveServicePerson.openTime')" prop="openTime">
            <el-select v-model="form.openTime" :placeholder="$t('addReserveServicePerson.openTimePlaceholder')"
              style="width:100%">
              <template v-for="(item, index) in form.openTimes">  
                <el-option v-if="item.isOpen == 'Y'" :key="index" :label="item.hours + $t('addReserveServicePerson.hour')"
                  :value="item.hours">
                </el-option>
              </template>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item :label="$t('addReserveServicePerson.remark')" prop="remark">
        <el-input type="textarea" :rows="3" v-model="form.remark"
          :placeholder="$t('addReserveServicePerson.remarkPlaceholder')">
        </el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveReserveGoodsOrder, listReserveParams } from '@/api/scm/reserveServiceManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddReserveServicePerson',
  data() {
    return {
      dialogVisible: false,
      form: {
        goodsId: '',
        paramsId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        receivableAmount: '',
        receivedAmount: '',
        payWay: '',
        state: 'S',
        remark: '',
        openTime: '',
        price: 0.0,
        hoursMaxQuantity: 1,
        quantity: 1,
        type: '2002',
        openTimes: [],
        communityId: '',
        times: []
      },
      rules: {
        personName: [
          { required: true, message: this.$t('addReserveServicePerson.personNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('addReserveServicePerson.personNameMaxLength'), trigger: 'blur' }
        ],
        personTel: [
          { required: true, message: this.$t('addReserveServicePerson.personTelRequired'), trigger: 'blur' },
          { max: 11, message: this.$t('addReserveServicePerson.personTelMaxLength'), trigger: 'blur' }
        ],
        appointmentTime: [
          { required: true, message: this.$t('addReserveServicePerson.appointmentTimeRequired'), trigger: 'blur' }
        ],
        receivableAmount: [
          { required: true, message: this.$t('addReserveServicePerson.receivableAmountRequired'), trigger: 'blur' }
        ],
        receivedAmount: [
          { required: true, message: this.$t('addReserveServicePerson.receivedAmountRequired'), trigger: 'blur' }
        ],
        payWay: [
          { required: true, message: this.$t('addReserveServicePerson.payWayRequired'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('addReserveServicePerson.remarkRequired'), trigger: 'blur' }
        ],
        openTime: [
          { required: true, message: this.$t('addReserveServicePerson.openTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      Object.assign(this.form, data)
      this.form.communityId = getCommunityId()
      this._computeMoney()
      this._listReserveParams()
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.form = {
        goodsId: '',
        paramsId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        receivableAmount: '',
        receivedAmount: '',
        payWay: '',
        state: 'S',
        remark: '',
        openTime: '',
        price: 0.0,
        hoursMaxQuantity: 1,
        quantity: 1,
        type: '2002',
        openTimes: [],
        communityId: '',
        times: []
      }
    },
    async _listReserveParams() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.form.communityId,
          paramsId: this.form.paramsId
        }
        const { data } = await listReserveParams(params)
        if (data && data.length > 0) {
          this.form.openTimes = data[0].openTimes
        }
      } catch (error) {
        console.error('获取预约参数失败:', error)
      }
    },
    _computeMoney() {
      const money = parseFloat(this.form.price) * parseFloat(this.form.quantity)
      this.form.receivableAmount = money.toFixed(2)
      this.form.receivedAmount = money.toFixed(2)
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            this.form.times = [{
              hours: this.form.openTime,
              quantity: this.form.quantity
            }]

            await saveReserveGoodsOrder(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('保存预约服务失败:', error)
            this.$message.error(this.$t('common.saveFailed'))
          }
        }
      })
    }
  }
}
</script>