<template>
  <div class="reserve-confirm-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <el-input
              v-model="reserveConfirmInfo.timeId"
              :placeholder="$t('reserveConfirm.scanPlaceholder')"
              @keyup.enter.native="confirmReserve"
              clearable
            ></el-input>
            <el-button
              type="primary"
              size="small"
              @click="confirmReserve"
              class="confirm-btn"
            >
              {{ $t('reserveConfirm.confirm') }}
            </el-button>
          </div>
          
          <div class="result-wrapper">
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.result') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.remark }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.confirmTime') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.createTime }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.goodsService') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.goodsName }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.quantity') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.quantity }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.appointmentDate') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.appointmentTime }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.appointmentHour') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.hours }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.appointmentPerson') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.personName }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('reserveConfirm.appointmentPhone') }}</span>
              <span class="value">{{ reserveConfirmInfo.order.personTel }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card class="box-card">
          <div slot="header" class="text-left">
            <span>{{ $t('reserveConfirm.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="reserveConfirmInfo.conditions.personName"
                :placeholder="$t('reserveConfirm.personNamePlaceholder')"
                clearable
              ></el-input>
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="reserveConfirmInfo.conditions.personTel"
                :placeholder="$t('reserveConfirm.personTelPlaceholder')"
                clearable
              ></el-input>
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="reserveConfirmInfo.conditions.appointmentTime"
                :placeholder="$t('reserveConfirm.appointmentTimePlaceholder')"
                clearable
              ></el-input>
            </el-col>
            <el-col :span="6">
              <el-button
                type="primary"
                @click="queryReserveConfirm"
                icon="el-icon-search"
              >
                {{ $t('reserveConfirm.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card table-wrapper">
          <el-table
            :data="reserveConfirmInfo.orders"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="orderId"
              :label="$t('reserveConfirm.orderId')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="goodsName"
              :label="$t('reserveConfirm.goodsService')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="quantity"
              :label="$t('reserveConfirm.quantity')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="appointmentTime"
              :label="$t('reserveConfirm.appointmentDate')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="hours"
              :label="$t('reserveConfirm.appointmentHour')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="personName"
              :label="$t('reserveConfirm.appointmentPerson')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="personTel"
              :label="$t('reserveConfirm.appointmentPhone')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="createTime"
              :label="$t('reserveConfirm.confirmTime')"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="remark"
              :label="$t('reserveConfirm.remark')"
              align="center"
            ></el-table-column>
          </el-table>

          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            class="pagination-wrapper"
          ></el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listReserveGoodsConfirmOrder, saveReserveGoodsConfirmOrder } from '@/api/scm/reserveConfirmApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReserveConfirmList',
  data() {
    return {
      loading: false,
      reserveConfirmInfo: {
        orders: [],
        order: {
          remark: '',
          appointmentTime: '',
          createTime: '',
          hours: '',
          spaceName: '',
          personName: '',
          personTel: '',
          quantity: '',
          goodsName: ''
        },
        timeId: '',
        conditions: {
          spaceId: '',
          personName: '',
          personTel: '',
          appointmentTime: '',
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
    this.reserveConfirmInfo.conditions.communityId = getCommunityId()
    this.listReserveConfirms()
  },
  methods: {
    async listReserveConfirms() {
      try {
        this.loading = true
        const params = {
          ...this.reserveConfirmInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listReserveGoodsConfirmOrder(params)
        this.reserveConfirmInfo.orders = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('reserveConfirm.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async confirmReserve() {
      if (!this.reserveConfirmInfo.timeId) {
        this.$message.warning(this.$t('reserveConfirm.scanWarning'))
        return
      }

      try {
        const data = {
          timeId: this.reserveConfirmInfo.timeId,
          communityId: this.reserveConfirmInfo.conditions.communityId
        }
        const res = await saveReserveGoodsConfirmOrder(data)
        
        if (res.code !== 0) {
          this.$message.error(res.msg)
          return
        }

        this.$message.success(this.$t('common.operationSuccess'))
        this.reserveConfirmInfo.timeId = ''
        this.listReserveConfirms()
        
        if (res.data && res.data.length > 0) {
          this.reserveConfirmInfo.order = { ...res.data[0] }
          if (!this.reserveConfirmInfo.order.remark) {
            this.reserveConfirmInfo.order.remark = this.$t('reserveConfirm.confirmSuccess')
          }
        }
      } catch (error) {
        this.$message.error(error.message)
        this.reserveConfirmInfo.timeId = ''
      }
    },
    queryReserveConfirm() {
      this.page.current = 1
      this.listReserveConfirms()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listReserveConfirms()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listReserveConfirms()
    }
  }
}
</script>

<style lang="scss" scoped>
.reserve-confirm-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
    
    .card-header {
      display: flex;
      margin-bottom: 20px;
      
      .confirm-btn {
        margin-left: 10px;
      }
    }
    
    .result-wrapper {
      .result-item {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
        padding: 5px 0;
        border-bottom: 1px solid #eee;
        
        .label {
          font-weight: bold;
        }
      }
    }
  }
  
  .table-wrapper {
    margin-top: 20px;
    
    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>