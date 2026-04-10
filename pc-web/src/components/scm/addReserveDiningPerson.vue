<template>
  <el-dialog :title="$t('addReserveDiningPerson.reserveDining')" :visible.sync="visible" width="70%"
    @close="closeDialog">
    <el-form :model="form" label-width="120px" ref="form">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.personName')" prop="personName" required>
            <el-input v-model="form.personName"
              :placeholder="$t('addReserveDiningPerson.personNamePlaceholder')"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.personTel')" prop="personTel" required>
            <el-input v-model="form.personTel"
              :placeholder="$t('addReserveDiningPerson.personTelPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.payWay')" prop="payWay" required>
            <el-select v-model="form.payWay" :placeholder="$t('addReserveDiningPerson.payWayPlaceholder')"
              style="width:100%">
              <el-option :label="$t('addReserveDiningPerson.cash')" value="1"></el-option>
              <el-option :label="$t('addReserveDiningPerson.wechat')" value="2"></el-option>
              <el-option :label="$t('addReserveDiningPerson.alipay')" value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.quantity')" prop="quantity" required>
            <el-select v-model="form.quantity" :placeholder="$t('addReserveDiningPerson.quantityPlaceholder')"
              @change="_computeMoney" style="width:100%">
              <el-option v-for="item in form.hoursMaxQuantity" :key="item"
                :label="`${item}${$t('addReserveDiningPerson.unit')}`" :value="item"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.receivableAmount')" prop="receivableAmount" required>
            <el-input v-model="form.receivableAmount"
              :placeholder="$t('addReserveDiningPerson.receivableAmountPlaceholder')" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.receivedAmount')" prop="receivedAmount" required>
            <el-input v-model="form.receivedAmount"
              :placeholder="$t('addReserveDiningPerson.receivedAmountPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.appointmentTime')" prop="appointmentTime" required>
            <el-date-picker v-model="form.appointmentTime" type="datetime"
              :placeholder="$t('addReserveDiningPerson.appointmentTimePlaceholder')"
              style="width:100%"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addReserveDiningPerson.openTime')" prop="openTime" required>
            <el-select v-model="form.openTime" :placeholder="$t('addReserveDiningPerson.openTimePlaceholder')"
              style="width:100%">
              <template v-for="item in form.openTimes">
                <el-option v-if="item.isOpen === 'Y'" :key="item.hours"
                  :label="`${item.hours}${$t('addReserveDiningPerson.hour')}`" :value="item.hours"></el-option>
              </template>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item :label="$t('addReserveDiningPerson.remark')" prop="remark" required>
        <el-input type="textarea" v-model="form.remark"
          :placeholder="$t('addReserveDiningPerson.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="save">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveReserveGoodsOrder, listReserveParams } from '@/api/scm/doDiningApi'

export default {
  name: 'AddReserveDiningPerson',
  data() {
    return {
      visible: false,
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
        type: '1001',
        openTimes: [],
        times: [],
        communityId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.resetForm()
      Object.assign(this.form, params)
      this.form.hoursMaxQuantity = parseInt(params.hoursMaxQuantity)
      this._computeMoney()
      this._listReserveParams()
      this.visible = true
    },
    closeDialog() {
      this.visible = false
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
        type: '1001',
        openTimes: [],
        times: [],
        communityId: ''
      }
    },
    save() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        this.form.communityId = getCommunityId()
        this.form.times = [{
          hours: this.form.openTime,
          quantity: this.form.quantity
        }]

        saveReserveGoodsOrder(this.form)
          .then(response => {
            if (response.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.closeDialog()
              this.$emit('success')
            } else {
              this.$message.error(response.msg)
            }
          })
          .catch(error => {
            console.error('请求失败:', error)
          })
      })
    },
    _listReserveParams() {
      const params = {
        page: 1,
        row: 1,
        communityId: getCommunityId(),
        paramsId: this.form.paramsId
      }

      listReserveParams(params)
        .then(response => {
          this.form.openTimes = response.data[0].openTimes
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    _computeMoney() {
      const money = parseFloat(this.form.price) * parseFloat(this.form.quantity)
      this.form.receivableAmount = money.toFixed(2)
      this.form.receivedAmount = money.toFixed(2)
    }
  }
}
</script>