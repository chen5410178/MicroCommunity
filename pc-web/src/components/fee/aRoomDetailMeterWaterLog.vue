<template>
  <div class="meter-water-container">
    <el-table :data="meterWaters" border>
      <el-table-column prop="meterTypeName" :label="$t('adminRoomFee.meterType')" align="center" />
      <el-table-column prop="objName" :label="$t('adminRoomFee.objName')" align="center" />
      <el-table-column prop="preDegrees" :label="$t('adminRoomFee.preDegrees')" align="center" />
      <el-table-column prop="curDegrees" :label="$t('adminRoomFee.curDegrees')" align="center" />
      <el-table-column prop="preReadingTime" :label="$t('adminRoomFee.preReadingTime')" align="center" />
      <el-table-column prop="curReadingTime" :label="$t('adminRoomFee.curReadingTime')" align="center" />
      <el-table-column prop="createTime" :label="$t('adminRoomFee.createTime')" align="center" />
    </el-table>

    <el-pagination
      :current-page="page.current"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { listAdminMeterWaters } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'ARoomDetailMeterWaterLog',
  props: {
    roomId: {
      type: String,
      default: ''
    },
    feeId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      meterWaters: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.roomId = params.roomId
      this.feeId = params.feeId
      this.page.current = 1
      this.loadData()
    },
    loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        objId: this.roomId,
        feeId: this.feeId
      }
      
      listAdminMeterWaters(params).then(res => {
        this.meterWaters = res.data
        this.page.total = res.total
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>