<template>
  <div>
    <div class="margin-top">
      <el-table
        :data="feeDetailMeterInfo.meterWaters"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="waterId"
          :label="$t('feeDetailMeter.meterId')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="meterTypeName"
          :label="$t('feeDetailMeter.meterType')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="objName"
          :label="$t('feeDetailMeter.objName')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="preDegrees"
          :label="$t('feeDetailMeter.preDegrees')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="curDegrees"
          :label="$t('feeDetailMeter.curDegrees')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="preReadingTime"
          :label="$t('feeDetailMeter.preReadingTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="curReadingTime"
          :label="$t('feeDetailMeter.curReadingTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('feeDetailMeter.createTime')"
          align="center"
        ></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
          ></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listMeterWaters } from '@/api/fee/feeDetailMeterApi'
import { getCommunityId } from '@/api/community/communityApi'   

export default {
  name: 'FeeDetailMeter',
  data() {
    return {
      feeDetailMeterInfo: {
        meterWaters: [],
        feeId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(params) {
      this.feeDetailMeterInfo.feeId = params.feeId
      this._loadFeeDetailMeterData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadFeeDetailMeterData(this.currentPage, this.pageSize)
    },
    async _loadFeeDetailMeterData(page, row) {
      try {
        const params = {
          communityId: getCommunityId(),
          feeId: this.feeDetailMeterInfo.feeId,
          page,
          row
        }
        
        const res = await listMeterWaters(params)
        this.feeDetailMeterInfo.meterWaters = res.data
        this.total = res.total
      } catch (error) {
        console.error('Failed to load meter data:', error)
      }
    }
  }
}
</script>