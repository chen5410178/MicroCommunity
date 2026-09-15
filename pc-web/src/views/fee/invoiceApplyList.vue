<template>
  <div class="padding">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('invoiceApply.searchTitle') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="conditions.invoiceCode" :placeholder="$t('invoiceApply.invoiceCodePlaceholder')" />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.invoiceKind" :placeholder="$t('invoiceApply.invoiceKindPlaceholder')">
                <el-option :label="$t('invoiceApply.invoiceKindAll')" value="" />
                <el-option :label="$t('invoiceApply.normalInvoice')" value="普票" />
                <el-option :label="$t('invoiceApply.specialInvoice')" value="专票" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.invoiceType" :placeholder="$t('invoiceApply.invoiceTypePlaceholder')">
                <el-option :label="$t('invoiceApply.invoiceTypeAll')" value="" />
                <el-option :label="$t('invoiceApply.personal')" value="1001" />
                <el-option :label="$t('invoiceApply.company')" value="2002" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('invoiceApply.table.submitState')">
                <el-option :label="$t('invoiceApply.submitStateAll')" value="" />
                <el-option :label="$t('invoiceApply.submitStateSubmit')" value="S" />
                <el-option :label="$t('invoiceApply.submitStateFail')" value="F" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.ownerName" :placeholder="$t('invoiceApply.ownerNamePlaceholder')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.createUserName" :placeholder="$t('invoiceApply.applicantPlaceholder')" />
            </el-col>
            <el-col :span="4" class="margin-top-sm">
              <el-input v-model="conditions.applyTel" :placeholder="$t('invoiceApply.phonePlaceholder')" />
            </el-col>
            <el-col :span="4" class="margin-top-sm">
              <el-button type="primary" @click="_queryInvoiceApplyMethod">
                <i class="el-icon-search" />
                <span>{{ $t('common.search') }}</span>
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row class="margin-top">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('invoiceApply.title') }}</span>
            <el-button type="primary" size="small" class="float-right" @click="_invoiceApply">
              {{ $t('invoiceApply.apply') }}
            </el-button>
          </div>
          <el-table :data="invoiceApplys" border style="width: 100%">
            <el-table-column prop="applyId" :label="$t('invoiceApply.table.id')" align="center" />
            <el-table-column :label="$t('invoiceApply.table.invoiceKind')" align="center">
              <template slot-scope="scope">
                {{ scope.row.invoiceKind || $t('invoiceApply.normalInvoice') }}
              </template>
            </el-table-column>
            <el-table-column prop="invoiceType" :label="$t('invoiceApply.table.invoiceType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.invoiceType === '1001' ? $t('invoiceApply.personal') : $t('invoiceApply.company') }}
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('invoiceApply.table.ownerName')" align="center" />
            <el-table-column :label="$t('invoiceApply.table.applicant')" align="center">
              <template slot-scope="scope">
                {{ scope.row.createUserName }}({{ scope.row.applyTel }})
              </template>
            </el-table-column>
            <el-table-column prop="invoiceName" :label="$t('invoiceApply.table.invoiceName')" align="center" />
            <el-table-column prop="invoiceNum" :label="$t('invoiceApply.table.taxNumber')" align="center" />
            <el-table-column prop="invoiceAddress" :label="$t('invoiceApply.table.addressPhone')" align="center" />
            <el-table-column prop="invoiceAmount" :label="$t('invoiceApply.table.amount')" align="center" />
            <el-table-column prop="invoiceCode" :label="$t('invoiceApply.table.invoiceCode')" align="center">
              <template slot-scope="scope">
                {{ scope.row.invoiceCode || $t('invoiceApply.notUploaded') }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('invoiceApply.table.submitState')" align="center" />
            <el-table-column :label="$t('invoiceApply.table.mallState')" align="center">
              <template slot-scope="scope">
                {{ mallStateName(scope.row.mallState) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('invoiceApply.table.failReason')" align="center">
              <template slot-scope="scope">
                {{ failReason(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('invoiceApply.table.applyTime')" align="center" />
            <el-table-column :label="$t('invoiceApply.table.invoicePdf')" align="center">
              <template slot-scope="scope">
                <el-button v-if="scope.row.mallPdfUrl" type="text" @click="_downloadInvoicePdf(scope.row)">
                  {{ $t('invoiceApply.downloadInvoice') }}
                </el-button>
                <span v-else>{{ $t('invoiceApply.noInvoicePdf') }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openInvoiceApplyDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
                <el-button v-if="scope.row.state === 'F'" size="mini" type="primary"
                  @click="_reSubmitInvoiceApply(scope.row)">
                  {{ $t('invoiceApply.reSubmit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="_openDeleteInvoiceApplyModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <delete-invoice-apply ref="deleteDialog" @success="listInvoiceApplys" />
  </div>
</template>

<script>
import { listInvoiceApply, reSubmitInvoiceApply } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteInvoiceApply from '@/components/fee/deleteInvoiceApply'

export default {
  name: 'InvoiceApplyList',
  components: {
    DeleteInvoiceApply
  },
  data() {
    return {
      conditions: {
        invoiceCode: '',
        invoiceKind: '',
        invoiceType: '',
        ownerName: '',
        applyTel: '',
        createUserName: '',
        state: ''
      },
      invoiceApplys: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.listInvoiceApplys()
  },
  methods: {
    async listInvoiceApplys() {
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const res = await listInvoiceApply(params)
        this.invoiceApplys = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listInvoiceApplys()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listInvoiceApplys()
    },
    _queryInvoiceApplyMethod() {
      this.page.current = 1
      this.listInvoiceApplys()
    },
    _invoiceApply() {
      this.$router.push('/views/fee/ownerApplyInvoice')
    },
    _openInvoiceApplyDetail(row) {
      this.$router.push(`/views/fee/invoiceApplyDetail?applyId=${row.applyId}`)
    },
    _downloadInvoicePdf(row) {
      if (!row.mallPdfUrl) {
        this.$message.warning(this.$t('invoiceApply.noInvoicePdf'))
        return
      }
      window.open(row.mallPdfUrl)
    },
    async _reSubmitInvoiceApply(row) {
      try {
        const res = await reSubmitInvoiceApply({ applyId: row.applyId })
        const data = res.data || {}
        if (data.state === 'F') {
          this.$message.error(data.failReason || this.$t('invoiceApply.submitFailed'))
        } else {
          this.$message.success(this.$t('invoiceApply.submitSuccess'))
        }
        this.listInvoiceApplys()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    _openDeleteInvoiceApplyModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    mallStateName(mallState) {
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
    },
    failReason(row) {
      if (row.failReason) {
        return row.failReason
      }
      if (row.state === 'F') {
        return row.remark
      }
      return '-'
    }
  }
}
</script>

<style scoped>
.border-radius {
  border-radius: 4px;
}


.float-right {
  float: right;
}
</style>
