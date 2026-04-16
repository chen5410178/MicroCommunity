<template>
  <div class="padding">
    <el-row :gutter="10">
      <el-col :span="3">
        <div class="list-group-border-radius">
          <div class=" treeview">
            <ul class="list-group text-center ">
              <li v-for="(item, index) in states" :key="index" @click="swatchState(item)" :class="{
                'vc-node-selected': conditions.state === item.state
              }" class="list-group-item node-orgTree">
                {{ $t(`invoiceApply.states.${item.stateName}`) }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="21">
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
                  <el-select v-model="conditions.invoiceType" :placeholder="$t('invoiceApply.invoiceTypePlaceholder')">
                    <el-option label="请选择发票类型" value="" />
                    <el-option label="个人" value="1001" />
                    <el-option label="企业" value="2002" />
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-input v-model="conditions.ownerName" :placeholder="$t('invoiceApply.ownerNamePlaceholder')" />
                </el-col>
                <el-col :span="4">
                  <el-input v-model="conditions.createUserName" :placeholder="$t('invoiceApply.applicantPlaceholder')" />
                </el-col>
                <el-col :span="4">
                  <el-input v-model="conditions.applyTel" :placeholder="$t('invoiceApply.phonePlaceholder')" />
                </el-col>
                <el-col :span="4">
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
                <el-table-column prop="stateName" :label="$t('invoiceApply.table.status')" align="center" />
                <el-table-column prop="createTime" :label="$t('invoiceApply.table.applyTime')" align="center" />
                <el-table-column :label="$t('common.operation')" align="center" width="220">
                  <template slot-scope="scope">
                    <el-button v-if="scope.row.state === 'W'" size="mini" @click="_openInvoiceAuditModel(scope.row)">
                      {{ $t('common.audit') }}
                    </el-button>
                    <template v-else>
                      <el-button v-if="hasPlugin('invoice') && scope.row.state !== 'W'" size="mini"
                        @click="_toOpenInvoicePlugin(scope.row)">
                        {{ $t('invoiceApply.openInvoice') }}
                      </el-button>
                      <div v-else>
                        <el-button v-if="['U', 'G', 'C'].includes(scope.row.state)" size="mini"
                          @click="_openUploadInvoicePhoto(scope.row)">
                          {{ scope.row.state === 'U' ? $t('invoiceApply.uploadInvoice') : $t('invoiceApply.reUpload') }}
                        </el-button>
                        <el-button v-if="['U', 'G', 'C'].includes(scope.row.state)" size="mini"
                          @click="_openUserGetInvoice(scope.row)">
                          {{ scope.row.state === 'G' ? $t('invoiceApply.verify') : $t('invoiceApply.register') }}
                        </el-button>
                      </div>
                    </template>
                    <el-button size="mini" type="danger" @click="_openDeleteInvoiceApplyModel(scope.row)">
                      {{ $t('common.delete') }}
                    </el-button>
                    <el-button size="mini" @click="_openInvoiceApplyDetail(scope.row)">
                      {{ $t('common.detail') }}
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
      </el-col>
    </el-row>

    <delete-invoice-apply ref="deleteDialog" @success="listInvoiceApplys" />
    <upload-invoice-photo ref="uploadPhotoDialog" @success="listInvoiceApplys" />
    <wirte-invoice-event ref="writeEventDialog" @success="listInvoiceApplys" />
    <audit-dialog ref="auditDialog" @success="notifyAuditInfo" />
  </div>
</template>

<script>
import { listInvoiceApply, auditInvoiceApply } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteInvoiceApply from '@/components/fee/deleteInvoiceApply'
import UploadInvoicePhoto from '@/components/fee/uploadInvoicePhoto'
import WirteInvoiceEvent from '@/components/fee/wirteInvoiceEvent'
import AuditDialog from '@/components/fee/audit'
import { jumpToPlugin } from '@/api/user/menuApi' 
import {getUserName} from '@/api/user/userApi'

export default {
  name: 'InvoiceApplyList',
  components: {
    DeleteInvoiceApply,
    UploadInvoicePhoto,
    WirteInvoiceEvent,
    AuditDialog
  },
  data() {
    return {
      states: [
        { stateName: 'all', state: '' },
        { stateName: 'pendingReview', state: 'W' },
        { stateName: 'pendingUpload', state: 'U' },
        { stateName: 'reviewFailed', state: 'F' },
        { stateName: 'pendingReceive', state: 'G' },
        { stateName: 'received', state: 'C' }
      ],
      conditions: {
        invoiceCode: '',
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
      },
      auditData: {}
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
    swatchState(item) {
      this.conditions.state = item.state
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
    _openInvoiceAuditModel(row) {
      this.auditData = { ...row }
      this.$refs.auditDialog.open()
    },
    async notifyAuditInfo(auditInfo) {
      try {
        const params = {
          ...this.auditData,
          state: auditInfo.state,
          remark: auditInfo.remark,
          communityId: this.communityId
        }
        await auditInvoiceApply(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.listInvoiceApplys()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    _openUploadInvoicePhoto(row) {
      this.$refs.uploadPhotoDialog.open(row)
    },
    _openUserGetInvoice(row) {
      this.$refs.writeEventDialog.open(row)
    },
    _toOpenInvoicePlugin(row) {
      jumpToPlugin(`/#/pages/index/plugin?pluginType=invoice&orderNo=&orderNo=${row.applyId}&staffName=${getUserName()}&communityId=${this.communityId}`)
    },
    _openDeleteInvoiceApplyModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    hasPlugin(pluginType) {
      const sysInfoStr = window.localStorage.getItem('java110SystemInfo')
      if (!sysInfoStr) {
        return false
      }
      let sysInfo
      try {
        sysInfo = JSON.parse(sysInfoStr)
      } catch (error) {
        return false
      }
      if (!sysInfo || !Object.prototype.hasOwnProperty.call(sysInfo, 'plugins')) {
        return false
      }
      const plugins = sysInfo.plugins
      let hasPlugin = false
      plugins.forEach(p => {
        if (pluginType === p.pluginType) {
          hasPlugin = true
        }
      })
      return hasPlugin
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