<template>
  <div class="margin-top">
    <el-row class="margin-top-lg">
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502020082314267912')"
          @click="_openBatchPayRoomFeeModal()">
          {{ $t('ownerDetailOweFee.batchPayment') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="ownerDetailOweFeeInfo.fees" style="width: 100%">
        <el-table-column prop="payerObjName" :label="$t('ownerDetailOweFee.paymentObject')"
          align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('ownerDetailOweFee.ownerName')" align="center"></el-table-column>
        <el-table-column prop="ownerTel" :label="$t('ownerDetailOweFee.phone')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('ownerDetailOweFee.startTime')" align="center"></el-table-column>
        <el-table-column prop="deadlineTime" :label="$t('ownerDetailOweFee.endTime')" align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('ownerDetailOweFee.totalAmount')"
          align="center"></el-table-column>
        <el-table-column prop="updateTime" :label="$t('ownerDetailOweFee.updateTime')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12">
          <span>{{ $t('ownerDetailOweFee.paymentNotice') }}</span>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="prev, pager, next, jumper">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryReportOweFee } from '@/api/fee/listOweFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailOweFee',
  data() {
    return {
      ownerDetailOweFeeInfo: {
        fees: [],
        ownerId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    switch(data) {
      this.ownerDetailOweFeeInfo.ownerId = data.ownerId
      this._loadOwnerDetailOweFeeData(1, this.pagination.pageSize)
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadOwnerDetailOweFeeData(val, this.pagination.pageSize)
    },
    _loadOwnerDetailOweFeeData(page, row) {
      const params = {
        communityId: this.communityId,
        ownerId: this.ownerDetailOweFeeInfo.ownerId,
        page: page,
        row: row
      }

      queryReportOweFee(params).then(response => {
        this.ownerDetailOweFeeInfo.fees = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _openBatchPayRoomFeeModal() {
      this.$router.push(`/views/fee/batchPayFeeOrder?ownerId=${this.ownerDetailOweFeeInfo.ownerId}&payerObjType=3333`)
    },
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.text-right {
  text-align: right;
}
</style>