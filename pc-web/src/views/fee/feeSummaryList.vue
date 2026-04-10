<template>
  <div class="fee-summary-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('feeSummary.title') }}</span>
            <div class="card-header-right">
              <el-button-group>
                <el-button size="small" :type="feeSummaryInfo.feeSummaryType === 1001 ? 'primary' : ''"
                  @click="_switchFeeSummaryType(1001)">
                  {{ $t('feeSummary.day') }}
                </el-button>
                <el-button size="small" :type="feeSummaryInfo.feeSummaryType === 1101 ? 'primary' : ''"
                  @click="_switchFeeSummaryType(1101)">
                  {{ $t('feeSummary.month') }}
                </el-button>
                <el-button size="small" :type="feeSummaryInfo.feeSummaryType === 1111 ? 'primary' : ''"
                  @click="_switchFeeSummaryType(1111)">
                  {{ $t('feeSummary.year') }}
                </el-button>
              </el-button-group>
            </div>
          </div>

          <el-table :data="feeSummaryInfo.fees" border style="width: 100%" v-loading="loading">
            <el-table-column prop="createTime" :label="$t('feeSummary.time')" align="center" />
            <el-table-column prop="configId" :label="$t('feeSummary.feeItemId')" align="center" />
            <el-table-column prop="feeName" :label="$t('feeSummary.feeItemName')" align="center" />
            <el-table-column prop="receivableAmounts" :label="$t('feeSummary.receivableAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.receivableAmounts }} {{ $t('feeSummary.yuan') }}
              </template>
            </el-table-column>
            <el-table-column prop="receivedAmounts" :label="$t('feeSummary.receivedAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.receivedAmounts }} {{ $t('feeSummary.yuan') }}
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="pagination-wrapper" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listFeeSummarys } from '@/api/fee/feeSummaryApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeSummaryList',
  data() {
    return {
      loading: false,
      feeSummaryInfo: {
        fees: [],
        feeSummaryType: '1001',
        total: 0
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listFeeSummarys(this.page.current, this.page.size)
  },
  methods: {
    async _listFeeSummarys(page, size) {
      try {
        this.loading = true
        const params = {
          page,
          row: size,
          feeSummaryType: this.feeSummaryInfo.feeSummaryType,
          communityId: this.communityId
        }
        const { fees, total } = await listFeeSummarys(params)
        this.feeSummaryInfo.fees = fees
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('feeSummary.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _switchFeeSummaryType(feeSummaryType) {
      this.feeSummaryInfo.feeSummaryType = feeSummaryType
      this.page.current = 1
      this._listFeeSummarys(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFeeSummarys(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFeeSummarys(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-summary-container {
  padding: 20px;

  .clearfix {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-header-right {
    display: flex;
    align-items: center;
  }

  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }

  .el-button-group {
    margin-left: 10px;
  }
}
</style>