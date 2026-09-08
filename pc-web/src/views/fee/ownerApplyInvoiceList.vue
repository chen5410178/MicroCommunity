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
            <el-form-item :label="$t('ownerApplyInvoice.room')" required>
              <el-input v-model="roomName" :placeholder="$t('ownerApplyInvoice.roomPlaceholder')" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="4" class="text-left">
            <el-button type="primary" size="small" style="margin-left: 150px; margin-bottom: 20px"
              @click="openChooseRoom">
              {{ $t('ownerApplyInvoice.chooseRoom') }}
            </el-button></el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.owner')" required>
              <el-select v-model="formData.ownerId" :placeholder="$t('ownerApplyInvoice.ownerPlaceholder')"
                style="width:100%" @change="handleOwnerChange">
                <el-option v-for="item in owners" :key="item.memberId || item.ownerId" :label="item.name"
                  :value="item.memberId || item.ownerId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceTitle')">
              <el-input v-model="formData.invoiceName" :placeholder="$t('ownerApplyInvoice.invoiceTitlePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="4" class="text-left">
            <el-button v-if="currentOwner && currentOwner.link" type="primary" size="small"
              style="margin-left: 150px; margin-bottom: 20px" @click="openSearchHeading">
              {{ $t('ownerApplyInvoice.chooseExistingHeading') }}
            </el-button></el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.taxpayerId')">
              <el-input v-model="formData.invoiceNum" :placeholder="$t('ownerApplyInvoice.taxpayerIdPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceType')" required>
              <el-select v-model="formData.invoiceType" :placeholder="$t('ownerApplyInvoice.invoiceTypePlaceholder')"
                style="width:100%">
                <el-option :label="$t('ownerApplyInvoice.personal')" value="1001" />
                <el-option :label="$t('ownerApplyInvoice.enterprise')" value="2002" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.contactPhone')" required>
              <el-input v-model="formData.invoiceLink" :placeholder="$t('ownerApplyInvoice.contactPhonePlaceholder')"
                disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.addressPhone')">
              <el-input v-model="formData.invoiceAddress" :placeholder="$t('ownerApplyInvoice.addressPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.email')">
              <el-input v-model="formData.email" :placeholder="$t('ownerApplyInvoice.emailPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.bankAccount')">
              <el-input v-model="formData.invoiceAccount"
                :placeholder="$t('ownerApplyInvoice.bankAccountPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.remark')">
              <el-input v-model="formData.remark" :placeholder="$t('ownerApplyInvoice.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 未开票缴费记录 -->
    <el-card class="mt-20">
      <div slot="header" class="flex justify-between">
        <h3>{{ $t('ownerApplyInvoice.paidFees') }}</h3>
      </div>

      <el-table :data="feeDetails" border style="width: 100%">
        <el-table-column width="55" align="center">
          <template slot-scope="scope">
            <el-radio v-model="selectedDetailId" :label="scope.row.detailId">&nbsp;</el-radio>
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

    <div class="mt-20 text-right">
      <el-button type="primary" @click="handleSubmit">
        {{ $t('ownerApplyInvoice.submit') }}
      </el-button>
    </div>

    <search-room ref="searchRoomRef" @chooseRoom="handleChooseRoom" />
    <search-owner-invoice ref="searchOwnerInvoice" :visible.sync="showSearchHeading" @select="handleHeadingSelect" />
  </div>
</template>

