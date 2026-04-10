<template>
  <div class="community-fee-summary-container">
    <el-row class="animated fadeInRight" :gutter="20">
      <el-col :span="4">
        <select-admin-one-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('communityFeeSummary.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-date-picker v-model="searchForm.startDate" type="date"
                :placeholder="$t('communityFeeSummary.search.startDate')" class="w-100" />
            </el-col>
            <el-col :span="6">
              <el-date-picker v-model="searchForm.endDate" type="date"
                :placeholder="$t('communityFeeSummary.search.endDate')" class="w-100" />
            </el-col>
            <el-col :span="6">
              <el-select v-model="searchForm.feeTypeCd" :placeholder="$t('communityFeeSummary.search.feeType')"
                class="w-100">
                <el-option :label="$t('communityFeeSummary.search.selectFeeType')" value="" />
                <el-option v-for="item in feeTypeOptions" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('communityFeeSummary.table.title') }}</span>
            <el-tooltip effect="dark" :content="$t('communityFeeSummary.table.tooltip')" placement="top">
              <i class="el-icon-info" style="cursor: pointer;"></i>
            </el-tooltip>
            <span style="font-size: 10px;">({{ $t('communityFeeSummary.table.updateDaily') }})</span>
          </div>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="roomCount" :label="$t('communityFeeSummary.table.totalRooms')" align="center" />
            <el-table-column prop="feeRoomCount" :label="$t('communityFeeSummary.table.feeRooms')" align="center" />
            <el-table-column prop="oweRoomCount" :label="$t('communityFeeSummary.table.oweRooms')" align="center" />
            <el-table-column :label="$t('communityFeeSummary.table.oweFeeFormula')" align="center">
              <el-table-column prop="hisOweFee" :label="$t('communityFeeSummary.table.hisOweFee')" align="center" />
              <el-table-column prop="curOweFee" :label="$t('communityFeeSummary.table.curOweFee')" align="center" />
              <el-table-column :label="$t('communityFeeSummary.table.totalOweFee')" align="center">
                <template slot-scope="scope">
                  {{ (scope.row.curOweFee + scope.row.hisOweFee).toFixed(2) }}
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column :label="$t('communityFeeSummary.table.receivedFeeFormula')" align="center">
              <el-table-column prop="hisReceivedFee" :label="$t('communityFeeSummary.table.hisReceivedFee')"
                align="center" />
              <el-table-column :label="$t('communityFeeSummary.table.curPartialFee')" align="center">
                <template slot-scope="scope">
                  {{ (scope.row.receivedFee - scope.row.hisReceivedFee - scope.row.preReceivedFee).toFixed(2) }}
                </template>
              </el-table-column>
              <el-table-column prop="preReceivedFee" :label="$t('communityFeeSummary.table.preReceivedFee')"
                align="center" />
              <el-table-column prop="receivedFee" :label="$t('communityFeeSummary.table.totalReceivedFee')"
                align="center" />
            </el-table-column>
            <el-table-column prop="curReceivableFee" :label="$t('communityFeeSummary.table.curReceivableFee')"
              align="center" />
            <el-table-column :label="$t('communityFeeSummary.table.curReceivedFee')" align="center">
              <template slot-scope="scope">
                {{ (scope.row.curReceivableFee - scope.row.curOweFee).toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('communityFeeSummary.table.roomFeeRate')" align="center">
              <template slot-scope="scope">
                {{ scope.row.feeRoomCount > 0 ?
                  (((scope.row.feeRoomCount - scope.row.oweRoomCount) / scope.row.feeRoomCount * 100).toFixed(2) + '%') :
                  '0%' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('communityFeeSummary.table.feeRate')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curReceivableFee > 0 ?
                  (((scope.row.curReceivableFee - scope.row.curOweFee) / scope.row.curReceivableFee * 100).toFixed(2) + '%'): '0%' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('communityFeeSummary.table.clearRate')" align="center">
              <template slot-scope="scope">
                {{ (scope.row.hisReceivedFee + scope.row.hisOweFee) > 0 ?
                  ((scope.row.hisReceivedFee / (scope.row.hisReceivedFee + scope.row.hisOweFee) * 100).toFixed(2) + '%') :
                  '0%' }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityFeeSummary } from '@/api/report/communityFeeSummaryApi'
import SelectAdminOneCommunity from '@/components/community/selectAdminOneCommunity'

export default {
  name: 'CommunityFeeSummaryList',
  components: {
    SelectAdminOneCommunity
  },
  data() {
    return {
      searchForm: {
        startDate: '',
        endDate: '',
        feeTypeCd: '',
        communityId: ''
      },
      feeTypeOptions: [],
      tableData: [],
      loading: false
    }
  },
  created() {
    this.initDate()
    this.loadFeeTypes()
  },
  methods: {
    initDate() {
      const today = new Date()
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1)
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0)

      this.searchForm.startDate = firstDay
      this.searchForm.endDate = lastDay
    },
    loadFeeTypes() {
      this.$store.dispatch('dict/getDict', {
        dictType: 'pay_fee_config',
        dictName: 'fee_type_cd'
      }).then(data => {
        this.feeTypeOptions = data
      })
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    },
    async handleSearch() {
      try {
        this.loading = true
        const params = {
          startDate: this.formatDate(this.searchForm.startDate),
          endDate: this.formatDate(this.searchForm.endDate),
          feeTypeCd: this.searchForm.feeTypeCd,
          communityId: this.searchForm.communityId
        }

        const { data } = await getCommunityFeeSummary(params)
        this.tableData = data
      } catch (error) {
        this.$message.error(this.$t('communityFeeSummary.fetchError'))
      } finally {
        this.loading = false
      }
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      const year = d.getFullYear()

      if (month.length < 2) month = '0' + month
      if (day.length < 2) day = '0' + day

      return [year, month, day].join('-')
    }
  }
}
</script>

<style lang="scss" scoped>
.community-fee-summary-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .mt-20 {
    margin-top: 20px;
  }

  .w-100 {
    width: 100%;
  }
}
</style>