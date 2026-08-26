<template>
  <div class="owner-apply-invoice-container">
    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <h3>{{ $t('ownerApplyInvoice.title') }}</h3>
        <div class="header-actions">
          <el-button type="primary" size="small" @click="goBack">
            {{ $t('ownerApplyInvoice.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="formData" label-width="150px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceTitle')" required>
              <el-input v-model="formData.invoiceName" :placeholder="$t('ownerApplyInvoice.chooseInvoiceTitle')"
                disabled />
            </el-form-item>
          </el-col>
          <el-col :span="4" class="text-left">
            <el-button type="primary" size="small" style="margin-left: 150px; margin-bottom: 20px"
              @click="openSelectOwner">
              {{ $t('ownerApplyInvoice.select') }}
            </el-button></el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.taxpayerId')" required>
              <el-input v-model="formData.invoiceNum" :placeholder="$t('ownerApplyInvoice.chooseTaxId')" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceType')" required>
              <el-select v-model="formData.invoiceType" :placeholder="$t('ownerApplyInvoice.chooseInvoiceType')"
                style="width:100%">
                <el-option :label="$t('ownerApplyInvoice.personal')" value="1001" />
                <el-option :label="$t('ownerApplyInvoice.enterprise')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.addressPhone')" required>
              <el-input v-model="formData.invoiceAddress" :placeholder="$t('ownerApplyInvoice.chooseAddressPhone')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.owner')" required>
              <el-input v-model="formData.ownerName" :placeholder="$t('ownerApplyInvoice.chooseContent')" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.content')" required>
              <el-select v-model="formData.invoiceFlag" :placeholder="$t('ownerApplyInvoice.chooseContent')"
                style="width:100%" @change="handleContentChange">
                <el-option :label="$t('ownerApplyInvoice.paidFees')" value="FEE" />
                <el-option :label="$t('ownerApplyInvoice.accountDeposit')" value="ACCT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>


    </el-card>

    <!-- 已缴费费用 -->
    <el-card v-if="formData.invoiceFlag === 'FEE'" class="mt-20">
      <div slot="header" class="flex justify-between">
        <h3>{{ $t('ownerApplyInvoice.paidFees') }}</h3>
      </div>

      <el-table :data="feeDetails" border style="width: 100%">
        <el-table-column width="55" align="center">
          <template slot-scope="scope">
            <el-checkbox 
              :checked="formData.detailIds.includes(scope.row.detailId)"
              @change="handleFeeCheckboxChange(scope.row.detailId, $event)">
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="feeName" :label="$t('ownerApplyInvoice.feeName')" align="center" />
        <el-table-column prop="payerObjName" :label="$t('ownerApplyInvoice.payer')" align="center" />
        <el-table-column :label="$t('ownerApplyInvoice.receivablePaid')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('ownerApplyInvoice.paymentMethod')" align="center" />
        <el-table-column :label="$t('ownerApplyInvoice.paymentPeriod')" align="center">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.startTime) }}~<br>
            {{ dateFormat(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerApplyInvoice.paymentTime')" align="center" />
        <el-table-column prop="cashierName" :label="$t('ownerApplyInvoice.cashier')" align="center">
          <template slot-scope="scope">
            {{ scope.row.cashierName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('ownerApplyInvoice.status')" align="center" />
        <el-table-column prop="remark" :label="$t('ownerApplyInvoice.remark')" align="center" />
      </el-table>
    </el-card>

    <!-- 账户预存 -->
    <el-card v-if="formData.invoiceFlag === 'ACCT'" class="mt-20">
      <div slot="" class="flex justify-between">
        <h3>{{ $t('ownerApplyInvoice.accountDeposit') }}</h3>
      </div>

      <el-table :data="acctDetails" border style="width: 100%">
        <el-table-column width="55" align="center">
          <template slot-scope="scope">
            <el-checkbox 
              :checked="formData.arIds.includes(scope.row.arId)"
              @change="handleAcctCheckboxChange(scope.row.arId, $event)">
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="acctName" :label="$t('ownerApplyInvoice.accountName')" align="center" />
        <el-table-column prop="acctTypeName" :label="$t('ownerApplyInvoice.accountType')" align="center" />
        <el-table-column prop="ownerName" :label="$t('ownerApplyInvoice.owner')" align="center" />
        <el-table-column prop="receivedAmount" :label="$t('ownerApplyInvoice.depositAmount')" align="center" />
        <el-table-column prop="primeRateName" :label="$t('ownerApplyInvoice.depositMethod')" align="center" />
        <el-table-column prop="amount" :label="$t('ownerApplyInvoice.totalAmount')" align="center" />
        <el-table-column prop="createTime" :label="$t('ownerApplyInvoice.depositTime')" align="center" />
      </el-table>
    </el-card>

    <div class="mt-20 text-right">
      <el-button type="primary" @click="handleSubmit">
        {{ $t('ownerApplyInvoice.submit') }}
      </el-button>
    </div>

    <search-owner-invoice ref="searchOwnerInvoice" :visible.sync="showSearchDialog" @select="handleOwnerSelect" />
  </div>
</template>

<script>
import { queryFeeDetail, listAccountReceipt, saveInvoiceApply } from '@/api/fee/ownerApplyInvoiceApi'
import SearchOwnerInvoice from '@/components/fee/searchOwnerInvoice'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'OwnerApplyInvoiceList',
  components: {
    SearchOwnerInvoice
  },
  data() {
    return {
      showSearchDialog: false,
      formData: {
        oiId: '',
        ownerId: '',
        ownerName: '',
        invoiceType: '',
        invoiceName: '',
        invoiceAddress: '',
        invoiceNum: '',
        detailIds: [],
        feeDetails: [],
        invoiceFlag: 'FEE',
        arIds: [],
        acctDetails: []
      },
      feeDetails: [],
      acctDetails: []
    }
  },
  methods: {
    dateFormat,
    goBack() {
      this.$router.go(-1)
    },
    openSelectOwner() {
      this.showSearchDialog = true
      this.$nextTick(() => {
        this.$refs.searchOwnerInvoice.open()
      })
    },
    handleOwnerSelect(owner) {
      Object.assign(this.formData, owner)
      this.formData.detailIds = []
      this.formData.arIds = []

      if (this.formData.invoiceFlag === 'FEE') {
        this.loadFeeDetails()
      } else {
        this.loadAcctDetails()
      }
    },
    async loadFeeDetails() {
      try {
        const params = {
          page: 1,
          row: 50,
          ownerId: this.formData.ownerId,
          openInvoice: 'N'
        }

        const { data: feeDetails } = await queryFeeDetail(params)
        this.feeDetails = feeDetails || []

        // 默认全选
        this.formData.detailIds = this.feeDetails.map(item => item.detailId)
      } catch (error) {
        console.error('Failed to load fee details:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    async loadAcctDetails() {
      try {
        const params = {
          page: 1,
          row: 50,
          ownerId: this.formData.ownerId
        }

        const res = await listAccountReceipt(params)
        this.acctDetails = res.data || []

        // 默认全选
        this.formData.arIds = this.acctDetails.map(item => item.arId)
      } catch (error) {
        console.error('Failed to load account details:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    handleContentChange() {
      if (!this.formData.ownerId) return

      this.formData.detailIds = []
      this.formData.arIds = []

      if (this.formData.invoiceFlag === 'FEE') {
        this.loadFeeDetails()
      } else {
        this.loadAcctDetails()
      }
    },
    handleFeeCheckboxChange(detailId, checked) {
      if (checked) {
        this.formData.detailIds.push(detailId)
      } else {
        this.formData.detailIds = this.formData.detailIds.filter(id => id !== detailId)
      }
    },
    handleAcctCheckboxChange(arId, checked) {
      if (checked) {
        this.formData.arIds.push(arId)
      } else {
        this.formData.arIds = this.formData.arIds.filter(id => id !== arId)
      }
    },
    async handleSubmit() {
      try {
        // 验证表单
        if (!this.formData.ownerId) {
          this.$message.warning(this.$t('ownerApplyInvoice.chooseOwner'))
          return
        }

        if (this.formData.invoiceFlag === 'FEE' && !this.formData.detailIds.length) {
          this.$message.warning(this.$t('ownerApplyInvoice.noFeeSelected'))
          return
        }

        if (this.formData.invoiceFlag === 'ACCT' && !this.formData.arIds.length) {
          this.$message.warning(this.$t('ownerApplyInvoice.noDepositSelected'))
          return
        }

        // 准备提交数据
        const submitData = {
          ...this.formData,
          detailIds: this.formData.invoiceFlag === 'FEE' ? this.formData.detailIds : [],
          arIds: this.formData.invoiceFlag === 'ACCT' ? this.formData.arIds : []
        }

        // 调用API
        await saveInvoiceApply(submitData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('Failed to submit invoice application:', error)
        this.$message.error(error.message || this.$t('common.submitFailed'))
      }
    }
  }
}
</script>

<style scoped>
.owner-apply-invoice-container {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}

.header-actions {}
</style>