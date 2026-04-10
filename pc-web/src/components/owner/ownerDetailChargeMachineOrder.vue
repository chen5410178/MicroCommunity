<template>
  <div class="margin-top">
    <el-table :data="ownerDetailChargeMachineOrderInfo.chargeMachineOrders" style="width: 100%" border stripe>
      <el-table-column prop="orderId" :label="$t('ownerDetailChargeMachineOrder.id')" align="center"></el-table-column>
      <el-table-column prop="personName" :label="$t('ownerDetailChargeMachineOrder.name')"
        align="center"></el-table-column>
      <el-table-column prop="personTel" :label="$t('ownerDetailChargeMachineOrder.phone')"
        align="center"></el-table-column>
      <el-table-column prop="machineName" :label="$t('ownerDetailChargeMachineOrder.charger')"
        align="center"></el-table-column>
      <el-table-column prop="portName" :label="$t('ownerDetailChargeMachineOrder.slot')"
        align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailChargeMachineOrder.chargeHours')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.chargeHours === 999">{{ $t('ownerDetailChargeMachineOrder.byAmount') }}</span>
          <span v-else>{{ scope.row.chargeHours }}{{ $t('ownerDetailChargeMachineOrder.hours') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="energy" :label="$t('ownerDetailChargeMachineOrder.chargeAmount')"
        align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('ownerDetailChargeMachineOrder.startTime')"
        align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('ownerDetailChargeMachineOrder.endTime')"
        align="center"></el-table-column>
      <el-table-column prop="acctDetailId" :label="$t('ownerDetailChargeMachineOrder.deductionAccount')" align="center"
        class-name="hand"></el-table-column>
      <el-table-column prop="durationPrice" :label="$t('ownerDetailChargeMachineOrder.hourPrice')"
        align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailChargeMachineOrder.deductionAmount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.amount }}(<a href="javascript:void(0)"
            @click="_viewOrderAccts(scope.row)">{{ $t('ownerDetailChargeMachineOrder.detail') }}</a>)
        </template>
      </el-table-column>
      <el-table-column :label="$t('ownerDetailChargeMachineOrder.socketStatus')" align="center">
        <template slot-scope="scope">
          {{ scope.row.stateName }}
          <span v-if="scope.row.state === '1001'">
            (<a href="javascript:void(0)"
              @click="_showStopCharge(scope.row)">{{ $t('ownerDetailChargeMachineOrder.stopCharge') }}</a>)
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" :label="$t('ownerDetailChargeMachineOrder.description')" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark || '-' }}
        </template>
      </el-table-column>
    </el-table>
    <el-row>
      <el-col :span="4"></el-col>
      <el-col :span="20">
        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
          layout="total, prev, pager, next, jumper" :total="total">
        </el-pagination>
      </el-col>
    </el-row>
    <!-- <stop-charge-machine ref="stopChargeMachine"></stop-charge-machine> -->
  </div>
</template>

<script>
import { getOpenApi } from '@/api/owner/ownerDetailChargeMachineOrderApi'
import { getCommunityId } from '@/api/community/communityApi'
// import StopChargeMachine from '@/components/machine/stopChargeMachine'

export default {
  name: 'OwnerDetailChargeMachineOrder',
  components: {
    //StopChargeMachine
  },
  data() {
    return {
      ownerDetailChargeMachineOrderInfo: {
        chargeMachineOrders: [],
        ownerId: '',
        link: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(ownerId, link) {
      this.ownerDetailChargeMachineOrderInfo.ownerId = ownerId
      this.ownerDetailChargeMachineOrderInfo.link = link
      this._loadOwnerDetailChargeMachineOrderData(this.currentPage, this.pageSize)
    },
    _loadOwnerDetailChargeMachineOrderData(page, row) {
      const param = {
        page: page,
        row: row,
        communityId: this.communityId,
        personTel: this.ownerDetailChargeMachineOrderInfo.link,
        iotApiCode: 'listChargeMachineOrderBmoImpl'
      }

      getOpenApi(param).then(response => {
        this.ownerDetailChargeMachineOrderInfo.chargeMachineOrders = response.data.data
        this.total = response.data.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _viewOrderAccts(order) {
      this.$router.push(`/pages/machine/chargeMachineOrderAccts?orderId=${order.orderId}`)
    },
    _showStopCharge(order) {
      this.$refs.stopChargeMachine.open(order)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadOwnerDetailChargeMachineOrderData(val, this.pageSize)
    }
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}
</style>