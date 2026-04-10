<template>
  <div class="do-dining-container">
    <el-row :gutter="20">
      <el-col :span="4" class="bg-white">
        <div class="margin-sm">{{ $t('doDining.selectGoods') }}</div>
        <div class="flex justify-start flex-wrap">
          <div v-for="reserveDining in doDiningInfo.reserveDinings" :key="reserveDining.goodsId" class="margin-right-xs margin-bottom-xs">
            <div 
              class="vc-border border-radius padding-xs cursor-pointer"
              :class="{'vc-reserve-active': doDiningInfo.goodsId == reserveDining.goodsId}"
              @click="_chooseDining(reserveDining)"
            >
              <div>
                <el-image 
                  style="width: 60px; height: 60px; border-radius: 5px;"
                  :src="reserveDining.imgUrl || '/img/noPhoto.jpg'"
                  fit="cover"
                ></el-image>
              </div>
              <div class="text-center margin-top-xs">
                {{ reserveDining.goodsName }}
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row :gutter="20">
          <el-col :span="6"></el-col>
          <el-col :span="12">
            <el-input 
              v-model="doDiningInfo.qrCode" 
              :placeholder="$t('doDining.scanQrCode')"
              @keyup.enter.native="_doDining()"
              id="qrCode"
            ></el-input>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="_doDining()" style="width:100%">
              {{ $t('doDining.dining') }}
            </el-button>
          </el-col>
        </el-row>

        <el-row class="margin-top">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('doDining.diningRecords') }}</span>
              </div>
              <el-row :gutter="20">
                <el-col :span="4">
                  <el-date-picker
                    v-model="doDiningInfo.conditions.startDate"
                    type="datetime"
                    :placeholder="$t('doDining.selectStartTime')"
                    style="width:100%"
                  ></el-date-picker>
                </el-col>
                <el-col :span="4">
                  <el-date-picker
                    v-model="doDiningInfo.conditions.endDate"
                    type="datetime"
                    :placeholder="$t('doDining.selectEndTime')"
                    style="width:100%"
                  ></el-date-picker>
                </el-col>
                <el-col :span="4">
                  <el-input
                    v-model="doDiningInfo.conditions.nameLike"
                    :placeholder="$t('doDining.enterOwnerName')"
                  ></el-input>
                </el-col>
                <el-col :span="4">
                  <el-input
                    v-model="doDiningInfo.conditions.dinerName"
                    :placeholder="$t('doDining.enterDinerName')"
                  ></el-input>
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" @click="_queryData()">
                    <i class="el-icon-search"></i>
                    {{ $t('common.search') }}
                  </el-button>
                  <el-button @click="_exportExcel()">
                    <i class="el-icon-download"></i>
                    {{ $t('common.export') }}
                  </el-button>
                </el-col>
              </el-row>

              <el-table
                :data="doDiningInfo.orders"
                style="width: 100%"
                border
                class="margin-top"
              >
                <el-table-column
                  prop="createTime"
                  :label="$t('doDining.diningTime')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="goodsName"
                  :label="$t('doDining.goods')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ownerName"
                  :label="$t('doDining.owner')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="personName"
                  :label="$t('doDining.diner')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="personTel"
                  :label="$t('doDining.dinerPhone')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="orderId"
                  :label="$t('doDining.orderNo')"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="remark"
                  :label="$t('doDining.remark')"
                  align="center"
                ></el-table-column>
              </el-table>

              <el-pagination
                :current-page.sync="page.current"
                :page-sizes="[10, 20, 30, 50]"
                :page-size="page.size"
                :total="page.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                class="margin-top"
              ></el-pagination>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <add-reserve-dining-person ref="addReserveDiningPerson"></add-reserve-dining-person>
    <delete-reserve-dining ref="deleteReserveDining"></delete-reserve-dining>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { 
  listReserveGoods,
  queryOwnerDining,
  exportData,
  doDining
} from '@/api/scm/doDiningApi'
import AddReserveDiningPerson from '@/components/scm/addReserveDiningPerson'
import DeleteReserveDining from '@/components/scm/deleteReserveDining'

export default {
  name: 'DoDiningList',
  components: {
    AddReserveDiningPerson,
    DeleteReserveDining
  },
  data() {
    return {
      communityId: '',
      doDiningInfo: {
        reserveDinings: [],
        orders: [],
        total: 0,
        records: 1,
        goodsId: '',
        moreCondition: false,
        qrCode: '',
        conditions: {
          goodsName: '',
          startDate: '',
          endDate: '',
          ownerName: '',
          dinerName: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      focusInterval: null
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.doDiningInfo.conditions.communityId = this.communityId
    this._listReserveDinings(1, 200)
    this._listReserveConfirms(this.page.current, this.page.size)
  },
  mounted() {
    document.getElementById("qrCode").focus()
    this.focusInterval = setInterval(() => {
      document.getElementById("qrCode").focus()
    }, 5000)
  },
  beforeDestroy() {
    if (this.focusInterval) {
      clearInterval(this.focusInterval)
    }
  },
  methods: {
    _listReserveDinings(_page, _rows) {
      const params = {
        page: _page,
        row: _rows,
        communityId: this.communityId
      }
      listReserveGoods(params)
        .then(response => {
          this.doDiningInfo.reserveDinings = response.data
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    _chooseDining(_dining) {
      this.doDiningInfo.goodsId = _dining.goodsId
      document.getElementById("qrCode").focus()
    },
    _doDining() {
      const data = {
        communityId: this.communityId,
        goodsId: this.doDiningInfo.goodsId,
        qrCode: this.doDiningInfo.qrCode
      }

      doDining(data)
        .then(response => {
          this.doDiningInfo.qrCode = ''
          if (response.code !== 0) {
            this.$message.error(response.msg)
            return
          }
          this._listReserveConfirms(this.page.current, this.page.size)
        })
        .catch(error => {
          console.error('请求失败:', error)
          this.doDiningInfo.qrCode = ''
        })
    },
    _listReserveConfirms(_page, _rows) {
      const params = {
        page: _page,
        row: _rows,
        ...this.doDiningInfo.conditions
      }
      queryOwnerDining(params)
        .then(response => {
          this.doDiningInfo.total = response.total
          this.doDiningInfo.records = response.records
          this.doDiningInfo.orders = response.data
          this.page.total = response.total
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    _queryData() {
      this.page.current = 1
      this._listReserveConfirms(this.page.current, this.page.size)
    },
    _exportExcel() {
      const params = {
        ...this.doDiningInfo.conditions,
        pagePath: 'doDining'
      }
      exportData(params)
        .then(response => {
          if (response.code === 0) {
            this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listReserveConfirms(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listReserveConfirms(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.do-dining-container {
  padding: 20px;
  .vc-reserve-active {
    border: 1px solid #409EFF;
    background-color: #f5f7fa;
  }
  .margin-sm {
    margin: 10px 0;
  }
  .margin-top {
    margin-top: 20px;
  }
  .margin-right-xs {
    margin-right: 5px;
  }
  .margin-bottom-xs {
    margin-bottom: 5px;
  }
  .padding-xs {
    padding: 5px;
  }
  .border-radius {
    border-radius: 4px;
  }
  .vc-border {
    border: 1px solid #ebeef5;
  }
  .text-center {
    text-align: center;
  }
  .flex {
    display: flex;
  }
  .justify-start {
    justify-content: flex-start;
  }
  .flex-wrap {
    flex-wrap: wrap;
  }
  .bg-white {
    background-color: #fff;
    padding: 10px;
    border-radius: 4px;
  }
  .cursor-pointer {
    cursor: pointer;
  }
}
</style>