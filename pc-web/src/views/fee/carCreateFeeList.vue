<template>
  <div class="car-create-fee-container">
    <div class="main-card">
      <el-card v-if="carCreateFeeInfo.cars.length === 0" class="empty-page">
        <i class="el-icon-document"></i>
        <span>请先添加车辆</span>
      </el-card>
      <el-row v-else :gutter="20">
        <el-col :span="4" class="left-panel">
          <el-card class="car-list-card">
            <div class="car-list-wrapper">
              <ul class="car-list">
                <li v-for="(item, index) in carCreateFeeInfo.cars" :key="index" class="car-item"
                  :class="{ 'selected': carCreateFeeInfo.carId === item.carId }" @click="swatchCar(item)">
                  {{ item.carNum }}({{ item.num }})--{{ item.ownerName }}
                </li>
              </ul>
            </div>
          </el-card>
        </el-col>

        <el-col :span="20" class="right-panel">
          <el-card>
            <div slot="header" class="card-header">
              <span>{{ carCreateFeeInfo.carNum }}</span>
              <div class="header-tools">
                <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
                  @click="_openFeeImportExcel">
                  <i class="el-icon-download"></i>
                  {{ $t('carCreateFee.customTemplate') }}
                </el-button>
                <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
                  @click="_openDoCreateRoomFee">
                  <i class="el-icon-plus"></i>
                  {{ $t('carCreateFee.customImport') }}
                </el-button>
              </div>
            </div>

            <div class="search-section">
              <el-row :gutter="20">
                <el-col :span="4">
                  <el-input :placeholder="$t('carCreateFee.enterCarNum')"
                    v-model="carCreateFeeInfo.conditions.carNumLike" @keyup.enter.native="_queryCar"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-select v-model="carCreateFeeInfo.conditions.paId"
                    :placeholder="$t('carCreateFee.selectParkingArea')" style="width:100%">
                    <el-option :label="$t('carCreateFee.selectParkingArea')" value=""></el-option>
                    <el-option v-for="(item, index) in carCreateFeeInfo.parkAreas" :key="index" :label="item.num"
                      :value="item.paId"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-input :placeholder="$t('carCreateFee.enterParkingSpace')"
                    v-model="carCreateFeeInfo.conditions.num" @keyup.enter.native="_queryCar"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-input :placeholder="$t('carCreateFee.enterOwnerName')"
                    v-model="carCreateFeeInfo.conditions.ownerName" @keyup.enter.native="_queryCar"></el-input>
                </el-col>
                <el-col :span="4">
                  <el-select v-model="carCreateFeeInfo.conditions.state"
                    :placeholder="$t('carCreateFee.selectParkingSpaceStatus')" style="width:100%">
                    <el-option :label="$t('carCreateFee.selectParkingSpaceStatus')" value=""></el-option>
                    <el-option v-for="(item, index) in carCreateFeeInfo.states" :key="index" :label="item.name"
                      :value="item.statusCd"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" @click="_queryCar">
                    {{ $t('common.search') }}
                  </el-button>
                </el-col>
              </el-row>
            </div>

            <div class="tab-section">
              <el-tabs v-model="carCreateFeeInfo._currentTab" @tab-click="changeTab(carCreateFeeInfo._currentTab)">
                <el-tab-pane label="车辆费用" name="simplifyCarFee">
                  <simplify-car-fee v-if="carCreateFeeInfo._currentTab === 'simplifyCarFee'"
                    :owner-id="carCreateFeeInfo.ownerId" :car-id="carCreateFeeInfo.carId"
                    :car-num="carCreateFeeInfo.carNum" :owner-name="carCreateFeeInfo.ownerName" ref="simplifyCarFee"/>
                </el-tab-pane>

                <el-tab-pane label="缴费历史" name="simplifyHisFee">
                  <simplify-his-fee v-if="carCreateFeeInfo._currentTab === 'simplifyHisFee'"
                    :owner-id="carCreateFeeInfo.ownerId" :payer-obj-id="carCreateFeeInfo.carId" ref="simplifyHisFee"/>
                </el-tab-pane>

                <el-tab-pane label="预付收款" name="prePayFeeDetail">
                  <pre-pay-fee-detail v-if="carCreateFeeInfo._currentTab === 'prePayFeeDetail'"
                    :payer-obj-id="carCreateFeeInfo.carId" ref="prePayFeeDetail"/>
                </el-tab-pane>

                <el-tab-pane label="设置费用" name="feeDetailCarFeeRule">
                  <fee-detail-car-fee-rule v-if="carCreateFeeInfo._currentTab === 'feeDetailCarFeeRule'"
                    :car-id="carCreateFeeInfo.carId" :car-num="carCreateFeeInfo.carNum"
                    :owner-id="carCreateFeeInfo.ownerId" :owner-name="carCreateFeeInfo.ownerName" ref="feeDetailCarFeeRule"/>
                </el-tab-pane>

                <el-tab-pane v-if="carCreateFeeInfo.ownerId" label="房屋" name="simplifyOwnerRooms">
                  <simplify-owner-rooms v-if="carCreateFeeInfo._currentTab === 'simplifyOwnerRooms'"
                    :owner-id="carCreateFeeInfo.ownerId" ref="simplifyOwnerRooms"/>
                </el-tab-pane>

                <el-tab-pane v-if="carCreateFeeInfo.ownerId" label="业主" name="carDetailOwner">
                  <car-detail-owner v-if="carCreateFeeInfo._currentTab === 'carDetailOwner'"
                    :owner-id="carCreateFeeInfo.ownerId" ref="carDetailOwner"/>
                </el-tab-pane>

                <el-tab-pane v-if="hasPrivilege('502020092373407363')" label="补打收据" name="simplifyFeeReceipt">
                  <simplify-fee-receipt v-if="carCreateFeeInfo._currentTab === 'simplifyFeeReceipt'"
                    :payer-obj-id="carCreateFeeInfo.carId" :owner-id="carCreateFeeInfo.ownerId"
                    :payer-obj-type="'6666'" ref="simplifyFeeReceipt"/>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 模态框组件 -->
    <delete-fee ref="deleteFee" @success="handleSuccess" />
    <edit-fee ref="editFee" @success="handleSuccess" />
    <split-fee ref="splitFee" @success="handleSuccess" />
    <export-car-fee-import-excel ref="exportCarFeeImportExcel" />
    <do-import-create-fee ref="doImportCreateFee" />
    <download-collection-letter-order ref="downloadCollectionLetterOrder" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwnerCars, listParkingAreas } from '@/api/fee/carCreateFeeApi'

