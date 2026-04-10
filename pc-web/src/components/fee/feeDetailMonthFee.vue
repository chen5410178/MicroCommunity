<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="feeDetailMonthFeeInfo.monthFees" style="width: 100%">
        <el-table-column prop="feeName" :label="$t('feeDetailMonthFee.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('feeDetailMonthFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('feeDetailMonthFee.feeType')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailMonthFee.chargeYearMonth')" align="center">
          <template slot-scope="scope">{{scope.row.detailYear}}-{{scope.row.detailMonth}}</template>
        </el-table-column>
        <el-table-column prop="receivableAmount" :label="$t('feeDetailMonthFee.receivableAmount')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailMonthFee.description')" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.computingFormula == '5005' || scope.row.computingFormula == '9009'">
              <div>
                <span>{{$t('feeDetailMonthFee.lastDegrees')}}</span>{{scope.row.preDegrees}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.currentDegrees')}}</span>{{scope.row.curDegrees}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.unitPrice')}}</span>{{getOnePrice1(scope.row)}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.additionalFee')}}</span>{{scope.row.additionalAmount}}
              </div>
            </div>
            <div v-else-if="scope.row.computingFormula == '6006'">
              <div>
                <span>{{$t('feeDetailMonthFee.usage')}}</span>{{_getAttrValue(scope.row.feeAttrs,'390006')}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.unitPrice')}}</span>{{scope.row.squarePrice}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.additionalFee')}}</span>{{scope.row.additionalAmount}}
              </div>
            </div>
            <div v-else-if="scope.row.feeTypeCd == '888800010017'" width="150">
              <div>
                <span>{{$t('feeDetailMonthFee.algorithm')}}</span>{{_getAttrValue(scope.row.feeAttrs,'390005')}}
              </div>
              <div>
                <span>{{$t('feeDetailMonthFee.usage')}}</span>{{_getAttrValue(scope.row.feeAttrs,'390003')}}
              </div>
            </div>
            <div v-else>
              <div>
                <span>{{$t('feeDetailMonthFee.unitPrice')}}</span>{{scope.row.squarePrice}}
              </div>
              <div v-if="scope.row.feeFlag == '1003006'">
                <span>{{$t('feeDetailMonthFee.additionalFee')}}</span>{{scope.row.additionalAmount}}
              </div>
              <div v-else>
                <span>{{$t('feeDetailMonthFee.fixedFee')}}</span>{{scope.row.additionalAmount}}
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-size="pagination.pageSize"
            layout="total, prev, pager, next"
            :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listMonthFee } from '@/api/fee/feeDetailMonthFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailMonthFee',
  data() {
    return {
      feeDetailMonthFeeInfo: {
        monthFees: [],
        feeId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailMonthFeeInfo.feeId = params.feeId
      this._loadFeeDetailMonthFeeData()
    },
    _loadFeeDetailMonthFeeData() {
      const params = {
        communityId: getCommunityId(),
        feeId: this.feeDetailMonthFeeInfo.feeId,
        detailId: '-1',
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      listMonthFee(params).then(res => {
        this.feeDetailMonthFeeInfo.monthFees = res.data
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load month fee details:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailMonthFeeData()
    },
    getOnePrice1(fee) {
      // Implement your logic here
      return fee.squarePrice || '-'
    },
    _getAttrValue(attrs, code) {
      if (!attrs) return '-'
      const attr = attrs.find(item => item.specCd === code)
      return attr ? attr.value : '-'
    }
  }
}
</script>