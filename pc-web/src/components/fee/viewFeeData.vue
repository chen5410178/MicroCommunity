<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose"
  >
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="label"
        :label="$t('viewFeeData.label')"
        width="180"
      />
      <el-table-column
        prop="value"
        :label="$t('viewFeeData.value')"
      />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'ViewFeeData',
  data() {
    return {
      dialogVisible: false,
      title: '',
      tableData: [],
      feeId: ''
    }
  },
  methods: {
    open(params) {
      this.feeId = params.feeId
      this.loadViewFeeData()
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    async loadViewFeeData() {
      try {
        const communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 1,
          communityId,
          feeId: this.feeId
        }

        const response = await this.$http.get('/fee.listFee', { params })
        const fee = response.data.fees[0]

        this.title = `${fee.feeName} ${this.$t('viewFeeData.details')}`

        const data = {
          [this.$t('viewFeeData.feeId')]: fee.feeId,
          [this.$t('viewFeeData.feeFlag')]: fee.feeFlagName,
          [this.$t('viewFeeData.feeType')]: fee.feeTypeCdName,
          [this.$t('viewFeeData.payerObj')]: fee.payerObjName,
          [this.$t('viewFeeData.feeName')]: fee.feeName,
          [this.$t('viewFeeData.state')]: fee.stateName,
          [this.$t('viewFeeData.createTime')]: fee.startTime,
          [this.$t('viewFeeData.billingStartTime')]: this.getViewFeeDataEndTime(fee),
          [this.$t('viewFeeData.billingEndTime')]: this.getViewFeeDataDeadlineTime(fee),
          [this.$t('viewFeeData.batch')]: fee.batchId
        }

        if (fee.feeAttrs) {
          fee.feeAttrs.forEach(attr => {
            data[attr.specCdName] = attr.value
          })
        }

        this.tableData = Object.keys(data).map(key => ({
          label: key,
          value: data[key]
        }))
      } catch (error) {
        console.error('加载费用数据失败:', error)
      }
    },
    getViewFeeDataDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime)
    },
    getViewFeeDataEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime)
    },
    handleClose() {
      this.tableData = []
      this.title = ''
      this.feeId = ''
    }
  }
}
</script>