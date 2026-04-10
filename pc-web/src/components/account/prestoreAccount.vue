<template>
  <el-dialog :title="$t('prestoreAccount.title')" :visible.sync="visible" width="800px" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('prestoreAccount.accountType')" prop="acctType" required>
        <el-select v-model="formData.acctType" class="w-full" :placeholder="$t('prestoreAccount.accountType')">
          <el-option v-for="(type, index) in acctTypes" :key="index" :label="type.name" :value="type.statusCd" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.ownerPhone')" prop="tel" required>
        <el-input v-model="formData.tel" :placeholder="$t('prestoreAccount.ownerPhone')" @blur="handleTelChange" />
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.ownerName')" prop="ownerId" required>
        <el-select v-model="formData.ownerId" class="w-full" :placeholder="$t('prestoreAccount.ownerName')"
          @change="loadOwnerRooms">
          <el-option v-for="owner in owners" :key="owner.ownerId" :label="owner.name" :value="owner.ownerId" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.acctType === '2004' || formData.acctType === '2005'"
        :label="$t('prestoreAccount.deductionRoom')" prop="roomId" required>
        <el-select v-model="formData.roomId" class="w-full" :placeholder="$t('prestoreAccount.deductionRoom')">
          <el-option v-for="room in rooms" :key="room.roomId" :label="room.roomName" :value="room.roomId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.prestoreAmount')" prop="amount" required>
        <el-input v-model="formData.amount" type="number" :placeholder="$t('prestoreAccount.prestoreAmount')" />
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.paymentMethod')" prop="primeRate" required>
        <el-select v-model="formData.primeRate" class="w-full" :placeholder="$t('prestoreAccount.paymentMethod')">
          <template v-for="(item, index) in primeRates">
            <el-option :key="index" v-if="item.statusCd !== '5' && item.statusCd !== '6'" :label="item.name"
              :value="item.statusCd" />
          </template>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount.remark')" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :placeholder="$t('prestoreAccount.remark')" :rows="3" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('prestoreAccount.cancel') }}</el-button>
      <el-button type="primary" @click="handleSave">{{ $t('prestoreAccount.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { ownerPrestoreAccount, queryOwners, queryRoomsByOwner } from '@/api/account/accountManageApi'
import { getDict } from '@/api/community/communityApi'
export default {
  name: 'PrestoreAccount',
  data() {
    return {
      visible: false,
      formData: {
        tel: '',
        ownerId: '',
        amount: '',
        remark: '',
        acctType: '2003',
        primeRate: '',
        roomId: ''
      },
      acctTypes: [],
      primeRates: [],
      owners: [],
      rooms: [],
      rules: {
        acctType: [{ required: true, message: this.$t('prestoreAccount.accountType'), trigger: 'change' }],
        tel: [{ required: true, message: this.$t('prestoreAccount.ownerPhone'), trigger: 'blur' }],
        ownerId: [{ required: true, message: this.$t('prestoreAccount.ownerName'), trigger: 'change' }],
        roomId: [{ required: true, message: this.$t('prestoreAccount.deductionRoom'), trigger: 'change' }],
        amount: [{ required: true, message: this.$t('prestoreAccount.prestoreAmount'), trigger: 'blur' }],
        primeRate: [{ required: true, message: this.$t('prestoreAccount.paymentMethod'), trigger: 'change' }]
      }
    }
  },
  methods: {
    open(params = {}) {
      this.visible = true
      this.resetForm()

      // 加载字典数据
      Promise.all([
        getDict('account', 'acct_type'),
        getDict('pay_fee_detail', 'prime_rate')
      ]).then(([acctTypes, primeRates]) => {
        this.acctTypes = acctTypes
        this.primeRates = primeRates

        // 设置传入参数
        if (params.tel) {
          this.formData = {
            ...this.formData,
            tel: params.tel,
            ownerId: params.ownerId,
            acctType: params.acctType,
            roomId: params.roomId
          }
          this.handleTelChange()
        }
      })
    },

    resetForm() {
      this.formData = {
        tel: '',
        ownerId: '',
        amount: '',
        remark: '',
        acctType: '2003',
        primeRate: '',
        roomId: ''
      }
      this.$refs.form && this.$refs.form.resetFields()
    },

    handleClose() {
      this.resetForm()
    },

    async handleTelChange() {
      if (!this.formData.tel) return

      try {
        const res = await queryOwners({
          row: 50,
          page: 1,
          link: this.formData.tel
        })

        this.owners = res.data
      } catch (error) {
        console.log(error)
        this.$message.error(this.$t('common.fetchError'))
        this.owners = []
      }
    },

    async loadOwnerRooms() {
      if (!this.formData.ownerId) return

      try {
        const res = await queryRoomsByOwner({
          row: 50,
          page: 1,
          ownerId: this.formData.ownerId
        })
        this.rooms = res.rooms || []
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
        this.rooms = []
      }
    },

    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },

    async handleSave() {
      const valid = await this.validateForm()
      if (!valid) return

      try {
        await ownerPrestoreAccount(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveError'))
      }
    }
  }
}
</script>

<style scoped>
.w-full {
  width: 100%;
}
</style>