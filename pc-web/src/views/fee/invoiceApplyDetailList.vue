<template>
  <div class="invoice-apply-detail">
    <el-card class="box-card">
      <div class="card-header">
        <div class="text-title">{{ $t('invoiceApplyDetail.title') }}</div>
        <div>
          <el-button type="primary" size="small" style="margin-left:10px" @click="goBack">
            {{ $t('invoiceApplyDetail.back') }}
          </el-button>
        </div>
      </div>

      <!-- 业主信息 -->
      <div class="margin-top text-left">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.applyId') }}</label>
              <label>{{ invoiceApplyDetailInfo.applyId }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceType') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceType === '1001' ? $t('invoiceApplyDetail.personal') :
                $t('invoiceApplyDetail.company') }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.ownerName') }}</label>
              <label>{{ invoiceApplyDetailInfo.ownerName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.createUserName') }}</label>
              <label>{{ invoiceApplyDetailInfo.createUserName }}({{ invoiceApplyDetailInfo.applyTel }})</label>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceName') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceNum') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceNum }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceAddress') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceAddress }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceAmount') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceAmount }}</label>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.stateName') }}</label>
              <label>{{ invoiceApplyDetailInfo.stateName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.mallState') }}</label>
              <label>{{ mallStateName }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.failReason') }}</label>
              <label>{{ invoiceApplyDetailInfo.failReason || '-' }}</label>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.createTime') }}</label>
              <label>{{ invoiceApplyDetailInfo.createTime }}</label>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <label class="col-form-label">{{ $t('invoiceApplyDetail.invoiceCode') }}</label>
              <label>{{ invoiceApplyDetailInfo.invoiceCode || $t('invoiceApplyDetail.notUploaded') }}</label>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-divider></el-divider>

      <el-tabs v-model="invoiceApplyDetailInfo._currentTab" @tab-click="handleTabClick">
        <el-tab-pane :label="$t('invoiceApplyDetail.feeTab')" name="invoiceApplyDetailFee">
          <invoice-apply-detail-fee v-if="invoiceApplyDetailInfo._currentTab === 'invoiceApplyDetailFee'"
            :apply-id="invoiceApplyDetailInfo.applyId"
            :owner-id="invoiceApplyDetailInfo.ownerId"></invoice-apply-detail-fee>
        </el-tab-pane>
        <el-tab-pane :label="$t('invoiceApplyDetail.imageTab')" name="invoiceApplyDetailJpg">
          <div v-if="invoiceApplyDetailInfo.mallPdfUrl">
            <a target="_blank" :href="invoiceApplyDetailInfo.mallPdfUrl">
              {{ $t('invoiceApplyDetail.downloadInvoice') }}
            </a>
          </div>
          <div class="padding" v-else>
            {{ $t('invoiceApplyDetail.noInvoicePdf') }}
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getInvoiceApplyDetail } from '@/api/fee/invoiceApplyDetailApi'
import InvoiceApplyDetailFee from '@/components/fee/invoiceApplyDetailFee'

export default {
  name: 'InvoiceApplyDetailList',
  components: {
    InvoiceApplyDetailFee
  },
  data() {
    return {
      invoiceApplyDetailInfo: {
        applyId: '',
        invoiceType: '',
        ownerName: '',
        createUserName: '',
        invoiceName: '',
        invoiceNum: '',
        invoiceAddress: '',
        invoiceAmount: '',
        stateName: '',
        mallState: '',
        failReason: '',
        createTime: '',
        invoiceCode: '',
        mallPdfUrl: '',
        _currentTab: 'invoiceApplyDetailFee'
      }
    }
  },
  computed: {
    mallStateName() {
      const mallState = this.invoiceApplyDetailInfo.mallState
      if (!mallState) {
        return '-'
      }
      const map = {
        AUDIT: this.$t('invoiceApply.mallState.AUDIT'),
        WAIT: this.$t('invoiceApply.mallState.WAIT'),
        DONE: this.$t('invoiceApply.mallState.DONE'),
        FAIL: this.$t('invoiceApply.mallState.FAIL'),
        RED: this.$t('invoiceApply.mallState.RED')
      }
      return map[mallState] || mallState
    }
  },
  created() {
    this.invoiceApplyDetailInfo.applyId = this.$route.query.applyId
    if (this.invoiceApplyDetailInfo.applyId) {
      this.loadInvoiceApplyInfo()
    }
  },
  methods: {
    async loadInvoiceApplyInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          applyId: this.invoiceApplyDetailInfo.applyId
        }
        const res = await getInvoiceApplyDetail(params)
        if (res.code === 0 && res.data && res.data.length > 0) {
          Object.assign(this.invoiceApplyDetailInfo, res.data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('invoiceApplyDetail.fetchError'))
      }
    },
    handleTabClick(tab) {
      this.invoiceApplyDetailInfo._currentTab = tab.name
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.invoice-apply-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.margin-top {
  margin-top: 20px;
}

.padding {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.col-form-label {
  margin-right: 10px;
}
</style>