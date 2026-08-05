<template>
  <div class="app-container padding">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('manualBillGeneration.title') }}</span>
      </div>
      <el-form :model="searchForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('manualBillGeneration.feeConfig')">
              <el-select v-model="searchForm.configId" :placeholder="$t('manualBillGeneration.selectFeeConfig')" filterable clearable style="width:100%">
                <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('manualBillGeneration.billMonth')">
              <el-date-picker v-model="searchForm.billMonth" type="month" value-format="yyyy-MM" :placeholder="$t('manualBillGeneration.selectBillMonth')" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" size="small" @click="handleSearch" :loading="searchLoading">
                {{ $t('manualBillGeneration.search') }}
              </el-button>
              <el-button size="small" @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="margin-top" v-if="searchResult.length > 0">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('manualBillGeneration.searchResult') }}（{{ searchResult.length }}）</span>
        <div>
          <el-checkbox v-model="selectAll" @change="handleSelectAll">
            {{ $t('manualBillGeneration.selectAll') }}
          </el-checkbox>
          <el-button type="primary" size="small" @click="handleSubmit" style="margin-left:10px">
            {{ $t('manualBillGeneration.submitApplication') }}
          </el-button>
        </div>
      </div>
      <el-table ref="resultTable" :data="searchResult" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('manualBillGeneration.feeName')" align="center"></el-table-column>
        <el-table-column prop="roomName" :label="$t('manualBillGeneration.roomName')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('manualBillGeneration.ownerName')" align="center"></el-table-column>
        <el-table-column prop="curYearMonth" :label="$t('manualBillGeneration.curYearMonth')" width="120" align="center"></el-table-column>
        <el-table-column prop="amount" :label="$t('manualBillGeneration.amount')" width="120" align="center"></el-table-column>
      </el-table>
    </el-card>

    <el-card class="margin-top" v-if="searchResult.length > 0">
      <div slot="header">
        <span>{{ $t('manualBillGeneration.remark') }}</span>
      </div>
      <el-input v-model="remark" type="textarea" :rows="3" :placeholder="$t('manualBillGeneration.remarkPlaceholder')"></el-input>
    </el-card>
  </div>
</template>

<script>
import { searchManualBillTargets, saveManualBillApplication } from '@/api/fee/manualBillGenerationApi'
import { listFeeConfigs } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'ManualBillGenerationList',
  data() {
    return {
      searchForm: {
        configId: '',
        billMonth: ''
      },
      feeConfigs: [],
      searchLoading: false,
      searchResult: [],
      selectedRows: [],
      selectAll: false,
      remark: ''
    }
  },
  mounted() {
    this.loadFeeConfigs()
  },
  methods: {
    loadFeeConfigs() {
      listFeeConfigs({
        page: 1,
        row: 200,
        communityId: this.getCommunityId(),
        feeFlag: '1003006'
      }).then(res => {
        if (res.code === 0) {
          this.feeConfigs = res.data || []
        }
      })
    },
    handleSearch() {
      if (!this.searchForm.configId) {
        this.$message.warning(this.$t('manualBillGeneration.pleaseSelectFeeConfig'))
        return
      }
      if (!this.searchForm.billMonth) {
        this.$message.warning(this.$t('manualBillGeneration.pleaseSelectBillMonth'))
        return
      }
      this.searchLoading = true
      this.searchResult = []
      this.selectedRows = []
      this.selectAll = false
      searchManualBillTargets({
        communityId: this.getCommunityId(),
        configId: this.searchForm.configId,
        billMonth: this.searchForm.billMonth
      }).then(res => {
        if (res.code === 0) {
          this.searchResult = res.data || []
        } else {
          this.$message.error(res.msg || this.$t('manualBillGeneration.searchFail'))
        }
        this.searchLoading = false
      }).catch(() => {
        this.searchLoading = false
        this.$message.error(this.$t('manualBillGeneration.searchFail'))
      })
    },
    handleReset() {
      this.searchForm = { configId: '', billMonth: '' }
      this.searchResult = []
      this.selectedRows = []
      this.selectAll = false
    },
    handleSelectAll(val) {
      if (val) {
        if (this.$refs.resultTable) {
          this.searchResult.forEach(row => {
            this.$refs.resultTable.toggleRowSelection(row, true)
          })
        }
      } else {
        if (this.$refs.resultTable) {
          this.$refs.resultTable.clearSelection()
        }
      }
    },
    handleSelectionChange(val) {
      this.selectedRows = val
      this.selectAll = val.length === this.searchResult.length
    },
    handleSubmit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning(this.$t('manualBillGeneration.pleaseSelectRecords'))
        return
      }
      this.$confirm(this.$t('manualBillGeneration.submitConfirm'), this.$t('common.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        saveManualBillApplication({
          communityId: this.getCommunityId(),
          configId: this.searchForm.configId,
          billMonth: this.searchForm.billMonth,
          remark: this.remark,
          details: this.selectedRows
        }).then(res => {
          if (res.code === 0) {
            this.$message.success(this.$t('manualBillGeneration.submitSuccess'))
            this.$router.push('/pages/fee/manualBillGenerationManage')
          } else {
            this.$message.error(res.msg || this.$t('manualBillGeneration.submitFail'))
          }
        }).catch(() => {
          this.$message.error(this.$t('manualBillGeneration.submitFail'))
        })
      }).catch(() => {})
    }
  }
}
</script>
