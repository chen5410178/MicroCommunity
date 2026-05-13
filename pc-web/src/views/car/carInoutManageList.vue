<template>
  <div class="animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3" class="padding-r-0">
        <div class="border-radius">
          <div class="margin-xs-r treeview">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in parkingAreas" :key="index" class="list-group-item node-orgTree"
                :class="{ 'vc-node-selected': conditions.paNum === item.num }" @click="swatchParkingArea(item)">
                {{ item.num }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>

      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class=" flex justify-between">
            <div>{{ $t('carInoutManage.searchCondition') }}</div>
          </div>
          <div class="card-body">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-input v-model="conditions.carNum" :placeholder="$t('carInoutManage.carNumRequired')" clearable />
              </el-col>
              <el-col :span="4">
                <el-select v-model="conditions.state" :placeholder="$t('carInoutManage.selectCarStatus')"
                  style="width:100%">
                  <el-option :label="$t('carInoutManage.selectCarStatus')" value="" />
                  <el-option :label="$t('carInoutManage.inStatus')" value="100300" />
                  <el-option :label="$t('carInoutManage.payCompleted')" value="100400" />
                  <el-option :label="$t('carInoutManage.outStatus')" value="100500" />
                  <el-option :label="$t('carInoutManage.payTimeout')" value="100600" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-select v-model="conditions.carType" :placeholder="$t('carInoutManage.selectCarType')"
                  style="width:100%">
                  <el-option :label="$t('carInoutManage.selectCarType')" value="" />
                  <el-option :label="$t('carInoutManage.temporaryCar')" value="1003" />
                  <el-option :label="$t('carInoutManage.saleCar')" value="S" />
                  <el-option :label="$t('carInoutManage.monthlyCar')" value="1001" />
                  <el-option :label="$t('carInoutManage.internalCar')" value="I" />
                  <el-option :label="$t('carInoutManage.freeCar')" value="NM" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="conditions.startTime" type="datetime"
                  :placeholder="$t('carInoutManage.startTimePlaceholder')" style="width:100%" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="conditions.endTime" type="datetime"
                  :placeholder="$t('carInoutManage.endTimePlaceholder')" style="width:100%" />
              </el-col>
              <el-col :span="4">
                <el-button type="primary" size="small" @click="queryCarInoutMethod">
                  {{ $t('carInoutManage.query') }}
                </el-button>
                <el-button size="small" @click="resetCarInoutMethod">
                  {{ $t('carInoutManage.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card" style="margin-top:20px">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('carInoutManage.carInoutRecord') }}</div>
          </div>
          <div class="card-body">
            <el-table :data="carInouts" border style="width:100%">
              <el-table-column :label="$t('carInoutManage.inPhoto')" align="center" width="100">
                <template slot-scope="scope">
                  <el-image style="width:60px;height:60px;cursor:pointer;" :src="scope.row.photoJpg || '/img/noPhoto.jpg'"
                    :preview-src-list="[scope.row.photoJpg]" fit="cover" />
                </template>
              </el-table-column>
              <el-table-column prop="inoutId" :label="$t('carInoutManage.inoutId')" align="center" />
              <el-table-column :label="$t('carInoutManage.carStatus')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.carInout === '3306' ? $t('carInoutManage.inStatus') : $t('carInoutManage.outStatus') }}
                  ({{ scope.row.stateName }})
                </template>
              </el-table-column>
              <el-table-column prop="carNum" :label="$t('carInoutManage.carNum')" align="center" />
              <el-table-column prop="paNum" :label="$t('carInoutManage.parkingArea')" align="center" />
              <!-- <el-table-column prop="feeName" :label="$t('carInoutManage.feeRule')" align="center" /> -->
              <el-table-column prop="carTypeName" :label="$t('carInoutManage.carType')" align="center" />
              <el-table-column prop="inTime" :label="$t('carInoutManage.inTime')" align="center" />
              <el-table-column :label="$t('carInoutManage.outTime')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.carInout === '3307' ? scope.row.openTime : '-' }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('carInoutManage.parkingTime')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.hours }}{{ $t('carInoutManage.hour') }}{{ scope.row.min }}{{ $t('carInoutManage.minute') }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('carInoutManage.chargeAmount')" align="center">
                <template slot-scope="scope">
                  <span v-if="scope.row.carType !== 'T'">-</span>
                  <span v-else>
                    {{ scope.row.payCharge }}
                    <span v-if="['100300', '100400', '100600'].includes(scope.row.inState)">
                      (<a href="javascript:void(0)" @click="carInoutTempCarPayFee(scope.row)">
                        {{ $t('carInoutManage.out') }}
                      </a>)
                    </span>
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="remark" :label="$t('carInoutManage.remark')" align="center" />
            </el-table>

            <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              layout="total, sizes, prev, pager, next, jumper" :total="page.total" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listParkingAreas, listCarInouts } from '@/api/car/carInoutManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarInoutManageList',
  data() {
    return {
      parkingAreas: [],
      carInouts: [],
      conditions: {
        state: '',
        carNum: '',
        carType: '',
        startTime: '',
        endTime: '',
        paNum: '',
        iotApiCode: 'listCarInoutDetailBmoImpl'
      },
      page: {
        current: 1,
        size: 10,
        total: 0,
        records: 0
      }
    }
  },
  created() {
    this.loadParkingArea()
  },
  methods: {
    async loadParkingArea() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const res = await listParkingAreas(params)
        this.parkingAreas = res.parkingAreas || []
        if (this.parkingAreas.length > 0) {
          this.swatchParkingArea(this.parkingAreas[0])
        }
      } catch (error) {
        console.error('加载停车场区域失败:', error)
      }
    },
    swatchParkingArea(parkingArea) {
      this.conditions.paNum = parkingArea.num
      this.listCarInouts()
    },
    async listCarInouts() {
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: getCommunityId()
        }

        // 处理车牌号空格
        if (params.carNum) params.carNum = params.carNum.trim()

        const res = await listCarInouts(params)
        this.carInouts = res.data || []
        this.page.total = res.total || 0
        this.page.records = res.records || 0
      } catch (error) {
        console.error('加载进出场记录失败:', error)
      }
    },
    queryCarInoutMethod() {
      this.page.current = 1
      this.listCarInouts()
    },
    resetCarInoutMethod() {
      this.conditions = {
        state: '',
        carNum: '',
        carType: '',
        startTime: '',
        endTime: '',
        paNum: this.conditions.paNum, // 保留当前停车场
        iotApiCode: 'listCarInoutDetailBmoImpl'
      }
      this.listCarInouts()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listCarInouts()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listCarInouts()
    },
    carInoutTempCarPayFee(row) {
      // 出场支付逻辑
      console.log('出场支付:', row)
    }
  }
}
</script>

<style scoped>
.animated {
  padding: 15px;
}

.border-radius {
  border-radius: 4px;
  overflow: hidden;
}

.margin-xs-r {
  margin-right: 5px;
}

.list-group {
  padding-left: 0;
  margin-bottom: 0;
}

.list-group-item {
  position: relative;
  display: block;
  padding: 10px 15px;
  margin-bottom: -1px;
  background-color: #fff;
  border: 1px solid #ddd;
  cursor: pointer;
}

.list-group-item:hover {
  background-color: #f5f5f5;
}

.vc-node-selected {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

.card-body {
  padding: 20px;
}

.el-col {
  margin-bottom: 15px;
}
</style>