<script>
import { queryOwnerByRoom, queryFeeDetail, saveInvoiceApply, listInvoiceHeadings } from '@/api/fee/ownerApplyInvoiceApi'
import SearchRoom from '@/components/room/searchRoom'
import SearchOwnerInvoice from '@/components/fee/searchOwnerInvoice'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'OwnerApplyInvoiceList',
  components: {
    SearchRoom,
    SearchOwnerInvoice
  },
  data() {
    return {
      showSearchHeading: false,
      roomName: '',
      roomId: '',
      owners: [],
      currentOwner: null,
      formData: {
        ownerId: '',
        ownerName: '',
        invoiceType: '',
        invoiceName: '',
        invoiceNum: '',
        invoiceAddress: '',
        invoiceLink: '',
        email: '',
        invoiceAccount: '',
        remark: ''
      },
      feeDetails: [],
      selectedDetailId: ''
    }
  },
  methods: {
    dateFormat,
    goBack() {
      this.$router.go(-1)
    },
    openChooseRoom() {
      if (this.$refs.searchRoomRef) {
        this.$refs.searchRoomRef.open()
      }
    },
    handleChooseRoom(room) {
      this.roomId = room.roomId
      this.roomName = room.floorNum + '-' + room.unitNum + '-' + room.roomNum
      this.owners = []
      this.currentOwner = null
      this.resetOwnerData()
      this.clearHeadingForm()
      this.loadOwners()
    },
    async loadOwners() {
      try {
        const res = await queryOwnerByRoom({
          page: 1,
          row: 50,
          roomId: this.roomId
        })
        this.owners = res.data || []
        if (!this.owners.length) {
          this.$message.warning(this.$t('ownerApplyInvoice.noOwner'))
          this.owners = []
          this.currentOwner = null
          this.resetOwnerData()
          return
        }
        // 未选择业主时自动带出第一个
        if (!this.formData.ownerId) {
          this.formData.ownerId = this.owners[0].memberId || this.owners[0].ownerId
          this.handleOwnerChange(this.formData.ownerId)
        }
      } catch (error) {
        console.error('Failed to load owners:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    resetOwnerData() {
      this.formData.ownerId = ''
      this.formData.ownerName = ''
      this.formData.invoiceLink = ''
      this.feeDetails = []
      this.selectedDetailId = ''
    },
    handleOwnerChange(ownerId) {
      const owner = this.owners.find(item => (item.memberId || item.ownerId) === ownerId)
      this.currentOwner = owner || null
      this.formData.ownerId = ownerId
      this.formData.ownerName = owner ? owner.name : ''
      this.formData.invoiceLink = owner ? owner.link : ''

      this.feeDetails = []
      this.selectedDetailId = ''
      this.clearHeadingForm()

      if (!owner) return

      if (!owner.link) {
        this.$message.warning(this.$t('ownerApplyInvoice.ownerNoPhone'))
        return
      }

      this.loadFeeDetails()
      this.loadHeadings(owner.link)
    },
    clearHeadingForm() {
      this.formData.invoiceType = ''
      this.formData.invoiceName = ''
      this.formData.invoiceNum = ''
      this.formData.invoiceAddress = ''
      this.formData.email = ''
      this.formData.invoiceAccount = ''
      this.formData.remark = ''
    },
    async loadHeadings(userPhone) {
      try {
        const res = await listInvoiceHeadings({
          page: 1,
          row: 20,
          userPhone: userPhone
        })
        const headings = res.data || []
        if (headings.length) {
          const defaultHeading = headings.find(item => item.isDefault === '1') || headings[0]
          this.applyHeading(defaultHeading)
        }
      } catch (error) {
        console.error('Failed to load headings:', error)
      }
    },
    applyHeading(heading) {
      this.formData.invoiceType = heading.headingType === '公司' ? '2002' : '1001'
      this.formData.invoiceName = heading.headingName
      this.formData.invoiceNum = heading.taxNo
      this.formData.invoiceAddress = heading.address
      this.formData.invoiceLink = this.currentOwner && this.currentOwner.link ? this.currentOwner.link : heading.phone
      this.formData.email = heading.email
      this.formData.invoiceAccount = heading.bankNo
      this.formData.remark = heading.remark
    },
    async loadFeeDetails() {
      try {
        const { data: feeDetails } = await queryFeeDetail({
          page: 1,
          row: 50,
          ownerId: this.formData.ownerId,
          openInvoice: 'N'
        })
        this.feeDetails = feeDetails || []
      } catch (error) {
        console.error('Failed to load fee details:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    openSearchHeading() {
      if (!this.currentOwner || !this.currentOwner.link) return
      this.showSearchHeading = true
      this.$nextTick(() => {
        if (this.$refs.searchOwnerInvoice) {
          this.$refs.searchOwnerInvoice.open(this.currentOwner.link)
        }
      })
    },
    handleHeadingSelect(heading) {
      this.applyHeading(heading)
    },
    async handleSubmit() {
      if (!this.formData.ownerId) {
        this.$message.warning(this.$t('ownerApplyInvoice.chooseOwner'))
        return
      }
      if (!this.formData.invoiceLink) {
        this.$message.warning(this.$t('ownerApplyInvoice.ownerNoPhone'))
        return
      }
      if (!this.formData.invoiceType) {
        this.$message.warning(this.$t('ownerApplyInvoice.invoiceTypeRequired'))
        return
      }
      if (!this.formData.invoiceName) {
        this.$message.warning(this.$t('ownerApplyInvoice.invoiceTitleRequired'))
        return
      }
      if (!this.selectedDetailId) {
        this.$message.warning(this.$t('ownerApplyInvoice.noFeeSelected'))
        return
      }

      try {
        await saveInvoiceApply({
          ...this.formData,
          detailIds: [this.selectedDetailId]
        })
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
