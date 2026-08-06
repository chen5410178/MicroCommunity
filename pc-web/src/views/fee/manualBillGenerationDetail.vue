<template>
  <div class="app-container padding">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('manualBillGeneration.detailTitle') }}</span>
        <div>
          <el-button size="small" @click="$router.go(-1)">{{ $t('common.back') }}</el-button>
        </div>
      </div>
      <el-descriptions :column="2" border v-if="application">
        <el-descriptions-item :label="$t('manualBillGeneration.applicationName')">{{ application.applicationName }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.billMonth')">{{ application.billMonth }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.feeName')">{{ application.feeName }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.state')">
          <el-tag v-if="application.state === '1001'" type="warning">{{ $t('manualBillGeneration.stateWait') }}</el-tag>
          <el-tag v-else-if="application.state === '1002'" type="success">{{ $t('manualBillGeneration.statePass') }}</el-tag>
          <el-tag v-else-if="application.state === '1003'" type="danger">{{ $t('manualBillGeneration.stateReject') }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.totalCount')">{{ application.totalCount }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.successCount')">{{ application.successCount }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.errorCount')">{{ application.errorCount }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.createTime')">{{ application.createTime }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.createUser')">{{ application.createUserName }}</el-descriptions-item>
        <el-descriptions-item :label="$t('manualBillGeneration.remark')" :span="2">{{ application.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="application.state !== '1001'" :label="$t('manualBillGeneration.auditUser')">{{ application.auditUserName }}</el-descriptions-item>
        <el-descriptions-item v-if="application.state !== '1001'" :label="$t('manualBillGeneration.auditTime')">{{ application.auditTime }}</el-descriptions-item>
        <el-descriptions-item v-if="application.state === '1003'" :label="$t('manualBillGeneration.auditRemark')" :span="2">{{ application.auditRemark }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="margin-top">
      <div slot="header">
        <span>{{ $t('manualBillGeneration.detailList') }}</span>
      </div>
      <el-table :data="details" border>
        <el-table-column prop="roomName" :label="$t('manualBillGeneration.roomName')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('manualBillGeneration.ownerName')" align="center"></el-table-column>
        <el-table-column prop="curYearMonth" :label="$t('manualBillGeneration.curYearMonth')" width="120" align="center"></el-table-column>
        <el-table-column :label="$t('manualBillGeneration.state')" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state === '1001'" type="warning" size="small">{{ $t('manualBillGeneration.stateWait') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '1002'" type="success" size="small">{{ $t('manualBillGeneration.stateGenerated') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '1003'" type="danger" size="small">{{ $t('manualBillGeneration.stateFail') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorMsg" :label="$t('manualBillGeneration.errorMsg')" align="center" show-overflow-tooltip></el-table-column>
      </el-table>
    </el-card>

    <el-card class="margin-top" v-if="application && application.state === '1001'">
      <div slot="header">
        <span>{{ $t('manualBillGeneration.auditTitle') }}</span>
      </div>
      <el-form label-width="100px">
        <el-form-item :label="$t('manualBillGeneration.auditRemark')">
          <el-input v-model="auditRemark" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="handleAudit('1002')">{{ $t('manualBillGeneration.auditPass') }}</el-button>
          <el-button type="danger" @click="handleAudit('1003')">{{ $t('manualBillGeneration.auditReject') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getManualBillApplication, auditManualBillApplication } from '@/api/fee/manualBillGenerationApi'

export default {
  name: 'ManualBillGenerationDetail',
  data() {
    return {
      application: null,
      details: [],
      auditRemark: ''
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      getManualBillApplication({
        applicationId: this.$route.query.applicationId,
        communityId: this.getCommunityId()
      }).then(res => {
        if (res.code === 0) {
          this.application = res.data.application
          this.details = res.data.details || []
        }
      })
    },
    handleAudit(state) {
      if (state === '1003' && !this.auditRemark) {
        this.$message.warning(this.$t('manualBillGeneration.pleaseInputRejectReason'))
        return
      }
      const confirmMsg = state === '1002'
        ? this.$t('manualBillGeneration.auditPassConfirm')
        : this.$t('manualBillGeneration.auditRejectConfirm')
      this.$confirm(confirmMsg, this.$t('common.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        auditManualBillApplication({
          applicationId: this.application.applicationId,
          communityId: this.getCommunityId(),
          state: state,
          auditRemark: this.auditRemark
        }).then(res => {
          if (res.code === 0) {
            this.$message.success(this.$t('manualBillGeneration.auditSuccess'))
            this.loadData()
          } else {
            this.$message.error(res.msg || this.$t('manualBillGeneration.auditFail'))
          }
        }).catch(() => {
          this.$message.error(this.$t('manualBillGeneration.auditFail'))
        })
      }).catch(() => {})
    }
  }
}
</script>
