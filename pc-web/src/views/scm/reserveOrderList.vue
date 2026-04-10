<template>
  <div class="reserve-order-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="tree-list">
            <li class="tree-item" :class="{ 'active': reserveOrderInfo.conditions.type === '1001' }"
              @click="_changeType('1001')">
              {{ $t('reserveOrder.diningOrder') }}
            </li>
            <li class="tree-item" :class="{ 'active': reserveOrderInfo.conditions.type === '2002' }"
              @click="_changeType('2002')">
              {{ $t('reserveOrder.serviceOrder') }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('reserveOrder.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="reserveOrderInfo.conditions.appointmentTime"
                :placeholder="$t('reserveOrder.selectAppointmentTime')" />
            </el-col>
            <el-col :span="6">
              <el-input v-model="reserveOrderInfo.conditions.personName"
                :placeholder="$t('reserveOrder.selectAppointmentPerson')" />
            </el-col>
            <el-col :span="6">
              <el-input v-model="reserveOrderInfo.conditions.personTel"
                :placeholder="$t('reserveOrder.selectAppointmentPhone')" />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="_queryReserveOrderPersonMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="6">
              <el-select v-model="reserveOrderInfo.conditions.state" :placeholder="$t('reserveOrder.selectStatus')"
                style="width:100%">
                <el-option :label="$t('reserveOrder.allStatus')" value="" />
                <el-option :label="$t('reserveOrder.successStatus')" value="S" />
                <el-option :label="$t('reserveOrder.failStatus')" value="F" />
                <el-option :label="$t('reserveOrder.waitAuditStatus')" value="W" />
                <el-option :label="$t('reserveOrder.waitPayStatus')" value="F" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-input v-model="reserveOrderInfo.conditions.goodsNameLike"
                :placeholder="$t('reserveOrder.inputName')" />
            </el-col>
          </el-row>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span v-if="reserveOrderInfo.conditions.type === '1001'">
              {{ $t('reserveOrder.diningOrder') }}
            </span>
            <span v-else>
              {{ $t('reserveOrder.serviceOrder') }}
            </span>
          </div>
          <el-table :data="reserveOrderInfo.orders" border style="width: 100%" v-loading="loading">
            <el-table-column prop="orderId" :label="$t('reserveOrder.orderId')" align="center" />
            <el-table-column prop="goodsName" :label="$t('reserveOrder.goodsName')" align="center" />
            <el-table-column prop="personName" :label="$t('reserveOrder.appointmentPerson')" align="center" />
            <el-table-column prop="personTel" :label="$t('reserveOrder.appointmentPhone')" align="center" />
            <el-table-column prop="appointmentTime" :label="$t('reserveOrder.appointmentDate')" align="center" />
            <el-table-column :label="$t('reserveOrder.appointmentTime')" align="center">
              <template slot-scope="scope">
                <span v-for="(time, index) in scope.row.times" :key="index">
                  {{ time.hours }}{{ $t('reserveOrder.hour') }}
                  <span v-if="scope.row.type === '1001'">({{ time.quantity }}{{ $t('reserveOrder.unit') }})</span>
                  <span v-if="index < scope.row.times.length - 1">,</span>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="receivableAmount" :label="$t('reserveOrder.receivableAmount')" align="center" />
            <el-table-column prop="receivedAmount" :label="$t('reserveOrder.receivedAmount')" align="center" />
            <el-table-column prop="payWayName" :label="$t('reserveOrder.payWay')" align="center" />
            <el-table-column prop="stateName" :label="$t('reserveOrder.status')" align="center" />
            <el-table-column prop="createTime" :label="$t('reserveOrder.createTime')" align="center" />
            <el-table-column prop="remark" :label="$t('reserveOrder.remark')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === 'S'" type="text" size="small"
                  @click="_openDeleteReserveOrderPersonModel(scope.row)">
                  {{ $t('reserveOrder.cancelAppointment') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="margin-top: 20px;" />
        </el-card>
      </el-col>
    </el-row>

    <delete-reserve-order-person ref="deleteReserveOrderPerson" @success="handleSuccess" />
  </div>
</template>

<script>
import { listReserveGoodsOrder } from '@/api/scm/reserveOrderApi'
import DeleteReserveOrderPerson from '@/components/scm/deleteReserveOrderPerson'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReserveOrderList',
  components: {
    DeleteReserveOrderPerson
  },
  data() {
    return {
      loading: false,
      reserveOrderInfo: {
        orders: [],
        conditions: {
          type: '1001',
          goodsNameLike: '',
          personName: '',
          personTel: '',
          appointmentTime: '',
          state: '',
          communityId: ''
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
    this.reserveOrderInfo.conditions.communityId = getCommunityId()
    this._listReserveOrderPersons(this.page.current, this.page.size)
  },
  methods: {
    async _listReserveOrderPersons(page, size) {
      try {
        this.loading = true
        const params = {
          ...this.reserveOrderInfo.conditions,
          page,
          row: size
        }
        const { data, total } = await listReserveGoodsOrder(params)
        this.reserveOrderInfo.orders = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('reserveOrder.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openDeleteReserveOrderPersonModel(row) {
      this.$refs.deleteReserveOrderPerson.open(row)
    },
    _queryReserveOrderPersonMethod() {
      this.page.current = 1
      this._listReserveOrderPersons(this.page.current, this.page.size)
    },
    _changeType(type) {
      this.reserveOrderInfo.conditions.type = type
      this._listReserveOrderPersons(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listReserveOrderPersons(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listReserveOrderPersons(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listReserveOrderPersons(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.reserve-order-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .tree-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .tree-item {
        padding: 10px;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }
}
</style>