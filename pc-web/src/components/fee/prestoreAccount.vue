<template>
  <el-dialog
    :title="$t('prestoreAccount.title')"
    :visible.sync="dialogVisible"
    width="50%"
  >
    <el-form :model="prestoreAccountInfo" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('prestoreAccount.accountType')" prop="acctType" required>
        <el-select
          v-model="prestoreAccountInfo.acctType"
          style="width: 100%"
        >
          <el-option
            :label="$t('prestoreAccount.selectAccountType')"
            value=""
            disabled
          ></el-option>
          <el-option
            v-for="(type,index) in prestoreAccountInfo.acctTypes"
            :key="index"
            :label="type.name"
            :value="type.statusCd"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.ownerPhone')" prop="tel" required>
        <el-input
          v-model="prestoreAccountInfo.tel"
          :placeholder="$t('prestoreAccount.ownerPhonePlaceholder')"
          @blur="_changeTel"
        ></el-input>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.ownerName')" prop="ownerId" required>
        <el-select
          v-model="prestoreAccountInfo.ownerId"
          style="width: 100%"
          @change="_loadOwnerRooms"
        >
          <el-option
            :label="$t('prestoreAccount.selectOwner')"
            value=""
            disabled
          ></el-option>
          <el-option
            v-for="(owner,index) in prestoreAccountInfo.owners"
            :key="index"
            :label="owner.name"
            :value="owner.ownerId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="prestoreAccountInfo.acctType == '2004' || prestoreAccountInfo.acctType == '2005'"
        :label="$t('prestoreAccount.deductionRoom')"
        prop="roomId"
        required
      >
        <el-select v-model="prestoreAccountInfo.roomId" style="width: 100%">
          <el-option
            :label="$t('prestoreAccount.selectRoom')"
            value=""
            disabled
          ></el-option>
          <el-option
            v-for="(room,index) in prestoreAccountInfo.rooms"
            :key="index"
            :label="room.roomName"
            :value="room.roomId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.prestoreAmount')" prop="amount" required>
        <el-input
          v-model="prestoreAccountInfo.amount"
          :placeholder="$t('prestoreAccount.prestoreAmountPlaceholder')"
        ></el-input>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.paymentMethod')" prop="primeRate" required>
        <el-select v-model="prestoreAccountInfo.primeRate" style="width: 100%">
          <el-option
            :label="$t('prestoreAccount.selectPaymentMethod')"
            value=""
            disabled
          ></el-option>
          <el-option
            v-for="(item,index) in prestoreAccountInfo.primeRates"
            v-if="item.statusCd != '5' && item.statusCd != '6'"
            :key="index"
            :label="item.name"
            :value="item.statusCd"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.remark')" prop="remark">
        <el-input
          type="textarea"
          :rows="3"
          v-model="prestoreAccountInfo.remark"
          :placeholder="$t('prestoreAccount.remarkPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('prestoreAccount.cancel') }}</el-button>
      <el-button type="primary" @click="savePrestoreAccountInfo">{{ $t('prestoreAccount.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { ownerPrestoreAccount, queryOwners, queryRoomsByOwner } from '@/api/fee/prestoreAccountApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PrestoreAccount',
  data() {
    return {
      dialogVisible: false,
      prestoreAccountInfo: {
        tel: '',
        ownerId: '',
        owners: [],
        amount: '',
        remark: '',
        acctTypes: [],
        acctType: '2003',
        primeRate: '',
        primeRates: [],
        rooms: [],
        roomId: '',
      },
      communityId: '',
      rules: {
        acctType: [{ required: true, message: this.$t('prestoreAccount.selectAccountType'), trigger: 'change' }],
        tel: [{ required: true, message: this.$t('prestoreAccount.ownerPhonePlaceholder'), trigger: 'blur' }],
        ownerId: [{ required: true, message: this.$t('prestoreAccount.selectOwner'), trigger: 'change' }],
        roomId: [{ required: true, message: this.$t('prestoreAccount.selectRoom'), trigger: 'change' }],
        amount: [{ required: true, message: this.$t('prestoreAccount.prestoreAmountPlaceholder'), trigger: 'blur' }],
        primeRate: [{ required: true, message: this.$t('prestoreAccount.selectPaymentMethod'), trigger: 'change' }],
        remark: [{ max: 200, message: this.$t('prestoreAccount.validateRemark'), trigger: 'blur' }]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initPrimeRates()
  },
  methods: {
    open(params) {
      this.dialogVisible = true
      this._initAcctTypes()
      
      if (params) {
        this.prestoreAccountInfo.tel = params.tel || ''
        this.prestoreAccountInfo.ownerId = params.ownerId || ''
        this.prestoreAccountInfo.acctType = params.acctType || '2003'
        this.prestoreAccountInfo.roomId = params.roomId || ''
        
        if (params.tel) {
          this._changeTel()
          this._loadOwnerRooms()
        }
      }
    },
    _initPrimeRates() {
      getDict('pay_fee_detail', 'prime_rate').then(data => {
        this.prestoreAccountInfo.primeRates = data
      })
    },
    _initAcctTypes() {
      getDict('account', 'acct_type').then(data => {
        this.prestoreAccountInfo.acctTypes = data
      })
    },
    prestoreAccountValidate() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    savePrestoreAccountInfo() {
      this.prestoreAccountValidate().then(valid => {
        if (valid) {
          const params = {
            ...this.prestoreAccountInfo,
            communityId: this.communityId
          }

          ownerPrestoreAccount(params).then(response => {
            if (response.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.dialogVisible = false
              this.clearPrestoreAccountInfo()
              this.$emit('save-success')
            } else {
              this.$message.error(response.msg)
            }
          }).catch(error => {
            console.error('请求失败:', error)
            this.$message.error(this.$t('prestoreAccount.saveFailed'))
          })
        }
      })
    },
    clearPrestoreAccountInfo() {
      const primeRates = this.prestoreAccountInfo.primeRates
      this.prestoreAccountInfo = {
        tel: '',
        ownerId: '',
        owners: [],
        amount: '',
        remark: '',
        acctTypes: [],
        acctType: '2003',
        primeRate: '',
        primeRates: primeRates,
        rooms: [],
        roomId: '',
      }
      // 重置表单验证状态
      this.$refs.form && this.$refs.form.resetFields()
    },
    _changeTel() {
      if (!this.prestoreAccountInfo.tel) return

      const params = {
        communityId: this.communityId,
        row: 50,
        page: 1,
        link: this.prestoreAccountInfo.tel,
        ownerTypeCd: '1001'
      }

      queryOwners(params).then(response => {
        this.prestoreAccountInfo.owners = response.data
      }).catch(error => {
        console.error('请求失败:', error)
        this.prestoreAccountInfo.owners = []
      })
    },
    _loadOwnerRooms() {
      if (!this.prestoreAccountInfo.ownerId) return

      const params = {
        communityId: this.communityId,
        row: 50,
        page: 1,
        ownerId: this.prestoreAccountInfo.ownerId,
      }

      queryRoomsByOwner(params).then(response => {
        this.prestoreAccountInfo.rooms = response.rooms
      }).catch(error => {
        console.error('请求失败:', error)
        this.prestoreAccountInfo.rooms = []
      })
    }
  }
}
</script>