// 导入组件
import SimplifyCarFee from '@/components/simplify/simplifyCarFee'
import SimplifyHisFee from '@/components/fee/simplifyHisFee'
import PrePayFeeDetail from '@/components/fee/prePayFeeDetail'
import SimplifyOwnerRooms from '@/components/fee/simplifyOwnerRooms'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import FeeDetailCarFeeRule from '@/components/fee/feeDetailCarFeeRule'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import SplitFee from '@/components/fee/splitFee'

import ExportCarFeeImportExcel from '@/components/fee/exportCarFeeImportExcel'
import DoImportCreateFee from '@/components/fee/doImportCreateFee'
import DownloadCollectionLetterOrder from '@/components/fee/downloadCollectionLetterOrder'

export default {
  name: 'CarCreateFeeList',
  components: {
    SimplifyCarFee,
    SimplifyHisFee,
    PrePayFeeDetail,
    SimplifyOwnerRooms,
    CarDetailOwner,
    SimplifyFeeReceipt,
    FeeDetailCarFeeRule,
    DeleteFee,
    EditFee,
    SplitFee,
    ExportCarFeeImportExcel,
    DoImportCreateFee,
    DownloadCollectionLetterOrder
  },
  data() {
    return {
      communityId: '',
      carCreateFeeInfo: {
        cars: [],
        states: [],
        parkAreas: [],
        carId: '',
        carNum: '',
        ownerId: '',
        ownerName: '',
        total: 0,
        records: 1,
        _currentTab: 'simplifyCarFee',
        conditions: {
          paId: '',
          ownerName: '',
          carNumLike: '',
          num: '',
          state: '',
          carTypeCd: '1001'
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
    await this.loadDictData()
    await this.loadParkAreas()
    await this.listCars(1, 500)
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('owner_car', 'state')
        this.carCreateFeeInfo.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async loadParkAreas() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        const response = await listParkingAreas(params)
        this.carCreateFeeInfo.parkAreas = response.parkingAreas || []
      } catch (error) {
        console.error('获取停车场数据失败:', error)
      }
    },

    async listCars(page, row) {
      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          ...this.carCreateFeeInfo.conditions
        }

        const response = await queryOwnerCars(params)
        this.carCreateFeeInfo.cars = response.data || []
        this.carCreateFeeInfo.total = response.total || 0
        this.carCreateFeeInfo.records = response.records || 0

        if (this.carCreateFeeInfo.cars.length > 0) {
          const lastSelected = localStorage.getItem('lastSelectedCar')
          if (lastSelected) {
            const selectedCar = JSON.parse(lastSelected)
            this.swatchCar(selectedCar)
          } else {
            this.swatchCar(this.carCreateFeeInfo.cars[0])
          }
        }
      } catch (error) {
        console.error('获取车辆列表失败:', error)
        this.$message.error('获取车辆列表失败')
      }
    },

    swatchCar(car) {
      this.carCreateFeeInfo.carId = car.carId
      this.carCreateFeeInfo.carNum = car.carNum
      this.carCreateFeeInfo.ownerId = car.ownerId
      this.carCreateFeeInfo.ownerName = car.ownerName
      this.changeTab(this.carCreateFeeInfo._currentTab)
      localStorage.setItem('lastSelectedCar', JSON.stringify(car))
    },

    changeTab(tabName) {
      this.carCreateFeeInfo._currentTab = tabName
      setTimeout(() => {
        if (this.$refs[tabName]) {
          this.$refs[tabName].open({
            carId: this.carCreateFeeInfo.carId,
            carNum: this.carCreateFeeInfo.carNum,
            ownerId: this.carCreateFeeInfo.ownerId,
            ownerName: this.carCreateFeeInfo.ownerName,
            payerObjId: this.carCreateFeeInfo.carId,
            payerObjType: '6666'
          })
        }
      }, 500)
    },

    async _queryCar() {
      await this.listCars(1, 500)
    },

    _openFeeImportExcel() {
      this.$refs.exportCarFeeImportExcel.open()
    },

    _openDoCreateRoomFee() {
      this.$refs.doImportCreateFee.open()
    },

    handleSuccess() {
      // 处理成功回调
      this.listCars(1, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.car-create-fee-container {
  padding: 20px;

  .main-card {
    min-height: 800px;
  }

  .empty-page {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 800px;
    color: #909399;
    font-size: 16px;

    i {
      font-size: 64px;
      margin-bottom: 20px;
    }
  }

  .left-panel {
    .car-list-card {
      height: 700px;

      .car-list-wrapper {
        height: 650px;
        overflow-y: auto;

        .car-list {
          list-style: none;
          padding: 0;
          margin: 0;

          .car-item {
            padding: 12px;
            margin-bottom: 8px;
            border: 1px solid #e6e6e6;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s;
            text-align: center;

            &:hover {
              background-color: #f5f7fa;
              border-color: #409eff;
            }

            &.selected {
              background-color: #ecf5ff;
              border-color: #409eff;
              color: #409eff;
              font-weight: bold;
            }
          }
        }
      }
    }
  }

  .right-panel {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        .el-button {
          margin-left: 10px;
        }
      }
    }

    .search-section {
      margin-bottom: 20px;

      .el-col {
        margin-bottom: 10px;
      }
    }

    .tab-section {
      margin-top: 20px;
    }
  }
}
</style>