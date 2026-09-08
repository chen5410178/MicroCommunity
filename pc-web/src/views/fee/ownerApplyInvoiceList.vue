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
            <el-form-item :label="$t('ownerApplyInvoice.owner')">
              <el-input v-model="formData.ownerName" :placeholder="$t('ownerApplyInvoice.ownerNamePlaceholder')"
                disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceKind')" required>
              <el-select v-model="formData.invoiceKind" :placeholder="$t('ownerApplyInvoice.invoiceKindPlaceholder')"
                style="width:100%" @change="handleInvoiceKindChange">
                <el-option :label="$t('ownerApplyInvoice.normalInvoice')" value="普票" />
                <el-option :label="$t('ownerApplyInvoice.specialInvoice')" value="专票" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.invoiceType')" required>
              <el-select v-model="formData.invoiceType" :placeholder="$t('ownerApplyInvoice.invoiceTypePlaceholder')"
                :disabled="formData.invoiceKind === '专票'" style="width:100%">
                <el-option :label="$t('ownerApplyInvoice.personal')" value="1001" />
                <el-option :label="$t('ownerApplyInvoice.enterprise')" value="2002" />
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
            <el-button v-if="formData.invoiceLink" type="primary" size="small"
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
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.contactPhone')" required>
              <el-input v-model="formData.invoiceLink" :placeholder="$t('ownerApplyInvoice.contactPhonePlaceholder')"
                disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.addressPhone')">
              <el-input v-model="formData.invoiceAddress" :placeholder="$t('ownerApplyInvoice.addressPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.email')">
              <el-input v-model="formData.email" :placeholder="$t('ownerApplyInvoice.emailPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('ownerApplyInvoice.bankAccount')">
              <el-input v-model="formData.invoiceAccount"
                :placeholder="$t('ownerApplyInvoice.bankAccountPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
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

    <room-tree ref="roomTree" @selectRoom="handleChooseRoom" />
    <search-owner-invoice ref="searchOwnerInvoice" :visible.sync="showSearchHeading" @select="handleHeadingSelect" />
  </div>
</template>

<script>
import { queryRooms, queryFeeDetail, saveInvoiceApply, listInvoiceHeadings } from '@/api/fee/ownerApplyInvoiceApi'
import RoomTree from '@/components/room/roomTree'
import SearchOwnerInvoice from '@/components/fee/searchOwnerInvoice'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'OwnerApplyInvoiceList',
  components: {
    RoomTree,
    SearchOwnerInvoice
  },
  data() {
    return {
      showSearchHeading: false,
      roomName: '',
      roomId: '',
      formData: {
        ownerId: '',
        ownerName: '',
        invoiceKind: '普票',
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
      if (this.$refs.roomTree) {
        this.$refs.roomTree.open()
      }
    },
    handleChooseRoom(room) {
      this.roomId = room.roomId
      this.roomName = room.roomName
      this.resetOwnerData()
      this.clearHeadingForm()
      this.loadRoomOwner()
    },
    async loadRoomOwner() {
      try {
        const res = await queryRooms({
          page: 1,
          row: 10,
          roomId: this.roomId
        })
        const rooms = res.rooms || []
        if (!rooms.length || !rooms[0].ownerId) {
          this.$message.warning(this.$t('ownerApplyInvoice.noOwner'))
          return
        }
        const room = rooms[0]
        this.formData.ownerId = room.ownerId
        this.formData.ownerName = room.ownerName
        this.formData.invoiceLink = room.link

        if (!room.link) {
          this.$message.warning(this.$t('ownerApplyInvoice.ownerNoPhone'))
          return
        }

        this.loadFeeDetails()
        this.loadHeadings(room.link)
      } catch (error) {
        console.error('Failed to load room owner:', error)
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
    handleInvoiceKindChange(invoiceKind) {
      if (invoiceKind === '专票') {
        this.formData.invoiceType = '2002'
      }
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
      if (this.formData.invoiceKind === '专票') {
        this.formData.invoiceType = '2002'
      } else {
        this.formData.invoiceType = heading.headingType === '公司' ? '2002' : '1001'
      }
      this.formData.invoiceName = heading.headingName
      this.formData.invoiceNum = heading.taxNo
      this.formData.invoiceAddress = heading.address
      this.formData.invoiceLink = this.formData.invoiceLink || heading.phone
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
      if (!this.formData.invoiceLink) return
      this.showSearchHeading = true
      this.$nextTick(() => {
        if (this.$refs.searchOwnerInvoice) {
          this.$refs.searchOwnerInvoice.open(this.formData.invoiceLink)
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
      if (!this.formData.invoiceKind) {
        this.$message.warning(this.$t('ownerApplyInvoice.invoiceKindRequired'))
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
      if (this.formData.invoiceKind === '专票') {
        if (!this.formData.invoiceNum) {
          this.$message.warning(this.$t('ownerApplyInvoice.taxNoRequired'))
          return
        }
        if (!this.formData.invoiceAccount) {
          this.$message.warning(this.$t('ownerApplyInvoice.bankAccountRequired'))
          return
        }
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
