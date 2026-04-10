<template>
  <div class="urgent-purchase-apply-step-container">
    <!-- 申请信息 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('urgentPurchaseApplyStep.applyInfo') }}</span>
      </div>
      <el-form label-width="120px">
        <el-form-item :label="$t('urgentPurchaseApplyStep.warehouse')">
          <el-select v-model="urgentPurchaseApplyStepInfo.shId" @change="_computeFlow" style="width:100%">
            <el-option disabled value="" :label="$t('urgentPurchaseApplyStep.requiredSelectWarehouse')" />
            <el-option v-for="(item, index) in urgentPurchaseApplyStepInfo.storehouses" :key="index" :label="item.shName"
              :value="item.shId" />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('urgentPurchaseApplyStep.contact')">
          <el-input v-model.trim="urgentPurchaseApplyStepInfo.endUserName"
            :placeholder="$t('urgentPurchaseApplyStep.requiredContact')" />
        </el-form-item>

        <el-form-item :label="$t('urgentPurchaseApplyStep.phone')">
          <el-input v-model.trim="urgentPurchaseApplyStepInfo.endUserTel"
            :placeholder="$t('urgentPurchaseApplyStep.requiredPhone')" />
        </el-form-item>

        <el-form-item :label="$t('urgentPurchaseApplyStep.applyDesc')">
          <el-input type="textarea" v-model.trim="urgentPurchaseApplyStepInfo.description"
            :placeholder="$t('urgentPurchaseApplyStep.requiredDesc')" />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 采购物品 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="card-header">
          <span>{{ $t('urgentPurchaseApplyStep.purchaseItems') }}</span>
          <el-button type="primary" size="small" @click="_openSelectResourceStoreInfoModel">
            <i class="el-icon-search"></i>
            {{ $t('urgentPurchaseApplyStep.select') }}
          </el-button>
        </div>
      </div>

      <div class="table-container">
        <table class="custom-table">
          <thead>
            <tr>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.itemType') }}</th>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.nameCode') }}</th>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.spec') }}</th>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.referencePrice') }}</th>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.stock') }}</th>
              <th style="text-align: center; padding: 12px 8px; width: 150px;">{{ $t('urgentPurchaseApplyStep.applyQuantity') }}</th>
              <th style="text-align: center; padding: 12px 8px; width: 150px;">{{ $t('urgentPurchaseApplyStep.purchasePrice') }}</th>
              <th style="text-align: center; padding: 12px 8px; width: 150px;">{{ $t('urgentPurchaseApplyStep.targetWarehouse') }}</th>
              <th style="text-align: center; padding: 12px 8px; width: 150px;">{{ $t('urgentPurchaseApplyStep.remark') }}</th>
              <th style="text-align: center; padding: 12px 8px;">{{ $t('urgentPurchaseApplyStep.operation') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in urgentPurchaseApplyStepInfo.resourceStores" :key="index" class="table-row">
              <td style="text-align: center; padding: 12px 8px;">
                {{ item.parentRstName || '-' }} > {{ item.rstName || '-' }}
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                {{ item.resName }}({{ item.resCode }})
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                {{ item.rssName || '-' }}
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                {{ item.price }}
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                {{ item.stock }}{{ item.unitCodeName }}
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                <el-input-number v-model="item.quantity" :min="1"
                  :placeholder="$t('urgentPurchaseApplyStep.requiredQuantity')" style="width: 80%" />
                {{ item.unitCodeName }}
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                <el-input-number v-model="item.urgentPrice" :min="0.01"
                  :placeholder="$t('urgentPurchaseApplyStep.requiredPrice')" style="width: 100%" />
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                <select v-model="item.shzId" @change="storeHousesChange($event, index)" class="custom-select">
                  <option value="">{{ $t('urgentPurchaseApplyStep.requiredTargetWarehouse') }}</option>
                  <option v-for="(storehouse, storeIndex) in urgentPurchaseApplyStepInfo.storehousesB" :key="storeIndex"
                    :value="storehouse.shId">
                    {{ storehouse.shName }}
                  </option>
                </select>
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                <el-input v-model="item.remark" :placeholder="$t('urgentPurchaseApplyStep.optionalRemark')" />
              </td>
              <td style="text-align: center; padding: 12px 8px;">
                <el-button type="danger" size="mini" @click="_removeSelectResourceStoreItem(item.resId)">
                  <i class="el-icon-delete"></i>
                  {{ $t('urgentPurchaseApplyStep.remove') }}
                </el-button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </el-card>

    <div class="footer-buttons">
      <el-button type="primary" @click="_finishStep">
        {{ $t('urgentPurchaseApplyStep.submit') }}
      </el-button>
    </div>

    <choose-resource-store4 ref="chooseResourceStore4" @setSelectResourceStores="handleSetSelectResourceStores" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStore4 from '@/components/resource/chooseResourceStore4'
import * as api from '@/api/resource/urgentPurchaseApplyStepApi'
import {getUserName,getUserTel} from '@/api/user/userApi'

export default {
  name: 'UrgentPurchaseApplyStepList',
  components: {
    ChooseResourceStore4
  },
  data() {
    return {
      urgentPurchaseApplyStepInfo: {
        resourceStores: [],
        description: '',
        endUserName: '',
        endUserTel: '',
        file: '',
        resOrderType: '',
        staffId: '',
        shId: '',
        staffName: '',
        communityId: '',
        storehouses: [],
        storehousesB: []
      }
    }
  },
  created() {
    this._initData()
  },
  methods: {
    async _initData() {
      this.urgentPurchaseApplyStepInfo.communityId = await getCommunityId()
      this.urgentPurchaseApplyStepInfo.resOrderType = this.$route.query.resOrderType
      this.urgentPurchaseApplyStepInfo.endUserName = getUserName()
      this.urgentPurchaseApplyStepInfo.endUserTel = getUserTel()
      this._listAllocationStorehouse()
      this._listAllocationStorehouseB()
    },
    async _listAllocationStorehouse() {
      try {
        const params = {
          page: 1,
          row: 100,
          allowPurchase: 'ON'
        }
        const { data } = await api.listStorehouses(params)
        this.urgentPurchaseApplyStepInfo.storehouses = data
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    async _listAllocationStorehouseB() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.urgentPurchaseApplyStepInfo.communityId
        }
        const { data } = await api.listStorehouses(params)
        this.urgentPurchaseApplyStepInfo.storehousesB = data
      } catch (error) {
        console.error('获取目标仓库列表失败:', error)
      }
    },
    _openSelectResourceStoreInfoModel() {
      if (!this.urgentPurchaseApplyStepInfo.shId) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.requiredSelectWarehouse'))
        return
      }
      if (!this.urgentPurchaseApplyStepInfo.endUserName) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.requiredContact'))
        return
      }
      if (!this.urgentPurchaseApplyStepInfo.endUserTel) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.requiredPhone'))
        return
      }
      if (!this.urgentPurchaseApplyStepInfo.description) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.requiredDesc'))
        return
      }
      this.$refs.chooseResourceStore4.open({
        shId: this.urgentPurchaseApplyStepInfo.shId
      })
    },
    handleSetSelectResourceStores(resourceStores) {
      let oldList = this.urgentPurchaseApplyStepInfo.resourceStores
      // 过滤重复选择的商品
      resourceStores.forEach((newItem, newIndex) => {
        newItem.rsId = ''
        newItem.shzId = ''
        oldList.forEach((oldItem) => {
          if (oldItem.resId === newItem.resId) {
            delete resourceStores[newIndex]
          }
        })
      })
      // 合并已有商品和新添加商品
      resourceStores.push.apply(resourceStores, oldList)
      // 过滤空元素
      resourceStores = resourceStores.filter((s) => {
        return s['resId']
      })
      this.urgentPurchaseApplyStepInfo.resourceStores = resourceStores
    },
    _resourcesFromSameHouse(resourcesList) {
      if (!resourcesList || resourcesList.length < 2) {
        return true
      }
      let lastHouse = ''
      let sign = true
      for (let i = 0; i < resourcesList.length; i++) {
        if (lastHouse === '') {
          lastHouse = resourcesList[i].shId
          continue
        }
        if (lastHouse === resourcesList[i].shId) {
          continue
        } else {
          sign = false
          break
        }
      }
      return sign
    },
    async _finishStep() {
      const _resourceStores = this.urgentPurchaseApplyStepInfo.resourceStores
      if (!_resourceStores || _resourceStores.length < 1) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.requiredSelectItems'))
        return
      }
      if (!this._resourcesFromSameHouse(_resourceStores)) {
        this.$message.error(this.$t('urgentPurchaseApplyStep.sameWarehouseRequired'))
        return
      }
      let _saveFlag = true
      _resourceStores.forEach(item => {
        if (!item.shzId) {
          this.$message.error(`${item.resName}${this.$t('urgentPurchaseApplyStep.requiredTargetWarehouse')}`)
          _saveFlag = false
          return
        }
        if (!item.quantity || item.quantity <= 0) {
          this.$message.error(`${item.resName}${this.$t('urgentPurchaseApplyStep.requiredQuantity')}`)
          _saveFlag = false
          return
        }
        if (!item.urgentPrice || item.urgentPrice <= 0) {
          this.$message.error(`${item.resName}${this.$t('urgentPurchaseApplyStep.requiredPrice')}`)
          _saveFlag = false
          return
        }
      })
      if (!_saveFlag) {
        return
      }
      try {
        const res = await api.urgentPurchaseApply(this.urgentPurchaseApplyStepInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error(this.$t('common.operationFailed'))
      }
    },
    storeHousesChange(e, i) {
      const shId = e
      this.urgentPurchaseApplyStepInfo.storehouses.forEach((item) => {
        if (item.shId === shId) {
          this.urgentPurchaseApplyStepInfo.resourceStores[i].shzName = item.shName
        }
      })
    },
    _removeSelectResourceStoreItem(resId) {
      this.urgentPurchaseApplyStepInfo.resourceStores.forEach((item, index) => {
        if (item.resId === resId) {
          this.urgentPurchaseApplyStepInfo.resourceStores.splice(index, 1)
        }
      })
    },
    _computeFlow() {
      this.urgentPurchaseApplyStepInfo.resourceStores = []
    }
  }
}
</script>

<style lang="scss" scoped>
.urgent-purchase-apply-step-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .footer-buttons {
    text-align: right;
    margin-top: 20px;
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