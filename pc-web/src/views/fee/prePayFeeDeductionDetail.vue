<template>
  <div class="pre-pay-fee-deduction-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">{{ $t('prePayFeeDeductionDetail.title') }}</span>
        <div class="card-tools">
          <el-button class="float-right" type="primary" @click="handleGoBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-row>
        <el-col :span="24">
          <el-table v-loading="loading" :data="prePayFeeDeductionDetailInfo.prePayFeeDetails" border style="width: 100%"
            class="table-wrapper">
            <el-table-column prop="detailId" :label="$t('prePayFeeDeductionDetail.table.detailId')" align="center"
              min-width="80" />
            <el-table-column prop="feeName" :label="$t('prePayFeeDeductionDetail.table.feeName')" align="center"
              min-width="120" />
            <el-table-column prop="preAmount" :label="$t('prePayFeeDeductionDetail.table.preAmount')" align="center"
              min-width="100" />
            <el-table-column prop="curAmount" :label="$t('prePayFeeDeductionDetail.table.curAmount')" align="center"
              min-width="100" />
            <el-table-column prop="remainAmount" :label="$t('prePayFeeDeductionDetail.table.remainAmount')"
              align="center" min-width="100" />
            <el-table-column prop="curOweAmount" :label="$t('prePayFeeDeductionDetail.table.curOweAmount')"
              align="center" min-width="100" />
            <el-table-column prop="hisOweAmount" :label="$t('prePayFeeDeductionDetail.table.hisOweAmount')"
              align="center" min-width="100" />
            <el-table-column prop="curYearMonth" :label="$t('prePayFeeDeductionDetail.table.curYearMonth')"
              align="center" min-width="100" />
            <el-table-column prop="payerObjName" :label="$t('prePayFeeDeductionDetail.table.payerObjName')"
              align="center" min-width="120" />
            <el-table-column prop="ownerName" :label="$t('prePayFeeDeductionDetail.table.ownerName')" align="center"
              min-width="120" />
            <el-table-column prop="ftName" :label="$t('prePayFeeDeductionDetail.table.ftName')" align="center"
              min-width="100" />
            <el-table-column prop="createTime" :label="$t('prePayFeeDeductionDetail.table.createTime')" align="center"
              min-width="120" />
            <el-table-column prop="remark" :label="$t('prePayFeeDeductionDetail.table.remark')" align="center"
              min-width="120" />
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" class="pagination-wrapper" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { listPrePayFeeDetails } from '@/api/fee/prePayFeeDeductionDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PrePayFeeDeductionDetailList',
  data() {
    return {
      loading: false,
      prePayFeeDeductionDetailInfo: {
        prePayFeeDetails: [],
        conditions: {
          ppfId: '',
          curYearMonth: '',
          communityId: '',
          ftName: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.prePayFeeDeductionDetailInfo.conditions.ppfId = this.$route.query.ppfId
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ppfId: this.prePayFeeDeductionDetailInfo.conditions.ppfId,
          curYearMonth: this.prePayFeeDeductionDetailInfo.conditions.curYearMonth,
          communityId: getCommunityId(),
          ftName: this.prePayFeeDeductionDetailInfo.conditions.ftName
        }

        const { data, total, records } = await listPrePayFeeDetails(params)
        this.prePayFeeDeductionDetailInfo.prePayFeeDetails = data
        this.page.total = records || total
      } catch (error) {
        this.$message.error(this.$t('prePayFeeDeductionDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleGoBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.page.current = 1
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.pre-pay-fee-deduction-detail-container {
  padding: 0;
  margin: 0;

  .box-card {
    margin: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }

      .card-tools {
        .float-right {
          float: right;
        }
      }
    }

    .table-wrapper {
      margin-top: 20px;
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>