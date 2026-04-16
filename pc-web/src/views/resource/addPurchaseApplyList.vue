<template>
  <div class="add-purchase-apply-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addPurchaseApply.purchaser') }}</span>
        <div class="card-tools">
          <el-button type="primary" size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <div class="card-content">
        <el-form label-width="120px">
          <el-form-item :label="$t('addPurchaseApply.warehouse')">
            <el-select v-model="addPurchaseApplyInfo.shId" @change="_computeFlow" style="width:100%">
              <el-option disabled value="" :label="$t('addPurchaseApply.requiredSelectWarehouse')"></el-option>
              <el-option v-for="(item, index) in addPurchaseApplyInfo.storehouses" :key="index" :label="item.shName"
                :value="item.shId">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('addPurchaseApply.contact')">
            <el-input :placeholder="$t('addPurchaseApply.requiredContact')"
              v-model="addPurchaseApplyInfo.endUserName"></el-input>
          </el-form-item>

          <el-form-item :label="$t('addPurchaseApply.phone')">
            <el-input :placeholder="$t('addPurchaseApply.requiredPhone')"
              v-model="addPurchaseApplyInfo.endUserTel"></el-input>
          </el-form-item>

          <el-form-item :label="$t('addPurchaseApply.applyDesc')">
            <el-input type="textarea" :placeholder="$t('addPurchaseApply.requiredDesc')"
              v-model="addPurchaseApplyInfo.description"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addPurchaseApply.purchaseItems') }}</span>
        <div class="card-tools">
          <el-button type="primary" size="small" style="margin-right:10px;" @click="_openSelectResourceStoreInfoModel">
            <i class="el-icon-plus"></i>
            {{ $t('addPurchaseApply.select') }}
          </el-button>
        </div>
      </div>
      <div class="card-content">
        <div class="table-container">
          <table class="custom-table">
            <thead>
              <tr>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.itemType') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.itemNameCode') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.itemSpec') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.price') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.itemStock') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.applyQuantity') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.remark') }}</th>
                <th style="text-align: center; padding: 12px 8px;">{{ $t('addPurchaseApply.operation') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in addPurchaseApplyInfo.resourceStores" :key="index" class="table-row">
                <td style="text-align: center; padding: 12px 8px;">
                  {{ item.parentRstName ? item.parentRstName : '-' }} >
                  {{ item.rstName ? item.rstName : '-' }}
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  {{ item.resName }}({{ item.resCode }})
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  {{ item.rssName ? item.rssName : '-' }}
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  <select v-model="item.timesId" @change="_changeTimesId($event, index)" class="custom-select">
                    <option value="">{{ $t('addPurchaseApply.selectPrice') }}</option>
                    <option v-for="time in item.times" :key="time.timesId" :value="time.timesId">
                      {{ time.price }}
                    </option>
                  </select>
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  {{ _getTimesStock(item) }}{{ item.unitCodeName }}
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  <el-input v-model="item.quantity" type="number" :placeholder="$t('addPurchaseApply.requiredQuantity')"
                    style="width:70%;margin:auto"></el-input>
                  &nbsp;&nbsp;{{ item.unitCodeName }}
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  <el-input v-model="item.remark" type="text"
                    :placeholder="$t('addPurchaseApply.optionalRemark')"></el-input>
                </td>
                <td style="text-align: center; padding: 12px 8px;">
                  <el-button type="danger" size="small" style="margin-right:10px;"
                    @click="_removeSelectResourceStoreItem(item.resId)">
                    {{ $t('addPurchaseApply.remove') }}
                  </el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </el-card>

    <el-row v-if="addPurchaseApplyInfo.audit.assignee == '-2'">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('addPurchaseApply.approver') }}</span>
          </div>
          <div class="card-content">
            <el-form label-width="120px">
              <el-form-item :label="$t('addPurchaseApply.approver')">
                <el-col :span="18">
                  <el-input type="text" :placeholder="$t('addPurchaseApply.requiredApprover')" disabled
                    v-model="addPurchaseApplyInfo.audit.staffName"></el-input>
                </el-col>
                <el-col :span="6">
                  <el-button type="primary" @click="chooseStaff">
                    <i class="el-icon-search"></i>
                    {{ $t('common.select') }}
                  </el-button>
                </el-col>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24" style="text-align:right">
        <el-button type="primary" style="margin-left:10px;" @click="_applyPurchaseSummit">
          {{ $t('addPurchaseApply.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <choose-resource-store2 ref="chooseResourceStore2"
      @setSelectResourceStores="handleSetSelectResourceStores"></choose-resource-store2>

    <select-staff ref="selectStaff" @selectStaff="handleStaffChange"></select-staff>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStore2 from '@/components/resource/chooseResourceStore2'
import SelectStaff from '@/components/staff/SelectStaff'
import { getUserName } from '@/api/user/userApi'
import {
  listStorehouses,
  listResourceSuppliers,
  queryFirstAuditStaff,
  purchaseApply
} from '@/api/resource/addPurchaseApplyApi'
import { getUserTel } from '../../api/user/userApi'

export default {
  name: 'AddPurchaseApply',
  components: {
    ChooseResourceStore2,
    SelectStaff
  },
  data() {
    return {
      addPurchaseApplyInfo: {
        resourceStores: [],
        resourceSuppliers: [],
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        description: '',
        endUserName: '',
        endUserTel: '',
        file: '',
        resOrderType: '10000',
        staffId: '',
        staffName: '',
        communityId: '',
        shId: '',
        storehouses: [],
        flowId: '',
        purchaseSwitch: ''
      }
    }
  },
  created() {
    this._reset()
    this.communityId = getCommunityId()
    this.addPurchaseApplyInfo.communityId = this.communityId
    this.addPurchaseApplyInfo.endUserName = getUserName()
    this.addPurchaseApplyInfo.endUserTel = getUserTel()
    this.addPurchaseApplyInfo.purchaseSwitch = this.$route.query.purchaseSwitch
    this._loadResourceSuppliers()
    this._listPurchaseStorehouses()
  },
  methods: {
    _reset() {
      this.addPurchaseApplyInfo = {
        resourceStores: [],
        resourceSuppliers: [],
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        description: '',
        endUserName: '',
        endUserTel: '',
        file: '',
        resOrderType: '10000',
        staffId: '',
        staffName: '',
        communityId: this.communityId,
        shId: '',
        storehouses: [],
        flowId: ''
      }
    },
    _resourcesFromSameHouse(resourcesList) {
      if (!resourcesList || resourcesList.length < 2) {
        return true
      }
      let lastHouse = ''
      let sign = true
      for (let i = 0; i < resourcesList.length; i++) {
        if (lastHouse == '') {
          lastHouse = resourcesList[i].shId
          continue
        }
        if (lastHouse == resourcesList[i].shId) {
          continue
        } else {
          sign = false
          break
        }
      }
      return sign
    },
    async _applyPurchaseSummit() {
      const _resourceStores = this.addPurchaseApplyInfo.resourceStores
      if (!_resourceStores || _resourceStores.length < 1) {
        this.$message.error(this.$t('addPurchaseApply.noItemsSelected'))
        return
      }
      if (!this._resourcesFromSameHouse(_resourceStores)) {
        this.$message.error(this.$t('addPurchaseApply.sameWarehouseRequired'))
        return
      }
      let isFlag = true
      let _quantity = true
      this.addPurchaseApplyInfo.resourceStores.forEach(item => {
        if (item.timesId == null || item.timesId == '' || item.timesId == undefined) {
          isFlag = false
        }
        if (item.quantity == null || item.quantity == '' || item.quantity == undefined) {
          _quantity = false
        }
      })
      if (!isFlag) {
        this.$message.error(this.$t('addPurchaseApply.selectPriceRequired'))
        return
      }
      if (!_quantity) {
        this.$message.error(this.$t('addPurchaseApply.quantityRequired'))
        return
      }

      try {
        const res = await purchaseApply(this.addPurchaseApplyInfo)
        if (res.code === 0) {
          this.$router.go(-1)
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(error)
      }
    },
    async _loadResourceSuppliers() {
      try {
        const res = await listResourceSuppliers({ page: 1, row: 50 })
        this.addPurchaseApplyInfo.resourceSuppliers = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _openSelectResourceStoreInfoModel() {
      const _shId = this.addPurchaseApplyInfo.shId
      const endUserName = this.addPurchaseApplyInfo.endUserName
      const endUserTel = this.addPurchaseApplyInfo.endUserTel
      const description = this.addPurchaseApplyInfo.description
      if (!_shId) {
        this.$message.error(this.$t('addPurchaseApply.selectWarehouseRequired'))
        return
      }
      if (!endUserName) {
        this.$message.error(this.$t('addPurchaseApply.contactRequired'))
        return
      }
      if (!endUserTel) {
        this.$message.error(this.$t('addPurchaseApply.phoneRequired'))
        return
      }
      if (!description) {
        this.$message.error(this.$t('addPurchaseApply.descRequired'))
        return
      }
      this.$refs.chooseResourceStore2.open({ shId: _shId })
    },
    _removeSelectResourceStoreItem(resId) {
      this.addPurchaseApplyInfo.resourceStores.forEach((item, index) => {
        if (item.resId == resId) {
          this.addPurchaseApplyInfo.resourceStores.splice(index, 1)
        }
      })
    },
    _changeTimesId(e, index) {
      const timeId = e
      const times = this.addPurchaseApplyInfo.resourceStores[index].times
      times.forEach((item) => {
        if (item.timesId == timeId) {
          this.addPurchaseApplyInfo.resourceStores[index].selectedStock = item.stock
        }
      })
      // 强制更新视图
      this.$nextTick(() => {
        this.$forceUpdate()
      })
    },
    _getTimesStock(_resourceStore) {
      if (!_resourceStore.timesId) {
        return "-"
      }
      let _stock = 0
      _resourceStore.times.forEach(_item => {
        if (_item.timesId == _resourceStore.timesId) {
          _stock = _item.stock
        }
      })
      // if (!_resourceStore.quantity) {
      //   _resourceStore.quantity = ''
      // }
      return _stock
    },
    async _loadStaffOrg(_flowId) {
      try {
        const res = await queryFirstAuditStaff({
          communityId: this.communityId,
          flowId: _flowId
        })
        if (res.code != 0) return
        const _data = res.data[0]
        Object.assign(this.addPurchaseApplyInfo.audit, _data)
        if (!_data.assignee.startsWith('-')) {
          this.addPurchaseApplyInfo.audit.staffId = this.addPurchaseApplyInfo.audit.assignee
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.addPurchaseApplyInfo.audit)
    },
    async _listPurchaseStorehouses() {
      try {
        const res = await listStorehouses({
          page: 1,
          row: 100,
          communityId: "",
          allowPurchase: 'ON'
        })
        this.addPurchaseApplyInfo.storehouses = res.data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _computeFlow() {
      this.addPurchaseApplyInfo.resourceStores = []

      const _storehouses = this.addPurchaseApplyInfo.storehouses
      let _flowId = ""
      _storehouses.forEach(item => {
        if (this.addPurchaseApplyInfo.shId == item.shId) {
          _flowId = item.purchaseFlowId
        }
      })
      this.addPurchaseApplyInfo.flowId = _flowId
      if (!_flowId) return
      this._loadStaffOrg(_flowId)
    },
    handleSetSelectResourceStores(resourceStores) {
      const oldList = this.addPurchaseApplyInfo.resourceStores
      resourceStores.forEach((newItem, newIndex) => {
        newItem.rsId = ''
        newItem.timesId = ''
        if (newItem.times && newItem.times.length > 0) {
          newItem.timesId = newItem.times[0].timesId
        }
        oldList.forEach((oldItem) => {
          if (oldItem.resId == newItem.resId && newItem.times && newItem.times.length < 2) {
            delete resourceStores[newIndex]
          }
        })
      })
      resourceStores.push.apply(resourceStores, oldList)
      resourceStores = resourceStores.filter((s) => {
        return s['resId']
      })
      this.addPurchaseApplyInfo.resourceStores = resourceStores
    },
    handleStaffChange(staff) {
      this.addPurchaseApplyInfo.audit.staffId = staff.staffId
      this.addPurchaseApplyInfo.audit.staffName = staff.staffName
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="scss" scoped>
.add-purchase-apply-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-content {
      padding: 20px;
    }
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .table-container {
    overflow-x: auto;
  }

  .custom-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #EBEEF5;
    
    th, td {
      border: 1px solid #EBEEF5;
      background-color: #fff;
    }
    
    th {
      background-color: #fafafa;
      font-weight: 500;
      color: #606266;
      font-size: 14px;
    }
    
    .table-row {
      &:hover {
        background-color: #f5f7fa;
      }
      
      &:nth-child(even) {
        background-color: #fafafa;
      }
    }
  }

  // 自定义 select 样式，模拟 Element UI 的外观
  .custom-select {
    width: 100%;
    height: 32px;
    line-height: 32px;
    padding: 0 12px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background-color: #fff;
    color: #606266;
    font-size: 14px;
    outline: none;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    cursor: pointer;
    
    &:hover {
      border-color: #c0c4cc;
    }
    
    &:focus {
      border-color: #409eff;
      box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    }
    
    // 自定义下拉箭头
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='m6 8 4 4 4-4'/%3e%3c/svg%3e");
    background-position: right 8px center;
    background-repeat: no-repeat;
    background-size: 16px;
    padding-right: 32px;
    
    // 移除默认样式
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    
    // 兼容性处理
    &::-ms-expand {
      display: none;
    }
    
    // option 样式
    option {
      padding: 8px 12px;
      background-color: #fff;
      color: #606266;
      font-size: 14px;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      &:checked {
        background-color: #409eff;
        color: #fff;
      }
    }
    
    // 禁用状态
    &:disabled {
      background-color: #f5f7fa;
      border-color: #e4e7ed;
      color: #c0c4cc;
      cursor: not-allowed;
      
      &:hover {
        border-color: #e4e7ed;
      }
    }
  }
}
</style>